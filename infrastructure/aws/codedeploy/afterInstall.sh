#!/bin/bash

sudo systemctl stop tomcat.service
sudo systemctl stop amazon-cloudwatch-agent.service

#removing previous build ROOT folder
sudo rm -rf /opt/tomcat/webapps/ROOT

sudo chown tomcat:tomcat /opt/tomcat/webapps/ROOT.war

# cleanup log files
sudo rm -rf /opt/tomcat/logs/catalina*
sudo rm -rf /opt/tomcat/logs/*.log
sudo rm -rf /opt/tomcat/logs/*.txt

sudo ls /opt/tomcat/logs/
sudo touch csye6225.log
sudo chgrp -R tomcat csye6225.log
sudo chmod -R g+r csye6225.log
sudo chmod g+x csye6225.log
sudo mv csye6225.log /opt/tomcat/logs/csye6225.log