package com.harmazing.openbridge.project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.project.vo.ProjectJiraRelation;
import com.harmazing.openbridge.project.vo.ProjectJiraStoryRelation;

public interface ProjectJiraRelationMapper extends IBaseMapper {
	@Insert("insert into mod_project_jira_relation(id,project_id,jira_project_key,source,creator,creation_time) values(#{id},#{projectId},#{jiraProjectKey},#{source},#{creator},#{creationTime})")
	void insert(ProjectJiraRelation pjr);

	@Update("update mod_project_jira_relation set jira_project_key=#{jiraProjectKey} where project_id=#{projectId} and source=#{source}")
	void update(ProjectJiraRelation pjr);

	@Delete("delete from mod_project_jira_relation where project_id=#{projectId} and source=#{source}")
	void delete(@Param("projectId") String projectId,
			@Param("source") int source);

	@Select("select * from mod_project_jira_relation where project_id=#{projectId} and source=#{source}")
	ProjectJiraRelation get(@Param("projectId") String projectId,
			@Param("source") int source);

	@Select("select jira_project_key projectId from mod_project_jira_relation where project_id=#{projectId} and source=#{source}")
	String getJiraProjectKey(@Param("projectId") String projectId,
			@Param("source") int source);
	
	List<ProjectJiraRelation> findByProperty(ProjectJiraRelation paramObj);
	
	@Insert("insert into mod_project_jira_story_relation(`id`, `story_key`, `jira_relation_id`, `creator`, `creation_time`) values(#{id},#{storyKey},#{jiraRelationId},#{creator},#{creationTime})")
	void insertStroeId(ProjectJiraStoryRelation pjr);

	@Update("update mod_project_jira_story_relation  set `story_key`=#{storyKey}, `jira_relation_id`=#{jiraRelationId}, `creator`=#{creator}, `creation_time`=#{creationTime} where id=#{id}")
	void updateStroeId(ProjectJiraStoryRelation pjr);

	@Delete("delete from mod_project_jira_story_relation where jira_relation_id=#{jiraRelationId}")
	void deleteStroeIds(String jiraRelationId);

	@Select("select * from mod_project_jira_story_relation where jira_relation_id=#{jiraRelationId}")
	List<ProjectJiraStoryRelation> getJiraProjectStroyIds(
			@Param("jiraRelationId") String jiraRelationId);
}
