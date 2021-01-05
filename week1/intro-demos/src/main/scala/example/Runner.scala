package example

/**
  Runner is a basic demo of Scala functionality

  This type of comment is Scaladoc, meant to explain the following class/object/method/field/etc
*/
object Runner extends App {
  // This is one of two ways to write a Scala application.
  // we can write an "object" that extends App, or we can write a class
  // that has a main method.

  println("Everything we write inside of our Runner that extends App is just going to run")

  // val is used to declare constant values that cannot be changed
  val myConstant = 45

  // var is used to declare variables that can be changed
  var myVariable = 55

  println(myConstant + myVariable)

  // Use // to write comments in Scala, lines of text that are not evaluated as code
  /*
    This syntax works for multiline comments
  */

  // Typing: Scala uses static typing, but has a type inference system
  //val myInt : Int = 45
  val myIntTwo = 55

  // Nice Types to start with in Scala:
  val myByte : Byte = 10 // integral (whole number) type of size 1 byte, or 8 bits
  val myShort : Short = 100 // integral type of size 2 bytes, 16 bits
  val myInt : Int = 1000 // integral type of size 4 bytes, 32 bits
  println("Int max value: " + Int.MaxValue)
  println("Short max value: " + Short.MaxValue)
  println(s"Byte max value: ${Byte.MaxValue}") // String Interpolation
  val myLong : Long = 10000L //integral type of size 8 bytes, 64 bits
  println(s"Long max value: ${Long.MaxValue}")

  //Decimal types: Double and Float
  // Decimal types don't have a max or a min, instead the more bytes they use
  // the more precise they are.
  // generally speaking, a float will be accurate out to 5 or 6 decimal places
  // and a double will be accurate out to 15 or 16
  val myFloat : Float = 33.33f // decimal type of size 4 bytes, 32 bits
  val myDouble : Double = 44.44 // decimal type of size 8 byes, 64 bits

  // The types above that use more memory are mostly fine to use everywhere
  // only be concerened about efficient storage when it really matters.

  val myBoolean : Boolean = true // takes values true and false

  // This is an if statement in Scala, its our first piece of control flow
  // if-else statements are a branching structure, which cause one out of two blocks of
  // code to evaluate.
  if (myBoolean) {
      println("myBoolean was true!")
  } else {
      println("myBoolean was false :(")
  }

  val condition : Boolean = myInt > 5

  println(condition)

  if (condition) {
      println(s"$myInt was greater than 5")
  }

  //second flow control, interesting syntax for for-loops in Scala:
  // for loops are a looping structure, which means a block of code will be repeated
  // 0 or more times
  for (i <- 10 to -10 by -2) {
      //this will loop, and i will take values from 0 to 10
      println(s"value of i: $i")
  }

  // This 1 to 20 by 3 is a Range, one of the things we can use with For loops
  // however, we can use a for loop over most sequential data structures (values in order)
  println(1 to 20 by 3)

  val myFavoriteNumbers : Seq[Int] = Seq(3,27,33,444) //Seq is a sequence
  for (number <- myFavoriteNumbers) {
      println(s"Adam's favorite number: $number")
  }

  //Sequence is a collection, it's a rich data structure that contains sequential values of a single type
  // A Seq[Int] will contain only Ints, and an Seq[String] will contain only Strings.
  // The [] where we declare the type contained in a Seq are how we write "generics" in Scala
  // A generic is just a parameter for type -- for now just know how to use them with Seq

  //array of strings, another use of generics:
  val myStrings: Array[String] = Array("a","b", "c", "d")
  for (string <- myStrings) {
      println(string)
  }

  //Every single class in Scala is a subclass of Any.  Ints are subclasses of Any, Strings are subclasses of Any
  // Doubles are subclasses of Any, and any classes we write will be subclasses of Any.
  
  //Object Polymorphism means that objects that are members of child classes can be treated as members
  // of their parent classes in the appropriate context.
  //This means that our Strings and Ints, both being subclasses of Any, can be placed in a Seq[Any].

  //There is another type of Polymorphism, Method Polymorphism, that involves overloading and overriding methods

  //There is no switch in Scala, but there are match expressions:
  val myNumber = 21
  val matchResult = myNumber match {
      //inside a of a match you have multiple cases
      case 5 => "myNumber was five"
      case 10 => "myNumber was ten"
      case 25 => {
          "myNumber was twenty-five"
      }
      //we can have variables and variables in expressions(!) in our match cases:
      case anything if anything > 20 => s"myNumber was $anything and greater than 20"
      case _ => "myNumber was not five, ten, or twenty-five, and was less than 20"
  }

  println(matchResult)

  // while loop loops while a condition is true and stops looping when it is false
  // while loops may never run, if the condition is false

  var myCounter = 0
  while(myCounter < 10) {
      println(myCounter) // print out myCounter
      myCounter += 1 // add one to myCounter
  }

  //this while loop never runs:
  while(myCounter < 5) {
      println("This will never print because mycounter is > 5")
  }

  //we can use a do-while loop, which functions like a while loop but will always run at least once
  do {
      println("this will run at least once, regardless of condition")
  } while(myCounter < 5)

  //println(myInt + 20000000000)

  






}