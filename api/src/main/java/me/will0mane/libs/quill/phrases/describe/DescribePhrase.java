package me.will0mane.libs.quill.phrases.describe;

import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.Phrase;

public interface DescribePhrase extends Phrase {

    @Override
    default Verb baseVerb() {
        return StandardVerbs.DESCRIBE;
    }

    @Override
    default QueryMethod method() {
        return QueryMethod.RETRIEVE_DATA;
    }

    DescribePhrase name(String name);

    default DescribePhrase table(String table) {
        return name(table);
    }
}


