package com.harmazing.openbridge.sys.user.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.sys.user.dao.SysGroupMapper;
import com.harmazing.openbridge.sys.user.model.SysGroup;
import com.harmazing.openbridge.sys.user.service.ISysGroupService;
import com.harmazing.framework.util.StringUtil;

@Service
public class SysGroupService implements ISysGroupService {
	@Autowired
	private SysGroupMapper groupMapper;

	@Override
	public List<SysGroup> getAll() {
		return groupMapper.getAll();
	}

	// @Transactional(propagation = Propagation.REQUIRED)

	/**
	 * 群组列表
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Map<String, Object>> groupPage(Map<String, Object> params) {
		Page<Map<String, Object>> xpage = Page.create(params);
		xpage.setRecordCount(groupMapper.groupPageRecordCount(params));
		xpage.addAll(groupMapper.groupPage(params));
		return xpage;
	}

	/**
	 * 删除群组
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteGroup(String groupId) {
		// delete user and group relation
		groupMapper.deleteRelationByGroup(groupId);
		// delete group from sys_group
		groupMapper.deleteById(groupId);
	}

	/**
	 * 批量删除群组
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteBatchGroup(String[] groupIds) {
		if (groupIds != null) {
			for (String id : groupIds) {
				// delete user and group relation
				groupMapper.deleteRelationByGroup(id);
				// delete group from sys_group
				groupMapper.deleteById(id);
			}
		}
	}

	/**
	 * 通过Id获取单个群组的信息
	 */
	@Override
	@Transactional(readOnly = true)
	public SysGroup getGroupById(String groupId) {
		return groupMapper.getGroupById(groupId);
	}

	/**
	 * 通过groupId获取对应user的信息
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getUsersByGroupId(String groupId) {
		return groupMapper.getUsersByGroupId(groupId);
	}

	/**
	 * 修改群组名称和成员
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateGroup(String groupId, String groupName,
			String originMembers, String currentMembers) {
		// 确保修改后的groupName不能和其他记录的groupName相同
		SysGroup oldGroup = groupMapper.getGroupByName(groupName);
		if (oldGroup != null && !oldGroup.getGroupId().equals(groupId)) {
			return -1;
		}

		// 更新sys_group表
		groupMapper.updateGroupName(groupId, groupName);
		String[] origin = StringUtil.split(originMembers);
		String[] current = StringUtil.split(currentMembers);
		// 删除原来某些用户的关联
		if (origin != null) {
			for (String memberId : origin) {
				if (currentMembers.indexOf(memberId) == -1)
					groupMapper.deleteRelation(groupId, memberId);
			}
		}
		// 添加新用户的关联
		if (current != null) {
			groupMapper.deleteRelationByGroup(groupId);
			for (String memberId : current) {
				if (originMembers == ""
						|| originMembers.indexOf(memberId) == -1) {
					String relationId = StringUtil.getUUID();
					groupMapper.addRelation(relationId, memberId, groupId);
				}
			}
		}
		return 1;
	}

	/**
	 * 新增群组及成员
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addGroup(SysGroup group, String members) {
		// 确保新增的群组名称不能和已有的群组名相同
		SysGroup oldGroup = groupMapper.getGroupByName(group.getGroupName());
		if (oldGroup != null) {
			return -1;
		}
		// 新增群组信息到sys_group表
		groupMapper.addGroup(group);
		// 设置用户与群组的关联
		String[] memberIds = StringUtil.split(members);
		if (memberIds != null) {
			for (String memberId : memberIds) {
				String relationId = StringUtil.getUUID();
				groupMapper.addRelation(relationId, memberId,
						group.getGroupId());
			}
		}
		return 1;
	}

	@Override
	@Transactional(readOnly = true)
	public String[] getGroupByUserId(String userId) {
		 
		return null;
	}

}
