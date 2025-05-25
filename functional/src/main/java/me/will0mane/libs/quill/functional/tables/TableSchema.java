package me.will0mane.libs.quill.functional.tables;

import me.will0mane.libs.quill.results.ResultReader;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TableSchema {

    private final Map<String, ColData> columns;

    public TableSchema(Map<String, ColData> columns) {
        this.columns = columns;
    }

    public TableSchema(ResultReader reader) throws SQLException {
        Map<String, ColData> map = new HashMap<>();
        while (reader.next()) {
            String id = reader.get("Field");
            String type = reader.get("Type");
            type = type.substring(0, type.indexOf("("));

            String nullable = reader.get("Null");
            String key = reader.get("Key");
            String def = reader.get("Default");
            String extra = reader.get("Extra");

            ColData data = new ColData(id, type, nullable.equals("YES"), KeyType.fromCode(key), def, extra);
            map.put(id, data);
        }

        this.columns = map;
    }

    public Map<String, ColData> getColumns() {
        return columns;
    }

}
