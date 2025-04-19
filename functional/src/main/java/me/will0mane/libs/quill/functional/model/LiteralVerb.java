package me.will0mane.libs.quill.functional.model;

import me.will0mane.libs.quill.model.Verb;

public class LiteralVerb implements Verb {

    private final String v;
    private final boolean appendSpace;

    private LiteralVerb(String v, boolean appendSpace) {
        this.v = v;
        this.appendSpace = appendSpace;
    }

    public static LiteralVerb of(String literal) {
        return new LiteralVerb(literal, true);
    }

    public static LiteralVerb of(String literal, boolean appendSpace) {
        return new LiteralVerb(literal, appendSpace);
    }

    @Override
    public boolean appendSpace() {
        return appendSpace;
    }

    @Override
    public String toSQL() {
        return v;
    }
}
