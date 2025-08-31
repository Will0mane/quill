package me.will0mane.libs.quill.model;

import me.will0mane.libs.quill.phrases.create.Column;

import java.util.Map;
import java.util.Set;

public class Plaster {

    private final String name;
    private final Set<String> generated;
    private final Map<String, Column> columns;
    private final Map<String, String> fieldMap;

    public Plaster(String name, Set<String> generated, Map<String, Column> columns, Map<String, String> fieldMap) {
        this.name = name;
        this.generated = generated;
        this.columns = columns;
        this.fieldMap = fieldMap;
    }

    public Set<String> generated() {
        return generated;
    }

    public Map<String, String> fieldMap() {
        return fieldMap;
    }

    public String name() {
        return name;
    }

    public Map<String, Column> columns() {
        return columns;
    }

}
