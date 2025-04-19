package me.will0mane.libs.quill.functional.phrases.select;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BaseFilterablePhrase;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.select.SelectPhrase;

public class FuncSelectPhrase extends BaseFilterablePhrase implements SelectPhrase {

    public FuncSelectPhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public SelectPhrase columns(String... columns) {
        boolean first = true;

        for (String column : columns) {
            if(first) first = false;
            else writeVerb(StandardVerbs.LISTING);
            writeVerb(LiteralVerb.of(column));
        }

        return this;
    }

    @Override
    public SelectPhrase asterisk() {
        writeVerb(StandardVerbs.ASTERISK);
        return this;
    }

    @Override
    public SelectPhrase from(String table) {
        writeVerb(StandardVerbs.FROM);
        writeVerb(LiteralVerb.of(table));
        return this;
    }
}
