package me.will0mane.libs.quill.model;

public enum ColumnOption {

    NOT_NULL(StandardVerbs.NOT_NULL),
    UNIQUE(StandardVerbs.UNIQUE),
    NULL(StandardVerbs.NULL),
    KEY(StandardVerbs.KEY),
    PRIMARY_KEY(StandardVerbs.PRIMARY_KEY),
    AUTO_INCREMENT(StandardVerbs.AUTO_INCREMENT),
    ;

    private final StandardVerbs verb;

    ColumnOption(StandardVerbs verb) {
        this.verb = verb;
    }

    public StandardVerbs getVerb() {
        return verb;
    }
}
