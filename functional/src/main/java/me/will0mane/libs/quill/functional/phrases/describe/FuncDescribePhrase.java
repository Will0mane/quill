package me.will0mane.libs.quill.functional.phrases.describe;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.model.LiteralVerb;
import me.will0mane.libs.quill.functional.phrases.BasePhrase;
import me.will0mane.libs.quill.phrases.describe.DescribePhrase;

public class FuncDescribePhrase extends BasePhrase implements DescribePhrase {

    public FuncDescribePhrase(QuillExecutor executor) {
        super(executor);
    }

    @Override
    public DescribePhrase name(String name) {
        writeVerb(LiteralVerb.of(name));
        return this;
    }
}
