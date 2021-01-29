package com.revature.hellospark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
  * Runner is our driver application that describes and kicks off the Spark job.
  * This is the driver program mentioned in spark docs.  It can communicate with
  * a Spark cluster -- the location of the cluster should be set in its SparkConf
  */
object Runner {
  def main(args: Array[String]): Unit = {
    //Set up a SparkConf here
    // This should almost certainly take command line arguments, but we'll hardcode for now
    // we're only setting two properties on the conf, the name and the master
    // Appname is straightforward
    // Master is the spark cluster we're communicating with.  We can pass setMaster the URL for a
    // cluster, then our job will be submitted to that cluster.  We can also specify that the job should
    // run locally.  We do this by setting the master to local[n], where n is the number of threads we
    // want Spark to use.
    val conf = new SparkConf().setAppName("hello-spark").setMaster("local[2]")
    //Build a SparkContext using that conf
    // SparkContext is the entrypoint for Spark functionality.
    // more specifically, SparkContext is the entrypoint for working with RDDs, the central abstraction
    // we'll see later on other ways of working with Spark, but those will be built on top of RDDs
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR") // setting the log level to ERROR means that log messages below
    // ERROR won't appear in the output
    // log levels (kind of standard): OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL

    helloDemo(sc)

    fileDemo(sc)

    closureDemo(sc)

    mapReduceWordCountDemo(sc)

  }

  def mapReduceWordCountDemo(sc: SparkContext) = {
    // we'll just quickly write the spark version of our mapreduce word count
    // MapReduce sorts during the shuffle and sort
    // shuffles in Spark don't necessarily sort, so we'll have to do it manually to get the same output

    val mrwcRdd = sc.textFile("somelines.txt")
      .flatMap(_.split("\\s+")) 
      //split lines on spaces and flatmap to words
      .filter(_.length() > 0)
      .map((_, 1)) 
      //map words to key, value pairs: (word, 1)
      .reduceByKey(_ + _)
      // a shuffle happens when we reduce by key!
      .sortByKey()

    // all the above are transformations, so nothing has happened yet
    // let's get some results and print them out, take(10) is the action:
    mrwcRdd.take(10).foreach(println)

    println(mrwcRdd.toDebugString)

    
  }

  def closureDemo(sc: SparkContext) = {
    // operations are broken up into tasks that run on the cluster
    // these tasks rely on values in memory
    // the *closure* of a task is all the variables and methods that the executor 
    // (the thing that runs the task) needs to complete the task.  Spark handles this for us
    // but we should know about it.
    // What we write as one value in our driver application here
    // become multiple values in memory distributed across the cluster

    //4 examples, using good and bad methods of having shared variables
    val listRdd = sc.parallelize(List(1,3,5,7,9), 3)

    //compute a sum, naive (bad) version:
    var sum = 0;
    //foreach is an action
    listRdd.foreach(sum += _)

    //behaviour is undefined, *may* work locally?
    println(s"bad sum is: $sum")

    //all of the addition to sum happened in tasks spread out across the cluster, no way
    // to retrieve/combine those values

    //compute a sum, good version:
    val sumAccumulator = sc.longAccumulator("goodsum")

    listRdd.foreach(sumAccumulator.add(_))

    println(s"good sum is: ${sumAccumulator.value}")

    //The next 2 examples use broadcast.  Not using a broadcast variable when you ought to
    // isn't catastrophic, it will just make your jobs slower.  When exactly to use a broadcast variable
    // is a bit of a judgment call, it depends on the job and the cluster.

    //Example: filter a list based on a cutoff value shared across the cluter

    val cutoff = 4

    println(s"OK List: ${listRdd.filter(_ > cutoff).collect().mkString(" ")}")

    //the above works just fine.  our cutoff val is copied and sent along with *every* task

    val cutoffBroadcast = sc.broadcast(4)

    println(s"good List: ${listRdd.filter(_ > cutoffBroadcast.value).collect().mkString(" ")}")

    //good list and OK list are going to be the same
    // the major advantage of broadcast variables appears when you have a *large* read only value.

  }

  def fileDemo(sc: SparkContext) = {
    //another way to create an RDD: from file.  second arg is suggested minimum partitions
    val distributedFile = sc.textFile("somelines.txt", 3)

    val lineLengths = distributedFile.map(_.length())

    //reduce is another action.  Use to take all the values in the RDD and reduce them to a single
    // value.  Reduce is a higher order function, meaning it takes a function to describe
    // this reduction
    println(s"Total Line Lengths: ${lineLengths.reduce(_ + _)}")
  }

  def helloDemo(sc: SparkContext) = {
    // From the paper, 4 ways of creating RDDs:
    // 1) from file, 2) from Scala collection,
    //   3) from transforming prior RDD, 4) changing persistence of RDD
    val data = List(1,2,3,4,5,6)

    //parallelize our Scala collection:
    val distributedData = sc.parallelize(data)

    //transform an existing RDD
    val dataPlusTwo = distributedData.map(_ + 2)

    //another transform
    val sampledData = distributedData.sample(false, 0.5, 11L)

    val cachedData = sampledData.cache()

    //Up until this point, we've been producing RDDs
    // RDDs are lazy, so while we've specified them, nothing will actually run.
    // We cause RDDs to run when we perform an *action* on them.  Actions
    // make use of the results of RDDs, which means the RDDs actually run.

    //Let's use collect
    println("Collect output:")
    println(cachedData.collect().mkString(" "))

    println("hello demo debugstring:")
    println(cachedData.toDebugString)

  }
}