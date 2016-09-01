package com.lachesis.mnis.core.duty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("dutyService")
public class DutyServiceImpl implements DutyService {

	@Autowired
	private DutyRepository dutyRepository;

	@Override
	public List<DutyBean> getDutyByDeptId(String deptId) {
		return dutyRepository.getDutyByDeptId(deptId);
	}

	@Override
	public int setUserName(String userName, String type) {
		
		return dutyRepository.setUserName(userName, type);
	}

	@Override
	public int setTel(String tel, String type) {
		// TODO Auto-generated method stub
		return dutyRepository.setTel(tel, type);
	}

}
