package me.will0mane.libs.quill.functional.driver;

import me.will0mane.libs.quill.results.ResultConstants;
import me.will0mane.libs.quill.results.ResultReader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class AbstractReader implements ResultReader {

    private final Map<Integer, ResultSet> spaces = new HashMap<>();

    protected AbstractReader() {
    }

    private ResultSet at(int space) {
        return spaces.get(space);
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
    }
}
