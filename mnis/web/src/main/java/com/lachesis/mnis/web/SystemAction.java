package com.lachesis.mnis.web;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.SysDicService;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.redis.RedisServerInfo;
import com.lachesis.mnis.core.redis.RedisService;
import com.lachesis.mnis.core.sysDic.entity.SysDic;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.core.util.SuperCacheUtil;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;

@Controller
@RequestMapping("/nur/system")
public class SystemAction{
	static final Logger LOGGER = LoggerFactory.getLogger(SystemAction.class);

	@Autowired
	private RedisService redisService;
	@Autowired
	private RedisServerInfo redisServerInfo;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private SysDicService sysDicService;

	/**
	 * 获取版本号
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping("/getMiddlewareVersion")
	public @ResponseBody
	BaseDataVo getMiddlewareVersion() {
		BaseDataVo vo = new BaseDataVo();

		vo.setData("2.0.2");
		vo.setRslt(ResultCst.SUCCESS);

		return vo;
	}

	/**
	 * 获取Redis配置
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping("/getRedisInfo")
	@ResponseBody
	public BaseDataVo getRedisInfo(@RequestParam(required = false) String token) {
		BaseDataVo vo = new BaseDataVo();

		RedisServerInfo info = new RedisServerInfo();
		info.setHost(redisServerInfo.getHost());
		info.setPort(redisServerInfo.getPort());
		info.setPassword(redisServerInfo.getPassword());

		vo.setData(info);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 获取提前提醒时间
	 * 
	 * @param token
	 * @param userId
	 * @param patientId
	 * @param channel
	 * @param field
	 * @return
	 */
	@RequestMapping("/getPubSubUserConfig")
	@ResponseBody
	public BaseDataVo getPubSubUserConfig(
			@RequestParam(required = false) String token,
			@RequestParam String userId,
			@RequestParam(required = false) String patientId,
			@RequestParam String channel, @RequestParam String field) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> SystemAction execute getPubSubUserConfig, userId:"
					+ userId
					+ "---patientId:"
					+ patientId
					+ "---channel:"
					+ channel + "---field:" + field);
		}
		BaseDataVo vo = new BaseDataVo();
		String value = redisService.getPubSubUserConfig(userId, patientId,
				channel, field);
		if (value != null) {
			vo.setData(value);
		}

		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 设置提醒时间
	 * 
	 * @param token
	 * @param userId
	 * @param patientId
	 * @param channel
	 * @param field
	 * @param value
	 * @return
	 */
	@RequestMapping("/setPubSubUserConfig")
	@ResponseBody
	public BaseVo setPubSubUserConfig(
			@RequestParam(required = false) String token,
			@RequestParam String userId, @RequestParam String patientId,
			@RequestParam String channel, @RequestParam String field,
			@RequestParam String value) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> SystemAction execute setPubSubUserConfig, userId:"
					+ userId
					+ "---patientId:"
					+ patientId
					+ "---channel:"
					+ channel + "---field:" + field + "---value:" + value);
		}
		BaseVo vo = new BaseVo();
		long count = redisService.setPubSubUserConfig(userId, channel,
				patientId, field, value);
		if (count < 0) {
			vo.setRslt(ResultCst.SYSCONFIG_PUB_SUB_FAILED);
		} else {
			vo.setRslt(ResultCst.SUCCESS);
		}

		return vo;
	}

	@RequestMapping("/getConfig")
	@ResponseBody
	public BaseMapVo getSystemConfig(String userId, String deptId) {
		BaseMapVo vo = new BaseMapVo();
		Map<String, Map<String, String>> configs = identityService.queryConfig(userId, deptId);
		
		vo.setRslt(ResultCst.SUCCESS);
		vo.addData("system", configs.get("system"));
		vo.addData("pcSystem", configs.get("pcSystem"));
		vo.addData("menu", SuperCacheUtil.getMENU_CONFIG().get("menu_config"));
		vo.addData("dept", configs.get("dept"));
		vo.addData("user", configs.get("user"));
		return vo;
	}
	
	@RequestMapping("/resetExecData")
	@ResponseBody
	public BaseDataVo resetExecData(){
		//重置执行数据
		BaseDataVo outVo = new BaseDataVo();
		try{
			String vesion = identityService.getConfigure("isTestVesion");
			if (!"1".equals(vesion)) {
				throw new MnisException("当前不是测试演示版本，不允许重置数据！");
			}
			identityService.resetData();
			outVo.setRslt(ResultCst.SUCCESS);
			outVo.setMsg("数据重置成功！");
		} catch (MnisException e) {
			LOGGER.error("",e);
			outVo.setRslt(ResultCst.FAILURE);
			outVo.setMsg(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("",e);
			outVo.setRslt(ResultCst.FAILURE);
			outVo.setMsg("操作失败！");
		}
		return outVo;
	}
	
	/**
	 * 获取下拉框的数据
	 * @param json
	 * @return
	 */
	@RequestMapping(value="/getSysDic")
	@ResponseBody
	public BaseMapVo getSysDic(){
		/*json = "[{'dicType':'papersType'}]";
		//参数获取
		Type type = new TypeToken<List<SysDic>>(){}.getType();
		List<SysDic> dicTypes = GsonUtils.fromJson(json, type);
		BaseMapVo outVo = new BaseMapVo();
		Map<String,List<SysDic>> dataMap = new HashMap<String,List<SysDic>>();
		if(null != dicTypes){
			//数据处理和获取
			for(SysDic dicType : dicTypes){
				dataMap.put(dicType.getDicType(), sysDicService.getSysDics(dicType.getDicType()));
			}
			//清理缓存
			dicTypes = null;
		}
		//返回的数据
		outVo.addData("dicInfo",dataMap);
		//成功编号
		outVo.setRslt(ResultCst.SUCCESS);
		//数据返回
		return outVo;*/
		BaseMapVo outVo = new BaseMapVo();
		List<String> list = sysDicService.queryDicTypes();
		Map<String,List<SysDic>> dataMap = new HashMap<String,List<SysDic>>();
		if(null != list){
			for (String string : list) {
				dataMap.put(string, sysDicService.getSysDics(string));
			}
		}
		//返回的数据
		outVo.addData("dicInfo",dataMap);
		//成功编号
		outVo.setRslt(ResultCst.SUCCESS);
		//数据返回
		return outVo;
	}
	
	/**
	 * 获取所有字典类型
	 * @return
	 */
	@RequestMapping(value="/getSysDicTypes")
	@ResponseBody
	public BaseMapVo getSysDicTypes(){
		BaseMapVo outVo = new BaseMapVo();
		//返回的数据
		outVo.addData("sysDicTypes", sysDicService.queryDicTypes());
		//成功编号
		outVo.setRslt(ResultCst.SUCCESS);
		//数据返回
		return outVo;
	}
}
