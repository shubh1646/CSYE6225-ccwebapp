#Task : Infrastructure Setup through Cloud Formation

1. Create a Virtual Private CLoud (VPC)
2. Create 3 subnets according to the requirements in the VPC with each in different availability zones
3. Create a Internet Gateway 
4. Attach the Internet Gateway to VPC
5. Create a Public Route-Table and attach all the 3 subnets to the Route-Table
6. Create a public Route in the Public Route-Table with Destination Cidr BLock of 0.0.0.0/0 and connect to internet gateway as well. 

#Setup Steps

1. Install AWS Command Line Interface
2. run aws configure command and enter all the credentials for the root user.
3. Create a Shell Script csye6225-aws-cf-create-stack.sh to setup network configurations through AWS Cloudformation.
4. Create a Shell Script csye6225-aws-cf-terminate-stack.sh to delete complete stack through AWS Cloudformation.
5. Create a Json File csye6225-cf-networking.json including parameters and resources to configure stack. 

#Run Shell Scripts

1. chmod 764 csye6225-aws-cf-create-stack.sh
2. ./csye6225-aws-cf-create-stack.sh
3. Enter Stack Name and Availability Zones   
4. chmod 764 csye6225-aws-cf-terminate-stack.sh
5. Enter the vpc id you wish to delete and all the dependencies are deleted and later vpc is deleted from the console. 
