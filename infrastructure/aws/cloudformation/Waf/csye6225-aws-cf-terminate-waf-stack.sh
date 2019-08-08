#!/bin/bash
echo "Enter the Stack name: "

read stack_name

echo "Initiating the script..."

echo "Checking if the stack already exists..."

if  aws cloudformation describe-stacks --stack-name $stack_name ; then


	echo -e "Stack already exists, terminating a stack..."

	aws cloudformation delete-stack --stack-name $stack_name


	echo "Waiting for stack to be terminated ..."

	aws cloudformation wait stack-delete-complete --stack-name $stack_name

	 echo "Stack terminated successfully!"

else
		echo -e "Stack does not exist!"
fi
