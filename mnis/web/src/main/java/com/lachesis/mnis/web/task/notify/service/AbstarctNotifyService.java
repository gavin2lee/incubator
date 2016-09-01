package com.lachesis.mnis.web.task.notify.service;

import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.web.task.notify.json.BaseJson;

public abstract class AbstarctNotifyService implements NotifyService {

	public abstract void doNotify(String userId);
	
	public BaseJson getBaseJsonParamsFromBedBase(Patient patient) {
		return new BaseJson(
				patient.getBedCode(), 
				patient.getName(), 
				patient.getPatId());
	}
}
