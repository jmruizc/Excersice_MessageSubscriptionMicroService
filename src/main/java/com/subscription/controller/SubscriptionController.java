package com.subscription.controller;

import com.subscription.model.Credentials;
import com.subscription.model.Info;
import com.subscription.model.Subscription;
import com.subscription.model.SubscriptionResponse;
import com.subscription.service.SubscriptionException;
import com.subscription.service.interfaces.SubscriptionInterface;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

	private Logger LOGGER = Logger.getLogger(SubscriptionController.class);

	private static final String SUBSCRIPTION_DOESNT_EXIST = "Subscription doesn't exist";

	@Autowired
	private SubscriptionInterface subscriptionService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/info")
	public String welcome() {
		return Info.INFO_MSG;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/create")
	public ResponseEntity createSubscription(@RequestParam(value = "id") String id,
			@RequestParam(value = "messageType") List<String> messageTypes,
			@RequestParam(value = "user", required=false) String username,
			@RequestParam(value = "password", required=false) String password) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest()
				.buildAndExpand(id).toUri());
		
		if (id.equalsIgnoreCase("") || messageTypes.isEmpty()) {
			return createInvalidResponse(httpHeaders);
			
		} else {
			Credentials credential = new Credentials(username, password);
			try {
				Subscription subscription = subscriptionService.createSubscription(id, messageTypes, credential);	
				return createReponseEntity(httpHeaders, subscription, HttpStatus.CREATED);
				
			} catch (SubscriptionException e) {
				return new ResponseEntity<>(
						"Subscription already exist"
						, httpHeaders, HttpStatus.ACCEPTED);
			}
		
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/read")
	public ResponseEntity readSubscription(@RequestParam(value = "id") String id) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest()
				.buildAndExpand(id).toUri());
		
		try {
			Subscription subscription = subscriptionService.readSubscription(id);
			return createReponseEntity(httpHeaders, subscription, HttpStatus.OK);
			
		} catch (SubscriptionException e) {			
			return new ResponseEntity<>(
					SUBSCRIPTION_DOESNT_EXIST,
					httpHeaders,
					HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/update")
	public ResponseEntity updateSubscription(@RequestParam(value = "id") String id,
			@RequestParam(value = "messageType") List<String> messageTypes,
			@RequestParam(value = "user", required=false) String username,
			@RequestParam(value = "password", required=false) String password) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest()
				.buildAndExpand(id).toUri());
		
		if (id.equalsIgnoreCase("") || messageTypes.isEmpty()) {
			return createInvalidResponse(httpHeaders);
			
		} else {
			Credentials credential = new Credentials(username, password);
			try {
				Subscription subscription = subscriptionService.updateSubscription(id, messageTypes, credential);	
				return createReponseEntity(httpHeaders, subscription, HttpStatus.OK);
				
			} catch (SubscriptionException e) {
				return new ResponseEntity<>(
						SUBSCRIPTION_DOESNT_EXIST,
						httpHeaders,
						HttpStatus.ACCEPTED);
			}
		
		}
		
	}
	
	@RequestMapping(method = RequestMethod.DELETE, path = "/delete")
	public ResponseEntity deleteSubscription(@RequestParam(value = "id") String id) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest()
				.buildAndExpand(id).toUri());
		
		try {
			subscriptionService.deleteSubscription(id);
			return new ResponseEntity<>(
					"Subscription removed",
					httpHeaders,
					HttpStatus.OK);
			
		} catch (SubscriptionException e) {			
			return new ResponseEntity<>(
					SUBSCRIPTION_DOESNT_EXIST,
					httpHeaders,
					HttpStatus.NOT_FOUND);
		}
	}
	
	private ResponseEntity<?> createReponseEntity(HttpHeaders httpHeaders, Subscription subscription,
			HttpStatus status) {
		return new ResponseEntity<>(
				createSubscriptionResponse(subscription),
				httpHeaders,
				status);
	}
	
	private SubscriptionResponse createSubscriptionResponse(Subscription subscription) {
		HashMap<String, Integer> numberOfMessagesByType = new HashMap<>();
		
		subscription.getMessagesByType().forEach((k,v)-> {
			int numberMsgs = 0;
			if (v != null) {
				numberMsgs = v.size();
			}
			numberOfMessagesByType.put(k, numberMsgs);
		});
		
		return new SubscriptionResponse(subscription.getMessageTypes(), numberOfMessagesByType);
	}
	
	private ResponseEntity<?> createInvalidResponse(HttpHeaders httpHeaders) {
		return new ResponseEntity<>("Invalid parameters in the request.",
				httpHeaders,
				HttpStatus.BAD_REQUEST);
	}
}
