package com.anyi.report.db;

import com.anyi.report.entity.ActionRecord;

import java.util.List;

/**
 * Copyright (c) 2016, Lachesis-mh.com
 * All rights reserved.
 * <p/>
 * Discription:模块行为记录表操作接口
 * <p/>
 * Created by junming.ren
 * on 2016/6/23.
 */
public interface ActionMapper {
    /**
     * 保存操作行为记录
     * @param record
     */
    public void saveActionRecord(ActionRecord record);

    /**
     * 获取某个模块中某个接口的操作记录
     * @param moduleName
     * @param interfaceName
     * @return
     */
    public List<ActionRecord> getRecordsByModuleAndInterface(String moduleName, String interfaceName);

}
