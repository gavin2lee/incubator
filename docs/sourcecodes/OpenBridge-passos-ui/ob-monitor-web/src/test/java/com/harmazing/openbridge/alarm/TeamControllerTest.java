package com.harmazing.openbridge.alarm;

import com.alibaba.fastjson.JSON;
import com.harmazing.openbridge.WebTestConfig;
import com.harmazing.openbridge.alarm.dao.GroupMapper;
import com.harmazing.openbridge.alarm.dao.HostMapper;
import com.harmazing.openbridge.alarm.dao.TeamMapper;
import com.harmazing.openbridge.alarm.dao.TemplateMapper;
import com.harmazing.openbridge.alarm.model.Team;
import com.harmazing.openbridge.alarm.model.User;
import com.harmazing.openbridge.alarm.service.IHostService;
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
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/3 19:23.
 */
public class TeamControllerTest extends WebTestConfig {
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
    private TeamMapper teamMapper;
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
    @Ignore
    public void testgetPageData(){
        List<Team> dto = teamMapper.getPageData(0,10);
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
    }

    @Ignore
    @Test
    public void testEmpty() throws Exception{

    }

    @Ignore
    @Test
    public void testgetAllUsesrByTeamName(){
        List<User> dto = teamMapper.getAllUserByTeamName("343");
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
    }
}
