# Quill - SQL Builder for Java
Do you find boring to write SQL statements by hand all the time? We've got you covered.
With Quill the job is easier and less prone to bugs and errors.

Example SELECT statement.
```
Result send = quill.async()
        .select().asterisk().from("example_table")
        .where().isGreater("some_column", 5).send();
```
Quill does all the query building and parameter assignment by itself.

