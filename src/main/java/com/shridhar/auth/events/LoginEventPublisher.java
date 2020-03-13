package com.shridhar.auth.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class LoginEventPublisher {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void publishEvent(final String email, final boolean status) {
		
		LoginEvent loginEvent = new LoginEvent(this, email, status);
		applicationEventPublisher.publishEvent(loginEvent);
		
		LOGGER.info("Generated login event");		
	}
	
}
