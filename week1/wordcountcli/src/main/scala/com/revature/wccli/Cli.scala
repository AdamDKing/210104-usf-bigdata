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
