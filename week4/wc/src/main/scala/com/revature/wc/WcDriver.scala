package com.revature.wc

import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.fs.Path


/**
  * WcDriver is the entrypoint for out word counting mapreduce
  * 
  * in this class we configure input and output data types, 
  * we configure Mapper and Reducer classes, and specify intermediate data formats
  * We're going to make use of command line arguments passed to this program
  * we can access them with "args"
  */
object WcDriver {
  def main(args : Array[String]): Unit = {
    if(args.length != 2) {
      println("Usage: wc <input dir> <output dir>")
      System.exit(-1)
    }

    //we start by instantiating a Job object we can configure
    val job = Job.getInstance()

    //we set the jar file that countains our job -- we specify this class
    job.setJarByClass(WcDriver.getClass())

    job.setJobName("Intro Word Count (wc)")

    //we set the input format, we're (almost) always going to be dealing with TextInputFormat
    job.setInputFormatClass(classOf[TextInputFormat])

    //set the file input and output based on the args passed in
    FileInputFormat.setInputPaths(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))

    //The above is mostly boilerplate, you'll want something very similar in all your MR jobs
    // below here we configure specific mappers and reducers, and we set the output key-value pair
    // types based on the specifics -- what our job does.

    //TODO: return + finish config

  }
}