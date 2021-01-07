package example

//This is an import statement, it lets us use code from another file
// Here we use the *fully qualified class name* to import ClassDemo
// The fully qualified name is just the name of the package followed by the name of the class/object
import example.classdemo.ClassDemo

/**  Runner is a basic demo of Scala functionality
  *
  *  This type of comment is Scaladoc, meant to explain the following class/object/method/field/etc
  */
object Runner extends App {
  // This is one of two ways to write a Scala application.
  // we can write an "object" that extends App, or we can write a class
  // that has a main method.

  println(
    "Everything we write inside of our Runner that extends App is just going to run"
  )

  ClassDemo.myMethod()

  // This line creates an instance of our ClassDemo class
  // the new keyword is used to create new objects
  var myInstance = new ClassDemo()
  myInstance =
    new ClassDemo() //create a second ClassDemo instance and assign it to myInstance

  myInstance.myInstanceMethod()

  ClassDemo.runExamples()

  runOperators()

  stackExceptionsProgramFlow()

  //introDemo()

  def introDemo(): Unit = {
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
    val myByte: Byte =
      10 // integral (whole number) type of size 1 byte, or 8 bits
    val myShort: Short = 100 // integral type of size 2 bytes, 16 bits
    val myInt: Int = 1000 // integral type of size 4 bytes, 32 bits
    println("Int max value: " + Int.MaxValue)
    println("Short max value: " + Short.MaxValue)
    println(s"Byte max value: ${Byte.MaxValue}") // String Interpolation
    val myLong: Long = 10000L //integral type of size 8 bytes, 64 bits
    println(s"Long max value: ${Long.MaxValue}")

    //Decimal types: Double and Float
    // Decimal types don't have a max or a min, instead the more bytes they use
    // the more precise they are.
    // generally speaking, a float will be accurate out to 5 or 6 decimal places
    // and a double will be accurate out to 15 or 16
    val myFloat: Float = 33.33f // decimal type of size 4 bytes, 32 bits
    val myDouble: Double = 44.44 // decimal type of size 8 byes, 64 bits

    // The types above that use more memory are mostly fine to use everywhere
    // only be concerened about efficient storage when it really matters.

    val myBoolean: Boolean = true // takes values true and false

    // This is an if statement in Scala, its our first piece of control flow
    // if-else statements are a branching structure, which cause one out of two blocks of
    // code to evaluate.
    if (myBoolean) {
      println("myBoolean was true!")
    } else {
      println("myBoolean was false :(")
    }

    val condition: Boolean = myInt > 5

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

    val myFavoriteNumbers: Seq[Int] = Seq(3, 27, 33, 444) //Seq is a sequence
    for (number <- myFavoriteNumbers) {
      println(s"Adam's favorite number: $number")
    }

    //Sequence is a collection, it's a rich data structure that contains sequential values of a single type
    // A Seq[Int] will contain only Ints, and an Seq[String] will contain only Strings.
    // The [] where we declare the type contained in a Seq are how we write "generics" in Scala
    // A generic is just a parameter for type -- for now just know how to use them with Seq

    //array of strings, another use of generics:
    val myStrings: Array[String] = Array("a", "b", "c", "d")
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
      case 5  => "myNumber was five"
      case 10 => "myNumber was ten"
      case 25 => {
        "myNumber was twenty-five"
      }
      //we can have variables and variables in expressions(!) in our match cases:
      case anything if anything > 20 =>
        s"myNumber was $anything and greater than 20"
      case _ =>
        "myNumber was not five, ten, or twenty-five, and was less than 20"
    }

    println(matchResult)

    // while loop loops while a condition is true and stops looping when it is false
    // while loops may never run, if the condition is false

    var myCounter = 0
    while (myCounter < 10) {
      println(myCounter) // print out myCounter
      myCounter += 1 // add one to myCounter
    }

    //this while loop never runs:
    while (myCounter < 5) {
      println("This will never print because mycounter is > 5")
    }

    //we can use a do-while loop, which functions like a while loop but will always run at least once
    do {
      println("this will run at least once, regardless of condition")
    } while (myCounter < 5)

    //println(myInt + 20000000000)

    // Functions!

    // A function takes an input and returns an output, though it doesn't actually have to do either
    // When we're writing a function, we specify what input it can take by specifying its *parameters*
    // When we're calling a function, we pass that input into the function as *arguments*
    // A function can have no parameters, which means it takes no arguments
    // When we're writing a function, we specify what it returns.  In Scala, it will return the result
    // of the last line of the function by default.  If we don't want our function to return anything we
    // can specify that it returns "Unit" (instead of void)

    // There are multiple ways to define functions in Scala:
    // this is a function that takes a String and returns a String
    def func1(param1: String): String = {
      s"Input string was length ${param1.length}"
    }

    println(func1("Hello world!"))

    def func2(param: String): Unit = {
      println(s"Input string was length ${param.length}")
    }
    func2("Howdy World!!")

    //Which of the above functions is pure? func1, nice!

    //We can also define functions as lambdas, which are anonymous inline functions

    val myFunc = (param: String) => { s"Lambda with input: $param" }

    println(myFunc("Adam"))

    //foreach is a higher order function:
    val mySeq = Seq("a", "b", "3", "green", "blue")

    mySeq.foreach(func2)

    //use a lambda
    mySeq.foreach((param) => { println(s"The string is $param") })
  }

  def runOperators() = {
    //an operator is just something like +, -, *, =, +=,
    // typically your operators operate on two values called the operands
    3 + 5 // + is an operator
    6 - 2 // - is an operator

    //Some languages allow you to redefine the functionality of operators
    // they do this in different ways.
    //In Scala, we allow this by making every operator a method
    // Operators are just a method called on the left operand
    //If a method takes a single argument, you can write it as if it were
    // an operator

    println(3 + 5)
    println(3.+(5)) // looks strange

    //we typically only use that syntax with methods that are short and operator-like
    var mySeq = Seq(1, 3, 5, 7, 8)
    println(mySeq :+ 22)

    println(mySeq contains 3) // this is not good style, but you can do it

    //This way of defining operators means we can override the default implementation
    // of an operator if we want to in a child class.

    //There are also unary operators like negation (!) and ternary operators, in other languages
    // if-else in Scala is an expression rather than a statement, different from many other languages
    // this just means that if-else returns a value instead of just executing code
    //expressions return some value, statements do not.  Expressions which always return Unit we sometimes call statements
    println(if (false) "value one" else "value two")

    //notable statements in Scala are class and method definitions

    //define MagicNum class (blueprint)
    class MagicNum(var value: Int) {
      def +(other: MagicNum) = {
        value + other.value + 45 //add magic numbers by adding their values and adding 45
      }
      override def toString() = { value.toString }
    }

    var num1 = new MagicNum(3) //create 1 MagicNum
    var num2 = new MagicNum(10) //create a 2nd MagicNum

    println(num1 + num2)

  }

  def stackExceptionsProgramFlow() = {
    def fun1() = {
      println("hi")
      throw new Exception("my message!")
    }
    def fun2() = {
      println("f2")
      fun1()
    }
    def fun3() = {
      //we use the try and catch keywords to handle Exceptions:
      // try marks a block of code for exception handling
      // catch specifies what to do with an Exception that occurs
      // finally is used to specify a block of code that will run
      // regardless of an Exception being thrown (if you need some code to
      // *always* run, use finally).
      try {
        5 / 0
        fun2()
      } catch {
        //we can specify multiple cases in our catch block in order to handle different types of Exceptions
        // the cases must be ordered so that more specific Exceptions come before less specific Exceptions
        case ae: ArithmeticException => {
          println("caught specifically an Arithmetic Exception")
        }
        case e: Exception => {
          println("caught an exception!")
          e.printStackTrace()
        }
      }
      //Whenever we catch an Exception, it's assumed that we're handling it, and program flow resumes
      // from where we catch the Exception.

      println("f3")
    }
    fun3()
  }

}
