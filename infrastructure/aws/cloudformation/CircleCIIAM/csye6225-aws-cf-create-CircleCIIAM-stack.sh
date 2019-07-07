#!/bin/bash

echo "Enter the name for your CircleCI Roles and Policies Stack(alphanumeric)"
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
    echo "Failed: enter your domain name, which will be usedd to create a S3 bucket"
    exit
fi

aws cloudformation describe-stacks --stack-name $stackName &> /dev/null
if [ $? -eq 0 ]
then
	echo "Failed: stack by this name already exist"
	exit
fi

status=$(aws cloudformation create-stack --stack-name $stackName \
--template-body file://csye6225-cf-CircleCIIAM.json \
--parameters \
ParameterKey=domainName,ParameterValue=$domainName \
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
