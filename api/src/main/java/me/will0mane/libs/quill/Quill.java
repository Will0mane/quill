package me.will0mane.libs.quill;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.tables.TableManager;

public interface Quill {

    QuillExecutor async();

    QuillExecutor async(String database);

    String defaultDatabase();

    QuillDriver driver();

    TableManager tables();

}
