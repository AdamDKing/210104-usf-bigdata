package com.revature.clickstreamprocessor

import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text

/** PLCM takes clickstream data, filter to only links, and produces intermediate pairs:
  * prev is the resulting key, and the value is curr and n, concatenated with a tab
  */
class PrevLinkCountMapper extends Mapper[LongWritable, Text, Text, Text] {

  override def map(
      key: LongWritable,
      value: Text,
      context: Mapper[LongWritable, Text, Text, Text]#Context
  ): Unit = {
    //record contains prev, curr, type, n
    val record = value.toString().split("\\t")
    //this should be cleaner, but we'd need to create types to work with hadoop, which is a decent chunk of work
    // I think we'll be ok with the murkiness here
    // we're accessing fields from the input record in order to construct the appropriate
    // intermediate k-v pair (described in scaladoc for this class)
    if (record(2) == "link") {
      context.write(
        new Text(record(0)),
        new Text(s"${record(1)}\t${record(3)}")
      )
    }
  }

}
