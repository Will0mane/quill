package me.will0mane.libs.quill.model;

import java.util.Map;

public class Data {

    private final Map<String, Object> compound;

    public Data(Map<String, Object> compound) {
        this.compound = compound;
    }

    public Map<String, Object> compound() {
        return compound;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) compound.get(key);
    }
}
