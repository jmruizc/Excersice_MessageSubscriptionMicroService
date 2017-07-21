# Java 8 Exercise - Rest API - Stand-up a small microservice
Microservice specification: 
A simple message subscription service that exposes the following functionality: 

* Create a subscription 
     (Would have at least one parameter, which would be a list of messageTypes the subscription wants to keep track of) 
* Read the subscription 
* Update the subscription 
* Post a message 
 
 The message would have at least a ‘messageType’ property. 
 In the response to the 'read subscription’ we would like also see how many times a particular event type has been received by a subscription. There may be more than one subscription listening for the same event type(s). 
 
Runnable service written in a Java 8 technology based on spring-boot. 

### Set up
* Spring Boot: [Spring-Boot](https://projects.spring.io/spring-boot/)
* Maven
* Java 8
* Git


#### Exercise
* Runnable JAR file can be built by Maven, starting up app in Tomcat server
* Rest Interfaces:
    * /subscriptions/info
        * RequestMethod.GET
        * Info page to get sanity check of app   
    * /subscriptions/create
        * RequestMethod.POST
        * Parameters:
            * id: Id of a subscription to create
            * messageType: Types of message to be managed by this subscription
        * Description: Create a new subscription
    * /subscriptions/update
        * RequestMethod.PUT
        * Parameters:
            * id: Id of a subscription known by the system
            * messageType: Types of message to be managed by this subscription
        * Description: Update type of messages to be managed by a subscription
    * /subscriptions/read
        * RequestMethod.GET
        * Parameters:
            * id: Id of a subscription to create
        * Description: Report all information available about a subscription according to the exercise. 
        Example of response: 
        {"numberOfMessagesByType":{"typeC":0,"typeB":0,"typeA":12}}
    * /messages/send
        * RequestMethod.POST
        * Body must contain JSON as:
            * {"messageType": "typeA","content": "Any content"}
        * Description: Sends a new message to the system of type = "typeA". After that if a subscription is read then the information will be properly updated
        If sends is successful then returns: "Message received"
    * /subscriptions/delete
        * RequestMethod.DELETE
        * Parameters
            * id: Id of a subscription to create
        * Description: Remove a subscription if exists in the system
        
Application was tested with JUnits and MockMVC 
Rest API was tested with exploratory techniques using POSTMAN as tool

Current coverage summary:
* Class: 100% (6/6)
* Method: 84.4% (27/32)
* Line: 87.6% (113/129)