 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/body.jsp">
	<template:replace name="title">
		访问地址不存在
	</template:replace>
	<template:replace name="page-content">
		<div class="error-container message_block">
			<div class="message_title">
				<i class="message_ico error_ico"></i>404
			</div>
			<div class="message_con">
				<dl>
					<dt class="text-green2">温馨提示：</dt>
					<dd style="margin-bottom: 10px;">您访问的页面不存在，可尝试进行关键字搜索查询</dd>
					<dd style="margin-bottom: 10px;">
						<input type="text" class="search-query"
							placeholder="Give it a search...">
						<button class="btn btn-sm btn-green" type="button">Go!</button>
					</dd>

				</dl>
				<div style="text-align: center; margin-top: 15px;">
					<a href="javascript:history.back()"
						class="btn btn-grey btn-green btn-sm mr20"> <i
						class="ace-icon fa fa-arrow-left"></i> 后退
					</a> <a href="${ WEB_APP_PATH }/" class="btn btn-grey btn-green btn-sm">
						<i class="ace-icon fa fa-tachometer"></i> 首页
					</a>
				</div>
			</div>
		</div>
	</template:replace>
</template:include>