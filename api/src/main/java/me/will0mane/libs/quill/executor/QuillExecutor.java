package me.will0mane.libs.quill.executor;

import me.will0mane.libs.quill.phrases.Phrase;
import me.will0mane.libs.quill.phrases.create.CreateDatabasePhrase;
import me.will0mane.libs.quill.phrases.create.CreateTablePhrase;
import me.will0mane.libs.quill.phrases.delete.DeletePhrase;
import me.will0mane.libs.quill.phrases.drop.DropPhrase;
import me.will0mane.libs.quill.phrases.insert.InsertPhrase;
import me.will0mane.libs.quill.phrases.select.SelectPhrase;
import me.will0mane.libs.quill.phrases.update.UpdatePhrase;
import me.will0mane.libs.quill.results.Result;

public interface QuillExecutor {

    Result execute(Phrase phrase);

    DeletePhrase delete();

    InsertPhrase insert();

    UpdatePhrase update();

    SelectPhrase select();

    DropPhrase drop();

    CreateTablePhrase createTable();

    CreateDatabasePhrase createDatabase();

}
