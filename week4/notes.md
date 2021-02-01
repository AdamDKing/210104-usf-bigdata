### Hive

Hive is a popular tool that allows for SQL-like querying on big data.  Hive was originally built as a way to run MapReduce jobs by writing SQL, but the underlying execution engine has since changed (we're still using Hive on MR because we're learning MR).  Hive allows us to query data that it manages and stores in an internal data warehouse (managed tables).  Hive also allows us to query data stored outside of Hive (external tables).  The idea here is that some datasets we will be frequently querying, so we store those in Hive + gain safety and efficiency because Hive controls them.  Other datasets, like a multi-TB output of a MapReduce in HDFS, we want to query once or twice, and we can just do that querying that output as an external table.  Hive is schema-on-read so it applies the table structure when reading data.  Typically Hive data is stored in HDFS as csv, tsv files in a directory.  Hive's warehouse (by default) is located at /user/hive/warehouse on HDFS.

#### Hive Metastore

Hive stores all of its metadata, for both types of tables, in an RDBMS called the metastore.  Metadata goes in the metastore.  The metastore contains columns, tablenames, db names, ... We're using derby since it's the default for standalone, but a proper installation of Hive would have the metastore running on another machine.

#### Hive Partitions

A typical Hive table might have TBs of data, anything we can do to speed up access to this data is good.  Partitioning is one such tool.  What partitioning does is it splits up the data in our table into multiple smaller datasets, based on the value of a column or set of columns.  We choose the column(s) to partition on, and selecting those columns appropriately can lead to dramatically increased performance.  In practice you almost always want to partition your data.  

One easy example of good partitioning (in many cases) is to partition by time.  This has a few useful features.  You can select the appropriate resolution to get reasonably sized partitions: if I have 300TB of data over 5 years, then I might partition by week + year.  This would get me 300TB / 5*52 ~= 1TB per partition, which is a nice (rule of thumb) size.  Also, partitioning by time makes it very easy to add new data, because all new data happens in a new partition.  Finally, partitioning by time is nice because many queries subset time.  Regardless of what question one is asking, it's common to look at the past quarter, past year, etc.  Querying the past quarter with our dataset partitioned by year + week allows us to query only the ~13 most recent partitions (~15TB) instead of the whole dataset (300TB).

#### Hive Bucketing

Another tool that subsets our data is bucketing.  Bucketing is a little harder to use correctly and a little less generally useful than partitioning.  Bucketing is used to create subsets of the data, based on a column or set of columns, but bucketing isn't meant to divide the data into meaningful subsets.  Instead, if we bucket a dataset into 4 buckets based on age, each bucket will have around 1/4 of the ages in it, and all records with the associated ages.  Typically, we want to bucket on a high cardinality column that isn't particularly important for our analysis.  When we partition, each partition is a piece of our total dataset with some specific feature (it occured in this week, it's associated with a specific state, ...).  When we bucket, we expect the buckets to be like a microcosm of our dataset, not specifically different from the whole.

### Spark

Framework for big/fast data processing.  Notable for extensive use of in-memory processing: Spark can keep datasets in memory across a cluster so subsequent analysis is much faster.  Contrast this to Hadoop, where each MapReduce job reads from / writes to HDFS.  Spark is largely the successor to Hadoop + the Hadoop Ecosystem.  There are still reasons to use other tools in the Hadoop ecosystem, but Spark is powerful + flexible.  One of the reasons Spark is used over Hadoop is its flexibility -- out of the box Spark can handle tasks that required specialized tools in Hadoop.

Running Spark:
- local mode: this runs Spark as a standalone process, useful for testing + debugging, similar to running Hadoop as a standalone process.
- cluster mode: this runs on Spark on a cluster, always used in production:
  - Mesos was the original cluster manager and is still supported
  - Spark can run on kubernetes (K8s) clusters
  - YARN.  Spark can run on a YARN cluster, this is the option we'll use.  There are actually two ways we can make this happen.  We can write a Driver program for Spark that communicates with an ApplicationMaster running our job on the cluster.  We can also submit our job to the cluster and have our Driver program run inside the ApplicationMaster.  This is "client" (Adam had this wrong yesterday) vs "cluster" mode and we'll see it in the wild.

#### RDDs : Resilient Distributed Datasets

RDDs are the central abstraction of Spark.  An RDD is a read-only collection of objects partitioned across a set of machines, that can be rebuilt if a partition is lost.  We should think of "partition" here as like an inputsplit for MapReduce.  In fact, you'll sometime see RDD partitions referred to as "splits".  Each partition in the RDD serves as the chunk of data for one task.  The Resilience of RDDs in enabled by their *lineage*.  Each RDD contains the instructions for recomputing itself from prior data.  This lets Spark recompute individual partitions if they are lost.  If our data processing reads from disk, does 3 transformations (3 maps) and then writes to disk, our RDD will contain that information.  If some of our partitions are lost after the second transformation, Spark has the information in that lineage to recompute them from prior steps stored in memory (if they exist), or from disk.  A major advantage here is that Spark can recompute *only* the partitions that are lost.  So if 1/10th of our job fails, only that 1/10th of our job is recomputed.

RDDs are lazy and ephemeral by default.  Lazy means that they are not immediately executed when we create them.  RDDs are only actually executed on the cluster when we call a *action* on them.  Some of the methods we can call on RDDs are *transformations* which are lazy (map, filter), others are *actions* which cause actual computation to occur (reduce, collect, foreach).  Anything that is "lazy" isn't evaluated until absolutely necessary.  Ephemeral just means temporary -- RDDs are removed from memory after they are used.  This is standard behaviour, most everything we store in memory is removed after we're done with it.  We mention this specifically because we have the option to .cache RDDs, saving them in memory to speed up future processing.  We'll actually have many options for caching RDDs, not just in memory.

#### Actions and Transformations

Actions do something with the result of an RDD, and so cause its execution.  Transformations produce an RDD from another RDD and so don't cause execution.  If we never call an action on an RDD, none of the processing described for that RDD will run.

#### Shared Variables

RDDs exist across the cluster as some number of partitions.  Remember partitions are like splits in MapReduce.  All the transformations performed on an RDD occur as tasks happening on a partition.  If we have 10 partitions in our RDD, spread out across the cluster, then a transformation on that RDD will involve 10 tasks, spread out across the cluster.

Some kinds of transformations require data from multiple partitions.  These transformations cause a "shuffle", which we'll care deeply about later but we'll just mention for now.

So, these tasks running on partitions distributed across a cluster sometimes need to have some information shared between them.  The first way that information is shared with tasks is by just including including that information in the *closure* of the task.  This sounds a little fancy but all it means is that the information (vals and vars) we declare in the scope of tasks is passed along with those tasks across the cluster.  This is simple functionality, just the things we define in the scope of a task are included.  The only real problems here are (1) we're making copies of this data that we send with every single task, which can be expensive and (2) the data sent in a closure cannot be effectively edited across the cluster.

There are 2 kinds of shared variables in Spark: Broadcast variables and Accumulators: 
- Broadcast variables solve the first problem.  If we broadcast a value, that value isn't copied with every task, instead it's copied to every *machine*.  For some jobs, this can dramatically speed up processing.
- Accumulators solve the second problem.  Accumulators let tasks across the cluster "add to" the global accumulator.  Accumulators don't actually have to be numbers, but they do need to have some concept of addition that tasks across the cluster can use to aggregate their value.  The reason we have accumulators using this concept of addition instead of just being mutable is to avoid multithreading woes.  This way we don't need to worry about the exact order tasks contribute to the accumulator.  The addition for accumulators should be commutative (1 + 2 = 2 + 1).

A final note on accumulators : to use accumulators correctly, they should:
- option 1: be added to only in actions, not in transformations
- option 2: have an idempotent addition method

The reason for the above rules is that RDDs are fault tolerant and can be recomputed on the fly.  We're not guarateed that a given transformation will only run once.

#### caching + persisting, shared variables, actions + transformations


