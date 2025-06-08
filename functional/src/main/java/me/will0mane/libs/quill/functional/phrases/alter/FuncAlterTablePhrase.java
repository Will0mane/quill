package me.will0mane.libs.quill.functional.phrases.alter;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BasePhrase;
import me.will0mane.libs.quill.model.ColumnOption;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.alter.AlterTablePhrase;
import me.will0mane.libs.quill.phrases.create.Column;

public class FuncAlterTablePhrase extends BasePhrase implements AlterTablePhrase {

    public FuncAlterTablePhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public AlterTablePhrase name(String name) {
        writeVerb(LiteralVerb.of(name));
        return this;
    }

    @Override
    public AlterTablePhrase add(Column column) {
        writeVerb(StandardVerbs.ADD);

        writeVerb(LiteralVerb.of(column.name()));
        writeVerb(LiteralVerb.of(column.type()));

        for (ColumnOption option : column.options()) {
            writeVerb(option.getVerb());
        }

        if(column.def() != null) {
            writeVerb(StandardVerbs.DEFAULT);
            writeVerb(StandardVerbs.UNKNOWN);
            assignParam(column.def());
        }
        return this;
    }

    @Override
    public AlterTablePhrase drop(String column) {
        writeVerb(StandardVerbs.DROP);
        writeVerb(LiteralVerb.of(column));
        return this;
    }

    @Override
    public AlterTablePhrase rename(String oldName, String newName) {
        writeVerb(StandardVerbs.RENAME_COLUMN);
        writeVerb(LiteralVerb.of(oldName));
        writeVerb(StandardVerbs.TO);
        writeVerb(LiteralVerb.of(newName));
        return this;
    }

    @Override
    public AlterTablePhrase modify(String column, String newType) {
        writeVerb(StandardVerbs.MODIFY_COLUMN);
        writeVerb(LiteralVerb.of(column));
        writeVerb(LiteralVerb.of(newType));
        return this;
    }
}
