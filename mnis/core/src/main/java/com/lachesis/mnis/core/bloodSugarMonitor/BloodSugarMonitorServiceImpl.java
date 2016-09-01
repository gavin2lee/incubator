package com.lachesis.mnis.core.bloodSugarMonitor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnis.core.BloodSugarMonitorService;
import com.lachesis.mnis.core.bloodPressMonitor.BloodConstant;
import com.lachesis.mnis.core.bloodSugarMonitor.entity.PatBloodSugarMonitor;
import com.lachesis.mnis.core.bloodSugarMonitor.repository.BloodSugarRepository;

@Service
public class BloodSugarMonitorServiceImpl implements BloodSugarMonitorService {
	@Autowired
	private BloodSugarRepository repository;
	
	@Override
	public List<PatBloodSugarMonitor> queryBloodSugarMonitor(String deptCode, String patId,
			String recordDate, boolean provisions){
		PatBloodSugarMonitor press = new PatBloodSugarMonitor();
		press.setPatId(patId);//患者流水号
		press.setDeptCode(deptCode);//科室编号
		press.setRecordTime(recordDate);//记录时间
		press.setProvisions(provisions);//是否查询指定时间的数据
		
		return repository.selectBloodSugarMonitor(press);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)//事物传递级别
	public void saveBloodSugarMonitor(List<PatBloodSugarMonitor> datas){
		if(null != datas){
			for(PatBloodSugarMonitor press : datas){
				//校验是否已经在数据库存在，如果存在则更新。如果不存在则新增
				PatBloodSugarMonitor dbPress = repository.selectBloodSugar(press);
				if(null != dbPress){
					if(StringUtils.isEmpty(press.getStatus())){
						press.setStatus(BloodConstant.BLOOD_STATUS_C);//新建
					}
					press.setId(dbPress.getId());
					repository.updateBloodSugarById(press);
				}else{
					press.setStatus(BloodConstant.BLOOD_STATUS_C);//新建
					repository.insertBloodSugarMonitor(press);
				}
				//释放缓存
				dbPress = null;
			}
		}
		//是否缓存
		datas = null;
	}
}