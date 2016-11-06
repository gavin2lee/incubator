/**
 * Project Name:ob-paasos-web
 * File Name:PaasProjectMonitorController.java
 * Package Name:com.harmazing.openbridge.paasos.project.controller
 * Date:2016年5月11日上午10:41:44
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.controller;

import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.vo.MonitorForm;
import com.harmazing.openbridge.paasos.project.model.vo.FalconVo;
import com.harmazing.openbridge.paasos.project.model.vo.FourTuple;
import com.harmazing.openbridge.paasos.project.model.vo.GraphAttrVo;
import com.harmazing.openbridge.paasos.project.model.vo.HighChartVo;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectBuildService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;
import com.harmazing.openbridge.paasos.utils.FalconUtil;
import com.harmazing.openbridge.paasos.utils.KubernetesUtil;

/**
 * ClassName:PaasProjectMonitorController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月11日 上午10:41:44 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
@Controller
@RequestMapping("/project/monitor")
public class PaasProjectMonitorController extends ProjectBaseController {
	
	private static Log logger =LogFactory.getLog(PaasProjectMonitorController.class);
	
	
	@Autowired
	private IPaasProjectDeployService iPaasProjectDeployService;
	
	//网络转换    docker监控的是上层docker的网络  作个映射
	//实际docker id --->虚拟docker id
	private static Map<String,String> dockerNet = new HashMap<String,String>();
	
	//虚拟docker name -->虚拟docker id
	private static Map<String,String> macheDocker = new HashMap<String,String>();
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject app = loadProjectModel(request);
			 
			
			PaasProjectDeploy param = new PaasProjectDeploy();
			param.setProjectId(app.getProjectId());
			param.setStatus(10);
			List<PaasProjectDeploy> projectDeploy = iPaasProjectDeployService.findDeployByEntity(param);
			request.setAttribute("projectDeploys", projectDeploy);
			
			Map<String,String> labels = new HashMap<String, String>();
			labels.put("projectCode", app.getProjectCode());
			PodList pl = KubernetesUtil.getPods(null, labels);
			request.setAttribute("podList", pl);
			
			return getUrlPrefix() + "/index";
		} catch (Exception e) {
			logger.error("失败", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	/**
	 *   在调用监控之前 获取监控一些预置数据
	 * @return
	 */
	@RequestMapping("/monitorPreData")
	@ResponseBody
	public JsonResponse monitorPreData(HttpServletRequest request, HttpServletResponse response){
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject app = loadProjectModel(request);
			String deployId = request.getParameter("key"); 
			
			
			Map<String,String> labels = new HashMap<String, String>();
			labels.put("serviceId", deployId);
			PodList pl = KubernetesUtil.getPods(null, labels);
			Map<String,Object> result = new HashMap<String,Object>();
			
			//pods 数据
			List<Map> param = new ArrayList<Map>();
			if(pl==null || pl.getItems()==null || pl.getItems().size()==0){
				throw new RuntimeException("无法获取部署关联的容器信息");
			}
			for(Pod pod: pl.getItems()){
				if(pod==null || pod.getStatus()==null || 
						CollectionUtils.isEmpty(pod.getStatus().getContainerStatuses())){
					continue;
				}
				String hostIp = pod.getStatus().getHostIP();
				for(ContainerStatus cs : pod.getStatus().getContainerStatuses()){
					if(cs.getState()==null || cs.getState().getRunning()==null){
						continue ;
					}
					Map<String,Object> p = new HashMap<String,Object>();
					p.put("endpoint", hostIp);
					p.put("dockerid", cs.getContainerID().replace("docker://", ""));
					p.put("key", pod.getMetadata().getName());
					param.add(p);
				}
			}
			if(CollectionUtils.isEmpty(param)){
				throw new RuntimeException("该部署的容器都不属于运行状态");
			}
			//在获取
			FourTuple<Map<String,List<GraphAttrVo>>, Map<String,String>,
				Map<String,String>, List<Map<String,String>>> f = obtainPreData(false,param);
			
			result.put("pods", pl);
			result.put("last", f.a);
			result.put("graph2Id", f.b);
			result.put("graph2Name", f.c);
			result.put("counts", f.d);
			return JsonResponse.success(result);
		} catch (Exception e) {
			logger.error("获取信息失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	/**
	 * 	@param isNode 是否为监控的为节点
	 * 	@param param 监控的样本
	 *  a:  
	 *  b: graph 对应 图形ID
	 *  c: graph 对应的 图形名称
	 *  d: 
	 * @return
	 */
	private FourTuple<Map<String,List<GraphAttrVo>>, Map<String,String>, 
			Map<String,String>, List<Map<String,String>>> obtainPreData(boolean isNode,List<Map> param){
		
		//ip---> counts
		Map<String,List<String>> cd = null;
		if(isNode){
			//如果监控的是节点  获取每个节点的所有的counts数据  因为例如网络，磁盘等每个节点都不一样 要模糊匹配出来
			String endpoints= "";
			for(Map<String,Object>  j : param){
				endpoints =endpoints+","+ j.get("endpoint");
			}
			endpoints = endpoints.substring(1);
			//不能查全部的counts 默认只能查500个   具体去查有%比的数据
			Map<String,Object> counters = counters(endpoints);
			if(counters==null || counters.get("data")==null){
				throw new RuntimeException("根据endpoint去获取counter失败");
			}
			cd = (Map<String,List<String>>) counters.get("data");
		}
		
		//如果从项目来  key为rc名称  如果从节点来key为节点ip
		Map<String,List<GraphAttrVo>> last = new TreeMap<String, List<GraphAttrVo>>(); //根据key分组  例如每个pod cpu 内存 网络在同一个层次
		Map<String,String> graph2Id = new HashMap<String, String>();//graph 对应 图形ID   key都为endpoint+"_"+counter
		Map<String,String> graph2Name = new HashMap<String, String>();//graph 对应的 图形名称  key都为endpoint+"_"+counter
		List<Map<String,String>> counts = new ArrayList<Map<String,String>>();//最终调用query 查询到参数
		for(Map<String,Object> j : param){
			String key = j.get("key")+"";
			String endpoint = j.get("endpoint")+"";
			if(!last.containsKey(key)){
				last.put(key, new ArrayList<GraphAttrVo>());
			}
			if(!isNode){
				//设置每个每个图形需要的查询需要的属性
				String dockerid = j.get("dockerid")+"";
				for(GraphAttrVo v : FalconUtil.DOCKER_QUOTA){
					GraphAttrVo c = new GraphAttrVo();
					String id =(key+"_"+v.getId()).replaceAll("\\.", "_");
					c.setId(id);
					c.setName(v.getName());
					for(Map.Entry<String, String> e : v.getLines().entrySet()){
						String n = e.getValue();
						String k = e.getKey();
						String ndid = null;
						if(k.startsWith("container.net")){
							//如果是网络  就要去获取docker 网络接口 挂在的 另外一个docker网络
							if(dockerNet.containsKey(dockerid)){
								ndid = dockerNet.get(dockerid);
							}
							if(!StringUtils.hasText(ndid)){
								for(String vid : macheDocker.keySet()){
									if(vid.indexOf(key)<0){
										continue ;
									}
									dockerNet.put(dockerid, macheDocker.get(vid));
									ndid = macheDocker.get(vid);
								}
							}
							if(!StringUtils.hasText(ndid)){
								try{
									String msg = PaasAPIUtil.get(null, "http://"+endpoint+":4194/api/v1.3/subcontainers");
									Pattern pa = Pattern.compile("\\[\\s*\"(k8s_POD[^\\s\"]+)\"\\s*\\,\\s*\"([^\\s\"]+)\"\\s*\\]");
									Matcher ma = pa.matcher(msg);
									while(ma.find()){
										String k8Name = ma.group(1);
										String k8Id = ma.group(2);
										macheDocker.put(k8Name, k8Id);
									}
									for(String vid : macheDocker.keySet()){
										if(vid.indexOf(key)<0){
											continue ;
										}
										dockerNet.put(dockerid, macheDocker.get(vid));
										ndid = macheDocker.get(vid);
									}
									logger.debug(ndid+"获取成功");
								}
								catch(Exception e1){
									logger.error(e1);
								}
							}
						}
						if(!StringUtils.hasText(ndid)){
							ndid = dockerid;
						}
						if(logger.isDebugEnabled()){
							logger.debug(dockerid+"   ----> "+ ndid);
						}
						String counter = k+"/id="+ndid;
						graph2Id.put(endpoint+"_"+counter, c.getId());
						graph2Name.put(endpoint+"_"+counter, n);
						Map<String,String> info = new HashMap<String,String>();
						info.put("endpoint", endpoint);
						info.put("counter", counter);
						counts.add(info);
						c.getLines().put(counter, n);
					}
					last.get(key).add(c);
				}
			}
			else{
				for(GraphAttrVo v : FalconUtil.NODE_QUOTA){
					GraphAttrVo c = new GraphAttrVo();
					String id =(key+"_"+v.getId()).replaceAll("\\.", "_");
					c.setId(id);
					c.setName(v.getName());
					for(Map.Entry<String, String> e : v.getLines().entrySet()){
						String n = e.getValue();
						String k = e.getKey();
						if(k.indexOf("%")>-1){
							//对于网络 和 磁盘  counter是不固定的 
							String k1 = k.substring(0,k.indexOf("%")-1);
							for(String s : cd.get(endpoint)){
								//模糊匹配 查询出来的counter 
								//不能用indexOf 要不会把docker 网卡取出来
								if(s.indexOf(k1)!=0){
									continue ;
								}
								String counter = s;
								graph2Id.put(endpoint+"_"+counter, c.getId());
								//好像网卡 和磁盘 的名称 都是放在最后一个等号的后面
								String rn = s.substring(s.lastIndexOf("=")+1);
								rn = rn+n;
								graph2Name.put(endpoint+"_"+counter, rn);
								Map<String,String> info = new HashMap<String,String>();
								info.put("endpoint", endpoint);
								info.put("counter", counter);
								counts.add(info);
								c.getLines().put(counter, rn);
							}
						}
						else{
							//对于普通的 如CPU 和内存 counter 是固定的
							String counter = k;
							graph2Id.put(endpoint+"_"+counter, c.getId());
							graph2Name.put(endpoint+"_"+counter, n);
							Map<String,String> info = new HashMap<String,String>();
							info.put("endpoint", endpoint);
							info.put("counter", counter);
							counts.add(info);
							c.getLines().put(counter, n);
						}
					}
					last.get(key).add(c);
				}
			}
			//l.get(counter).add(j);
		}
		return new FourTuple<Map<String,List<GraphAttrVo>>, 
				Map<String,String>, Map<String,String>, List<Map<String,String>>>(last, graph2Id, graph2Name, counts);
	}
	
	@RequestMapping("/view")
	public String view(HttpServletRequest request, HttpServletResponse response,@RequestParam String data){
		IUser user = WebUtil.getUserByRequest(request);
		
		String source = request.getParameter("source");
		boolean isNode = false;
		if(StringUtils.hasText(source) && source.equals("node")){
			request.setAttribute("source", "node");
			isNode = true;
		}
		else{
			PaasProject app = loadProjectModel(request); 
		}
		
	
		List<Map> ja = JSONArray.parseArray(data,Map.class);
		if(ja==null){
			request.setAttribute("exception", "参数获取失败");
			return forward(ERROR);
		}
		
		FourTuple<Map<String,List<GraphAttrVo>>, Map<String,String>, 
			Map<String,String>, List<Map<String,String>>> f = obtainPreData(isNode,ja);
		
		request.setAttribute("last", f.a);
		request.setAttribute("graph2Id", JSONObject.toJSONString(f.b));
		request.setAttribute("graph2Name", JSONObject.toJSONString(f.c));
		request.setAttribute("counts", JSONArray.toJSONString(f.d));
		if("true".equals(request.getParameter("isInfo"))){
			return getUrlPrefix() + "/viewinfo";
		}
		return getUrlPrefix() + "/view";
	}
	

	@RequestMapping("/dataForApiApp")
	@ResponseBody
	public Map<String,Object> dataForApiApp(@RequestBody MonitorForm form){
		return data(form);
	}
	
	
	@RequestMapping("/last/data")
	@ResponseBody
	public JsonResponse lastData(MonitorForm form){
//		List<Map<String,String>> endpointCounters = form.getEndpointCounters();
		try{
			String data = FalconUtil.graphLastQuery(form.getEndpointCounters());
			logger.debug(data);
			List<FalconVo> rs = JSONArray.parseArray(data, FalconVo.class);
			return JsonResponse.success(rs);
		}
		catch(Exception e){
			logger.error("获取数据失败", e);
			return JsonResponse.failure(500, "获取最新数据失败");
		}
	}
	
	@RequestMapping("/data")
	@ResponseBody
	public Map<String,Object> data(MonitorForm form){
		Map<String,Object> result = new HashMap<String, Object>();
		try{
			Date d = new Date();
			if(form.getStart()==null){
				form.setStart(d.getTime()-2*60*60*1000);
				form.setStart(form.getStart()/1000);
			}
			
			//默认不用给end  一段一段时间传递到前台 前台每次展示2个小时的数据 如果超过最大结束时间 就展示到最大结束时间的数据
			if(form.getEnd()==null){
				//查看 距离开始时间 2个小时的数据
				long defaultEnd = form.getStart()+2*60*60;
				//最大的结束时间 不能查看当前时间前5分钟数据 时间差
				long maxEnd = d.getTime()-5*60*1000;
				maxEnd =maxEnd/1000;
				if(defaultEnd>maxEnd){
					form.setEnd(maxEnd);
				}
				else{
					form.setEnd(defaultEnd);
				}
			}
			if(logger.isDebugEnabled()){
				logger.debug((new Date(form.getStart()*1000))+"  "+ (new Date(form.getEnd()*1000)));
			}
			if(!StringUtils.hasText(form.getCf())){
				form.setCf("AVERAGE");
			}
			String data = FalconUtil.graphQuery(form.getEndpointCounters(), form.getCf(), form.getStart(),form.getEnd());
			List<HighChartVo> lastData = new ArrayList<HighChartVo>();
			if(StringUtils.hasText(data)){
				List<FalconVo> rs = JSONArray.parseArray(data, FalconVo.class);
				if(rs!=null && rs.size()>0){
					for(FalconVo r : rs){
						String endpoint = r.getEndpoint();
						String counter = r.getCounter();
						HighChartVo v = new HighChartVo();
						lastData.add(v);
						v.setCounter(counter);
						v.setEndpoint(endpoint);
						if(r.getValues()!=null && r.getValues().size()>0){
							for(Map<String, Object> va : r.getValues()){
								Map<String, Object> m = new HashMap<String, Object>();
								m.put("x", Long.parseLong(va.get("timestamp")+"")*1000);
								m.put("y", va.get("value"));
								v.getData().add(m);
							}
							Collections.sort(v.getData(), new Comparator<Map<String, Object>>() {

								@Override
								public int compare(Map<String, Object> o1,
										Map<String, Object> o2) {
									long m1 = Long.parseLong(o1.get("x")+"");
									long m2 = Long.parseLong(o2.get("x")+"");
									return m2-m1<0? 1 : -1;
								}
							});
							
						}
					}
				}
			}
			result.put("start", form.getStart());
			result.put("end", form.getEnd());
			result.put("cf", form.getCf());
			result.put("data", lastData);
			result.put("code", 0);
		}
		catch(Exception e){
			logger.error("获取数据失败", e);
			result.put("code", -1);
			result.put("info", StringUtils.hasText(e.getMessage())?e.getMessage():"查询数据失败");
		}
		return result;
	}
	
	@RequestMapping("/counters")
	@ResponseBody
	public Map<String,Object> counters(@RequestParam String endpoints){
		Map<String,Object> result = new HashMap<String, Object>();
		try{
			String[] endpoint = endpoints.split(",");
			CountDownLatch dl = new CountDownLatch(endpoint.length);
			List<Exception> errors = Collections.synchronizedList(new ArrayList<Exception>());
			
			Map<String,List<String>> data = Collections.synchronizedMap(new HashMap<String,List<String>>());
			//可以一起发请求  但是一起发的请求 数据 没有 ip和counter关联关系
			for(int i=0;i<endpoint.length;i++){
				new Thread(new CountRunnable(endpoint[i],errors,data,dl)).start();
			}
			//等待10秒应该行把
			dl.await(10, TimeUnit.SECONDS);
			if(errors.size()>0){
				//只要有一个counter获取失败就失败
				throw new RuntimeException("获取counter失败",errors.get(0));
			}
			
			Map<String,List<String>> dataResult = new HashMap<String, List<String>>();
			for(Map.Entry<String, List<String>> k : data.entrySet()){
				String ip = k.getKey();
				if(!dataResult.containsKey(ip)){
					dataResult.put(ip, new ArrayList<String>());
				}
				for(String o : k.getValue()){
					JSONObject ja = JSONObject.parseObject(o);
					if(!ja.getBooleanValue("ok")){
						continue ;
					}
					JSONArray ja1 = ja.getJSONArray("data");
					for(int j=0;j<ja1.size();j++){
						JSONArray info = ja1.getJSONArray(j);
						String counter = info.getString(0);
						dataResult.get(ip).add(counter);
					}
				}
			}
			result.put("code", 0);
			result.put("data", dataResult);
		}
		catch(Exception e){
			logger.error("获取数据失败", e);
			result.put("code", -1);
			result.put("info", StringUtils.hasText(e.getMessage())?e.getMessage():"查询数据失败");
		}
		return result;
	}
	
	class CountRunnable implements Runnable{
		private String ip;
		private List<Exception> errors;
		private Map<String,List<String>> result;
		private CountDownLatch dl;
		public CountRunnable(String ip,List<Exception> errors,Map<String,List<String>> result,CountDownLatch dl){
			this.ip = ip;
			this.errors = errors;
			this.result = result;
			this.dl = dl;
		}
		@Override
		public void run() {
			try{
				String url = ConfigUtil.getConfigString("paasos.monitor.query");
				if(!url.endsWith("/")){
					url = url+"/";
				}
				url = url+"api/counters";
				for(String q : FalconUtil.COUNT_FILE){
					Map<String,Object> m = new HashMap<String, Object>();
					m.put("endpoints", "[\""+this.ip+"\"]");
					m.put("limit", 1500);
					m.put("q", q);
					String msg = PaasAPIUtil.post("falcon", url, DataType.FORM, m);
					if(!result.containsKey(this.ip)){
						result.put(this.ip, new ArrayList<String>());
					}
					result.get(this.ip).add(msg);
				}
//				result.put(this.ip, msg);
			}
			catch(Exception e){
				e.printStackTrace();
				errors.add(e);
			}
			finally{
				dl.countDown();
			}
		}
		
	}

}

