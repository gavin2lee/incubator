package com.harmazing.openbridge.paasos.oslog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.openbridge.paasos.oslog.dao.PaasProjectLogMapper;
import com.harmazing.openbridge.paasos.oslog.model.PaasProjectLog;
import com.harmazing.openbridge.paasos.oslog.service.IPaasProjectLogService;

@Service
public class PaasProjectLogServiceImpl implements IPaasProjectLogService{
	
	@Autowired
	private PaasProjectLogMapper paasProjectLogMapper;
	
	@Transactional
	@Override
	public int batchSave(List<PaasProjectLog> logs){
		if(logs==null || logs.size()==0){
			return 0;
		}
		return paasProjectLogMapper.batchSave(logs);
	}

	@Override
	public List<PaasProjectLog> getLogHistory(String key) {
		PaasProjectLog param = new PaasProjectLog();
		param.setKey(key);
		param.setBegin("1");
		return paasProjectLogMapper.getLogHistory(param);
	}

	@Override
	public List<PaasProjectLog> getLogHistoryInfo(String logId,boolean isFirst) {
		PaasProjectLog param = new PaasProjectLog();
		param.setId(logId);
		List<PaasProjectLog> r = paasProjectLogMapper.getLogHistory(param);
		if(r==null || r.size()==0){
			throw new RuntimeException("获取定位日志失败");
		}
		PaasProjectLog l = r.get(0);
		
//		PaasProjectLog p1 = new PaasProjectLog();
//		p1.setKey(l.getKey());
//		p1.setType(l.getType());
//		p1.setBegin("1");
//		p1.setGteCreateDate(l.getCreateDate());
//		p1.setNeid(l.getId());
//		p1.setSort("asc");
//		p1.setLimit(1);
//		r = paasProjectLogMapper.getLogHistory(p1);
		
//		PaasProjectLog p1 = null;
		PaasProjectLog p1 = new PaasProjectLog();
		p1.setKey(l.getKey());
		p1.setType(l.getType());
		p1.setGteCreateDate(l.getCreateDate());
		p1.setSort("asc");
		if(!isFirst){
			p1.setNeid(l.getId());
		}
		p1.setSessionId(l.getSessionId());
//		if(r!=null && r.size()>0){
//			PaasProjectLog end = r.get(0);
//			p1.setLtCreateDate(end.getCreateDate());
////			p1.setNeid(end.getId());
//		}
		return paasProjectLogMapper.getLogHistory(p1);
	}

}
