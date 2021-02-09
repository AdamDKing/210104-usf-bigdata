package hellosparksql;

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions

object Runner {
  def main(args: Array[String]): Unit = {
    println("get here, we'll be back in 10, at noon")

    //initialize a SparkSession, by convention called spark
    //SparkSession is the entrypoint for a Spark application using Spark SQL
    // it's new in Spark 2 + unifies older context objects.
    //SparkSession is different from SparkContext in that we can have multiple sessions
    // in the same runtime, where we only wanted 1 SparkContext per application.
    val spark = SparkSession.builder()
      .appName("Hello Spark SQL")
      .master("local[4]")
      .getOrCreate()

    //we want to always add an import here, it enables some syntax and code generation:
    // if you run into mysterious errors with what should be working code, check to make sure this import exists
    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    helloSparkSql(spark)
    
  }

  def helloSparkSql(spark: SparkSession):Unit = {
    //break intro demo off into a method, add import here as well.
    import spark.implicits._

    //From the Spark Session, we can create a DataFrame.  We're going to read from a JSON file to do so
    // though you can also create DFs from RDDs, parquet files, Hive tables, SQL tables, ... spark inputs
    // Spark will infer the schema for our json file, though we can also specify.
    val df = spark.read.option("multiline", "true").json("persons.json")

    df.show()

    df.printSchema()

    //select columns by name, in a few ways:
    df.select("name").show()
    df.select("name", "age").show()

    //use the $ syntax to select columns
    // this gets us Column objects instead of just strings.  These can be used in expressions
    df.select($"name", $"age").show()

    df.select($"name", ($"age" + 10).as("age plus ten")).show() // adds 10 to every value in the age column

    //fruit csv interlude
    val dfCsv = spark.read.option("header", "true").csv("fruits.csv")

    dfCsv.show()

    dfCsv.printSchema()

    //access nested values using a "."

    df.select("name.first", "name.last").show()

    //we can operate on DataFrames:
    // group by eye color and count each group
    df.groupBy("eyeColor").count().show()

    // filter based on age:
    df.filter($"age" > 30).show()

    // instead of the $ syntax, we can use the identifier for the dataframe:
    df.filter(df("age") > 30).show() // this gives the same output

    // The $ is often preferred so we don't repeat the df identifier many times, ex:
    //adamsNeatDf.filter(adamsNeatDf("num") < 200)

    //There are many built in functions, we can access some on dfs and some on a functions object that we import
    // these include scalar and aggregate functions, similar to SQL.
    df.select(functions.exp($"age")).show() // exponentiation
    df.select(functions.round($"age", -1)).show() // round age to nearest 10

    //average age by eye color for people with a first name less than 6 characters in length:
    val demoQuery = df.filter(functions.length($"name.first") < 6)
      .groupBy($"eyeColor")
      .agg(functions.avg($"age"))

    demoQuery.show()

    //knowing where to find functionality requires exploring the API
    // typically scalar/aggregate functions from SQL can be found on the functions object

    //2 more lines of code to run before lunch:
    // explain catalyst's plan for running the query
    demoQuery.explain(true)

    // access underlying rdd.  Since we're dealing with a DataFrame, which is a DataSet[Row]
    // the RDD contains Row objects
    println(demoQuery.rdd.toDebugString)
    







  }
}