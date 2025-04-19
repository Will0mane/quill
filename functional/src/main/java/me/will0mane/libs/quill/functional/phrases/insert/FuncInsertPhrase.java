package me.will0mane.libs.quill.functional.phrases.insert;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BasePhrase;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.insert.InsertPhrase;

public class FuncInsertPhrase extends BasePhrase implements InsertPhrase {

    private boolean wroteValuesHeader = false;
    private boolean wroteOnConflictHeader = false;

    public FuncInsertPhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public InsertPhrase into(String table) {
        writeVerb(LiteralVerb.of(table));
        return this;
    }

    @Override
    public InsertPhrase columns(String... columns) {
        writeVerb(StandardVerbs.COMPLEX_OPEN_PARAM);

        boolean first = true;
        for (String column : columns) {
            if (first) first = false;
            else writeVerb(StandardVerbs.COMPLEX_LISTING);
            writeVerb(LiteralVerb.of(column));
        }

        writeVerb(StandardVerbs.COMPLEX_CLOSE_PARAM);
        return this;
    }

    @Override
    public InsertPhrase row(Object... values) {
        if(!wroteValuesHeader) {
            writeVerb(StandardVerbs.VALUES);
            wroteValuesHeader = true;
        }else {
            writeVerb(StandardVerbs.COMPLEX_LISTING);
        }

        writeVerb(StandardVerbs.COMPLEX_OPEN_PARAM);

        boolean first = true;
        for (Object param : values) {
            if (first) first = false;
            else writeVerb(StandardVerbs.COMPLEX_LISTING);
            writeVerb(StandardVerbs.UNKNOWN);
            assignParam(param);
        }

        writeVerb(StandardVerbs.COMPLEX_CLOSE_PARAM);
        return this;
    }

    @Override
    public InsertPhrase onConflictUpdate(String column, Object value) {
        if(!wroteOnConflictHeader) {
            writeVerb(StandardVerbs.ON_CONFLICT_UPDATE);
            wroteOnConflictHeader = true;
        }else {
            writeVerb(StandardVerbs.LISTING);
        }

        writeVerb(LiteralVerb.of(column));
        writeVerb(StandardVerbs.EQUAL);
        writeVerb(StandardVerbs.UNKNOWN);
        assignParam(value);
        return this;
    }
}
