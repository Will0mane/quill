package me.will0mane.libs.quill.functional;

import me.will0mane.libs.quill.QuillDriver;
import me.will0mane.libs.quill.QuillSession;
import me.will0mane.libs.quill.executor.QuillExecutor;
import me.will0mane.libs.quill.functional.driver.BaseQuillDriver;
import me.will0mane.libs.quill.functional.driver.FixedConnectionProvider;
import me.will0mane.libs.quill.functional.driver.NonClosingConnection;
import me.will0mane.libs.quill.functional.executor.FuncExecutor;

import java.sql.Connection;
import java.sql.SQLException;

public class FuncQuillSession implements QuillSession {

    private final Connection realConnection;
    private final QuillExecutor executor;
    private final boolean previousAutoCommit;

    public FuncQuillSession(Connection realConnection, String database) {
        this.realConnection = realConnection;
        try {
            this.previousAutoCommit = realConnection.getAutoCommit();
            realConnection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start session on database " + database, e);
        }

        Connection guarded = NonClosingConnection.wrap(realConnection);
        QuillDriver sessionDriver = new BaseQuillDriver(new FixedConnectionProvider(guarded));
        this.executor = new FuncExecutor(sessionDriver, database, false);
    }

    @Override
    public QuillExecutor executor() {
        return executor;
    }

    @Override
    public void commit() {
        try {
            realConnection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to commit session", e);
        }
    }

    @Override
    public void rollback() {
        try {
            realConnection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to rollback session", e);
        }
    }

    @Override
    public void close() {
        try {
            realConnection.setAutoCommit(previousAutoCommit);
        } catch (SQLException ignored) {
        }
        try {
            realConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close session connection", e);
        }
    }
}
