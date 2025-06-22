package me.will0mane.libs.quill.phrases.insert;

import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.Phrase;

public interface InsertPhrase extends Phrase {

    @Override
    default Verb baseVerb() {
        return StandardVerbs.INSERT_INTO;
    }

    @Override
    default QueryMethod method() {
        return QueryMethod.UPDATE;
    }

    InsertPhrase into(String table);

    InsertPhrase columns(String... columns);

    InsertPhrase row(Object... values);

    default InsertPhrase values(Object... values) {
        return row(values);
    }

    InsertPhrase onConflictUpdate(String column, Object value);

    InsertPhrase onConflictUpdate(String column, String expression, Object value);
}
