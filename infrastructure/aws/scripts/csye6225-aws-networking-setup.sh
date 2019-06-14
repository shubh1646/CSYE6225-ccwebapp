#!/bin/bash
# create-aws-vpc

echo "Enter a name"
read name

# region=$(aws configure get region --profile default)
# echo $region

# zones=$(aws ec2 describe-availability-zones --region $region)
# echo $zones

echo "Enter availability zone 1"
read availabilityZone1 

echo "Enter availability zone 2"
read availabilityZone2

echo "Enter availabilty zone 3"
read availabilityZone3

#variables used in script:
vpcName="$name VPC"
vpcCidrBlock="10.0.0.0/16"
subnetName1="$name Subnet1"
subnetName2="$name Subnet2"
subnetName3="$name Subnet3"
subNetCidrBlock1="10.0.64.0/18"
subNetCidrBlock2="10.0.128.0/18"
subNetCidrBlock3="10.0.192.0/18"
gatewayName="$name Gateway"
routeTableName="$name Route Table"
destinationCidrBlock="0.0.0.0/0"

echo "Creating VPC..."
#create vpc with cidr block /16
vpcId=$(aws ec2 create-vpc --cidr-block $vpcCidrBlock --instance-tenancy default --query 'Vpc.VpcId' --output text)
aws ec2 create-tags --resources $vpcId --tags "Key=name,Value=$vpcName"

echo "vpc id : $vpcId vpc name :  $vpcName"

#add dns support
modify_response=$(aws ec2 modify-vpc-attribute \
 --vpc-id "$vpcId" \
 --enable-dns-support "{\"Value\":true}")
 echo $modify_response
#add dns hostnames
modify_response=$(aws ec2 modify-vpc-attribute \
  --vpc-id "$vpcId" \
  --enable-dns-hostnames "{\"Value\":true}")

echo $modify_response


subnet_response1=$(aws ec2 create-subnet \
 --cidr-block "$subNetCidrBlock1" \
 --availability-zone "$availabilityZone1" \
 --vpc-id "$vpcId" --query 'Subnet.SubnetId' --output text)
 if [ $? -eq 0 ]; then
 #name the subnet
aws ec2 create-tags \
  --resources "$subnet_response1" \
  --tags Key=Name,Value="$subnetName1"
 echo "Subnet Response 1: $subnet_response1"
else
    echo "Subnet Generation 1 Failed"
    exit
fi


subnet_response2=$(aws ec2 create-subnet \
 --cidr-block "$subNetCidrBlock2" \
 --availability-zone "$availabilityZone2" \
 --vpc-id "$vpcId" --query 'Subnet.SubnetId' --output text)
 if [ $? -eq 0 ]; then
 #name the subnet
aws ec2 create-tags \
  --resources "$subnet_response2" \
  --tags Key=Name,Value="$subnetName2"
 echo "Subnet Response 2: $subnet_response2"
else
    echo "Subnet Generation 2 Failed"
    exit
fi


subnet_response3=$(aws ec2 create-subnet \
 --cidr-block "$subNetCidrBlock3" \
 --availability-zone "$availabilityZone3" \
 --vpc-id "$vpcId" --query 'Subnet.SubnetId' --output text)
 if [ $? -eq 0 ]; then
 #name the subnet
aws ec2 create-tags \
  --resources "$subnet_response3" \
  --tags Key=Name,Value="$subnetName3"
 echo "Subnet Response 3: $subnet_response3"
else
    echo "Subnet Generation 3 Failed"
    exit
fi


gatewayId=$(aws ec2 create-internet-gateway \
            --query 'InternetGateway.InternetGatewayId' --output text)
    
if [ $? -eq 0 ]; then
    aws ec2 create-tags \
        --resources "$gatewayId" \
        --tags Key=Name,Value="$gatewayName"
    echo "Gateway Response: $gatewayId"
else
    echo "Internet Gateway Failed"
    exit
fi

#attach gateway to vpc
attach_response=$(aws ec2 attach-internet-gateway \
        --internet-gateway-id "$gatewayId"  \
        --vpc-id "$vpcId")
if [ $? -eq 0 ]; then
    echo Attach Response: $attach_response
 else
    echo "Attaching vpc to gateway failed"
    exit
fi


#enable public ip on subnet
modify_response=$(aws ec2 modify-subnet-attribute \
    --subnet-id "$subnet_response1" \
    --map-public-ip-on-launch)
if [ $? -eq 0 ]; then
    echo Public Ip: $modify_response
 else
    echo "Public ip on subnet Failed"
    exit
fi

#enable public ip on subnet
modify_response=$(aws ec2 modify-subnet-attribute \
    --subnet-id "$subnet_response2" \
    --map-public-ip-on-launch)
if [ $? -eq 0 ]; then
    echo $modify_response
 else
    echo "Public ip on subnet Failed"
    exit
fi

#enable public ip on subnet
modify_response=$(aws ec2 modify-subnet-attribute \
    --subnet-id "$subnet_response3" \
    --map-public-ip-on-launch)
if [ $? -eq 0 ]; then
    echo $modify_response
 else
    echo "Public ip on subnet Failed"
    exit
fi

#create route table for vpc
routeTableId=$(aws ec2 create-route-table \
 --vpc-id "$vpcId" --query 'RouteTable.RouteTableId' --output text )
if [ $? -eq 0 ]; then 
#name the route table
    aws ec2 create-tags \
        --resources "$routeTableId" \
        --tags Key=Name,Value="$routeTableName"
    echo Route Table: $routeTableId
else
    echo "Route Table Creation Failed"
    exit
fi

#add route to subnet
associate_response1=$(aws ec2 associate-route-table \
    --subnet-id "$subnet_response1" \
    --route-table-id "$routeTableId")
if [ $? -eq 0 ]; then
    echo Route-Subnet1: $associate_response1
else   
    echo "Route Subnet 1 Failed"
    exit
fi

#add route to subnet
associate_response2=$(aws ec2 associate-route-table \
    --subnet-id "$subnet_response2" \
    --route-table-id "$routeTableId")
if [ $? -eq 0 ]; then
    echo Route-Subnet2: $associate_response2
else   
    echo "Route Subnet 2 Failed"
    exit
fi

#add route to subnet
associate_response3=$(aws ec2 associate-route-table \
    --subnet-id "$subnet_response3" \
    --route-table-id "$routeTableId")
if [ $? -eq 0 ]; then
    echo Route-Subnet3: $associate_response3
else   
    echo "Route Subnet 3 Failed"
    exit
fi

#add route for the internet gateway
if [ $? -eq 0 ]; then
    route_response=$(aws ec2 create-route --route-table-id "$routeTableId" --destination-cidr-block "$destinationCidrBlock" --gateway-id "$gatewayId")
    echo Route For the Internet Gateway: $route_response
else
   echo "Adding route to the gateway failed"
   exit
fi
 
echo  " "
echo  "VPC created"
