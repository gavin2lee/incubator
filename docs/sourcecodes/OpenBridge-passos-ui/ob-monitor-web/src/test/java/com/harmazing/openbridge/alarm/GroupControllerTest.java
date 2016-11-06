package com.harmazing.openbridge.alarm;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.harmazing.openbridge.WebTestConfig;
import com.harmazing.openbridge.alarm.dao.GroupMapper;
import com.harmazing.openbridge.alarm.dao.HostMapper;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.vo.GroupIndexDTO;
import com.harmazing.openbridge.alarm.service.IHostService;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/1 10:30.
 */
@Transactional
public class GroupControllerTest extends WebTestConfig {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private Environment env;
    @Autowired
    private HostMapper hostMapper;
    @Autowired
    private GroupMapper groupMapper;
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
    public void testfindAllDTO(){
        List<GroupIndexDTO> dto = groupMapper.findAllDTO();
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
    }

    @Test
    @Ignore
    public void testControllerGgetById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/groups/4/hosts")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Ignore
    public void testCRUD(){
        Group group = new Group("test1","liyang","1","0");
        System.out.println("-----------insert------------");
        groupMapper.insert(group);
        System.out.println("-----------findById------------");
        Group entity = groupMapper.findById(group.getId());
        log.info(JSON.toJSONString(entity));
        System.out.println("-----------update------------");
        entity.setCreateUser("ssss");
        groupMapper.update(entity);
        entity = groupMapper.findById(group.getId());
        log.info(JSON.toJSONString(entity));
        System.out.println("-----------deleteById------------");
        groupMapper.deleteById(group.getId());

    }

    @Ignore
    @Test
    public void testEmpty() throws Exception{

    }
}
