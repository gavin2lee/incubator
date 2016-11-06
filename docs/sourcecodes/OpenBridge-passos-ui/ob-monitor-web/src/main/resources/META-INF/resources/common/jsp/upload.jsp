<%@ page language="java" pageEncoding="UTF-8"%><%@ include file="/common/include/taglibs.jsp"%><%
if(request.getAttribute("WEB_APP_PATH")==null){
	request.setAttribute("WEB_APP_PATH", request.getContextPath());
}%><!doctype html>
<html>
<head>
<link rel="stylesheet" href="${ WEB_APP_PATH }/assets/css/layout.css"/> 
<link rel="stylesheet" href="${ WEB_APP_PATH }/assets/css/main.css"/>
<link rel="stylesheet" href="${ WEB_APP_PATH }/assets/css/app_deployment.css"/>
<script type="text/javascript" src="${ WEB_APP_PATH }/assets/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="${ WEB_APP_PATH }/assets/js/layer.js"></script>
<script type="text/javascript" src="${ WEB_APP_PATH }/assets/js/common.js"></script>
<script type="text/javascript" src="${ WEB_APP_PATH }/assets/js/util.js"></script>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta charset="utf-8" />
<style type="text/css">
	html,body {
		margin: 0px;
		height: 100%;
	}
</style>
</head>
<body>
	<%
	String accept = "";
	if(request.getParameter("accept")!=null&&request.getParameter("accept").trim().length()>0){
		accept = "accept='"+request.getParameter("accept").trim()+"'";
	}
	%>
	<form action="${WEB_APP_PATH}/file/upload.do" method="post" enctype="multipart/form-data">
		<input type="file" id="fuMain" name="upfile" <%=accept%> multiple="${ param['multiple'] eq 'true' ? 'true' : 'false'  }" onchange="onUserSelectFile()" style="position: absolute;width: 100%;height: 100%;opacity:0;cursor: pointer;">
	</form>
	<script type="text/javascript">
	
	function dataValidation(){
		var obj=document.getElementById('fuMain'); 
		var pos = obj.value.match(/^(.*)(\.)(.{1,8})$/)[3]; 
		 if ((pos.toLowerCase() != "gif" )&&(pos.toLowerCase() != "jpg")&&(pos.toLowerCase() != "png")&&(pos.toLowerCase() != "jpeg"))
			 {
			 top.common.alert("上传文件必须是图片");
	    		return false;
			 }
		 return true;
	}
		function onUserSelectFile(){
			if(dataValidation()){
			frameElement.onStartUpload();
			document.forms[0].submit();
			}
		}
	</script>
</body>
</html>