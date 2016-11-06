package com.harmazing.openbridge.alarm;

import com.alibaba.fastjson.JSON;
import com.harmazing.openbridge.WebTestConfig;
import com.harmazing.openbridge.alarm.dao.GroupMapper;
import com.harmazing.openbridge.alarm.dao.HostMapper;
import com.harmazing.openbridge.alarm.dao.TemplateMapper;
import com.harmazing.openbridge.alarm.model.Template;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateEditDTO;
import com.harmazing.openbridge.alarm.model.vo.TemplateIndexDTO;
import com.harmazing.openbridge.alarm.service.IHostService;
import com.harmazing.openbridge.alarm.util.MetricsUtil;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/3 17:24.
 */
public class TemplateControllerTest extends WebTestConfig {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private Environment env;
    @Autowired
    private HostMapper hostMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private IHostService hostService;
    @Value("${local.server.port}")
    private int port;
    @Autowired
    private WebApplicationContext webContext;
    private MockMvc mockMvc;
    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    //@Ignore
    public void testfindDtoById(){
        TemplateEditDTO dto = templateMapper.findDtoById(7);
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
    }

    @Test
    @Ignore
    public void testgetMertics(){
        MetricsUtil metrics = new MetricsUtil();
        List<String> str = metrics.getMertics("cp");
        System.out.println(str);
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(str));
    }

    @Test
    @Ignore
    public void testfindAllDTO(){
        List<TemplateIndexDTO> dto = templateMapper.findAllDTO();
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
    }

    @Test
    @Ignore
    public void testfindAll(){
        List<Template> dto = templateMapper.findAll();
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
    }

    @Ignore
    @Test
    public void testEmpty() throws Exception{

    }
}
