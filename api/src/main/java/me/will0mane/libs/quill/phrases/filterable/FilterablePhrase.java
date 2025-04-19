package me.will0mane.libs.quill.phrases.filterable;

import me.will0mane.libs.quill.conditions.WhereCondition;
import me.will0mane.libs.quill.phrases.Phrase;

public interface FilterablePhrase extends Phrase {

    WhereCondition where();

    WhereCondition and();

    WhereCondition or();

}
