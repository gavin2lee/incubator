package com.harmazing.openbridge.paasos.manager.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.async.AsyncFactory;
import com.harmazing.openbridge.paas.async.AsyncRunnable;
import com.harmazing.openbridge.paasos.imgbuild.BuildCallback;
import com.harmazing.openbridge.paasos.imgbuild.BuildCommand;
import com.harmazing.openbridge.paasos.imgbuild.BuildStatus;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.imgbuild.DeleteCommand;
import com.harmazing.openbridge.paasos.manager.dao.PaaSBaseBuildMapper;
import com.harmazing.openbridge.paasos.manager.model.PaaSBaseBuild;
import com.harmazing.openbridge.paasos.manager.service.IPaaSBaseBuildService;
import com.harmazing.openbridge.web.fileupload.FileUploadHandler;

@Service
public class PaaSBaseBuildService implements IPaaSBaseBuildService {
	protected final static Logger logger = Logger
			.getLogger(IPaaSBaseBuildService.class);
	private static final String PREFIX_BASE = "base";
	@Resource
	private PaaSBaseBuildMapper paaSBaseBuildMapper;

	@Override
	public Page<PaaSBaseBuild> getPage(int pageNo, int pageSize) {
		Page<PaaSBaseBuild> page = new Page<PaaSBaseBuild>(pageNo, pageSize);
		page.addAll(paaSBaseBuildMapper.getPageData(page.getStart(),
				page.getPageSize()));
		page.setRecordCount(paaSBaseBuildMapper.getPageCount());
		return page;
	}

	@Override
	@Transactional
	public void delete(String id) {
		final PaaSBaseBuild build = this.getById(id);
		if (build != null) {
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
				paaSBaseBuildMapper.updateStatus(build.getId(),
						build.getStatus());// 更新状态
				AsyncFactory.execute(new AsyncRunnable(buildTask,
						new DeleteCommand(), new BuildCallback() {
					
							public void execute(BuildTask buildTask) {
								if (buildTask == null
										|| buildTask.getBuildStatus() == null) {
									build.setStatus(BuildStatus.DELETE_FAIL);
								} else {
									if (buildTask.getBuildStatus().intValue() != 0) {
										build.setStatus(BuildStatus.DELETE_FAIL);
										build.setBuildLogs(buildTask.getBuildLogs());
									} else {
										dbDelete(build);
									}
								}
								if(build.getStatus()==BuildStatus.DELETE_FAIL){
									paaSBaseBuildMapper.update(build);
								}
							}

						}));
			}
		}

	}

	protected void dbDelete(final PaaSBaseBuild build) {
		if (StringUtil.hasText(build.getFilePath())) {
			FileUtils.deleteQuietly(new File(build.getFilePath()));
		}
		if (StringUtil.hasText(build.getIconPath())) {
			FileUtils.deleteQuietly(new File(build.getIconPath()));
		}
		paaSBaseBuildMapper.delete(build.getId());
	}

	@Override
	@Transactional
	public void saveOrUpdate(PaaSBaseBuild build) {
		String id = build.getId();
		boolean add = true;
		if(!build.checkPortKeyDuplicate()){
			throw new RuntimeException("端口关键字不能重复！");
		}
		if (StringUtil.isNotNull(id)) {
			PaaSBaseBuild origin = paaSBaseBuildMapper.getById(id);
			if (null != origin) {
				add = false;
			}else{
				origin = new PaaSBaseBuild();
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
				logger.error("文件保存时从临时目录移动到正式目录失败", e1);
				throw new RuntimeException(e1);
			}
			//处理图片
			build.setIconPath(saveIcon(build.getIconPath(), origin.getIconPath()));
			
			if (!add) {
				paaSBaseBuildMapper.update(build);
			} else {
				id = StringUtil.getUUID();
				build.setId(id);
				build.setCreateTime(new Date());
				paaSBaseBuildMapper.add(build);
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
			logger.error("logo图片保存时从临时目录移动到正式目录失败", e1);
			throw new RuntimeException(e1);
		}
	}

	@Override
	public PaaSBaseBuild getById(String id) {
		return paaSBaseBuildMapper.getById(id);
	}

	@Override
	public List<PaaSBaseBuild> getTenantBuild(String tenantId) {
		if (StringUtil.isNull(tenantId)) {
			return new ArrayList<PaaSBaseBuild>();
		}
		return paaSBaseBuildMapper.getTenantBuild(tenantId);
	}

	@Override
	@Transactional
	public void build(String id, IUser user) {
		final PaaSBaseBuild build = this.getById(id);
		if (build != null) {
			BuildTask buildTask = build
					.generateBuildTask(id, user, PREFIX_BASE);
			build.setStatus(BuildStatus.BUILD_DOING);
			paaSBaseBuildMapper.updateStatus(build.getId(), build.getStatus());// 更新状态
			BuildCommand command = new BuildCommand(id);
			AsyncFactory.execute(new AsyncRunnable(buildTask,
					command, new BuildCallback() {

						@Override
						public void execute(BuildTask buildTask) {
							try {
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
								paaSBaseBuildMapper.update(build);
							} catch (Exception e) {
								logger.error("base build callback failed.taskid:"+buildTask.getTaskId(),e);
							}
						}

					}));
		}
	}

	@Override
	public String getBuildLog(String id) {
		
		return paaSBaseBuildMapper.getBuildLog(id);
	}


	@Override
	@Transactional
	public void updateLogoAndDesc(String id, String iconPath,
			String description, String tenantIds, String dockerfile) {
		PaaSBaseBuild origin = paaSBaseBuildMapper.getById(id);
		if(origin!=null){
			iconPath = saveIcon(iconPath, origin.getIconPath());
		}
		paaSBaseBuildMapper.updateLogoAndDesc(iconPath, description, tenantIds, dockerfile, id);
	}

	@Override
	public List<PaaSBaseBuild> getByName(String name) {
		if(StringUtil.isNull(name)){
			return new ArrayList<PaaSBaseBuild>();
		}
		return paaSBaseBuildMapper.getByName(name);
	}

	@Override
	public int getPublicImageCount() {
		return paaSBaseBuildMapper.getPublicImageCount();
	}

	
	@Override
	public int existNameAndVersion(String name, String version, String id) {
		List<String> list = paaSBaseBuildMapper.getByNameAndVersion(name, version);
		if(list.size()==1 && StringUtil.isNotNull(id)){
			if(list.get(0).equals(id)){
				return 0;
			}
		}
		return list.size();
	}
}
