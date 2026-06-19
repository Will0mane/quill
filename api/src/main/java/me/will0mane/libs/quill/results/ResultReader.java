package me.will0mane.libs.quill.results;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultReader extends AutoCloseable {

    boolean next() throws SQLException;

    boolean isSuccess();

    /**
     * Affected row count for UPDATE queries, separate from {@link #isSuccess()}.
     * An update can legitimately affect 0 rows (e.g. an upsert that matched but
     * changed nothing) without that being a failure. Returns -1 when not applicable.
     */
    default int updateCount() {
        return -1;
    }

    <T> T get(int index) throws SQLException;

    <T> T get(String column) throws SQLException;

    void add(int space, ResultSet set);

    <T> T get(int space, int index) throws SQLException;

    <T> T get(int space, String column) throws SQLException;

    default String getString(String column) throws SQLException {
        return get(column);
    }

    default Boolean getBoolean(String column) throws SQLException {
        return get(column);
    }

    default Integer getInt(String column) throws SQLException {
        Object value = get(column);
        if (value == null) return null;
        if (value instanceof Number number) return number.intValue();
        return Integer.parseInt(value.toString());
    }

    default Long getLong(String column) throws SQLException {
        Object value = get(column);
        if (value == null) return null;
        if (value instanceof Number number) return number.longValue();
        return Long.parseLong(value.toString());
    }

    default BigDecimal getBigDecimal(String column) throws SQLException {
        Object value = get(column);
        if (value == null) return null;
        if (value instanceof BigDecimal decimal) return decimal;
        return new BigDecimal(value.toString());
    }

    default BigInteger getBigInteger(String column) throws SQLException {
        Object value = get(column);
        if (value == null) return null;
        if (value instanceof BigInteger integer) return integer;
        if (value instanceof Number number) return BigInteger.valueOf(number.longValue());
        return new BigInteger(value.toString());
    }

    default <T> T getOrDefault(String column, T fallback) throws SQLException {
        T value = get(column);
        return value != null ? value : fallback;
    }

    default String getString(String column, String fallback) throws SQLException {
        return getOrDefault(column, fallback);
    }

    default BigDecimal getBigDecimal(String column, BigDecimal fallback) throws SQLException {
        BigDecimal value = getBigDecimal(column);
        return value != null ? value : fallback;
    }

}
