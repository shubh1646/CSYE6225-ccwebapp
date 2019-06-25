echo "Enter the Stack name: "
read stack_name

echo "Initiating the script..."

   aws_response=$(aws cloudformation create-stack --stack-name $stack_name --template-body file://csye6225-cf-application.json)

   echo "Waiting for stack to be created ..."
   aws cloudformation wait stack-create-complete --stack-name $stack_name

   echo "Stack Id = $aws_response created successfully!"

 echo "Script completed successfully!"

