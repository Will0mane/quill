package me.will0mane.libs.quill.model;

public final class SQL {

    private SQL() {
    }

    public static SQLRaw raw(String expression) {
        return new SQLRaw(expression);
    }

    public static SQLFunction func(String functionName, Object... params) {
        return new SQLFunction(functionName, params);
    }

    public static SQLNull nullOf(int sqlType) {
        return new SQLNull(sqlType);
    }

}
