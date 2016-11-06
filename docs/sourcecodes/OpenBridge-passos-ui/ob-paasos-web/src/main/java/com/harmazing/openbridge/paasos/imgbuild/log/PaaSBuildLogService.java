package com.harmazing.openbridge.paasos.imgbuild.log;

import java.util.Date;

import javax.annotation.Resource;



import org.springframework.stereotype.Service;

import com.harmazing.framework.util.StringUtil;

@Service
public class PaaSBuildLogService implements IPaaSBuildLogService {

	@Resource
	private PaaSBuildLogMapper paaSBuildLogMapper;
	
	@Override
	public void saveLog(String busId, String buildLog) {
		/*if(paaSBuildLogMapper.count(busId)>0){
			paaSBuildLogMapper.updateLog(busId, buildLog);
		}else{
			paaSBuildLogMapper.add(StringUtil.getUUID(), busId, buildLog, new Date());
		}*/
		paaSBuildLogMapper.add(StringUtil.getUUID(), busId, buildLog, new Date());
	}

	@Override
	public void delete(String busId) {
		paaSBuildLogMapper.delete(busId);
	}

	@Override
	public String getLog(String busId) {
		return paaSBuildLogMapper.getLog(busId);
	}

}
