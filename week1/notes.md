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


### Intro Scala

## REPL : Read, Evalulate (Eval), Print, Loop

Scala comes with a REPL that lets us write interactive Scala code on the command line.  This is very useful for exploring the language, because you get instant feedback on the commands you type.

## Type system

Scala is statically typed, which means our vars/vals have a type that cannot change.  However, unlike Java, Scala does not require that you declare type, you can instead take advantage of the type inference in Scala to determine the appropriate type for for you vars/vals.

## val vs var

Use val to declare "values" that cannot change.  Use var to declare "variables" that can change.
