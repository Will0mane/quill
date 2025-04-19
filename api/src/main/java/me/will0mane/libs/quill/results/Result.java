package me.will0mane.libs.quill.results;

import java.util.concurrent.CompletableFuture;

public interface Result {

    CompletableFuture<ResultReader> await();

}
