package com.harmazing.openbridge.alarm.service;

import com.harmazing.openbridge.alarm.model.Strategy;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/4 14:19.
 */
public interface IStrategyService {
    void deleteById(long id);
    int insert(Strategy strategy);
    int update(Strategy strategy);
    int deleteByTplId(long tplId);
}
