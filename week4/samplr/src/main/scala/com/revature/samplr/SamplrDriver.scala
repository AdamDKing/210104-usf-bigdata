package com.revature.samplr

import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.Text

object SamplrDriver {
  def main(args: Array[String]): Unit = {

    if (args.length != 3) {
      println("Usage: samplrjar <fraction> <input> <output> ")
      System.exit(-1)
    }

    val frac = args(0).toDouble

    if (frac <= 0 || frac >= 1) {
      println(s"Input fraction of ${frac} must be between 0 and 1")
    }

    val job = Job.getInstance()
    job.setJarByClass(SamplrDriver.getClass())
    job.setJobName("Samplr")
    job.setInputFormatClass(classOf[TextInputFormat])

    FileInputFormat.setInputPaths(job, new Path(args(1)))
    FileOutputFormat.setOutputPath(job, new Path(args(2)))
    job.setMapperClass(classOf[RandomSampleMapper])

    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[Text])

    val success = job.waitForCompletion(true)
    System.exit(if (success) 0 else 1)

  }
}
