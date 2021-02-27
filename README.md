#### Gini Challenge

* This project is a scheduler which runs for weekly basis (as of now 3 minutes for testing) and check the database for null serial numbers and update them with the help of mock service.
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

#### There are major four components which by which I am trying to solve this challenge

##### Null SerialNumber retrieval from database
* As we know there are millions of records in table and retrieving all the records at one shot is not god idea, so I am using limit clause in database query. limit clause have start and max inputs.
* example: (SELECT id, serial_number from document where serial_number is null limit startIndex, max)
* max property in above clause is present in application.properties file and it is configurable according your hardware capacity
* start index is the starting row from where records fetch has to start. it is  maintained in database table called query_offset. Reason for creating separate table is for start index is, suppose there are multiple instance running of this service then startIndex will be used to avoid duplicate rows.    

##### Messaging queue usage to support bulk messages
* Once documents are retrieved from database it is pushed to rabbitmq queues to further process.
* queue listener will registered in the beginning. There are 3 queues used in this service. 
* once queue receive message it will connect to mock service  and try to get result for id. There is threadpool used when connecting to mockserivce and poolsize is configurable. 
* If mock result is INCOMPLETE then there is a retry which will happen for that id. The max retry count is configurable. 
* If result is OK then message will be push to other queue.
* I am not updating serialNumber directly, instead I am pushing data to queue for batch update process. 

##### Java BiqQueues to support bulk database operations 
* BiqQueues are used as java persistent queue which holds up the document till batch size (configurable) then once the limit is exceeds it will call the batch update api which will update the serialNumber in batches 

##### Bonus optional challenges
* api endpoint: http://localhost:8080/V1/documents PATCH request which expect id and pages input.
* The patch has been introduced for this purpose it's a asyc request which responded with 202 accepted status.
* Once the request comes it will directed to queue, then once the listener pick that up it will push the patch data to BiqQueue. 
* And once the max result in the queue is reached then, everything from BigQueue is dequeued and pushed to batch operations. 

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

#### Technology used 
* Java
* SpringBoot
* H2 Database
* RabbitMQ
* BiqQueue

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

#### How Run
* Using Docker 
* sudo docker image build -t backend-challenge .
* sudo docker network create gini
* sudo docker-compose up
* Using IDE
* Download project and import as Gradle project.
* Make sure you are running RabbitMQ instance and give the configuration in applicaiton.properties file.
* Run as SpringBoot application.


------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

#### High level service 
![Test](src/main/resources/architecture.png?raw=true "Architecture Diagram")

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

#### Notes
* Service has designed to run as multiple instances 
* Database is used here is H2 in memory for simplicity purpose but can be changed through configuration.
* Java BiqQueue is used for persistent queues and can be configured for any other technology like redis or kafka.
* Update status has not maintained for patch api (bonus challenges).

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



