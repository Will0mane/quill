package me.will0mane.libs.quill.results;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Result {

    @Deprecated
    CompletableFuture<ResultReader> await();

    /**
     * Returns the {@link ResultReader} immediately, without going through a callback.
     * Only available when the query was executed through a synchronous executor
     * (e.g. {@code Quill#sync()}); calling this on an async result throws.
     * The caller owns the returned reader and must close it (try-with-resources).
     */
    ResultReader get();

    void await(Consumer<ResultReader> consumer);

    default void await(Consumer<ResultReader> consumer, Consumer<Throwable> onError) {
        try {
            await(consumer);
        } catch (Throwable t) {
            onError.accept(t);
        }
    }

}
