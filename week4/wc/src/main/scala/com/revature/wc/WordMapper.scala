package com.revature.wc

import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.io.IntWritable

/** WordMapper defined the map functionality for wc (this mapreduce project)
  *
  * We extend Mapper and specify input and outpuit key value pairs for the mapper.
  * We then define a map function that specifies the transformation from input to output
  *
  * We use hadoop.io types instead of Scala types when defining the Mapper
  */
class WordMapper extends Mapper[LongWritable, Text, Text, IntWritable] {

  /**
    * Defines our map transformation, turning an input k-v pair into 0 or more intermediate k-v pairs
    * 
    * we need to manually specify the typing for Context here
    *
    * @param key
    * @param value
    * @param context
    */
  override def map(
      key: LongWritable,
      value: Text,
      context: Mapper[LongWritable, Text, Text, IntWritable]#Context
  ): Unit = {
    //we actually write our logic in here.  We create output by using context.write
    val line = value.toString() // get input as a String

    // split lines into words, filter empty words, transform to uppercase, write each out
    line.split("\\W+").filter(_.length > 0).map(_.toUpperCase()).foreach(
      (word:String) => {context.write(new Text(word), new IntWritable(1))}
    )
  }

}
