package me.will0mane.libs.quill.functional.phrases.select;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BaseFilterablePhrase;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.select.SelectPhrase;

public class FuncSelectPhrase extends BaseFilterablePhrase implements SelectPhrase {

    private int tableCounter = 0;

    private boolean firstOrder = true;

    public FuncSelectPhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public SelectPhrase columns(String... columns) {
        boolean first = true;

        for (String column : columns) {
            if (first) first = false;
            else writeVerb(StandardVerbs.LISTING);
            writeVerb(LiteralVerb.of(column));
        }

        return this;
    }

    @Override
    public SelectPhrase as(String name) {
        writeVerb(StandardVerbs.AS);
        writeVerb(LiteralVerb.of(name));
        return this;
    }

    @Override
    public SelectPhrase asterisk() {
        writeVerb(StandardVerbs.ASTERISK);
        return this;
    }

    @Override
    public SelectPhrase from(String table) {
        writeVerb(StandardVerbs.FROM);
        writeVerb(LiteralVerb.of(table));
        return this;
    }

    @Override
    public SelectPhrase limit(int limit) {
        writeVerb(StandardVerbs.LIMIT);
        writeVerb(StandardVerbs.UNKNOWN);
        assignParam(limit);
        return this;
    }

    @Override
    public SelectPhrase offset(int offset) {
        writeVerb(StandardVerbs.OFFSET);
        writeVerb(StandardVerbs.UNKNOWN);
        assignParam(offset);
        return this;
    }

    @Override
    public SelectPhrase orderBy(String column, boolean descending) {
        if (firstOrder) {
            writeVerb(StandardVerbs.ORDER_BY);
            firstOrder = false;
        } else {
            writeVerb(StandardVerbs.LISTING);
        }

        writeVerb(LiteralVerb.of(column));

        if (descending) writeVerb(StandardVerbs.DESC);
        else writeVerb(StandardVerbs.ASC);
        return this;
    }

    @Override
    public SelectPhrase innerJoin(String table, String columnOne, String columnTwo) {
        writeVerb(StandardVerbs.INNER);
        writeVerb(StandardVerbs.JOIN);
        joinContent(table, columnOne, columnTwo);
        return this;
    }

    private void joinContent(String table, String columnOne, String columnTwo) {
        writeVerb(LiteralVerb.of(table));

        tableCounter++;
        String literal = "q" + tableCounter;
        writeVerb(LiteralVerb.of(literal));

        writeVerb(StandardVerbs.ON);

        writeVerb(LiteralVerb.of(literal + "." + columnOne));
        writeVerb(StandardVerbs.EQUAL);
        writeVerb(LiteralVerb.of(columnTwo));
    }

    @Override
    public SelectPhrase leftJoin(String table, String columnOne, String columnTwo) {
        writeVerb(StandardVerbs.LEFT);
        writeVerb(StandardVerbs.JOIN);
        joinContent(table, columnOne, columnTwo);
        return this;
    }

    @Override
    public SelectPhrase rightJoin(String table, String columnOne, String columnTwo) {
        writeVerb(StandardVerbs.RIGHT);
        writeVerb(StandardVerbs.JOIN);
        joinContent(table, columnOne, columnTwo);
        return this;
    }

    @Override
    public SelectPhrase fullJoin(String table, String columnOne, String columnTwo) {
        writeVerb(StandardVerbs.FULL);
        writeVerb(StandardVerbs.JOIN);
        joinContent(table, columnOne, columnTwo);
        return this;
    }

    @Override
    public SelectPhrase union() {
        writeVerb(StandardVerbs.UNION);
        writeVerb(StandardVerbs.SELECT);
        return this;
    }

    @Override
    public SelectPhrase unionAll() {
        writeVerb(StandardVerbs.UNION);
        writeVerb(StandardVerbs.ALL);
        writeVerb(StandardVerbs.SELECT);
        return this;
    }
}
