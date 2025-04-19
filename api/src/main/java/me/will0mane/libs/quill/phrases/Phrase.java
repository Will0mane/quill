package me.will0mane.libs.quill.phrases;

import me.will0mane.libs.quill.model.QueryMethod;
import me.will0mane.libs.quill.model.QueryPoint;
import me.will0mane.libs.quill.model.Verb;

import java.util.Collection;

public interface Phrase extends QueryPoint {

    String literal();

    Collection<Object> parameters();

    Verb baseVerb();

    QueryMethod method();

    void create();

}
