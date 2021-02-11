package hellosparksql

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.IntegerType

object ParquetDemo {

  def run(spark: SparkSession): Unit = {
    import spark.implicits._

    //Parquet is a file format, so we'll run the demo first
    // then we'll talk about pros/cons
    //Since Parquet is just a file format, we use it by saving output as parquet files
  // THen we can read from parquet -- it's easy!

    val df = spark.read.option("header", "true")
      .csv("student-house.csv")
      .withColumn("age", $"age".cast(IntegerType)) //not sure why metals is broken here
    //the other way to change data types when reading in is to select the columns you want,
    // while making modifications/casting, then alias them

    df.show()

    df.printSchema()

    //lets save it as a parquet file:
    df.write.parquet("student-house.parquet")

    val readFromParquetDf = spark.read.parquet("student-house.parquet")

    readFromParquetDf.show()

    readFromParquetDf.printSchema()

    //We can write partitioned files to parquet as well, similar to Hive partitions:
    readFromParquetDf.write.partitionBy("house").parquet("student-house-partitioned.parquet")

    //We don't need to do anything special to read from partitioned parquet files
    val partDf = spark.read.parquet("student-house-partitioned.parquet")

    partDf.show()




  }

}