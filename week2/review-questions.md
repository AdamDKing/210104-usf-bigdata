- What is SQL?
- What is a relational database management system?
- What is a database?
- What are the sublanguages of SQL?
- What is cardinality?
  - > https://en.wikipedia.org/wiki/Cardinality_(data_modeling)
- What is a candidate key?
- What is referential integrity?
- What are primary keys? Foreign keys?
- What are some of the different constraints on columns?
- What is an entity relation diagram?
- What are the differences between WHERE vs HAVING?
- What are the differences between GROUP BY and ORDER BY?
- What does LIKE do?
- How do I use sub queries?
- How does BETWEEN work? 
- What is the order of operations in an SQL statement?
  - > The order of a SELECT, FROM, WHERE, GROUP BY, HAVING, ORDER BY query is FROM, WHERE, GROUP BY, HAVING, ORDER BY, SELECT.  The columns are actually SELECTed at the end, despite SELECT coming first.
- What is the difference between an aggregate function and a scalar function?
- What are examples of aggregate and scalar functions?
- What are the different joins in SQL?
  - > We have INNER, OUTER LEFT, OUTER RIGHT, and OUTER FULL joins.  INNER joins only include records with a match in the output (so records where the join condition is true).  OUTER joins includes records with a match *and* all unmatched records from the left, right, or both tables.
  - > The part of the JOIN ```ON album.artist_id = artist.artist_id``` is the join condition.  WHen the join condition is true for a pair of records, those records are matched together in the output.  90+% of the time, the join condition will be equality based on a foreign key relationship, but you can have various strange join conditions.  A join condition of just TRUE will include all pairs of records in the output and is called a CROSS JOIN.
- What are the different set operations in SQL? Which set operations support duplicates?
  - > UNION, INTERSECT, UNION ALL are good to know.  UNION combines two resultsets removing duplicates, INTERSECT produces results that appear in both of two result sets, and UNION ALL combines two resultsets including duplicates.
- What is the difference between joins and set operations?
- How can I create an alias in SQL? 
- What does the AS keyword do in a query? 
- What is a transaction?
- What are the properties of a transaction?
- What are the transaction isolation levels and what do they prevent?
- What are dirty reads, non repeatable reads, and phantom reads?
- What is normalization?
- What are the requirements for different levels of normalization?
- What is a view? a materialized view?

- What is a dao?
- What is the danger of putting values directly into our queries?

- How do we specify dependencies using sbt?
- What is a port number?
- What's the default port for Postgres?
- What is multiplicity?  Examples of 1-to-1, 1-to-N, N-to-N?
- What is an Index?  
- What advantages does creating an Index give us? Disadvantages?
- What is CRUD?

- What is the CAP Theorem?
- What does CAP mean for our distributed data stores when they have network problems?
- What does it mean that an operation or transaction on a data store is atomic?
- What does ACID stand for?
- What does BASE stand for?


We didn't get to the following, so look into them if you like but they wont be on assessments:
- What is a database in Mongo?
- What is a collection?
- What is a document? 
- What rules does Mongo enforce about the structure of documents inside a collection?
- What is JSON? BSON?
- What is a distributed application?  A distributed data store?
- What is High Availability? How is it achieved in Mongo?
- What is Scalability? How is it achieved in Mongo?
- Explain replica sets and sharding

