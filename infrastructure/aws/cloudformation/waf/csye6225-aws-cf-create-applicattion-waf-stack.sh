#!/bin/bash

echo "enter the name for your Auto-Scaling Application Stack(alphanumeric)"
read applicationStackName
echo "enter the name for your Web-Applicatiton firewall Stack(alphanumeric)"
read wafStackName
if [ -z "$applicationStackName" ] || [ -z "$wafStackName" ]
then
    echo "Failed: either Application Stack Name or WAF Stack Name is Null"
    exit
fi

aws cloudformation describe-stacks --stack-name $wafStackName &> /dev/null
if [ $? -eq 0 ]
then
   echo "Failed: Stack by this name already exist"
   exit
fi

aws cloudformation describe-stacks --stack-name $applicationStackName &> /dev/null
if [ $? -ne 0 ]
then
   echo "Failed: Your Auto-Scaling Applicattion Stack not setup"
   exit
fi

status=$(aws cloudformation create-stack --stack-name $wafStackName \
--template-body file://csye6225-cf-application-waf.json \
--parameters \
ParameterKey=ApplicationStackName,ParameterValue=$applicationStackName \
--capabilities CAPABILITY_NAMED_IAM --on-failure DELETE)

if [ $? -eq 0 ]
then
    echo "please wait....."
    aws cloudformation wait stack-create-complete --stack-name $wafStackName
    if [ $? -eq 0 ]
    then
        echo "Successfully setup the stack"
        echo $status
    else
        echo "Failed: failed to deploy the stack"
        echo $status
    fi
else
    echo "Failed: failed to deploy the stack"
    echo $status
fi