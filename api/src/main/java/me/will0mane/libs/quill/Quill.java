package me.will0mane.libs.quill;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.tables.TableManager;

public interface Quill {

    QuillExecutor async();

    QuillDriver driver();

    TableManager tables();

}
