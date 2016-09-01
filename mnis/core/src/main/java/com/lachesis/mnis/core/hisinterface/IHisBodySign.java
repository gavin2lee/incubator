package com.lachesis.mnis.core.hisinterface;

import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;

public interface IHisBodySign {
	
	/**
	 * 插入体征数据
	 * @return
	 */
	public boolean insertBodySign(BodySignRecord bodySignRecord);
}
