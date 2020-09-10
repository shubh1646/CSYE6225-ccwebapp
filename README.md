# AWS (CSYE 6225)

---------------------------------------------------------------

### Summary

This is a  web application Library Management system built with spring
boot and deployed on AWS

-   EC2 instances are built on a custom
    [AMI](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/AMIs.html)
    using [packer](https://packer.io/)
-   Setting up the network and creation of resources is automated with
    Cloud formation, aws cli and shell scripts
-   Instances are autoscaled with
    [ELB](https://aws.amazon.com/elasticloadbalancing/) to handle the
    web traffic
-   Created a [serverless](https://aws.amazon.com/lambda/) application
    to facilitate the password reset functionality using
    [SES](https://aws.amazon.com/ses/) and
    [SNS](https://aws.amazon.com/sns/)
-   The application is deployed with Circle CI and AWS Code Deploy

### Architecture Diagram

 ![aws_full](https://user-images.githubusercontent.com/42703011/92800898-211c7580-f383-11ea-9b4e-76c171fca750.png)


Tools and Technologies
----------------------
                          
| Infrastructure       | VPC, ELB, EC2, Route53, Cloud formation, Shell, Packer |
|----------------------|--------------------------------------------------------|
| Webapp               | Java, Spring Boot, MySQL, Maven                        |
| CI/CD                | Circle CI, AWS Code Deploy                             |
| Alerting and logging | statsd, Cloud Watch, SNS, SES, Lambda                  |
| Security             | WAF                                                    |


Infrastructure-setup
--------------------

-   Create the networking setup using cloud formation and aws cli
-   Create the required IAM policies and users
-   Setup Load Balancers, Route53, DynamoDB, SNS, SES, RDS, WAF

Webapp
------

-   The Library Management System Web application is developed using
    Java Spring Boot framework that uses the REST architecture
-   Secured the application with [Spring
    Security](https://spring.io/projects/spring-security) Basic
    [authentication](https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication)
    to retrieve user information
-   Created Maven profiles to run the app locally and when deployed on
    AWS
-   Storing the images of Book covers in S3
-   Generating [Pre-signed
    URL](https://docs.aws.amazon.com/AmazonS3/latest/dev/PresignedUrlUploadObjectJavaSDK.html)
    to with expiration of 2 minutes


## Build Instructions
Pre-Requisites: Need to have postman installed
-  Clone this repository  into the local system 
-  Go to the folder csye6225/dev/ccwebapp/webapp
-  Download all maven dependencies by going to File > Settings > Maven > Importing. 
-  Run WebappApplication by going to csye6225/dev/ccwebapp/webapp/src/main/java/com/neu/webapp/WebappApplication.java


## Running Tests
- Used mockito and junit for test case.
- Run WebappApplication test cases: open the webapp aplication on your IDE -> right click on webapp -> Run 'All Tests'


CI/CD
-----

-   Created a webhook from github to CircleCI
-   Bootstrapped the docker container in CircleCI to run the unit tests,
    integration tests and generate the artifact
-   The artifact generated is stored S3 bucket and deployed to an
    autoscaling group. ![ci-cd](https://user-images.githubusercontent.com/42703011/92802596-a7858700-f384-11ea-89db-85f0f8de8bc7.png)


Auto scaling groups
-------------------

-   Created auto scaling groups to scale to the application to handle
    the webtraffic and keep the costs low when traffic is low
-   Created cloud watch alarms to scale up and scale down the EC2
    instances

Serverless computing
--------------------

-   Created a pub/sub system with SNS and lambda function
-   When the user request for a password reset a message is published to
    the SNS topic.
-   The lambda function checks for the entry of the email in DynamoDB if
    it has no entry then it inserts a record with a TTL of 15 minutes
    and sends the notification to the user with SES ![alt
    text]![lambda](https://user-images.githubusercontent.com/42703011/92802718-c126ce80-f384-11ea-843f-a06d1267bdd9.png)


[Packer](https://packer.io/)
----------------------------

-   Implemented CI to build out an AMI and share it between organization
    on AWS
-   Created provisioners and bootstrapped the EC2 instance with required
    tools like Tomcat, JAVA, Python
    
    
## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| shubham sharma| 001447366 | sharma.shubh@husky.neu.edu|
| Cyril Sebestian | 001448384 | sebastian.c@huky.neu.edu |
| Mansi Gandhi | 001494387 | gandhi.man@husky.neu.edu |

