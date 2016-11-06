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
						<th style="width: 50px;"></th>
						<th>名称</th>
						<th>创建人</th>
						<th>创建时间</th>
					</tr>
					<c:forEach items="${ pageData }" var="row" varStatus="index">
						<tr>
							<td style="text-align: center;"><c:choose>
									<c:when
										test="${ not empty param['multiple'] and param['multiple'] eq 'false' }">
										<input class="userChk" type="radio" name="receiveTeam" value="${ row.id }" data-name="${row.name }" id="${row.name }"
										data-creator_user="${row.creator_user }" data-created="${row.created }" data-id="${row.id }"
											style="width: auto;" />
									</c:when>
								</c:choose></td>
							<td><span class="tplName">${ row.name }</span></td>
							<td><span class="createUser">${ row.creator_user }</span></td>
							<td><span class="teamName">${ row.created }</span></td>
						</tr>
					</c:forEach>
				</table>
      <ui:pagination data="${ pageData }" href="/teams/listTeam?multiple=${param['multiple']}&keyword=${keyword }" id="host"></ui:pagination>
			</div>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<template:super />
		<script>
		//用于返回选中的team
			var mySelectTeam;
			
			if(!$.isEmptyObject(frameElement.mySelectTeam)){
				mySelectTeam = frameElement.mySelectTeam;
			}

		    var multiple="${param['multiple']}";
			var params = frameElement.params;
			var id = params.teamId;
		
			$("#"+id).attr("checked",true);
			

			
			if(params == null){
				params = {};
			}
			var ids = null;
			if(params.Ids==null||$.trim(params.Ids) == ""){
				ids = [];
			}else{
				ids = params.Ids.split(";"); 
			}
			var names = null;
			if(params.tplNames==null||$.trim(params.tplNames) == ""){
				names = [];
			}else{
				names = params.tplNames.split(";"); 
			}
			var creates = null;
			if(params.createUsers==null||$.trim(params.createUsers) == ""){
				creates = [];
			}else{
				creates = params.createUsers.split(";");
			}
			var teams = null;
			if(params.teamNames==null||$.trim(params.teamNames) == ""){
				teams = [];
			}else{
				teams = params.teamNames.split(";"); 
			}
			$(function() {
	

				$(":radio").click(function(){
					if(!(typeof($(this).val()) == "undefined")){
						mySelectTeam.id=$(this).val();
					}
					if(!(typeof($(this).attr("data-name")) == "undefined")){
						mySelectTeam.name=$(this).attr("data-name");
					}
					if(!(typeof($(this).attr("data-creator_user")) == "undefined")){
						mySelectTeam.creator_user=$(this).attr("data-creator_user");
					}
					if(!(typeof($(this).attr("data-created")) == "undefined")){
						mySelectTeam.created=$(this).attr("data-created");
					}
					//mySelectTeam.creator_user=$(this).attr("data-creator_user");
					//mySelectTeam.created=$(this).attr("data-created");
				});
				
				$("#search").bind('click',
					function() {
						common.goto("${ WEB_APP_PATH }/teams/listTeam?multiple=${param['multiple']}&keyword="+$.trim($("#keyword").val()));
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


			function dialogClose(){
				
				var myReturn = {};
				var myThis = $('input:radio[name="receiveTeam"]:checked');
				if(!(typeof($(myThis).val()) == "undefined")){
					myReturn.id=$(myThis).val();
				}
				var t = $(myThis).val();
				
				if(!(typeof($(myThis).attr("data-name")) == "undefined")){
					myReturn.name=$(myThis).attr("data-name");
				}
				if(!(typeof($(myThis).attr("data-creator_user")) == "undefined")){
					myReturn.creator_user=$(myThis).attr("data-creator_user");
				}
				if(!(typeof($(myThis).attr("data-created")) == "undefined")){
					myReturn.created=$(myThis).attr("data-created");
				}
				return myReturn;
			}
		</script>
	</template:replace>
</template:include>