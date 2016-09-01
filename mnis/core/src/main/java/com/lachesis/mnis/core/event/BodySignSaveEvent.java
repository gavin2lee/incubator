package com.lachesis.mnis.core.event;

import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;

@SuppressWarnings("serial")
public class BodySignSaveEvent extends MnisEvent {
	public BodySignRecord bodySignRecord;
	
	public BodySignSaveEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}
}
