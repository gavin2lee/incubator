<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.harmazing.framework.authorization.IHttpUserManager"%>
<%@page import="com.harmazing.framework.util.SpringUtil"%> 
<%
	String path = request.getContextPath(); 

	IHttpUserManager httpUserManager = (IHttpUserManager)SpringUtil.getBean("httpUserManager");
	httpUserManager.userLogout(request, response);
	

	request.setAttribute("targetUrl", "");
	request.setAttribute("WEB_APP_PATH", path);
%>
<script>
	location.href = "${WEB_APP_PATH}/";
</script>
