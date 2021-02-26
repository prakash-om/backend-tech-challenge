#### Gini Challenge

This project is a scheduler which runs for weekly basis (as of now 3 minutes for testing) and check the database for null serial numbers and update them with the help of mock service.


#### There are major four components which by which I am trying to solve this challenge

##### Null SerialNumber retrieval from database
* As we know there are millions of records in table and retrieving all the records at one shot is not god idea, so I am using limit clause in database query. limit clause have start and max inputs.
* max property in above clause is present in application.properties file and it is configurable according your hardware capacity
* start index is something which is maintained in database table called query_offset. Reason is start and max 

##### Messaging queue usage to support bulk messages

##### Java BiqQueues to support bulk database operations 

 * SchedulerService retrieve the documents without a serial number and start processing by putting them into messaging queue (RabbitMQ is used)
 * In above task the scheduler will not retrieve all the documents at one go, but limit will be applied while querying.max limit is configurable which is present in appliction.propertis file as db.max.rows property.  
 * call the external client endpoint at ${client.api.uri} for each document separately (mock is provided)
 * check the returned processing status and if OK, extract the serial number
 * persist the serial number for the document

Technical constraints and details:
* Client's API is not very efficient and each call is very heavy, up to several seconds
* Client API only expects document id
* It's planned to deploy multiple instances of your Spring Boot service

#### Bonus optional challenges

Imagine there is another microservice called document-collector where users can add or delete pages to existing documents.
You are not concerned how the service works. All you need to know is that document-collector calls your service to update page count in the database.
Create a REST endpoint that accepts document id and number of added/removed pages (not the total number of pages in the document).
Update the document count in the database.

`Technical constraints: new endpoint should handle thousands of requests each second as there are several instances of document-collector running.`

Optionally cover all your code with tests.


#### Notes

You are encouraged to make arbitrary decisions on details that are not specified. Please let us know about these decisions and reasoning behind them in the readme.

Do not feel pressured to cover all the points.
It's much better to cover few points and do them well than do half-hearted job on all of them.
Feel free to use the provided skeleton project or start from scratch on your own.

You can use Maven or Gradle.
