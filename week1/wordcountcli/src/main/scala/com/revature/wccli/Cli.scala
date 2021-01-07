package com.revature.wccli

import scala.io.StdIn
import scala.util.matching.Regex
import java.io.FileNotFoundException

/** A CLI that allows the user to interact with our application
  *
  * This Cli is a class because in the future we might provide customization options
  * that can be set when creating a new Cli instance.
  *
  * This Cli class will contain all our logic involving interacting with the user
  * we don't want all of our classes to be able to receive input from the command line
  * or write to the command line.  Instead, we'll have (almost) all that happen here.
  */
class Cli {

  /** commandArgPattern is a regular expression (regex) that will help us
    * extract the command and argument from user input on the command line
    *
    * Regex is a tool used for pattern matching strings.  Lots of languages and other tools
    * support regex.  It's good to learn at least the basic, but you can also just use this
    * code for your project if you like.
    */
  val commandArgPattern: Regex = "(\\w+)\\s*(.*)".r

  /** Prints a greeting to the user
    */
  def printWelcome(): Unit = {
    println("Welcome to Word Count CLI!")
  }

  /** Prints the commands available to the user
    */
  def printOptions(): Unit = {
    println("Commands available:")
    println(
      "wordcount [filename] : prints a count of the frequency of each word in a file"
    )
    println("exit : exits Word Count CLI")
    println("Bonus Commands:")
    println("echo [string to repeat] : repeats a string back to the user")
    println("addFive [number] : returns the given number, plus 5")
    println(
      "printTextContent [filename] : prints the text contained in a given file"
    )
  }

  /** This runs the menu, this is the entrypoint to the Cli class.
    *
    * The menu will interact with the user on a loop and call other methods/classes
    * in order to achieve the result of the user's commands
    */
  def menu(): Unit = {
    printWelcome()
    var continueMenuLoop = true
    while (continueMenuLoop) {
      printOptions()
      // take user input using StdIn.readLine
      // readLine is *blocking* which means that it pauses program execution while it waits for input
      // this is fine for us, but we do want to take note.
      val input = StdIn.readLine()
      // Here's an example using our regex above, feel free to just follow along with similar commands and args
      input match {
        case commandArgPattern(cmd, arg)
            if cmd.equalsIgnoreCase("wordcount") => {
          try {
            val text = FileUtil.getTextContent(arg)
            //we're going to have a chain of methods on text that specifies how we
            // want to transform it to arrive at a count of the frequency of each word:
            // replaceAll will find instances of the first String and replace them with the second
            // We're going to use a pinch of regex to replace all punctuation with nothing
            // make all characters lowercase
            // split the String into an Array[String] on space characters

            // TODO: handle contractions better than just ignoring them
            text.replaceAll("\\p{Punct}", "")
              .toLowerCase()
              .split("\\s+")
              .groupMapReduce(word => word)(word => 1)(_ + _)
              .toSeq
              .sortBy({ case (word, count) => count })
              .foreach({ case (word, count) => println(s"$word, $count") })

            //groupMapReduce! it's OK to not get this yet
            // the first function is used to create groups from our array of Strings
            // in general, elements with the same output from this function are grouped together
            // word => word means that the words are grouped together based on their identity
            // we end up with all the "the" words groups, all the "only" words groups, ...
            //
            // the second function is used to map (transform) the words.  Here we transform each word
            // into its count, which is easy.  Every word counts for 1.  Since there are 5 instances
            // of "only", each of those instances of "only" in the "only" group is transformed into a 1
            //
            // the third function is a reducer.  It specifies how we combine all the transformed outputs
            // from the second function in each group.  This reducer just sums them.  So the five 1 values
            // we have in the "only" group are all summed and reduced to "5"
            
          } catch {
            case fnfe: FileNotFoundException => {
              println(s"Failed to find file: ${fnfe.getMessage}")
              println(s"""Found top level files:
              ${FileUtil.getTopLevelFiles.mkString(", ")}""")
            }
          }
        }
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("echo") => {
          println(arg)
        }
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("exit") => {
          continueMenuLoop = false
        }
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("addfive") => {
          addFive(arg)
        }
        case commandArgPattern(cmd, arg)
            if cmd.equalsIgnoreCase("printtextcontent") => {
          printTextContent(arg)
        }
        case commandArgPattern(cmd, arg) => {
          println(s"""Failed to parse command: "$cmd" with arguments: "$arg"""")
        }
        case _ => {
          println("Failed to parse any input")
        }
      }
    }
    println("Thank you for using Word Count CLI, goodbye!")
  }

  def addFive(arg: String) = {
    try {
      println(s"result: ${arg.toDouble + 5.0}")
    } catch {
      case nfe: NumberFormatException => {
        println(s"""Failed to parse "$arg" as a double""")
      }
    }
  }

  def printTextContent(arg: String) = {
    try {
      println(FileUtil.getTextContent(arg))
    } catch {
      case fnfe: FileNotFoundException => {
        println(s"Failed to find file: ${fnfe.getMessage}")
        println(s"""Found top level files:
              ${FileUtil.getTopLevelFiles.mkString(", ")}""")
      }
    }
  }

}
