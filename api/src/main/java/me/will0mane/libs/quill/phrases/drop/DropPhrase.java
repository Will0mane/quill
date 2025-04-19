package me.will0mane.libs.quill.phrases.drop;

import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.Phrase;

public interface DropPhrase extends Phrase {

    @Override
    default Verb baseVerb() {
        return StandardVerbs.DROP;
    }

    @Override
    default QueryMethod method() {
        return QueryMethod.NORMAL;
    }

    DropPhrase table(String name);

    DropPhrase database(String name);

    DropPhrase ifExists();

}
