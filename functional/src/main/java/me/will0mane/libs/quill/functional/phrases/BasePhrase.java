package me.will0mane.libs.quill.functional.phrases;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.Scribe;
import me.will0mane.libs.quill.model.QueryOption;
import me.will0mane.libs.quill.model.Verb;
import me.will0mane.libs.quill.phrases.Phrase;
import me.will0mane.libs.quill.results.Result;

import java.util.Collection;

public abstract class BasePhrase implements Phrase {

    private final Scribe scribe = new Scribe();
    private final QuillExecutor executor;

    protected BasePhrase(QuillExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void create() {
        Verb verb = baseVerb();
        if (verb == null) return;
        writeVerb(verb);
    }

    public Scribe scribe() {
        return scribe;
    }

    protected void writeVerb(Verb verb) {
        scribe.write(verb);
    }

    protected void assignParam(Object o) {
        scribe.assign(o);
    }

    @Override
    public String literal() {
        return scribe.statement();
    }

    @Override
    public Collection<Object> parameters() {
        return scribe.parameters();
    }

    @Override
    public Result send() {
        return executor.execute(this);
    }

    @Override
    public Result send(QueryOption... options) {
        return executor.execute(this, options);
    }
}
