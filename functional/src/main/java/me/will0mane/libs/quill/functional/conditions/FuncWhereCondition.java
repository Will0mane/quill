package me.will0mane.libs.quill.functional.conditions;

import me.will0mane.libs.quill.conditions.WhereCondition;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.model.Scribe;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.filterable.FilterablePhrase;

public class FuncWhereCondition implements WhereCondition {

    private final Scribe scribe;

    private final FilterablePhrase phrase;

    public FuncWhereCondition(Scribe scribe, FilterablePhrase phrase) {
        this.scribe = scribe;
        this.phrase = phrase;
    }

    private void write(String val) {
        write(LiteralVerb.of(val));
    }

    private void write(Verb verb) {
        scribe.write(verb);
    }

    private void writeCompact(String field, Verb verb, Object value) {
        write(field);
        write(verb);
        write(StandardVerbs.UNKNOWN);

        scribe.assign(value);
    }

    @Override
    public FilterablePhrase isEqual(String column, Object param) {
        writeCompact(column, StandardVerbs.EQUAL, param);
        return phrase;
    }

    @Override
    public FilterablePhrase isNotEqual(String column, Object param) {
        writeCompact(column, StandardVerbs.NOT_EQUAL, param);
        return phrase;
    }

    @Override
    public FilterablePhrase isGreater(String column, Object param) {
        writeCompact(column, StandardVerbs.GREATER, param);
        return phrase;
    }

    @Override
    public FilterablePhrase isLess(String column, Object param) {
        writeCompact(column, StandardVerbs.LESS, param);
        return phrase;
    }

    @Override
    public FilterablePhrase isGreaterOrEqual(String column, Object param) {
        writeCompact(column, StandardVerbs.GREATER_EQUAL, param);
        return phrase;
    }

    @Override
    public FilterablePhrase isLessOrEqual(String column, Object param) {
        writeCompact(column, StandardVerbs.LESS_EQUAL, param);
        return phrase;
    }

    @Override
    public FilterablePhrase isLike(String column, String param) {
        writeCompact(column, StandardVerbs.LIKE, "%" + param + "%");
        return phrase;
    }

    @Override
    public FilterablePhrase isNotLike(String column, String param) {
        writeCompact(column, StandardVerbs.NOT_LIKE, "%" + param + "%");
        return phrase;
    }

    @Override
    public FilterablePhrase in(String column, Object... params) {
        write(LiteralVerb.of(column));
        write(StandardVerbs.IN);
        write(StandardVerbs.COMPLEX_OPEN_PARAM);
        
        boolean first = true;
        
        for (Object param : params) {
            if (first) first = false;
            else write(StandardVerbs.COMPLEX_LISTING);
            write(StandardVerbs.UNKNOWN);
            scribe.assign(param);
        }
        
        write(StandardVerbs.COMPLEX_CLOSE_PARAM);
        return phrase;
    }
}
