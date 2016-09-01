package com.lachesis.mnis.web;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.core.BloodPressMonitorService;
import com.lachesis.mnis.core.bloodPressMonitor.BloodConstant;
import com.lachesis.mnis.core.bloodPressMonitor.BloodPressUtil;
import com.lachesis.mnis.core.bloodPressMonitor.entity.PatBloodPressMonitor;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseMapVo;

@Controller
@RequestMapping("/nur/bloodPressMonitor")
public class BloodPressMonitorAction {
	private static final Logger log = LoggerFactory.getLogger(BloodPressMonitorAction.class);

	@Autowired
	private BloodPressMonitorService bloodPressService;

	/**
	 * 查询血压监控信息
	 * 
	 * @param deptCode
	 *            : 科室编号(必填)
	 * @param patId
	 *            : 患者流水号(非必填)
	 * @param recordDate
	 *            : 监测记录日期(必填,格式为YYYY-MM-DD)
	 * @param provisions
	 *            : 是否查询指定记录(非必填:01-不查询指定记录。02-查询指定记录。如果为空则默认为01)
	 * @return
	 */
	@RequestMapping("/queryBloodPress")
	@ResponseBody
	public BaseMapVo queryBloodPress(String deptCode, String patId,
			String recordDate, String provisions) {
		BaseMapVo outVo = new BaseMapVo();// 输出的对象
		log.debug("queryBloodPress:deptCode["+deptCode+"],patId["+patId+"]," +
				"recordDate["+recordDate+"],provisions["+provisions+"]");
		/*
		 * 输入数据校验
		 */
		try {
			//校验科室编号
			if(StringUtils.isEmpty(deptCode)){
				throw new MnisException("科室编号不允许为空!");
			}
			//校验血糖记录日期、
			if(StringUtils.isEmpty(deptCode)){
				throw new MnisException("记录日期不允许为空!");
			}
			try{
				//校验格式是否正确
				DateUtil.parse(recordDate,DateFormat.YMD);
			}catch (Exception e) {
				throw new MnisException("记录日期格式错误！需要[YYYY-MM-DD]格式。");
			}
			//是否查询指定时间的数据
			boolean is_pro = false;
			if(BloodConstant.PROVISIONS.equals(provisions)){
				is_pro = true;
			}
			//数据查询
			List<PatBloodPressMonitor> lst = bloodPressService.queryBloodPressMonitor(deptCode, patId, recordDate, is_pro);
			
			//查询成功-数据返回
			outVo.setRslt(ResultCst.SUCCESS);
			outVo.addData("lst", lst);
			
		} catch (MnisException e) {
			//失败记录失败信息
			outVo.setRslt(ResultCst.FAILURE);
			outVo.setMsg(e.getMessage());
			log.error("queryBloodPress",e);
		} catch (Exception e) {
			outVo.setRslt(ResultCst.FAILURE);
			outVo.setMsg("操作失败！");
			log.error("queryBloodPress",e);
		}
		log.debug(GsonUtils.toJson(outVo));
		return outVo;
	}
	
	/**
	 * 保存血压监测数据
	 * @param text
	 * @return
	 */
	@RequestMapping("/saveBloodPress")
	@ResponseBody
	public BaseDataVo saveBloodPress(String text){
		BaseDataVo outVo = new BaseDataVo();//数据返回对象
		log.debug(text);
		List<PatBloodPressMonitor> lst = null;
		try{
			Type type = new TypeToken<List<PatBloodPressMonitor>>(){}.getType();
			try{
				lst = GsonUtils.fromJson(text, type);
			}catch (Exception e) {
				throw new MnisException("数据解析失败！");
			}
			if(null != lst){
				//数据校验
				BloodPressUtil.validBloodPressList(lst);
				//数据录入
				bloodPressService.saveBloodPressMonitor(lst);
				outVo.setRslt(ResultCst.SUCCESS);
				outVo.setMsg("操作成功！");
			}else{
				throw new MnisException("数据为空!");
			}
		}catch (MnisException e) {
			log.error("",e);
			outVo.setRslt(ResultCst.FAILURE);
			outVo.setMsg(e.getMessage());
		}catch (Exception e) {
			log.error("",e);
			outVo.setRslt(ResultCst.FAILURE);
			outVo.setMsg("操作失败！");
		}finally{
			//释放缓存
			lst = null;
			text = null;
		}
		log.debug(GsonUtils.toJson(outVo));
		return outVo;
	}
}