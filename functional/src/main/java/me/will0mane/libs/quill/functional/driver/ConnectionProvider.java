package me.will0mane.libs.quill.functional.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;

public interface ConnectionProvider {

    Executor executor();

    Connection connection(String database) throws SQLException;

}
