package example.classdemo

//we're just going to create something with a method we can import here
//importing code from another just lets you use classes/objects/methods/functions/...
// defined in that other file.

//We can create an object.  This is a singleton object
// which means there is only ever one copy of it in existence.
object ClassDemo {

  //fields (variables) and methods associated with the class itself go here

  def myMethod(): Unit = {
    println("myMethod on the ClassDemo object")
  }

  def runExamples(): Unit = {
    //we're fine to define classes here in this demo method
    //The following line creates a Person class
    //class Person() {}
    //We can add fields to the class in two ways.  This is an example of adding fields that are
    // specified in the constructor:
    class Person(var firstName: String, val lastName: String) {}
    // A constructor is what is used to create an instance of a class.
    // In Scala, the main constructor is defined when we define the class
    // any parameters in the main constructor are automatically attached to the class
    // as fields.
    val adam = new Person("Adam", "King")

    def printPerson(person: Person) = {
      println(s"first name: ${person.firstName}, last name: ${person.lastName}")
    }

    // we can change the firstName, since it's declared with var
    adam.firstName = "Jeff"
    // we can't change the lastName, since it's declared with val
    //adam.lastName = "Goldblum"

    printPerson(adam)

    //one way to provide default values: add a default param value "King"
    class DetailedPerson(var firstName: String, val lastName: String = "King") {
      //here we are defining both the class and the constructor
      println("constructor begins!")
      //we can add fields using val and var
      //any values we hardcode here will apply to all instance of DetailedPerson
      val ssn = 438238764
      var age = 28

      //we can define methods
      def sayHello(): Unit = {
        //access the field on the object in the method
        println(s"$firstName says Hello!")
      }

      //the override keyword here means that we're replacing behaviour inheritied from
      // a parent class.  Default gives us example.classdemo.ClassDemo$DetailedPerson$1@40fd0443
      // We'd prefer a more sensible toString
      override def toString(): String = {
        s"$firstName $lastName, age $age, ssn $ssn"
      }
    }

    val dave = new DetailedPerson("Dave")
    dave.sayHello()
    println(dave.toString())
    //incidentally, every single object has a toString method.
    //println will call the object's tostring method in order to print it
    println(dave)
    //where is the default toString defined? the Any class!
    // Object polymorphism means println can treat DetailedPerson as Any
    // Method polymorphism means the .toString called will actually
    //  be the .toString we defined on DetailedPerson
    // very similar to the object class in Java

    val jimmy = new DetailedPerson("Jimmy", "Jones")
    jimmy.sayHello()

    // class practice and multiple constructors.  In scala we have a primary constructor and optional
    // auxiliiary constructors.
    class Fruit(
        var color: String,
        var tastiness: Int,
        var sourness: Int,
        var numSeeds: Int = 4
    ) {
      println("beginning primary fruit constructor")

      //we can define other constructors using the 'this' keyword:
      def this(color: String, tastiness: Int) {
        //set a default value for sourness, based on tastiness
        this(color, tastiness, tastiness / 2)
        println("auxilliary constructor")
        //our auxilliary constructors can take different args
        // they need to call the primary constructor at some point

      }

    }

    //using aux constructor
    val lime = new Fruit("green", 20)
    //using primary constructor with default param
    val lemon = new Fruit("yellow", 15, 15)
    //using primary constructor overwriting default param
    val apple = new Fruit("red", 20, 0, 10)
    //can specify parameter names for your arguments if you want to change the order
    val pear = new Fruit(tastiness = 30, color = "green")

  }
}

//We can create a class.  This will be a blueprint
// for objects.  We can use the class to make many objects
class ClassDemo {

  //fields and methods associated with individual members of the class go here

  def myInstanceMethod(): Unit = {
    println("myInstanceMethod on an instance of ClassDemo")
  }
}

//Here we have two definitions with the same name -- one class and one object.
// This is the only time we want to do this, but it is useful
// This reproduces the functionality of the static keyword in Java.
