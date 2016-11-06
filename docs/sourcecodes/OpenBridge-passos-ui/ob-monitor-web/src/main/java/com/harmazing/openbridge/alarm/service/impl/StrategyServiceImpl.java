package com.harmazing.openbridge.alarm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.openbridge.alarm.dao.StrategyMapper;
import com.harmazing.openbridge.alarm.model.Strategy;
import com.harmazing.openbridge.alarm.service.IStrategyService;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/4 14:20.
 */
@Service
@Transactional
public class StrategyServiceImpl implements IStrategyService {
    @Autowired
    private StrategyMapper strategyMapper;

    public int insert(Strategy strategy){
        return strategyMapper.insert(strategy);
    }

    public void deleteById(long id){
        strategyMapper.deleteById(id);
    }

	@Override
	public int update(Strategy strategy) {
		return strategyMapper.update(strategy);
	}

	@Override
	public int deleteByTplId(long tplId) {
		return strategyMapper.deleteByTplId(tplId);
	}
}
