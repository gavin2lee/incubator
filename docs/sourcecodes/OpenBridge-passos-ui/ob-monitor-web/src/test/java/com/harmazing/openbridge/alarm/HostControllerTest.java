package com.harmazing.openbridge.alarm;

import com.alibaba.fastjson.JSON;
import com.harmazing.openbridge.WebTestConfig;
import com.harmazing.openbridge.alarm.controller.HostController;
import com.harmazing.openbridge.alarm.dao.GroupMapper;
import com.harmazing.openbridge.alarm.dao.HostMapper;
import com.harmazing.openbridge.alarm.model.Group;
import com.harmazing.openbridge.alarm.model.Host;
import com.harmazing.openbridge.alarm.model.vo.HostDTO;
import com.harmazing.openbridge.alarm.service.IHostService;
import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by liyang on 2016/7/28.
 */
@Transactional
public class HostControllerTest extends WebTestConfig {
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

    @Test(expected=HttpClientErrorException.class)
    @Ignore
    public void pageNotFound() {
        try {
            RestTemplate rest = new RestTemplate();
            rest.getForObject("http://localhost:"+port+"/bogusPage", String.class);
            fail("Should result in HTTP 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            throw e;
        }
    }

    @Test
    @Ignore
    public void testControllerGgetById() throws Exception{
        HostDTO dto = new HostDTO(4,"123.456.789.0", "19191919", "","", 0, 0);
        Host entity = hostService.insert(dto);
        mockMvc.perform(MockMvcRequestBuilders.get("/hosts/"+entity.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hostname").value("123.456.789.0"))
                .andExpect(jsonPath("$.ip").value("19191919"));
    }

    @Test
    @Ignore
    public void testServiceHostInsert() throws Exception{
        HostDTO dto = new HostDTO(4,"123.456.789.0", "19191919", "","", 0, 0);
        Host entity = hostService.insert(dto);
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(entity));
    }

    @Test
    @Ignore
    public void testMapperHostInsert(){
        Host entity = new Host("123.456.789.0", "", "","", 0, 0);
        int key = hostMapper.insert(entity);
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(entity));
        log.info("auto generate entity hey is " + key);
    }

    @Test
    @Ignore
    public void testMapperGroupfindById(){
        Group dto = groupMapper.findById(1);
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
        log.info("dto is null :"+dto==null);
    }

    @Test
    @Ignore
    public void testMapperpageFindByGroupId(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",3);
        map.put("start",0);
        map.put("size",1);
        List<Host> dto = hostMapper.pageFindByGroupId(map);
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
    }

    @Test
    @Ignore
    public void testMapperfindByGroupId(){
        List<Host> dto = hostMapper.findByGroupId(3);
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
    }

    @Test
    @Ignore
    public void testServicefindById(){
        Host dto = hostService.findById(1);
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
    }

    @Test
    @Ignore
    public void testMapperfindById(){
        Host dto = hostMapper.findById(1);
        System.out.println("-----------------------");
        log.info(JSON.toJSONString(dto));
    }

    @Ignore
    @Test
    public void testEmpty() throws Exception{

    }
}
