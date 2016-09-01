package com.lachesis.mnisqm.module.user.dao;

import com.lachesis.mnisqm.module.user.domain.ComDeptUser;
import com.lachesis.mnisqm.module.user.domain.ComUser;

public interface ComDeptUserMapper {

    public int insert(ComDeptUser record);

    public ComDeptUser selectByUserCode(String userCode);

    /**
     * 更新离职
     * @param user
     */
    public void updateForLeave(ComUser user);
    
    /**
     * 员工科室变更
     * @param user
     */
    public void updateByPrimaryUser(ComDeptUser user);
}