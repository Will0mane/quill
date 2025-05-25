package me.will0mane.libs.quill.phrases.create;

import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.Phrase;

public interface CreateDatabasePhrase extends Phrase {

    @Override
    default Verb baseVerb() {
        return StandardVerbs.CREATE_DATABASE;
    }

    @Override
    default QueryMethod method() {
        return QueryMethod.NORMAL;
    }

    CreateDatabasePhrase name(String name);

    CreateDatabasePhrase ifNotExists();

}
