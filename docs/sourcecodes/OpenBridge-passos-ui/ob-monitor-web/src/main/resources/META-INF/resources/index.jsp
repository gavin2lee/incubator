<%@page import="com.harmazing.framework.authorization.IUser" %>
<%@page import="com.harmazing.framework.util.WebUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<% response.addHeader("login", "true"); %>
<%
	IUser user = WebUtil.getUserByRequest(request);
	String path = request.getContextPath();
	if(request.getParameter("targetUrl")!=null){
		request.setAttribute("redirectUrl", request.getParameter("targetUrl"));
	} else {
		request.setAttribute("redirectUrl", path + "/portal/home");
	}
%>

<script>
	location.href = "${redirectUrl}";
</script>
