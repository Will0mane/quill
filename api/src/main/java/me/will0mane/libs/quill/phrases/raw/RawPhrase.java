package me.will0mane.libs.quill.phrases.raw;

import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.Phrase;

public interface RawPhrase extends Phrase {

    @Override
    default Verb baseVerb() {
        return null;
    }

    RawPhrase method(QueryMethod method);

    RawPhrase query(String query);

    RawPhrase param(Object param);

}

