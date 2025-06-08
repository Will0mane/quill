package me.will0mane.libs.quill.functional.phrases.raw;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BasePhrase;
import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.phrases.raw.RawPhrase;

public class FuncRawPhrase extends BasePhrase implements RawPhrase {

    private QueryMethod method = QueryMethod.NORMAL;

    public FuncRawPhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public RawPhrase method(QueryMethod method) {
        this.method = method;
        return this;
    }

    @Override
    public RawPhrase query(String query) {
        writeVerb(LiteralVerb.of(query));
        return this;
    }

    @Override
    public RawPhrase param(Object param) {
        assignParam(param);
        return this;
    }

    @Override
    public QueryMethod method() {
        return method;
    }
}
