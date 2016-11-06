package com.harmazing.openbridge.paasos.project.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectMapper;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.model.PaasProjectBuild;
import com.harmazing.openbridge.paasos.project.model.vo.BuildVersion;
import com.harmazing.openbridge.paasos.project.model.vo.TwoTuple;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectBuildService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;
import com.harmazing.openbridge.paasos.store.dao.PaasStoreAppMapper;
import com.harmazing.openbridge.paasos.store.model.PaasStoreApp;
import com.harmazing.openbridge.paasos.store.service.IPaasStoreAppService;

@Service
public class PaasProjectServiceImpl implements IPaasProjectService {

	private static Logger logger = Logger
			.getLogger(PaasProjectServiceImpl.class);

	@Autowired
	private PaasProjectMapper paasProjectMapper;

	@Autowired
	private IPaasProjectBuildService iPaasProjectBuildService;

	@Autowired
	private IPaasProjectDeployService iPaasProjectDeployService;

	@Resource
	private IPaasStoreAppService paasStoreAppService;

	@Autowired
	private PaasStoreAppMapper paasStoreAppMapper;
	
	@Autowired
	private IPaasProjectEnvService iPaasProjectEnvService;

	@Override
	public PaasProject getPaasProjectInfoById(String projectId) {
		// TODO Auto-generated method stub
		return paasProjectMapper.getPaasProjectInfoById(projectId);
	}

	@Override
	public Page<Map<String, Object>> page(Map<String, Object> params) {
		Page<Map<String, Object>> xpage = Page.create(params);
		xpage.setRecordCount(paasProjectMapper.pageCount(params));
		xpage.addAll(paasProjectMapper.page(params));
		return xpage;
	}

	@Transactional
	public String save(String userId, PaasProject project) {
		boolean isUpdate = true;
		if (!StringUtils.hasText(project.getProjectCode())) {
			throw new RuntimeException("项目编码不能为空");
		}
		if (!StringUtils.hasText(project.getBusinessId())) {
			throw new RuntimeException("关联项目不能为空");
		}
		if (StringUtils.isEmpty(project.getProjectId())) {
			project.setProjectId(StringUtil.getUUID());
			isUpdate = false;
		} else {
			PaasProject old = paasProjectMapper.getPaasProjectInfoById(project
					.getProjectId());
			if (old.getProjectCode() != null
					&& !old.getProjectCode().equals(project.getProjectCode())) {
				PaasProjectDeploy param = new PaasProjectDeploy();
				param.setProjectId(old.getProjectId());
				List<PaasProjectDeploy> pd = iPaasProjectDeployService.findDeployByEntity(param);
				if (pd != null && pd.size() > 0) {
					throw new RuntimeException("该项目已经部署，请先删除部署在修改项目编码");
				}
			}

		}
		if (StringUtils.isEmpty(project.getCreateUser())) {
			project.setCreateUser(userId);
		}
		if (project.getCreateDate() == null) {
			project.setCreateDate(new Date());
		}
		Map<String, Object> so = new HashMap<String, Object>();
		if (isUpdate) {
			so.put("projectId", project.getProjectId());
		}
		so.put("businessId", project.getBusinessId());
		if (!project.getProjectType().equals("store")) {
			int m = paasProjectMapper.getCountCheckBusinessId(so);
			if (m > 0) {
				throw new RuntimeException("该外部项目已经关联,请选择其它");
			}
		} else {
			PaasStoreApp app = paasStoreAppService.getById(project
					.getBusinessId());
			project.setDescription(app.getDescription());
		}

		
		if (isUpdate) {
			paasProjectMapper.update(project);
		} else {
			/**
			 * update by weiyujia @2016/6/1 
			 * 添加一层校验，项目编码唯一性校验
			 */
			PaasProject paasProject_ = paasProjectMapper.getPaasProjectInfoByProjectCode(project.getProjectCode());
			if(paasProject_ != null){
				throw new RuntimeException("已存在该项目编码，请重新填写!");
			}
			paasProjectMapper.create(project);
		}
		return project.getProjectId();
	}

	@Override
	public Page<Map<String, Object>> listRelation(Map<String, Object> params,
			String type) {
		Page<Map<String, Object>> xpage = Page.create(params);

		if ("api".equals(type) || "app".equals(type)) {
			String url = "";
			String userId = params.get("userId").toString();
			params.remove("userId");
			if ("api".equals(type)) {
				url = ConfigUtil.getConfigString("paasos.api.url")
						+ "/api/rest/list.do";

			}
			if ("app".equals(type)) {
				url = ConfigUtil.getConfigString("paasos.app.url")
						+ "/app/rest/list.do";

			}
//			url = "http://localhost:8888/ob/api/rest/list.do";
			/**
			 * update by weiyujia 2016年5月30日 下午5:32:32
			 * 查询已存在的项目，用于在新建项目时过滤项目
			 */
			List<Map<String,Object>> existProjectMap = paasProjectMapper.getPaasProjectList();
			params.put("exisitIds", packageIdList(existProjectMap));
			String json = PaasAPIUtil.post(userId, url, DataType.FORM, params);
			if (StringUtil.isNotNull(json)) {
				JSONObject response = JSONObject.parseObject(json).getJSONObject("data");
				xpage.setRecordCount(response.getIntValue("recordCount"));
				JSONArray list =  response.getJSONArray("list");
				List<Map<String, Object>> xxx = new ArrayList<Map<String,Object>>();
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> val = new HashMap<String, Object>();
					JSONObject o = list.getJSONObject(i);
					Iterator<String> keys = o.keySet().iterator();
//					String serviceId = type.equals("api") ? o.getString("serviceId") : o.getString("appId");
//					if(/*!checkExistsFlag(serviceId, existProjectList)*/true){
					while(keys.hasNext()){
						String key = keys.next();
						String alias = key;
						if(key.equals("appId") || key.equals("serviceId")){
							alias = "id";
						}
						if(key.equals("appName") || key.equals("serviceName")){
							//为了与预置应用列表名称一致。故将app和api的名字从name改为app_name；
							alias = "appName";
						}
						if(key.equals("creationTime") || key.equals("createTime")){
							alias = "createTime";
						}
						if(key.equals("appLeaderName") || key.equals("serviceLeaderName")){
							alias = "leaderUser";
						}
						val.put(alias, o.get(key));
					}
					xxx.add(val);
				}
//				}
				xpage.addAll(xxx);
			}
		} else if ("store".equals(type)) {
//			params.put("status", 10);
			xpage.setRecordCount(paasStoreAppMapper.getStoreCount(params));
			xpage.addAll(paasStoreAppMapper.getStoreList(params));
		}
		return xpage;
	}


	/**
	* @Title: packageIdList 
	* @author weiyujia
	* @Description: 拼接值
	* @param existProjectMap
	* @return  设定文件 
	* @return Object    返回类型 
	* @throws 
	* @date 2016年6月3日 上午10:55:10
	 */
	private String packageIdList(List<Map<String, Object>> existProjectMap) {
		StringBuffer buffer = new StringBuffer();
		for(Map<String,Object> map : existProjectMap){
			buffer.append("'");
			buffer.append(map.get("businessId"));
			buffer.append("',");
		}
		return buffer.length() > 0 ? buffer.substring(0, buffer.length() - 1) : "";
	}

	/**
	* @Title: checkExistsFlag 
	* @author weiyujia
	* @Description: 检查serviceId是否已存在于已创建的项目
	* @param serviceId
	* @param existProjectList
	* @return  设定文件 
	* @return boolean    返回类型 
	* @throws 
	* @date 2016年5月30日 下午5:49:25
	 */
	public boolean checkExistsFlag(String serviceId, List<Map<String, Object>> existProjectList) {
		boolean flag = false;
		for(Map<String,Object> map : existProjectList){
			String businessId = (String) map.get("business_id");
			if(serviceId.equals(businessId)){
				flag = true;
				break;
			}
		}
		return flag;
	}

	@Override
	public TwoTuple<List<BuildVersion>,String> findBuildVersions(String businessId,
			String projectType, String userId) {

		String url = "";
		if (projectType.equals("app")) {
			url = ConfigUtil.getConfigString("paasos.app.url");
			if (!url.endsWith("/")) {
				url = url + "/";
			}
			url = url + "app/rest/version/list.do?appId=" + businessId;
		} else {
			url = ConfigUtil.getConfigString("paasos.api.url");
			if (!url.endsWith("/")) {
				url = url + "/";
			}
			url = url + "api/rest/version/list.do?serviceId=" + businessId;
		}
		logger.debug(url);
		String r = null;
		try {
			r = PaasAPIUtil.get(userId, url);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取接口失败");
			throw new RuntimeException("获取应用版本数据失败,情检查app，api应用是否正常");
		}

		logger.debug(r);
		if (!StringUtils.hasText(r)) {
			throw new RuntimeException("获取应用版本数据失败,情检查app，api应用是否正常");
		}
		JSONObject result = JSONObject.parseObject(r);
		int code = result.getIntValue("code");
		if (code != 0) {
			throw new RuntimeException(result.getString("msg"));
		}
		JSONObject data = result.getJSONObject("data");
		String framework = data.getString("framework");
		List<BuildVersion> jo = JSONObject.parseArray(data.getString("array"),
				BuildVersion.class);
		return new TwoTuple<List<BuildVersion>, String>(jo, framework);
		// List<BuildVersion> result = new ArrayList<BuildVersion>();
		// BuildVersion b1 = new BuildVersion();
		// b1.setVersionId("12313");
		// b1.setVersionName("1.0");
		// List<BuildVersionFile> files1 = new ArrayList<BuildVersionFile>();
		// files1.add(new BuildVersionFile("haha.zip","/data/haha.zip"));
		// files1.add(new BuildVersionFile("haha1.zip","/data/haha1.zip"));
		// b1.setFiles(files1);
		//
		// BuildVersion b2 = new BuildVersion();
		// b2.setVersionId("12313555");
		// b2.setVersionName("2.0");
		// List<BuildVersionFile> files2 = new ArrayList<BuildVersionFile>();
		// files2.add(new BuildVersionFile("haha.2zip","/data/haha23.zip"));
		// files2.add(new BuildVersionFile("haha1.1121zip","/data/haha2.zip"));
		// b2.setFiles(files2);
		//
		// result.add(b1);
		// result.add(b2);
		// return result;
	}

	@Override
	public List<PaasProject> getProjectByBusId(String businessId) {
		return paasProjectMapper.getProjectByBusId(businessId);
	}

	@Transactional
	@Override
	public void delete(String projectId) {
		PaasProjectDeploy param = new PaasProjectDeploy();
		param.setProjectId(projectId);
		List<PaasProjectDeploy> pd = iPaasProjectDeployService
				.findDeployByEntity(param);
		if (pd != null && pd.size() > 0) {
			throw new RuntimeException("该项目已经部署，请先删除部署");
		}
		List<PaasEnv> env = iPaasProjectEnvService.getEnvListByBusinessId(projectId);
		if(env !=null && env.size()>0){
			throw new RuntimeException("该项目已经创建环境，请先删除环境");
		}
		
		List<PaasProjectBuild> ppbs = iPaasProjectBuildService
				.findAvailableBuild(projectId);
		if (ppbs != null && ppbs.size() > 0) {
			for(PaasProjectBuild pb : ppbs){
				iPaasProjectBuildService.deleteBuildById(pb.getBuildId());
			}
		}

		iPaasProjectBuildService.deleteByProjectId(projectId);
		paasProjectMapper.delete(projectId);
	}

	@Transactional
	@Override
	public void deleteForAppApi(String businessId, String type) {
		PaasProject pp = paasProjectMapper.getPaasProjectByBusinessIdAndType(
				businessId, type);
		if (pp == null) {
			return ;
//			throw new RuntimeException("根据参数无法获取paas项目");
		}
		delete(pp.getProjectId());
	}

	@Override
	public PaasProject getPaasProjectByBusinessIdAndType(String businessId,
			String type) {
		PaasProject pp = paasProjectMapper.getPaasProjectByBusinessIdAndType(
				businessId, type);
		return pp;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updatePaasProjectByBusinessIdAndType(PaasProject project,String userId) {
		// TODO Auto-generated method stub
		PaasProject old = paasProjectMapper.getPaasProjectByBusinessIdAndType(project.getBusinessId(), project.getProjectType());
		if(StringUtils.hasText(project.getProjectCode()) && (!project.getProjectCode().equals(old.getProjectCode()))){
			//修改项目编号
			//判断有没有部署 有部署就不能修改
			//放在save中判断
//			PaasProjectDeploy param = new PaasProjectDeploy();
//			param.setProjectId(old.getProjectId());
//			List<PaasProjectDeploy> pd = iPaasProjectDeployService.findDeployByEntity(param);
//			if (pd != null && pd.size() > 0) {
//				throw new RuntimeException("该项目已经部署，请先删除部署在修改项目编码");
//			}
			old.setProjectCode(project.getProjectCode());
		}
		old.setProjectName(project.getProjectName());
		old.setDescription(project.getDescription());
		save(userId,old);
	}
}
