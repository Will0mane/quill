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


| Statement       | Supported | Example                                                                                                                                       | Equivalent Query                                                                               |
|-----------------|-----------|-----------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| SELECT          | ✅         | ```quill.async().select().asterisk().from("example")```                                                                                             | ```SELECT * FROM example```                                                                          |
| INSERT INTO     | ✅         | ```quill.async().insert().into("table").columns("first").values("1")```                                                                             | ```INSERT INTO table (first) VALUES ("1")```                                                         |
| CREATE DATABASE | ✅         | ```quill.async.createDatabase().ifNotExists().name("db_name")```                                                                                    | ```CREATE DATABASE IF NOT EXISTS db_name```                                                          |
| CREATE TABLE    | ✅         | ```quill.async.createTable().ifNotExists().name("table_name").columns(Column.of("column_name", "bigint", NOT_NULL, PRIMARY_KEY, AUTO_INCREMENT))``` | ```CREATE TABLE IF NOT EXISTS table_name (column_name bigint not null primary key auto_increment)``` |
| UPDATE          | ✅         | ```quill.async.update().table("table_name").set("column_name", "some_value").where().isEqual("id", 1)```                                            | ```UPDATE table_name SET column_name = 'some_value' WHERE id = 1```                                  |
| DELETE FROM     | ✅         | ```quill.async.delete().from("table_name").where().isEqual("some_column", "some_value")```                                                          | ```DELETE FROM table_name WHERE some_column = 'some_value'```                                        |
| DROP DATABASE   | ✅         | ```quill.async.drop().database("db_name")```                                                                                                        | ```DROP DATABASE db_name```                                                                          |
| DROP TABLE      | ✅         | ```quill.async.drop().table("table_name")```                                                                                                        | ```DROP TABLE table_name```                                                                          |
| CREATE INDEX    | ❌         |                                                                                                                                               |                                                                                                |
| CREATE VIEW     | ❌         |                                                                                                                                               |                                                                                                |
| DROP INDEX      | ❌         |                                                                                                                                               |                                                                                                |
| ALTER TABLE     | ❌         |                                                                                                                                               |                                                                                                |
| GRANT           | ❌         |                                                                                                                                               |                                                                                                |
| REVOKE          | ❌         |                                                                                                                                               |                                                                                                |


