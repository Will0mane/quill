package me.will0mane.libs.quill.functional.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;

public class FixedConnectionProvider implements ConnectionProvider {

    private final Connection connection;

    public FixedConnectionProvider(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Executor executor() {
        return Runnable::run;
    }

    @Override
    public Connection connection(String database) throws SQLException {
        return connection;
    }
}
