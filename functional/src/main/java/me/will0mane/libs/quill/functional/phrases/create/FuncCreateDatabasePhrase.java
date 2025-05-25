package me.will0mane.libs.quill.functional.phrases.create;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BasePhrase;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.create.CreateDatabasePhrase;

public class FuncCreateDatabasePhrase extends BasePhrase implements CreateDatabasePhrase {

    public FuncCreateDatabasePhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public CreateDatabasePhrase name(String name) {
        writeVerb(LiteralVerb.of(name));
        return this;
    }

    @Override
    public CreateDatabasePhrase ifNotExists() {
        writeVerb(StandardVerbs.IF_NOT_EXISTS);
        return this;
    }
}
