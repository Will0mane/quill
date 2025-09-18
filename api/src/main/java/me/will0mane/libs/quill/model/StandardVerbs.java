package me.will0mane.libs.quill.model;

public enum StandardVerbs implements Verb {

    SELECT("select"),
    INSERT("insert"),
    INSERT_INTO("insert into"),
    ALTER_TABLE("alter table"),
    RENAME_COLUMN("rename column"),
    TO("to"),
    ON_CONFLICT_UPDATE("on duplicate key update"),
    CREATE("create"),
    CREATE_TABLE("create table"),
    CREATE_DATABASE("create database"),
    DROP("drop"),
    ADD("add"),
    DESCRIBE("describe"),
    MODIFY_COLUMN("modify column"),
    DEFAULT("default"),
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
    UNIQUE("unique"),
    NULL("null"),
    INNER_JOIN("inner join"),
    ON("on"),
    KEY("key"),
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
    LIKE("like"),
    NOT_LIKE("not like"),
    NOT("not"),
    IN("in");

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
