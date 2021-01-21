### MapReduce

MapReduce is an algorithm and data processing platform that came out of google in the early 2000s.  Google needed large clusters of machines to run their search algorithms (and other things) on large amounts of data.  They ran into classic cluster computing problems, and developed MapReduce as a way to run many different algorithms on a cluster without rewriting much of the same logic.  Every large set of calculations ran on a cluster needs to be distribute work among nodes (computers) in the cluster, needs to be able to handle failure and slow machines, and needs to aggregate the results of its processing from across the cluster at the end of the job.  MapReduce provides the above functionality, so that developers can just focus on the actualy processing they need done.  Developers using MapReduce write custom Map tasks and custom Reduce tasks to specify the content of their job.

Hadoop began as an implementation of google's MapReduce, then grew into a general big data processing platform.  Many different projects were built alongside / on top of Hadoop in order to fulfill specific needs.  Also, Hadoop itself evolved.  Version 2 of Hadoop uses YARN (Yet Another Resource Negotiator) to run jobs and can handles tasks outside of traditional MapReduce.

The core of Hadoop v2+ that we should start with is HDFS (Hadoop Distributed FileSystem) and YARN (Yet Another Resource Negotiator).  Both of these tools run on a cluster, which means they run as daemons on multiple machines that are networked together.  HDFS is data storage spread across a cluster, and YARN is data processing spread across a cluster.  It is still possible to run MapReduce jobs using YARN, but YARN supports other data processing as well.  Notably, for us, Apache Spark jobs can run on YARN.

A daemon is just a long-running process.  HDFS and YARN both involve multiple different daemons running on different machines.  Typically, applications running on a cluster will have one or more master daemons responsible for coordinating work and many worker daemons responsible for actually doing the work.

### Unix + Unix-like OSs

We discussed this week 1, but a brief refresher: Unix was an OS, came out of Bell Labs in the late 60s early 70s.  Large community of hackers + academics modified the source of Unix to create and share distributions.  In the early 80s Bell decided to close the source, so they sold new versions of Unix as software with licensing that prevented editing/sharing the course code.

The community was not happy, some continued working on BSD (Berkeley Software Distribution) based on earlier Unix, some (Stallman) worked on the GNU (Gnu's Not Unix) Project, which was an attempt to recreate Unix, open source + copyleft, from the ground up.  GNU/Linux is the Linux we know today, Ubuntu is a distribution based off of GNU/Linux.

### Open Source, CopyLeft

Open source software is software that is free to be used in any manner.  You can share it, modify it, remove parts of it, add onto it, ...  You're free to do what you like with it.  We say "free as in freedom, no free as in free beer" or "libre vs gratis".  Open source is about having the freedom to use and modify your software as you see fit, it's not necessarily about not having to pay for software.  Notably, Open Source software can be used in commercial products.  You are able to use some piece of open source code in your closed source project that you sell.  A common license is the MIT license.

Copyleft software (starting with the GNU project) is open source with one notable exception. It's not really open source since it has this exception.  Software under a copyleft license can be shared/modified freely, but you cannot use in closed source commercial projects.  ALl software that uses copyleft software must preserve the copyleft licensing.  A common license here is the GPL.

Of course you can have many different types of licensing, these are just broad categories (with nice examples with MIT and GPL).  Apache is Open Source, similar to MIT.

### Unix-like OS Shell utilities (BASH commands)
- ls
- cd
- pwd
- mkdir
- touch : make a new file
- nano : command line text editor, used if you don't know vim
- man : manual for a command, used for help.  Can also use \[command\] --help
- less
- cat : prints contents to the screen.  Useful with |
- mv
- cp
- rm
- history : shows history of commands
  - history | grep \[old command\] : this will show you prior usage of some command.  very handy when you forget.
- clear
- ls -l : displays contents of a directory in longform, with details.  Lets us see permissions

In Unix, there are 2 locations with special character names, those are root (/) and home (~).  Root is the root (top) of the filesystem.  Home is your current user's home, found in /home/username

Every path we specify is either relation or absolute.  Absolute paths start with / and fully specify the location.  Relative paths don't start with / and specify the location based on our current directory

The Unix philosophy for all these tools is to have a small tool that does its one job well. 
Then, users can combine these small tools in order to achieve their desired result.
To this end, we can use | to take the output from the left command and feed it in as input on the right. 
We can also use > and >> to overwrite and append the output of the left to a file on the right.
echo "Hello BASH" has "Hello BASH" as its output.  We can use the command echo "Hello BASH" > myfile to write that to file.

### Unix permissions

We're going to use a bit of this, but let's try to install Hadoop!  Will return here.

### HDFS Daemons

There are two main daemons for HDFS to run on a cluster, a master daemon
and a worker daemon:
- NameNode: master daemon.  There is one of these per cluster unless your cluster is multiple thousands of machines.  The Name Node keeps
  the image of the distributed filesystem.  This means it knows the directory structure, the name of all the files and directories, and 
  it know where to find them on the cluster.  It doesn't store any of the actual data in the files/directories.  It does contain the metadata
  for files and directories.
- DataNode: worker daemon.  There are many of these in your cluster, typically 1 per machine, except you wouldn't run a DataNode on the machine
  running your Name Node.  If i have a 6 machine cluster, probably I have the NN on one machine and DNs running on the other 5.  The DN stores
  the actual data stored in the filesystem and communicates its status with the NN.  It doesn't know about the metadata or the filesystem.
DataNodes run on "commodity hardware", which just means regular servers, nothing specialized.  All the files stored in HDFS are stored in blocks
of size 128MB.  This means if we have a file that is 128MB, it will be stored in 1 block.  If we have a file that's 127MB, it will be stored in 1 block
256MB -> 2 128MB blocks, 129MB -> 2 blocks, 1 is 128MB, the other is 1MB.  Each file/block within a file is replicateed across the cluster.  
The default here is 3 replications.  Replication information and other metadata is stored on the NameNode, and the NameNode makes all decisions
about where data/replicas will be stored on the cluster.
The NN and the DNs communicate constantly.  The DNs are sending "heartbeats" to NN, which just means they periodically send a network request
to the NN, containing a report on the blocks of data they're storing.  In this way, the NN maintains a complete picture of the functioning
of all DNs *and* the status of all the blocks stored across the cluster.

Bonus NameNode details:
- Name Node store all information about the filesystem in a file called FSImage, a file stored on local disk.
- Name Node records edits to the filesystem in a log called EditLog, also stored on local disk.

### YARN Daemons
