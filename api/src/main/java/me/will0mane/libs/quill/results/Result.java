package me.will0mane.libs.quill.results;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Result {

    CompletableFuture<ResultReader> await();

    void await(Consumer<ResultReader> consumer);

}
