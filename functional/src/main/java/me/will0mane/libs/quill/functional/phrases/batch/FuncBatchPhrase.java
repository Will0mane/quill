package me.will0mane.libs.quill.functional.phrases.batch;

import me.will0mane.libs.quill.QuillDriver;
import me.will0mane.libs.quill.functional.driver.StatementBinder;
import me.will0mane.libs.quill.phrases.batch.BatchPhrase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncBatchPhrase implements BatchPhrase {

    private final QuillDriver driver;
    private final String database;
    private final String literal;
    private final List<Object[]> rows = new ArrayList<>();

    public FuncBatchPhrase(QuillDriver driver, String database, String literal) {
        this.driver = driver;
        this.database = database;
        this.literal = literal;
    }

    @Override
    public BatchPhrase add(Object... params) {
        rows.add(params);
        return this;
    }

    @Override
    public int[] execute() {
        if (rows.isEmpty()) return new int[0];

        Connection connection = driver.connection(database);
        try (PreparedStatement statement = connection.prepareStatement(literal)) {
            for (Object[] row : rows) {
                int i = 0;
                for (Object param : row) {
                    i++;
                    StatementBinder.bind(statement, i, param);
                }
                statement.addBatch();
            }
            return statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
