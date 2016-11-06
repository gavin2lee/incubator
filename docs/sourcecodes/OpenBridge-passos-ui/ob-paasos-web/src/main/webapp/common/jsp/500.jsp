<%@ page import="java.io.PrintWriter"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/body.jsp">
	<template:replace name="title">
		出错了 @@@
	</template:replace>
	<template:replace name="page-content">
		<!-- #section:pages/error -->
		<div class="error-container">
			<div class="well">
				<h1 class="grey lighter smaller">
					<span class="blue bigger-125"> <i
						class="ace-icon fa fa-random"></i> 500
					</span> 发生了一些错误
				</h1>

				<hr>
				<h3 class="lighter smaller">
					But we are working <i
						class="ace-icon fa fa-wrench icon-animated-wrench bigger-125"></i>
					on it!
				</h3>

				<div class="space"></div>

				<div>
					<h4 class="lighter smaller">Meanwhile, try one of the
						following:</h4>

					<ul class="list-unstyled spaced inline bigger-110 margin-15">
						<li><i class="ace-icon fa fa-hand-o-right blue"></i> Read the
							faq</li>

						<li><i class="ace-icon fa fa-hand-o-right blue"></i> Give us
							more info on how this specific error occurred!</li>
					</ul>
				</div>

				<hr>
				<div class="space"></div>

				<div class="center">
					<a href="javascript:history.back()" class="btn btn-grey"> <i
						class="ace-icon fa fa-arrow-left"></i> 返回
					</a> <a href="${ WEB_APP_PATH }/" class="btn btn-primary"> <i
						class="ace-icon fa fa-tachometer"></i> 首页
					</a>
				</div>
			</div>
		</div>


		<pre style="display: none;">
			=======================================================================================================================
			<%=exception.toString()%>
			=======================================================================================================================
			<%
			Exception ex = (Exception) request
							.getAttribute("exception");
					if (ex != null) {
						ex.printStackTrace(new PrintWriter(out));
					}
			%>
			=======================================================================================================================
		</pre>
	</template:replace>
</template:include>