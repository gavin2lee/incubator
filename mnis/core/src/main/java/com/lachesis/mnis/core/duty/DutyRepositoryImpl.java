package com.lachesis.mnis.core.duty;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository("dutyRepository")
public class DutyRepositoryImpl implements DutyRepository {

	@Override
	public List<DutyBean> getDutyByDeptId(String deptId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setUserName(String userName, String type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setTel(String tel, String type) {
		// TODO Auto-generated method stub
		return 0;
	}

}
