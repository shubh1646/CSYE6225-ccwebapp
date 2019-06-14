# CSYE 6225 - Summer 2019

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| shubham sharma| 001447366 | sharma.shubh@husky.neu.edu|
| Cyril Sebestian | 001448384 | sebastian.c@huky.neu.edu |
| Mansi Gandhi | 001494387 | gandhi.man@husky.neu.edu |

## Technology Stack
This web application is developed using spring boot and uses rest controller for achieving any use case. 
 
## Build Instructions
Pre-Requisites: Need to have postman installed
1. Clone this repository:git@github.com:cyrilsebastian1811/ccwebapp.git into the local system using SSH Key. 
2. Traverse to the folder csye6225/dev/ccwebapp/webapp
3. Download all maven dependencies by going to File > Settings > Maven > Importing. 
4. Run WebappApplication by going to csye6225/dev/ccwebapp/webapp/src/main/java/com/neu/webapp/WebappApplication.java

Note: -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' might have to tweak based on the credentials.
## Deploy Instructions
	Register an User: 
	curl -X POST \
	  http://localhost:8080/user/register \
	  -H 'Accept: */*' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'Postman-Token: f7773239-edd0-413b-a7a9-c498efe29453,bad696de-cb78-4144-9322-4e1f3968f095' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache' \
	  -H 'content-length: 67' \
	  -d '{
		"emailId" : "mansigandhi@gmail.com",
		"password" : "Mansi@123"
	}'
	Response: {
	    "emailIdError": "-",
	    "passwordError": "-"
	}

	Retrieve Current Time:
	curl -X GET \
	  http://localhost:8080/ \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'Postman-Token: 60dc9aef-cb42-4ddb-8663-15c87081a6b4,948e561d-e402-4dcf-b1bc-d38c84b5c570' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache'
	Response: Welcome  current time: 03:13:07

	Register a Book:
	curl -X POST \
	  http://localhost:8080/book \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'Postman-Token: a2de4ff2-67b5-4d5f-9c55-12748e100b11,ea9df96d-0d23-457f-85d9-d0e241373562' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache' \
	  -H 'content-length: 126' \
	  -d '{
		"title" : "The Adventures of Sherlock Holmes",
		"author" : "David Shroff",
		"isbn" : "123-9876543210",
		"quantity" : "12"
	}'
	Response: 
	{
	    "id": "af367e68-ff49-4905-aa7c-6bdedaf1f5b0",
	    "title": "The Adventures of Sherlock Holmes",
	    "author": "David Shroff",
	    "isbn": "123-9876543210",
	    "quantity": 12
	}

	Retrieve a book by id:
	curl -X GET \
	  http://localhost:8080/book/af367e68-ff49-4905-aa7c-6bdedaf1f5b0 \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'Postman-Token: 5a40059d-6d7a-4296-9fb3-2ab766b9b9df,636c8ee4-6533-46c6-997e-60e5682c5157' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache'
	Response: 
	{
	    "id": "af367e68-ff49-4905-aa7c-6bdedaf1f5b0",
	    "title": "The Adventures of Sherlock Holmes",
	    "author": "David Shroff",
	    "isbn": "123-9876543210",
	    "quantity": 12
	}

	Delete a book:
	curl -X DELETE \
	  http://localhost:8080/book/af367e68-ff49-4905-aa7c-6bdedaf1f5b0 \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'Postman-Token: ab4dc3e4-8733-4fe7-ad2e-f2f31511236c,f9179c4e-9b05-4296-aa9e-958745e89154' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache' \
	  -H 'content-length: '
	Response: Blank with 204 No Content Status Code
	
	Update a book:
	curl -X PUT \
	  http://localhost:8080/book \
	  -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' \
	  -H 'Content-Type: application/json' \
	  -H 'Postman-Token: ed850f17-8b8f-4f6a-96f5-691801127529' \
	  -H 'cache-control: no-cache' \
	  -d '{
	    "id": "c51de37e-c380-4fb4-996a-50c9c826bc8e",
	    "title": "Java Basics",
	    "author": "Mark Munson",
	    "isbn": "978-0132126954",
	    "quantity": 10
	}'
	Response: Blank with 204 No Content Status Code
	
	Get all books:
	curl -X GET \
	  http://localhost:8080/book \
	  -H 'Accept: */*' \
	  -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' \
	  -H 'Cache-Control: no-cache' \
	  -H 'Connection: keep-alive' \
	  -H 'Content-Type: application/json' \
	  -H 'Host: localhost:8080' \
	  -H 'Postman-Token: e5377569-135b-4772-bc0f-f03d8e2d4b30,fc190521-b4f5-40fc-a866-d145d6cb61fd' \
	  -H 'User-Agent: PostmanRuntime/7.13.0' \
	  -H 'accept-encoding: gzip, deflate' \
	  -H 'cache-control: no-cache'
	  Response: Retrieves all books in the database
	  
	Get Cover Image of a given Book:
	curl -X GET \
	  http://localhost:8080/book/179d316f-6ca2-4317-9d66-94d1d3409fd1/image/75f83e7b-790e-4d83-a980-1d7316e7e705 \
	  -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' \
	  -H 'Content-Type: application/json' \
	  -H 'Postman-Token: 543e5a1d-ca9a-4773-8ecb-2e2c088ec172' \
	  -H 'cache-control: no-cache'
	  Response:
	{
	    "id": "75f83e7b-790e-4d83-a980-1d7316e7e705",
	    "url": "/home/cyrilsebastian/Desktop/BookCovers/179d316f-6ca2-4317-9d66-94d1d3409fd1-cover2.png"
	}
	
	Add Cover Image for a given Book:
	curl -X POST \
	  http://localhost:8080/book/cfe6d387-acb3-40e2-8f95-1a77e37e32ba/image \
	  -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' \
	  -H 'Content-Type: application/json' \
	  -H 'Postman-Token: d69f2a39-2ff9-4ef1-ae44-8cde7e3eb9de' \
	  -H 'cache-control: no-cache' \
	  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
	  -F image=@/home/cyrilsebastian/Desktop/cover2.png
	  Response:
	{
	    "id": "07efa585-4460-4d03-a390-a6cc2910b843",
	    "url": "/home/cyrilsebastian/Desktop/BookCoverImages/cfe6d387-acb3-40e2-8f95-1a77e37e32ba-cover2.png"
	}
	
	Update Cover Image of a given Book:
	curl -X PUT \
	  http://localhost:8080/book/cfe6d387-acb3-40e2-8f95-1a77e37e32ba/image/07efa585-4460-4d03-a390-a6cc2910b843 \
	  -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' \
	  -H 'Content-Type: application/json' \
	  -H 'Postman-Token: 1cce9d3f-8d4e-4cde-b94b-379187a27631' \
	  -H 'cache-control: no-cache' \
	  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
	  -F image=@/home/cyrilsebastian/Desktop/cover1.jpg
	  Response: Blank with 204 No Content Status Code
	  
	Delete Cover Image of a given Book:
	curl -X DELETE \
	  http://localhost:8080/book/cfe6d387-acb3-40e2-8f95-1a77e37e32ba/image/07efa585-4460-4d03-a390-a6cc2910b843 \
	  -H 'Authorization: Basic bWFuc2lnYW5kaGlAZ21haWwuY29tOk1hbnNpQDEyMw==' \
	  -H 'Content-Type: application/json' \
	  -H 'Postman-Token: d9132f37-020f-45d9-85c4-97befc14911b' \
	  -H 'cache-control: no-cache' \
	  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
	  Response: Blank with 204 No Content Status Code

## Running Tests
Used mockito and junit for test case.
Run WebappApplication test cases: open the webapp aplication on your IDE -> right click on webapp -> Run 'All Tests'

## CI/CD
