<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/WEB-INF/view/resource/base.jsp" nav-left="mysql">
	<template:replace name="title">
		PaaSOS--资源服务
	</template:replace>
	<template:replace name="content-path">
		<em>&gt;</em>
		<p class="app_a"> <a href="${WEB_APP_PATH}/resource/mysql/index.do">数据库</a> </p>
		<em>&gt;</em>
		<p class="app_a">修改数据库实例</p>
	</template:replace>
	<template:replace name="content-body">
		<div class="plate">
            <div class="project_r">
                <!--修改数据库实例开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="javascript:void(0);">
                                <i class="icons ico_title_list mr5"></i>修改数据库实例
                            </a>
                        </h3>

                        <div class="title_line"></div>
                    </div>
                    <div class="r_con p10_0">
                        <div class="form_control p20">
                            <div class="form_block">
                                <label>实例名称</label>
                                <input type="text" name="instanceName" value="${mysqlInfo.instanceName }"><em>*</em>
                            </div>
                            <div class="form_block">
                                <label>版本</label>
                                <div class="specList">
                                    <ul class="u-flavor" id="version">
                                    </ul>
                                </div>
                            </div>
                            <div class="form_block">
                                <label>环境类别</label>
                                <ul class="u-flavor" id="envType">
                                	<c:forEach items="${envTypes }" var="item">
	                                	<li data="${item.key}" class="${mysqlInfo.envType eq item.key ? 'selected' : '' }">${item.value}</li>	                                
	                                </c:forEach>
                                </ul>
                            </div>
                            <div class="form_block">
                                <label>服务模式</label>
                                <div class="specList">
                                    <ul class="u-flavor" id="mysqlType">
                                        <!--Regular list-->
                                        <li class="${mysqlInfo.mysqlType eq 'mysql' ? 'selected' : '' }" data="mysql">单机</li>
<%--                                         <li class="${mysqlInfo.mysqlType eq 'mysqlShare' ? 'selected' : '' }" data="mysqlShare">共享</li> --%>
                                        <li class="${mysqlInfo.mysqlType eq 'mysqlCluster' ? 'selected' : '' }" data="mysqlCluster">集群</li>
                                    </ul>
                                    <div class="jq_set" style="display:none;" id="mysqlTypeAttach">
                                        <span class="mr5"><input class="w100" name="mysqlTypeAttachMain" type="text" value="1"> 主</span>
                                        <span class="mr10"><input class="w100" name="mysqlTypeAttachBackup" type="text" value="1"> 从</span>
                                    </div>
                                </div>
                            </div>
                            <div class="form_block">
                                <label>内存</label>
                                <div class="specList">
                                    <ul class="u-flavor" id="mysqlMemory">
                                    </ul>
                                </div>
                            </div>
                            <div class="form_block">
                                <label>存储空间</label>
                                <div class="specList">
                                    <ul class="u-flavor" id="mysqlStorage">
                                    </ul>
                                </div>
                            </div>
                            <div class="form_block">
                                <label>描述信息</label>
                                <textarea rows="4" cols="35" id="resDesc">${mysqlInfo.resDesc}</textarea>
                            </div>
                            <div class="form_block">
                                <label>租户组织</label>
                                <input type="hidden" name="tenantId" value="${tenantId}" />
                                ${tenantName}
                            </div>
                            <div class="form_block mt10">
                                <label>&nbsp;</label>
                                <button class="btn btn-default btn-yellow f16  mt10" onclick="updatePaasMysql()"><i class="ico_check"></i>保存
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--修改数据库实例结束-->
            </div>
        </div>		
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script>
		var userId = '${userId}';
		var tenantId = '${tenantId}';
		
		$(function(){
			var options= '${options}';
			if(options){
				try{
					var jsonOptions = JSON.parse(options);
					var memOptions = jsonOptions.memory;
					var storageOptions = jsonOptions.storage;
					if(memOptions&& memOptions.length){
						for(var i=0; i<memOptions.length;i++){
							$("#mysqlMemory").append("<li>"+memOptions[i]+"</li>");
						}
					}
					if(storageOptions&& storageOptions.length){
						for(var i=0; i<storageOptions.length;i++){
							$("#mysqlStorage").append("<li>"+storageOptions[i]+"</li>");
						}
					}
					var versionOptions = jsonOptions.version;
					if(versionOptions && versionOptions.length){
						for(var i=0; i<versionOptions.length;i++){
							$("#version").append("<li>"+versionOptions[i]+"</li>");
						}
					}
				}catch(e){
					common.tips("获取资源配额失败");
				}
			}
			$("#mysqlType").find(">li").bind("click",function(){
				var ob = $(this);
				$("#mysqlType").find(">li").each(function(){$(this).removeAttr("class");})
				ob.attr("class","selected");
				if(ob.text()=='集群'){
					$("#mysqlTypeAttach").removeAttr("style");
				}else{
					$("#mysqlTypeAttach").css("display","none");
				}
			});
			bindLiEleClickByParent('envType');
			renderSelectParams('${mysqlInfo.version}','version');
			renderSelectParams('${mysqlInfo.allocateMemInfo}','mysqlMemory');
			renderSelectParams('${mysqlInfo.allocateStorageInfo}','mysqlStorage');
		});
		
		function renderSelectParams(selectValue,parentEleId){
			var found = false;
			var parentEle = $("#"+parentEleId);
			parentEle.find(">li").each(function(){
				if($(this).text()==selectValue){
					$(this).attr("class","selected");
					found = true;
				}
			});
			if(!found){
				parentEle.append("<li class='selected'>"+selectValue+"</li>");
			}
			bindLiEleClickByParent(parentEleId);
		}
		
		function bindLiEleClickByParent(parentId){
			var parentEle= $("#"+parentId);
			parentEle.find(">li").bind("click",function(){
				var ob = $(this);
				parentEle.find(">li").each(function(){	$(this).removeAttr("class");})
				ob.attr("class","selected");
			});
		}
		
		function updatePaasMysql(){
			var data ={};
			data.mysqlId = '${mysqlInfo.mysqlId}';
			data.userId = userId;
			var instanceName = $.trim($("input[name='instanceName']").val());
			if(instanceName == ''){
				common.tips("实例名称为空");
				return false;
			}
			data.instanceName = instanceName;
			var version = $("#version").find(">li.selected").text();
			data.version = version;
			var mysqlType = $("#mysqlType").find(">li.selected").attr("data");
			data.type = mysqlType;
			var mysqlTypeAttach={};
			if(mysqlType=='mysqlCluster'){
				mysqlTypeAttach.main = $("input[name='mysqlTypeAttachMain']").val();
				mysqlTypeAttach.back = $("inpuat[name='mysqlTypeAttachBackup']").val();
			}
			data.TypeAttach = mysqlTypeAttach;
			var tenantId = $("input[name='tenantId']").val();
			data.tenantId = tenantId;
			var mysqlMemory = $("#mysqlMemory").find(">li.selected").text();
			data.memory = mysqlMemory;
			var mysqlStorage = $("#mysqlStorage").find(">li.selected").text();
			data.storage = mysqlStorage;
			var envType = $("#envType").find(">li.selected").attr("data");
			data.envType = envType;
			data.resDesc = $("#resDesc").val();
			if(mysqlType=='mysql'||mysqlType=='mysqlCluster'){
				if(mysqlMemory==''|| mysqlStorage==''){
					common.tips("没有选择内存和存储空间的配额大小");
					return false;
				}
			}
			var url = "${WEB_APP_PATH}/resource/mysql/saveUpdate.do";
			var load = common.loading();
			$.post(url,data,function(json){
				load.close();
				if(json.code==0){
					common.goto("${WEB_APP_PATH}/resource/mysql/index.do");
				}else{
					common.tips("修改数据库实例失败"+json.msg);
				}
			});
		}
	</script>
	</template:replace>
</template:include>