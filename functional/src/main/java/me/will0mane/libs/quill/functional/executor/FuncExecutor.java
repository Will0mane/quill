package me.will0mane.libs.quill.functional.executor;

import me.will0mane.libs.quill.QuillDriver;
import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.phrases.delete.FuncDeletePhrase;
import me.will0mane.libs.quill.functional.phrases.insert.FuncInsertPhrase;
import me.will0mane.libs.quill.functional.phrases.select.FuncSelectPhrase;
import me.will0mane.libs.quill.functional.phrases.update.FuncUpdatePhrase;
import me.will0mane.libs.quill.functional.results.FuncResult;
import me.will0mane.libs.quill.model.Query;
import me.will0mane.libs.quill.phrases.Phrase;
import me.will0mane.libs.quill.phrases.delete.DeletePhrase;
import me.will0mane.libs.quill.phrases.insert.InsertPhrase;
import me.will0mane.libs.quill.phrases.select.SelectPhrase;
import me.will0mane.libs.quill.phrases.update.UpdatePhrase;
import me.will0mane.libs.quill.results.Result;
import me.will0mane.libs.quill.results.ResultReader;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class FuncExecutor implements QuillExecutor {

    private static final Logger LOGGER = Logger.getLogger("quill");

    private final QuillDriver driver;

    public FuncExecutor(QuillDriver driver) {
        this.driver = driver;
    }

    @Override
    public Result execute(Phrase phrase) {
        Query query = driver.makeQuery();

        String literal = phrase.literal();

        if (System.getProperties().get("debug-quill") != null) {
            LOGGER.info("Execute query: " + literal + "   [Params: " + phrase.parameters() + "]");
        }

        query.literal(literal);
        query.method(phrase.method());
        query.params(phrase.parameters());

        CompletableFuture<ResultReader> reader = new CompletableFuture<>();
        driver.async(() -> reader.complete(query.execute()));
        return new FuncResult(reader);
    }

    @Override
    public DeletePhrase delete() {
        FuncDeletePhrase funcDeletePhrase = new FuncDeletePhrase(this);
        funcDeletePhrase.create();
        return funcDeletePhrase;
    }

    @Override
    public InsertPhrase insert() {
        FuncInsertPhrase funcInsertPhrase = new FuncInsertPhrase(this);
        funcInsertPhrase.create();
        return funcInsertPhrase;
    }

    @Override
    public UpdatePhrase update() {
        FuncUpdatePhrase funcUpdatePhrase = new FuncUpdatePhrase(this);
        funcUpdatePhrase.create();
        return funcUpdatePhrase;
    }

    @Override
    public SelectPhrase select() {
        FuncSelectPhrase funcSelectPhrase = new FuncSelectPhrase(this);
        funcSelectPhrase.create();
        return funcSelectPhrase;
    }
}
