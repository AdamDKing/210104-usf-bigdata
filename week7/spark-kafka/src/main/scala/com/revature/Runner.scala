package com.revature.sparkkafka

import org.apache.spark.sql.SparkSession

object Runner {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Spark Kafka Consumer + Producer")
      .master("local[*]")
      .getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")
    import spark.implicits._

    //read from kafka with readStream
    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "quickstart-events")
      .option("startingOffsets", "earliest")
      .load()

      //key and value for kafka events are binary
      // In order to read them as text, we cast them to string:
      // we'll just get the value:
    // df.selectExpr("CAST(value AS STRING)")
    //   .writeStream
    //   .outputMode("append")
    //   .format("console")
    //   .start()
    //   .awaitTermination()

    //Instead of writing to console, let's write to another kafka topic
    // our spark app here will be both a kafka producer and a kafka consumer
    // obviously we don't want to write to the same topic -- we'd arrive at an endless loop
    //for "processing", we'll just run UPPER
    //When we write out to kafka we must specify value, we can specify topic and key
    // We're skipping key, specifying topic as an option rather than a column, and aliasing
    // to create our value:
    df.selectExpr("UPPER(CAST(value AS STRING)) AS value")
      .writeStream
      .outputMode("append")
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("topic", "processed-events")
      .option("checkpointLocation", "/home/adam/tmp/checkpoint")
      .start()
      .awaitTermination()
      

  }
}