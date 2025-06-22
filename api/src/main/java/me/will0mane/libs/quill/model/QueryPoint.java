package me.will0mane.libs.quill.model;

import me.will0mane.libs.quill.results.Result;

public interface QueryPoint {

    Result send();

    void sendAndIgnore();

    Result send(QueryOption... options);

}
