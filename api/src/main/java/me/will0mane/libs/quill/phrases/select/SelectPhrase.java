package me.will0mane.libs.quill.phrases.select;

import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.filterable.FilterablePhrase;

public interface SelectPhrase extends FilterablePhrase {

    @Override
    default Verb baseVerb() {
        return StandardVerbs.SELECT;
    }

    @Override
    default QueryMethod method() {
        return QueryMethod.RETRIEVE_DATA;
    }

    SelectPhrase columns(String... columns);

    SelectPhrase asterisk();

    SelectPhrase from(String table);

}
