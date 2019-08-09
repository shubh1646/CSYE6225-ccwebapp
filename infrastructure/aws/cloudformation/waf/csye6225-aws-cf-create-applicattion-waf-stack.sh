#!/bin/bash

echo "enter the name for your Application Stack(alphanumeric)"
read applicationWAFStackName


aws cloudformation describe-stacks --stack-name $applicationWAFStackName &> /dev/null
if [ $? -eq 0 ]
then
   echo "Failed: Stack by this name already exist"
   exit
fi
status=$(aws cloudformation create-stack --stack-name $applicationWAFStackName \
--template-body file://csye6225-cf-application-waf.json --capabilities CAPABILITY_NAMED_IAM --on-failure DELETE)

if [ $? -eq 0 ]
then
    echo "please wait....."
    aws cloudformation wait stack-create-complete --stack-name $applicationWAFStackName
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