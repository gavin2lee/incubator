package com.lachesis.mnis.core.nursing;

public interface NurseRecordDataCopyService {
	/**
	 * 
	 * 根据体征主表id，转抄体征记录到护理记录中
	 * @param masterId
	 * @param time
	 * @param emplCode
	 * @param loginName
	 * @return
	 */
	void transBodysignForNurseRecord(int masterId, String time, String emplCode, String loginName);
}
