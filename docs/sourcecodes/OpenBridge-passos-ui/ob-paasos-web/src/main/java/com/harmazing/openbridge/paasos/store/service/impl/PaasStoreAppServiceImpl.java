package com.harmazing.openbridge.paasos.store.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.async.AsyncCallback;
import com.harmazing.openbridge.paas.async.AsyncFactory;
import com.harmazing.openbridge.paas.async.AsyncRunnable;
import com.harmazing.openbridge.paas.async.AsyncTask;
import com.harmazing.openbridge.paasos.imgbuild.BuildCommand;
import com.harmazing.openbridge.paasos.imgbuild.BuildStatus;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.imgbuild.DeleteCommand;
import com.harmazing.openbridge.paasos.manager.model.PaaSBaseBuild;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenantPreset;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;
import com.harmazing.openbridge.paasos.store.dao.PaasStoreAppMapper;
import com.harmazing.openbridge.paasos.store.model.PaasStoreApp;
import com.harmazing.openbridge.paasos.store.service.IPaasStoreAppService;
import com.harmazing.openbridge.web.fileupload.FileUploadHandler;

@Service
public class PaasStoreAppServiceImpl implements IPaasStoreAppService {
	protected final static Logger logger = Logger
			.getLogger(PaasStoreAppServiceImpl.class);
	private static final String PREFIX_BASE = "store";
	@Autowired
	private PaasStoreAppMapper paasStoreAppMapper;
	@Autowired
	private IPaasProjectService iPaasProjectService;

	public static class PaasStoreOptException extends Exception{
		//
		private static final long serialVersionUID = 1L;
		public PaasStoreOptException(){
			super();
		}
		public PaasStoreOptException(String message){
			super(message);
		}
	}
	
	@Override
	public Page<PaasStoreApp> getPage(int pageNo, int pageSize,String keyword,IUser iUser) {
		Page<PaasStoreApp> page = new Page<PaasStoreApp>(pageNo, pageSize);
		page.addAll(paasStoreAppMapper.getPageData(page.getStart(), page.getPageSize(),keyword,iUser));
		page.setRecordCount(paasStoreAppMapper.getPageCount(keyword,iUser));
		return page;
	}

	@Override
	public PaasStoreApp getById(String id) {
		return paasStoreAppMapper.getById(id);
	}

	@Override
	@Transactional
	public void delete(String id) throws PaasStoreOptException {
		final PaasStoreApp build = paasStoreAppMapper.getById(id);
		if (build != null) {
			if(iPaasProjectService.getProjectByBusId(id).size()>0){
				throw new PaasStoreOptException("删除项目后，才能删除！");
			}
			if (build.getStatus().equals(BuildStatus.BUILD_CHANGEING)
					|| build.getStatus().equals(BuildStatus.BUILD_DOING)) {
				throw new RuntimeException("当前状态不允许删除！");
			} else if (build.getStatus().equals(BuildStatus.SAVED)) {
				dbDelete(build);
			} else {
				BuildTask buildTask = new BuildTask();
				buildTask.setTaskId(build.getId());
				buildTask.setBuildImageId(build.getImageId());
				buildTask.setImageName(PREFIX_BASE + "/" + build.getName());
				buildTask.setImageVersion(build.getVersion());
				build.setStatus(BuildStatus.BUILD_CHANGEING);
				paasStoreAppMapper.updateStatus(build.getId(),
						build.getStatus());// 更新状态
				AsyncRunnable pr = new AsyncRunnable(buildTask,
						new DeleteCommand(), new AsyncCallback() {

							@Override
							public void execute(AsyncTask task) {
								BuildTask buildTask = (BuildTask) task;
								if (buildTask == null
										|| buildTask.getBuildStatus() == null) {
									build.setStatus(BuildStatus.DELETE_FAIL);
								} else {
									if (buildTask.getBuildStatus().intValue() != 0) {
										build.setStatus(BuildStatus.DELETE_FAIL);
									} else {
										dbDelete(build);
									}
									build.setBuildLogs(buildTask.getBuildLogs());
								}
								if(build.getStatus()==BuildStatus.DELETE_FAIL){
									paasStoreAppMapper.update(build);
								}
							}

						});
				AsyncFactory.execute(pr);
			}

		}
	}

	protected void dbDelete(final PaasStoreApp build) {
		if (StringUtil.hasText(build.getFilePath())) {
			FileUtils.deleteQuietly(new File(build.getFilePath()));
		}
		if (StringUtil.hasText(build.getIconPath())) {
			FileUtils.deleteQuietly(new File(build.getIconPath()));
		}
		List<String> files = getConigFile(build);
		for (String filepPath : files) {
			FileUtils.deleteQuietly(new File(filepPath));
		}
		paasStoreAppMapper.delete(build.getId());
	}

	/**
	 * @author chenjinfan
	 * @Description 获取config中的文件
	 * @param origin
	 * @return
	 */
	protected List<String> getConigFile(PaasStoreApp origin) {
		List<String> list = new ArrayList<String>();
		if (StringUtil.hasText(origin.getConfig())) {// 删除初始化文件
			String configString = origin.getConfig();
			JSONArray array = JSON.parseArray(configString);
			Iterator<Object> iterator = array.iterator();
			while (iterator.hasNext()) {
				JSONObject object = (JSONObject) iterator.next();
				JSONObject initAction = object.getJSONObject("initAction");
				if (initAction != null) {
					JSONObject file = initAction.getJSONObject("file");
					if (file != null) {
						String filePath = file.getString("filePath");
						list.add(filePath);
					}
				}
			}
		}
		return list;
	}

	@Override
	@Transactional
	public void saveOrUpdate(PaasStoreApp build,IUser user) {
		String id = build.getId();
		boolean add = true;
		if(!build.checkPortKeyDuplicate()){
			throw new RuntimeException("端口关键字不能重复！");
		}
		if (StringUtil.isNotNull(id)) {
			PaasStoreApp origin = paasStoreAppMapper.getById(id);
			if (null != origin) {
				add = false;
			}else{
				origin = new PaasStoreApp();
			}
			//处理文件
			try {
				String newBuildFilePath = FileUploadHandler
						.delOldFileAndCutNewFile(origin.getFilePath(),
								build.getFilePath());
				if (StringUtil.isNotNull(newBuildFilePath)) {
					build.setFilePath(newBuildFilePath);
				}
			} catch (IOException e1) {
				logger.error("文件保存时从临时目录移动到正式目录", e1);
				throw new RuntimeException(e1);
			}
			//处理图片
			build.setIconPath(saveIcon(build.getIconPath(), origin.getIconPath()));
			//处理依赖资源中的初始化文件
			List<String> originFiles = getConigFile(origin);
			List<String> buildFiles = getConigFile(build);
			try {
				build.setConfig(FileUploadHandler.delOldFileAndCutNewFile(
						originFiles, buildFiles, build.getConfig()));
			} catch (IOException e) {
				logger.error("数据库资源初始化脚本文件保存时从临时目录移动到正式目录", e);
				throw new RuntimeException(e);
			}
			if (!add) {
				build.setUpdateTime(new Date());
				paasStoreAppMapper.update(build);
			} else {
				id = StringUtil.getUUID();
				build.setId(id);
				build.setCreateTime(new Date());
				paasStoreAppMapper.add(build);
				PaaSTenantPreset paaSTenantPreset = new PaaSTenantPreset();
				paaSTenantPreset.setId(StringUtil.getUUID());
				paaSTenantPreset.setTenantId(user.getTenantId());
				paaSTenantPreset.setPresetId(id);
				paasStoreAppMapper.addSysTenantPreset(paaSTenantPreset);
			}
		}
	}
	protected String saveIcon(String iconPath, String originIconPath) {
		try {
			// logo图片
			String newIconPath = FileUploadHandler
					.delOldFileAndCutNewFile(originIconPath,
							iconPath);
			return newIconPath;
		} catch (IOException e1) {
			logger.error("logo图片保存时从临时目录移动到正式目录", e1);
		}
		return null;
	}
	@Override
	@Transactional
	public void build(String id, IUser user) {
		final PaasStoreApp build = this.getById(id);
		if (build != null) {
			BuildTask buildTask = build
					.generateBuildTask(id, user, PREFIX_BASE);
			build.setStatus(BuildStatus.BUILD_DOING);
			paasStoreAppMapper.updateStatus(build.getId(), build.getStatus());// 更新状态
			AsyncFactory.execute(new AsyncRunnable(buildTask,
					new BuildCommand(id), new AsyncCallback() {

						@Override
						public void execute(AsyncTask task) {//回调
							BuildTask buildTask = (BuildTask) task;
							if (buildTask == null
									|| buildTask.getBuildStatus() == null) {
								build.setStatus(BuildStatus.BUILD_FAIL);
							} else {
								if (buildTask.getBuildStatus().intValue() != 0) {
									build.setStatus(BuildStatus.BUILD_FAIL);
								} else {
									build.setStatus(BuildStatus.BUILD_SUCCESS);
									build.setImageId(buildTask
											.getBuildImageId());
								}
								build.setBuildLogs(buildTask.getBuildLogs());
							}
							paasStoreAppMapper.update(build);
						}

					}));
		}
	}

	@Override
	@Transactional(readOnly=true)
	public String getBuildLog(String id) {
		return paasStoreAppMapper.getBuildLog(id);
	}

	@Override
	public int existNameAndVersion(String name, String version, String id) {
		List<String> list = paasStoreAppMapper.getByNameAndVersion(name, version);
		if(list.size()==1 && StringUtil.isNotNull(id)){
			if(list.get(0).equals(id)){
				return 0;
			}
		}
		return list.size();
	}

	@Override
	@Transactional
//	public void updateLogoAndDesc(String id, String iconPath, String description,String appName,String documentation,String dockerfile) {
	public void updateLogoAndDesc(PaasStoreApp build) throws IOException{
		PaaSBaseBuild origin = paasStoreAppMapper.getById(build.getId());
		if(origin!=null){
			build.setIconPath(saveIcon(build.getIconPath(), origin.getIconPath()));
		}
		String newFilePath = FileUploadHandler.delOldFileAndCutNewFile(origin.getFilePath(),build.getFilePath());
		if (StringUtil.isNotNull(newFilePath)) {
			build.setFilePath(newFilePath);
		}
//		paasStoreAppMapper.updateLogoAndDesc(iconPath, description, id,appName,documentation,dockerfile);
		paasStoreAppMapper.updateLogoAndDesc(build);
	}
}
