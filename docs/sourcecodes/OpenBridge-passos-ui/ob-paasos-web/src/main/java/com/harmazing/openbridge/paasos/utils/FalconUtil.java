/**
 * Project Name:ob-paasos-web
 * File Name:FalconUtil.java
 * Package Name:com.harmazing.openbridge.paasos.utils
 * Date:2016年5月11日下午5:24:28
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.openbridge.paasos.project.model.vo.GraphAttrVo;

/**
 * ClassName:FalconUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月11日 下午5:24:28 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class FalconUtil {
	
	public static final List<GraphAttrVo> NODE_QUOTA = new ArrayList<GraphAttrVo>();

	public static final List<GraphAttrVo> DOCKER_QUOTA = new ArrayList<GraphAttrVo>();
	
	public static final String[] COUNT_FILE=new String[]{"disk","iface"};
	
	static{
		//docker 配置
		GraphAttrVo cpu = new GraphAttrVo();
		cpu.setId("cpu");
		cpu.setName("CPU使用情况");
		cpu.getLines().put("container.cpu.usage.busy", "CPU使用情况％");
		cpu.getLines().put("container.cpu.usage.system", "CPU系统使用情况％");
		cpu.getLines().put("container.cpu.usage.user", "CPU用户使用情况％");
		DOCKER_QUOTA.add(cpu);
		
		GraphAttrVo memory = new GraphAttrVo();
		memory.setId("memory");
		memory.setName("内存使用情况");
		memory.getLines().put("container.mem.limit", "内存总量M");
		memory.getLines().put("container.mem.usage", "内存使用情况M");
		DOCKER_QUOTA.add(memory);
		
		GraphAttrVo net = new GraphAttrVo();
		net.setId("net");
		net.setName("网络使用情况");
		net.getLines().put("container.net.if.in.bytes", "下载流量");
		net.getLines().put("container.net.if.out.bytes", "上传流量");
		DOCKER_QUOTA.add(net);
		
		//node配置

		GraphAttrVo cpuNode = new GraphAttrVo();
		cpuNode.setId("cpu");
		cpuNode.setName("CPU使用情况");
		cpuNode.getLines().put("cpu.busy", "CPU使用情况％");
		cpuNode.getLines().put("cpu.system", "CPU系统使用情况％");
		cpuNode.getLines().put("cpu.user", "CPU用户使用情况％");
		NODE_QUOTA.add(cpuNode);
		
		GraphAttrVo memoryNode = new GraphAttrVo();
		memoryNode.setId("memory");
		memoryNode.setName("内存使用情况");
		memoryNode.getLines().put("mem.memtotal", "内存总量M");
		memoryNode.getLines().put("mem.memused", "内存使用情况M");
		NODE_QUOTA.add(memoryNode);
		
		GraphAttrVo netNode = new GraphAttrVo();
		netNode.setId("net");
		netNode.setName("网络使用情况");
		netNode.getLines().put("net.if.in.bytes%", "下载流量");
		netNode.getLines().put("net.if.out.bytes%", "上传流量");
		NODE_QUOTA.add(netNode);
		
		GraphAttrVo diskNode = new GraphAttrVo();
		diskNode.setId("disk");
		diskNode.setName("磁盘使用情况");
		diskNode.getLines().put("disk.io.read_bytes%", "读取磁盘");
		diskNode.getLines().put("disk.io.write_bytes%", "写入磁盘");
		NODE_QUOTA.add(diskNode);
	}
	
	/*
	 *    '''
    params:
        endpoint_counters: [
            {
                "endpoint": "xx",
                "counter": "load.1min",
            },
            {
                "endpoint": "yy",
                "counter": "cpu.idle",
            },
        ]
    return:
        [
            {
                "endpoint": "xx",
                "counter": "yy",
                "Values": [
                    {
                        "timestamp": 1422868140,
                        "value": 0.32183299999999998
                    },
                    {
                        "timestamp": 1422868200,
                        "value": 0.406167
                    },
                ]
            },
            {
                "endpoint": "xx",
                "counter": "yy",
                "Values": [
                    {
                        "timestamp": 1422868140,
                        "value": 0.32183299999999998
                    },
                    {
                        "timestamp": 1422868200,
                        "value": 0.406167
                    },
                ]
            },
        ]
	 */
	
	
	public static String graphQuery(List<Map<String,String>> endpointCounters,String cf,long start,long end){
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("start", start);
		param.put("end", end);
		param.put("cf", cf);
		param.put("endpoint_counters", endpointCounters);
		String url = ConfigUtil.getConfigString("paasos.monitor.query");
		if(!url.endsWith("/")){
			url = url+"/";
		}
		url = url+"graph/history";
		JSONObject jo = new JSONObject(param);
		String msg = PaasAPIUtil.post("falcon", url, DataType.JSON, jo.toJSONString());
		return msg;
	}
	
	public static String graphLastQuery(List endpointCounters) {
		String url = ConfigUtil.getConfigString("paasos.monitor.query");
		if(!url.endsWith("/")){
			url = url+"/";
		}
		url = url+"graph/last";
		JSONArray jo = new JSONArray(endpointCounters);
		String msg = PaasAPIUtil.post("falcon", url, DataType.JSON, jo.toJSONString());
		return msg;
	}
	
	public static void main(String[] args){
//		Map<String,String> m1 =  new HashMap<String, String>();
//		m1.put("endpoint", "192.168.1.70");
//		m1.put("counter", "container.cpu.usage.busy/id=0431f74a5b1e0211ca0095c10bd4301f0efe6a0e1448f304e385c9627f9c176d");
//		List<Map<String,String>> endpointCounters = new ArrayList<Map<String,String>>();
//		endpointCounters.add(m1);
//		System.out.println(graphQuery(endpointCounters,"AVERAGE",1462953307,1462956847));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss Z");
		try {
			Date d = sdf.parse("2016-05-11 03:03:15 +0800");
		} catch (ParseException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

	

}

