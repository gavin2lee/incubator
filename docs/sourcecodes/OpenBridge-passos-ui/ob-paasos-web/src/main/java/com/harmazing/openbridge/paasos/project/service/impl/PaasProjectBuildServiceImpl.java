package com.harmazing.openbridge.paasos.project.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.imgbuild.dao.PaasOsImageMapper;
import com.harmazing.openbridge.paasos.imgbuild.model.PaasOsImage;
import com.harmazing.openbridge.paasos.imgbuild.service.IPaasImageService;
import com.harmazing.openbridge.paasos.project.build.ProjectBuilder;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectBuildMapper;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.model.PaasProjectBuild;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paasos.project.model.vo.BuildImagePort;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectBuildService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;

@Service
public class PaasProjectBuildServiceImpl implements IPaasProjectBuildService {

	@Autowired
	private PaasProjectBuildMapper paasProjectBuildMapper;

	@Autowired
	private IPaasImageService iPaasImageService;
	
	@Autowired
	private IPaasProjectDeployService iPaasProjectDeployService;
	
	@Autowired
	private IPaasProjectService iPaasProjectService;
	
	@Autowired
	private PaasOsImageMapper paasOsImageMapper;
	
	@Autowired
	private ProjectBuilder projectBuilder;

	@Transactional
	@Override
	public void save(IUser user, PaasProjectBuild build) {
		boolean isUpdate = true;
		if (StringUtils.isEmpty(build.getBuildId())) {
			build.setBuildId(StringUtil.getUUID());
			build.setStatus(1);
			isUpdate = false;
			build.setCreateDate(new Date());
			build.setCreateUser(user.getUserId());
			
			if(StringUtils.hasText(build.getBuildNo())){
				//针对api 和app构建   文件名没有意义  同一个版本构建 文件名都相同 所以拿jekin构建id
				PaasProject pp = iPaasProjectService.getPaasProjectInfoById(build.getProjectId());
				build.setBuildTag(build.getVersionCode()+"-"+build.getBuildNo());
				build.setBuildName(pp.getProjectCode());
			}
			if(StringUtils.hasText(build.getImageName())){
				String dockerRegistry = ConfigUtil.getConfigString("paasos.docker.registry");
				// TODO 修改基础镜像
				String imageSource = build.getImageName();
				if (!imageSource.startsWith("/")) {
					imageSource = "/" + imageSource;
				}
				String imageName = dockerRegistry + imageSource;
				PaasOsImage poi = paasOsImageMapper.findImageByName(imageName, imageSource.split(":")[1]);
				if(StringUtils.hasText(build.getPort())){
					if(StringUtils.hasText(poi.getPorts())){
						List<BuildImagePort> imagePorts = JSONArray.parseArray(poi.getPorts(), BuildImagePort.class);
						List<BuildImagePort> taskPorts = JSONArray.parseArray(build.getPort(), BuildImagePort.class);
						Map<String,BuildImagePort> ref = new HashMap<String,BuildImagePort>();
						if(taskPorts==null){
							taskPorts = new ArrayList<BuildImagePort>();
						}
						if(taskPorts!=null && taskPorts.size()>0){
							for(BuildImagePort bp : taskPorts){
								ref.put(bp.getPort(),bp);
							}
						}
						if(imagePorts!=null && imagePorts.size()>0){
							for(BuildImagePort ip : imagePorts){
								if(!ref.containsKey(ip.getPort())){
									taskPorts.add(ip);
								}
							}
						}
						
						build.setPort(JSONArray.toJSONString(taskPorts));
					}
				}
				else{
					//如果没有传递 端口 以基础镜像为准
					build.setPort(poi.getPorts());
				}
				
			}
		}
		if (isUpdate) {
			PaasProjectBuild old = paasProjectBuildMapper.findById(build
					.getBuildId());
			if (old.getStatus() != null && old.getStatus().intValue() != 1
					&& old.getStatus().intValue() != 0) {
				throw new RuntimeException("构建不处于未构建、构建失败状态,不能修改");
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("buildId", build.getBuildId());
			param.put("versionId", build.getVersionId());
			param.put("fileName", build.getFileName());
			param.put("buildName", build.getBuildName());
			Integer l = paasProjectBuildMapper.findAlreadyCount(param);
			if (l != null && l.intValue() > 0) {
				throw new RuntimeException("该版本的构建文件已经被构建过，请删除在构建");
			}
			paasProjectBuildMapper.update(build);
		} else {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("versionId", build.getVersionId());
			param.put("fileName", build.getFileName());
			param.put("buildName", build.getBuildName());
			param.put("buildTag", build.getBuildTag());
			Integer l = paasProjectBuildMapper.findAlreadyCount(param);
			if (l != null && l.intValue() > 0) {
				throw new RuntimeException("该版本的构建文件已经被构建过，请删除在构建");
			}
			paasProjectBuildMapper.create(build);
		}
	}

	@Override
	public Page<Map<String, Object>> page(Map<String, Object> params) {
		Page<Map<String, Object>> xpage = Page.create(params);
		xpage.setRecordCount(paasProjectBuildMapper.pageCount(params));
		xpage.addAll(paasProjectBuildMapper.page(params));
		return xpage;
	}

	@Override
	public PaasProjectBuild findById(String buildId) {
		return paasProjectBuildMapper.findById(buildId);
	}

	@Transactional
	@Override
	public void beginBuild(String buildId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("buildId", buildId);
		param.put("status", 5);
		param.put("buildTime", new Date());
		paasProjectBuildMapper.updateStatus(param);
	}

	@Transactional
	@Override
	public void endBuild(String buildId, int status, String buildLogs) {
		endBuild(buildId, status, buildLogs, null);
	}

	@Transactional
	@Override
	public void endBuild(String buildId, int status, String buildLogs,
			String imageId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("buildId", buildId);
		param.put("status", status);
		param.put("buildLogs", buildLogs);
		param.put("buildSuccess", new Date());
		if (imageId != null) {
			param.put("imageId", imageId);
		}
		paasProjectBuildMapper.updateStatus(param);
	}

	@Transactional
	@Override
	public void deleteById(String buildId) {
		
		paasProjectBuildMapper.deleteById(buildId);
	}

	@Transactional
	@Override
	public void beginBuildDelete(String buildId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("buildId", buildId);
		param.put("deleteStatus", 1);
		paasProjectBuildMapper.updateStatus(param);
	}

	@Transactional
	@Override
	public void endBuildDelete(String buildId, int deleteStatus, String log) {
		if (deleteStatus == 3) {
			// 镜像删除成功
//			PaasProjectBuild pb = paasProjectBuildMapper.findById(buildId);
//			if (StringUtils.hasText(pb.getImageId())) {
//				iPaasImageService.deleteById(pb.getImageId());
//			}
			deleteById(buildId);
			return;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("buildId", buildId);
		param.put("deleteStatus", deleteStatus);
		param.put("buildLogs", log);
		paasProjectBuildMapper.updateStatus(param);
	}

	@Override
	public List<PaasProjectBuild> findAvailableBuild(String projectId) {
		return paasProjectBuildMapper.findAvailableBuild(projectId);
	}

	@Transactional
	@Override
	public void deleteByProjectId(String projectId) {
		 paasProjectBuildMapper.deleteByProjectId(projectId);
	}

	@Override
	public List<Map<String,Object>> findByVersionId(String versionId) {
		return paasProjectBuildMapper.findByVersionId(versionId);
	}

	@Override
	public Map<String,List<Map<String,String>>> getBuildImageByBusinessId(String businessId) {
		List<Map<String,String>> data = paasProjectBuildMapper.getBuildImageByBusinessId(businessId);
		if(data==null || data.size()==0){
			return new HashMap<String, List<Map<String,String>>>();
		}
		Map<String,List<Map<String,String>>> ref = new TreeMap<String, List<Map<String,String>>>();
		for(Map<String,String> d : data){
//			String imageId = d.get("imageId")+"";
//			String buildId = d.get("buildId")+"";
//			String buildName = d.get("buildName")+"";
//			String imageName = d.get("imageName")+"";
			String versionCode = d.get("versionCode")+"";
//			String imageVersion = d.get("imageVersion")+"";
//			String fileName = d.get("fileName")+"";
//			String ports = d.get("ports")+"";
			if(!ref.containsKey(versionCode)){
				ref.put(versionCode, new ArrayList<Map<String,String>>());
			}
			ref.get(versionCode).add(d);
		}
		return ref;
	}
	
	public List<Map<String, Object>> data(Map<String, String> param){
		return paasProjectBuildMapper.data(param);
	}

	@Override
	public List<Map<String, Object>> findAlreadBuildVersionList(String projectId) {
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("projectId", projectId);
		return paasProjectBuildMapper.findAlreadBuildVersionList(paraMap);
	}

	@Override
	public void deleteBuildById(String buildId) {
		PaasProjectBuild pb = paasProjectBuildMapper.findById(buildId);
		if(pb==null){
			return ;
		}
// 1已保存  5创建中 10创建成功 0创建失败   
//		if(pb.getStatus()!=null && pb.getStatus().intValue()==5){
//			throw new RuntimeException("镜像处于创建中，不能删除");
//		}
//		if(pb.getDeleteStatus()!=null && pb.getDeleteStatus().intValue()==1){
//			throw new RuntimeException("镜像处于删除中，不能删除");
//		}
		PaasProjectDeploy param = new PaasProjectDeploy();
		param.setBuildId(buildId);
		List<PaasProjectDeploy> pds = iPaasProjectDeployService.findDeployByEntity(param);
		if(pds!=null && pds.size()>0){
			throw new RuntimeException("该构建镜像已经有部署在使用，请先删除部署");
		}
		if(pb.getStatus()!=null && ( pb.getStatus().intValue()==1 || pb.getStatus().intValue()== 0) ){
			//处于1和0直接删除表中记录
			deleteById(buildId);
		}
		else{
			PaasProject paasProject = iPaasProjectService.getPaasProjectInfoById(pb.getProjectId());
			BuildTask buildTask = new BuildTask();
			buildTask.setTaskId(pb.getBuildId());
			buildTask.setBuildImageId(pb.getImageId());
			buildTask.setImageName(IMAGE_PREFIX+pb.getBuildName());
			buildTask.setImageVersion(pb.getBuildTag());
			buildTask.setProjectType(paasProject.getProjectType());
			projectBuilder.delete(buildTask);
		}
	}

}
