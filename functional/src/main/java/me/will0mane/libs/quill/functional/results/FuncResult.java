package me.will0mane.libs.quill.functional.results;

import me.will0mane.libs.quill.results.Result;
import me.will0mane.libs.quill.results.ResultReader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FuncResult implements Result {

    private static final Logger LOGGER = Logger.getLogger("quill");

    private final CompletableFuture<ResultReader> future;
    private final boolean sync;

    public FuncResult(CompletableFuture<ResultReader> future, boolean sync) {
        this.future = future;
        this.sync = sync;
    }

    @Override
    public CompletableFuture<ResultReader> await() {
        return future;
    }

    @Override
    public ResultReader get() {
        if (!sync) {
            throw new IllegalStateException("get() is only available for results of a sync executor; use await(...) for async ones");
        }
        try {
            return future.join();
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException re) throw re;
            throw new RuntimeException(cause != null ? cause : e);
        }
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
