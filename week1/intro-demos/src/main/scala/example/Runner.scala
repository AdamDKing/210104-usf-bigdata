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
  for (i <- -10 to 10 by 2) {
      //this will loop, and i will take values from 0 to 10
      println(s"value of i: $i")
  }

  //println(myInt + 20000000000)






}