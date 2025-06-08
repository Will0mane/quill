package me.will0mane.libs.quill.functional.tables;

import me.will0mane.libs.quill.Quill;
import me.will0mane.libs.quill.exceptions.NotationNotPresentException;
import me.will0mane.libs.quill.model.JSON;
import me.will0mane.libs.quill.model.Table;
import me.will0mane.libs.quill.model.annotations.*;
import me.will0mane.libs.quill.results.Result;
import me.will0mane.libs.quill.results.ResultReader;
import me.will0mane.libs.quill.tables.TableManager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FuncTableManager implements TableManager {

    private final Quill quill;

    public FuncTableManager(Quill quill) {
        this.quill = quill;
    }

    private String checkIdAnnotation(Class<? extends Table> clazz) throws NotationNotPresentException {
        if (!clazz.isAnnotationPresent(Id.class))
            throw new NotationNotPresentException("The Id annotation is not present on the class. (" + clazz.getName() + ")");
        return clazz.getAnnotation(Id.class).value();
    }

    private String typeToString(Class<?> clazz) throws UnsupportedOperationException {
        if (clazz == int.class) {
            return "int";
        }

        if (clazz == long.class) {
            return "bigint";
        }

        if (clazz == boolean.class) {
            return "tinyint";
        }

        if (clazz == short.class) {
            return "smallint";
        }

        if (clazz == String.class) {
            return "varchar";
        }

        if (clazz == float.class) {
            return "float";
        }

        if (clazz == double.class) {
            return "double";
        }

        if (clazz == JSON.class) {
            return "longtext";
        }

        throw new UnsupportedOperationException("Conversion type for " + clazz.getName() + " was not found");
    }

    private boolean isNullable(Field field) {
        return !field.isAnnotationPresent(NotNull.class);
    }

    private KeyType keyTypeFrom(Field field) {
        if (field.isAnnotationPresent(PrimaryKey.class)) return KeyType.PRIMARY_KEY;
        if (field.isAnnotationPresent(Key.class)) return KeyType.KEY;
        return KeyType.NOT_KEY;
    }

    private String defaultFrom(Field field) {
        if (field.isAnnotationPresent(DefaultInt.class))
            return String.valueOf(field.getAnnotation(DefaultInt.class).def());
        if (field.isAnnotationPresent(DefaultBigInt.class))
            return String.valueOf(field.getAnnotation(DefaultBigInt.class).def());
        if (field.isAnnotationPresent(DefaultBoolean.class))
            return String.valueOf(field.getAnnotation(DefaultBoolean.class).def());
        if (field.isAnnotationPresent(DefaultFloat.class))
            return String.valueOf(field.getAnnotation(DefaultFloat.class).def());
        if (field.isAnnotationPresent(DefaultDouble.class))
            return String.valueOf(field.getAnnotation(DefaultDouble.class).def());
        if (field.isAnnotationPresent(DefaultSmallInt.class))
            return String.valueOf(field.getAnnotation(DefaultSmallInt.class).def());
        if (field.isAnnotationPresent(DefaultString.class)) return field.getAnnotation(DefaultString.class).def();

        return null;
    }

    private String extraFrom(Field field) {
        if (field.isAnnotationPresent(AutoIncrement.class)) return "auto_increment";
        return "";
    }

    private TableSchema fromClass(Class<? extends Table> clazz) throws NotationNotPresentException {
        Map<String, ColData> dataMap = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class)) continue;
            String id = field.getAnnotation(Id.class).value();

            String type = typeToString(field.getType());
            boolean nullable = isNullable(field);
            KeyType keyType = keyTypeFrom(field);
            String defaultValue = defaultFrom(field);
            String extra = extraFrom(field);

            dataMap.put(id, new ColData(id, type, nullable, keyType, defaultValue, extra));
        }

        return new TableSchema(dataMap);
    }

    @Override
    public void handle(Table table) {
        try {
            Class<? extends Table> aClass = table.getClass();
            String id = checkIdAnnotation(aClass);

            TableSchema actual = fromClass(aClass);

            quill.driver().async(() -> {
                try {
                    Result send = quill.async().describe().name(id).send();
                    ResultReader reader = send.await().get();

                    TableSchema schema = new TableSchema(reader);
                    if (actual.equals(schema)) return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
