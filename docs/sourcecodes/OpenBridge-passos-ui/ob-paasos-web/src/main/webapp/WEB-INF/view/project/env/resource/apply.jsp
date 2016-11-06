<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="title">
		申请资源
	</template:replace>
	<template:replace name="body">
		<style type="text/css">
			.title01 {
			    font-size: 20px;
			    padding: 8px;
			}
			.form_block label.w80 {
			 	float: left;
			 	padding-left: 35px;
			 	vertical-align: top;
    			line-height: 35px;
    			width:80px;
			}
			.w150{
				width:150px;
				padding: 3px 0;
			}
			.table {
				width: 90%;
			} 
			.table th,.table td{
				padding: 8px;
				border:1px solid #f1f1f1;
			}
		</style>
		<div class="row">
			<div class="col-xs-12">
			 	<div class="page-header">
					<h4 class="pull-left title01">
						<c:if test="${ env.envType eq 'dev' }">
							开发环境
						</c:if> 
						<c:if test="${ env.envType eq 'test' }">
							测试环境
						</c:if> 
						<c:if test="${ env.envType eq 'live' }">
							生产环境
						</c:if>
						>> 申请资源 >>
						<small> 
							${ resource.name }
						</small>
					</h4>
				</div>
			 
				<div class="clearfix"></div>
				<form class="form-horizontal" style="margin-top:20px;" role="form" id="bindForm"
					action="${ WEB_APP_PATH }/project/env/resource/save.do?projectId=${project.projectId}&resId=${resource.id}&envId=${env.envId}&source=${project.projectType}"
					method="post">
					
					<div class="title_tab title_tab_item"> 
						<ul>
							<li>
						        <h5 class="f14"><a href="javascript:void(0)" class="active" onclick="toggleResource(this)" data="new">申请新资源</a></h5>
						    </li>
							<li>
								<h5 class="f14"><a href="javascript:void(0)" onclick="toggleResource(this)" data="old">重用旧资源</a></h5>
							</li>
						</ul>  
					</div>
					<input type="hidden" name="applyType" value="new" />
					<div id="newResource" style="float:left;margin-top:15px;width:100%;">
						<c:if test="${resource.id eq 'mysql51' or resource.id eq 'redis' or resource.id eq 'rocket'}">
							<div class="form_block">
								<label class="w80">资源版本</label>
								<div>
									<select name="version" class="w150">
									<c:forEach items="${options.version}" var="versionItem">
										<option value="${versionItem}" >${versionItem}</option>
									</c:forEach>
									</select>
								</div>
							</div> 
							<div class="form_block">
								<label class="w80">内存大小</label>
								<div>
									<select name="memory" class="w150">
									<c:forEach items="${options.memory}" var="memoryItem">
										<option value="${memoryItem}" >${memoryItem}</option>
									</c:forEach>
									</select>
								</div>
							</div>
						</c:if>
						<c:if test="${resource.id eq 'mysql51' or resource.id eq 'nas'}">
							<div class="form_block">
								<label class="w80">存储空间</label>
								<div>
									<select name="storage" class="w150">
									<c:forEach items="${options.storage}" var="storageItem">
										<option value="${storageItem}" >${storageItem}</option>
									</c:forEach>
									</select>
								</div>
							</div>
							<div class="form_block">
								<label class="w80">
									<c:choose>
										<c:when test="${resource.id eq 'mysql51'}">实例名称</c:when>
										<c:otherwise>
											挂载路径
										</c:otherwise>
									</c:choose>
								</label>
								<div>
									<input type="text" name="reourceNewAddition" class="w150" />&nbsp;
									<span style="color:red;">
										<c:choose>
											<c:when test="${resource.id eq 'mysql51'}">*</c:when>
											<c:otherwise>
												建议挂载到/mnt/xxx或/data目录
											</c:otherwise>
										</c:choose>
									</span>
								</div>
							</div>
						</c:if>
					</div>
					<div id="oldResource" style="display:none;float:left;margin-top:15px;width:100%;">
						<div style="padding:0 40px;"></div>
						<c:if test="${resource.id eq 'mysql51' or resource.id eq 'nas'}">
							<div>
								<div class="form_block">
									<label class="w80">
										<c:choose>
											<c:when test="${resource.id eq 'mysql51'}">实例名称</c:when>
											<c:otherwise>
												挂载路径
											</c:otherwise>
										</c:choose>
									</label>
									<div>
										<input type="text" name="reourceOldAddition" class="w150"/>&nbsp;
										<span style="color:red;">
											<c:choose>
												<c:when test="${resource.id eq 'mysql51'}">*</c:when>
												<c:otherwise>
													建议挂载到/mnt/xxx或/data目录
												</c:otherwise>
											</c:choose>
										</span>
									</div>
								</div>
							</div>
						</c:if>
					</div>
					
					<div class="clearfix"></div>
					<div class="form_block" style="margin-top: 5px;">
						<div style="padding-left:35px;">
							<button type="button" onclick="bindResource();" class="btn btn-info" type="button" id="saveServiceDefineBtn">
								<i class="ace-icon fa fa-check bigger-110"></i> 保存
							</button>
							&nbsp; &nbsp; &nbsp;
							<!--设置按钮类型，防止错误触发表单提交事件  -->
							<button class="btn" type="button" onclick="common.goto('${WEB_APP_PATH}/project/env/resource/add.do?projectId=${project.projectId}&envId=${env.envId}')">
								<i class="ace-icon fa fa-undo bigger-110"></i> 返回
							</button>
						</div>
					</div>
				</form>
			 
			 </div>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<script>
			var resId = '${resource.id}';
			function checkParams(){
				var applyType = $("input[name='applyType']").val();
				if(applyType=='new'){
					if(resId=='mysql51'){
						var version = $("select[name='version']").find("option:selected").val();
						var memory = $("select[name='memory']").find("option:selected").val();
						var storage = $("select[name='storage']").find("option:selected").val();
						if(typeof version == 'undefined' || version=='' ||
							typeof memory == 'undefined' || memory==''||
							typeof storage == 'undefined' || storage==''){
							common.tips("资源参数内存、存储大小不正确");
							return false;
						}
						var dbName = $.trim($("input[name='reourceNewAddition']").val());
						var filter = /[a-zA-Z0-9]{3,}/;
						if(!filter.test(dbName)){
							common.tips("数据库实例名称格式不正确，必须为3位及3位以上的字母和数字");
							return false;
						}
					}else if(resId=='redis'||resId=='rocket'){
						var version = $("select[name='version']").find("option:selected").val();
						var memory = $("select[name='memory']").find("option:selected").val();
						if(typeof version == 'undefined' || version==''||typeof memory == 'undefined' || memory==''){
							common.tips("资源参数内存大小不正确");
							return false;
						}
					}else  if(resId=='nas'){
						var storage = $("select[name='storage']").find("option:selected").val();
						if(typeof storage == 'undefined' || storage==''){
							common.tips("资源参数存储大小不正确");
							return false;
						}
						var additionValue = $.trim($("input[name='reourceNewAddition']").val());
						if(additionValue==""){
							common.tips("挂载路径必填");
							return false;
						}
// 						var filter = /\/mnt\/[a-zA-Z0-9]{1,}/;
// 						if(!filter.test(additionValue)){
// 							common.tips("挂载路径格式必须为/mnt/xxx");
// 							return false;
// 						}
					}
				}else{
					if(!$("input[type='radio']:checked")|| !$("input[type='radio']:checked").length){
						common.tips("请选择");
						return false;
					}
					var additionValue = $.trim($("input[name='reourceOldAddition']").val());
					if(resId =='mysql51'){
						var filter = /[a-zA-Z0-9]{3,}/;
						if(!filter.test(additionValue)){
							common.tips("数据库实例名称格式不正确，必须为3位及3位以上的字母和数字");
							return false;
						}
					}
					if(resId =='nas'){
						if(additionValue==""){
							common.tips("挂载路径必填");
							return false;
						}
// 						var filter = /\/mnt\/[a-zA-Z0-9]{1,}/;
// 						if(!filter.test(additionValue)){
// 							common.tips("挂载路径格式必须为/mnt/xxx");
// 							return false;
// 						}
					}
				}
				return true;
			}
			function toggleResource(obj){
				var link= $(obj);
				if(link.attr("class")=='active'){
					return false;
				}
				var resourceType = link.attr("data");
				link.closest("ul").find("a").removeAttr("class");
				link.attr("class","active");
				$("input[name='applyType']").val(resourceType);
				$("#newResource").toggle();
				$("#oldResource").toggle();
				if(resourceType=='old' && $("#oldResource").find("div:first").children().length==0){
					gotoPage();
				}
			}
			function gotoPage(pageNo, pageSize){
				if(typeof pageNo == 'undefined'){
					pageNo=1;
				}
				if(typeof pageSize == 'undefined'){
					pageSize=10;
				}
				$("#oldResource").find("div:first").append("<img src='"+WEB_APP_PATH+"/assets/images/loading.gif' />&nbsp; 正在加载原来申请的资源...");
				$.get("${WEB_APP_PATH}/project/env/rest/page.do",{'resouceType':'${resource.id}',
					'envType':'${ env.envType}','source':'${project.projectType}',"pageNo":pageNo,"pageSize":pageSize},function(json){
					$("#oldResource").find("div:first").empty();
					if(json.code==0){
						var data= json.data;
						var size = data.pageSize;
						var num = data.pageNo;
						var oldResources = data.data;
						if(oldResources && oldResources.length){
							var recordCount= data.pageCount;
							var table=$("<table class='table_ob'><thead><th style='width:50px;'>选择</th><th style='width:150px;'>资源名称</th><th style='width:180px;'>资源参数</th><th>资源描述</th></thead></table>");
							var tbody= $("<tbody></tbody>");
							for(var j=0;j<oldResources.length;j++){
								var resource= oldResources[j];
								var tr = $("<tr></tr>");
								var cell1 = $("<td><input type='radio' name='oldResource' value='"+resource.id+"' /></td>");
								tr.append(cell1);
								var cell2 = $("<td>"+resource.name+"</td>");
								tr.append(cell2);
								var cell3 = $("<td></td>");
								if(resId=='mysql51'){
									cell3.append("内存:"+ resource.memory +" 存储空间"+resource.storage);
								}else if(resId=='redis'||resId=='rocket'){
									cell3.append("内存:"+ resource.memory );
								}else  if(resId=='nas'){
									cell3.append("存储空间"+resource.storage);
								}
								tr.append(cell3);
								var cell4 = $("<td>"+(resource.desc==null ?'':resource.desc)+"</td>");
								tr.append(cell4);
								tbody.append(tr);
							}
							table.append(tbody);
							var navigate = $("<div class='pull-right'>当前第"+(num+1)+"页/总共"+recordCount+"页 </div>");
							if(num>1){
								navigate.append("<a href='javascript:void(0)' onclick='gotoPage("+(num-1)+","+size+")'>上一页</a>");
							}
							if(num < recordCount-1){
								navigate.append("<a href='javascript:void(0)' onclick='gotoPage("+(num+1)+","+size+")'>下一页</a>");
							}
							$("#oldResource").find("div:first").append(table);
						}else{
							$("input[name='applyType']").val('new');
							$("#newResource").toggle();
							$("#oldResource").toggle();
							$(".title_tab_item").find("a").removeAttr("class");
							$(".title_tab_item").find("a:first").attr("class","active");
							common.tips("没有已申请的该资源可用，请申请新资源");
						}
					}else{
						$("input[name='applyType']").val('new');
						$("#newResource").toggle();
						$("#oldResource").toggle();
						$(".title_tab_item").find("a").removeAttr("class");
						$(".title_tab_item").find("a:first").attr("class","active");
						common.tips("暂时查询不到已申请的资源");
					}
				});
			}
			function bindResource(serviceId){
				if(checkParams()){
					var url = $("#bindForm").attr("action");
					var data = $("#bindForm").serialize();
			    	var loading = top.common.loading(); 
					$.getJSON(url,data,function(json){
		    			loading.close();
						if(json.code == 0){
							top.common.tips("成功申请资源",1,function(){
			    				frameElement.callback();
			    			});
						} else{
							top.common.alert(json.msg);
						}
					});
				}
			}
		</script>
	</template:replace>
</template:include>