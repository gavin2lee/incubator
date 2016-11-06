package com.harmazing.openbridge.alarm.model.vo;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/7/29 9:18.
 */
public class ParentDTO implements Serializable {

	private static final long serialVersionUID = 1L;


	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


    public String toJson() {
        return JSON.toJSONString(this);
    }


}