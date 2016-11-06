package com.harmazing.openbridge.monitor;

import com.alibaba.fastjson.JSON;
import com.harmazing.openbridge.WebTestConfig;
import com.harmazing.openbridge.monitor.service.IAppMonitorService;
import com.harmazing.openbridge.util.KubernetesUtil;
import io.fabric8.kubernetes.api.model.PodList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.jdbc.SQL;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/15 9:43.
 */
public class AppMonitorControllerTest extends WebTestConfig {
    private final Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    private IAppMonitorService appMonitorService;

    @Ignore
    @Test
    public void testgetPods() throws Exception{
        PodList pl = KubernetesUtil.getPods(null, new HashMap<String, String>() {{
            put("projectCode", appMonitorService.findProjectCodeByProjectId("70p0f5k69htfpx2jspztyul8pkbn3ez"));
        }});
        logger.info("---------findProjectCodeByProjectId---------------");
        logger.info(JSON.toJSONString(pl));
        logger.info("---------findProjectCodeByProjectId---------------");
    }

    @Ignore
    @Test
    public void testFind() throws Exception {
        String projectCode = appMonitorService.findProjectCodeByProjectId("70p0f5k69htfpx2jspztyul8pkbn3ez");
        logger.info("---------findProjectCodeByProjectId---------------");
        logger.info(projectCode);
        logger.info("---------findProjectCodeByProjectId---------------");
        List<Map<String, Object>> map = appMonitorService.findDeployByProjectId(10, "70p0f5k69htfpx2jspztyul8pkbn3ez");
        logger.info("---------findDeployByProjectId---------------");
        logger.info(JSON.toJSONString(map));
        logger.info("---------findDeployByProjectId---------------");
    }


    @Ignore
    @Test
    public void testSQLBuilder() throws Exception {
        SQL sql = new SQL() {{
            DELETE_FROM("PERSON");
            WHERE("ID = #{id}");
        }};
        logger.info("---------testSQLBuilder---------------");
        logger.info(sql.toString());
        logger.info("---------testSQLBuilder---------------");
    }

    @Ignore
    @Test
    public void testEmpty() throws Exception {

    }
}
