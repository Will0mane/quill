package me.will0mane.libs.quill.functional.results;

import me.will0mane.libs.quill.results.Result;
import me.will0mane.libs.quill.results.ResultReader;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FuncResult implements Result {

    private static final Logger LOGGER = Logger.getLogger("quill");

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
        await(consumer, Throwable::printStackTrace);
    }

    @Override
    public void await(Consumer<ResultReader> consumer, Consumer<Throwable> onError) {
        await().whenComplete((reader, ex) -> {
            if (ex != null) {
                onError.accept(ex);
                return;
            }
            try {
                consumer.accept(reader);
            } catch (Throwable t) {
                onError.accept(t);
            } finally {
                try {
                    reader.close();
                } catch (Throwable e) {
                    LOGGER.log(Level.WARNING, "Failed to close ResultReader", e);
                }
            }
        });
    }

}
