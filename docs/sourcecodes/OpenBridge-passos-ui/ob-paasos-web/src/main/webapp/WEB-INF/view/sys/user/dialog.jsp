<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
			body {
				overflow: auto;
			}
		</style>
		<div class="form_control">
			<div class="form_block choose_item">
				<label style="width:56px; font-size:12px;">关键字：</label><input type="text" id="keyword" name="keyword" value="${keyword }"/><button class="btn btn-default btn-sm btn-green" id="search">搜索</button>
			</div>
			<div class="choose_table_list">
			<table class="table_ob" id="userTable">
	               <tr>
	                   <th style="width:50px;text-align: center;"><input type="checkbox" id="checkAll"/></th>
	                   <th style="width:130px;">用户名</th>
	                   <th style="width:150px;">登陆名</th>
	                   <th>邮箱</th>
	               </tr>
				<c:forEach items="${ pageData }" var="row" varStatus="index">
	               <tr>
	                   <td style="text-align:center;">
	                   		<c:choose>
								<c:when test="${ not empty param['multiple'] and param['multiple'] eq 'true' }">
									<input class="userChk" type="checkbox" value="${ row.userId }" data-name="${ row.userName }" style="width: auto;" />
								</c:when>
								<c:otherwise>
									<input class="userChk" type="radio" name="userId" value="${ row.userId }" data-name="${ row.userName }"  style="width: auto;" />
								</c:otherwise>
							</c:choose>
	                   </td>
	                   <td><span class="userName">${ row.userName }</span></td>
	                   <td>${ row.loginName }</td>
	                   <td>${ row.email }</td>
	               </tr>
	            </c:forEach>
	        </table> 
			<ui:pagination data="${ pageData }" href="/sys/user/dialog.do?multiple=${param['multiple']}&query=${param['query']}&keyword=${keyword }&table=${table }&tenantId=${tenantId}" id="user"></ui:pagination>
		</div>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script>
		    var multiple="${param['multiple']}";
			var params = frameElement.params;
			if(params == null){
				params = {};
			}
			var ids = null;
			if(params.userIds==null||$.trim(params.userIds) == ""){
				ids = [];
			}else{
				ids = params.userIds.split(";"); 
			}
			var names = null;
			if(params.userNames==null||$.trim(params.userNames) == ""){
				names = [];
			}else{
				names = params.userNames.split(";"); 
			}

			$(function() {
				$("#userTable tr:gt(0)").click(function(e){
            		if(e.target.nodeName=='INPUT'){
            			return ;
            		}
            		var chb = $(this).find("td [type=checkbox]");
            		chb.attr("checked",chb.attr("checked")=="checked" ? false : true);
            		chb.trigger("change");
            	})
            	$("#checkAll").click(function(){
            		var ch = $(this).attr("checked")=="checked";
            		$("#userTable tr:gt(0) [type=checkbox]").each(function(i,v){
            			$(this).attr("checked", ch ? true : false);
            			$(this).trigger("change");
            		});
            	});
				
				$(".userChk").change(function(e){
					if($(this).attr("type")=='checkbox'){
						onCheckClick($(this)[0]);
					}else{
						onradioClick($(this)[0]);
					}
				})
				
				$("#search").bind('click',
					function() {
						common.goto("${ WEB_APP_PATH }/sys/user/dialog.do?multiple=${param['multiple']}&query=${param['query']}&table=${table }&tenantId=${tenantId}&keyword="+$.trim($("#keyword").val()));
					}
				);
				if(multiple){
					if(ids.length > 0){
						var rows = $("#userTable").find("tr");
						for(var i=0;i<rows.length;i++){
							if(i==0){}
							else{ 
								var row = rows[i];
								var chb = $(".userChk",row);
								for(var j=0;j<ids.length;j++){
									if(chb.val()==ids[j]){
										chb.attr("checked","checked");
									}
								}
							}
						} 
					}
				}
			});
			function onCheckClick(obj){ 
				var xobj = $(obj);
				var isExt = false;
				var xi = 0;
				for(var i=0;i<ids.length;i++){
					if(ids[i]==xobj.val()){
						isExt = true;
						xi = i;
						break;
					}
				}
				if(xobj.is(":checked")){
					if(isExt){
						
					}else{
						ids[ids.length] = xobj.val();
						names[names.length] = xobj.attr("data-name");
					}
				}else{
					if(isExt){
						ids.splice(xi,1);
						names.splice(xi,1);
					}else{
						
					}
				}
				frameElement.params = {};
				frameElement.params.userIds = ids.join(";");
				frameElement.params.userNames = names.join(";");
			}
			
			function onradioClick(obj){ 
				var xobj = $(obj);
				var isExt = false;
				var xi = 0;
				if(xobj.is(":checked")){
					if(isExt){
						
					}else{
						ids[ids.length-1] = xobj.val();
						names[names.length-1] = xobj.attr("data-name");
					}
				}else{
					if(isExt){
						ids.splice(xi,1);
						names.splice(xi,1);
					}else{
						
					}
				}
				frameElement.params = {};
				frameElement.params.userIds = ids.join(";");
				frameElement.params.userNames = names.join(";");
			}
			function dialogClose(){
				if(multiple){
					var obj = {};
					obj.ids = ids;
					obj.texts = names;
					return obj;
				}else{
					var obj = {};
					obj.ids = [];
					obj.texts = [];
					var rows = $("#userTable").find("tr");
					for(var i=0;i<rows.length;i++){
						if(i==0){}
						else{  
							var row = rows[i];
							var chb = $(":checked",row);
							if(chb && chb.length > 0){
								obj.ids.push(chb.val());
								obj.texts.push($(row).find(".userName").text());
							}
						}
					} 
					return obj;
				}
			}
		</script>
	</template:replace>
</template:include>