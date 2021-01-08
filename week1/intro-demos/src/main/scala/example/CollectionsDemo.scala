package example

import scala.collection.mutable.ArrayBuffer
import example.classdemo.ClassDemo
import scala.collection.mutable.Map
import scala.collection.mutable.Set
import scala.io.Source
import scala.collection.mutable.HashSet

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
    // the key value pairs that we get for a foreach here are tuple2s
    states.foreach({ case (key, value) => println(s"$key : $value") })

    // Your Map provides constant time lookup based on key.  Regardless of how big your Map is,
    // finding a key, value pair is almost instantaneous

    // We can make Maps using any 2 types for the key and the value, the types don't have to be the same
    // the restriction is that the type of the key must implement the hashcode() and equals() methods
    // The reason for this is that your Map uses a hash table under the hood.  If you don't know what that
    // is, its really interesting but not necessary at the moment.

    // we can also have an immutable, by importing it from scala.collection.immutable
    // we can use both in the same file by using the fully qualified class name:
    val imMap = scala.collection.immutable.Map(
      "yellow" -> 6,
      "red" -> 3,
      "green" -> 5,
      "purple" -> 6
    )

    //this fails:
    //imMap("yellow") = 20

    //can get just the values
    println(imMap.values)

    // A Set is a collection without duplicate elements.  We can iterate over it, but it is
    // not indexed.  We shouldn't assume that a Set has a fixed order.  Some varieties of Set do,
    // and others do not.

    //Sets can be both immutable or mutable, similar to Maps.  Also similar to Maps, checking whether
    // a value is contained within a Set is *very* fast

    val shorts = Set[Short](3,5,7,9)

    shorts += 11
    shorts += 13 // the order changes when we add a 13 to the Set

    //these lines won't change the Set, since it cannot contain duplicates:

    shorts ++= List(1,3,5,7)
    shorts += 13

    //The default implementation of Set is a HashSet, which has no guaranteed order.
    // A HashSet uses a Hash Table under the hood, similar to a Map.  In general, we can think
    // of a Set as Map without any values.  On this note, Sets can only contain objects that have
    // a hashcode() implementation!

    println(shorts)

    shorts.add(10)
    shorts.remove(5)

    println(shorts)

    // We can also use SortedSet

    // A Scala type that will often appear and we should be familiar with is Option.  It's not a collection,
    // but it is kind of a container for another value.  Option represents some value that may or may
    // not exist.  The use of Option is very common in functional code, and it's nice because it sometimes
    // lets us avoid try-catch exception handling.

    // we can get Options from Maps:
    println(states.get("AK"))
    println(states.get("MA"))
    println(states.get("VA"))

    // An Option containing a value is represented with Some(value), an Option not containing a value
    // will be represented by None

    // we can use match statements very effectively with Options:
    states.get("VA") match {
      case Some(state) => println(state)
      case None => println("not found in Map")
    }

    val abbrevs = List("AK", "KY", "VA", "MA", "CA")

    //map is a higher order function that takes a function that is used to tranform every element in
    // a data structure.  map always returns a new copy, it never edits the original

    //abbrevs.map((abbrev) => abbrev.last).foreach(println)
    //underscore shortcut:
    //abbrevs.map(_.last).foreach(println)

    abbrevs.map(states.get(_)).foreach(println)

    //above, map is returning a List[Option[String]].  Whenever we have nested data structures
    // like this, we can call .flatten to reduce them to a single level, in this case
    // to get a List[String]

    println(abbrevs.map(states.get(_)).flatten)

    //we can also use flatMap, which is just map followed by flatten.  This method is very useful
    // for when you have a chain of tranformations (map calls) that all produce Options

    println(abbrevs.flatMap(states.get(_)))

    // Option is like a container for a value that may or may not exist
    // values that exist are Some(value)
    // values that dont exist are None
    // we can flatMap to work with transformations that produce Options

    // map, filter, reduce:
    // map filter and reduce are all higher order functions.  Lets save the reduce for later.
    // These functions can be found in many programming languages, each of them takes a function as an
    // argument that operates on some data structure (often a List in Scala). map and filter both
    // return a new List.  None of map, filter, or reduce modify the existing List.

    // map is used to transform the elements in a data structure.  The output of map will be a new
    // List with the same number of elements as the original list, but the value/type of elements
    // in the new List may be different

    // filter is used to filter the elements in a data structure.  The output of filter will be
    // a new List with either the same or a smaller number of elements as the original list.  None
    // of the values/types will be different.

    // foreach is somewhat similar, but it doesn't return anything.  Instead, foreach just calls
    // a function on each element in your data structure

    //multiply each element in the List by 2
    List(1,2,3,4,5).map(_*2).foreach(println)

    //add 3 to each element
    List(1,2,3,4,5).map(_+3).foreach(println)

    //move to the REPL to try it out! We can chain these methods

    //multiply each element in the list by 7, then filter out all the odd elements
    // then convert them all to strings, then transform them into tuples containing
    // (String, Int), with the number as a string and its length

    // scala> l.map(_*7)
    // res4: List[Int] = List(7, 14, 21, 28, 35, 42, 49, 56, 63)

    // scala> l.map(_*7).filter(_%2 == 0)
    // res5: List[Int] = List(14, 28, 42, 56)

    // scala> l.map(_*7).filter(_%2 == 0).map(_.toString)
    // res6: List[String] = List(14, 28, 42, 56)

    // scala> l.map(_*7).filter(_%2 == 0).map(_.toString).map((num)=>{(num, num.length)})
    // res7: List[(String, Int)] = List((14,2), (28,2), (42,2), (56,2))

    //tuples: a tuple is an ordered series of values of various types, up to 22!
    // Use tuples when you don't want to create a class or a case class for some
    // grouped properties
    val myTuple: (Int, String, List[Int]) = (3, "hello", List[Int](3, 4))

    val myInferredTuple = (4.5, 55, "hi", 5.6)

    //We can have collections of tuples, this can be an easy and flexible way to
    // store structured data:
    val myPriorities =
      List[(String, Int)](("wash dishes", 3), ("get oil changed", 7))
    println(myPriorities :+ ("water plants", 5))

    //access the elements of a tuple using ._ followed by the number we want
    println(myTuple._2)

    val myWeirdTuple: (ClassDemo, String, String, Float) = null

    //case classes provide more structure and information than tuples, but are easier
    // to quickly create and use than regular classes.
    
    //A case class doesn't provide any totally new functionality over regular classes,
    // it just provides automatically generated methods  + defaults that we would
    // have to write ourselves in a regular class.  Everything that a case class does
    // can be done with a regular class.  "syntactic sugar"

    //case classes are by default immutable, so they are often used in FP style code

    case class Fruit(name: String, color: String) {
      //can still add functionality and fields
      def sing() = {
        println("la la la")
      }
    }

    //now we can use the case class:
    println(Fruit("lemon", "yellow"))

    val lime = Fruit("lime", "green")
    //can access fields but cannot change them
    //lime.color = "red"

    lime.sing()

    case class Todo(task: String, priority: Int) {}

    val myTodos = List(Todo("wash dishes", 7), Todo("water plants", 5))

    println(lime.copy(color = "red"))

    println(myTodos)

    // What exactly does a case class get you?
    // - fields default to val, case classes are by default immutable, but you can access their fields
    // you just can't change them
    // - equals and hashcode methods are generated based on the fields.  This means equality between case
    // classes will check their fields' equality, and you put them in Sets and Maps
    // - a default toString method is generated
    // - a copy() method is generated that will let us make modified copies of instances of the case class
    // - apply and unapply methods are generated
    // unapply lets your use the case class in match expressions, we'll see this later
    // apply is a special method in Scala that is used whenever you just use the name of the class
    // as if it were a function.  We haven't called out apply when we've seen it, but we've seen it frequently

    //examples of apply:
    //creating a new List:
    List(1,2,3,4,5)

    //accessing by index
    doubles(1)

    //accessing by key in a map
    states("AK")

    //creating new instances of our case class
    Todo("explain apply method", 9)

  }
}
