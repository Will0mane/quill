package me.will0mane.libs.quill.functional;

import me.will0mane.libs.quill.model.Table;
import me.will0mane.libs.quill.model.annotations.*;

@Id("example")
public class ExampleTable implements Table {

    @Id("id_player")
    @NotNull
    @PrimaryKey
    @AutoIncrement
    int id;

    @Id("money")
    @DefaultBigInt(def = 0)
    @NotNull
    long money;

}
