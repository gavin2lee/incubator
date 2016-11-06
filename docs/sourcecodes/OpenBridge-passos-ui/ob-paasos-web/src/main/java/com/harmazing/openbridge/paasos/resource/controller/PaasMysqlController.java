package com.harmazing.openbridge.paasos.resource.controller;

import static com.harmazing.openbridge.web.fileupload.FileUploadHandler.getStoreDir;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.ExceptionUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.util.StorageUtil;
import com.harmazing.openbridge.paasos.resource.model.PaasMysql;
import com.harmazing.openbridge.paasos.resource.service.IPaasMysqlService;
import com.harmazing.openbridge.paasos.store.model.PaasStoreApp;
import com.harmazing.openbridge.paasos.store.service.IPaasStoreAppService;
import com.harmazing.openbridge.paasos.utils.ConfigUtils;

@Controller
@RequestMapping("/resource/mysql")
public class PaasMysqlController extends PaasResourceBaseController {
	protected static final Log logger = LogFactory
			.getLog(PaasMysqlController.class);
	
	@Autowired
	private IPaasMysqlService mysqlService;
	@Autowired
	private IPaasStoreAppService paasStoreAppService;
	
	@RequestMapping("/list")//首页的列表
	public String list(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			Map<String, Object> params = new HashMap<String, Object>();
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
			String listType = request.getParameter("listType");
			params.put("pageNo", pageNo);
			params.put("pageSize", pageSize);
			/*if(!user.isAdministrator()){
				params.put("userId", user.getUserId());
			}*/
			if(listType != null && listType.equals("tenant")){
				params.put("tenantId", user.getTenantId());
			}else{ 
				params.put("userId", user.getUserId());
				params.put("tenantId", user.getTenantId());
			}
			List<PaasMysql> pageData = mysqlService.queryPaasMysqlsByParams(params);
			request.setAttribute("pageData", pageData);
			String phpMyAdmin = ConfigUtil.getConfigString("paasos.phpMyAdminUrl");
			String mysqlAdminUrlBase = ConfigUtil.getConfigString("paasos.resRestfulUrl");
			if(StringUtil.isNotNull(phpMyAdmin)){
				request.setAttribute("phpMyAdmin", phpMyAdmin);
				request.setAttribute("mysqlAdminUrlBase", mysqlAdminUrlBase);
			}
			return getUrlPrefix()+"/list";
		}catch(Exception e){
			request.setAttribute("exception", e);
			logger.error("获取数据库列表信息出错",e);
			return forward(ERROR);
		}
	}
	
	@Override
	protected void setResourceLimit2Request(HttpServletRequest request){
		String userId = (String) request.getAttribute("userId");
		String tenantId = (String)request.getAttribute("tenantId");
		JSONObject MsqlOptions = mysqlService.getPaasMysqlOptions(userId, tenantId);
		if(MsqlOptions!=null){
			request.setAttribute("options", MsqlOptions);
		}
	}
	
	@RequestMapping("/options")//实时rest请求查询mysql配额信息
	@ResponseBody
	public JsonResponse queryMysqlOptions(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			JSONObject MsqlOptions = mysqlService.getPaasMysqlOptions(userId, tenantId);
			return JsonResponse.success(MsqlOptions);
		}catch(Exception e){
			logger.error("查询mysql配额信息失败"+e.getMessage(),e);
			return JsonResponse.failure(500, "查询mysql配额信息失败");
		}
	}
	
	@RequestMapping("/queryMysqlStatusInfo")//实时rest请求查询mysql实例的运行状态
	@ResponseBody
	public JsonResponse queryMysqlInfo(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String mysqlId = request.getParameter("resId");
			String paasOsUserId = user.getUserId();
			String paasOsTenantId = user.getTenantId();
			if(StringUtil.isNull(mysqlId)|| StringUtil.isNull(paasOsUserId)|| StringUtil.isNull(paasOsTenantId)){
				return JsonResponse.failure(403, "查询参数不正确");
			}
			JSONObject MySQLObj = mysqlService.queryMysqlInfo(mysqlId, paasOsUserId, paasOsTenantId);
			String status = MySQLObj.getString("status");
			switch(status){
			case "creating":
				status = "创建中";
				break;
			case "starting":
				status = "启动中";
				break;
			case "running":
				status = "运行中";
				break;
			case "stopped":
				status = "已停止";
				break;
			default:
				status ="";
			}
			JSONObject returnObj = new JSONObject();
			returnObj.put("status", status);
			if(MySQLObj.containsKey("podInfo")){
				if(MySQLObj.getJSONObject("podInfo")!=null){
					returnObj.put("podName", MySQLObj.getJSONObject("podInfo").get("podName"));
					returnObj.put("namespace", MySQLObj.getJSONObject("podInfo").get("namespace"));
				}
			}
			return JsonResponse.success(returnObj);
		}catch(Exception e){
			logger.error("查询mysql运行状态失败"+e.getMessage());
			return JsonResponse.failure(500, "查询mysql运行状态失败");
		}
	}
	
	@RequestMapping("/save")//保存数据库实例的操作
	@ResponseBody
	public JsonResponse addMysqlSave(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			String instanceName = request.getParameter("instanceName");
			String version = request.getParameter("version");
			String type = request.getParameter("type");
//			String TypeAttach = request.getParameter("TypeAttach");
			String memory = request.getParameter("memory");
			String storage = request.getParameter("storage");
			String projectId = request.getParameter("projectId");
			String envId = request.getParameter("envId");
			String envType = request.getParameter("envType");
			String desc = request.getParameter("resDesc");
			String applyType = request.getParameter("applyType");
			if(StringUtil.isNull(applyType) || !"api".equals(applyType)&&!"app".equals(applyType)){
				applyType="paasOS";
			}
			if(StringUtil.isNull(instanceName)|| StringUtil.isNull(tenantId)){
				return JsonResponse.failure(400, "新建mysql资源失败,实例名和租户不能为空");
			}
			JSONObject MysqlConfig = new JSONObject();
			PaasMysql mysql = new PaasMysql();
			MysqlConfig.put("name", instanceName);
			MysqlConfig.put("memory", memory);
			
			mysql.setMemory(StorageUtil.getMemory(memory));
			mysql.setStorage(StorageUtil.getMemory(storage));
			
			MysqlConfig.put("storage", storage);
			MysqlConfig.put("version", version);
			MysqlConfig.put("type", type);
			JSONObject attributes = new JSONObject();
			attributes.put("envId", envId);
			attributes.put("projectId", projectId);
			attributes.put("envType", envType);
			
			MysqlConfig.put("attributes", attributes);
			
			mysql.setMysqlId(StringUtil.getUUID());
			mysql.setInstanceName(instanceName);
			mysql.setMysqlType(type);
			mysql.setApplyContent(MysqlConfig.toJSONString());
			mysql.setCreater(userId);
			Date now = new Date();
			mysql.setCreateDate(now);
			mysql.setUpdateDate(now);
			mysql.setAllocateContent("");
			mysql.setTenantId(tenantId);
			mysql.setProjectId(projectId);
			mysql.setEnvId(envId);
			mysql.setEnvType(envType);
			mysql.setOnReady(false);
			mysql.setResDesc(desc);
			mysql.setApplyType(applyType);
			mysqlService.addPaasMysqlResource(mysql);
			logger.debug("成功添加mysql实例，"+instanceName);
			return JsonResponse.success(mysql.getMysqlId());
		}catch(Exception e){
			logger.error("添加mysql实例失败"+e.getMessage());
			return JsonResponse.failure(500, "新建mysql资源失败"+e.getMessage());
		}
	}
	
	@RequestMapping("/delete")//删除数据库的操作
	@ResponseBody
	public JsonResponse deleteMysql(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String mysqlId = request.getParameter("resId");
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			if(StringUtil.isNull(mysqlId)||StringUtil.isNull(tenantId)||StringUtil.isNull(userId)){
				logger.error("删除mysql资源出错，mysqlId/userId/tenantId参数为空");
				return JsonResponse.failure(500, "删除mysql资源失败,mysqlId/userId/tenantId参数为空");
			}
			JSONObject res = mysqlService.deletePaasMysqlById(mysqlId,userId, tenantId);
			if(res.containsKey("result")|| res.getString("result").equals("success")){
				logger.debug("删除mysql资源"+mysqlId);
				return JsonResponse.success(mysqlId);
			}else{
				logger.error("删除mysql资源操作失败"+mysqlId);
				return JsonResponse.failure(500, "删除mysql资源失败");
			}
		}catch(Exception e){
			logger.error("删除mysql资源操作失败"+e.getMessage());
			return JsonResponse.failure(500, "删除mysql资源失败"+e.getMessage());
		}
	}
	
	@RequestMapping("/info")//数据库的详情页面
	public String MysqlInfo(HttpServletRequest request, HttpServletResponse response){
		String mysqlId = request.getParameter("mysqlId");
		PaasMysql mysqlInfo = mysqlService.getPaasMysqlInfoById(mysqlId);
		request.setAttribute("mysqlInfo", mysqlInfo);
		ConfigUtils.setEnvType2Request(request);
		return getUrlPrefix()+"/info";
	}
	
	@RequestMapping("/getMysqlInfo")//数据库的详情页面
	@ResponseBody
	public PaasMysql getMysqlInfo(HttpServletRequest request, HttpServletResponse response){
		String mysqlId = request.getParameter("mysqlId");
		PaasMysql mysqlInfo = mysqlService.getPaasMysqlInfoById(mysqlId);
		return mysqlInfo;
	}
	
	@RequestMapping("/startMysql")
	@ResponseBody
	public JsonResponse startPassMysql(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String mysqlId = request.getParameter("resId");
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			if(StringUtil.isNull(mysqlId)){
				logger.error("启动mysql出错，resId参数为空");
				return JsonResponse.failure(500, "启动mysql失败,resId参数为空");
			}
			JSONObject res = mysqlService.startPaasMysql(mysqlId,userId, tenantId);
			if(res.containsKey("result")&& res.getString("result").equals("success")){
				logger.debug("成功启动mysql--"+mysqlId);
				return JsonResponse.success();
			}else{
				logger.error("启动mysql失败"+mysqlId);
				return JsonResponse.failure(500, "启动mysql失败");
			}
		}catch(Exception e){
			logger.error("启动mysql失败"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/stopMysql")
	@ResponseBody
	public JsonResponse stopPassMysql(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String mysqlId = request.getParameter("resId");
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			if(StringUtil.isNull(mysqlId)){
				logger.error("停止mysql出错，resId参数为空");
				return JsonResponse.failure(500, "停止mysql失败,resId参数为空");
			}
			JSONObject stopRes = mysqlService.stopPaasMysql(mysqlId,userId, tenantId);
			if(stopRes.containsKey("result")&& stopRes.getString("result").equals("success")){
				logger.debug("成功停止mysql--"+mysqlId);
				return JsonResponse.success();
			}else{
				logger.error("停止mysql失败"+mysqlId);
				return JsonResponse.failure(500, "停止mysql失败");
			}
		}catch(Exception e){
			logger.error("停止mysql失败"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/import")//导入数据库的弹出页面
	public String executeDialog(HttpServletRequest request, HttpServletResponse response){
		String mysqlId = request.getParameter("mysqlId");
		PaasMysql mysqlInfo = mysqlService.getPaasMysqlInfoById(mysqlId);
		request.setAttribute("mysqlInfo", mysqlInfo);
		return getUrlPrefix()+"/import";
	}
	
	@RequestMapping("/update")//更改数据库的页面
	public String updateMysql(HttpServletRequest request, HttpServletResponse response){
		String mysqlId = request.getParameter("mysqlId");
		PaasMysql mysqlInfo = mysqlService.getPaasMysqlInfoById(mysqlId);
		request.setAttribute("mysqlInfo", mysqlInfo);
		setUserInfo2Request(request);
		ConfigUtils.setEnvType2Request(request);
		setResourceLimit2Request(request);
		return getUrlPrefix()+"/update";
		
	}
	
	@RequestMapping("/saveUpdate")//更改数据库的保存逻辑
	@ResponseBody
	public JsonResponse updateMysqlSave(HttpServletRequest request, HttpServletResponse response){
		try{
			String mysqlId = request.getParameter("mysqlId");
			String userId = request.getParameter("userId");
			String instanceName = request.getParameter("instanceName");
			String version = request.getParameter("version");
			String type = request.getParameter("type");
//			String TypeAttach = request.getParameter("TypeAttach");
			String tenantId = request.getParameter("tenantId");
			String memory = request.getParameter("memory");
			String storage = request.getParameter("storage");
			String projectId = request.getParameter("projectId");
			String envId = request.getParameter("envId");
			String envType = request.getParameter("envType");
			String desc = request.getParameter("resDesc");
			if(StringUtil.isNull(instanceName)|| StringUtil.isNull(tenantId)){
				return JsonResponse.failure(400, "修改mysql资源失败,实例名和租户不能为空");
			}
			JSONObject MysqlConfig = new JSONObject();
			MysqlConfig.put("name", instanceName);
			MysqlConfig.put("memory", memory);
			MysqlConfig.put("storage", storage);
			MysqlConfig.put("version", version);
			MysqlConfig.put("type", type);
			JSONObject attributes = new JSONObject();
			attributes.put("envId", envId);
			attributes.put("projectId", projectId);
			attributes.put("envType", envType);
			MysqlConfig.put("attributes", attributes);
			PaasMysql mysql = new PaasMysql();
			mysql.setMysqlId(mysqlId);
			mysql.setInstanceName(instanceName);
			mysql.setMysqlType(type);
			mysql.setApplyContent(MysqlConfig.toJSONString());
			Date now = new Date();
			mysql.setUpdateDate(now);
			mysql.setTenantId(tenantId);
			mysql.setEnvType(envType);
			mysql.setOnReady(true);
			mysql.setResDesc(desc);
			mysqlService.updatePaasMysqlInfo(mysql);
			logger.debug("成功修改mysql实例，"+instanceName);
			return JsonResponse.success();
		}catch(Exception e){
			logger.error("修改mysql失败"+e.getMessage());
			return JsonResponse.failure(500, "修改mysql配置失败");
		}
		
	}
	
	@RequestMapping("getSchemeta")
	@ResponseBody
	public List<String> getSchemeta(String url, String username, String password) {
		Assert.hasText(url);
		Assert.hasText(username);
		Assert.hasText(password);
		
		List<String> list = new ArrayList<String>();
		// String sql="SELECT SCHEMA_NAME FROM information_schema.SCHEMATA";
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username,
					password);
			// Statement st=(Statement) conn.createStatement();
			DatabaseMetaData dmd = (DatabaseMetaData) conn.getMetaData();
			ResultSet rs = dmd.getCatalogs();
			while (rs.next()) {
				list.add(rs.getString("TABLE_CAT"));
			}
		} catch (SQLException e) {
			
		} catch (ClassNotFoundException e) {
			
		} finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("connection close failed.("+url+")",e);
				}
			}
		}
		return list;
	}
	
	@RequestMapping("executeSql")
	@ResponseBody
	public JsonResponse executeSql(String storeId,String number, String url, String username, String password) {
		PaasStoreApp paasStoreApp = paasStoreAppService.getById(storeId);
		if(paasStoreApp!=null && StringUtil.isNotNull(paasStoreApp.getFilePath())){
			Map<String, String> map = paasStoreApp.getConfigFileByNO(number);
			if(map!=null){
				String filePath = map.get("filePath");
				if(!filePath.startsWith(getStoreDir())){
					filePath = getStoreDir()+filePath;
				}
				File file = new File(filePath);
				if(file.exists()){
					try {
						SQLExec sqlExec = new SQLExec();
						// 设置数据库参数
						sqlExec.setDriver("com.mysql.jdbc.Driver");
						sqlExec.setUrl(url);
						sqlExec.setUserid(username);
						sqlExec.setPassword(password);
						sqlExec.setSrc(file);
						sqlExec.setPrint(true); // 设置是否输出
						// 输出到文件 sql.out 中；不设置该属性，默认输出到控制台
//					sqlExec.setOutput(new File("d:/script/sql.out"));
						sqlExec.setProject(new Project());	// 要指定这个属性，不然会出错
						sqlExec.execute();
					} catch (Exception e) {
						return JsonResponse.failure(500, ExceptionUtil.getExceptionString(e));
					}
				}
			}
		}
		return JsonResponse.success();
	}
	@RequestMapping("createDatabase")
	@ResponseBody
	public JsonResponse createDatabase(String databaseName, String url, String username, String password) {
		Assert.hasText(databaseName);
		Assert.hasText(url);
		Assert.hasText(username);
		Assert.hasText(password);
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username,
					password);
			PreparedStatement st = conn.prepareStatement("create database "+databaseName+" DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci");
//			st.setString(1, databaseName);
			st.executeUpdate();
		} catch (SQLException e) {
			return JsonResponse.failure(500, e.getMessage());
		} catch (ClassNotFoundException e) {
			return JsonResponse.failure(500, e.getMessage());
		} finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("connection close failed.("+url+")",e);
				}
			}
		}
		return JsonResponse.success();
	}
	
	@RequestMapping("/getConfig")
	@ResponseBody
	public JsonResponse getMysqlConfig(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			String resId = request.getParameter("resId");
			logger.debug("resId = "+ resId);
			return JsonResponse.success(mysqlService.getPaasMysqlConfig(resId));
		}catch(Exception e){
			logger.error("获取mysql配置出错---getConfig--"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getInstances")
	@ResponseBody
	public JsonResponse getMysqlInstance(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("start", 0);
			params.put("size", 999999);
			//params.put("userId", user.getUserId());
			params.put("tenantId", user.getTenantId());
			StringBuilder sb = new StringBuilder();
			JSONObject mysqls = mysqlService.queryPaasMysqlWithStatus(params,sb);
			if(logger.isDebugEnabled()){
				logger.debug("统计mysql实例及运行状态"+sb.toString());
			}
			return JsonResponse.success(mysqls);
		}catch(Exception e){
			logger.error("统计mysql实例及运行状态出错--"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}