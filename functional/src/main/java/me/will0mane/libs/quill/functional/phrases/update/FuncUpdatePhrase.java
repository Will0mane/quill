package me.will0mane.libs.quill.functional.phrases.update;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.FunctionWriter;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BaseFilterablePhrase;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.update.UpdatePhrase;

public class FuncUpdatePhrase extends BaseFilterablePhrase implements UpdatePhrase {

    private boolean wroteSetHeader = false;

    public FuncUpdatePhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public UpdatePhrase table(String name) {
        writeVerb(LiteralVerb.of(name));
        return this;
    }

    @Override
    public UpdatePhrase set(String name, Object value) {
        if (!wroteSetHeader) {
            writeVerb(StandardVerbs.SET);
            wroteSetHeader = true;
        } else {
            writeVerb(StandardVerbs.LISTING);
        }

        writeVerb(LiteralVerb.of(name));
        writeVerb(StandardVerbs.EQUAL);
        FunctionWriter.process(value, scribe());
        return this;
    }

    @Override
    public UpdatePhrase set(String name, String expression, Object value) {
        if (!wroteSetHeader) {
            writeVerb(StandardVerbs.SET);
            wroteSetHeader = true;
        } else {
            writeVerb(StandardVerbs.LISTING);
        }

        writeVerb(LiteralVerb.of(name));
        writeVerb(StandardVerbs.EQUAL);
        writeVerb(LiteralVerb.of(name));
        writeVerb(LiteralVerb.of(expression));

        FunctionWriter.process(value, scribe());
        return this;
    }
}
