package com.subscription.service.interfaces;

import com.subscription.model.Credentials;
import com.subscription.model.Message;
import com.subscription.model.Subscription;
import com.subscription.service.SubscriptionException;

import java.util.List;

public interface SubscriptionInterface {
	
	Subscription createSubscription(String id, List<String> messageTypes, Credentials credential)
			throws SubscriptionException;
	
	Subscription updateSubscription(String id, List<String> messageTypes, Credentials credential)
			throws SubscriptionException;
	
	void deleteSubscription(String id) throws SubscriptionException;
	
	Subscription readSubscription(String id) throws SubscriptionException;
	
	void processMessage(Message message);

}
