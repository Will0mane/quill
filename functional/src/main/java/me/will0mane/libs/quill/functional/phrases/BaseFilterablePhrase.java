package me.will0mane.libs.quill.functional.phrases;

import me.will0mane.libs.quill.conditions.WhereCondition;
import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.conditions.FuncWhereCondition;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.filterable.FilterablePhrase;

public abstract class BaseFilterablePhrase extends BasePhrase implements FilterablePhrase {

    protected BaseFilterablePhrase(QuillExecutor executor) {
        super(executor);
    }

    private WhereCondition makeCond() {
        return new FuncWhereCondition(scribe(), this);
    }

    @Override
    public WhereCondition where() {
        writeVerb(StandardVerbs.WHERE);
        return makeCond();
    }

    @Override
    public WhereCondition and() {
        writeVerb(StandardVerbs.AND);
        return makeCond();
    }

    @Override
    public WhereCondition or() {
        writeVerb(StandardVerbs.OR);
        return makeCond();
    }

}
