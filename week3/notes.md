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
Two main YARN Daemons:
- Resource Manager : one resource manager per cluster, the RM is the master daemon.  Responsible for providing computing resources for jobs.  Computing resources here are things like RAM, cores, disk.  Allocates the computing resources necessary to do tasks like MapReduce.
- Node Manager : one per machine, the worker daemon.  Node managers manage bundles of resources called *containers* running on their machine and report the status back to the RM.

Computations occuring on the cluster can be described as *jobs* composed of *tasks*.  A job is something like an entire MapReduce.  We submit jobs to the Resource Manager (more specific later).  Tasks are the individual pieces Jobs are broken up into.  If we submit a MapReduce job to the cluster, that job may spawn 100s to 1000s of map and reduce tasks.  Tasks are what run inside of containers. 

If we submit a MapReduce job with 300 map tasks and 4 reduce tasks, we submit that job to the RM, and the RM makes those tasks actually run in 304+ containers across the cluster, and tracks progress based on the information it receives from the Node Managers.

We need a more detailed picture here, the Resource Manager is composed of a few pieces:
- Scheduler : this is responsible for allocating resources (containers) across the cluster based on requests.  It doesn't know much about the source of those requests or the overarching jobs occuring, it just allocates computing resources.
- ApplicationsManager : Accepts job submissions, and creates the ApplicationMaster for each submitted job.  Also responsible for the fault tolerance of ApplicationMasters.  ApplicationMasters run in containers on the cluster, and are responsible for communicating with the scheduler to achieve their jobs.  This allows this ApplicationsManager to be ultimately responsible for job completion, while offloading most of the work to ApplicationMasters running on worker nodes.

Scheduler : 1 per cluster (part of RM)
ApplicationsManager : 1 per cluster (part of RM)
ApplicationMaster : 1 per job (managed by ApplicationsManager)

### Fault Tolerance

Fault tolerance is just the ability of a system to continue functioning after faults have occurred -- after something has gone wrong.  Fault tolerance is very important in distributed systems, because there are always failures occurring.  We're going to discuss how Hadoop achieves fault tolerance, and we'll have similar discussions for future technologies as well.  One important piece to achieving fault tolerance is avoid Single Points of Failure (SPoF).  Any part of the system that breaks the whole system when it fails is a single point of failure and should be avoided/mitigated.  All systems fail, fail-safe systems fail when their fail-safe mechanism fails.

A related concept is High Availability.  This just means maintaining availability of a system for as much uptime as possible.  It's a little fuzzy, we often talk about percentages of uptime in practice.

### Fault Tolerance in HDFS

- For DataNodes, their fault tolerance is handled by the NameNode.  DNs send heartbeats to the NN, so when a DN goes down, it stops sending those heartbeats, and the NN knows to make new replicas of all the data stored on the downed DN.  The NN makes copies up to the replication factor (default 3, we've set it to 1).
- For NameNode, in Hadoop v1 it was a SPoF.  In Hadoop 2+ we have multiple options for fault tolerance:
  - In the first case, the NN has its FSImage and EditLog, which it periodically checkpoints and write to disk.  This information stored on disk enables easy recovery if just the NN daemon crashes.
  - We can have a Secondary NameNode.  This periodically (every hour) keeps backups of the NN metadata.  The Secondary NN isn't capable of stepping in or functioning as a replacement NameNode, it just provides functionality to preserve FS information in a secondary location in the case of total failure of the NN.  Avoid catastrophic data loss.
  - We can have a Standby NameNode.  This is a daemon that runs on another machine and follows the same steps as the NameNode while they are occuring in real time.  The Standby NameNode doesn't receive outside traffic and doesn't make decisions about the filesystem, it just receives the information in the EditLog and keeps its own FSImage.  Then, if the real NameNode fails, the Standby NameNode steps in and becomes the new NameNode.  This is the best option, but requires more resources (must allocate resources for 2 NN instead of 1).  This behaviour, where a system fails and another system steps in to handle that failure, is called *failover*.

### Fault Tolerance in YARN

- For NodeManagers, their fault tolerance is handled by the ResourceManager.  NMs send heartbeats to the RM, the RM knows when a NM is down, and can re-run the lost tasks elsewhere.
- For Tasks within a job that fail, their fault tolerance is handled by that Job's ApplicationMaster.  The single ApplicationMaster per job receives reports about the progress of tasks and can request new resources from the scheduler for tasks that fail.
- For ApplicationMasters, their fault tolerance is handled by the ApplicationsManager.  If the container running an ApplicationMaster goes down (or the whole node), the ApplicationsManager will restart it, potentially on a different machine.
- It is possible (with some config) to restart NodeManagers and allow the containers (ApplicationMasters / Tasks) to continue where they left off before the failure.  These two systems for fault tolerance work together.
- For the ResourceManager, it was a SPoF before Hadoop 2.4.  You can now configure a standby ResourceManager, capable of stepping in in the case the active ResourceManager goes down.  This process is managed by Zookeeper, which we won't get to yet.

### Gardner's 3 Vs, Unix permissions (octal), rack awareness and data locality

Some topics to look into ^
