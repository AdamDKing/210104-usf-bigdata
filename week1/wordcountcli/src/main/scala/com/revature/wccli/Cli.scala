package com.revature.wccli

/**
  * A CLI that allows the user to interact with our application
  * 
  * This Cli is a class because in the future we might provide customization options
  * that can be set when creating a new Cli instance.
  * 
  * This Cli class will contain all our logic involving interacting with the user
  * we don't want all of our classes to be able to receive input from the command line
  * or write to the command line.  Instead, we'll have (almost) all that happen here.
  *
  */
class Cli {

  /**
    * Prints a greeting to the user
    */
  def printWelcome(): Unit = {
    println("Welcome to Word Count CLI!")
  }

  /**
    * Prints the commands available to the user
    */
  def printOptions(): Unit = {
    println("Commands available:")
    println("echo [string to repeat] : repeats a string back to the user")
  }

  /**
    * This runs the menu, this is the entrypoint to the Cli class.
    * 
    * The menu will interact with the user on a loop and call other methods/classes
    * in order to achieve the result of the user's commands
    */
  def menu() : Unit = {
    printWelcome()
    printOptions()
  }

}