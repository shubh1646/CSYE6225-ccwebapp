#!/bin/bash

echo "enter the name for your stack(alphanumeric)"
read stackName

aws cloudformation describe-stacks --stack-name $stackName &> /dev/null

if [ $? -eq 0 ]
then
	echo "Failed: stack by this name already exist"
	exit
fi

if [ -z "$stackName" ]
then
    echo "Failed: enter a stack name"
    exit
fi

echo "enter the CidrBlock you wish for your VPC, should be of the format - 10.0.0.0/16"
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
--template-body file://csye6225-cf-networking.json \
--parameters ParameterKey=vpcCidrBlock,ParameterValue=$vpcCidrBlock  ParameterKey=subnetCidrBits,ParameterValue=$subnetCidrBits \
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

