<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", -10);
%>
<template:include file="../../base.jsp" nav-left="envdeploy">
	<template:replace name="title">
		部署列表
	</template:replace>
	<template:replace name="content-body">
		<div class="app_name">
			<a href="${WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

			<p class="app_a">
				<a href="../tenant/list.do">环境管理</a>
			</p>
			<em>&gt;</em>

			<p class="app_a">
				<a href="../deploy/env/list.do">环境部署</a>
			</p>
		</div>
		<div class="plate">
			<div class="project_r">
				<div class="r_block p20">
					<div class="r_title">
						<h3 class="f14 h3_color h3_btm_line blue mr10">
							<a href="#"> <i class="icons cy_ico mr5"></i>部署列表
							</a>
						</h3>
						<div class="title_line"></div>
					</div>
					<div class="r_con p10_0">
						<div class="ser_bar mt10 mb10">
							
						</div>
						<div class="r_con p10_0" id="list_view">
							
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<script type="text/javascript" src="${WEB_APP_PATH}/assets/js/manager/deploy/env/envresource.js"></script>
    <script>
      $(function(){
    	  EnvDeploy.init({
						'webapp' : '${WEB_APP_PATH}',
						'id' : 'list_view'
					});
				});
   </script>
	</template:replace>
</template:include>