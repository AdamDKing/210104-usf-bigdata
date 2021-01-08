### Intro discussion notes + comments

- computer basics:
  - processor: this is the brain.  Every single application your computer runs ultimately comes through here.  Your processor runs commands written in machine language (x-86 for most of us).  Every program we write will eventually be compiled to machine language, which allows it to run on our processor.
  - RAM (Random Access Memory), or Memory: RAM is your short term memory.  RAM is where the inputs to the commands running on the processor are stored, where the outputs are stored, and where your programs themselves are stored while they're running.  Actually there are multiple levels of memory, from *registers* on the processor to *caches* between the processor and RAM, out to *RAM* itself.
  - HDD, SSD, or Storage: Your hard disk is long term memory.  This is the only part of your computer that preserves anything after you shut down your machine.
  - All of the above is hardware, we install an Operating System on that hardware, then run the OS.  That's what we're doing right now!

- virtual machines:
  - A virtual machine is like the OS running on your hardware that we're using right now, but running on *virtual* hardware instead.
  - I actually have Windows installed on my computer, my Windows installation makes use of my processor, RAM, and disk directly.
  - When I use a virtual machine, I create some virtual resources on top of my physical resources and install an OS on those virtual resources.

- Unix-like OS:
  - An OS similar to the Unix OS.  Linux OSs are Unix-like as is MacOS and BSD.  Windows is not.  Many of our big data tools work better on Unix-like OSs.  Most(?) servers run Unix-like OSs, though you can run Windows Server.

- Binaries:
  - an application that can run, often shortened to bin, written in machine language for your processor.

- Shell / CLI (Command Line Interface) / Terminal:
  - Your shell is a text-based interface with your OS, different from the graphical interface we're more familiar with.  We'll get comfortable with this over time, you don't need to be familiar with it right now.

- WSL2 : Windows Subsystem for Linux 2.  This lets us install and run Ubuntu (or another GNU/Linux OS) inside/alongside our Windows installation.  More convenient than dual-booting and better (in my experience) than running a VM

- Cluster : multiple computers networked together than function as a single unit.  Many of the tools we use will run "on a cluster", which means each machine in the cluster will be running an application, and the machines in the cluster working together run our tool.  Example: we will be running Apache Spark on a cluster.
  - We can also use "single node clusters" for development purposes, which are clusters that run on only one machine.  These will not be suitable for production workloads, but they are great for learning and development.
  - We care about clusters, and we run most of our tools on clusters, because we're going to need the computing resources (processing, memory, disk) of many machines in order to process large amounts of data.

- Node : In a cluster, a single machine is a node.

- Big Data vs "Data Science" vs "Statistics" vs ... : There are many different things you can use data for, most often it involves answering questions or providing insight to decision makers.  We're going to lump all those uses together as "data science".  This is separate from but related to the concerns of big data.  Big Data is about processing large quantities of data, and can be loosely described with 3 Vs: Volume, Velocity, Variety.
  - Volume : Big data processing involves large amounts of data at least >1TB
  - Velocity : Big/Fast data involves processing data that is produced rapidly and may need to be processed in near-real-time.
  - Variety : Big data involves processing data in multiple formats from multiple sources.
  - ^ These are Gardner's 3 Vs.  There are 2 other Vs floating around out there for the curious!
Mostly we're going to be concerned with the processing of large quantities of data, but we'll discuss and in project you'll be expected to attempt some "data science".  I'll never expect statistical analyses, but I will expect you to think about the assumptions and limitations of your project work.

- Git : Git is a tool used for Source Control Management.  You can say its an SCM tool.  What git does is it saves the entire development history of a project, rather than saving just the most recent version.  It also allows this development history to branch, meaning you can simultaneously save and develop on different, parallel versions of your codebase.

- Git Repository : A repository (or repo) is how git saves your code.  We're going to have a git repo for notes and demos that I'll share today.  Your projects will each have a git repo, containing their code.

- GitHub : GitHub is a free, commonly used remote repository for git.  It lets us share a git repository across github itself and across multiple machines.

- Package Manager : package managers download applications or parts of applications for you, installing and managing dependencies automatically.  Managing dependencies means that if the application I want requires another application to function, my package manager will get both of them for me (the application I want and its dependency).  Unix-like OSs have package managers for the OS that make installing new applications very easy.  Windows does as well, Chocolatey and Scoop are options, Brew is an option on MacOS.  On Ubuntu we'll use APT (Advanced Package Tool).
  - Programming languages and runtime environments might also have package managers that we'll use to retrieve dependencies necessary for our code to function.

Our notes and demos repo on github: https://github.com/AdamDKing/210104-usf-bigdata

List of technologies for this week:
  - WSL2 + Ubuntu (Windows only)
    - 20.04 or 18.04 of Ubuntu
    - You may need to enable virtualization in your BIOS.
    - https://github.com/microsoft/WSL/issues/4103 will help if you get an error (Error: 0xc03a001a The requested operation could not be completed due to a virtual disk system limitation.  Virtual hard disk files must be uncompressed and unencrypted and must not be sparse."
)
  - On Mac, you will probably want a package manager (Homebrew?)

  Install on Mac/Windows:
  - vscode

  Install on Mac/Ubuntu: (use a package manager)
  - git
  - java 8 (openjdk is good)
  - sbt (scala build tool)
  - scala (not totally necessary with sbt, but nice)

Commands for installation on Ubuntu:
1. sudo apt update
2. sudo apt upgrade
3. sudo apt install openjdk-8-jdk
4. sudo apt install scala
5. follow commands https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html

### Basic Unix Commands
- ls: list files and directories in current directory
- ls -a: list files and directories, including hidden
- cd: change directory.  You can cd to a specific path, or if you just type cd, it takes you to your home directory (~)
  - Home is denoted by a ~, and is found at /home/username
- less: used to read files.  Can also use cat to print the contents of a file
- sudo: elevate your permissions for a command.  Use with caution!
- apt: use the Advanced Package Tool to install/manage/update packages
- man: check the manual for a command.  very useful!
- nano: very basic command line text editor

### Intro Scala

## REPL : Read, Evalulate (Eval), Print, Loop

Scala comes with a REPL that lets us write interactive Scala code on the command line.  This is very useful for exploring the language, because you get instant feedback on the commands you type.

## Type system

Scala is statically typed, which means our vars/vals have a type that cannot change.  However, unlike Java, Scala does not require that you declare type, you can instead take advantage of the type inference in Scala to determine the appropriate type for for you vars/vals.

## val vs var

Use val to declare "values" that cannot change.  Use var to declare "variables" that can change.

## Control Flow

See the Runner.scala in intro-demos for control flow! if-else, match, for and while loops

### What is Scala?

The name Scala comes from "Scalable Language", and Scala was written by Martin Odersky, one of the contributors to the java compiler (javac).  Scala is written on top of Java.  In order to write and run Scala, you need Java tools.

Java is a very popular OOP language used in enterprise.  The important pieces to writing and running Java are the JDK (Java Development Kit), the JRE (Java Runtime Environment), and the JVM (Java Virtual Machine).  The JDK is useful for Java developers, while the JRE + JVM together (the JVM is contained in the JRE) is useful to run Java code on any system.  When Java developers write code, they write .java files.  The JDK is then used (javac) to transform that .java code into *bytecode* that runs on the JVM.  Bytecode is contained in .class files instead of .java files.  Then, this bytecode can be run on any JVM on any system, and the JVM compiles the bytecode down into machine language for that system.  The major advantage of this approach is that you can compile your code to bytecode once, then that bytecode can be used on many different architectures.

Most systems have JVMs available, so making use of the JVM means your code can run almost everywhere.

Scala is written to compile down to Java bytecode.  This means that we write .scala files, and under the hood those are compiled down to .class files which can run on JVMs on any system.  Scala is not the only language to do this -- the JVM is widespread and highly performant, so there are many languages that take advantage of it.  In addition, we're able to use Java dependencies in Scala code.  Since Java is so popular, tooling exists for Java that may not exist for Scala.  We're able to use the Java tooling.

Scala is:
- A High level language, meaning it abstracts away the hardware and runs on the JVM.
- Statically typed, meaning variables cannot change types
- Has a type inference system, so type does not always need to be declared.
- compiles down to .class files that run on the JVM and is interoperable with Java code
- Scala is built on Java, so supports almost all Java functionality, but draws inspiration from many other languages
- Supports OOP (Object Oriented Programming) Paradigm
- Supports FP (Functional Programming) Paradigm

### Imperative programming

Writing a program in Imperative style means that your write the program as a list of instructions for the computer to carry out.  Runner.scala is imperative, it just tells the computer to run a bunch of statements for our demos.  OOP and FP are easier to understand when contrasted with Imperative programming.  Both of those paradigms are used when your programs become large/complicated.

### Object-Oriented Programming

OOP is a programming paradigm where your write your application by defining classes and instantiating objects from those classes.  The objects have state and behaviour, and the interactions between the objects constitute the functionality of your application.  Typically the state of the objects in OOP evolves over time, but the evolution of state is managed by the objects themselves (encapsulation!).
Pillars of OOP:
- Encapsulation: Objects control access to their own state and behaviour.  This is enabled in Scala (+ Java) with *access modifiers*
  -In Scala, we can mark members of a class as private (accessible only in that class), protected (accessible in that class and subclasses in the package), and public (accessible everywhere).  The default is public.
- Inheritance: Child classes inherit state and behaviour from their parent classes.  This lets us share functionality from parent to child and reuse code.
- Polymorphism: "Many forms", your objects and methods can take multiple forms depending on the context
  - Object polymorphism means instances of child classes can be treated as instances of parent classes
  - Method polymorphism means that a single method might have different functionalty based on the arguments given or the object it is attached to. (Method overloading and overriding)
- Abstraction: Focusing on relevant details in a given context while ignoring irrelevant details in that context.
  - When we're using a TV remote to change the channel, we only really care about the remote having buttons that are labelled and we press the buttons corresponding to the channel we want.  We're not interested in the electronics inside the remote, how the button press transfers information to your television, what the television does with the received signal, how channel 5 is coming to your TV from outside, ...
  - When we're writing and running code, we *mostly* don't care about the way that code is using processing power and memory, though we will care if we're optimizing code.
  - Abstraction in Scala is achieved in many different ways, but abstract classes and traits are two prominent tools.

When learning OOP its fine to talk about Dog classes that extend Animal.  In practice, though, your objects and classes exist to do something in your application.  Actual classes you might have: DbConnectionUtil that provides utilities for connecting to a database, or TextFileCsvParser that parses the contents of a text file as a csv, ...

## Functional programming

FP is a programming paradigm where we write our application out of the composition and application of pure functions.  *Composition* is just combining functions, we can compose two functions to create a third function that consists of the functionality of both applied in sequence.  *Applying* functions just means that we run them on some data.
Useful FP definitions:
- Function: something that takes an input and produces an output
- Pure Function: A function that does nothing other than take an input (arguments) and produce an output (return value).  It should not read any data other than its input and it should not have any effect other than returning its output.
  - One way you can recognize a pure function is if your function *always* returns the same output given the same input.  A function that returns the square of its input will *always* return 4 if given 2.
- Side Effect: Anything a function does other than return a value.  Printing to the console and writing to file are both side effects.  Changing the value of a variable outside the function is also a side effect.
- Higher order functions: A higher order function is a function that takes function(s) as input, returns a function, or both.  Notable higher order functions for now : map, filter, reduce, foreach

In Functional programming, we write our application out of pure functions.  Typically those pure functions operate on immutable data.  We almost never want to mutate data when writing in an FP style, instead producing new outputs.  In real functional programming, which we won't do, there are fancy methods for avoiding side effects in your functions while still writing to disk, printing to the console, having useful side effects occur in your application.  For us, we'll sometimes write in an FP style but we'll still write functions with side effects (impure functions) in order to save results to a database or print to the console or similar.

### What makes a language functional?

Functional Programming is a paradigm, it's a way to write/structure your applications.  Some languages support FP and some do not.  The essential feature to support FP is having functions as *first-class citizens* of the language.  Some data type being a first class citizen of a language means that your can pass that data type into function, return the data type from functions, and store that data type in variabes and data structures.

Therefore, functions as first class citizens (which Scala supports) means that we can pass functions into other functions, return functions from other functions, and store functions in variables and data structures.

### Errors and Exceptions

Generally speaking, there are two kinds of problems that can happen in your running Scala program:
these are erros and exceptions.  Both of them are objects, with some special behaviour in the Scala (Java) runtime.  The main difference between them is the sort of problem they represent and the expected behaviour of our code in response to them:
- Exception: some exceptional behaviour, something unexpected and often bad has occurred.  Most of the time, your application should attempt to recover from Exceptions.  We should write our own code to throw Exceptions when exceptional behaviour happens, and then we can write other code elsewhere to catch and handle those Exceptions.  We do both sides (throw and catch) at different places in the codebase.  Typically you throw Exceptions where the problem occurs, and you handle exceptions where you have the ability to fix the problem.  Common exceptions: ArrayIndexOutOfBoundsException, ArithmeticException, FileNotFoundException, RuntimeException (general class for Exceptions that happen while app is running), NullPointerException (this exception is what gets thrown when you attempt to call a method or access a field on something that is null.  null is the value that your vals and vars have before your assign something to them).
- Error: an Error most often represents a problem with the JVM itself, some problem with the basic machinery used to run your application.  Common Errors: OutOfMemoryError and StackOverflowError.  OutOfMemoryError happens when your use up too much memory storing objects.  StackOverflowError happens when you call too many methods/functions.  We often should not and cannot recover from Errors.
- Handling Exceptions with try/catch/finally: When an Exception is thrown, regular program flow ceases.  Instead, the exception gets thrown to all the methods/functions previously called in reverse order.
- Example:
```
def fun1() = {
  println("hi")
  //throw new Exception("my message!")
}
def fun2() = {
  println("f2")
  fun1()
}
def fun3() = {
  fun2()
  println("f3")
}
fun3()
```
- One of those methods/functions might (and should) catch the Exception and handle it.  If this doesn't happen, the Exception will continue being thrown all the way down to the JVM, where the JVM will print a stack trace and exit.  Exceptions should not bring out program to a halt, so we need to write the logic to catch and handle them before they reach the JVM.
- Exceptions and Errors are arranged in a class hierarchy.  The parent of all of them is Throwable, which has direct children Exception and Error.  RuntimeException extends from (is a child of) Exception.  Often Exceptions in a single domain will have a shared parent (like IOException).
- One of very few features *removed* from Java in Scala is the concept of checked vs unchecked Exceptions.

### Stack and Heap

We're to talk a bit about the internal memory model of the JVM.  Remember that Scala is built on top of Java, all Scala code is converted to Java Bytecode (.class files) and runs on the Java Virtual Machine (JVM).

There are two main areas of memory.  One of them is the Stack, the other is the Heap.  The Stack is where method execution happens.  The Heap is where objects are stored.  Let's talk about the Heap first.  Whenever we create a new object, the constructor for that object runs.  The first thing the constructor does is allocate some space on the Heap for the object.  Then, all the fields in that object are stored in that space.  The constructor then *returns* a reference to that area of memory.  This reference is how we retrieve and interact with the data stored in the object on the heap.  We sometimes call objects "refence types" instead of "primitive types" because we use references to access them.  A "primitive" is something we need to pay attention to in Java code, but we don't really need to worry about it in Scala code.  A primitive is not a reference type, instead the primitive just contains its actual value.

Every single method call that occurs in the lifetime of our application goes on the Stack to be resolved.  Our application begins running when the JVM calls our (in Scala) class Runner extends App, or our main method.  The Stack is a stack (the data structure) which means it is LIFO (Last In First Out).  New items placed on the stack are the first items to be taken off.  This means our main method (our entrypoint) goes on the bottom of the Stack, and all the methods it calls go above it on the stack.

Each method called goes on the stack as a *stack frame*.  Whatever method is currently on the top of the stack is the method currently being executed.  If that method, being executed, calls another method, the new method call goes on the stack and begins execution.  Our program continues running in the way, with methods popping on and off the stack, until we reach the end of the main method.  When main pops off the stack, our program exits.

The stack frame associated with each method call stores primitive values and references to the Heap (object references discussed above)

### Threads

A thread is a single flow of program execution.  Thus far, we've only been dealing (explicitly) with a single thread.  That thread is the main thread that starts at the top of our main method and flows through our entire application.

Our computer is not limited to doing one thing at a time.  Your processor might be able to simultaneously work on 4, 8, 16, ... tasks at the same time.  Our programs can be responsible for more than one of those tasks at the same time, as well.

We can have many threads in our applications (many flows of program execution), and more than one of those threads can be executing at any given time.  We're not limited to having a number of threads <= cores in our processor, our Scala applications can have 1000s of threads, but not all of those threads can run at the exact same time.

Running multiple Threads in an application can have many benefits:
- if you're running multiple threads at the same time, you have effectively multiplied your processing power.  If we 8 threads on a problem instead of 1, it will complete in roughly 1/8 the time (depends on overhead and specifics of the problem).
- We can use threads to wait and listen for events to occur and take action in response.  If you want to process some twitter data as soon as it becomes available, you'd need to have a Thread listening for that Twitter data to become available, at which point that Thread could trigger (multithreaded) processing
- A fancier version of our pj0 would have multiple threads so we could have one thread interacting with the user and one or more threads working on DB access + file reading behind the scenes.  If the main thread is doing both then delays reading files will impact the user's experience.

Running multiple Threads (and, for us in the future, running tasks on multiple machines across a cluster) also involves a lot of challenges.  You have to write code that doesn't break if certain parts of it execute in an unexpected order.  FP style is very nice for this.

