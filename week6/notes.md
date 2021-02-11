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

### Streaming in Spark

Handling streaming data means that we write data processing pipelines, then those processes operate on data being produced/collected in near-real-time.  Instead of aggregating our 20TB of data over the week and running a Spark job on the weekend to analyze it, we write a streaming job and run the Spark job to analyze the data as it is produced/collected.

In practice, our streaming with Spark will be creating micro-batches under the hood.  So Spark will separate the incoming stream into small chunks for processing.  This is why it's near-real-time instead of real-time.

Spark actually provides two different approaches if we want to handle streaming data.  These are *spark streaming* and *structured streaming*.  Spark streaming is built on core Spark and processes data using a DStream -- a streaming RDD.  Structured streaming is part of Spark SQL and processes data using streaming dataframes/datasets.  These two different parts of the API don't really interfere with each other -- they aren't related under the hood except they're both built using RDDs.  Typically Structured streaming is much easier to use if you're already using Spark SQL.  Typically Spark Streaming allows more fine-grained control over stream processing.  My plan is to just use Structured Streaming.

We've been using structured streaming for Twitter data written to file, all it takes (most of the time) to transform a static analysis to a streaming analysis in Spark SQL is specifying that you're reading a stream + specifying some options.  Structured Streaming essentially just adds records to the bottom of your DataFrame/DataSet as new data comes in.  Your processing operates on these new records as they appear.

#### Output and Format

When we're specifying a stream, we need to specify output and format on that stream.  Format is how the results are delivered to us.  Thus far we've just used "console" to get them appearing on the screen, but we can write to file.

Output mode is an option that lets us specify how we update our output based on the new data.  There are 3 options here:
- complete : the entire result will be written out.  We saw this with streaming updating twitter handle counts
- append : only new records produced based on the recent data will be written out.  We saw this with streaming the locations + text of tweets to the console
- update : only new + updated records will be written out.  This will include new records, similar to append, but it will also include older records if those records have changed.  We *could* use this with our twitter handle count, then we would get twitter handles + counts in the output only when those counts went up.

For Format, we specify a *sink*, which is just a destination for our streaming data:
- file : we can write to file, using JSON, CSV, parquet, ... (we'll see some parquet this week)
- console : write output to console for debugging
- foreach : this runs arbitrary computations on the data as it is produced.
- kafka : write output to a kafka stream.  We'll talk Apache Kafka next week!

### Parquet

Apache Parquet is a file format used with Apache products + beyond!  It's just a file format, so we can use it in our code (writing + reading parquet files) the same way we use .csv, or .json.  The parquet files on disk will look quite different, but we don't need to worry about those differences in our code.

Parquet files on disk will be stored as files in a .parquet dir.  The contents are both encoded and compressed, so opening them with a text editor won't go very well.  We can see a bit of info about the contents -- Parquet is a *columnar* storage format, meaning it stores columns in order on disk instead of storing rows in order on disk.  In a .csv file, all the info about Sam Jones is stored in the same location in the file (one row).  In parquet, all that information is spread out.  Instead, all the SSNs are grouped together in parquet, and all the first_names, and all the ages, ...

This columnar storage, *and* the encoding + compression involved in parquet files makes parquet a very good format for some uses, and a very poor format for other uses.  Parquet is very good at storing data efficiently on disk (takes up much less space than a csv file), and parquet is very good for rapid retrieval of data, including retrieving data based on some condition.  Parquet readers can do something like "querying" parquet files for data.  In exchange for these advantages, Parquet is time-consuming to write and *very* difficult to edit.  Most often the answer to "how do we change the information in this parquet file?" is, "you don't".  We use parquet when we have a large amount of data we want to write once and then read repeatedly.  We definitely don't use parquet when we have data that must be updated in the future.

The columnar format of Parquet lets parquet use a few tricks for efficiently storing data.  There are more than the ones we'll mention here:
- Dictionary Encoding:  If a column has low cardinality (relatively small number of unique values), parquet will encode those values using a dictioanry to save space.  For example:
  - Gryffindor, Gryffindor, Hufflepuff, Gryffindor, Slytherin, Slytherin, Ravenclaw, Gryffindor, ...
  - 1, 1, 2, 1, 3, 3, 4, 1, ...
  - Instead of taking up a lot of space storing the same value repeatedly, parquet will store a number of bytes repeatedly and use a dictionary to keep the actual values associated with those bytes. 1: Gryffindor, 2: Hufflepuff, 3: Slytherin, 4: Ravenclaw.  Instead of storing the ~144 bit string Gryffindor 100000 times, parquet stores the ~2 bit number 1 100000 times.
- Run Length Encoding (RLE): If a column has many repeated values that occur in a row, parquet will encode the "run" of values instead of each individual value.  For example:
  - Gryffindor, Gryffindor, Gryffindor, Gryffindor, Hufflepuff, Hufflepuff, Hufflepuff, Ravenclaw, Ravenclaw, ...
  - Gryffindor 4, Hufflepuff 3, Ravenclaw 2, ...
  - space saving here is quite straightforward.  RLE will only happen on columns that are well-suited for it.
- Bit packing: Parquet has data types for 32bit, 64bit, and 96bit integers (deprecated), but it will "bit pack" smaller values.  Small integers won't take up their full 32 bits on disk, instead multiple smaller values will be packed into that space.



