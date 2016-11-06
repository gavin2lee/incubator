package com.harmazing.openbridge.sys.user.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.sys.user.model.SysGroup;

public interface ISysGroupService extends IBaseService {
	public List<SysGroup> getAll();

	/**
	 * 群组成员列表
	 * 
	 * @param params
	 * @return
	 */
	Page<Map<String, Object>> groupPage(Map<String, Object> params);

	/**
	 * 删除群组
	 * 
	 * @param groupId
	 */
	void deleteGroup(String groupId);

	/**
	 * 批量删除群组
	 * 
	 * @param groupIds
	 */
	void deleteBatchGroup(String[] groupIds);

	/**
	 * 根据群组id获取群组信息
	 * 
	 * @param groupId
	 * @return
	 */
	SysGroup getGroupById(String groupId);

	/**
	 * 根据群组id获取成员id和名称
	 * 
	 * @param groupId
	 * @return
	 */
	List<Map<String, Object>> getUsersByGroupId(String groupId);

	/**
	 * 修改群组名称和成员
	 * 
	 * @param groupId
	 * @param groupName
	 * @param originMembers
	 * @param currentMembers
	 */
	int updateGroup(String groupId, String groupName, String originMembers,
			String currentMembers);

	/**
	 * 新增群组及成员
	 * 
	 * @param group
	 * @param members
	 */
	int addGroup(SysGroup group, String members);

	/**
	 * 根据用户获取用户所属于的组
	 * 
	 * @param userId
	 * @return
	 */
	String[] getGroupByUserId(String userId);
}
