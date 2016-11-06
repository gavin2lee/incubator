<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.harmazing.framework.util.WebUtil"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp" nav-header="project">
	<template:replace name="title">
		项目管理
	</template:replace>
	<template:replace name="content">
		<style>
			.content_frame .r_title .noactive{
				border-bottom: 0px;
			}
		</style>
	        <div class="plate">
	            <div class="project_r">
	            <c:set var="web_username" value="<%=WebUtil.REQUESTUSERKEY%>"></c:set>
				<c:if test="${ requestScope[web_username] != null && requestScope[web_username].tenantId != null  }">
	                <div class="pas_info mb10 p20" style="background:#f0f0f0;">
	                    <a href="${WEB_APP_PATH}/project/create.do" class="btn btn-default btn-oranger f16 mr20"><em>+</em> 新建项目 </a>
	                    <em class="mr10 f14">|</em>
	                    <span class="f14">项目是 PAASOS 管理单元，一个应用程序的：镜像构建、部署、运营监控 都是以项目为维度来管理。</span>
	                </div></c:if>
	                <!--项目概述开始-->
	                <div class="r_block p20">
	                    <div class="r_title">
	                        <h3 class="f14 h3_color h3_btm_line  ${param['tab'] == 'tenant' ? ' gray noactive' : ' blue '}">
	                            <a href="${param['tab'] == 'tenant' ? (requestScope.WEB_APP_PATH.concat('/project/index.do')) : 'javascript:void(0);'}">
	                                <i class="icons cy_ico mr5"></i>我的项目
	                            </a>
	                        </h3>
	                        <h3 class="f14 h3_color h3_btm_line   ${param['tab'] == 'tenant' ? ' blue ' : ' gray noactive'}">
	                            <a href="${param['tab'] == 'tenant' ? 'javascript:void(0);' : (requestScope.WEB_APP_PATH.concat('/project/index.do?tab=tenant')) }">
	                                <i class="icons cy_ico mr5"></i>组织的项目
	                            </a>
	                        </h3>
	                        <div class="title_line"></div>
	                    </div>
						<div class="ser_bar mt10">
							<span style="margin-left:15px">
								<lable>项目来源：</lable>
								<select name="projectType" id="projectType">
									<option value="">--不限--</option>
									<option value="app">APPFactory</option>
									<option value="api">APIManager</option>
									<option value="store">预置应用</option>
								</select>
							</span> 
							<!-- <span style="margin-left:15px">
								<lable>项目名称：</lable>
								<input name="projectName" id="projectName"  style="width: 170px" type="text" placeholder="搜索.." onkeypress="changeKeyword(event)">
							</span> -->
							<span style="margin-left:15px">
								<lable>关键字：</lable>
								<input name="projectCode" id="keyword" style="width: 170px" type="text" placeholder="搜索.." onkeypress="changeKeyword(event)">
								<a class="btn btn-sm btn-yellow2" href="javascript:loadMyApp();">查询</a>
								<a class="btn btn-sm btn-yellow2" href="javascript:resetData();">重置</a>
							</span>
						</div>
					<div class="r_con p10_0"  id="list_view">
	                        
	                    </div>
	                </div>
	                <!--项目概述结束-->
	            </div>
			</div>
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		
		
 		
 		<script>
 			function loadMyApp(pageNo,pageSize){
		    	var params = {};
		    	params.viewType = "table";
		    	params.listType = "${param['tab'] == 'tenant' ? 'tenant' : 'notenant'}";
		    	params.keyword = $("#keyword").val();
		    	params.projectType = $("#projectType").val();
//		    	params.projectName = $("#projectName").val();
//		    	params.projectCode = $("#projectCode").val();
		    	var url = "${WEB_APP_PATH}/project/list.do?sjs="+(new Date().getTime());
		    	if(pageNo!=null){
		    		url+="&pageNo="+pageNo;
		    	}
		    	if(pageSize!=null){
		    		url+="&pageSize="+pageSize;
		    	}
		  //  	common.ajaxLoading($("#jiraTableWrapper"));
		    	$("#list_view").load(url,params);
		    }
 			$(function(){
 				loadMyApp();
 			});
 			
 			function changeKeyword(evt){
 				evt = evt | event;
		    	if(event.keyCode==13) {
		    		loadMyApp();
		    	}
 			}
 			
 			function resetData(){
 				$("#projectType").val("");
 				$("#projectName").val("");
 				$("#projectCode").val("");
 				window.location.reload();
 			}
 		</script>
	</template:replace>
</template:include>