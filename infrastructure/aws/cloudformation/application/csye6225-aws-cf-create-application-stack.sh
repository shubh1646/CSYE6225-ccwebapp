#!/bin/bash

echo "enter the name for your Application Stack(alphanumeric)"
read applicationstackName
echo "enter the name of your Virtual Private Cloud stack(alphanumeric)"
read networkingstackName

if [ -z "$applicationstackName" ] || [ -z "$networkingstackName" ]
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
aws cloudformation describe-stacks --stack-name $networkingstackName &> /dev/null
if [ $? -ne 0 ]
then
	echo "Failed: Your Virtual Private Cloud Stack not setup"
	exit
fi

echo "enter the "
read vpcCidrBlock
if [ -z "$vpcCidrBlock" ]
then
    echo "Failed: enter a CidrBlock representation for you VPC"
    exit
fi


echo "enter the CidrBits of the Subnet between 4-16"
read subnetCidrBits
if [ -n $subnetCidrBits ]
then
    if [[ ( $subnetCidrBits -gt 16 ) || ( $subnetCidrBits -lt 4 ) ]]
    then
        exit
    fi
else
    echo "cidrBits cant be null"
    exit
fi


status=$(aws cloudformation create-stack --stack-name $stackName \
--template-body file://csye6225-cf-networking.json --on-failure DELETE)
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
