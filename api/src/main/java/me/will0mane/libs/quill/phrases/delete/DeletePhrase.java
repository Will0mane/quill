package me.will0mane.libs.quill.phrases.delete;

import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.filterable.FilterablePhrase;

public interface DeletePhrase extends FilterablePhrase {

    @Override
    default QueryMethod method() {
        return QueryMethod.UPDATE;
    }

    @Override
    default Verb baseVerb() {
        return StandardVerbs.DELETE;
    }

    DeletePhrase from(String table);

}
