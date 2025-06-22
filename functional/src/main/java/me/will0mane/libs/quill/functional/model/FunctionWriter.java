package me.will0mane.libs.quill.functional.model;

import me.will0mane.libs.quill.model.SQLFunction;
import me.will0mane.libs.quill.model.StandardVerbs;

public class FunctionWriter {

    public static void write(SQLFunction function, Scribe scribe) {
        scribe.write(LiteralVerb.of(function.getFunctionName() + "("));

        boolean first = true;
        for (Object param : function.getParams()) {
            if (first) first = false;
            else scribe.write(StandardVerbs.COMPLEX_LISTING);

            scribe.write(StandardVerbs.UNKNOWN);
            scribe.assign(param);
        }
    }

    public static void process(Object param, Scribe scribe) {
        if (param instanceof SQLFunction function) {
            write(function, scribe);
            return;
        }

        scribe.write(StandardVerbs.UNKNOWN);
        scribe.assign(param);
    }

}
