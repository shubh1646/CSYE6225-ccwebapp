#!/bin/bash

echo "enter the name for your Application Roles and Policies Stack"
read stackName
if [ -z "$stackName" ]
then
    echo "Failed: enter a stack name"
    exit
fi

echo "enter your domainName.csye6225.com for s3 bucket"
read domainName
if [ -z "$domainName" ]
then
    echo "Failed: enter a domain name"
    exit
fi

aws cloudformation describe-stacks --stack-name $stackName &> /dev/null
if [ $? -eq 0 ]
then
	echo "Failed: stack by this name already exist"
	exit
fi

bucketName="$domainName.csye6225.com"

status=$(aws cloudformation create-stack --stack-name $stackName \
--template-body file://csye6225-cf-appIAM.json --parameters \
ParameterKey=bucketName,ParameterValue=$bucketName \
--capabilities CAPABILITY_NAMED_IAM --on-failure DELETE)
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
