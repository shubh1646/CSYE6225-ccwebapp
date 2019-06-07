# CSYE 6225 - Summer 2019

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| shubham sharma| 001447366 | sharma.shubh@husky.neu.edu|
| Cyril Sebestian | 001448384 | sebastian.c@huky.neu.edu |
| Mansi Gandhi | 001448384 | gandhi.man@husky.neu.edu |

## Technology Stack
This web application is developed using spring boot and uses rest controller for achieving any use case. 
 
## Build Instructions
Pre-Requisites: Need to have postman installed
1. Clone this repository:git@github.com:cyrilsebastian1811/ccwebapp.git into the local system using SSH Key. 
2. Traverse to the folder home/mansi/Desktop/csye6225/dev/ccwebapp/webapp
3. Download all maven dependencies by going to File > Settings > Maven > Importing. 
4. Run WebappApplication by going to csye6225/dev/ccwebapp/webapp/src/main/java/com/neu/webapp/WebappApplication.java

## Deploy Instructions
	Register an User: 
	POST: localhost:8080/user/register
	Auth: No Auth 
	Body: <br/>
	{
		"emailId" : "mansigandhi@gmail.com",
		"password" : "Mansi@123"
	}
	Response: {
	    "emailIdError": "-",
	    "passwordError": "-"
	}

	Retrieve Current Time:
	GET: localhost:8080/ 
	Auth: Basic Auth
	username: mansigandhi@gmail.com 
	password: Mansi@123 
	Response: Welcome  current time: 03:13:07

	Register a Book:
	POST: localhost:8080/book 
	Auth : Basic Auth
	username: mansigandhi@gmail.com
	password: Mansi@123 
	Body:
	{
		"title" : "The Adventures of Sherlock Holmes",
		"author" : "David Shroff",
		"isbn" : "123-9876543210",
		"quantity" : "12"
	} 
	Response: 
	{
	    "id": "fd138038-4047-42dd-9ada-f32135ddbbcf",
	    "title": "The Adventures of Sherlock Holmes",
	    "author": "David Shroff",
	    "isbn": "123-9876543210",
	    "quantity": 12
	}

	Retrieve a book by id:
	GET: localhost:8080/book/fd138038-4047-42dd-9ada-f32135ddbbcf
	Auth : Basic Auth 
	username: mansigandhi@gmail.com
	password: Mansi@123 
	Response: Retrieved all book details with that id

	Delete a book:
	DELETE: localhost:8080/book/fd138038-4047-42dd-9ada-f32135ddbbcf
	Auth : Basic Auth 
	username: mansigandhi@gmail.com
	password: Mansi@123 
	Response: Blank with 204 No Content Status Code

## Running Tests
Used mockito and junit for test case.

## CI/CD
