<%@page import="net.jeeshop.core.front.SystemManager"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="net.jeeshop.core.ManageContainer"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="panel panel-primary">
	<div class="panel-heading">帮助中心</div>
  <div class="panel-body">
    	<ul>
    	<s:iterator value="#application.newCatalogs" status="i" var="help">
			<li class="list-item0">
				<h5>
					<s:property escape="false" value="name" />
				</h5>
				<s:iterator value="news" status="i2" var="item">
					<div style="margin-left: 20px;">
						<a href="<%=request.getContextPath()%>/help/<s:property escape="false" value="code" />.html"><s:property escape="false" value="title" /></a>
					</div>
				</s:iterator>
			</li>
		</s:iterator>
    	</ul>
  </div>
</div>


