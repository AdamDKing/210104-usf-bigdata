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

    helloDemo(sc)

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
    val sampledData = distributedData.sample(false, 0.5, 10L)

    val cachedData = sampledData.cache()

    //Up until this point, we've been producing RDDs
    // RDDs are lazy, so while we've specified them, nothing will actually run.
    // We cause RDDs to run when we perform an *action* on them.  Actions
    // make use of the results of RDDs, which means the RDDs actually run.

    //Let's use collect
    println(cachedData.collect().mkString(" "))



  }
}