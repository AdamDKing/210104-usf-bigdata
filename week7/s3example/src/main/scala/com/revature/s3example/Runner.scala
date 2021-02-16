package com.revature.s3example

import org.apache.spark.sql.SparkSession

object Runner {
  def main(args: Array[String]): Unit = {
    //Let's write a spark application to read from S3, do some processing, and write to S3
    // This is meant to run on EMR, so it will *not* work locally.
    // The s3 paths that we use require deps we're not including
    //If we wanted to use S3 files locally, we'd use s3a + configure access to s3 from our machine

    val spark = SparkSession.builder()
      .appName("S3 example")
      .getOrCreate()

    import spark.implicits._

    //create a dataframe from file, but the file path is on S3
    spark.read.json("s3://bigdata-pj2-teamadam/data/tweets.json")
      .select($"data.text")
      .write
      .text("s3://bigdata-pj2-teamadam/data/tweets-content.txt")
      //we may need to fiddle with permissions to make this work ^
      // We can give the machines in our EMR cluster "roles" in AWS
      // Writing to s3 is a permission associated with some roles.  We can
      // provide that permission if it isn't already present

  }
}