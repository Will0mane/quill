package me.will0mane.libs.quill.phrases.alter;

import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.Phrase;
import me.will0mane.libs.quill.phrases.create.Column;

public interface AlterTablePhrase extends Phrase {

    @Override
    default Verb baseVerb() {
        return StandardVerbs.ALTER_TABLE;
    }

    @Override
    default QueryMethod method() {
        return QueryMethod.NORMAL;
    }

    AlterTablePhrase name(String name);

    default AlterTablePhrase table(String table) {
        return name(table);
    }

    AlterTablePhrase add(Column column);

    default AlterTablePhrase drop(Column column) {
        return drop(column.name());
    }

    AlterTablePhrase drop(String table);

    AlterTablePhrase rename(String oldName, String newName);

    AlterTablePhrase modify(String column, String newType);
}

