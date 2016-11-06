package com.harmazing.openbridge.project.service;

import java.util.List;

import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.project.vo.ProjectJiraRelation;
import com.harmazing.openbridge.project.vo.ProjectJiraStoryRelation;

/**
 * @author chenjinfan
 * @Description 项目与jira关联服务
 */
public interface IProjectJiraRelationService extends IBaseService {

	/**
	 * @author chenjinfan
	 * @Description 添加项目jira关联，若关联已存在则修改。
	 * @param pjr
	 */
	void saveOrUpdate(ProjectJiraRelation pjr, String[] storyIds);

	/**
	 * @author chenjinfan
	 * @Description 根据项目ID、关联来源删除关联。
	 * @param projectId
	 * @param source
	 */
	void delete(String projectId, int source);

	/**
	 * @author chenjinfan
	 * @Description 获取项目jira关联记录
	 * @param projectId
	 * @return
	 */
	ProjectJiraRelation get(String projectId, int source);

	/**
	 * @author chenjinfan
	 * @Description 获取关联的jira项目key
	 * @param projectId
	 * @return
	 */
	String getJiraProjectKey(String projectId, int source);

	List<ProjectJiraStoryRelation> getJiraStoryRelation(String jiraRelationId);
	
	/**
	 * 条件查询app jiraProject 关联
	 * @author chenjinfan
	 * @Description
	 * @param paramObj
	 * @return
	 */
	List<ProjectJiraRelation> findByProperty(ProjectJiraRelation paramObj);
}
