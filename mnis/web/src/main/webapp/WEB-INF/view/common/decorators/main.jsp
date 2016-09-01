<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">   
		
<title><decorator:title default="数字小护士" /></title> 

<link rel="shortcut icon" HREF="${pageContext.request.contextPath}/resources/img/favicon.ico" /> 
<link id="themeStyles" rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/easyui/themes/anyi/easyui.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/js/easyui/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/aphrodite/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/public.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/adapter.css" />
<script language="javascript">
var ay = ay || {};
ay.contextPath = "${pageContext.request.contextPath}";
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.8.3.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/underscore.js"></script> 
<!-- import easyui framework. -->
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/easyuiUtils.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/css/aphrodite/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/common.js" charset="utf-8"></script>
<!-- import data picker control. -->
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/DatePicker/WdatePicker.js" charset="utf-8"></script>
<decorator:head />
</head>
<%--oncontextmenu="return false"--%>
<body <decorator:getProperty property="body.id" writeEntireProperty="true" /> <decorator:getProperty property="body.class" writeEntireProperty="true" /> <decorator:getProperty property="body.style" writeEntireProperty="true" />>
<decorator:body />
</body>
</html>
