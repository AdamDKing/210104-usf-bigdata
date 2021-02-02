package com.revature.uniqe

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Runner {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("uniqe-name-app").setMaster("local[2]")
    val sc = new SparkContext(conf)

    sc.setLogLevel("ERROR")

    //All I want our unique name app to do
    // is to read from a file a collection of names
    // then, I want to find the number of unique names that start with each letter
    // if the file contained Adam, Alice, Adam, Alfred, Amy, Ben, Bill
    // The ideal output would be:
    // (A, 4)
    // (B, 2)

    //Fortunately, we already have student-house.csv with names, lets use it:
    val studentLines = sc.textFile("student-house.csv")
    //access first_name, the second entry in each csv line
    val names = studentLines.map(_.split(",")(1))
    //store each name as a key,value pair, with the key being the first letter
    val kvNames = names.map(name => {(name.charAt(0), name)})
    //group all the names with each starting letter together
    val groupedNames = kvNames.groupByKey()
    //convert the values for each key into a count of unique names
    // do this by transforming the iterable of values into a set and then using its size
    // remember that sets cannot contain duplicates
    val uniqueCounts = groupedNames.mapValues(_.toSet.size)

    println(uniqueCounts.sortByKey().collect().mkString(" "))

    println(uniqueCounts.toDebugString)



  }
}




