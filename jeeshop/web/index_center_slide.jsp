<%@page import="net.jeeshop.core.front.SystemManager"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page import="net.jeeshop.services.front.news.bean.News"%>
<%@page import="net.jeeshop.core.ManageContainer"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<link href="<%=request.getContextPath() %>/resource/js/slideTab2/css/lanrenzhijia.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/slideTab2/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/slideTab2/js/lanrenzhijia.js"></script>
<style type="text/css">
.lazy {
  display: none;
}
</style>
</head>

<body>
<%application.setAttribute("indexImages", SystemManager.indexImages);%>
<!-- 首页中间位置图片轮播 -->
<div class="zhuanti_box">
  <div id="slideBox">
    <div class="J_slide" style="height: 100%;">
      <div class="J_slide_clip">
        <ul class="J_slide_list">
        	<s:iterator value="#application.indexImages" status="i" var="row"><!-- height="180" width="100%" -->
		          <li class="J_slide_item">
		          <s:if test="link!=null">
			          <a href="<s:property escape="false" value="link"/>" target="_blank">
			          	<img style="max-width: 100%;" 
			          	src="<%=SystemManager.systemSetting.getImageRootPath()%><s:property escape="false" value="picture"/>" >
			          </a>
		          </s:if>
		          <s:else>
		          		<img style="max-width: 100%;" 
			          	src="<%=SystemManager.systemSetting.getImageRootPath()%><s:property escape="false" value="picture"/>" >
		          </s:else>
		          </li>
			</s:iterator>
        </ul>
      </div>
      <ul class="J_slide_trigger">
      	<s:iterator value="#application.indexImages" status="i" var="row">
	        <li class="">
	        	<a href="javascript:" title="<s:property value="title" escape="false"/>">
	        		<s:property value="title" escape="false"/>
	        	</a>
	        </li>
      	</s:iterator>
      </ul>
    </div>
  </div>
</div>
  <script type="text/javascript">
   new Tab('.J_tab',{auto:false});
   new Slide('#slideBox',{index: 1 ,effect:'slide', firstDelay:8});
  </script>
</body>
</html>
