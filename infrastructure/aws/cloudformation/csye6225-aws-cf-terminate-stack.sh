echo "Enter the stack name that need to be deleted"
read stackName

  

aws cloudformation  delete-stack --stack-name $stackName

echo "deleting.........."
aws cloudformation wait stack-delete-complete  --stack-name $stackName


if [ $? -eq 0 ]
	then 
	echo "Deletion sucess!"

else

echo "delete command failure"

fi  