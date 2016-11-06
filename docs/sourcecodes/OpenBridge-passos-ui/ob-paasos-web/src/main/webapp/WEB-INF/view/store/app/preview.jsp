
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>

<div class="mirro_list">
	<c:forEach items="${page }" var="app">
		<div class="cluster_block_three active pos-rel">
			<div class="cluster-header">
				<h3 class="cluster_name" id="appName"><a href="detail.do?id=${app.id }">${app.name }</a></h3>
				<p style="display:none;" id="config_${app.id }">${app.config }</p>

		      <div class="cluster-right pull-right" style="margin-top:2px;">
		          <button class="btn btn-more"><i class="icons ico_more2"></i></button>
		          <div class="list_sub_menu">
		             <div class="blank_grid">&nbsp;</div>
		                 <ul class="list_menu">
		                     <div class="arrow-up arrow_pos"></div>
				<auth:validator value="functionValidator(functionId=paasos.store.deploy)">
					<c:if test="${app.status==1 || app.status==0 || app.status==10 }">
						<li><a href="javascript:buildApp('${app.id }');"><span class="fa fa-magic mr10" aria-hidden="true"></span>构建</a></li>
					</c:if>
					<c:if test="${app.status==10}">
						<li><a href="javascript:deployApp('${app.id }','${app.appName }');"><span class="fa fa-bolt mr10" aria-hidden="true"></span>部署</a></li>
					</c:if></auth:validator>
					<auth:validator value="functionValidator(functionId=paasos.store.manager)">
						<li><a href="add_app.do?id=${app.id }"><span class="fa fa-pencil mr10" aria-hidden="true"></span>编辑</a></li>
					<c:if test="${app.status!=5 && app.status!=6 }">
						<li><a href="javascript:deleteApp('${app.id }');"><span class="fa fa-trash mr10" aria-hidden="true"></span>删除</a></li>
					</c:if></auth:validator>
					<c:if test="${app.status!=1 && app.status!=6}">
						<li><a class="log-button" href="javascript:viewLog('${app.id }');"><span class="fa fa-book mr10" aria-hidden="true"></span>日志</a></li>
					</c:if>
			             </ul>
		</div>
		</div>
			</div>
			<%-- <div class="mirro_info mirro_info2">
				<p>
					<i class="mirri_ico"><img
						src="${WEB_APP_PATH}/paas/file/view.do?filePath=${app.iconPath}" width="80"
						height="80"></i>
				</p>
				<dl>
					<dd>
						${app.description }
					</dd>

				</dl>
				<div class="hot">
					<span class="download"><i class="icon-favorite"></i> 27</span>
				</div>
			</div> --%>
			<div class="img-container" style="float: left;padding: 10px;">
           		<img src="${WEB_APP_PATH }/paas/file/view.do?filePath=${app.iconPath}">
          	</div>
			<div class="cluster-content cluster-content2" style="float:left;width:290px;">
                <ul>
                    <li>
                        <label>应用名称</label>
                        <p>${app.appName }&nbsp;</p>
                    </li>
                    <li>
                        <label>应用版本</label>
                        <p>${app.version }</p>
                    </li>
<!--                     <li> -->
<!--                         <label>构建文件</label> -->
<%--                         <p>${app.fileName }</p> --%>
<!--                     </li> -->
                    <li>
                        <label>状态</label>
                        <p>
                        	<span class="zt_span">
                        	<c:choose>
							<c:when test="${app.status == 0}"><i class="pas_ico zt_ico fail_ico mt5"></i>构建失败</c:when>
							<c:when test="${app.status == 1}"><i class="pas_ico zt_ico doing_ico mt5"></i>未构建</c:when>
							<c:when test="${app.status == 5}"><i class="pas_ico zt_ico doing_ico mt5"></i>构建中</c:when>
							<c:when test="${app.status == 6}"><i class="pas_ico zt_ico doing_ico mt5"></i>删除中</c:when>
							<c:when test="${app.status == 10}"><i class="pas_ico zt_ico done_ico mt5"></i>可部署</c:when>
							<c:when test="${app.status == 11}"><i class="pas_ico zt_ico fail_ico mt5"></i>删除失败</c:when>
							</c:choose>
                        	</span>
                        </p>
                    </li>
                    <%-- <li>
                        <label>镜像信息</label>
                        <p>project/${row.build_name }:${row.build_tag }</p>
                    </li> --%>
                    <li>
                    	<label>应用部署</label>
                    	<script>
                    	$.get(WEB_APP_PATH+"/project/getProjectByBusId.do",{
                			businessId : '${app.id}'
                		},function(projList){
                			var p = $("div[data-id='${app.id}']");
                			if(projList && projList.length>0){
           						var projCon = "";
           						$.each(projList,function(i,v){
           							if(i>0)	projCon += "、";
           							projCon += "<a href='"+WEB_APP_PATH+"/project/overview/index.do?projectId="+v.projectId+"'>"+v.projectName+"</a>"
           						})
           						p.html(projCon);
                			}else{
                				p.html("无");
                			}
                		});
                    	</script>
                        <div data-id="${app.id }" style="/* word-wrap: break-word; */overflow: hidden;width: 170px;float:left;text-overflow: ellipsis;"></div>
                    </li>
                </ul>
            </div>

		</div>
	</c:forEach>
</div>


<div id="apps" class="table_page">
	<c:if test="${not empty page}">
		<ui:pagination data="${page }"
			href="javascript:loadItems(!{pageNo},!{pageSize});"
			id="presetAppPage"></ui:pagination>
	</c:if>
</div>

<script>
	$(document).ready(function() {
	$(".cluster-right").hover(function () {
	$(this).addClass("open");
	}, function () {
	$(this).removeClass("open");
	});
	});
</script>
