package com.harmazing.openbridge.alarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.openbridge.alarm.dao.ActionMapper;
import com.harmazing.openbridge.alarm.dao.TemplateMapper;
import com.harmazing.openbridge.alarm.model.Action;
import com.harmazing.openbridge.alarm.model.Template;
import com.harmazing.openbridge.alarm.model.vo.ActionDTO;
import com.harmazing.openbridge.alarm.model.vo.ActionFalconDTO;
import com.harmazing.openbridge.alarm.service.IActionService;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/5 9:36.
 */
@Service
@Transactional
public class ActionServiceImpl implements IActionService {
	@Autowired
	private ActionMapper actionMapper;
	@Autowired
	private TemplateMapper templateMapper;
	@Override
	@Transactional(readOnly = true)
	public Action findById(long id) {
		return actionMapper.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public ActionFalconDTO findFalconById(long id) {
		return actionMapper.findFlaconById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Action findByUic(String uic) {
		return actionMapper.findByUic(uic);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Action> findAll() {
		return actionMapper.findAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Action insert(ActionDTO actionDTO) {
		Action action = null;
		Template template = templateMapper.findById(actionDTO.getTplId());
		if (template != null) {
			action = actionDTO.getAction();
			actionMapper.insert(action);
			templateMapper.updateActionIdById(action.getId(), template.getId());
		}
		return action;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(Action action) {
		return actionMapper.update(action);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteById(long id) {
		return actionMapper.deleteById(id);
	}

	@Override
	public int getCountByUic(String uic) {		
		return actionMapper.getCountByUic("uic");
	}
}
