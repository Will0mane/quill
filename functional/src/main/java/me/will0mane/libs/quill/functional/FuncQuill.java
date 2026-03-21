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
    private final Map<String, QuillExecutor> syncExecutors = new ConcurrentHashMap<>();

    private final String defaultDatabase;
    private final QuillExecutor defExecutor;
    private final QuillExecutor defSyncExecutor;

    private final ModelHandler models;

    public FuncQuill(QuillDriver driver, String defaultDatabase) {
        this.driver = driver;
        this.defaultDatabase = defaultDatabase;

        defExecutor = new FuncExecutor(driver, defaultDatabase, true);
        register(defaultDatabase, defExecutor, true);

        defSyncExecutor = new FuncExecutor(driver, defaultDatabase, false);
        register(defaultDatabase, defExecutor, false);

        models = new ModelHandler();
    }

    private void register(String database, QuillExecutor executor, boolean async) {
        if(async) executors.put(database, executor);
        else syncExecutors.put(database, executor);
    }

    @Override
    public QuillExecutor async() {
        return defExecutor;
    }

    @Override
    public QuillExecutor async(String database) {
        return executors.computeIfAbsent(database, db -> new FuncExecutor(driver, db, true));
    }

    @Override
    public QuillExecutor sync() {
        return defSyncExecutor;
    }

    @Override
    public QuillExecutor sync(String database) {
        return syncExecutors.computeIfAbsent(database, db -> new FuncExecutor(driver, db, false));
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
