 <%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="sl_block cluster_block_three active row-td" id="loadbalance_${loadbalanceItem.confId}">
	<div class="sl_content sl_content_fzjh h_auto">
	    <div class="sl_tit">
	    	${loadbalanceItem.nginxConfigName}
	        <span class="pull-right">
	        <%-- 
	            <a href="#" title="帮助"><i class="tit_ico help_ico"></i></a>
	         --%>
	            <a href="javascript:void(0)" onclick="projectEnvEditLoadbalance('${loadbalanceItem.confId}')" title="编辑"><i class="tit_ico edit_ico"></i></a>
	            <a href="javascript:void(0)" onclick="projectEnvDelLoadbalance('${loadbalanceItem.confId}')" title="删除"><i class="tit_ico close_ico"></i></a>
	        </span>
	    </div>
	    <div class="sl_zs h_auto">
	        <div class="sl_zs_img">
	            <i class="nginx_ico"></i>
	        </div>
	        <div class="sl_zs_con">
	            <dl>
	                <dt>访问域名</dt>
	                <dd><a href="http://${ loadbalanceItem.url }" target="_blank">${ loadbalanceItem.url }</a></dd>
	                	<c:if test="${! empty loadbalanceItem.slaveDomain }">
				            	<c:forEach items="${loadbalanceItem.slaveDomain }" var="row">
					            	<dt></dt>
		                		<dd><a href="http://${ row }" target="_blank">${ row }</a></dd>
				            	</c:forEach>
				            </c:if>
	            </dl>
	            <dl>
	                <dt>负载IP</dt>
	                <dd>${ loadbalanceItem.nginxIp }</dd>
	            </dl>
	            <dl>
	                <dt>代理记录</dt>
	                <dd>
	                	<c:forEach items="${loadbalanceItem.confList}" var="confItem">
	                		${ confItem.ip }:${ confItem.port }
	                		|${ confItem.weight } <br>
	                	</c:forEach>
	                </dd>
	            </dl>
	        </div>
	    </div>
	</div>
</div>
