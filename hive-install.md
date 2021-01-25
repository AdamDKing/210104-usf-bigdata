#### Download + install Hive
- cd to your home directory
- ```wget https://mirrors.sonic.net/apache/hive/hive-2.3.8/apache-hive-2.3.8-bin.tar.gz```
- wait for this complete
- ```tar -xvzf apache-hive-2.3.8-bin.tar.gz```

#### Add exports to .bashrc
- ```sudo nano .bashrc```
- Add the lines below to the .bashrc that opens, replacing username with your username
- ```export HADOOP_HOME=/home/username/hadoop-2.7.7```
- ```export HIVE_HOME=/home/username/apache-hive-2.3.8-bin```
- ```export PATH=$HADOOP_HOME/bin:$HIVE_HOME/bin:$PATH```
- ```#Additional shortcuts, from Danny:```
- ```alias startdfs='$HADOOP_HOME/sbin/start-dfs.sh'```
- ```alias startyarn='$HADOOP_HOME/sbin/start-yarn.sh'```
- ```alias stopdfs='$HADOOP_HOME/sbin/stop-dfs.sh'```
- ```alias stopyarn='$HADOOP_HOME/sbin/stop-yarn.sh'```
- Close and restart your Ubuntu shell.

#### Run HDFS commands necessary for Hive setup
- start hdfs + yarn if not already running (you can check with ```jps```)
- ```startdfs``` (makes use of the alias above)
- ```startyarn```
- ```hadoop fs -mkdir /tmp```
- ```hadoop fs -mkdir /user/hive```
- ```hadoop fs -mkdir /user/hive/warehouse```
- ```hadoop fs -chmod g+w /tmp```
- ```hadoop fs -chmod g+w /user/hive```
- ```hadoop fs -chmod g+w /user/hive/warehouse```

#### Initialize Hive
- ```schematool -dbType derby -initSchema```
- This should say 'schemaTool completed' at the end.  It will create a derby.log file and
- a metastore_db directory in our ~.  If you ever need to destructive restart hive, delete those files and run this command again.
- We can now connect on the command line using: ```beeline -u jdbc:hive2://```
- Use Ctrl+D to exit beeline
- The next steps are necessary to run hiveserver2 and connect from DBeaver.

#### Change Hive config
- ```sudo nano apache-hive-2.3.8-bin/conf/hive-site.xml```
- add the following lines to this xml file (it will be empty): 
```xml
<configuration>
<property>
<name>hive.server2.enable.doAs</name>
<value>false</value>
</property>

<property>
<name>hive.server2.enable.impersonation</name>
<value>true</value>
</property>
</configuration>
```

#### Run + use hiveserver2
- start hiverserver2 with:
- ```hiveserver2```
- open a new ubuntu tab
- connect to your running server from the command line (beeline):
- ```beeline -u jdbc:hive2://localhost:10000```
- Use Ctrl+D to exit beeline
- run a create database command to make sure it's working:
- ```CREATE DATABASE testdb;```
- If this works, open DBeaver, and create a new Apache Hive connection
- The default settings should let you connect.  Install the drivers when it prompts you

#### Final notes
- Hive runs using Hadoop, so you'll need to have HDFS + YARN running.
- You'll also need to remember to start the ssh daemon (sshd) when you boot up Ubuntu after a restart
- This install guide required some fiddling, and it's possible I missed a necessary step.  If you're sure you followed directions, reach out + let me know if you run into unexpected problems.  Especially if Hive is telling you that you're not allowed to impersonate another user!

