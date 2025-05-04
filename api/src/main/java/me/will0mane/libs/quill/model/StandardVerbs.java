package me.will0mane.libs.quill.model;

public enum StandardVerbs implements Verb {

    SELECT("select"),
    INSERT("insert"),
    INSERT_INTO("insert into"),
    ON_CONFLICT_UPDATE("on duplicate key update"),
    CREATE("create"),
    CREATE_TABLE("create table"),
    CREATE_DATABASE("create database"),
    DROP("drop"),
    DELETE("delete"),
    INTO("into"),
    FROM("from"),
    WHERE("where"),
    UPDATE("update"),
    JOIN("join"),
    AND("and"),
    VALUES("values"),
    OR("or"),
    SET("set"),
    LIMIT("limit"),
    OFFSET("offset"),
    ORDER_BY("order by"),
    DESC("desc"),
    ASC("asc"),
    UNKNOWN("?"),
    ASTERISK("*"),
    TABLE("table"),
    DATABASE("database"),
    IF_EXISTS("if exists"),
    IF_NOT_EXISTS("if not exists"),
    NOT_NULL("not null"),
    NULL("null"),
    PRIMARY_KEY("primary key"),
    AUTO_INCREMENT("auto_increment"),
    COMPLEX_UNKNOWN(false, "?"),
    OPEN_PARAM("("),
    COMPLEX_OPEN_PARAM(false, "("),
    CLOSE_PARAM(")"),
    COMPLEX_CLOSE_PARAM(false, ")"),
    LISTING(","),
    COMPLEX_LISTING(false, ","),
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER(">"),
    LESS("<"),
    GREATER_EQUAL(">="),
    LESS_EQUAL("<="),
    ;

    private final boolean appendSpace;
    private final String verb;

    StandardVerbs(boolean appendSpace, String verb) {
        this.appendSpace = appendSpace;
        this.verb = verb;
    }

    StandardVerbs(String verb) {
        this(true, verb);
    }

    @Override
    public boolean appendSpace() {
        return appendSpace;
    }

    public String get() {
        return verb;
    }

    @Override
    public String toSQL() {
        return get();
    }
}
