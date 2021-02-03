import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.collection.mutable.ArrayBuffer
object Runner {
  def main(args: Array[String]): Unit = {
    /*
    The idea is to fill out some functionality, test it locally on a local somelines.txt
    then change your application to run on EMR (which is just commenting one snippet and
    uncommenting another).  Then sbt assembly and send the jar to Adam (main channel is fine on Zoho)
    */
    val conf = new SparkConf().setAppName("CHANGE THIS")

    //Only have one of the following 2 snippets commented out
    // Make sure the Final S3 version is uncommented and the local version is commented
    // when you assemble the final jar and send it to Adam
    // Local version:
    conf.setMaster("local[4]")
    val someLinesLocation = "somelines.txt"

    // Final S3 version:
    // val someLinesLocation = "s3://rev-big-data/somelines.txt"
    
    //if you get an Exception: Exception in thread "main" java.io.IOException: No FileSystem for scheme: s3
    // this means you are running it locally with the Final S3 version
  
    val sc = new SparkContext(conf)

    val startingTextRdd = sc.textFile(someLinesLocation)
    //Functionality goes here: feel free to experiment!

    //Lets try getting a count of all the characters that follow an 'a' in the text
    startingTextRdd
        .map(_.toLowerCase())
        .flatMap(extractCharPairs) //get pairs of characters starting with 'a'
        .groupBy(_.charAt(1)) //group by second character
        .mapValues(_.toList.length)
        .collect()
        .foreach(println)

  }

  /**
    * assumes the input is all lowercase
    *
    * @param line
    * @return
    */
  def extractCharPairs(line: String): ArrayBuffer[String] = {
    var i = 0
    val out: ArrayBuffer[String] = ArrayBuffer()
    while(i < line.length()-1) {
      val gram = line.substring(i, i+2)
      if(gram.matches("a.")) {
        out.append(gram)
      }
      i += 1
    }
    out
  }
}