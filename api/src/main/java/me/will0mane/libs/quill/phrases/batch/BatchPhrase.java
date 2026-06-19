package me.will0mane.libs.quill.phrases.batch;

public interface BatchPhrase {

    BatchPhrase add(Object... params);

    int[] execute();

}
