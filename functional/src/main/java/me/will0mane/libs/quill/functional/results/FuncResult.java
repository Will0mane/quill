package me.will0mane.libs.quill.functional.results;

import me.will0mane.libs.quill.results.Result;
import me.will0mane.libs.quill.results.ResultReader;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class FuncResult implements Result {

    private final CompletableFuture<ResultReader> future;

    public FuncResult(CompletableFuture<ResultReader> future) {
        this.future = future;
    }

    @Override
    public CompletableFuture<ResultReader> await() {
        return future;
    }

    @Override
    public void await(Consumer<ResultReader> consumer) {
        await().thenAccept(reader -> {
            consumer.accept(reader);
            try {
                reader.close();
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

}
