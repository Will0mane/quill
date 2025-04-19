package me.will0mane.libs.quill.functional.results;

import me.will0mane.libs.quill.results.Result;
import me.will0mane.libs.quill.results.ResultReader;

import java.util.concurrent.CompletableFuture;

public class FuncResult implements Result {

    private final CompletableFuture<ResultReader> future;

    public FuncResult(CompletableFuture<ResultReader> future) {
        this.future = future;
    }

    @Override
    public CompletableFuture<ResultReader> await() {
        return future;
    }

}
