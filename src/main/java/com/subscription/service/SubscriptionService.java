package com.subscription.service;

import com.subscription.model.Credentials;
import com.subscription.model.Message;
import com.subscription.model.Subscription;
import com.subscription.service.interfaces.SubscriptionInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SubscriptionService implements SubscriptionInterface {

	@Autowired
	private SubscriptionManagement subsManagement;

	public SubscriptionService() {
	}

	public SubscriptionService(SubscriptionManagement subsManagement) {
		this.subsManagement = subsManagement;
	}
	
	public Subscription createSubscription(String id, List<String> messageTypes, Credentials credential) 
			throws SubscriptionException {
		HashSet<String> setOfMessageTypes = new HashSet();
		messageTypes.stream().sorted().distinct().forEach(p -> setOfMessageTypes.add(p));
		
		Subscription subscription = subsManagement.readSubscription(id);
		
		if (subscription != null) {
			throw new SubscriptionException();
		} else {
			Subscription newSubscription = new Subscription(id, setOfMessageTypes);
			newSubscription.setCredentials(credential);
			subsManagement.createSubscription(newSubscription);
			return newSubscription;
		}
	}
	
	public Subscription updateSubscription(String id, List<String> messageTypes, Credentials credential) 
			throws SubscriptionException {
		HashSet<String> setOfMessageTypes = new HashSet();
		messageTypes.stream().sorted().distinct().forEach(p -> setOfMessageTypes.add(p));
		
		Subscription subscription = subsManagement.readSubscription(id);
		
		if (subscription != null) {
			ConcurrentHashMap<String, List<Message>> messagesByType = subscription.getMessagesByType();
			ConcurrentHashMap<String, List<Message>> newMessagesByType = new ConcurrentHashMap<String, List<Message>>();
			
			setOfMessageTypes.forEach( k -> newMessagesByType.put(k, new ArrayList<>()));

			messagesByType.forEach((k,v)->{	
				if (setOfMessageTypes.contains(k)) {
					newMessagesByType.put(k, v);
				}
			});
			
			subscription.setCredentials(credential);
			subscription.setMessageTypes(setOfMessageTypes);
			subscription.setMessagesByType(newMessagesByType);
			
			subsManagement.updateSubscription(subscription);
			return subscription;
			
		} else {
			throw new SubscriptionException();
		}
	}
	
	public void deleteSubscription(String id) throws SubscriptionException {
		if (subsManagement.readSubscription(id) != null) {
			subsManagement.deleteSubscription(id);
		} else {
			throw new SubscriptionException();
		}
	}
	
	public Subscription readSubscription(String id) throws SubscriptionException {
		if (subsManagement.readSubscription(id) != null) {
			return subsManagement.readSubscription(id);
		} else {
			throw new SubscriptionException();
		}
	}
	
	public void processMessage(Message message) {
		subsManagement.addMessageToSubscriptions(message);
	}
	
}
