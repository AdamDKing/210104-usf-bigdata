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
- start hdfs if not already running (you can check with ```jps```)
- ```startdfs``` (makes use of the alias above)
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
Now, let’s try to initialize Hive. It should say ‘schemaTool completed’ at the very end.
schematool -dbType derby -initSchema
Finally, let’s try running Beeline. This was the final step we got to, and we’ll add tables and perform SQL
queries in Hive tomorrow.
beeline -u jdbc:hive2://
If you see the ‘0: jdbc:hive2://>’ prompt, you’re all set. You can press [Crtl+D] to exit beeline. Don’t
forget to run stop-yarn.sh and stop-dfs.sh before quitting the Ubuntu shell, or they will still be running
even if you quit the window.
Final Notes
Finally, you can delete the downloaded .tar.gz files to get some disk space back. These will be where you
downloaded them, likely in your home directory.
rm apache-hive-3.1.2-bin.tar.gz
rm hadoop-3.2.1.tar.gz
Some more files you might need are student-house.csv and the data for Project 1. They should be linked
somewhere in the Discord under the notes-resources channel or in the Zoho Connect chat.
Additional Notes from Danny
These aliases and shorthand commands were noted by Danny, and seem very useful!
Starting HDFS: $HADOOP_HOME/sbin/start-dfs.sh
Starting Yarn: $HADOOP_HOME/sbin/start-yarn.sh
Stoping HDFS: $HADOOP_HOME/sbin/stop-dfs.sh
Stoping Yarn: $HADOOP_HOME/sbin/stop-yarn.sh
These can be added to your .bashrc for aliases to the commands above.
alias startdfs='$HADOOP_HOME/sbin/start-dfs.sh'
alias startyarn='$HADOOP_HOME/sbin/start-yarn.sh'
alias stopdfs='$HADOOP_HOME/sbin/stop-dfs.sh'
alias stopyarn='$HADOOP_HOME/sbin/stop-yarn.sh'
WSL seems to turn off sshd (ssh daemon) every time its restarted. It could be a time saver to add these two lines
below to your .bashrc, but it will mean typing your password when you login because the command is using
sudo.
echo "Starting sshd..."
sudo service ssh start  

