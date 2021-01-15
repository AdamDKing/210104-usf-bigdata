### DataBases

A Database is a way to store data.  We use DBs for long term and (relatively) short term storage.  Typically your database will communicate with your application(s) over a network.

Imagine we have some web application that allows users to login and post on a personal blog.  This application consists of multiple pieces spread out over multiple machines.  The most straightforward, because we have the most experience with it, is the part of the app that runs on the user's computer in the user's browser (like Cliq).  A second piece is server side (sometimes middleware).  Your computer needs to get the client app from somewhere, and it needs to communicate with another machine to make the full functionality of the client app work.  This other machine is a server, running a Java/Scala/Go/Python/Ruby/JS/... application called a server or webserver.  Both of these pieces, client and server, are applications that don't have any (or much) long term memory.  Neither of them are suitable for storing the users' account information, the text of past blog posts, message history between users, ...neither of them are suitable for storing any of the information the application needs long term.

While we could theoretically just use the standard long term storage for computers (writing to disk), this solution is inadequate in a lot of ways.  Your typical file is a series of bits saved in order on disk.  This means if we want to, for instance, find the password associated with a username, we would need to scan through the file from the beginning, searching for that user.  Also, if users have related information like their account info, their email address(es), multiple sub-blogs associated with them, message histories between them and many other users, ... it becomes very difficult to effectively organize that information just writing to disk.  It becomes even more troublesome to organize your data if you don't want to repeat information.  Every time a user's username, for instance, is repeated throughout the dataset, that is one more location that must be successfully updated each time the user's username changes.

The above problems aren't exhaustive, and they aren't insurmountable either.  In a month and a half, we'll talk about Parquet, which provides very nice organized and efficient storage in .parquet files on disk.

Just some motivating discussion above.  A database is an application that is responsible for efficiently storing and querying your long term data.  Ultimately, the DB does need to write that data to disk, but when we use a DB we interact with the DB using the DB's abstractions, rather than worrying about how exactly the data is written to disk.

What does this mean?  For Postgres, a "relational" database, we store data in records/rows in tables/relations in schema in databases.  Each individual record is a single item of data, the table it is contained in provides the structure to that item of data, and schema and database are levels of further organization that contain tables.  For MongoDB, a "document" (NoSQL) database, we store data in documents contained in collections contained in databases.  Each document is an item of data, potentially containing other items of data.  Collections store related documents and the database is another layer of organization containing collections.

Quick summary of advantages of DB over writing to file:
- RDBMS provides and enforces structure on your stored data
- RDBMS makes it easy and efficient to search, filter, query stored data
- Retrieving specific subsets of data from an RDBMS is more efficient than scanning through files (Indexes!)
- Scalability : RDBMSs tend to work well with data up to 1TB (rule of thumb),  we'll discuss a bit
  how to make your DB handle more data (it needs to run on a cluster)
- Popularity : SQL has been around a long time and is used in many places
- Concurrent access : RDBMS manages multiple simultaneous connections, and allows us to modify
  the settings to achieve our desired balance of speed / safety.
- authentication, authorization, security is handled for us (or by our DB admin using DCL)
- data integrity : RDBMS protects against bad / corrupt data being stored in the DB.
  - mostly happens in transactions
  - everything done on the database is stored in a log on disk, this makes rollback "easy" within a transaction
  - this DB log will have a lot of significance when we get to Kafka in a month and a half

### Note on SQL and NoSQL
SQL is Structured Query Language.  It's a language that lets us communicate with relational databases to store/retrieve/edit data.  SQL looks like this : SELECT name FROM customers;, INSERT INTO customer VALUES (4, 'adam', 'king');, ...  There is a bit more to SQL because of its history.  SQL has been around for a very long time, and is used everywhere.  Because of this widespread use, many other tools support SQL, in addition to relational DBs.

NoSQL, you may hear as "No SQL", as in without SQL, or you may hear as "Not Only SQL".  There are many different types of databases that bill themselves as NoSQL: Mongo stores documents, DynamoDB stores data as key-value pairs, ...  Most NoSQL indeed do not support SQL, though some do.  Typically your NoSQL databases exist to solve some problem that is not well handled by traditional relational databases.

NoSQL vs. SQL isn't really something fundamental about a database.  tech will bill itself as SQL or NoSQL, but ultimately a database is an application for efficiently storing and querying long term data, so your application might support some functionality to that end and might not support other functionality.  (almost) everything you can do in MongoDB you can do in Postgres.  (almost) everything you can do in Postgres you can do in MongoDB.

This NoSQL vs SQL divide is similar to a few other distinctions we make when discussing databases.  We'll see ACID and BASE this week, and the line between them will be similarly blurry.  The CAP Therorem is useful for drawing real distinctions.

SQL and NoSQL better describe tendencies than brute facts about what some database technology is capable of.  A major part of the "tendency" is that SQL databases / relational databases are intended to be used with "normalized" data.  The default assumption when you are using a SQL database is that you will organize your data into a "normal form" (I like at least 3rd normal form).  Normalization provides a quite strict structure for your data.  NoSQL databases do not tend to encourage normalization.  Nested structure within data items is common in NoSQL and almost nonexistant in SQL

### Intro SQL Script:

```
-- This is a comment in sql
-- mostly your editors will give you comment syntax if you hit Ctrl+/

-- sql, the language, is case insensitive and each statement with a ;
-- sql is the main way that we interact with sql databases (rdbms)
--   rdbms is a Relational database Management system
-- as such, sql has quite varied functionality.  We divide it into a few
-- sublanguages:
-- DML : data manipulation language, used to insert, update, and delete individual records
-- DDL : data definition language, used to define the structure of tables + schemas + ...
--   DDL lets us create, alter, and drop tables
-- DQL : data query language, sometime included in DML.  lets us select records that match
--   a query from the database.  primary tool to retrieve data.
-- DCL : data control language, used to create and control users and access to the database
--   lets us grant and revoke access to tables/schemas/databases + functionality
-- TCL : transaction control language, used to define the limits of a database transaction.
--   also lets us specify what behaviour should happen when parts of that transaction fail

-- Quick tour!

-- CREATE is a DDL keyword, used to define a new table.  We can also ALTER tables to change them
-- and DROP tables to delete them.
-- TRUNCATE is also a DDL keyword, TRUNCATE will remove all the contents of a table while preserving the table itself.
create table dogs (id SERIAL, name TEXT);

-- modify the dogs_id_seq to change the starting value of the SERIAL.
alter sequence dogs_id_seq restart with 100;

-- DML has insert, update, and delete
insert into dogs values 
(default, 'fido'),
(default, 'rex'),
(default, 'fido'),
(default, 'fido');

-- update the names of the dogs with id 102 and 103, we don't want 3 dogs named fido
update dogs set name='blue' where id=102;
update dogs set name='red' where id=103;

-- delete the dog named 'red' from the dogs table
delete from dogs;

-- something to be careful of when using delete and update is that your conditions like name='red' are very necessary
-- if you don't have a condition, delete and update will modify every record

select * from dogs;

-- let's go ahead and drop the dogs table:
drop table dogs;

```

### Upcoming SQL Topics

At this juncture, we could go a few different directions:
- Creating your "data model" by specifying normalized tables using DDL (2)
  - We saw the basics of creating tables
  - Creating tables to actually use follows normalization rules and requires a few new features
- Discuss SELECT querying and SELECT clauses (1)
  - SELECT * FROM table; gets you all the data from a table.  In practice, we want to write
    more complicated queries to select only the data we need.
  - This leads into JOINs, once we cover SELECT clauses : SELECT, FROM, WHERE, GROUP BY, HAVING, ORDER BY, LIMIT, OFFSET
- ACID, DB transactions, isolation levels. (4)
  - Then BASE, CAP Theorem, running on a cluster, sharding
- Connecting PostgreSQL to a Scala application (3)
  - sbt + dependencies
  - JDBC PostgreSQL driver

### Dialects of SQL

SQL, having been around a long time and being wildly popular, is supported by multiple *vendors*.  There are multiple applications that are RDBMSs in popular use.  Examples: PostgreSQL, OracleSQL, MySQL, SQLite, SQL Server, ...  Each of these applications supports SQL, but each also has slight differences in the SQL syntax they support.  Because of this, we think of SQL as having "dialects".  PostgreSQL (the dialect) might have slightly different keywords and support slightly different functionality in comparison to OracleSQL (another dialect).  Some examples: VARCHAR2 in OracleSQL instead of VARCHAR, PostgreSQL supporting DDL in transactions (transactional DDL), some dialects use AUTOINCREMENT for an automatically incrementing integer, instead of SERIAL, ...

One of the reasons we're using Postgres is that it provides a lot of extra functionality on top of standard SQL: transactional DDL, support for Python/JS/other programming languages in addition to PLSQL (Programming Language SQL), support for storing indexed JSON, ...

### Indexes

All RDBMSs support the creation of indexes.  When you create an index you specify a column or a set of columns.  We say that you create the index on those column(s).  This will make select statements that filter on the column(s) of the index much faster.  If we create an index on a phone_num column, then for all future queries we will very rapidly retrieve records by their phone_num.

The tradeoff here is that writes to the table with the index will be slightly slower.  This is because new records in the table need to be indexed.  You index will also take up space on disk!  Under the hood your index is a search tree (B Tree specifically), that maintains the sorted order of records based on the column of the index.

Your search tree lets postgres find records in a table, querying by indexed column, in log(n) time rather than n time, where n is the number of records in the table.

Mostly know the effect of indexes, and that there's a search tree under the hood.

Example: CREATE INDEX ifk_album_artist_id ON album (artist_id);
This creates an index called ifk_album_artist_id
on the column artist_id
in the table album
This index will make retrieving albums by their artist id very fast (log(n) instead of n)
Since there are 347 albums, it will take roughly 9 steps to find albums by artist with an index
It would take 347 steps to find all albums by artist without an index (we'd have to check them all)

Each index maintains a sorted order for the records in a table.  One of the indexes is called *clustered* and is the actual order of the records, this is most often the *primary key* (which we haven't discussed yet).  The other indexes are unclustered, which means they maintain a sorted order that is different from the actual order of the records.

Specifying that a column is *UNIQUE* means that an index will be created on that column automatically.  Indexed columns do not need to be unique, however.

### SQL Injection

SQL Injection is a potential security flaw where text input by a user is evaluated as SQL.  Using user input directly or indirectly as part of a String that will be evaluated as SQL can provide the desired functionality when we're developing.  However, malicious users can exploit it to destroy or modify your database.

Example: I want to allow users to retrieve widgets by name:
- I have the line: ```val userInput = StdIn.readLine()```
- I get the user's widgets by name from the database with the following SQL:
- ```val sqlStatement = s"SELECT * FROM widgets WHERE name = '${userInput}';"```
- If the user is being nice and actually giving me the names of widgets, this is fine
- The problem occurs if the user instead inputs something like:
- ``` ';DROP TABLE widgets;SELECT 'sql injection >:) ```
- your SQL statement becomes:
- "SELECT * FROM widgets WHERE name = '';DROP TABLE widgets;SELECT 'sql injection >:)';"


### Normalization

Why do we normalize data? to avoid 3 "anomalies":
- update anomaly : we update some data in one part of our database but not everywhere
- creation anomaly : we are unable to create data of one type without also creating data of another type
- deletion anomaly : we are unable to delete data of one type without also deleting data of another type

One short way of describing the motivation for normalization is that our goal is to represent each
piece of data *exactly once*.  So, the title of each book should show up exactly once, the name of each
author should show up exactly once, the username for an account should show up exactly once...

We've left our book, appuser, note tables not quite normalized, so we can see examples there.  How many times does Stephen King appear in our database? (3 times, associated with 3 of his books: Carrie, The Stand, The Dark Tower ).  This means every time we update the name of Stephen King, we have to update it in all 3 places (this is more expensive and opens us up to update anomalies).  Also, what happens if we delete the books The Dark Tower, Carrie, and The Stand from our database? We delete Stephen King from our DB as well (deletion anomaly).  Finally, suppose we want to add another author to our DB: Margaret Atwood.  We're unable to just add Margaret Atwood, instead we need to add one of her books alongside her (creation anomaly).

Normalization is defined in multiple, cumulative "normal forms".  We're going to discuss up to 3rd Normal Form, which is what we should aim for in pj0.  Normalization is a little mathy and has some definitions involved:
- Candidate Key : any minimal column or set of columns that uniquely identifies every record in the table
  - The really technical way to talk about this is functional dependency.  Every record should be functionally dependent on the candidate key.  That just means, if we know the candidate key, we know the rest of the record.  Everything is functionally dependent on the id.  Some other examples: country is functionally dependent on state.  If we a record has the value "Virginia" for its State, then we know it has the value "USA" for its Country.
- Primary Key : the candidate key that we choose to use to actually uniquely identify every record in the table
  - Most of the time, we create a SERIAL id field and use it as our primary key.
  - the primary key constraint in postgres just marks a field as unique and not null.
- Composite Key : candidate key consisting of more than 1 column.  Good to know, rarely used as primary key.
- Foreign Key : a foreign key is a reference to a primary key, used to link records together across tables with joins.  Normalization tends to split your data across many tables.  Foreign keys are useful to put it back together when necessary.

Let's start with 1st Normal Form : "the key"
- Every record in the relation must be unique (another way to say it: there must be a primary key)
- Every data value must be atomic, so we can't have columns that are lists
Then we have 2nd Normal Form : "the whole key"
- 2nd Normal Form is only really relevant if we have composite keys (candidate keys with >1 column)
- If there are no composite keys, every relation in 1NF is also in 2NF
- Any column or set of columns that depends on a composite key must depend on the entirety of that
  composite key, not just a part
- we also say "no partial dependencies"
Then, we have 3rd Normal Form : "nothing but the key"
- All columns depend only on candidate keys.  No non-candidate-key columns depend on each other.
- no dependencies except dependencies on keys

There are normal forms past 3NF.  Also see https://en.wikipedia.org/wiki/Database_normalization#Example_of_a_step_by_step_normalization

### Transactions

Every interaction we have with the database takes place as part of a database transaction.  All of these are transactions are stored in the DB log before they are carried out.

Transaction in RDBMSs are controlled using TCL (Transaction Control Language), but we always are using transactions even if we don't control them with TCL.  The TCL keywords are BEGIN to start a transaction, COMMIT to end a transaction, and SAVEPOINT and ROLLBACK for fine-grained control of transaction checkpointing and reverting.

The default transaction behaviour in Postgres is to make every statement a transaction.

Why does this matter? Transactions in an RDBMS have ACID properties:
- Atomic : Your transaction is a single unit.  It either entirely succeeds or entirely fails.  If we're transferring money between bank accounts, we want the entire tranfer to succeed or the entire transfer to fail, we don't want to have half-transfers occur (money mysteriously appearing or disappearing)
- Consistent : Transactions take your database from one valid state to another.  Constraints are checked in each transaction to ensure the database remains valid.
- Isolated : Transactions occurring simultaneously don't interfere with each other (to some extent).  Isolation on RDBMSs is specified with "isolation levels".  This lets us adjust the classic multithreading speed / safety tradeoff:
  - READ UNCOMMITTED : transactions can read data modified inside another transaction.  These changes may later be rolled back, which can cause problems.  Reading data from an ongoing transaction is called a "dirty read".  Maximum speed, essentially no safety.
  - READ COMMITTED : transactions can read data modified by another transaction once that transaction is complete.  These changes won't be rolled back, but values may change in the middle of a transaction.  Called a "nonrepeatable read".
  - REPEATABLE READ : transactions will not read data modified by another transaction while in progress, BUT they can read new data inserted by another transaction.  This is called a "phantom read".  Repeatable read is kind of a strange isolation level, but it makes sense for some use cases.
  - SERIALIZABLE : transactions will takes place as if they took place in serial, meaning one after the other with no interference.  Maximum safety, substantial loss in speed in some cases.
  - Under the hood, all of this is achieved by locking relevant records/tables while transactions are ongoing.
- Durable : ACID properties and data are preserved even when a catastrophe occurs.  If your postgresql server loses power mid-transaction, it will ensure ACID properties when it reboots.  This is enabled by the transaction log.

There is an alternative NoSQL-ish acronym that's even more contrived, BASE:
BASE focuses more on availabilty and speed in data store across a cluster, in comparison to ACID's focus on safety.
- Basically Available : database operations remain available in the case of failures, though those operations may be overwritten or reverted later.
- Soft State : across the machines in the data store, data may not be consistent, instead different machines may have different data that must be later reconciled.
- Eventual Consistency : Given enough time without new operations, data stored across machines in the cluster will become consistent

We care more about ACID than BASE, and understanding the important things about BASE we get from the CAP Theorem:
CAP : Consistency, Availability, and Partition Tolerance:
- Consistency: This consistency is *not* the consistency in ACID, instead it is having consistent data across machines in a cluster.  If all the machines in the cluster agree on the value for some piece of data, our data is consistent.
  - technical: every read receives the most recent write or an error
- Availability: basic operations are available, users can read/write/CRUD.
  - technical: every request receives a non-error response, without guaranteeing it is the most recent write
- Partition Tolerance: The system continues to operate despite network failure.  It tolerates network partitions (breaking apart the network).
  - Ultimately, we *must* guarantee partition tolerance, because network failures are a fact of life.

The CAP Theorem applies to data stores (including RDBMSs) that run on a cluster.

The CAP Theorem says that we can only guarantee 2 out of the 3.  In practice, this means that we must choose to guarantee Partition Tolerance and one of either Consistency or Availability.
If we choose Availability, then our disconnected machines will attempt to fulfill basic requests even if that might lead to inconsistent state across the (temporarily disconnected) cluster.
If we choose Consistency, then our disconnected machines may return errors instead of fulfilling basic requests, in order to ensure they remain consistent across the (temporarily disconnected) cluster.
















