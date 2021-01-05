package example.classdemo

//we're just going to create something with a method we can import here
//importing code from another just lets you use classes/objects/methods/functions/...
// defined in that other file.

//We can create an object.  This is a singleton object 
// which means there is only ever one copy of it in existence.
object ClassDemo {

    //fields (variables) and methods associated with the class itself go here

    def myMethod() : Unit = {
        println("myMethod on the ClassDemo object")
    }

    def runExamples() : Unit = {
        //we're fine to define classes here in this demo method
        //The following line creates a Person class
        //class Person() {}
        //We can add fields to the class in two ways.  This is an example of adding fields that are
        // specified in the constructor:
        class Person(var firstName : String, val lastName : String) {}
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
        



    }
}

//We can create a class.  This will be a blueprint
// for objects.  We can use the class to make many objects
class ClassDemo {

    //fields and methods associated with individual members of the class go here

    def myInstanceMethod() : Unit = {
        println("myInstanceMethod on an instance of ClassDemo")
    }
}

//Here we have two definitions with the same name -- one class and one object.
// This is the only time we want to do this, but it is useful
// This reproduces the functionality of the static keyword in Java.