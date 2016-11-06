<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="title">
		环境变量
	</template:replace>
	<template:replace name="body">
		<style type="text/css">
		body {
			overflow: auto;
		}
		.w160 {
			width:160px;
		}
		</style>
		<div class="r_block p20" id="applyResource">
			<div class="panel-heading">
				<h3 class="f14 h3_color h3_btm_line">
					环境变量
				</h3> 
			</div>
			<div class="r_con p10_0"> 
				<table class="table_ob">
					<thead>
						<tr>
							<th style="width: 180px;">Key</th>
							<th>Value</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ environment }" var="row">
							<tr>
								<td>${row.key}</td>
								<td>${row.value}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div> 
		</div>
		<div class="r_block p20" >
			<div class="panel-heading">
				<span class="f14 h3_color ">
						自定义环境变量
				</span> 
				 <div class="pull-right"><a href="javascript:void(0)" onclick="toggleEnv()">展开/折叠自定义参数</a></div>
				 <div class="h3_btm_line"></div>
			</div>
			<div class="panel-body" id="defineResource">
				<table class="table_ob">
					<thead>
						<tr>
							<th style="width: 180px;">Key</th>
							<th>Value</th>
							<th style="width: 50px;text-align: center;">
								<a href="javascript:void(0)" title="添加资源键值对" onclick="addNewRow()">
									<i class=" " style="font-size: 120%;">+</i> 
								</a>
							</th>
						</tr>
					</thead>
					<tbody id="userDefinedResTbl">
						<c:forEach items="${ defineEnv }" var="row">
							<tr>
								<td><input type='text' class="w160" name='resourceKey' value="${row.key}"/></td>
								<td><input type='text' class="w160" name='resourceValue' value="${row.value}" /></td>
								<td class="centerAlign" style='text-align:center;font-size:125%;'><a href="javascript:void(0)" onclick="deleteRow(this)"><i class=' '>-</i></a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="clearfix"></div>
				<div style="padding:20px 0px"><a href="javascript:void(0)" class="btn btn-default btn-sm" onclick="saveDefineResource()">保存自定义变量</a></div>
			</div>
			<script>
				function addNewRow(){
					var table = $("#userDefinedResTbl");
					var row =$("<tr></tr>");
					var cell1 = $("<td><input type='text' class='w160' name='resourceKey' /></td>");
					row.append(cell1);
					var cell2 = $("<td><input type='text' class='w160' name='resourceValue'/></td>");
					row.append(cell2);
					var cell3 = $("<td class='centerAlign' style='text-align: center;font-size: 125%;'><a href='javascript:void(0)' onclick=\"deleteRow(this)\"><i class=' '>-</i></a></td>");
					row.append(cell3);
					table.append(row);
				}
				
				function deleteRow(obj){
					var deleteTR = $(obj).closest("tr");
					deleteTR.remove();
				}
				
				function toggleEnv(){
					$("#defineResource").toggle();
				}
				
				function saveDefineResource(){
					var trs = $("#userDefinedResTbl").find(">tr");
					var defineData=[];
					for(var i=0; i< trs.length ;i++){
						var tr = $(trs[i]);
						var key = $.trim(tr.find("input[name='resourceKey']").val());
						var value= $.trim(tr.find("input[name='resourceValue']").val());
						var filter = /^[A-Za-z0-9_]+$/;
						if(!filter.test(key) || value == ''){
							common.tips("第"+(i+1)+"行填写不合法,名称只能包含数字字母下划线，同时value不能为空");
							return false;
						}
						var systemVar=['PAASOS_NFS_PATH','PAASOS_MYSQL_USERNAME','PAASOS_MYSQL_PASSWORD','PAASOS_MYSQL_DATABASE',
						               'PAASOS_MYSQL_HOST_IP','PAASOS_MYSQL_HOST_PORT','PAASOS_REDIS_HOST_IP','PAASOS_REDIS_HOST_PORT','PAASOS_RABBIT_HOST_IP','PAASOS_RABBIT_HOST_PORT'];
						for(var j=0;j<systemVar.length;j++){
							if(key==systemVar[j]){
								common.tips("第"+(i+1)+"行key命名不合法，与内置变量名冲突");
								return false;
							}
						}
						for(var j=0;j<defineData.length;j++){
							if(key==defineData[j].key){
								common.tips("第"+(i+1)+"行key与前面定义的key重复");
								return false;
							}
						}
						var data={};
						data.key=key;
						data.value=value;
						defineData.push(data);
					}
					var load= common.loading();
					var url="${WEB_APP_PATH}/project/env/resource/saveOrUpdateDefines.do?projectId=${project.projectId}";
					$.post(url,{"data":JSON.stringify(defineData),"envId":"${envId}"},function(json){
						load.close();
						if(json.code==0){
							common.tips("保存成功",1);
						}else{
							common.tips("保存失败");
						}
					});
				}
			</script>
		</div>
	</template:replace>
</template:include>