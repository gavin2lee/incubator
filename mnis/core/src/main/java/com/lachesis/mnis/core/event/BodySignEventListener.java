package com.lachesis.mnis.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lachesis.mnis.core.BodySignService;

@Component
public class BodySignEventListener implements MnisEventListener<BodySignSaveEvent> {
	static final Logger LOGGER = LoggerFactory.getLogger(BodySignEventListener.class);

	@Autowired
	BodySignService bodysService;

	@Override
	public void onApplicationEvent(BodySignSaveEvent event) {
		LOGGER.warn("BodySignEventListener： Processing bodysignsave event!");
		bodysService.copyBodySignFromDoc();
	}

}