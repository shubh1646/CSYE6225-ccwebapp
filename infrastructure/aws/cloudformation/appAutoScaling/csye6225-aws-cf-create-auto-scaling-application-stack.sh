#!/bin/bash

echo "enter the name for your Application Stack(alphanumeric)"
read applicationStackName
echo "enter the name of your Virtual Private Cloud (networking) stack(alphanumeric)"
read NetworkStackName
echo "enter the name of your CircleCI Roles and Policies stack(alphanumeric)"
read CircleCIStackName
if [ -z "$applicationStackName" ] || [ -z "$NetworkStackName" ] || [ -z "$CircleCIStackName" ]
then
    echo "Failed: either Application Stack Name or Networking Stack Name or CircleCI Stack Name Null"
    exit
fi

echo "enter the ami id"
read amiId
if [ -z "$amiId" ]
then
    echo "Failed: entered amiId is Null"
    exit
fi
echo "enter your domain name"
read domainName
if [ -z "$domainName" ]
then
    echo "Failed: entered domainName is Null"
    exit
fi

aws cloudformation describe-stacks --stack-name $applicationStackName &> /dev/null
if [ $? -eq 0 ]
then
   echo "Failed: Stack by this name already exist"
   exit
fi
aws cloudformation describe-stacks --stack-name $NetworkStackName &> /dev/null
if [ $? -ne 0 ]
then
   echo "Failed: Your Virtual Private Cloud Stack not setup"
   exit
fi
aws cloudformation describe-stacks --stack-name $CircleCIStackName &> /dev/null
if [ $? -ne 0 ]
then
   echo "Failed: Your CircleCI Stack not setup"
   exit
fi

certificateArn=$(aws acm list-certificates --output text --query 'CertificateSummaryList[0].CertificateArn' 2> /dev/null)
if [ $? -ne 0 ]
then
    echo "Failure: could not get SSL Certificate, either not created"
    exit
fi
hostedZone=$(aws route53 list-hosted-zones --output text --query 'HostedZones[0].Id' 2> /dev/null)
if [ $? -ne 0 ]
then
    echo "Failure: could not get Hosted Zones Corresponding to your domain"
    exit
fi

hostedZoneID=${hostedZone:12}
echo $hostedZoneID

status=$(aws cloudformation create-stack --stack-name $applicationStackName \
--template-body file://csye6225-cf-auto-scaling-application.json \
--parameters \
ParameterKey=NetworkStackName,ParameterValue=$NetworkStackName \
ParameterKey=amiId,ParameterValue=$amiId \
ParameterKey=CircleCIStackName,ParameterValue=$CircleCIStackName \
ParameterKey=domainName,ParameterValue=$domainName \
ParameterKey=certificateArn,ParameterValue=$certificateArn \
ParameterKey=hostedZoneID,ParameterValue=$hostedZoneID \
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