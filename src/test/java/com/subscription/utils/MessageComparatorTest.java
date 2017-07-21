package com.subscription.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.subscription.model.Message;


public class MessageComparatorTest {
	
	private MessageComparator comparator = new MessageComparator();
	
	@Test
	public void compareMessages(){
		Message message1 = new Message();
		message1.setId(1);
    	message1.setMessageType("typeA");
    	message1.setContent("contentA");
    	
    	Message message2 = new Message();
		message2.setId(2);
    	message2.setMessageType("typeB");
    	message2.setContent("contentB");
    	
    	int result = comparator.compare(message1, message2);
    	assertEquals(result, 1);
	}

	@Test
	public void compareNullIdsForMessages(){
		Message message1 = new Message();
		message1.setId(null);
    	message1.setMessageType("typeA");
    	message1.setContent("contentA");
    	
    	Message message2 = new Message();
		message2.setId(null);
    	message2.setMessageType("typeB");
    	message2.setContent("contentB");
    	
    	int result = comparator.compare(message1, message2);
    	assertEquals(result, 0);
	}

	@Test
	public void compareNullIdForMessages(){
		Message message1 = new Message();
		message1.setId(null);
    	message1.setMessageType("typeA");
    	message1.setContent("contentA");
    	
    	Message message2 = new Message();
		message2.setId(2);
    	message2.setMessageType("typeB");
    	message2.setContent("contentB");
    	
    	int result = comparator.compare(message1, message2);
    	assertEquals(result, 0);
	}

	@Test
	public void compareNullId2ForMessages(){
		Message message1 = new Message();
		message1.setId(1);
    	message1.setMessageType("typeA");
    	message1.setContent("contentA");
    	
    	Message message2 = new Message();
		message2.setId(null);
    	message2.setMessageType("typeB");
    	message2.setContent("contentB");
    	
    	int result = comparator.compare(message1, message2);
    	assertEquals(result, 0);
	}
	
	@Test
	public void compareNullMessages(){
    	
    	int result = comparator.compare(null, null);
    	assertEquals(result, 0);
    	
    	Message message1 = new Message();
		message1.setId(1);
    	message1.setMessageType("typeA");
    	message1.setContent("contentA");
    	int result2 = comparator.compare(message1, null);
    	assertEquals(result2, 0);

    	int result3 = comparator.compare(null, message1);
    	assertEquals(result3, 0);
	}

}
