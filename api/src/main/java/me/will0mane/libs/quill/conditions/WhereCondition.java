package me.will0mane.libs.quill.conditions;

import me.will0mane.libs.quill.phrases.filterable.FilterablePhrase;

public interface WhereCondition extends Condition{

    FilterablePhrase isEqual(String column, Object param);

    FilterablePhrase isNotEqual(String column, Object param);

    FilterablePhrase isGreater(String column, Object param);

    FilterablePhrase isLess(String column, Object param);

    FilterablePhrase isGreaterOrEqual(String column, Object param);

    FilterablePhrase isLessOrEqual(String column, Object param);
    
    FilterablePhrase isLike(String column, String param);
    
    FilterablePhrase isNotLike(String column, String param);
    
}
