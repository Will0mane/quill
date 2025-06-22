package me.will0mane.libs.quill.phrases.update;

import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.filterable.FilterablePhrase;

public interface UpdatePhrase extends FilterablePhrase {

    @Override
    default Verb baseVerb() {
        return StandardVerbs.UPDATE;
    }

    @Override
    default QueryMethod method() {
        return QueryMethod.UPDATE;
    }

    UpdatePhrase table(String name);

    UpdatePhrase set(String name, Object value);

    UpdatePhrase set(String name, String expression, Object value);
}
