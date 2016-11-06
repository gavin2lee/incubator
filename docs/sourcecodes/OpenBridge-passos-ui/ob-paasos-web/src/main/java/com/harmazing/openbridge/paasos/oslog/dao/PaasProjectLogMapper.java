package com.harmazing.openbridge.paasos.oslog.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paasos.oslog.model.PaasProjectLog;

public interface PaasProjectLogMapper extends IBaseMapper{

	int batchSave(List<PaasProjectLog> logs);

	
	List<PaasProjectLog> getLogHistory(PaasProjectLog param);

	

}
