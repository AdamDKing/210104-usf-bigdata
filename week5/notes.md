### Week 5 Notes

### Spark Continued:

#### Caching + Persisting

One of the major selling points of Spark is its ability to cache datasets in memory across a cluster.  This enables rapid iterative analysis on the same dataset.  This is useful for algorithms that repeatedly run over the same data, and it's useful for ad hoc data analysis, since the developer can repeatedly and rapidly query a dataset in memory.

We decide when to enable this functionality, by calling .cache() or .persist() on an RDD.  persist is the same as cache, except it lets us specify a "storage level" -- it lets us tune the way we are persisting the RDD.  Persisting an RDD is lazy and doesn't actually cause anything to happen by itself.  Once the RDD is actually computed, it will be saved according to the storage level we provided with persist().  All caching/persisting is a suggestion, if there aren't enough resources then RDDs won't be persisted and will instead be recomputed.

#### Storage Levels
- MEMORY_ONLY : Store RDD partitions as Java objects, in memory.  If it's not possible to do so, recompute partitions.  This is the default, also provided by .cache().
- MEMORY_AND_DISK : Store RDD partitions as Java objects, in memory.  If it's not possible to do so, store them on disk instead.  Caching on disk is slower than caching in memory, but can be faster than recomputing
- DISK_ONLY : Store RDD partitions on disk, not attempting to cache them in memory first.
- MEMORY_ONLY_SER, MEMORY_AND_DISK_SER : The "SER" means "Serialized".  Here we have the same storage strategy as MEMORY_ONLY and MEMORY_AND_DISK, but we also store the RDD partitions serialized.  When we serialize something, we transform it into bytes that can be written to disk/sent over the network and reconstituted later.
  - Storing your partitions serialized will make them use less space, but require more processing power.  This is because the serialized form of your partitions is smaller, but you need to use processing power for the serialization/deserialization process.
- MEMORY_ONLY_2, MEMORY_AND_DISK_2, DISK_ONLY_2 : The "2" means replications.  These storage levels cache your RDD partitions on multiple machines across the cluster.  This can make jobs faster (due to data locality + redundant copies), but obviously uses more resources.

#### Spilling to disk

We'll see this term (you may have seen it in MapReduce output as well).  Spilling to disk just means that your task ran out of space in memory and so wrote some data to disk temporarily.  Every time we spill to disk, we incur disk IO costs, and it makes our tasks and subsequently jobs slower.  We want to avoid spilling to disk in the course of regular processing, and we want to avoid it when caching, in most cases.

#### RDD Lineages, Physical + Logical Execution Plan, Tasks + Stages

Each RDD contains the processes necessary to reocmpute itself.  We can see the *lineage* of the RDD by calling .toDebugString on the RDD.  If we check out the web UI we can se eit there as well.  This is called the *logical* execution plan.  Spark takes that logical execution plan and turns it into a *physical* execution plan, that actually runs on the cluster.  The physical plan is divided into stages and the stages are divided into tasks.  The number of splits in the RDD is constant within a stage, and the series of tasks within a stage runs on each split.

Within a stage, tasks proceed in order on each split.  If we map then filter, each split will independently go through a map task, then the output of that map task will go through a filter task.  Between stages, splits might be totally or partially recombined.  The transition between stages corresponds to operations that require a *shuffle*.  The shuffle is a similar operation to MapReduce shuffle, during a shuffle we transform the dataset as a whole rather than transforming individual records.  Sorts, Joins, ReduceByKey, are the kind of transformations that cause a shuffle.  These operations require operating on the entire dataset, rather than just transforming individual records.

#### Applications, Jobs, Stages, Tasks
- An Application is one call to spark-submit with a .jar file, one program, one SparkContext.  RDDs that are cached in an application can be reused.
- A Job is what happens each time we perform an action.  An application can have 1 or more jobs.  Jobs consist of stages containing tasks
- Each stage is a series of tasks that run on the same data (the same splits).  Jobs are divided up into stages based on how many times we shuffle.  Each shuffle starts a new stage, on the splits produced by that shuffle.  A Job might have 1 or many stages.
- Tasks are transformations like map or filter that operate on single splits.  Tasks exist in order within stages.  The *wide* transformations that cause shuffler also produce tasks.

A quick note on shuffling and caching:  Just like in MapReduce, all data is written to disk before a shuffle occurs.  This means we get some caching "for free" whenever we shuffle, since the shuffle inputs are written to disk.  Spark will skip entire stages if possible by using the data already stored on disk.

### AWS : Amazon Web Services

AWS is one major cloud provider, other major contenders are Google's GCP (Google Cloud Platform) and Microsoft's Azure.  AWS is the largest cloud provider, meaning it's used the most in the wild.

AWS provides services that let us, at the most basic level, rent computers in the cloud.  The cloud is jsut someone else's computer, so when we're renting computers in the cloud, we're really jsut using computers running in a datacenter somewhere.  One of the benefits of using a cloud provider is that we don't have to worry (too much) about the location and setup of the physical servers.

If we just want a server (some bundle of computational resources), AWS provides us with EC2 instances.  EC2 stands for Elastic Compute Cloud.  When we set up an EC2 instance, we decide how much memory, processing power, and disk space we want attached, and corresponding to that, how much we pay AWS for the use of the instance.  This is a bit of a simplification, since AWS sells resources more efficiently than just providing you a fixed amount of memory/processing power.  Notably, EC2 instances are *elastic* which means their resources can grow and shrink over time based on demand/cost.  Fundemental value proposition of the cloud: scale out to meet demand and scale in to reduce costs.

We can have AWS attach disks to our EC2 instances and use those hard disks for storage, but AWS also provides S3 (Simple Storage Service) for storage of files and directories.  S3 is practically infinitely scalable and we can access files stored on S3 through our browsers, from the command line, and programmatically in our scala code.  Storing your data and output on S3 has some advantages compared to storing it on disk, but it can also be expensive.

EC2 and S3 are the basics of AWS, AWS provides many services in addition to those (and provides of configurable settings for your use of S3 and EC2).  We're going to be using EMR (Elastic MapReduce).  EMR is essentially paying Amazon not just for the computational resources in a cluster of machines for big data jobs, but also paying Amazon to install + manage Hadoop on that cluster of machines.  When we spin up an EMR cluster, AWS will spin up some number of EC2 instances in that cluster, network them together, and install + run Hadoop (YARN) on those machines.  While we could use HDFS with EMR, we're going to use S3 instead. 

Many AWS services work similarly, where we pay Amazon not just for resources, but also to install + manage software.  RDS (Relational Database Service) is another example, we can pay Amazon for a managed PostgreSQL server.

## We'll pick up with writing jarfiles for EMR tomorrow morning
