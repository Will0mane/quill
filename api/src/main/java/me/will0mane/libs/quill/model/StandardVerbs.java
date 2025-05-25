package me.will0mane.libs.quill.model;

public enum StandardVerbs implements Verb {

    SELECT("select"),
    INSERT("insert"),
    INSERT_INTO("insert into"),
    ON_CONFLICT_UPDATE("on duplicate key update"),
    CREATE("create"),
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
    UNKNOWN("?"),
    ASTERISK("*"),
    TABLE("table"),
    DATABASE("database"),
    IF_EXISTS("if exists"),
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
    NOT_LIKE("not like")
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
