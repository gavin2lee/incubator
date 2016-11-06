package com.harmazing.openbridge.alarm.service;

import java.util.List;

import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.User;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;
import com.harmazing.openbridge.alarm.model.vo.GroupManageDTO;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/1 14:42.
 */
public interface IGroupService {
	List<Group> findAll();

	List<GroupIndexDTO> findAllDTO();

	Group findById(long id);

	void deleteById(long id);

	void save(String userId, String hostData, Group group);

	void saveDTO(GroupManageDTO dto) throws Exception;

	List<User> findUserByGroupId(long id);
	
	Page<User> findUserPageByGroupId(long id,int pageNo,int pageSize );

    Page<GroupIndexDTO> 	getPageDTO(int pageNo,int pageSize);
}
