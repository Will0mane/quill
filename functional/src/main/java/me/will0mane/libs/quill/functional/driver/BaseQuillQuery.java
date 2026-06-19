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

    @Override
    public ResultReader execute() {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = driver.connection(database);
            statement = connection.prepareStatement(literal,
                    options != null && options.contains(QueryOption.RETURN_GENERATED) ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
            int i = 0;
            if (params == null) params = java.util.Collections.emptyList();
            for (Object param : params) {
                i++;
                StatementBinder.bind(statement, i, param);
            }

            ResultReader reader;
            switch (method) {
                case RETRIEVE_DATA -> reader = driver.reader(statement.executeQuery());
                case UPDATE -> reader = driver.reader(statement.executeUpdate());
                case NORMAL -> reader = driver.reader(statement.execute());
                default -> throw new IllegalStateException("Unexpected value: " + method);
            }

            if (options != null && options.contains(QueryOption.RETURN_GENERATED)) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    reader.add(ResultConstants.GENERATED_KEYS_SPACE, generatedKeys);
                } else {
                    generatedKeys.close();
                }
            }

            ((AbstractReader) reader).withResources(statement, connection);
            return reader;
        } catch (Throwable e) {
            try { if (statement != null) statement.close(); } catch (Exception ignored) {}
            try { if (connection != null) connection.close(); } catch (Exception ignored) {}
            throw new RuntimeException(e);
        }
    }
}

