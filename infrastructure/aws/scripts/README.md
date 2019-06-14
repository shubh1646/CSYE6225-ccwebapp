#Task : Infrastructure Setup through AWS CLI

1. Create a Virtual Private CLoud (VPC)
2. Create 3 subnets according to the requirements in the VPC with each in different availability zones
3. Create a Internet Gateway 
4. Attach the Internet Gateway to VPC
5. Create a Public Route-Table and attach all the 3 subnets to the Route-Table
6. Create a public Route in the Public Route-Table with Destination Cidr BLock of 0.0.0.0/0 and connect to internet gateway as well. 


#Setup Steps

1. Install AWS Command Line Interface
2.  run aws configure command and enter all the credentials of the root user.
3. Create a Shell Script named csye6225-aws-networking-setup.sh to setup networking configurations through AWS CLI.
4. Create a Shell Script named csye6225-aws-networking-teardown.sh to delete complete configurations of the virtual private cloud from the network through AWS CLI.
5. Resources here are created only if the previous command is executed successfully!

#Run Shell Scripts

1. chmod 764 csye6225-aws-networking-setup.sh
2. ./csye6225-aws-networking-setup.sh
3. Enter a Name: csye6225
4. Enter availability zone 1 = us-east-1a, availability zone 2 = us-east-1b, availability zone 3 = us-east-1c   
5. chmod 764 csye6225-aws-networking-teardown.sh
6. Enter the vpc id you wish to delete and all the dependencies are deleted and later vpc is deleted from the console. 
