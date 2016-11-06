<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp">
	<template:replace name="content"> 
	       <div class="left_sub_menu left_sub_menu2">
	           <ul> 
	               <li>
	               	  <a ${ param['active'] eq 'config' ? ' class="select" ' : '' } href="${WEB_APP_PATH}/sys/config/view.do">
	                   	<i class="left_menu_ico l_ico03"></i>系统配置
	                    </a>
	               </li> 
	               <li>
	               	  <a ${ param['active'] eq 'user' ? ' class="select" ' : '' } href="${WEB_APP_PATH}/sys/user/page.do">
	                   	<i class="left_menu_ico l_ico05"></i>用户管理
	                    </a>
	               </li> 
	               <li>
	               	  <a ${ param['active'] eq 'access' ? ' class="select" ' : '' } href="${WEB_APP_PATH}/sys/access/function.do?systemKey=paasos">
	                   	<i class="left_menu_ico l_ico04"></i>权限管理
	                    </a>
	               </li> 
	               <li>
	               	  <a ${ param['active'] eq 'nginx' ? ' class="select" ' : '' } href="${WEB_APP_PATH}/paas/nginx/host.do">
	                   	<i class="left_menu_ico l_ico04"></i>访问代理
	                    </a>
	               </li>
	                <li>
	               	  <a ${ param['active'] eq 'toolbox' ? ' class="select" ' : '' } href="${WEB_APP_PATH}/sys/toolbox/index.do">
	                   	<i class="left_menu_ico l_ico04"></i>运维工具
	                    </a>
	               </li>
	           </ul>
	       </div>
	       <div class="right_body">
		       <div class="app_name">
		           <a href="javascript:void(0)"><i class="icons go_back_ico"></i></a>
		           <p class="app_a">系统设置</p>
		           <template:block name="content-path">
			
			      </template:block>
		        </div>
	        	<div class="plate">
		            <div class="project_r "> 
						<template:block name="detail"></template:block>
					</div>
				</div>
			</div>
	</template:replace>
</template:include>
