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
- Close and restart your Ubuntu shell. After this, run some HDFS commands to create directories and set
permissions so Hive can interact with them.
Additional Notes from Danny
These can be added to your .bashrc for aliases to the commands above.

cd hadoop-3.2.1
sbin/start-dfs.sh
cd ..
hadoop fs -mkdir /tmp
hadoop fs -mkdir /user/hive
hadoop fs -mkdir /user/hive/warehouse
hadoop fs -chmod g+w /tmp
hadoop fs -chmod g+w /user/hive
hadoop fs -chmod g+w /user/hive/warehouse
Before we actually initialize Hadoop, we need to replace some .jar files.
rm apache-hive-3.1.2-bin/lib/guava-19.0.jar
cp hadoop-3.2.1/share/hadoop/common/lib/guava-27.0-jre.jar apache-hive-3.1.2-bin/lib/
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

