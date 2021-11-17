
ReadingIsGood Application
- Java version: 11
- Database: H2
- Database schema management: liquibase
- Default url: http://localhost:8080
- Exposed endpoints: POST {{URL}}/books/login, POST {{URL}}/books/customer, POST {{URL}}/books/orders, GET {{URL}}/books/orders, GET {{URL}}/books/orders/customer, {{URL}}/swagger-ui.html and metrics endpoints like {{URL}}/actuator, {{URL}}/actuator/health, {{URL}}/actuator/prometheus
- OpenAPI specification (api-docs-3.yaml) enclosed in the folder 
- Postman collection (Books API.postman_collection.json) enclosed in the folder
- To run the project from a terminal "mvn spring-boot:run" command can be run inside the project.
- Integration tests can be found on the test package. BookStoreControllerIntegrationTest class holds some basic test cases that can be used to test the endpoints in an automated way.
- There is also a Dockerfile to start the application in a container. 
  To build the image "docker build -t getir/reading-is-good-docker . " 
  and to run the built image "docker run -p 8080:8080 getir/reading-is-good-docker" commands can be used.
  
Implementation Details

- All the endpoints can be found in src/main/java/com/getir/controller/BookStoreController
- All the business related endpoints are secured by a JWT token (except /login )
- The database tables are automatically created during startup by liquibase (changelog file can be found at src/main/resources/db/changelog)
- Name of the database files are Customer, Customer_Aud, Book, BookOrder, CustomerOrder, CustomerOrderAud and RevisionInfo
- Hibernate Envers has been used to log the changes that are made on entities.
- Database console can be reached from http://localhost:8080/h2-console. default username is: sa and password can be left empty.
- The books are inserted into the database during startup. Only 3 books are available and their ISBN's are 1111111111111,2222222222222,3333333333333. Any other values others than these while creating an order will result in error. 
- API request and response model classes can be found under src/main/java/com/getir/model. Validations are included in the POST request to prevent the user from abusing the endpoints with invalid values. Validation errors result in a 400 Bad Request  


How to call the endpoints?

- POST {{URL}}/books/login endpoint should be called before the others to get an authentication token. It is called with a query parameter "username" and returns a Bearer token that can be used as an authentication to call the other APIs. An example of how to call this endpoint can be found in OpenAPI schema or postman collection.
- POST {{URL}}/books/customer endpoint is used to create a customer. The request must have first_name, last_name, customer_email, phone_number, address and username while creating a customer. Failure to provide one of them will result in a 400 Bad Request Error with an error message. After the customer is created a Location header is returned in the response. That is the email of the customer that can be used while getting the details of an order with another endpoint.
- POST {{URL}}/books/orders endpoint is used to create an order. The request must have customer_email and book_isbns (a list of strings) in the request. Books are already created in the database by a liquibase update. The existing book isbns are (1111111111111, 2222222222222, 3333333333333) any other book isbns sent in the request will result in 400 Bad Request since the book doesn't exist in the database. After the order is created a Location header is returned in the response. That is the id of the order that can be used while getting the details of an order with another endpoint.
- GET {{URL}}/books/orders/customer endpoint is used to get the orders of a specific customer by sending customer email as a query parameter. 
- GET {{URL}}/books/orders endpoint is used to get the details of a specific orders by sending order id as a query parameter. (returned in Location header of create order call)  
- GET {{URL}}/swagger-ui/index.html?usrl=/v3/api-docs returns the Swagger UI for the endpoints
- GET {{URL}}://localhost:8080/v3/api-docs/ returns the OpenApi json file generated for the endpoints
- GET {{URL}}/actuator/prometheus is used to examine the metrics of the system. The duration of calls and the number of times that they are called are collected as metrics and can be found here.
  
Sample response: http_server_requests_seconds_count{exception="NotFoundException",method="GET",outcome="CLIENT_ERROR",status="404",uri="/books/orders",} 1.0
  http_server_requests_seconds_sum{exception="NotFoundException",method="GET",outcome="CLIENT_ERROR",status="404",uri="/books/orders",} 0.106615625
 HELP http_server_requests_seconds_max
 TYPE http_server_requests_seconds_max gauge
http_server_requests_seconds_max{exception="None",method="POST",outcome="CLIENT_ERROR",status="403",uri="root",} 0.261825709
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/books/orders/customer",} 0.0