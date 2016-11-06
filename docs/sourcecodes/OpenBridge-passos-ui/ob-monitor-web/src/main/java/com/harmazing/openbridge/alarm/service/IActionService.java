package com.harmazing.openbridge.alarm.service;

import com.harmazing.openbridge.alarm.model.Action;
import com.harmazing.openbridge.alarm.model.vo.ActionDTO;
import com.harmazing.openbridge.alarm.model.vo.ActionFalconDTO;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/5 9:35.
 */
public interface IActionService {
    Action findById(long id);
    ActionFalconDTO findFalconById(long id);
    Action findByUic(String uic);
    List<Action> findAll();
    Action insert(ActionDTO action);
    int update(Action action);
    int deleteById(long id);
    int getCountByUic(String uic);
}
