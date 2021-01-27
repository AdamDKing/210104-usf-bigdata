-- This is HQL : Hive Query Language
-- HQL is very similar to SQL, it's built that way.
-- We can still talk about DML and DDL, but Hive is meant to work with big data
-- there are ways to use it to update and insert individual records, but that is not the primary use case

-- Hive offers some of the features that an RDBMS does, with caveats
-- In the first case, Hive support for ACID is incomplete (check the docs for the exact problems)
-- In the second case, Hive only really have control over some of its tables, we call these *managed* tables.
-- A Table in Hive can be *managed*, which means Hive controls that table and can provide some guarantees about its data
-- Or, a table in Hive can be *external*, which means Hive doesn't control the table, and the data within might be wrong
-- or nonexistant.

-- How does Hive possibly have an external table that it doesn't control?  These external tables are *schema on read*, and they
-- contain files in HDFS.  When we try to use an external table, the schema Hive has for that table is applied to the files.

-- A final difference is that we don't have any conception of foreign key relationships in Hive.

-- start by creating the database
CREATE DATABASE student_db;
--make sure you have it
SHOW DATABASES;

--use the created database
USE student_db;

-- create a managed table student:
CREATE TABLE STUDENT (
	ssn STRING,
	first_name STRING,
	last_name STRING,
	age INT,
	state STRING,
	house STRING)
	ROW FORMAT DELIMITED
	FIELDS TERMINATED BY ','
	TBLPROPERTIES("skip.header.line.count"="1");
	
DESCRIBE STUDENT;

--our table is currently empty.
-- we can load local data into our table:
LOAD DATA LOCAL INPATH '/home/adam/student-house.csv' INTO TABLE STUDENT;

SELECT * FROM STUDENT;

--We can check out this csv inside Hive's warehouse, inside the student_db, inside table student.
-- Hive stores all of its data as files in HDFS.  Managed tables are stored in the warehouse.

--adam@DESKTOP-VRM1V1M:~$ hdfs dfs -ls /user/hive/warehouse
--Found 3 items
--drwxrwxr-x   - adam supergroup          0 2021-01-25 11:03 /user/hive/warehouse/adamdb.db
--drwxrwxr-x   - adam supergroup          0 2021-01-27 15:27 /user/hive/warehouse/student_db.db
--drwxrwxr-x   - adam supergroup          0 2021-01-25 11:04 /user/hive/warehouse/testdb2.db
--adam@DESKTOP-VRM1V1M:~$ hdfs dfs -ls /user/hive/warehouse/student_db.db
--Found 1 items
--drwxrwxr-x   - adam supergroup          0 2021-01-27 15:30 /user/hive/warehouse/student_db.db/student
--adam@DESKTOP-VRM1V1M:~$ hdfs dfs -ls /user/hive/warehouse/student_db.db/student
--Found 1 items
---rwxrwxr-x   1 adam supergroup      51070 2021-01-27 15:30 /user/hive/warehouse/student_db.db/student/student-house.csv
--adam@DESKTOP-VRM1V1M:~$

DROP TABLE STUDENT;

--Since Hive controls managed tables, dropping them removes the data.

--adam@DESKTOP-VRM1V1M:~$ hdfs dfs -ls /user/hive/warehouse/student_db.db/student
--ls: `/user/hive/warehouse/student_db.db/student': No such file or directory

-- Create an external version
-- add the EXTERNAL keyword and provide a location for the data in HDFS
-- (since external tables are not managed, they don't go in the warehouse)
-- For this table, Hive will make mydata and store data for the student table there
CREATE EXTERNAL TABLE STUDENT (
	ssn STRING,
	first_name STRING,
	last_name STRING,
	age INT,
	state STRING,
	house STRING)
	ROW FORMAT DELIMITED
	FIELDS TERMINATED BY ','
	LOCATION '/user/adam/mydata'
	TBLPROPERTIES("skip.header.line.count"="1");
	
-- This time we'll load data from HDFS
LOAD DATA INPATH '/user/adam/student/student-house.csv' INTO TABLE STUDENT;

SELECT * FROM STUDENT;

-- if we check hdfs, we see the csv was moved from its original location into the location for the external table.

-- if we DROP TABLE STUDENT now, the file will remain, since Hive doesn't delete the contents of external tables.

-- Just a few example queries:
-- 50 students ordered by last name
SELECT * FROM STUDENT
ORDER BY last_name DESC
LIMIT 50;

-- The above query spawned a MR job under the hood.  You can check it out on your hiveserver2 window

-- get counts by house, save it as output in a .tsv file
INSERT OVERWRITE DIRECTORY '/user/hive/output'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
SELECT house, COUNT(house) FROM STUDENT
GROUP BY house;

--adam@DESKTOP-VRM1V1M:~$ hdfs dfs -ls /user/hive/output
--Found 1 items
---rwxrwxr-x   1 adam supergroup         58 2021-01-27 16:08 /user/hive/output/000000_0
--adam@DESKTOP-VRM1V1M:~$ hdfs dfs -get /user/hive/output/000000_0 .
--adam@DESKTOP-VRM1V1M:~$ head 000000_0
--Gryffindor      248
--Hufflepuff      242
--Ravenclaw       264
--Slytherin       246

-- Display all students in california or virginia
SELECT first_name, last_name, state FROM STUDENT
WHERE UPPER(state) = 'VIRGINIA' OR UPPER(state) = 'CALIFORNIA'; 

--Display first name and ssn for students in Hufflepuff with names beginning with C
SELECT first_name, ssn FROM student
WHERE house='Hufflepuff' AND first_name LIKE 'C%';

-- we could carry on like this, the point is, it's similar to SQL querying

--Display average age by house, rounded to 2 decimal places:
SELECT house, ROUND(AVG(AGE), 2) FROM student
GROUP BY house;

-- we can create new managed tables from queries.  Managed tables are stored in hive's warehouse.
CREATE TABLE student_state
AS SELECT COUNT(*) AS Num_Students, state FROM student
GROUP BY state;

SELECT * FROM student_state;

-- we'll pause it here, we'll pick up with partitioning and bucketing tomorrow. 

