package me.will0mane.libs.quill.functional;

import me.will0mane.libs.quill.Quill;
import me.will0mane.libs.quill.QuillDriver;
import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.executor.FuncExecutor;
import me.will0mane.libs.quill.model.ModelHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FuncQuill implements Quill {

    private final QuillDriver driver;

    private final Map<String, QuillExecutor> executors = new ConcurrentHashMap<>();
    private final String defaultDatabase;
    private final QuillExecutor defaultExecutor;

    private final ModelHandler models;

    public FuncQuill(QuillDriver driver, String defaultDatabase) {
        this.driver = driver;
        this.defaultDatabase = defaultDatabase;

        defaultExecutor = new FuncExecutor(driver, defaultDatabase);
        register(defaultDatabase, defaultExecutor);

        models = new ModelHandler();
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
    public ModelHandler models() {
        return models;
    }

}
