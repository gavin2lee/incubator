<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="deploy">
	<template:replace name="title">
		项目部署
	</template:replace>
	<template:replace name="content-body"> 
		<div class="app_name">
            <a href="${ WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="${ WEB_APP_PATH }/project/index.do">项目管理</a></p>
            <em>&gt;</em>

            <p class="app_a">
            	<a href="${ WEB_APP_PATH }/project/overview/index.do?projectId=${project.projectId}">
            	${project.projectName }&nbsp;
            	</a>
            </p>
            <em>&gt;</em>

            <p class="app_a">项目部署</p>
        </div>
        <div class="plate">
        	<!--  tab -->
	        <div class="tab_title">
	             <div class="title_tab title_tab_item">
	                 <ul>
	                     <li>
	                         <h5 class="f14">
	                             <a class="${ envType eq 'test' ? 'active' : ''}" href="${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envType=test"><i class="tab_ico tab_ico07 mr5"></i>开发测试环境</a>
	                         </h5>
	                     </li>
	                     <li>
	                         <h5 class="f14">
	                             <a class="${ envType eq 'live' ? 'active' : ''}" href="${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envType=live"><i class="tab_ico tab_ico06 mr5"></i>生产环境</a>
	                         </h5>
	                     </li>
	                 </ul>
	                 <div class="title_line"></div>
	             </div>
	        </div>
         	

        <c:set var="isShow" value="N"></c:set>
        <c:if test="${ not empty envId }"> 
	        <auth:validator value="ProjectEnvValidator(projectId=${param.projectId},envId=${envId})">
	       		<c:set var="isShow" value="Y"></c:set>
	       	</auth:validator>
       	</c:if>
       	<c:if test="${   empty envId }"> 
	        <auth:validator value="ProjectCreateEnvValidator(projectId=${param.projectId},envType=${envType })">
	       		<c:set var="isShow" value="Y"></c:set>
	       	</auth:validator>
       	</c:if>
  		 <c:choose>
				<c:when test="${isShow eq 'Y'}">
       	<!-- tab -->
         	<c:if test="${ envList == null or fn:length(envList) == 0 }">
	        <p class="text-center p30_10" style="font-size: 16px;">暂未配置环境，立刻去添加一个环境配置</p>
	        <div class="add_content_btn text-center p30_10">
	            <ul>
	                <li>
	                    <a href="javascript:void(0)" onclick="projectEnvCreate('${envType}')">
	                        <i class="icons add_ico2"></i>
	                        <span>
	                        	<c:choose>
	                        		<c:when test="${envType eq 'test'}">
	                        			开发测试环境
	                        		</c:when>
	                        		<c:when test="${envType eq 'live'}">
	                        			生产环境
	                        		</c:when>
	                        	</c:choose>
	                        </span>
	                    </a>
	                </li>
	            </ul>
	        </div>
	     </c:if>
       	
       	<!-- -------- begin -->
	     <c:if test="${ envList != null and fn:length(envList) >0 }">
	     <div class="project_r p20"> 
		    <div class="no_content p0_10"> 
	        	<div class="bs_name_tit">
	               <ul>
	                   	<c:forEach items="${envList}" var="row">
	                   		<li class="bs_name_menu${ row.envId eq envId ? ' active' : ''}">
	                   			<a class="li_menu" href="${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envId=${row.envId}" style="min-width: 100px;text-align: center;"><c:if test="${! empty row.envMarkName}">${row.envMarkName}-</c:if>${ row.envName }<i class="arrow-down"></i></a>
	                 			<c:if test="${ row.envId eq envId }">
									<div class="bs_action">
	                 			<button class="drop_down"><i class="arrow-down3"></i><i class="arrow-down2"></i> </button>
									<div class="list_sub_menu2">
									<div class="blank_grid">&nbsp;</div>
	                                  <div class="list_menu">
	                                      <a href="javascript:void(0)" onclick="projectEnvEdit()">修改名称</a><br/>
	                                      <!-- <a href="javascript:void(0)" onclick="projectEnvClone()">克隆环境</a><br/> -->
	                                      <a href="javascript:void(0)" onclick="projectEnvDel()">删除环境</a><br/>
	                                  </div>
                                     </div>
									</div>
	                             </c:if>
	                   		</li>
	                   	</c:forEach>
	               </ul>
	               <a href="javascript:void(0)" onclick="projectEnvCreate('${envType}')" title="添加环境"><i class="icons add_ico16"></i></a>
	           	</div>
	           	<c:if test="${ not empty envId }"> 
		         		<c:import url="/project/env/info.do?envId=${envId}&projectId=${project.projectId}"></c:import>
		         </c:if>
          	</div>
     	 </div>
         </c:if>
		 <!-- ---------------------end -->
		 
	  		 	</c:when>
	  		 	<c:otherwise>
	  		 	<div class=" ">
			    <div class=" ">
			        <div class=" ">
			               <div class="message_block">
			                   <div class="message_title"><i class="message_ico error_ico"></i>访问受限</div>
			                   <div class="message_con">
			                       <dl>
			                           <dt class="text-green2">温馨提示：</dt>
			                           <dd>您暂无当前操作的访问权限，如需继续访问点击联系<a href="#">管理员</a></dd>
			
			                       </dl>
			                       <span class="pull-right"><a class="more" href="javascript:history.back()">后退 >></a> </span>
			                   </div> 
			               </div>
			        </div>
			    </div>
			</div>
		       
		         		</c:otherwise>  
		         	</c:choose>
        </div>
	</template:replace>
	<template:replace name="bottom">
	<template:super/>
		<script>
			$(function(){
	//			getContainerStatus();
			}); 
		</script>
		<jsp:include page="script.jsp" />
	</template:replace>
</template:include>
	<script>
	//    隐藏按钮JS
	$(function(){
	$(".bs_action").hover(function () {
	$(this).parent().addClass("open");
	}, function () {
	$(this).parent().removeClass("open");
	});
	});
	</script>