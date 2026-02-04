package me.will0mane.libs.quill.model;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum StandardVerbs implements Verb {

    UNKNOWN("?"),
    ASTERISK("*"),
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

    SELECT("select"),
    INSERT("insert"),
    INTO("into"),
    INSERT_INTO(INSERT, INTO),
    ALTER("alter"),
    TABLE("table"),
    ALTER_TABLE(ALTER, TABLE),
    RENAME("rename"),
    COLUMN("column"),
    RENAME_COLUMN(RENAME, COLUMN),
    CREATE("create"),
    DATABASE("database"),
    CREATE_TABLE(CREATE, TABLE),
    CREATE_DATABASE(CREATE, DATABASE),
    DESCRIBE("describe"),

    ON_CONFLICT_UPDATE("on duplicate key update"),

    TO("to"),
    DROP("drop"),
    ADD("add"),
    ALL("all"),
    MODIFY("modify"),
    MODIFY_COLUMN(MODIFY, COLUMN),
    DEFAULT("default"),
    DELETE("delete"),
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

    ORDER("order"),
    BY("by"),
    ORDER_BY(ORDER, BY),

    DESC("desc"),
    ASC("asc"),

    IF("if"),
    EXISTS("exists"),
    NOT("not"),

    IF_EXISTS(IF, EXISTS),
    IF_NOT_EXISTS(IF, NOT, EXISTS),

    UNIQUE("unique"),
    UNION("union"),
    NULL("null"),
    INNER("inner"),
    ON("on"),
    PRIMARY("primary"),
    KEY("key"),
    PRIMARY_KEY(PRIMARY, KEY),

    AUTO_INCREMENT("auto_increment"),

    LIKE("like"),
    NOT_LIKE(NOT, LIKE),

    IN("in"),
    IS("is"),
    IS_NOT(IS, NOT),

    NOT_NULL(NOT, NULL),

    BETWEEN("between"),
    AS("as"),
    LEFT("left"),
    RIGHT("right"),
    FULL("full"),
    ;

    private final boolean appendSpace;
    private final String verb;

    StandardVerbs(boolean appendSpace, String verb) {
        this.appendSpace = appendSpace;
        this.verb = verb;
    }

    StandardVerbs(StandardVerbs... verbs) {
        this.appendSpace = true;

        boolean first = true;
        StringBuilder builder = new StringBuilder();
        for (StandardVerbs standardVerbs : verbs) {
            if(first) first = false;
            else builder.append(' ');

            builder.append(standardVerbs.get());
        }
        this.verb = builder.toString();
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
