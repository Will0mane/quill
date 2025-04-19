package me.will0mane.libs.quill.functional;

import me.will0mane.libs.quill.Quill;
import me.will0mane.libs.quill.QuillDriver;
import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.executor.FuncExecutor;

public class FuncQuill implements Quill {

    private final QuillExecutor async;

    public FuncQuill(QuillDriver driver) {
        async = new FuncExecutor(driver);
    }

    @Override
    public QuillExecutor async() {
        return async;
    }
}
