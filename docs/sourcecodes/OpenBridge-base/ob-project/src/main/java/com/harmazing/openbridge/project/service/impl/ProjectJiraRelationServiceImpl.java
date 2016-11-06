package com.harmazing.openbridge.project.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.project.dao.ProjectJiraRelationMapper;
import com.harmazing.openbridge.project.service.IProjectJiraRelationService;
import com.harmazing.openbridge.project.vo.ProjectJiraRelation;
import com.harmazing.openbridge.project.vo.ProjectJiraStoryRelation;
import com.jcraft.jsch.Logger;

@Service
public class ProjectJiraRelationServiceImpl implements
		IProjectJiraRelationService {
	@Autowired
	private ProjectJiraRelationMapper projectJiraRelationMapper;

	@Override
	@Transactional
	public void saveOrUpdate(ProjectJiraRelation pjr, String[] storyIds) {
		String jiraKey = projectJiraRelationMapper.getJiraProjectKey(
				pjr.getProjectId(), pjr.getSource());
		projectJiraRelationMapper.deleteStroeIds(pjr.getId());
		if (StringUtil.isNotNull(jiraKey)) {
			projectJiraRelationMapper.update(pjr);
		} else {
			pjr.setCreationTime(new Date());
			pjr.setId(StringUtil.getUUID());
			projectJiraRelationMapper.insert(pjr);
		}
		if (storyIds != null) {
			for (int i = 0; i < storyIds.length; i++) {
				if(!storyIds[i].startsWith(pjr.getJiraProjectKey())){
					//只能选择项目下的故事
					continue ;
				}
				
				ProjectJiraStoryRelation story = new ProjectJiraStoryRelation();
				story.setId(StringUtil.getUUID());
				story.setJiraRelationId(pjr.getId());
				story.setCreationTime(new Date());
				story.setCreator(pjr.getCreator());
				story.setStoryKey(storyIds[i]);
				projectJiraRelationMapper.insertStroeId(story);
			}
		}
	}

	@Override
	@Transactional
	public void delete(String projectId, int source) {
		// TODO Auto-generated method stub
		ProjectJiraRelation s = projectJiraRelationMapper.get(projectId, source);
		if(s!=null){
			projectJiraRelationMapper.deleteStroeIds(s.getId());
		}
		projectJiraRelationMapper.delete(projectId, source);
	}

	@Override
	public ProjectJiraRelation get(String projectId, int source) {
		return projectJiraRelationMapper.get(projectId, source);
	}

	@Override
	public String getJiraProjectKey(String projectId, int source) {
		return projectJiraRelationMapper.getJiraProjectKey(projectId, source);
	}

	@Override
	public List<ProjectJiraStoryRelation> getJiraStoryRelation(
			String jiraRelationId) {
		return projectJiraRelationMapper.getJiraProjectStroyIds(jiraRelationId);
	}

	@Override
	public List<ProjectJiraRelation> findByProperty(ProjectJiraRelation paramObj) {
		return projectJiraRelationMapper.findByProperty(paramObj);
	}
}
