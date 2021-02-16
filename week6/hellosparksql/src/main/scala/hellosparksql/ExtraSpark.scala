package hellosparksql

import org.apache.spark.sql.SparkSession
import scala.concurrent.Future

object ExtraSpark {
  /**
    * An example running processing on groups and using DataSets instead of DataFrames.
    * 
    * To use DataSets, we need case classes to represent our data.
    *
    * @param spark
    */
  def run(spark: SparkSession):Unit = {
    import spark.implicits._

    val bearerToken = System.getenv("TWITTER_BEARER_TOKEN")

    import scala.concurrent.ExecutionContext.Implicits.global
    Future {
      Runner.tweetStreamToDir(bearerToken, "countrytweetstream", queryString="?tweet.fields=geo&expansions=geo.place_id&place.fields=country")
    }

    val static = spark.read.json("countrytweetstream")

    static.printSchema()

    static.as[Tweet]
            .filter(_.includes.getOrElse(Includes(places=None)).places.nonEmpty)
      .groupByKey((tweet) => {
        tweet.includes.get.places.get(0).country.get //assumes all data is present, p not safe
      })
      .mapGroups({case (country: String, tweets) => {
        //here we do some analysis on the contents of these tweets, grouped by country
        // going to keep it simple and count some words
        (country, tweets.flatMap(_.data.text.split("\\s+"))
          .toList
          .groupBy(str => str)
          .mapValues(_.size)
          .mkString(" "))

      }})
      .collect()
      .foreach(println)

    val streamingDs = spark.readStream.schema(static.schema).json("countrytweetstream").as[Tweet]

    streamingDs
      .filter(_.includes.getOrElse(Includes(places=None)).places.nonEmpty)
      .groupByKey((tweet) => {
        tweet.includes.get.places.get(0).country.get //assumes all data is present, p not safe
      })
      .mapValues((tweet) => {
        //here we do some analysis on the contents of these tweets, grouped by country
        // going to keep it simple and count some words
        tweet.data.text.split("\\s+")
          .groupBy(str => str)
          .mapValues(_.size)
      })
      .reduceGroups((map1, map2) => {
        //sum values for keys in 2 maps
        map1 ++ map2.map({case(k,v) => k -> (v + map1.getOrElse(k,0))} )
      }) //reduceGroups is an aggregator for DataSets, so we can use "complete"
      //Just going to get the most used word per country, updating over time
      .map({case (country, wordcount) => {(country, wordcount.reduce((p1, p2) => {if (p1._2 > p2._2) p1 else p2}))}})    
      .writeStream
      .outputMode("complete")
      .format("console")
      .option("truncate", false)
      .start()
      .awaitTermination()

  }

  //example bit of data:
  //{"data":{"geo":{"place_id":"00d8"},"text":"dx5dYDt","id":"1365"},
  //"includes":{"places":[{"country":"Republic of the Philippines","full_name":"Tanay, Calabarzon","id":"00d4183c21f8ce08"}]}}

  //some of these could have more fields, depending on what we ask for from the API
  case class Geo(place_id: Option[String]) {}
  case class Data(geo: Option[Geo], text: String, id: String) {}
  case class Place(country: Option[String], full_name: String, id: String) {}
  case class Includes(places: Option[Array[Place]]) {}
  case class Tweet(data: Data, includes: Option[Includes]) {}

}