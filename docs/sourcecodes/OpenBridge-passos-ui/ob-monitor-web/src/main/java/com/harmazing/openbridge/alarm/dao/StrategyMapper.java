package com.harmazing.openbridge.alarm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.alarm.model.Strategy;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/4 9:48.
 */
public interface StrategyMapper extends IBaseMapper {
    @Select("SELECT id, metric, tags, max_step, priority, func, op, right_value, note, run_begin, run_end, " +
            "tpl_id FROM strategy ORDER BY id desc ")
    List<Strategy> findAll();
    @Select("SELECT id, metric, tags, max_step, priority, func, op, right_value, note, run_begin, run_end, " +
            "tpl_id FROM strategy WHERE id = #{id} ")
    Strategy findById(long id);
    @Select("SELECT id, metric, tags, max_step, priority, func, op, right_value, note, run_begin, run_end, " +
            "tpl_id FROM strategy WHERE tpl_id = #{tplId} ")
    Strategy findByTplId(long tplId);
    @Insert("INSERT INTO strategy (metric, tags, max_step, priority, func, op, right_value, note, " +
            "run_begin, run_end, tpl_id) VALUES (#{metric},#{tags},#{maxStep},#{priority},#{func},#{op}," +
            "#{rightValue},#{note},#{runBegin},#{runEnd},#{tplId}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Strategy strategy);
    @Delete("DELETE FROM strategy WHERE id = #{id} ")
    int deleteById(long id);
    
    @Update("UPDATE strategy SET strategy.`metric`=#{metric},strategy.`max_step`=#{maxStep},strategy.`priority`=#{priority},strategy.`func`=#{func},strategy.`op`=#{op},strategy.`right_value`=#{rightValue} WHERE id=#{id}")
    int update(Strategy strategy);
    
    @Delete("DELETE FROM strategy WHERE tpl_id = #{tplId}")
    int deleteByTplId(long tplId);
}
