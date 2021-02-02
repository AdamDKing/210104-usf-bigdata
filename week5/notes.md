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



