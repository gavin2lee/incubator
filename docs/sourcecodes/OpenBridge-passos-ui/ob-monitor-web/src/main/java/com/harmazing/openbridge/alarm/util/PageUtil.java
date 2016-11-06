package com.harmazing.openbridge.alarm.util;

import com.harmazing.framework.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/7/28 18:35.
 */
public class PageUtil {
    public static Map<String, Object> initPageSize(Integer pageNo, Integer pageSize){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageNo", getIntParam(pageNo, 0));
        params.put("pageSize", getIntParam(pageSize, 10));
        return params;
    }
    public static int getIntParam(Integer value,int defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return value.intValue();
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }
}
