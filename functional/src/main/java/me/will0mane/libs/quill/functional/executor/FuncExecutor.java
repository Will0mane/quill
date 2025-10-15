package me.will0mane.libs.quill.functional.executor;

import me.will0mane.libs.quill.QuillDriver;
import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.phrases.alter.FuncAlterTablePhrase;
import me.will0mane.libs.quill.functional.phrases.create.FuncCreateDatabasePhrase;
import me.will0mane.libs.quill.functional.phrases.create.FuncCreateTablePhrase;
import me.will0mane.libs.quill.functional.phrases.delete.FuncDeletePhrase;
import me.will0mane.libs.quill.functional.phrases.describe.FuncDescribePhrase;
import me.will0mane.libs.quill.functional.phrases.drop.FuncDropPhrase;
import me.will0mane.libs.quill.functional.phrases.insert.FuncInsertPhrase;
import me.will0mane.libs.quill.functional.phrases.raw.FuncRawPhrase;
import me.will0mane.libs.quill.functional.phrases.select.FuncSelectPhrase;
import me.will0mane.libs.quill.functional.phrases.update.FuncUpdatePhrase;
import me.will0mane.libs.quill.functional.results.FuncResult;
import me.will0mane.libs.quill.model.Query;
import me.will0mane.libs.quill.model.QueryOption;
import me.will0mane.libs.quill.phrases.Phrase;
import me.will0mane.libs.quill.phrases.alter.AlterTablePhrase;
import me.will0mane.libs.quill.phrases.create.CreateDatabasePhrase;
import me.will0mane.libs.quill.phrases.create.CreateTablePhrase;
import me.will0mane.libs.quill.phrases.delete.DeletePhrase;
import me.will0mane.libs.quill.phrases.describe.DescribePhrase;
import me.will0mane.libs.quill.phrases.drop.DropPhrase;
import me.will0mane.libs.quill.phrases.insert.InsertPhrase;
import me.will0mane.libs.quill.phrases.raw.RawPhrase;
import me.will0mane.libs.quill.phrases.select.SelectPhrase;
import me.will0mane.libs.quill.phrases.update.UpdatePhrase;
import me.will0mane.libs.quill.results.Result;
import me.will0mane.libs.quill.results.ResultReader;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class FuncExecutor implements QuillExecutor {

    private static final Logger LOGGER = Logger.getLogger("quill");

    private final QuillDriver driver;
    private final String database;

    public FuncExecutor(QuillDriver driver, String database) {
        this.driver = driver;
        this.database = database;
    }

    @Override
    public Result execute(Phrase phrase, QueryOption... options) {
        Query query = driver.makeQuery();

        String literal = phrase.literal();

        if (System.getProperties().get("debug-quill") != null) {
            LOGGER.info("Execute query: " + literal + "   [Params: " + phrase.parameters() + "]");
        }

        query.database(database);
        query.literal(literal);
        query.method(phrase.method());
        query.params(phrase.parameters());
        query.options(Arrays.asList(options));

        CompletableFuture<ResultReader> reader = new CompletableFuture<>();
        driver.async(() -> {
			try {
				reader.complete(query.execute());
			} catch (Throwable e) {
				e.printStackTrace();
				reader.completeExceptionally(e);
			}
		});
		
        return new FuncResult(reader);
    }

    private <T extends Phrase> T makePhrase(T phrase) {
        phrase.create();
        return phrase;
    }

    @Override
    public RawPhrase raw() {
        return makePhrase(new FuncRawPhrase(this));
    }

    @Override
    public DeletePhrase delete() {
        return makePhrase(new FuncDeletePhrase(this));
    }

    @Override
    public InsertPhrase insert() {
        return makePhrase(new FuncInsertPhrase(this));
    }

    @Override
    public UpdatePhrase update() {
        return makePhrase(new FuncUpdatePhrase(this));
    }

    @Override
    public SelectPhrase select() {
        return makePhrase(new FuncSelectPhrase(this));
    }

    @Override
    public DropPhrase drop() {
        return makePhrase(new FuncDropPhrase(this));
    }

    @Override
    public CreateTablePhrase createTable() {
        return makePhrase(new FuncCreateTablePhrase(this));
    }

    @Override
    public CreateDatabasePhrase createDatabase() {
        return makePhrase(new FuncCreateDatabasePhrase(this));
    }

    @Override
    public AlterTablePhrase alterTable() {
        return makePhrase(new FuncAlterTablePhrase(this));
    }

    @Override
    public DescribePhrase describe() {
        return makePhrase(new FuncDescribePhrase(this));
    }
}
