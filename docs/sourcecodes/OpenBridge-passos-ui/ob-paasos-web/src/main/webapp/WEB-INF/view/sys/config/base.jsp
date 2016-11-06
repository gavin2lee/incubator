<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../manager.jsp" active="config">
	<template:replace name="title">
		系统参数设置
	</template:replace>
	<template:replace name="content-path">
		<em>&gt;</em>
		<p class="app_a">
			<a href="${WEB_APP_PATH}/sys/config/view.do">系统参数设置</a>
		</p>
	</template:replace>
	<template:replace name="detail">
         <div class="tab_title">
             <div class="title_tab title_tab_item title_tab_item_pas text-center">
                 <ul>
                     <li>
                         <h5 class="f14">
                             <a class="${param.active !=null && param.active=='view' ? 'active' : ''}"  href="${WEB_APP_PATH}/sys/config/view.do">基本信息</a>
                         </h5>
                     </li>
                     <li>
                         <h5 class="f14">
                             <a class="${param.active !=null && param.active=='integrate' ? 'active' : ''}"  href="${WEB_APP_PATH}/sys/config/integrate.do">
                                 集群与资源
                             </a>
                         </h5>
                     </li>
                      <li>
                         <h5 class="f14">
                             <a class="${param.active !=null && param.active=='codemanage' ? 'active' : ''}"  href="${WEB_APP_PATH}/sys/config/codemanage.do">
                                 构建与仓库
                             </a>
                         </h5>
                     </li>
                     <li>
                         <h5 class="f14">
                             <a class="${param.active !=null && param.active=='monitor' ? 'active' : ''}"  href="${WEB_APP_PATH}/sys/config/monitor.do">
                                 监控与预警
                             </a>
                         </h5>
                     </li>
                 </ul>
                 <div class="title_line"></div>
             </div>
         </div>
         <div class="r_con p10_0"  id="detail-info">
					<template:block name="detail-info"></template:block>
         </div> 
	</template:replace>
</template:include>