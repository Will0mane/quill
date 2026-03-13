package me.will0mane.libs.quill.results;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Result {

    @Deprecated
    CompletableFuture<ResultReader> await();

    void await(Consumer<ResultReader> consumer);

    default void await(Consumer<ResultReader> consumer, Consumer<Throwable> onError) {
        try {
            await(consumer);
        } catch (Throwable t) {
            onError.accept(t);
        }
    }

}
