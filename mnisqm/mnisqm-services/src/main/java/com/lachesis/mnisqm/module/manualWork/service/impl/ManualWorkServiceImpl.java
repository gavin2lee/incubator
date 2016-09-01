package com.lachesis.mnisqm.module.manualWork.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.CodeUtils;
import com.lachesis.mnisqm.module.manualWork.dao.ManualWorkDetailMapper;
import com.lachesis.mnisqm.module.manualWork.dao.ManualWorkItemMapper;
import com.lachesis.mnisqm.module.manualWork.dao.ManualWorkMapper;
import com.lachesis.mnisqm.module.manualWork.domain.ManualWork;
import com.lachesis.mnisqm.module.manualWork.domain.ManualWorkDetail;
import com.lachesis.mnisqm.module.manualWork.domain.ManualWorkItem;
import com.lachesis.mnisqm.module.manualWork.service.IManualWorkService;

@Service
public class ManualWorkServiceImpl implements IManualWorkService {

	@Autowired
	private ManualWorkItemMapper manualWorkItemMapper;

	@Autowired
	private ManualWorkMapper manualWorkMapper;

	@Autowired
	private ManualWorkDetailMapper manualWorkDetailMapper;

	@Override
	public int insertManualWorkItem(ManualWorkItem manualWorkItem){
		manualWorkItem.setItemCode(CodeUtils.getSysInvokeId());
		return manualWorkItemMapper.insert(manualWorkItem);
	}
	
	@Override
	public int updateManualWorkItem(ManualWorkItem manualWorkItem){
		return manualWorkItemMapper.updateByPrimaryKey(manualWorkItem);
	}
	
	@Override
	public void deleteManualWorkItem(ManualWorkItem manualWorkItem){
		if(null == manualWorkItem.getSeqId()){
			throw new CommRuntimeException("seqId不允许为空！");
		}
		manualWorkItem.setStatus(MnisQmConstants.STATUS_WX);//无效状态
		manualWorkItemMapper.updateManualWorkItemForDelete(manualWorkItem);
	}
	
	@Override
	public int insertManualWork(ManualWork manualWork){
		manualWork.setWorkCode(CodeUtils.getSysInvokeId());
		return manualWorkMapper.insert(manualWork);
	}
	
	@Override
	public int updateManualWork(ManualWork manualWork){
		return manualWorkMapper.updateByPrimaryKey(manualWork);
	}
	
	@Override
	public void deleteManualWork(ManualWork manualWork){
		if(null == manualWork.getSeqId()){
			throw new CommRuntimeException("seqId不允许为空！");
		}
		manualWork.setStatus(MnisQmConstants.STATUS_WX);//无效状态
		manualWorkMapper.updateManualWorkForDelete(manualWork);
	}
	
	@Override
	public int insertManualWorkDetail(ManualWorkDetail manualWorkDetail){
		return manualWorkDetailMapper.insert(manualWorkDetail);
	}
	
	@Override
	public int updateManualWorkDetail(ManualWorkDetail manualWorkDetail){
		return manualWorkDetailMapper.updateByPrimaryKey(manualWorkDetail);
	}
	
	@Override
	public void deleteManualWorkDetail(ManualWorkDetail manualWorkDetail){
		if(null == manualWorkDetail.getSeqId()){
			throw new CommRuntimeException("seqId不允许为空！");
		}
		manualWorkDetail.setStatus(MnisQmConstants.STATUS_WX);//无效状态
		manualWorkDetailMapper.updatemanualWorkDetailForDelete(manualWorkDetail);
	}
	
	@Override
	public List<ManualWorkItem> queryManualWorkItemByDeptCode(String deptCode){
		return manualWorkItemMapper.queryByDeptCode(deptCode);
	}
	
	public List<ManualWork> queryManualWorkByDeptCodeAndDate(String deptCode,String beginTime,String endTime){
		Map<String, Object> conditionMap = new HashMap<String, Object>(); 
		conditionMap.put("deptCode", deptCode);
		conditionMap.put("beginTime", beginTime);
		conditionMap.put("endTime", endTime);
		return manualWorkMapper.queryByDeptCodeAndDate(conditionMap);
	}
}
