#!/bin/bash

echo "enter the name for your Application Stack(alphanumeric)"
read applicationWAFStackName
echo "enter the name of your Virtual Private Cloud (networking) stack(alphanumeric)"
read NetworkStackName
if [ -z "$applicationWAFStackName" ] || [ -z "$NetworkStackName" ]
then
    echo "Failed: either Application Stack Name or Networking Stack Name"
    exit
fi


aws cloudformation describe-stacks --stack-name $applicationWAFStackName &> /dev/null
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

certificateArn=$(aws acm list-certificates --output text --query 'CertificateSummaryList[0].CertificateArn' 2> /dev/null)
if [ $? -ne 0 ]
then
    echo "Failure: could not get SSL Certificate, either not created"
    exit
fi
hostedZone=$(aws route53 list-hosted-zones --output text --query 'HostedZones[0].Id' 2> /dev/null)
# echo "${hostedZone}"
if [ $? -ne 0 ]
then
    echo "Failure: could not get Hosted Zones Corresponding to your domain"
    exit
fi
hostedZoneID=${hostedZone:12}
echo $hostedZoneID

status=$(aws cloudformation create-stack --stack-name $applicationWAFStackName \
--template-body file://csye6225-cf-application-waf.json \
--parameters \
ParameterKey=NetworkStackName,ParameterValue=$NetworkStackName \
ParameterKey=certificateArn,ParameterValue=$certificateArn \
ParameterKey=hostedZoneID,ParameterValue=$hostedZoneID \
--capabilities CAPABILITY_NAMED_IAM)

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