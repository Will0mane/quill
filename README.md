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
That query is equivalent to 
```
SELECT * FROM example_table WHERE some_column > 5;
```

# Current support for queries:


| Statement  | Supported | Example |
| ------------- | ------------- | ------------- |
| SELECT  | ✅  | quill.async().select().asterisk().from("example") | |
| INSERT INTO | ✅ | quill.async().insert().into("table").columns("first").values("1") |
| CREATE DATABASE  | ✅  |  |
| CREATE TABLE  | ✅  |  |
| UPDATE  | ✅  |  |
| DELETE FROM  | ✅  |  |
| DROP DATABASE  | ✅  |  |
| DROP TABLE  | ✅  |  |
| TRUNCATE TABLE  | ✅  |  |
| CREATE INDEX  | ❌  |  |
| CREATE VIEW  | ❌  |  |
| DROP INDEX  | ❌  |  |
| ALTER TABLE  | ❌  |  |
| GRANT  | ❌  |  |
| REVOKE  | ❌  |  |


