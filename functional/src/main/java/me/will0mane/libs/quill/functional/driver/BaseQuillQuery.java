package me.will0mane.libs.quill.functional.driver;

import me.will0mane.libs.quill.QuillDriver;
import me.will0mane.libs.quill.model.Query;
import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.QueryOption;
import me.will0mane.libs.quill.results.ResultConstants;
import me.will0mane.libs.quill.results.ResultReader;

import java.sql.*;
import java.util.Collection;

public class BaseQuillQuery implements Query {

    private final QuillDriver driver;

    public BaseQuillQuery(QuillDriver driver) {
        this.driver = driver;
    }

    private String database = null;
    private String literal;
    private QueryMethod method;
    private Collection<Object> params;
    private Collection<QueryOption> options;

    @Override
    public void database(String s) {
        this.database = s;
    }

    @Override
    public void literal(String literal) {
        this.literal = literal;
    }

    @Override
    public void params(Collection<Object> params) {
        this.params = params;
    }

    @Override
    public void options(Collection<QueryOption> collection) {
        this.options = collection;
    }

    @Override
    public void method(QueryMethod method) {
        this.method = method;
    }

    private void setParam(PreparedStatement statement, int i, Object o) throws SQLException {
        if (o instanceof String string) {
            statement.setString(i, string);
            return;
        }

        if (o instanceof Integer integer) {
            statement.setInt(i, integer);
            return;
        }

        if (o instanceof Long longInteger) {
            statement.setLong(i, longInteger);
            return;
        }

        if (o instanceof Float floatInteger) {
            statement.setFloat(i, floatInteger);
            return;
        }

        if (o instanceof Double doubleInteger) {
            statement.setDouble(i, doubleInteger);
            return;
        }

        if (o instanceof Boolean booleanInteger) {
            statement.setBoolean(i, booleanInteger);
            return;
        }

        if (o instanceof byte[] bytes) {
            statement.setBytes(i, bytes);
            return;
        }

        statement.setObject(i, o);
    }

    @Override
    public ResultReader execute() {
        try (Connection connection = driver.connection(database); PreparedStatement statement =
                connection.prepareStatement(literal, options.contains(QueryOption.RETURN_GENERATED) ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS)) {
            int i = 0;
            for (Object param : params) {
                i++;
                setParam(statement, i, param);
            }

            ResultReader reader = null;
            switch (method) {
                case RETRIEVE_DATA -> reader = driver.reader(statement.executeQuery());
                case UPDATE -> reader = driver.reader(statement.executeUpdate());
                case NORMAL -> reader = driver.reader(statement.execute());
            }

            if (options.contains(QueryOption.RETURN_GENERATED)) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                reader.add(ResultConstants.GENERATED_KEYS_SPACE, generatedKeys);
            }

            return reader;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

