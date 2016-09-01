package com.lachesis.mnis.core.mybatis.mapper;

import java.util.List;

import com.lachesis.mnis.core.duty.DutyBean;

public interface DutyServiceMapper {

	List<DutyBean> getDutyByDeptId(String deptId);
	
	int setUserName(String userName, String type);
	
	int setTel(String tel, String type);
}
