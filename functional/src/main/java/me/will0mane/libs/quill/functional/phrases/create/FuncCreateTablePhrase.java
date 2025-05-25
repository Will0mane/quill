package me.will0mane.libs.quill.functional.phrases.create;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BasePhrase;
import me.will0mane.libs.quill.model.ColumnOption;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.create.Column;
import me.will0mane.libs.quill.phrases.create.CreateTablePhrase;

public class FuncCreateTablePhrase extends BasePhrase implements CreateTablePhrase {

    public FuncCreateTablePhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public CreateTablePhrase name(String name) {
        writeVerb(LiteralVerb.of(name));
        return this;
    }

    @Override
    public CreateTablePhrase columns(Column... columns) {
        writeVerb(StandardVerbs.COMPLEX_OPEN_PARAM);

        boolean first = true;
        for (Column column : columns) {
            if(first) first = false;
            else writeVerb(StandardVerbs.COMPLEX_LISTING);

            writeVerb(LiteralVerb.of(column.name()));
            writeVerb(LiteralVerb.of(column.type()));

            for (ColumnOption option : column.options()) {
                writeVerb(option.getVerb());
            }
        }

        writeVerb(StandardVerbs.COMPLEX_CLOSE_PARAM);
        return this;
    }

    @Override
    public CreateTablePhrase ifNotExists() {
        writeVerb(StandardVerbs.IF_NOT_EXISTS);
        return this;
    }
}
