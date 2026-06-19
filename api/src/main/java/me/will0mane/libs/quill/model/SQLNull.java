package me.will0mane.libs.quill.model;

public class SQLNull {

    private final int sqlType;

    public SQLNull(int sqlType) {
        this.sqlType = sqlType;
    }

    public int getSqlType() {
        return sqlType;
    }

}
