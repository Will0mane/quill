package me.will0mane.libs.quill.phrases.create;

import me.will0mane.libs.quill.model.ColumnOption;

public record Column(String name, String type, Object def, ColumnOption[] options) {

    public static Column of(String name, String type, Object def, ColumnOption... options) {
        return new Column(name, type, def, options);
    }

    public static Column of(String name, String type, ColumnOption... options) {
        return of(name, type, null, options);
    }

}
