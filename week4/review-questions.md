### Hive
- What is Hive?
- Where is the default location of Hive's data in HDFS?
- What is an External table?
- What is a managed table?
- What is a Hive partition?
- Provide an example of a good column or set of columns to partition on.
- What's the benefit of partitioning?
- What does a partitioned table look like in HDFS?
- What is a Hive bucket?
- What does it mean to have data skew and why does this matter when bucketing?
- What does a bucketed table look like in HDFS?
- What is the Hive metastore?
- What is beeline?

### Hive Syntax questions: How do we....
- create a table?
- load data into a table?
- query data in a table?
- filter the records from a query?
- group records and find the count in each group?
- write the output of a query to HDFS?
- specify we're reading from a csv file?

### Spark : Cluster Computing with Working Sets
- What does Cluster Computing refer to?
- What is a Working Set?
- What does RDD stand for?
- What does it mean when we say an RDD is a collection of objects partitioned across a set of machines?
- Why do we say that MapReduce has an acyclic data flow?
- Explain the deficiency in using Hive for interactive analysis on datasets.  How does Spark alleviate this problem?
- What is the *lineage* of an RDD?
- RDDs are lazy and ephemeral.  What does this mean?
- What are the 4 ways provided to construct an RDD?
- What does it mean to transform an RDD?
- What does it mean to cache an RDD?
- What does it mean to perform a parallel operation on an RDD?
- Why does Spark need special tools for shared variables, instead of just declaring, for instance, var counter=0?
- What is a broadcast variable?
- What is an accumulator?
- How would the Logistic Regression example function differently if we left off the call to .cache() on the points parsed from file?

- Be comfortable enough with the following terms to recognize them:
- RDD
- Action
- Transformation
- lineage
- cache
- lazy evaluation
- broadcast variable
- accumulator

- What are some transformations available on an RDD?
- What are some actions available on an RDD?
- What is a shuffle in Spark?
- What's the difference in output between MapReduce wordcount in Hadoop and .map followed by .reduceByKey in Spark?
- Why should we be careful about using accumulators outside of an action?
- What is the closure of a task?  Can we use variables in a closure?
- How can we see the lineage of an RDD?
