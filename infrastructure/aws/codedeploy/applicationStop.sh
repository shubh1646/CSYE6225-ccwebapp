#!/bin/bash

#stoping tomcat
sudo systemctl stop tomcat
if [ $? -ne 0 ]
then
    echo "Tomcat Stoped"
fi