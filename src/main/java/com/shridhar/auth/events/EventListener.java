package com.shridhar.auth.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener implements ApplicationListener<LoginEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

	@Override
	public void onApplicationEvent(LoginEvent event) {
		LOGGER.info("Received login event for: " + event.getEmail() + " with a status: " + event.getStatus());
	}

}