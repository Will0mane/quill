package me.will0mane.libs.quill.results;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultReader extends AutoCloseable {

    boolean next() throws SQLException;

    boolean isSuccess();

    <T> T get(int index) throws SQLException;

    <T> T get(String column) throws SQLException;

    void add(int space, ResultSet set);

    <T> T get(int space, int index) throws SQLException;

    <T> T get(int space, String column) throws SQLException;

}
