package com.harmazing.openbridge.alarm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.alarm.dao.GroupHostMapper;
import com.harmazing.openbridge.alarm.dao.GroupMapper;
import com.harmazing.openbridge.alarm.dao.GroupTemplateMapper;
import com.harmazing.openbridge.alarm.dao.HostMapper;
import com.harmazing.openbridge.alarm.dao.UserMapper;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.User;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;
import com.harmazing.openbridge.alarm.model.vo.GroupManageDTO;
import com.harmazing.openbridge.alarm.service.IGroupService;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/1 14:46.
 */
@Service
@Transactional
public class GroupServiceImpl implements IGroupService {
	@Autowired
	private GroupMapper groupMapper;
	@Autowired
	private GroupHostMapper groupHostMapper;
	@Autowired
	private GroupTemplateMapper groupTemplateMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private HostMapper hostMapper;

	@Override
	public List<Group> findAll() {
		return groupMapper.findAll();
	}

	public List<GroupIndexDTO> findAllDTO() {
		return groupMapper.findAllDTO();
	}

	public Group findById(long id) {
		return groupMapper.findById(id);
	}

	public void deleteById(long id) {
		groupHostMapper.deleteById(id);
		groupTemplateMapper.deleteByGrpId(id);
		groupMapper.deleteById(id);

	}

	public void save(String userId, String hostData, Group group) {
		Long id = group.getId();
		if (StringUtil.isNull(group.getGrpName())) {
			throw new RuntimeException("节点组名不能为空！");
		}
		int sysGroup = groupMapper.getCountByName(group.getGrpName(), group.getId());
		if (sysGroup > 0) {
			throw new RuntimeException("已存在节点组名,请修改！");
		}
		if (id.longValue() != 0) {
			groupHostMapper.deleteById(id);
			group.setCreateUser(userId);
			groupMapper.update(group);
		} else {
			group.setCreateUser(userId);
			groupMapper.insert(group);
		}
		if (StringUtil.isNotNull(hostData) && JSONArray.parseArray(hostData).size() > 0) {
			List<Map> host = JSONArray.parseArray(hostData, Map.class);
			for (Map m : host) {
				Long hostId = Long.parseLong(m.get("key").toString());
				groupHostMapper.insert(group.getId(), hostId);
			}
		}
	}

	public void saveDTO(GroupManageDTO dto) throws Exception {
		long id = dto.getId();
		if (StringUtil.isNull(dto.getGroupName())) {
			throw new Exception("节点组名不能为空！");
		}
		int sysGroup = groupMapper.getCountByName(dto.getGroupName(), dto.getId());
		if (sysGroup > 0) {
			throw new Exception("已存在节点组名,请修改！");
		}
		Group group = new Group();
		if (id != 0) {
			groupHostMapper.deleteById(id);
			groupTemplateMapper.deleteByGrpId(id);
			group = new Group(dto.getId(), dto.getGroupName(), dto.getCreateUser(), "1", "0");
			groupMapper.update(group);
		} else {
			group = new Group(dto.getGroupName(), dto.getCreateUser(), "1", "0");
			groupMapper.insert(group);
		}
		if (dto.getHostIds() != null && dto.getHostIds().size() > 0) {
			for (long hostId : dto.getHostIds()) {
				groupHostMapper.insert(group.getId(), hostId);
			}
		}
		if (dto.getTplIds() != null && dto.getTplIds().size() > 0) {
			for (long tplId : dto.getTplIds()) {
				groupTemplateMapper.insert(group.getId(), tplId, dto.getCreateUser());
			}
		}
	}

	@Override
	public List<User> findUserByGroupId(long id) {
        
		return userMapper.findUsersByGroupId(id);
	}

	@Override
	public Page<GroupIndexDTO> getPageDTO(int pageNo,int pageSize) {
		//查询出 分页的group
		Page<GroupIndexDTO> page=new Page<GroupIndexDTO>(pageNo, pageSize);
		page.addAll(groupMapper.findPageAllDTO(page.getStart(), page.getPageSize()));
		page.setRecordCount(groupMapper.getCount());	
		return page;
	}

	@Override
	public Page<User> findUserPageByGroupId(long id, int pageNo, int pageSize) {
		//查询出 分页的group
		Page<User> page=new Page<User>(pageNo, pageSize);
		page.addAll(userMapper.findUserPageByGroupId(id, page.getStart(), page.getPageSize()) );
		page.setRecordCount(userMapper.findUserCount(id));	
		return page;
	}
}
