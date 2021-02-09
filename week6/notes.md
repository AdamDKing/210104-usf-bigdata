## Spark SQL

Spark SQL is built on top of Spark, but is still part of the Spark project.  Spark, out of the box, includes some tools that build on the functionality of base RDDs.  All of the work that we do with Spark SQL will ultimately be done using RDDs under the hood.  At any point when we're working with Spark SQL, we can "drop down" and work directly with the underlying RDDs, if we want.  Spark SQL is an API that is sometimes/often easier to use than working with RDDs directly.

A quick side note: In addition to Spark SQL, Spark ships with Spark Streaming (for streaming data), MLib (for machine learning workloads) and GraphX (for graph computation).  These are built in + built on top of RDDs, similar to Spark SQL.

Spark SQL lets us execute Spark jobs by writing SQL, but it often does more than that.  Originally, the motivation for Spark SQL was similar to the motivation for Hive (allow users who knew SQL but didn't know scala to run Spark jobs).  This SQL -> Spark functionality is still supported.  In addition to this functionality, during the lifetime of Spark 1 the DataFrame and DataSet APIs were introduced.  These two APIs were "unified" in Spark 2.  Both DataFrames and DataSets provide a different way of working with Spark SQL.  These APIs are similar to DataFrames in R and Python pandas.  The motivation here was first to make an API that's easier to work with than RDDs, but also to make an API similar to the APIs data scientists were already using.

The way Spark SQL works is using a tool called the *Catalyst Optimizer*.  Catalyst takes our SQL query / DataFrame processing / DataSet processing and transforms it into an appropriate logical plan -> physical plan -> execution with RDDs.  Catalyst makes an efficient logical plan using the information it has about our tables + views.  Then it produces multiple candidate physical plans and selects among them using a *Cost Model* we can tune.  Tuning catalyst and understanding what it does can provide major benefits.  Using Spark SQL can make it easier to write efficient data processing pipelines, since Catalyst will optimize the logical plan.

We have three ways of using Spark SQL: SQL queries, DataFrames, and DataSets.  DataFrames are like tables stored in SQL.  They contain rows/records and have columns.  The columns are stored by name, with datatypes.  DataSets we can think of as between DataFrames and RDDs.  DataSets contain strongly typed data, we use case classes for them in Scala.  Both of these are distributed collections, and in Scala they are quite similar.

The actual types we'll be using in Scala will look like: DataSet\[Person\] or DataSet\[Tweet\].  DataFrames in Scala are just DataSet\[Row\].  DataFrames and DataSets used to be two entirely separate parts of the Spark SQL API, but they were unified in this was in Spark 2.  

ex: df("tweet_author") or df("mycolumn") <- this is one way we access columns in a dataframe, by name
for DataSets, we just have Tweet objects, instead of named columns.

Why the difference?  First, there's a historical difference.  DataFrames were introduced in Spark 1.3, while DataSets were introduced in Spark 1.6.  The APIs weren't unified until Spark 2.  Second, coming from a SQL background DataFrames will be more comfortable, while DataSets will be more similar to RDDs.  Third, the catalyst optimizer has different infromation in each case, and so using DataFrames vs DataSets *can* impact efficiency, but we won't worry about this too much.

### SparkContext, SqlContext, HiveContext, SparkSession

We've seen SparkContext, this is how we make + use RDDs.  Spark SQL before Spark 2 used a SQLContext for SQL, DataFrames, DataSets.  It also used a HiveContext for HQL, DataFrames, DataSets.  There three contexts were brought together in Spark 2 as the SparkSession.  From here on out we'll use SparkSession to create DataFrames/DataSets.  At any point we can access the underlying SparkContext if we need to.