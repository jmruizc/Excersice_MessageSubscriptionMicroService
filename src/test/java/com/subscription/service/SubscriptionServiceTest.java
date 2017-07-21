package com.subscription.service;

import com.subscription.model.Credentials;
import com.subscription.model.Subscription;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SubscriptionServiceTest {

	private SubscriptionService service = new SubscriptionService(new SubscriptionManagement());
	
	private List<String> messageTypes;
		 
    @Before
    public void setup() throws Exception {
    	messageTypes = new ArrayList<String>();
		messageTypes.add("TypeA");
		messageTypes.add("TypeB");
		messageTypes.add("TypeC");

    }
    
	@Test
	public void createSubscription() throws SubscriptionException{
		Credentials credential = new Credentials("John", "Doe");
		
		Subscription subscription = service.createSubscription("A", messageTypes, credential);

    	assertEquals(subscription.getCredentials().getName(), credential.getName());
    	assertEquals(subscription.getCredentials().getPassword(), credential.getPassword());
    	assertEquals(subscription.getId(), "A");
    	assertNotNull(subscription.getMessageTypes());
    	assertEquals(subscription.getMessageTypes().size(), 3);
    	assertNotNull(subscription.getMessagesByType());
    	assertEquals(subscription.getMessagesByType().size(), 3);
    	assertEquals(subscription.getMessagesByType().get("TypeA").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeB").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeC").size(), 0);
	}
	
	@Test(expected=SubscriptionException.class)
	public void createSubscriptionAlreadyExist() throws SubscriptionException{
		Credentials credential = new Credentials("John", "Doe");
		
		service.createSubscription("B", messageTypes, credential);
		service.createSubscription("B", messageTypes, credential);
	}
	   
	@Test
	public void updateSubscription() throws SubscriptionException{
		Credentials credential = new Credentials("John", "Doe");
		
		Subscription subscription = service.createSubscription("C", messageTypes, credential);

    	assertEquals(subscription.getCredentials().getName(), credential.getName());
    	assertEquals(subscription.getCredentials().getPassword(), credential.getPassword());
    	assertEquals(subscription.getId(), "C");
    	assertNotNull(subscription.getMessageTypes());
    	assertEquals(subscription.getMessageTypes().size(), 3);
    	assertNotNull(subscription.getMessagesByType());
    	assertEquals(subscription.getMessagesByType().size(), 3);
    	assertEquals(subscription.getMessagesByType().get("TypeA").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeB").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeC").size(), 0);
    	

		messageTypes.add("TypeD");
    	Subscription subscription2 = service.updateSubscription("C", messageTypes, credential);
    	assertEquals(subscription.getCredentials().getName(), credential.getName());
    	assertEquals(subscription.getCredentials().getPassword(), credential.getPassword());
    	assertEquals(subscription.getId(), "C");
    	assertNotNull(subscription.getMessageTypes());
    	assertEquals(subscription.getMessageTypes().size(), 4);
    	assertNotNull(subscription.getMessagesByType());
    	assertEquals(subscription.getMessagesByType().size(), 4);
    	assertEquals(subscription.getMessagesByType().get("TypeA").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeB").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeC").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeD").size(), 0);
    	
    	messageTypes.remove("TypeA");
    	messageTypes.remove("TypeB");
    	Subscription subscription3 = service.updateSubscription("C", messageTypes, credential);
    	assertEquals(subscription.getCredentials().getName(), credential.getName());
    	assertEquals(subscription.getCredentials().getPassword(), credential.getPassword());
    	assertEquals(subscription.getId(), "C");
    	assertNotNull(subscription.getMessageTypes());
    	assertEquals(subscription.getMessageTypes().size(), 2);
    	assertNotNull(subscription.getMessagesByType());
    	assertEquals(subscription.getMessagesByType().size(), 2);
    	assertEquals(subscription.getMessagesByType().get("TypeC").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeD").size(), 0);
	}
	
	@Test(expected=SubscriptionException.class)
	public void updateSubscriptionDoesntExist() throws SubscriptionException{
		Credentials credential = new Credentials("John", "Doe");
		
		service.updateSubscription("Z", messageTypes, credential);
	}
    
	@Test
	public void readSubscription() throws SubscriptionException{
		Credentials credential = new Credentials("John", "Doe");
		messageTypes.add("TypeH");
		service.createSubscription("D", messageTypes, credential);
		
		Subscription subscription = service.readSubscription("D");

    	assertEquals(subscription.getCredentials().getName(), credential.getName());
    	assertEquals(subscription.getCredentials().getPassword(), credential.getPassword());
    	assertEquals(subscription.getId(), "D");
    	assertNotNull(subscription.getMessageTypes());
    	assertEquals(subscription.getMessageTypes().size(), 4);
    	assertNotNull(subscription.getMessagesByType());
    	assertEquals(subscription.getMessagesByType().size(), 4);
    	assertEquals(subscription.getMessagesByType().get("TypeA").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeB").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeC").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeH").size(), 0);
	}
    
	@Test
	public void deleteSubscription() throws SubscriptionException{
		Credentials credential = new Credentials("John", "Doe");
		messageTypes.add("TypeJ");
		Subscription subscription = service.createSubscription("E", messageTypes, credential);

    	assertEquals(subscription.getCredentials().getName(), credential.getName());
    	assertEquals(subscription.getCredentials().getPassword(), credential.getPassword());
    	assertEquals(subscription.getId(), "E");
    	assertNotNull(subscription.getMessageTypes());
    	assertEquals(subscription.getMessageTypes().size(), 4);
    	assertNotNull(subscription.getMessagesByType());
    	assertEquals(subscription.getMessagesByType().size(), 4);
    	assertEquals(subscription.getMessagesByType().get("TypeA").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeB").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeC").size(), 0);
    	assertEquals(subscription.getMessagesByType().get("TypeJ").size(), 0);

    	service.deleteSubscription("E");
	}

	@Test(expected=SubscriptionException.class)
	public void readSubscriptionDoesntExist() throws SubscriptionException{	
		service.readSubscription("Z");
	}
	
	@Test(expected=SubscriptionException.class)
	public void deleteSubscriptionDoesntExist() throws SubscriptionException{	
		service.deleteSubscription("Z");
	}
}
