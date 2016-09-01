package com.lachesis.mnis.core.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MnisEventPublisher{
	
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * 
	 * @param event
	 */
	public void publish(MnisEvent event) {
		applicationContext.publishEvent(event);
	}

}
