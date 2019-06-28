#!/bin/bash

echo "enter the name for your Application Stack(alphanumeric)"
read applicationstackName
echo "enter the name of your Virtual Private Cloud stack(alphanumeric)"
read networkStackName

if [ -z "$applicationstackName" ] || [ -z "$networkStackName" ]
then
    echo "Failed: Enter a valid name for both application stack and networking stack"
    exit
fi

aws cloudformation describe-stacks --stack-name $applicationstackName &> /dev/null
if [ $? -eq 0 ]
then
	echo "Failed: Stack by this name already exist"
	exit
fi
aws cloudformation describe-stacks --stack-name $networkStackName &> /dev/null
if [ $? -ne 0 ]
then
	echo "Failed: Your Virtual Private Cloud Stack not setup"
	exit
fi

echo "enter the your domain Name"
read domainName

status=$(aws cloudformation create-stack --stack-name $stackName \
--template-body file://csye6225-cf-application.json \
--parameters \
ParameterKey=networkStackName,ParameterValue=$networkStackName \
ParameterKey=domainName,ParameterValue=$domainName \
--on-failure DELETE)
if [ $? -eq 0 ]
then
    echo "please wait....."
    aws cloudformation wait stack-create-complete --stack-name $stackName
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