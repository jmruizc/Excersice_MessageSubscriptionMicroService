package com.subscription.model;

import java.util.HashMap;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionResponse {
	
	private HashMap<String, Integer> numberOfMessagesByType;
	private HashSet<String> messageTypes;
	
	public SubscriptionResponse(HashSet<String> messageTypes, HashMap<String, Integer> numberOfMessagesByType) {
		this.numberOfMessagesByType = numberOfMessagesByType;
		this.messageTypes = messageTypes;
	}

	public HashMap<String, Integer> getNumberOfMessagesByType() {
		return numberOfMessagesByType;
	}
	
//	public String getSubscriptionInfo() {
//		StringBuffer subscriptionInfo = new StringBuffer();
//		if (numberOfMessagesByType != null && !numberOfMessagesByType.isEmpty()) {
//			numberOfMessagesByType.forEach((k,v) ->{
//				subscriptionInfo.append(String.format("Message type: %s || Number of messages: %s"
//						, k, v.toString()));
//				
//				subscriptionInfo.append("\n");
//			});
//		} else {
//			messageTypes.forEach(k -> {
//				subscriptionInfo.append(String.format("Message type: %s || Number of messages: 0", k));
//				subscriptionInfo.append("\n");
//			});
//		}
//		return subscriptionInfo.toString();
//		
//	}
	
	

}
