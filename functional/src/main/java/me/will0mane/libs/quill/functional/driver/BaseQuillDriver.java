package me.will0mane.libs.quill.functional.driver;

import me.will0mane.libs.quill.QuillDriver;
import me.will0mane.libs.quill.model.Query;
import me.will0mane.libs.quill.results.ResultReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class BaseQuillDriver implements QuillDriver {

    private final ConnectionProvider provider;

    public BaseQuillDriver(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void async(Runnable runnable) {
        CompletableFuture.runAsync(runnable, provider.executor());
    }

    @Override
    public Connection connection(String database) {
        try {
            return provider.connection(database);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Query makeQuery() {
        return new BaseQuillQuery(this);
    }

    @Override
    public ResultReader reader(ResultSet set) {
        return new BaseReaderSet(set);
    }

    @Override
    public ResultReader reader(int i) {
        return new BaseReaderSuccess(i > 0);
    }

    @Override
    public ResultReader reader(boolean success) {
        return new BaseReaderSuccess(success);
    }
}

