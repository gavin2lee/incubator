package com.lachesis.mnis.core.bloodPressMonitor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnis.core.BloodPressMonitorService;
import com.lachesis.mnis.core.bloodPressMonitor.entity.PatBloodPressMonitor;
import com.lachesis.mnis.core.bloodPressMonitor.repository.BloodPressRepository;

@Service
public class BloodPressMonitorServiceImpl implements BloodPressMonitorService {
	@Autowired
	private BloodPressRepository pressRepository;
	
	@Override
	public List<PatBloodPressMonitor> queryBloodPressMonitor(String deptCode, String patId,
			String recordDate, boolean provisions){
		PatBloodPressMonitor press = new PatBloodPressMonitor();
		press.setPatId(patId);//患者流水号
		press.setDeptCode(deptCode);//科室编号
		press.setRecordTime(recordDate);//记录时间
		press.setProvisions(provisions);//是否查询指定时间的数据
		
		return pressRepository.selectBloodPressList(press);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)//事物传递级别
	public void saveBloodPressMonitor(List<PatBloodPressMonitor> datas){
		if(null != datas){
			for(PatBloodPressMonitor press : datas){
				//校验是否已经在数据库存在，如果存在则更新。如果不存在则新增
				PatBloodPressMonitor dbPress = pressRepository.selectBloodPress(press);
				if(null != dbPress){
					if(StringUtils.isEmpty(press.getStatus())){
						press.setStatus(BloodConstant.BLOOD_STATUS_C);//新建
					}
					press.setId(dbPress.getId());
					pressRepository.updateBloodPressById(press);
				}else{
					press.setStatus(BloodConstant.BLOOD_STATUS_C);//新建
					pressRepository.insertBloodPressMonitor(press);
				}
				//释放缓存
				dbPress = null;
			}
		}
		//是否缓存
		datas = null;
	}
}
