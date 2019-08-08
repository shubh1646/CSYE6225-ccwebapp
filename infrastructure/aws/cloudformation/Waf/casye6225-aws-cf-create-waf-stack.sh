#!/bin/bash

echo "Enter the Stack name: "
read stack_name

echo "Initiating the script..."
echo "Checking if the stack already exists..."

if  aws cloudformation describe-stacks --stack-name $stack_name ; then

   echo -e "Stack already exists, terminating a stack..."

 else

   echo -e "Stack does not exist, creating a stack..."
   aws_response=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-waf.json --on-failure DELETE)

   echo "Waiting for stack to be created ..."
   aws cloudformation wait stack-create-complete --stack-name $stack_name

   echo "Stack Id = $aws_response created successfully!"

 echo "Script completed successfully!"
fi
