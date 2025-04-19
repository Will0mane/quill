package me.will0mane.libs.quill.results;

import java.sql.SQLException;

public interface ResultReader {

    boolean next() throws SQLException;

    boolean isSuccess();

    <T> T get(int index) throws SQLException;

    <T> T get(String column) throws SQLException;

}
