#!/bin/bash

echo Please Enter the  vpc id of the stack that need to be deleted 
read VpcId

allre=$(aws ec2 describe-vpcs)
VPC=$(echo -e "$allre" |  /usr/bin/jq '.Vpcs[].VpcId' | tr -d '"')

echo "Checking for VPC..............."
for vpc in $VPC; do
    if [ $vpc = "$VpcId" ]
    then
        echo "VPC is  present !"
fi
done

echo "searching for the  InternetGatewayId............................."

interGateResponse=$(aws ec2 describe-internet-gateways --filters "Name=attachment.vpc-id,Values=$VpcId")

internetGatewayId=$(echo -e "$interGateResponse" |  /usr/bin/jq '.InternetGateways[0].InternetGatewayId' | tr -d '"')
echo "internetGatewayId : $internetGatewayId"

echo "Deteching the Internet Gateway from the stack ..........................."
aws ec2 detach-internet-gateway --internet-gateway-id "$internetGatewayId" --vpc-id "$VpcId"


echo "Deleting the Internet Gaeway........................................."
aws ec2 delete-internet-gateway --internet-gateway-id "$internetGatewayId"


echo "Geting the  routeTableId ................................................"
routeTableResponse=$(aws ec2 describe-route-tables --filters "Name=vpc-id,Values=$VpcId" "Name=association.main,Values=false")
RouteTableId=$(echo -e "$routeTableResponse" |  /usr/bin/jq '.RouteTables[].RouteTableId' | tr -d '"')
#echo "RouteTableId : $RouteTableId"
echo "Finding the  Route Table association  ID ..........................."
RouteTableAssociationId=$(echo -e "$routeTableResponse" |  /usr/bin/jq '.RouteTables[0].Associations[].RouteTableAssociationId' | tr -d '"')


echo "RouteTableAssociationId is  : $RouteTableAssociationId"

echo "dis-associating the route table from ................................................."
for routeAssoID in $RouteTableAssociationId; do
    echo "Disassociating route table : $routeAssoID"
    res=$(aws ec2 disassociate-route-table --association-id "$routeAssoID")
done

echo "Deleting the Route Table......................................................"
aws ec2 delete-route-table --route-table-id "$RouteTableId"


echo "Finding  the Subnet ID................................."
subnetRes=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$VpcId")
SubnetId=$(echo -e "$subnetRes" |  /usr/bin/jq '.Subnets[].SubnetId' | tr -d '"')
echo "SubnetId : $SubnetId"


echo "Deleting the Subnet .................................."
for subnet in $SubnetId; do
    echo "Deleting subnet : $subnet"
    aws ec2 delete-subnet --subnet-id "$subnet"
done

echo "Deleting teh VPC ......."      #last step in the process
aws ec2 delete-vpc --vpc-id "$VpcId"
echo "Stack deleted compeletely... sucesss!"

        exit
    else
        echo "VPC not found"
    fi
   
done
