package me.will0mane.libs.quill;

import me.will0mane.libs.quill.executor.QuillExecutor;

public interface QuillSession extends AutoCloseable {

    QuillExecutor executor();

    void commit();

    void rollback();

    @Override
    void close();

}
