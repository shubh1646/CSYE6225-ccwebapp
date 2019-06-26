#!/bin/bash

echo "enter the name for your Application Stack(alphanumeric)"
read applicationStackName
echo "enter the name of your Virtual Private Cloud stack(alphanumeric)"
read networkStackName

if [ -z "$applicationStackName" ] || [ -z "$networkStackName" ]
then
    echo "Failed: Enter a valid name for both application stack and networking stack"
    exit
fi

aws cloudformation describe-stacks --stack-name $applicationStackName &> /dev/null
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
if [ -z "domainName" ]
then
    echo "Failed: enter a domainName"
    exit
fi

echo "enter the ami id"
read amiId
if [ -z "amiId" ]
then
    echo "Failed: enter a Ami Id"
    exit
fi

status=$(aws cloudformation create-stack --stack-name $applicationStackName \
--template-body file://csye6225-cf-application.json \
--parameters \
ParameterKey=networkStackName,ParameterValue=$networkStackName \
ParameterKey=domainName,ParameterValue=$domainName \
ParameterKey=amiId,ParameterValue=$amiId \
--on-failure DELETE)