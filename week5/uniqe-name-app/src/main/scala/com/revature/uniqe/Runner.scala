package com.revature.uniqe

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Runner {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("uniqe-name-app").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val wordsRdd = sc.parallelize(List("Hi", "There"))

    println(wordsRdd.collect().mkString(" "))

  }
}




