package com.harmazing.openbridge.alarm;

import com.alibaba.fastjson.JSON;
import com.harmazing.framework.util.PaasAPIUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/11 17:45.
 */
public class UtilTest {
    @Test
    public void testMap(){
        Map<String,String> macheDocker = new HashMap<String,String>();
        String msg = PaasAPIUtil.get(null, "http://192.168.1.82:4194/api/v1.3/subcontainers");
        Pattern pa = Pattern.compile("\\[\\s*\"(k8s_POD[^\\s\"]+)\"\\s*\\,\\s*\"([^\\s\"]+)\"\\s*\\]");
        Matcher ma = pa.matcher(msg);
        while(ma.find()){
            String k8Name = ma.group(1);
            String k8Id = ma.group(2);
            macheDocker.put(k8Name, k8Id);
        }
        System.out.println(JSON.toJSONString(macheDocker));
    }
}
