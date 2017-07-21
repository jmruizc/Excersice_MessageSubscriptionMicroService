package com.subscription.controller;

import com.subscription.model.Message;
import com.subscription.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/messages")
public class MessageController {

	@Autowired
	private SubscriptionService subscriptionService;

	@RequestMapping(method = RequestMethod.POST, path = "/send")
	public ResponseEntity receiveMessage(@RequestBody Message input) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest()
				.buildAndExpand(input).toUri());
		
		if (input.getMessageType() != null && !input.getMessageType().isEmpty()) {
	    	subscriptionService.processMessage(input);	
			return createReponseEntity(httpHeaders,
					"Message received",
					HttpStatus.CREATED);
			
	    } else {
	    	return createReponseEntity(httpHeaders,
					"Message and message type are mandatory",
					HttpStatus.BAD_REQUEST);
		}
		
	}
	
	private ResponseEntity createReponseEntity(HttpHeaders httpHeaders, String content,
			HttpStatus status) {
		return new ResponseEntity<>(content,
				httpHeaders,
				status);
	}
}
