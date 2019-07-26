#!/bin/bash

#Starting tomcat service
#sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
#    -a fetch-config \
 #   -m ec2 \
#    -c file:/opt/cloudwatch-config.json \
#    -s

sudo systemctl start tomcat.service
# sudo systemctl status tomcat