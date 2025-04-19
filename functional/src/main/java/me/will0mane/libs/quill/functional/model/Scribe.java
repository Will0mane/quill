package me.will0mane.libs.quill.functional.model;

import me.will0mane.libs.quill.model.Verb;

import java.util.Collection;
import java.util.LinkedList;

public class Scribe {

    private final StringBuilder verbs = new StringBuilder();

    private boolean firstWord = true;

    private final Collection<Object> parameters = new LinkedList<>();

    public void write(Verb verb) {
        write(verb.toSQL(), verb.appendSpace());
    }

    public void write(String verb, boolean appendSpace) {
        if (firstWord) firstWord = false;
        else if (appendSpace) verbs.append(" ");
        verbs.append(verb);
    }

    public void assign(Object parameter) {
        parameters.add(parameter);
    }

    public String statement() {
        return verbs.toString();
    }

    public Collection<Object> parameters() {
        return parameters;
    }

}
