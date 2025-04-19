package me.will0mane.libs.quill.functional.phrases.delete;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BaseFilterablePhrase;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.delete.DeletePhrase;

public class FuncDeletePhrase extends BaseFilterablePhrase implements DeletePhrase {

    public FuncDeletePhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public DeletePhrase from(String table) {
        writeVerb(StandardVerbs.FROM);
        writeVerb(LiteralVerb.of(table));
        return this;
    }
}
