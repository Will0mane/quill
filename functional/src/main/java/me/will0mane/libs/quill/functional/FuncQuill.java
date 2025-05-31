package me.will0mane.libs.quill.functional;

import me.will0mane.libs.quill.Quill;
import me.will0mane.libs.quill.QuillDriver;
import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.executor.FuncExecutor;
import me.will0mane.libs.quill.functional.tables.FuncTableManager;
import me.will0mane.libs.quill.tables.TableManager;

import java.util.HashMap;
import java.util.Map;

public class FuncQuill implements Quill {

    private final QuillDriver driver;

    private final Map<String, QuillExecutor> executors = new HashMap<>();
    private final String defaultDatabase;
    private final QuillExecutor defaultExecutor;

    private final TableManager tableManager;

    public FuncQuill(QuillDriver driver, String defaultDatabase) {
        this.driver = driver;
        this.defaultDatabase = defaultDatabase;

        defaultExecutor = new FuncExecutor(driver, defaultDatabase);
        register(defaultDatabase, defaultExecutor);

        tableManager = new FuncTableManager(this);
    }

    private void register(String database, QuillExecutor executor) {
        executors.put(database, executor);
    }

    @Override
    public QuillExecutor async() {
        return defaultExecutor;
    }

    @Override
    public QuillExecutor async(String database) {
        if (!executors.containsKey(database)) {
            register(database, new FuncExecutor(driver, database));
        }
        return executors.get(database);
    }

    @Override
    public String defaultDatabase() {
        return defaultDatabase;
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
