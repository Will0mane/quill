# Quill

A fluent SQL builder and lightweight ORM for Java 21+. Quill lets you construct and execute SQL queries using a type-safe, chainable API — no more hand-written SQL strings or manual parameter binding.

```java
quill.async()
    .select().asterisk().from("users")
    .where().isGreater("age", 18)
    .orderBy("name", false)
    .limit(10)
    .send().await(reader -> {
        while (reader.next()) {
            String name = reader.get("name");
        }
    });
```

## Modules

Quill is split into two Gradle subprojects:

| Module | Artifact | Description |
|--------|----------|-------------|
| `api` | `me.will0mane.libs.quill:api` | Interfaces, annotations, and model classes. No external dependencies. |
| `functional` | `me.will0mane.libs.quill:functional` | Concrete implementation. Depends on `api`. |

Add both modules to your project. The `functional` module transitively includes `api`.

```groovy
dependencies {
    implementation 'me.will0mane.libs.quill:functional:1.0-SNAPSHOT'
}
```

Requires **Java 21** or later.

## Setup

### 1. Create a connection provider

Extend `BaseConnectionProvider` to supply JDBC connections. You control the connection strategy (pooling, HikariCP, single connection, etc.).

```java
public class MyConnectionProvider extends BaseConnectionProvider {

    public MyConnectionProvider(Executor executor, SQLCredentials credentials, String defaultDb) {
        super(executor, credentials, defaultDb);
    }

    @Override
    public ConnectionManager manager(String database) {
        // Return a ConnectionManager that provides connections for the given database.
        // For example, wrapping a HikariCP DataSource:
        return () -> dataSource.getConnection();
    }
}
```

### 2. Initialize Quill

```java
SQLCredentials credentials = new SQLCredentials("localhost:3306", "root", "password");
Executor executor = Executors.newFixedThreadPool(4);
ConnectionProvider provider = new MyConnectionProvider(executor, credentials, "my_database");
QuillDriver driver = new BaseQuillDriver(provider);
Quill quill = new FuncQuill(driver, "my_database");
```

### 3. Run queries

```java
QuillExecutor exec = quill.async();          // uses the default database
QuillExecutor exec = quill.async("other_db"); // targets a specific database
```

All queries execute asynchronously on the executor thread pool.

## Query API

Every query starts from a `QuillExecutor`, chains builder methods, and ends with `.send()` (returns a `Result`) or `.sendAndIgnore()` (fire-and-forget).

### SELECT

```java
quill.async()
    .select().columns("id", "name", "email")
    .from("users")
    .where().isEqual("active", true)
    .and().isGreater("age", 18)
    .orderBy("name", false)   // false = ASC, true = DESC
    .limit(25)
    .offset(50)
    .send().await(reader -> {
        while (reader.next()) {
            int id = reader.get("id");
            String name = reader.get("name");
        }
    }, error -> {
        error.printStackTrace();
    });
```

Select all columns:

```java
quill.async().select().asterisk().from("users").send();
```

#### Joins

```java
quill.async()
    .select().columns("users.name", "orders.total")
    .from("users")
    .innerJoin("orders", "user_id", "users.id")
    .send();
```

Supported join types: `innerJoin`, `leftJoin`, `rightJoin`, `fullJoin`.

#### Union

```java
quill.async()
    .select().columns("name").from("employees")
    .union()
    .columns("name").from("contractors")
    .send();
```

Use `.unionAll()` to include duplicates.

#### Column aliases

```java
quill.async()
    .select().columns("first_name").as("name")
    .from("users")
    .send();
```

### INSERT

```java
quill.async()
    .insert().into("users")
    .columns("name", "email")
    .values("Alice", "alice@example.com")
    .send();
```

Insert multiple rows:

```java
quill.async()
    .insert().into("users")
    .columns("name", "email")
    .row("Alice", "alice@example.com")
    .row("Bob", "bob@example.com")
    .send();
```

#### Retrieve generated keys

```java
quill.async()
    .insert().into("users")
    .columns("name")
    .values("Alice")
    .send(QueryOption.RETURN_GENERATED)
    .await(reader -> {
        Object generatedId = reader.get(ResultConstants.GENERATED_KEYS_SPACE, 1);
    });
```

#### ON DUPLICATE KEY UPDATE

```java
quill.async()
    .insert().into("users")
    .columns("id", "name")
    .values(1, "Alice")
    .onConflictUpdate("name", "Alice")
    .send();
```

With an expression:

```java
.onConflictUpdate("login_count", "+", 1)  // login_count = login_count + 1
```

### UPDATE

```java
quill.async()
    .update().table("users")
    .set("name", "Bob")
    .set("email", "bob@example.com")
    .where().isEqual("id", 1)
    .send().await(reader -> {
        boolean success = reader.isSuccess();
    });
```

With an expression:

```java
quill.async()
    .update().table("products")
    .set("stock", "-", 1)  // stock = stock - 1
    .where().isEqual("id", 42)
    .send();
```

### DELETE

```java
quill.async()
    .delete().from("users")
    .where().isEqual("id", 1)
    .send();
```

### CREATE TABLE

```java
quill.async()
    .createTable()
    .ifNotExists()
    .name("users")
    .columns(
        Column.of("id", "bigint", ColumnOption.NOT_NULL, ColumnOption.PRIMARY_KEY, ColumnOption.AUTO_INCREMENT),
        Column.of("name", "varchar(255)", ColumnOption.NOT_NULL),
        Column.of("email", "text"),
        Column.of("score", "int", 0, ColumnOption.NOT_NULL)  // with default value
    )
    .send();
```

### CREATE DATABASE

```java
quill.async()
    .createDatabase()
    .ifNotExists()
    .name("my_database")
    .send();
```

### DROP

```java
// Drop table
quill.async().drop().ifExists().table("users").send();

// Drop database
quill.async().drop().ifExists().database("my_database").send();
```

> **Note:** Call `.ifExists()` before `.table()` or `.database()`.

### ALTER TABLE

```java
quill.async()
    .alterTable().name("users")
    .add(Column.of("phone", "varchar(20)"))
    .send();

// Rename a column
quill.async().alterTable().name("users").rename("phone", "phone_number").send();

// Modify column type
quill.async().alterTable().name("users").modify("phone_number", "text").send();

// Drop a column
quill.async().alterTable().name("users").drop("phone_number").send();
```

### DESCRIBE

```java
quill.async()
    .describe().name("users")
    .send().await(reader -> {
        while (reader.next()) {
            String field = reader.get("Field");
            String type = reader.get("Type");
        }
    });
```

### RAW SQL

For queries that the builder doesn't cover, use raw SQL with parameterized placeholders:

```java
quill.async()
    .raw()
    .method(QueryMethod.RETRIEVE_DATA)
    .query("SELECT * FROM users WHERE name LIKE ? AND age > ?")
    .param("%alice%")
    .param(21)
    .send().await(reader -> {
        while (reader.next()) {
            String name = reader.get("name");
        }
    });
```

Query methods: `RETRIEVE_DATA` (SELECT), `UPDATE` (INSERT/UPDATE/DELETE), `NORMAL` (DDL).

## WHERE Conditions

All filterable phrases (`SELECT`, `UPDATE`, `DELETE`) support WHERE clauses with the following operators:

| Method | SQL |
|--------|-----|
| `.isEqual(column, value)` | `column = ?` (or `column IS NULL` if value is null) |
| `.isNotEqual(column, value)` | `column != ?` (or `column IS NOT NULL`) |
| `.isGreater(column, value)` | `column > ?` |
| `.isLess(column, value)` | `column < ?` |
| `.isGreaterOrEqual(column, value)` | `column >= ?` |
| `.isLessOrEqual(column, value)` | `column <= ?` |
| `.isLike(column, value)` | `column LIKE '%value%'` |
| `.isNotLike(column, value)` | `column NOT LIKE '%value%'` |
| `.in(column, values...)` | `column IN (?, ?, ...)` |
| `.between(column, low, high)` | `column BETWEEN ? AND ?` |

Chain conditions with `.and()`, `.or()`, and `.not()`:

```java
.where().isEqual("status", "active")
.and().isGreater("age", 18)
.or().not().isEqual("role", "banned")
```

## SQL Functions

Use `SQLFunction` to call database functions in parameters:

```java
quill.async()
    .insert().into("logs")
    .columns("message", "created_at")
    .values("Hello", new SQLFunction("NOW"))
    .send();
// INSERT INTO logs (message, created_at) VALUES (?, NOW())
```

With function arguments:

```java
new SQLFunction("CONCAT", "hello", " ", "world")
// CONCAT(?, ?, ?)
```

## Result Handling

### Async callback (recommended)

```java
result.await(reader -> {
    // called on success
}, error -> {
    // called on failure
});
```

The `ResultReader` is automatically closed after the callback completes.

### Single-arg callback

```java
result.await(reader -> {
    // errors are printed to stderr
});
```

### CompletableFuture (deprecated)

```java
CompletableFuture<ResultReader> future = result.await();
// You are responsible for closing the ResultReader
```

### Reading results

```java
reader.next();                 // advance to next row, returns false when exhausted
reader.get("column_name");     // get value by column name
reader.get(1);                 // get value by column index
reader.isSuccess();            // check if an UPDATE/DELETE/DDL succeeded
```

## Model ORM

Quill includes a lightweight ORM layer for mapping Java classes to database tables.

### Define a model

```java
@Name("users")
public class User implements Model {

    @PrimaryKey
    @AutoIncrement
    private long id;

    @NotNull
    private String name;

    @Name("email_address")
    private String email;

    @Default
    @NotNull
    private int score = 100;  // @Default uses the field's value

    @Unsigned
    private int age;
}
```

### Available annotations

| Annotation | Target | Description |
|------------|--------|-------------|
| `@Name("value")` | Class, Field | Custom table or column name. Without it, the Java field name is used. |
| `@PrimaryKey` | Field | Marks column as `PRIMARY KEY`. |
| `@AutoIncrement` | Field | Marks column as `AUTO_INCREMENT`. Excluded from INSERT columns and populated from generated keys. |
| `@NotNull` | Field | Adds `NOT NULL` constraint. |
| `@Null` | Field | Explicitly allows `NULL`. |
| `@Key` | Field | Adds an index on the column. |
| `@Unsigned` | Field | Appends `UNSIGNED` to the column type. |
| `@Default` | Field | Uses the field's initialized value as the column default. |
| `@Keys({"col1", "col2"})` | Class | Defines composite keys at table level. |
| `@DisableAutoUpdate` | Field | Excludes the field from update operations. |

### ORM operations

```java
ModelHandler models = quill.models();
QuillProperties props = new QuillProperties(quill, "my_database");

// Load model metadata (do this once, cache the result)
User template = new User();
Plaster plaster = models.loadModel(template);

// Create the table if it doesn't exist
models.createTableQuery(props, plaster);

// Insert (auto-increment fields are populated on the returned entity)
User user = new User();
user.name = "Alice";
user.email = "alice@example.com";
CompletableFuture<User> inserted = models.insert(props, plaster, user);
inserted.thenAccept(u -> System.out.println("ID: " + u.id));

// Select by primary key
RowIdentifier id = new RowIdentifier("id", 1);
CompletableFuture<Data> found = models.select(props, plaster, id);
found.thenAccept(data -> {
    if (data != null) {
        String name = data.get("name");
    }
});

// Update all fields
models.update(props, plaster, user, id);

// Update specific fields only
models.updateSpecific(props, plaster, user, id, "name", "email");

// Delete
models.delete(props, plaster, id);
```

### Data class

`Data` is a generic key-value container returned by `select` and usable with `insert`/`update` when you don't want a full model class:

```java
Map<String, Object> map = new LinkedHashMap<>();
map.put("name", "Bob");
map.put("email", "bob@example.com");
Data data = new Data(map);

String name = data.get("name");
```

### Custom type mappings

Quill maps Java types to SQL types automatically. Add custom mappings for types not covered:

```java
ModelHandler.FIELD_TYPE_MAPPER.put(UUID.class, field -> "char(36)");
ModelHandler.FIELD_TYPE_MAPPER.put(LocalDateTime.class, field -> "datetime");
```

Built-in mappings:

| Java Type | SQL Type |
|-----------|----------|
| `boolean` | `tinyint(1)` |
| `int` | `int` |
| `long` | `bigint` |
| `float` | `float` |
| `double` | `double` |
| `short` | `smallint` |
| `byte` | `byte` |
| `String` | `text` |
| `BigInteger` | `bigint` |
| `BigDecimal` | `decimal(35,2)` |
| `JSON` | `json` |

## Debugging

Enable SQL query logging by setting the `debug-quill` system property:

```
-Ddebug-quill=true
```

This logs every executed query and its parameters through `java.util.logging` under the `quill` logger name.

## Supported Statements

| Statement | Example | Equivalent SQL |
|-----------|---------|----------------|
| SELECT | `select().asterisk().from("t")` | `SELECT * FROM t` |
| INSERT | `insert().into("t").columns("a").values(1)` | `INSERT INTO t (a) VALUES (1)` |
| UPDATE | `update().table("t").set("a", 1).where().isEqual("id", 1)` | `UPDATE t SET a = 1 WHERE id = 1` |
| DELETE | `delete().from("t").where().isEqual("id", 1)` | `DELETE FROM t WHERE id = 1` |
| CREATE TABLE | `createTable().ifNotExists().name("t").columns(...)` | `CREATE TABLE IF NOT EXISTS t (...)` |
| CREATE DATABASE | `createDatabase().ifNotExists().name("db")` | `CREATE DATABASE IF NOT EXISTS db` |
| ALTER TABLE | `alterTable().name("t").add(column)` | `ALTER TABLE t ADD COLUMN ...` |
| DROP TABLE | `drop().ifExists().table("t")` | `DROP TABLE IF EXISTS t` |
| DROP DATABASE | `drop().ifExists().database("db")` | `DROP DATABASE IF EXISTS db` |
| DESCRIBE | `describe().name("t")` | `DESCRIBE t` |
| RAW | `raw().method(RETRIEVE_DATA).query("...").param(v)` | *(your SQL)* |
