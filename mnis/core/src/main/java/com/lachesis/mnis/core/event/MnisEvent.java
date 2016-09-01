package com.lachesis.mnis.core.event;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class MnisEvent extends ApplicationEvent {

	public MnisEvent(Object source) {
		super(source);
	}

}
