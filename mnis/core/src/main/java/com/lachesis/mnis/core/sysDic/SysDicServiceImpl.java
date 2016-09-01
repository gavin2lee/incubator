package com.lachesis.mnis.core.sysDic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.SysDicService;
import com.lachesis.mnis.core.patientManage.entity.PatOrderConfiguration;
import com.lachesis.mnis.core.patientManage.reposity.PatientManageRepository;
import com.lachesis.mnis.core.sysDic.entity.SysDic;
import com.lachesis.mnis.core.sysDic.entity.repository.SysDicRepository;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.core.util.StringUtil;

@Service("sysDicService")
public class SysDicServiceImpl implements SysDicService {

	private Logger log = LoggerFactory.getLogger(SysDicServiceImpl.class);
	
	public static Map<String,List<SysDic>> dicCache =  new HashMap<String, List<SysDic>>();
	
	public static Map<String,Map<String,String>> orderCache =  new HashMap<String, Map<String,String>>();
	
	@Autowired
	private SysDicRepository sysDicRepository;
	
	@Autowired
	private PatientManageRepository patientManageRepository;
	
	/**
	 * 数据初始化
	 */
	@PostConstruct
	public void init(){
		//获取数据字典的缓存
		List<SysDic> dics =  sysDicRepository.queryAll();
		log.debug(GsonUtils.toJson(dics));
		if(null != dics){
			for(SysDic dic : dics){
				String dicType = dic.getDicType();
				List<SysDic> dicList = dicCache.get(dicType);
				if(null == dicList){
					dicList = new ArrayList<SysDic>();
				}
				dicList.add(dic);
				//缓存信息存放
				dicCache.put(dicType, dicList);
			}
			//是否缓存
			dics = null;
		}
		
		//获取顺序配置表缓存
		List<PatOrderConfiguration> patOCList = patientManageRepository.queryAllPatOrderConfiguration();
		if(null != patOCList){
			for (PatOrderConfiguration patOrderConfiguration : patOCList) {
				String moduleName = patOrderConfiguration.getModulename();
				Map<String, String> mapNode = orderCache.get(moduleName);
				if(null == mapNode){
					mapNode = new HashMap<String, String>();
					orderCache.put(moduleName, mapNode);
				}
				mapNode.put(patOrderConfiguration.getStatus(), patOrderConfiguration.getBackstatus()+"&"+patOrderConfiguration.getDescribe());
			}
			patOCList = null;
		}
	}
	
	/**
	 * 获取缓存中的数据字典
	 * @param dicType
	 * @return
	 */
	public List<SysDic> getSysDics(String dicType){
		List<SysDic> dics = dicCache.get(dicType);
		return dics;
	}
	
	/**
	 * 获取缓存中的数据字典
	 * 以MAP的形式输出
	 * @param dicType
	 * @return
	 */
	public Map<String,SysDic> getSysDicMap(String dicType){
		Map<String,SysDic> dicMap = new HashMap<String,SysDic>();
		List<SysDic> dics = dicCache.get(dicType);
		if(null != dics){
			for(SysDic dic : dics){
				dicMap.put(dic.getDicCode(), dic);
			}
		}
		dics = null;
		return dicMap;
	}
	
	/**
	 * 获取指定的数据字典
	 * @param dicType
	 * @param dicCode
	 * @return
	 */
	public SysDic getSysDic(String dicType,String dicCode){
		SysDic sysDic = null;
		List<SysDic> dics = dicCache.get(dicType);
		if(null != dics){
			for(SysDic dic : dics){
				if(dic.getDicCode().equals(dicCode)){
					sysDic = dic;
				}
			}
		}
		dics = null;
		return sysDic;
	}
	
	/**
	 * 获取指定的数据字典
	 * @param dicType
	 * @param dicCode
	 * @return
	 */
	public String getSysDicValue(String dicType,String dicCode){
		String dicName = null;
		List<SysDic> dics = dicCache.get(dicType);
		if(null != dics){
			for(SysDic dic : dics){
				if(dic.getDicCode().equals(dicCode)){
					dicName = dic.getDicName();
				}
			}
		}
		dics = null;
		return dicName;
	}

	@Override
	public List<String> queryDicTypes() {
		return sysDicRepository.queryDicTypes();
	}

	@Override
	public Map<String, String> getOrderCache(String modelName) {
		if(StringUtil.hasValue(modelName)){
			return orderCache.get(modelName);
		}
		return null;
	}

	@Override
	public String getBackStatus(String modelName, String status) {
		if(StringUtil.hasValue(modelName) && StringUtil.hasValue(status)){
			return orderCache.get(modelName).get(status);
		}
		return null;
	}

}
