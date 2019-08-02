#!/bin/bash

echo "enter the name for your Lambda Stack(alphanumeric)"
read lambdaStackName
echo "enter the name for your Application Stack(alphanumeric)"
read applicationStackName
echo "enter the name of your CircleCI Roles and Policies stack(alphanumeric)"
read CircleCIStackName
if [ -z "$applicationStackName" ] || [ -z "$applicationStackName" ] || [ -z "$CircleCIStackName" ]
then
    echo "Failed: either Lambda Stack Name or Application Stack Name or CircleCI Stack Name Null"
    exit
fi

echo "enter your domain name"
read domainName
if [ -z "$domainName" ]
then
    echo "Failed: entered domainName is Null"
    exit
fi

aws cloudformation describe-stacks --stack-name $lambdaStackName &> /dev/null
if [ $? -eq 0 ]
then
   echo "Failed: Stack by this name already exist"
   exit
fi
aws cloudformation describe-stacks --stack-name $applicationStackName &> /dev/null
if [ $? -ne 0 ]
then
   echo "Failed: Your Application Stack not setup"
   exit
fi
aws cloudformation describe-stacks --stack-name $CircleCIStackName &> /dev/null
if [ $? -ne 0 ]
then
   echo "Failed: Your CircleCI Stack not setup"
   exit
fi

status=$(aws cloudformation create-stack --stack-name $applicationStackName \
--template-body file://csye6225-cf-application.json \
--parameters \
ParameterKey=CircleCIStackName,ParameterValue=$CircleCIStackName \
ParameterKey=domainName,ParameterValue=$domainName \
--capabilities CAPABILITY_NAMED_IAM --on-failure DELETE)

if [ $? -eq 0 ]
then
    echo "please wait....."
    aws cloudformation wait stack-create-complete --stack-name $applicationStackName
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