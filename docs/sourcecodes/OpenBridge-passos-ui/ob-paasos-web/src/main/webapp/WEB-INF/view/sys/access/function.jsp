<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../../sys/manager.jsp" active="access">
	<template:replace name="title">
		权限设置
	</template:replace>
	<template:replace name="content-path">
		<em>&gt;</em>
		<p class="app_a">
			<a href="${WEB_APP_PATH}/sys/access/function.do?systemKey=paasos">权限设置</a>
		</p>
	</template:replace>
	<template:replace name="detail">
		<div class="p20">
			<form action="${WEB_APP_PATH}/sys/access/saveRoleFunc.do"
				method="post" id="roleFunctionForm">
				<div class="r_block">
					<div class="r_title">
						<h3 class="f14 h3_color h3_btm_line blue">
							<a href="javascript:void(0)"> <i class="icons cy_ico mr5"></i>角色权限
							</a>
						</h3>

						<div class="title_tab">
							<ul>
								<li>
									<h5 class="f14">
										<a class="active_green" href="javascript:void(0)"
											onclick="onAddRole()"> <i
											class="icons add_ico_yellow mr5"></i>添加角色
										</a>
									</h5>
								</li>
							</ul>
						</div>
						<div class="title_line"></div>
					</div>
					<div class="r_con p10_0">
						<div class="ser_bar mt10 mb10">
							<label> 选择角色： </label> <select id="roleSelect" name="roleId"
								onchange="onRoleChange(this)" style="width: 200px;">
								<c:forEach items="${ roles }" var="item">
									<option data-desc="${ item.roleDesc }" value="${ item.roleId }">${ item.roleName }</option>
								</c:forEach>
								<option data-desc="普通用户拥有创建项目及资源的基本权限,可以查看预置应用" value="">普通用户</option>
							</select> <span style="position: relative; top: 2px; cursor: pointer;"
								onclick="onRoleEdit()" id="EditRoleUser"> <a
								class="btn btn-default btn-sm btn-yellow2">修改角色</a>
							</span> <span style="position: relative; top: 2px; cursor: pointer;"
								onclick="onRoleDel()" id="DelRoleUser"> <a
								class="btn btn-default btn-sm btn-yellow2">删除角色</a>
							</span>
						</div>
						<div class="r_con" style="margin: 0 -.5%;">
							<div class="sl_block sl_block_two">
								<div class="role_info">
									<dl>
										<dt>角色描述</dt>
										<dd id="roleDesc"></dd>
									</dl>
								</div>
							</div>
							<div class="sl_block sl_block_two">
								<div class="role_info">
									<dl>
									<dt>包含人员</dt>
									<dd id="userlist"></dd>
									</dl>
								</div>
							</div>
						</div>


						<div class="role_edit" id="funcsEdit">
							<div class="role_all">
								<label><input type="checkbox" id="sel_chk_all"></label>
								全选
							</div>
							<c:forEach items="${funcs}" var="entry" varStatus="st">
								<div class="role_block" id="module_${st.index}">
									<div class="role_block_tit">
										<label><input type="checkbox"
											id="chk_module_${st.index}"></label> ${ entry.key }
									</div>
									<div class="role_block_con">
										<ul>
											<c:forEach items="${ entry.value }" var="row">
												<li><label> <input class="funcCla"
														id="${ row.funcId }" name="funcId" value="${ row.funcId }"
														type="checkbox">
												</label>${ row.funcName }</li>
											</c:forEach>
										</ul>
									</div>
								</div>
							</c:forEach>
						</div>
						<div style="padding: 10px 0;" id="SaveBtn">
							<span style="position: relative; top: 2px; cursor: pointer;"
								onclick="onSave()"> <a class="btn btn-yellow f14">保存</a>
							</span>
						</div>
					</div>
				</div>
			</form>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<template:super />
		<script type="text/javascript"
			src="${WEB_APP_PATH}/assets/js/icheck.min.js"></script>
		<script type="text/javascript">
			$(function(){
				$('input').iCheck({
		            checkboxClass: 'icheckbox_square-blue',  // 注意square和blue的对应关系,用于type=checkbox
		            radioClass: 'iradio_square-blue', // 用于type=radio
		            increaseArea: '20%' // 增大可以点击的区域
		        }).on("ifClicked",onCheckBoxClick);
				onRoleChange(document.getElementById("roleSelect"));
			});
			function onCheckBoxClick(event){
				var obj = $(event.target);
				var xid = obj.attr("id")
				var isCheck = obj.is(":checked");
				if(xid == "sel_chk_all"){
					$(".role_edit").find("input").each(function(n,item){
						setFuncValueByClass(item,!isCheck);
					});
				}else if(xid.indexOf("chk_module_")==0){
					xid = xid.substring(11);
					$("#module_"+xid).find("input").each(function(n,item){
						setFuncValueByClass(item,!isCheck);
					});
				}
			}
			function onAddRole(){ 
				var dialog = common.dialogIframe("添加角色","${WEB_APP_PATH}/sys/access/role/add.do",600,530,function(){
					dialog.close();
					common.forward('${WEB_APP_PATH}/sys/access/function.do?systemKey=paasos');
				}); 
			}
			function onRoleEdit(){
				var roleId = $("#roleSelect").val();
				var dialog = common.dialogIframe("角色编辑","${WEB_APP_PATH}/sys/access/role/edit.do?roleId="+roleId,600,530,function(){
					dialog.close();
					common.forward('${WEB_APP_PATH}/sys/access/function.do?systemKey=paasos');
				}); 
			}
			function setFuncValueById(xid,isCheck){
				if(isCheck){
					$(document.getElementById(xid)).iCheck('check');
					$(document.getElementById(xid)).attr('checked','checked');
				}else{
					$(document.getElementById(xid)).iCheck('uncheck');
					$(document.getElementById(xid)).removeAttr('checked');
				}
			}
			function setFuncValueByClass(className,isCheck){
				if(isCheck){
					$(className).iCheck('check');
					$(className).attr('checked','checked');
				}else{
					$(className).iCheck('uncheck');
					$(className).removeAttr('checked');
				}
			}
			
			function onRoleChange(obj){
				var role = $(obj).find("option:selected");
				//$("#roleName").html(role.text());
				if(role.val()==""){
				$("#EditRoleUser").hide();
				$("#DelRoleUser").hide();
				$("#funcsEdit").hide();
				$("#SaveBtn").hide();
				}else{
					$("#EditRoleUser").show();
					$("#DelRoleUser").show();
					$("#funcsEdit").show();
					$("#SaveBtn").show();
				}
				$("#roleDesc").html(role.attr("data-desc"));
				var load = common.loading();
				var url = "${WEB_APP_PATH}/sys/access/rolefunc.do?roleId="+role.val();
				$.getJSON(url,function(json){
					load.close();
					if(json.code==0){
						setFuncValueByClass('.funcCla',false); 
						if(json.data.funcs!=null && json.data.funcs.length>0){
							for(var i=0;i<json.data.funcs.length;i++){
								setFuncValueById(json.data.funcs[i].funcId,true);
							}
						}
						isSelectAllCheck();
						var dd   = $("#userlist");
						var html = "";
						 $("#userlist").empty();
						 if(role.val()==""){
							 html+="<span>"+"所有人员"+"</span>";
							 $(html).appendTo(dd);
						 }else{
						  if( json.data.role.users.length>0)
				             {
				               for(var i=0;i<json.data.role.users.length;i++)
				               {
				                html+="<span>"+json.data.role.users[i].userName+";"+"</span>";
				               }
				               $(html).appendTo(dd);
				             }
						  }
					}else{
						common.alert(json.msg);
					}
				});
			}
			function isSelectAllCheck(){
				var xDiv = $("div[id^='module_']");
				var xSel = [];
				for(var i=0;i<xDiv.length;i++){
					var item = xDiv[i];
					xSel[i] = isSelectModuleCheck(item);
				}
				setFuncValueById("sel_chk_all",false);
				for(var i=0;i<xSel.length;i++){
					if(i<xSel.length-1){
						if(!xSel[i]){ 
							return false;
						}
					}else{
						if(xSel[i]){
							setFuncValueById("sel_chk_all",true);
							return true;
						}
					}
				}
			}
			function isSelectModuleCheck(item){
				var checkboxs = $(item).find("input");
				for(var j=0;j<checkboxs.length;j++){
					if(j==0){
						setFuncValueByClass(checkboxs[0],false);
					}else {
						if(j==checkboxs.length-1){
							if($(checkboxs[j]).is(":checked")){
								setFuncValueByClass(checkboxs[0],true);
								return true;
							}else{
								return false;
							}
						} else {
							if(!($(checkboxs[j]).is(":checked"))){
								return false;
							} 
						}
					}
				}
			}
			function onSave(){
				var form = $("#roleFunctionForm");
		    	var url = form.attr("action");
		    	var params = form.serialize();
			    var loading = top.common.loading(); 
			    	$.getJSON(url,params,function(json){ 
		    			loading.close();
			    		if(json.code==0){ 
			    			top.common.tips("角色权限保存成功",1);
			    		}else{ 
			    			top.common.tips(json.msg);
			    		}
			    	});
			}
			
			
			function onRoleDel(){
						var data = {};
						data.roleId = $("#roleSelect").val();
						common.confirm("你确定删除该角色信息吗？",function(isOk){
							if(isOk){ 
								var url = "${ WEB_APP_PATH }/sys/access/role/delete.do";
								ajaxQuery(url,data);
							}
						});
				}
			function ajaxQuery(url,data){
				var load = common.loading();
				$.post(url,data,function(json){
					if(json.code == 0){
						common.tips("操作成功",1,function(){
								var formalurl = location.href;
								common.goto(formalurl);
							});
					
					}else{
						load.close();
		    			common.tips(json.msg);
					}
				},'json');
			}
		</script>
	</template:replace>
</template:include>