package me.will0mane.libs.quill.functional.phrases.drop;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BasePhrase;
import me.will0mane.libs.quill.model.StandardVerbs;
import me.will0mane.libs.quill.phrases.drop.DropPhrase;

public class FuncDropPhrase extends BasePhrase implements DropPhrase {

    private boolean ifExists = false;

    public FuncDropPhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public DropPhrase table(String name) {
        writeVerb(StandardVerbs.TABLE);
        if(ifExists) {
            writeVerb(StandardVerbs.IF_EXISTS);
        }
        writeVerb(LiteralVerb.of(name));
        return this;
    }

    @Override
    public DropPhrase database(String name) {
        writeVerb(StandardVerbs.DATABASE);
        if(ifExists) {
            writeVerb(StandardVerbs.IF_EXISTS);
        }
        writeVerb(LiteralVerb.of(name));
        return this;
    }

    @Override
    public DropPhrase ifExists() {
        ifExists = true;
        return this;
    }
}
