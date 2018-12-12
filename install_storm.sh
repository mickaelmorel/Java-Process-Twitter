#!/bin/bash

# This automated installation script let you install an instance of Storm Node (Mono-Cluster)
# By Knut (adrien.stefanski@gmail.com)

#
# Requirement for reporting
#
sudo rm -rf /opt/storm
sudo rm -rf /opt/zookeeper

sudo apt-get install mysql-server mysql-client
sudo /etc/init.d/mysql start
sudo apt-get install apache2
sudo apt-get install php5

# Install requirement

#
# Install Java JDK
#
cd /tmp
wget https://download.oracle.com/otn-pub/java/jdk/8u191-b12/2787e4a523244c269598db4e85c51e0c/jdk-8u191-linux-x64.tar.gz
tar -xvzf jdk-8u191-linux-x64.tar.gz
sudo mkdir /opt/jdk
sudo mv jdk1.8.0_111 /opt/jdk/

export JAVA_HOME =/opt/jdk/

#
# Install Zookeeper
#

sudo mkdir /opt/zookeeper
sudo mv zookeeper-3.4.12/* /opt/zookeeper/

#
# Configuration Zookeeper
#
sudo mkdir /opt/zookeeper/data
cat > /opt/zookeeper/conf/zoo.cfg << EOF
tickTime=2000
dataDir=/opt/zookeeper/data
clientPort=2181
initLimit=5
syncLimit=2

EOF

/opt/zookeeper/bin/zkServer.sh start
#
# Optionnal Zookeeper Command
#

# Accès au CLI
#/opt/zookeeper/bin/zkCli.sh
# Stop Zookeeper
#/opt/zookeeper/bin/zkServer.sh stop

#
# Install Apache STORM
#

sudo mkdir /opt/storm
sudo mkdir /opt/storm/data
sudo mv apache-storm-1.0.2 /opt/storm/

#
# Configuration Apache STORM
#
cat > /opt/storm/apache-storm-1.0.2/conf/storm.yaml << EOF
storm.zookeeper.servers:
- "localhost"
storm.local.dir: “/opt/storm/data”
nimbus.host: "localhost"
supervisor.slots.ports:
- 6700
- 6701
- 6702
- 6703
EOF

#
# Lancement de STORM
#
/opt/storm/apache-storm-1.0.2/bin/storm nimbus
/opt/storm/apache-storm-1.0.2/bin/storm supervisor
/opt/storm/apache-storm-1.0.2/bin/storm ui
