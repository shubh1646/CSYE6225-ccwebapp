#!/bin/bash

echo "--------------------------Welcome--------------------------"
echo ""

AvailibilityZones=$(aws ec2 describe-availability-zones --output text --query 'AvailabilityZones[*].ZoneName')
read -ra AZs <<< "$AvailibilityZones"
echo "Enter a name for your VPC"
read myVpc
echo "Enter a cidr block you wish for your VPC"
read myVpcCidrBlock
echo "No of Subnets required"
read number

# myVpc="CustomVpc"
# myVpcCidrBlock="10.0.0.0/16"
# number=3
declare -a subnet_cidrArray
echo "Enter CidrBlocks for your subnets"
for(( i = 0; i < $number ; i++ ))
do
    echo "Subnet$(( i+1 )) CidrBlock:"
    read cidr
    subnet_cidrArray[$i]="$cidr"
done

echo ""
# =======================================================================================================================================

vpcId=$(aws ec2 create-vpc --cidr-block $myVpcCidrBlock --instance-tenancy default --output text --query 'Vpc.VpcId' 2> /dev/null)
if [ $? -ne 0 ]
then
    echo "Failure: The Cidr Block you entered is not valid"
    exit
fi
aws ec2 modify-vpc-attribute --vpc-id $vpcId --enable-dns-support "{\"Value\":true}"
aws ec2 modify-vpc-attribute --vpc-id $vpcId --enable-dns-hostnames "{\"Value\":true}"
aws ec2 create-tags --resources $vpcId --tags Key=Name,Value=$myVpc
echo "$myVpc Created ................"

# ---------------------------------------------------------------------------------------------------------------------------------------

igwId=$(aws ec2 create-internet-gateway --output text --query 'InternetGateway.InternetGatewayId' 2> /dev/null)
if [ $? -ne 0 ]
then
    echo "Failure: coudn't create $myVpc-IGW"
    exit
fi
aws ec2 create-tags --resources $igwId --tags Key=Name,Value="$myVpc-IGW"
echo "$myVpc-IGW Created ................"

aws ec2 attach-internet-gateway --internet-gateway-id $igwId --vpc-id $vpcId 2> /dev/null
if [ $? -ne 0 ]
then
    echo "Failure: Could not attach $myVpc-IGW to your $myVpc"
    exit
fi
echo "$myVpc-IGW Attachment to $myVpc Completed ................"

# ---------------------------------------------------------------------------------------------------------------------------------------

declare -a subnetIds
a=0
while [ $a -lt $number ]
do
    subnetId=$(aws ec2 create-subnet --vpc-id $vpcId --cidr-block ${subnet_cidrArray[$a]} --availability-zone ${AZs[$a]} \
    --output text --query 'Subnet.SubnetId' 2> /dev/null)
    subnetIds[$a]=$subnetId
    if [ $? -ne 0 ]
    then
        echo "Failure: coudn't create $myVpc-Subnet$(( a+1 ))"
        exit
    fi

    aws ec2 create-tags --resources ${subnetIds[$a]} --tags Key=Name,Value="$myVpc-Subnet${a+1}"
    echo "$myVpc-Subnet$(( a+1 )) Created ................"

    (( a++ ))
done

# ---------------------------------------------------------------------------------------------------------------------------------------

routeTableId=$(aws ec2 create-route-table --vpc-id $vpcId --output text --query 'RouteTable.RouteTableId' 2> /dev/null)
if [ $? -ne 0 ]
then
    echo "Failure: coudn't create Route Table with the name $myVpc-RouteTable"
    exit
fi
aws ec2 create-tags --resources $routeTableId --tags Key=Name,Value="$myVpc-RouteTable"
echo "$myVpc-RouteTable Created ................"

# ---------------------------------------------------------------------------------------------------------------------------------------

status=$(aws ec2 create-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $igwId \
--output text --query 'Return' 2> /dev/null)
if [ "$status" = "False" ]
then
    echo "Failure: coudn't add Route 0.0.0.0/0 to your $myVpc-RouteTable"
    exit
fi
echo "Route 0.0.0.0/0 add to $myVpc-RouteTable ................"

# ---------------------------------------------------------------------------------------------------------------------------------------

for (( i=0; i<${#subnetIds[@]} ; i++ ))
do
    associationId=$(aws ec2 associate-route-table --route-table-id $routeTableId --subnet-id ${subnetIds[$i]} \
    --output text --query 'AssociationId' 2> /dev/null)
    if [ -z $associationId ]
    then
        echo "Failure: coudn't associate Subnet-$(( i+1 )) to $myVpc-RouteTable"
        exit
    fi
    echo "Associated $myVpc-Subnet$(( i+1 )) to $myVpc-RouteTable ................"
done

echo ""
echo "--------------------------"
echo "--------------------------"
echo "Stack created Successfully"
echo "--------------------------"
echo "--------------------------"