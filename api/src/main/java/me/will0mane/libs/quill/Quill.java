package me.will0mane.libs.quill;

import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.model.ModelHandler;

public interface Quill {

    QuillExecutor async();

    QuillExecutor async(String database);

    QuillExecutor sync();

    QuillExecutor sync(String database);

    QuillExecutor executor(boolean async);

    QuillExecutor executor(String database, boolean async);

    String defaultDatabase();

    QuillDriver driver();

    ModelHandler models();

}
