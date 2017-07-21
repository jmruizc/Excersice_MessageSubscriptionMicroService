package com.subscription.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.subscription.model.Message;
import com.subscription.model.Subscription;
import com.subscription.utils.MessageComparator;

@Component
public class SubscriptionManagement {

	@Autowired
	private MessageComparator messageComparator;
	
	private ConcurrentHashMap<String, Subscription> subscriptions;
	
	public SubscriptionManagement() {
		subscriptions = new ConcurrentHashMap<>(1000);
	}
	
	public void createSubscription(Subscription subscription) {
		subscriptions.put(subscription.getId(), subscription);
	}
	
	public void deleteSubscription(String id) {
		subscriptions.remove(id);
	}
	
	public boolean updateSubscription(Subscription subscription){
		subscriptions.put(subscription.getId(), subscription);
		return true;
	}
	
	public Subscription readSubscription(String id) {		
		return subscriptions.get(id);
	}
	
    public void addMessageToSubscriptions(Message message) {
		subscriptions.forEach(10l, (k,v) -> {
			if (v.getMessageTypes().contains(message.getMessageType())) {
				List<Message> messages = new ArrayList<Message>();
				if (v.getMessagesByType().containsKey(message.getMessageType())) {
					messages = v.getMessagesByType().get(message.getMessageType());
					message.setId(messages.size() + 1);
					messages.add(message);
					messages.sort(messageComparator);
				}
			}
		});
	}
    
    public void clear() {
    	subscriptions = new ConcurrentHashMap<>(1000);
    }

}
