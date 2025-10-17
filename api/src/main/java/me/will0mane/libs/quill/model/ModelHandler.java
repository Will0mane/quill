package me.will0mane.libs.quill.model;

import me.will0mane.libs.quill.exceptions.NotationNotPresentException;
import me.will0mane.libs.quill.exceptions.QuillException;
import me.will0mane.libs.quill.model.annotations.*;
import me.will0mane.libs.quill.phrases.create.Column;
import me.will0mane.libs.quill.phrases.insert.InsertPhrase;
import me.will0mane.libs.quill.phrases.update.UpdatePhrase;
import me.will0mane.libs.quill.results.ResultConstants;
import me.will0mane.libs.quill.results.ResultReader;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ModelHandler {

    public static final Map<Class<?>, Function<Field, String>> FIELD_TYPE_MAPPER = new HashMap<>();

    static {
        FIELD_TYPE_MAPPER.put(boolean.class, ignored -> "tinyint(1)");
        FIELD_TYPE_MAPPER.put(int.class, field -> "int");
        FIELD_TYPE_MAPPER.put(long.class, field -> "bigint");
        FIELD_TYPE_MAPPER.put(float.class, field -> "float");
        FIELD_TYPE_MAPPER.put(double.class, field -> "double");
        FIELD_TYPE_MAPPER.put(String.class, field -> "text");
        FIELD_TYPE_MAPPER.put(byte.class, field -> "byte");
        FIELD_TYPE_MAPPER.put(short.class, field -> "smallint");
        FIELD_TYPE_MAPPER.put(JSON.class, field -> "json");
        FIELD_TYPE_MAPPER.put(BigInteger.class, field -> "bigint");
    }

    public Plaster loadModel(Model model) throws QuillException, IllegalAccessException {
        Class<? extends Model> aClass = model.getClass();

        Map<String, Column> columnMap = new HashMap<>();
        if (!aClass.isAnnotationPresent(Name.class)) {
            throw new NotationNotPresentException("@Name annotation is not present in " + aClass + ", please add it.");
        }

        Set<String> generatedKeys = new HashSet<>();

        Map<String, String> fieldMap = new HashMap<>();

        String name = aClass.getAnnotation(Name.class).value();
        for (Field declaredField : aClass.getDeclaredFields()) {
            String fieldName = declaredField.isAnnotationPresent(Name.class) ? declaredField.getAnnotation(Name.class).value()
                    : declaredField.getName();

            Function<Field, String> function = FIELD_TYPE_MAPPER.get(declaredField.getType());
            if (function == null) {
                throw new QuillException("We don't have a mapper for java type " + declaredField.getType() + " to SQL. Register it to ModelHandler.FIELD_TYPE_MAPPER.");
            }

            String type = function.apply(declaredField);

            if (declaredField.isAnnotationPresent(Unsigned.class)) {
                type = type + " unsigned";
            }

            Object def = null;
            if (declaredField.isAnnotationPresent(Default.class)) {
                def = declaredField.get(model);
            }

            Set<ColumnOption> options = new HashSet<>();
            if (declaredField.isAnnotationPresent(NotNull.class)) {
                options.add(ColumnOption.NOT_NULL);
            }

            if (declaredField.isAnnotationPresent(Null.class)) {
                options.add(ColumnOption.NULL);
            }

            if (declaredField.isAnnotationPresent(PrimaryKey.class)) {
                options.add(ColumnOption.PRIMARY_KEY);
            }

            if (declaredField.isAnnotationPresent(Key.class)) {
                options.add(ColumnOption.KEY);
            }

            if (declaredField.isAnnotationPresent(AutoIncrement.class)) {
                options.add(ColumnOption.AUTO_INCREMENT);
                generatedKeys.add(fieldName);
            }

            fieldMap.put(fieldName, declaredField.getName());
            Column column = Column.of(fieldName, type, def, options.toArray(new ColumnOption[]{}));
            columnMap.put(fieldName, column);
        }

        return new Plaster(name, generatedKeys, columnMap, fieldMap);
    }

    public void createTableQuery(QuillProperties properties, Plaster plaster) {
        properties.quill().async(properties.database())
                .createTable()
                .ifNotExists()
                .name(plaster.name())
                .columns(plaster.columns().values().toArray(new Column[]{}))
                .sendAndIgnore();
    }

    public CompletableFuture<Data> select(QuillProperties properties, Plaster plaster, RowIdentifier identifier) {
        CompletableFuture<Data> future = new CompletableFuture<>();
        properties.quill().async(properties.database())
                .select().asterisk()
                .from(plaster.name())
                .where().isEqual(identifier.primaryKey(), identifier.value())
                .send().await(reader -> {
                    try {
                        future.complete(query(plaster, reader));
                    } catch (Throwable e) {
						e.printStackTrace();
                        future.complete(null);
                    }
                });
        return future;
    }

    public CompletableFuture<Boolean> delete(QuillProperties properties, Plaster plaster, RowIdentifier identifier) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        properties.quill().async(properties.database())
                .delete()
                .from(plaster.name())
                .where().isEqual(identifier.primaryKey(), identifier.value())
                .send().await(reader -> future.complete(reader.isSuccess()));
        return future;
    }

    public <T> CompletableFuture<T> insert(QuillProperties properties, Plaster plaster, T entity) throws NoSuchFieldException, IllegalAccessException {
        CompletableFuture<T> future = new CompletableFuture<>();
        Class<?> aClass = entity.getClass();

        Map<String, Object> compound = new HashMap<>();
		
		if (entity instanceof Data data) {
			for (Column column : plaster.columns().values()) {
				if (plaster.generated().contains(column.name())) continue;
				Object o = data.get(column.name());
				if (o == null && column.def() != null) {
					o = column.def();
				}
				
				compound.put(column.name(), o);
			}
		} else {
			for (Column value : plaster.columns().values()) {
				if (plaster.generated().contains(value.name())) continue;
				compound.put(value.name(), aClass.getDeclaredField(plaster.fieldMap().get(value.name())).get(entity));
			}
		}

        InsertPhrase values = properties.quill().async(properties.database())
                .insert().into(plaster.name())
                .columns(compound.keySet().toArray(new String[0]))
                .values(compound.values().toArray());

        if (plaster.generated().isEmpty()) {
            values.send().await(reader -> {
                future.complete(entity);
            });
        } else {
            values.send(QueryOption.RETURN_GENERATED).await(reader -> {
                try {
                    int i = 1;
                    for (String s : plaster.generated()) {
                        Object o = reader.get(ResultConstants.GENERATED_KEYS_SPACE, i);
                        Field declaredField = aClass.getDeclaredField(plaster.fieldMap().get(s));

                        if (o instanceof BigInteger bigInteger) {
                            if (declaredField.getType() == long.class) {
                                o = bigInteger.longValue();
                            }
                            if (declaredField.getType() == int.class) {
                                o = bigInteger.intValue();
                            }
                        }

                        declaredField.set(entity, o);

                        i++;
                    }
                    future.complete(entity);
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        }
        return future;
    }

    public CompletableFuture<Boolean> update(QuillProperties properties, Plaster plaster, Object entity, RowIdentifier identifier) throws NoSuchFieldException, IllegalAccessException {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        UpdatePhrase updatePhrase = properties.quill().async(properties.database())
                .update().table(plaster.name());

        if (entity instanceof Data data) {
            for (Column column : plaster.columns().values()) {
                if (plaster.generated().contains(column.name())) continue;
                Object o = data.get(column.name());
                updatePhrase.set(column.name(), o);
            }
        } else {
            Class<?> aClass = entity.getClass();
            for (Column column : plaster.columns().values()) {
                if (plaster.generated().contains(column.name())) continue;
                Object o = aClass.getDeclaredField(plaster.fieldMap().get(column.name())).get(entity);
                updatePhrase.set(column.name(), o);
            }
        }

        updatePhrase.where().isEqual(identifier.primaryKey(), identifier.value());

        updatePhrase.send().await(reader -> future.complete(reader.isSuccess()));
        return future;
    }

    public CompletableFuture<Boolean> updateSpecific(QuillProperties properties, Plaster plaster, Object entity, RowIdentifier identifier, String... fields) throws NoSuchFieldException, IllegalAccessException {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        UpdatePhrase updatePhrase = properties.quill().async(properties.database())
                .update().table(plaster.name());

        boolean minimum = false;

        if (entity instanceof Data data) {
            for (String column : fields) {
                if (plaster.generated().contains(column)) continue;
                Object o = data.compound().get(column);
                updatePhrase.set(column, o);
                minimum = true;
            }
        } else {
            Class<?> aClass = entity.getClass();
            for (String column : fields) {
                if (plaster.generated().contains(column)) continue;
                Object o = aClass.getDeclaredField(plaster.fieldMap().get(column)).get(entity);
                updatePhrase.set(column, o);
                minimum = true;
            }
        }

        if (!minimum) {
            throw new IllegalAccessException("Cannot update with those fields! Either the set is empty or you passed autogenerated columns.");
        }

        updatePhrase.where().isEqual(identifier.primaryKey(), identifier.value());

        updatePhrase.send().await(reader -> future.complete(reader.isSuccess()));
        return future;
    }

    public Data query(Plaster plaster, ResultReader reader) throws SQLException {
        if (!reader.next()) return null;
        Map<String, Object> compound = new HashMap<>();
        for (Column value : plaster.columns().values()) {
            Object o = reader.get(value.name());
            compound.put(value.name(), o);
        }
        return new Data(compound);
    }

}
