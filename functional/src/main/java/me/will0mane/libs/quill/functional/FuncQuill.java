package me.will0mane.libs.quill.functional;

import me.will0mane.libs.quill.Quill;
import me.will0mane.libs.quill.QuillDriver;
import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.executor.FuncExecutor;
import me.will0mane.libs.quill.functional.tables.FuncTableManager;
import me.will0mane.libs.quill.tables.TableManager;

public class FuncQuill implements Quill {

    private final QuillDriver driver;
    private final QuillExecutor async;
    private final TableManager tableManager;

    public FuncQuill(QuillDriver driver) {
        this.driver = driver;

        async = new FuncExecutor(driver);
        tableManager = new FuncTableManager(this);
    }

    @Override
    public QuillExecutor async() {
        return async;
    }

    @Override
    public QuillDriver driver() {
        return driver;
    }

    @Override
    public TableManager tables() {
        return tableManager;
    }
}
