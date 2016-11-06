<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/body.jsp">
	<template:replace name="title">
		拒绝访问
	</template:replace>
	<template:replace name="page-content">
		               <div class="message_block">
		                   <div class="message_title"><i class="message_ico error_ico"></i>访问受限</div>
		                   <div class="message_con">
		                       <dl>
		                           <dt class="text-green2">温馨提示：</dt>
		                           <dd>您暂无当前操作的访问权限，如需继续访问点击联系<a href="#">管理员</a>......<span id="s_ss">5</span>秒后自动跳转到 <a class="text-green2" href="${ WEB_APP_PATH }/">首页</a></dd>
		
		                       </dl>
		                       <span class="pull-right"><a class="more" href="javascript:history.back()">后退 >></a> </span>
		                   </div> 
		               </div>
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script>
			var loop = 6;
			window.setInterval(function(){
				loop--;
				document.getElementById("s_ss").innerHTML = loop;
				if(loop==0){
					location.href = "${WEB_APP_PATH}/"
				}
			}, 1000);
		</script>
	</template:replace>
</template:include>
