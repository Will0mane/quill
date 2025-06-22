package me.will0mane.libs.quill.model;

import me.will0mane.libs.quill.results.ResultReader;

import java.util.Collection;
import java.util.List;

public interface Query {

    void database(String database);

    void literal(String literal);

    void params(Collection<Object> params);

    void options(Collection<QueryOption> options);

    void method(QueryMethod method);

    ResultReader execute();

}
