package me.will0mane.libs.quill.functional.driver;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class StatementBinder {

    private StatementBinder() {
    }

    public static void bind(PreparedStatement statement, int i, Object o) throws SQLException {
        if (o == null) {
            statement.setNull(i, java.sql.Types.NULL);
            return;
        }

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
}
