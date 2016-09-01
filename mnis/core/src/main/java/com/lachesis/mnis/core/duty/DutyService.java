package com.lachesis.mnis.core.duty;

import java.util.List;


public interface DutyService {

	List<DutyBean> getDutyByDeptId(String deptId);
	
	int setUserName(String userName, String type);
	
	int setTel(String tel, String type);

}
