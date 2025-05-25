package me.will0mane.libs.quill.functional.tables;

public enum KeyType {

    PRIMARY_KEY("PRI"),
    KEY("UNI"),
    MULTIPLE("MUL"),
    NOT_KEY(""),
    ;

    private final String code;

    private static final KeyType[] VALS = values();

    KeyType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static KeyType fromCode(String code) {
        for (KeyType keyType : VALS) {
            if (keyType.getCode().equals(code)) {
                return keyType;
            }
        }
        return null;
    }
}
