package me.will0mane.libs.quill;

import me.will0mane.libs.quill.model.Query;
import me.will0mane.libs.quill.results.ResultReader;

import java.sql.Connection;
import java.sql.ResultSet;

public interface QuillDriver {

    void async(Runnable runnable);

    Connection connection();

    Query makeQuery();

    ResultReader reader(ResultSet set);

    ResultReader reader(int i);

    ResultReader reader(boolean success);

}
