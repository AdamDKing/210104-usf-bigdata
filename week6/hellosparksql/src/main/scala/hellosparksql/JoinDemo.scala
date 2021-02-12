package hellosparksql

import org.apache.spark.sql.SparkSession

object JoinDemo {
  def run(spark: SparkSession): Unit = {
    import spark.implicits._

    val df = spark.read.option("header", "true").csv("student-house.csv")

    df.show()

    //let's make a dataframe to join with this one, we'll start with a dataset and transform it
    val houseDetails = spark.createDataset(List(
      HouseDetail("Gryffindor", 200, "red"),
      HouseDetail("Slytherin", 150, "green"),
      HouseDetail("Ravenclaw", 170, "blue"),
      HouseDetail("Hufflepuff", 400, "yellow")
    )).toDF()

    houseDetails.show()

    //similar to SQL joins, we specify a column (or expression)
    // records are joined when the value in the column is the same.
    houseDetails.join(df, "house").show()

  }

  /**
    * Details about Hogwarts house
    *
    * @param house
    * @param points
    * @param color
    */
  case class HouseDetail(house: String, points: Int, color: String) {}
}