package com.subscription.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscription implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private Credentials credentials;
	
	private HashSet<String> messageTypes;
	
	private ConcurrentHashMap<String, List<Message>> messagesByType = new ConcurrentHashMap<String, List<Message>>(1000);

	public Subscription(String id, HashSet<String> messageTypes) {
		this.id = id;
		this.messageTypes = messageTypes;
		messageTypes.forEach(k -> {
			messagesByType.put(k, new ArrayList<Message>());
		});
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HashSet<String> getMessageTypes() {
		return messageTypes;
	}

	public void setMessageTypes(HashSet<String> messageTypes) {
		this.messageTypes = messageTypes;
	}

	public ConcurrentHashMap<String, List<Message>> getMessagesByType() {
		return messagesByType;
	}

	public void setMessagesByType(ConcurrentHashMap<String, List<Message>> messagesByType) {
		this.messagesByType = messagesByType;
	}

}
