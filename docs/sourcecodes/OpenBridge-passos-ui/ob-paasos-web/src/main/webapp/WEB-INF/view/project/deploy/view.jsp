<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<c:if test="${empty param.dialog || !param.dialog }">

	<c:set var="isDialog" value="false"/>
<template:include file="../base.jsp"  nav-left="deploy">
	<template:replace name="title">
		项目概述
	</template:replace>
	<template:replace name="content-body">
			
	     <div class="app_name">
	     	
            <a href="${WEB_APP_PATH }"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="${WEB_APP_PATH }/project/index.do">项目管理</a></p>
            <em>&gt;</em>
			
            <p class="app_a">
            	<a href="${WEB_APP_PATH }/project/overview/index.do?projectId=${param.projectId}">${project.projectName }&nbsp;</a>
            </p>
            <em>&gt;</em>
            <p class="app_a">
            	<a href="${WEB_APP_PATH }/project/env/index.do?projectId=${param.projectId}">项目部署</a>
            </p>
            <em>&gt;</em>

            <p class="app_a">查看项目部署</p>
        </div>
        
        <div class="plate">
        	<form id="deploy_area">
            <div class="project_r">
                <div class="r_block p20">
                    <div class="r_block">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line"><i class="icons add_ico_blue mr5"></i>查看项目部署</h3>
							<div class="title_line"></div>
							<div style="position: absolute;right: 30px;">
	                        	<span class="zt_span">
	                            	<c:if test="${paasProjectDeploy.status == 1 || paasProjectDeploy.status == 0 }">
			                    		<a attrid="${paasProjectDeploy.deployId }" href="javascript:void(0);" class="deploy-button btn btn-yellow btn_sm f12  currentpage">启动</a>
			                    	</c:if>
			                    	<c:if test="${paasProjectDeploy.status == 10}">
			                    		<a attrid="${paasProjectDeploy.deployId }" href="javascript:void(0);" class="replicas-button btn btn-yellow btn_sm f12">扩容</a>
			                    	</c:if>
			                    	<c:if test="${paasProjectDeploy.status == 1 || paasProjectDeploy.status == 0 }">
			                    		<a attrid="${paasProjectDeploy.deployId }" href="javascript:void(0);" class="modify-button btn btn-yellow btn_sm f12 currentpage">修改</a>
			                    	</c:if>
			                    	<c:if test="${paasProjectDeploy.status == 10 || paasProjectDeploy.status == 11 }">
			                    		<a attrid="${paasProjectDeploy.deployId }" href="javascript:void(0);" class="stop-button btn btn-yellow btn_sm f12 currentpage">停止</a>
			                    	</c:if>
			                    	<c:if test="${paasProjectDeploy.status != 5 && paasProjectDeploy.status != 6 && paasProjectDeploy.status != 7}">
			                    		<a attrid="${paasProjectDeploy.deployId }" href="javascript:void(0);" class="delete-button btn btn-yellow btn_sm f12 currentpage">删除</a>
			                    	</c:if>
	                            </span>
	                        </div>
                        </div>
                        
	                    <%@include file="viewContent.jsp" %> 
                    </div>
                </div>
                <!--创建应用部署结束-->
            </div>
			</form>
        </div>
        
       
	</template:replace>
</template:include>

</c:if>

<c:if test="${not empty param.dialog && param.dialog }">
	<c:set var="isDialog" value="true"/>
	<%@include file="viewContent.jsp" %> 
</c:if>