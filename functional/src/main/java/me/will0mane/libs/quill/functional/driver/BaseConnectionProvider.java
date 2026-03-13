package me.will0mane.libs.quill.functional.driver;

import me.will0mane.libs.quill.model.SQLCredentials;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public abstract class BaseConnectionProvider implements ConnectionProvider {

    private final Executor executor;
    private final SQLCredentials credentials;
    private final String defaultDb;
    private final Map<String, ConnectionManager> connections = new ConcurrentHashMap<>();

    protected BaseConnectionProvider(Executor executor, SQLCredentials credentials, String defaultDb) {
        this.executor = executor;
        this.credentials = credentials;
        this.defaultDb = defaultDb;
    }

    @Override
    public Executor executor() {
        return executor;
    }

    public SQLCredentials credentials() {
        return credentials;
    }

    public abstract ConnectionManager manager(String database);

    @Override
    public Connection connection(String database) throws SQLException {
        database = database == null ? defaultDb : database;
        return connections.computeIfAbsent(database, this::manager).connection();
    }
}
