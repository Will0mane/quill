package me.will0mane.libs.quill.functional.driver;

import me.will0mane.libs.quill.results.ResultConstants;
import me.will0mane.libs.quill.results.ResultReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class AbstractReader implements ResultReader {

    private final Map<Integer, ResultSet> spaces = new HashMap<>();
    private Statement managedStatement = null;
    private Connection managedConnection = null;

    protected AbstractReader() {
    }

    void withResources(Statement statement, Connection connection) {
        this.managedStatement = statement;
        this.managedConnection = connection;
    }

    private ResultSet at(int space) {
        ResultSet rs = spaces.get(space);
        if (rs == null) {
            throw new IllegalStateException("No ResultSet available for space " + space);
        }
        return rs;
    }

    @Override
    public boolean next() throws SQLException {
        return at(ResultConstants.DEFAULT_SPACE).next();
    }

    @Override
    public <T> T get(int index) throws SQLException {
        return get(ResultConstants.DEFAULT_SPACE, index);
    }

    @Override
    public <T> T get(String column) throws SQLException {
        return get(ResultConstants.DEFAULT_SPACE, column);
    }

    @Override
    public void add(int i, ResultSet resultSet) {
        spaces.put(i, resultSet);
    }

    @Override
    public <T> T get(int i, int i1) throws SQLException {
        return (T) at(i).getObject(i1);
    }

    @Override
    public <T> T get(int i, String s) throws SQLException {
        return (T) at(i).getObject(s);
    }

    @Override
    public void close() throws Exception {
        for (Map.Entry<Integer, ResultSet> entry : spaces.entrySet()) {
            entry.getValue().close();
        }
        spaces.clear();
        if (managedStatement != null) {
            try { managedStatement.close(); } catch (Exception ignored) {}
        }
        if (managedConnection != null) {
            try { managedConnection.close(); } catch (Exception ignored) {}
        }
    }
}
