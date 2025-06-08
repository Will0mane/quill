package me.will0mane.libs.quill.model;

import me.will0mane.libs.quill.results.ResultReader;

import java.util.Collection;

public interface Query {

    void database(String database);

    void literal(String literal);

    void params(Collection<Object> params);

    void method(QueryMethod method);

    ResultReader execute();

}
