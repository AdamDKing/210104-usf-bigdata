package example

import scala.collection.mutable.ArrayBuffer
import example.classdemo.ClassDemo
import scala.collection.mutable.Map

object CollectionsDemo {
  //Scala fun fact: you can write import statements in your code, it doesn't have to be at the top
  def run() = {
    //import an immutable Map
    //import scala.collection.immutable.Map
    //option for wildcard import:
    //import scala.collection.mutable._

    // WE're going to talk about collections, all the built in ones are found in scala.collection
    // though we're free to write our own.  A collection is an object that lets us store other objects
    // (most often data values, but Scala is functional!) in some structured manner.

    // We often describe these collections as being "backed by" some "data structure".  Each of these
    // collections works by having some classic data structure under the hood that actually stores the data
    // we mostly don't need to worry about this right now, but it's good to know a bit about the
    // data structures backing each collection so you're able to use the right collection for your
    // specific use case.  A collection might be backed by "arrays" or "a linked list" or "a binary tree"
    // or "a hash table".  If you've never heard of these before, don't worry about them yet.  Knowing about
    // data structures will just let you use collections more effectively

    // We should mentions *generics* here.  *generics* allow us to parameterize over type, which gets us
    // compile time type safety for our collections.  Generics are denoted with [], and they let us
    // define a List[String] vs. List[Int] vs. List[Pizza].

    // Let's start with ArrayBuffer

    val ints = ArrayBuffer[Int]()

    // ArrayBuffer is mutable, we can modify the values stored within it and its length
    // ArrayBuffer is also indexed, so it's like a mutable array

    // add elements
    ints += 4
    ints += 7
    ints += 1

    println(ints)
    //remove elements
    ints -= 4

    println(ints)

    // add and remove multiple values

    ints ++= Seq(3, 4, 5, 4, 5, 3)
    ints --= Seq(7, 5)

    println(ints)

    // check methods using Scala Metals
    ints.append(4, 5)
    ints.prepend(99)
    println(ints)
    ints.remove(3, 5)
    println(ints)

    //access via index
    println(ints(0))

    // ArrayBuffer is mutable and indexed, really just like a mutable array

    //List is an immutable sequence.  It stores elements in order like an ArrayBuffer,
    // but its elements and its length cannot be changed.  It also works differently under the hood

    val doubles = List(4.4, 5.5, 6.6, 7.222)

    // any methods we call on a List will return a new List, rather than modifying the existing list
    // append and prepend using :+ and +:

    println(doubles :+ 55.55)
    println(55.55 +: doubles)

    println(doubles) //still unchanged

    //append and prepend multiples
    println(doubles :++ List(3.3, 1.5345))
    println(List(3.0, 4.0, 3.0) :++ doubles)

    // insert new values with patch, but this is a kind of rare use case
    println(doubles.patch(1, List(77.777), 1))

    println(doubles(2))

    // Lists are stored in a sequence, and so we can access them by index.  They are immutable
    // under the hood, a List is a singly-linked list, instead of being backed by an array.
    // The end of the List, under the hood, has an element pointing to Nil.

    //Let's talk for a moment about immutability here.  Our Lists are immutable, meaning we cant add
    // or remove or edit values in them.  However, we *can* change properties on the objects
    // stored within them.  What does this mean?
    class MutableFieldHaver(var field: String) { //var makes it editable
      override def toString() = { s"field: $field" }
    }

    val mfhList = List(
      new MutableFieldHaver("green"),
      new MutableFieldHaver("red"),
      new MutableFieldHaver("seven")
    )
    println(mfhList)
    mfhList :+ new MutableFieldHaver("blue")
    println(mfhList)
    mfhList(1).field = "something else!"
    println(mfhList)

    // line 104 edits one of the objects contained in mfhList, it doesn't edit the list itself
    // watch out for this when you have immutable data.
    // To make a *really* immutable list, you'll want to (1) declare with val, (2) use an immutable List,
    // and (3) store immutable objects inside your immutable list

    // Vector is an indexed, immutable collection, backed by arrays.  It's like List in that it's immutable
    // it's like ArrayBuffer in that it's good for random access

    val longs = Vector[Long](44L, 23L, -90L)

    println(longs)
    //we can explore Vector methods, they are not too different

    // Map is a different sort of data structure.  It's like a dictionary in Python, and kind of similar
    // to a dictionary outside of the computer.  Your Map stores key, value pairs.  We don't put single values
    // inside of a Map, instead we store pairs, one is the key one is the value.  We are able to quickly
    // and easily access these pairs in the Map using their key.  We store a key and a value, and we lookup
    // by key.

    // Map can be immutable or mutable, it just depends whether we import it from scala.collection.mutable
    // or scala.collection.immutable

    // Map syntax:
    val states = Map[String, String](
      "key" -> "value",
      "AK" -> "Alaska",
      "KY" -> "Kentucky",
      "CA" -> "California"
    )

    //retrieve based on key
    println(states("KY"))

    println("AK".hashCode())

    println(states("AK"))

    states += ("AR" -> "Arkansas") // add k,v pairs
    states("AK") = "Alaska, a really big state"

    println(states)

    // loop over the key values pairs using a foreach:
    states.foreach({ case (key, value) => println(s"$key : $value") })

    // Your Map provides constant time lookup based on key.  Regardless of how big your Map is,
    // finding a key, value pair is almost instantaneous

    // We can make Maps using any 2 types for the key and the value, the types don't have to be the same
    // the restriction is that the type of the key must implement the hashcode() and equals() methods
    // The reason for this is that your Map uses a hash table under the hood.  If you don't know what that
    // is, its really interesting but not necessary at the moment.

    //tuples: a tuple is an ordered series of values of various types, up to 22!
    // Use tuples when you don't want to create a class or a case class for some
    // grouped properties
    val myTuple: (Int, String, List[Int]) = (3, "hello", List[Int](3, 4))

    //We can have collections of tuples, this can be an easy and flexible way to
    // store structured data:
    val myPriorities =
      List[(String, Int)](("wash dishes", 3), ("get oil changed", 7))
    println(myPriorities :+ ("water plants", 5))

    //access the elements of a tuple using ._ followed by the number we want
    println(myTuple._2)

    val myWeirdTuple: (ClassDemo, String, String, Float) = null
  }
}
