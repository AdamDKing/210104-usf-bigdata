package com.revature.clickstreamprocessor

import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.io.Text
import org.apache.hadoop.io.IntWritable

/** Takes curr + n values associated with prev values from clickstream data
  * and produces the prev + curr, n pair with largest n
  */
class MaxPrevCountReducer extends Reducer[Text, Text, Text, IntWritable] {

  override def reduce(
      key: Text,
      values: java.lang.Iterable[Text],
      context: Reducer[Text, Text, Text, IntWritable]#Context
  ): Unit = {
    val prev = key.toString()
    // this is a little Java style, essentially we're for-looping through the iterator
    // the iterator itself keeps track of our position
    val iter = values.iterator()
    var max = 0 //default to 0
    var prevcurrPair = prev //default to just the key
    while (iter.hasNext()) {
      val nextValue =
        iter.next().toString //gets the next value from the iterator
      val curr = nextValue.split("\\t")(0) //grab the curr field off it
      val n = nextValue.split("\\t")(1).toInt //grab n off it
      if (n > max) {
        prevcurrPair = s"$prev -> $curr"
        max = n
      }
    }
    context.write(new Text(prevcurrPair), new IntWritable(max))
  }

}
