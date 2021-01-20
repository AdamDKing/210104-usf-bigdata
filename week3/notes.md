### MapReduce

MapReduce is an algorithm and data processing platform that came out of google in the early 2000s.  Google needed large clusters of machines to run their search algorithms (and other things) on large amounts of data.  They ran into classic cluster computing problems, and developed MapReduce as a way to run many different algorithms on a cluster without rewriting much of the same logic.  Every large set of calculations ran on a cluster needs to be distribute work among nodes (computers) in the cluster, needs to be able to handle failure and slow machines, and needs to aggregate the results of its processing from across the cluster at the end of the job.  MapReduce provides the above functionality, so that developers can just focus on the actualy processing they need done.  Developers using MapReduce write custom Map tasks and custom Reduce tasks to specify the content of their job.

Hadoop began as an implementation of google's MapReduce, then grew into a general big data processing platform.  Many different projects were built alongside / on top of Hadoop in order to fulfill specific needs.  Also, Hadoop itself evolved.  Version 2 of Hadoop uses YARN (Yet Another Resource Negotiator) to run jobs and can handles tasks outside of traditional MapReduce.

The core of Hadoop v2+ that we should start with is HDFS (Hadoop Distributed FileSystem) and YARN (Yet Another Resource Negotiator).  Both of these tools run on a cluster, which means they run as daemons on multiple machines that are networked together.  HDFS is data storage spread across a cluster, and YARN is data processing spread across a cluster.  It is still possible to run MapReduce jobs using YARN, but YARN supports other data processing as well.  Notably, for us, Apache Spark jobs can run on YARN.

A daemon is just a long-running process.  HDFS and YARN both involve multiple different daemons running on different machines.  Typically, applications running on a cluster will have one or more master daemons responsible for coordinating work and many worker daemons responsible for actually doing the work.

### 

