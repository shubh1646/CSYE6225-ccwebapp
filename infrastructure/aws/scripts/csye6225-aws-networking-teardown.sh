#!/bin/bash

echo "--------------------------Teardown--------------------------"
echo ""

echo "Enter a name of the VPC you want to delete"
read myVpc


# =======================================================Describe Statements==========================================================

vpcId=$(aws ec2 describe-vpcs --filters "Name=tag:Name,Values=$myVpc" --output text --query 'Vpcs[0].VpcId' 2> /dev/null)
echo $vpcId
if [ "$vpcId" = "None" ]
then
    echo "No vpc with the name $myVpc exists"
    exit
fi
# ---------------------------------------------------------------------------------------------------------------------------------------
igwId=$(aws ec2 describe-internet-gateways --filters "Name=tag:Name,Values=$myVpc-IGW" --output text \
--query 'InternetGateways[0].InternetGatewayId' 2> /dev/null)
echo $igwId
if [ "$igwId" = "None" ]
then
    echo "No Internet Gateway with the name $myVpc-IGW exists"
    exit
fi
# ---------------------------------------------------------------------------------------------------------------------------------------
subnetIds=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$vpcId" --output text \
--query 'Subnets[*].SubnetId')
echo $subnetIds
if [ "$subnetIds" = "None" ]
then
    echo "No Subnets exists in $myVpc"
    exit
fi
# ---------------------------------------------------------------------------------------------------------------------------------------
routeTableId=$(aws ec2 describe-route-tables --filters "Name=tag:Name,Values=$myVpc-RouteTable" --output text \
--query 'RouteTables[0].RouteTableId')
echo $routeTableId
if [ "$routeTableId" = "None" ]
then
    echo "No RouteTable with the name $myVpc-RouteTable exists"
    exit
fi
# ---------------------------------------------------------------------------------------------------------------------------------------
routeTableAssociations=$(aws ec2 describe-route-tables --filters "Name=route-table-id,Values=$routeTableId" --output text \
--query "RouteTables[0].Associations[*].RouteTableAssociationId" 2> /dev/null)
if [ -z "$routeTableAssociations" ]
then
    echo "No Associations exists in $myVpc-RouteTable"
    exit
fi
read -ra associationIds <<< "$routeTableAssociations"
echo ${associationIds[@]}

echo ""
# ===========================================================Detach Statements=============================================================

aws ec2 detach-internet-gateway --internet-gateway-id $igwId --vpc-id $vpcId
if [ $? -ne 0 ]
then
    echo "Could not dettach $myVpc-IGW from $myVpc"
    exit
fi

# ===========================================================Delete Statements===========================================================

aws ec2 delete-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0
if [ $? -eq 0 ]
then
    echo "Successfully deleted Route from $myVpc-RouteTable"
else
    echo "Failure: coudn't delete $myVpc-RouteTable"
    exit
fi
# ---------------------------------------------------------------------------------------------------------------------------------------
k=0
for associationId in ${associationIds[@]};
do
    aws ec2 disassociate-route-table --association-id $associationId &> /dev/null
    if [ $? -eq 0 ]
    then
        echo "Successfully deleted Association$(( k+1 )) from $myVpc-RouteTable"
        (( k++ ))
    else
        echo "Failure: coudn't delete the $myVpc-RouteTable"
        exit
    fi
done
# ---------------------------------------------------------------------------------------------------------------------------------------
aws ec2 delete-route-table --route-table-id $routeTableId &> /dev/null
if [ $? -eq 0 ]
then
    echo "Successfully deleted $myVpc-RouteTable"
else
    echo "Failure: coudn't delete $myVpc-RouteTable"
    exit
fi
# ---------------------------------------------------------------------------------------------------------------------------------------
k=0
for subnetId in $subnetIds
do
    aws ec2 delete-subnet --subnet-id $subnetId &> /dev/null
    if [ $? -eq 0 ]
    then
        echo "Successfully deleted Subnet with name $myVpc-Subnet$(( k+1 ))"
        (( k++ ))
    else
        echo "Failure: coudn't delete $myVpc-Subnet$(( k+1 ))"
        exit
    fi
done
# ---------------------------------------------------------------------------------------------------------------------------------------
aws ec2 delete-internet-gateway --internet-gateway-id $igwId &> /dev/null
if [ $? -eq 0 ]
then
    echo "Successfully deleted Internet Gateway with name $myVpc-IGW"
else
    echo "Failure: coudn't delete $myVpc-IGW"
    exit
fi
# ---------------------------------------------------------------------------------------------------------------------------------------
aws ec2 delete-vpc --vpc-id $vpcId &> /dev/null
if [ $? -eq 0 ]
then
    echo "Successfully deleted Vpc with name $myVpc"
else
    echo "Failure: coudn't delete $myVpc"
    exit
fi

echo ""
echo "--------------------------"
echo "--------------------------"
echo "Stack deleted Successfully"
echo "--------------------------"
echo "--------------------------"