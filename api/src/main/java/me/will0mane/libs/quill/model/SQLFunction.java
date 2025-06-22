package me.will0mane.libs.quill.model;

public class SQLFunction {

    private final String functionName;
    private final Object[] params;

    public SQLFunction(String functionName, Object... params) {
        this.functionName = functionName;
        this.params = params;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Object[] getParams() {
        return params;
    }

}
