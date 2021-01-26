package com.revature.wc

import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.io.Text
import org.apache.hadoop.io.IntWritable

/** WordReducer is similar to WordMapper, but it defines how we aggregate intermediate key value pairs
  * instead of defining a transformation on input key value pairs
  */
class WordReducer extends Reducer[Text, IntWritable, Text, IntWritable] {

  /**
    * reduce will run once for each intermediate key received from the shuffle and sort in MapReduce
    * each intermediate key will be paired with all intermediate values associated with that key
    * 
    * In this way, our reducer aggregates the values associated with each key.
    *
    * @param key
    * @param values
    * @param context
    */
  override def reduce(
      key: Text,
      values: java.lang.Iterable[IntWritable],
      context: Reducer[Text, IntWritable, Text, IntWritable]#Context
  ): Unit = {
    var count = 0

    values.forEach(count += _.get()) //get each value and add it to the count

    context.write(key, new IntWritable(count))
    //hamlet (1,1,1,1,1) =>
    //hamlet 5
  }

}
