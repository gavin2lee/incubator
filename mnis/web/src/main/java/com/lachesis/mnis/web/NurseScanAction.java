package com.lachesis.mnis.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.NurseScanService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.scan.entity.NurseScanInfo;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseMapVo;

/**
 * 护士扫描信息记录
 * 
 * @author ThinkPad
 *
 */
@RequestMapping("/nur/nurseScan")
@Controller
public class NurseScanAction {

	private static Logger LOGGER = LoggerFactory
			.getLogger(NurseScanAction.class);

	@Autowired
	private NurseScanService nurseScanService;
	
	@RequestMapping("/saveNurseScan")
	@ResponseBody
	public BaseMapVo saveNurseScan(String nurseScanInfo){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(nurseScanInfo)){
			vo.setMsg("参数为空!");
			vo.setRslt(ResultCst.ALERT_ERROR);
			LOGGER.error("NurseScanAction saveNurseScan nurseScanInfo is null!");
			return vo;
		}
		
		int count = 0 ;
		try {
			NurseScanInfo info = GsonUtils.fromJson(nurseScanInfo, NurseScanInfo.class);
			
			//是否包含多个条码
			if(StringUtils.isNotBlank(info.getBarcode())
					&& info.getBarcode().split(MnisConstants.COMMA).length > 1){
				for (String barcode : info.getBarcode().split(MnisConstants.COMMA)) {
					NurseScanInfo scanInfo = info;
					scanInfo.setBarcode(barcode);
					count = nurseScanService.saveNurseScan(scanInfo);
				}
			}else{
				count = nurseScanService.saveNurseScan(info);
			}
			
			
			if(count == 0){
				vo.setMsg("保存失败!");
				vo.setRslt(ResultCst.ALERT_ERROR);
				LOGGER.error("NurseScanAction saveNurseScan 插入失败!");
			}else{
				vo.setMsg("保存成功!");
				vo.setRslt(ResultCst.SUCCESS);
			}
		} catch (Exception e) {
			vo.setMsg("保存失败!");
			vo.setRslt(ResultCst.ALERT_ERROR);
			LOGGER.error("NurseScanAction saveNurseScan error:" + e.getMessage());
		}
		
		return vo;
		
	}

}
