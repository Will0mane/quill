package me.will0mane.libs.quill.functional.driver;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionManager {

    Connection connection() throws SQLException;

}
