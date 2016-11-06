<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../../sys/manager.jsp" active="nginx">
	<template:replace name="title">
		服务器配置
	</template:replace> 
	<template:replace name="content-path">
		<em>&gt;</em>
		<p class="app_a">
			<a href="${WEB_APP_PATH}/paas/nginx/host.do">访问代理</a>
		</p>
	</template:replace>
	<template:replace name="detail">
		 <!--服务数据开始-->
         <div class="p20">
             <div class="r_title">
                 	<c:import url="../nav.jsp?tab=hosts"></c:import>
             </div>
             <div class="r_con p10_0">
				关键字: <input type="text" id="keyword" name="keyword" value="${keyword}"/>
				<button class="btn btn-default btn-sm btn-green" id="search">
					<i class="fa fa-refresh"></i>
					查询
				</button>
				<button class="btn btn-default btn-sm btn-green" id="addUser" onclick="addHost()">
					<i class="glyphicon glyphicon-plus" ></i>
						添加
				</button>
				<button class="btn btn-default btn-sm btn-green" style="display:none;" id="delUsers">
					<i class="fa fa-times"></i>
					删除
				</button>
			  <table class="table_ob">
	               <tr>
	                   <th>服务器IP</th>
	                   <th>SSH用户名</th>
	                   <th>SSH端口</th>
	                   <th>环境</th>
	                   <th>备用服务器IP</th>
	                   <th>虚拟IP</th>
	                   <th>配置文件目录</th>
	                   <th>应用类型</th>
	                   <th>操作</th>
	               </tr>	
	          <c:forEach items="${ pageData }" var="row" varStatus="index">
	               <tr>
	               		<Td>${ row.hostIp }</Td>
	               		<td>${ row.hostUser }</td>
	               		<td>${ row.hostPort }</td>
	               		<td>
	               			<c:choose>
								<c:when test="${ row.envType eq 'dev' }">
									开发
								</c:when>
								<c:when test="${ row.envType eq 'test' }">
									测试环境
								</c:when>
								<c:when test="${ row.envType eq 'live' }">
									生产环境
								</c:when>
								<c:otherwise>
									${ row.envType }
								</c:otherwise>
							</c:choose>
	               		</td>
	               		<td>${ row.backupHost }</td>
	               		<td>${ row.virtualHost }</td>
	               		<td>${ row.directoryName}</td>
	               		<td>${ row.hostPlatform}</td>
	               		<td>
	               			<a href="${WEB_APP_PATH}/paas/nginx/edit.do?hostId=${row.hostId}"><i class="fa fa-pencil"></i>编辑</a>
	               			&nbsp;&nbsp;
	               			<a href="javascript:void(0);" onclick="deleteHost('${row.hostId}')"><i class="fa fa-pencil"></i>删除</a>
	               		</td>
	               </tr>
	          </c:forEach>
	          </table> 
			 
				<ui:pagination id="userPagging" data="${ pageData }" href="/paas/nginx/host.do">
				</ui:pagination> 
		
			 </div>
		</div> 
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script>
			function addHost(){
				common.goto('${WEB_APP_PATH}/paas/nginx/add.do');
			}
			$(function() {
				$("#search").bind('click',
					function() {
						if(!$.trim($("#keyword").val())){
							common.goto('${ WEB_APP_PATH }/paas/nginx/host.do');
							return false;
						}
						common.goto('${ WEB_APP_PATH }/paas/nginx/host.do?keyword='+$.trim($("#keyword").val()));
					}
				);
			});
			function deleteHost(hostId){
				if(common.confirm("确定删除该服务器?",function(ok){
					if(ok){
						var url ="${WEB_APP_PATH}/paas/nginx/delete.do";
						$.post(url,{"hostId":hostId},function(json){
							if(json.code==0){
								common.goto('${ WEB_APP_PATH }/paas/nginx/host.do');
							}else{
								common.alert(json.msg);
							}
						});
					}
				}));
			}
		</script>
	</template:replace>
</template:include>