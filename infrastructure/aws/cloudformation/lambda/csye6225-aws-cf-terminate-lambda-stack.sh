echo "Enter the stack name that need to be deleted"
read stackName

aws cloudformation describe-stacks --stack-name $stackName  &> /dev/null

if [ $? -ne 0 ]
then
	echo "Failed: stack by this name does not exist"
	exit
fi
$(aws cloudformation  delete-stack --stack-name $stackName)

if [ $? -eq 0 ]
then
	echo "deleting..........***.........."
	aws cloudformation wait stack-delete-complete  --stack-name $stackName
	if [ $? -eq 0 ]
	then 
	
    echo "Yess !!! Stack is deleted !"
	else
		echo "delete command failure"
	fi
else
	echo "delete command failure"
fi
