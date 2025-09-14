package me.will0mane.libs.quill.functional.phrases.create;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BasePhrase;
import me.will0mane.libs.quill.model.ColumnOption;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.create.Column;
import me.will0mane.libs.quill.phrases.create.CreateTablePhrase;

public class FuncCreateTablePhrase extends BasePhrase implements CreateTablePhrase {

    private String[] primaryKeys = null;

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

        int primaryCount = 0;

        boolean first = true;
        for (Column column : columns) {
            if (first) first = false;
            else writeVerb(StandardVerbs.COMPLEX_LISTING);

            writeVerb(LiteralVerb.of(column.name()));
            writeVerb(LiteralVerb.of(column.type()));

            for (ColumnOption option : column.options()) {
                if(option == ColumnOption.PRIMARY_KEY) primaryCount++;

                if(primaryCount > 1) {
                    throw new RuntimeException("To define multiple primary keys, use the primaryKeys method on the createTable before calling columns!");
                }

                writeVerb(option.getVerb());
            }

            if (column.def() != null) {
                writeVerb(StandardVerbs.DEFAULT);
                writeVerb(StandardVerbs.UNKNOWN);
                assignParam(column.def());
            }
        }

        if (primaryKeys != null) {
            writeVerb(StandardVerbs.PRIMARY_KEY);
            writeVerb(StandardVerbs.COMPLEX_OPEN_PARAM);

            boolean f = true;
            for (String primaryKey : primaryKeys) {
                if (f) f = false;
                else writeVerb(StandardVerbs.COMPLEX_LISTING);
                writeVerb(LiteralVerb.of(primaryKey));
            }

            writeVerb(StandardVerbs.COMPLEX_CLOSE_PARAM);
        }

        writeVerb(StandardVerbs.COMPLEX_CLOSE_PARAM);
        return this;
    }

    @Override
    public CreateTablePhrase primaryKeys(String... columns) {
        primaryKeys = columns;
        return this;
    }

    @Override
    public CreateTablePhrase ifNotExists() {
        writeVerb(StandardVerbs.IF_NOT_EXISTS);
        return this;
    }
}
