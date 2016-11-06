<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="cluster_block_three active row-td pos-rel" id="resource_${resourceItem.recordId}" style="height:220px;">
	
	    <div class="cluster-header">
	        <h3 class="cluster_name">${ resourceItem.attach['node'].name }</h3>
	        <div class="cluster-right pull-right" style="margin-top:2px;">
	        <button class="btn btn-more"><i class="icons ico_more2"></i></button>
	        <div class="list_sub_menu">
	        <div class="blank_grid">&nbsp;</div>
	        <ul class="list_menu">
	        <div class="arrow-up arrow_pos"></div>


	        <%--
	            <a href="${resourceItem.attach['node'].help}" target="_blank" title="帮助"><i class="tit_ico help_ico"></i></a>
	             --%>
<%-- 	            <a href="javascript:void(0)" onclick="projectEnvEditResource('${resourceItem.recordId}')"  title="修改"><i class="tit_ico edit_ico"></i></a> --%>

	<li><a class="btn_sm f12" href="javascript:void(0)" onclick="projectEnvResourceHelp('${resourceItem.recordId}')"  title="查看"><span class="fa fa-book mr10" aria-hidden="true"></span>帮助</a></li>

	<li><a class="btn_sm f12" href="javascript:void(0)" onclick="projectEnvDelResource('${resourceItem.recordId}')" title="删除"><span class="fa fa-trash mr10" aria-hidden="true"></span>删除</a></li>
</ul></div>
	</div>
	    </div>
	    <div class="cluster-content cluster-content2">
	        <div class="sl_zs_img">
							<img style="width:64px;height: 64px;" src="${ hfn:urlPath(resourceItem.attach['node'].icon,pageContext.request) }" / >
	        </div>
	        <div class="sl_zs_con cluster-content cluster-content2">
	        		<ul>
	        			<li>
	        				<label>资源参数</label>
	        				<p><a class="fa fa-question" href="javascript:void(0)" onclick="projectEnvViewResource('${resourceItem.recordId}')"  title="查看">点击查看</a></p>
	        			</li>
	        			<li>
	        				<label>描述</label>
	        				<p style="height:30px;overflow:hidden;">${ resourceItem.attach['node'].summary }</p>
	        			</li>
	        			<li>
		        			<c:choose>
				        		<c:when test="${resourceItem.resourceId eq 'mysql51' }">
				        			<label>实例名称</label>
				        			<p>${resourceItem.resAddition }</p>
				        		</c:when>
				        		<c:when test="${resourceItem.resourceId eq 'nas' }">
				        			<label>挂载路径</label>
				        			<p>${resourceItem.resAddition }</p>
				        		</c:when>
				        		<c:otherwise>
				        		</c:otherwise>
				        	</c:choose>
	        			</li>
	        			<li>
	        				<label>资源状态</label>
	        				<p id="resourceStatus_${resourceItem.recordId}">
	        					<span></span>
	        					<a href="javascript:void(0)" onclick="showProjectEnvResourceStatus('${resourceItem.recordId}')">刷新</a>
	        				</p>
	        			</li>
	        		</ul>
	        </div>
	    </div>

</div>
<script>
$(function(){
	showProjectEnvResourceStatus('${resourceItem.recordId}');
});
</script>