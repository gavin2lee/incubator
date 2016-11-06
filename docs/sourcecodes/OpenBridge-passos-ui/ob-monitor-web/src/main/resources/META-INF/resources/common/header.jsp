<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/include/taglibs.jsp"%>
<%
if(request.getAttribute("WEB_APP_PATH")==null){
	request.setAttribute("WEB_APP_PATH", request.getContextPath());
}%>