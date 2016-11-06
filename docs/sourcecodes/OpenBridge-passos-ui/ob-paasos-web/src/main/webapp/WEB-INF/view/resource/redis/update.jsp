<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/WEB-INF/view/resource/base.jsp" nav-left="redis">
	<template:replace name="title">
		PaaSOS--资源服务
	</template:replace>
	<template:replace name="content-path">
		<em>&gt;</em>
		<p class="app_a"> <a href="${WEB_APP_PATH}/resource/redis/index.do">高速缓存 </a> </p>
		<em>&gt;</em>
		<p class="app_a">修改redis实例</p>
	</template:replace>
	<template:replace name="content-body">
		<div class="plate">
            <div class="project_r">
                <!--修改redis实例开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="javascript:void(0);">
                                <i class="icons ico_title_list mr5"></i>修改redis实例
                            </a>
                        </h3>

                        <div class="title_line"></div>
                    </div>
                    <div class="r_con p10_0">
                        <div class="form_control p20">
                            <div class="form_block">
                                <label>实例名称</label>
                                <input type="text" name="redisName" value="${redisInfo.redisName }"><em>*</em>
                            </div>
                            <div class="form_block">
                                <label>版本</label>
                                <select class="version" style="width:272px;">
                                </select>
                            </div>
                            <div class="form_block">
                                <label>租户组织</label>
                                <input type="hidden" name="tenantId" value="${tenantId}"/>
                                <input type="text" name="tenantName" value="${tenantName }" readonly="readonly">
                            </div>
                            <div class="form_block">
                                <label>内存</label>
                                <div class="specList">
                                    <ul class="u-flavor" id="redisMemory">
                                    </ul>
                                </div>
                            </div>
                            <div class="form_block">
                                <label>环境类型</label>
                                <ul class="u-flavor" id="envType">
                                	<c:forEach items="${envTypes }" var="item">
	                                	<li data="${item.key}" class="${redisInfo.envType eq item.key ? 'selected' : '' }">${item.value}</li>	                                
	                                </c:forEach>
                                </ul>
                            </div>
                            <div class="form_block mt10">
                                <label>&nbsp;</label>
                                <button class="btn btn-default btn-yellow f16  mt10" onclick="addPaasRedis()"><i class="ico_check"></i>创 建
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--修改redis实例结束-->
            </div>
        </div>		
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script>
		var userId = '${userId}';
		var tenantId = '${tenantId}';
	
		$(function(){
			queryResourceOptions();
			$("#envType").find(">li").bind("click",function(){
				var ob = $(this);
				$("#envType").find(">li").each(function(){	$(this).removeAttr("class");})
				ob.attr("class","selected");
			});
		});
		
		function queryResourceOptions(){
			var url = "${WEB_APP_PATH}/resource/redis/options.do";
			var load = common.loading();
			var data ={};
			data.paasOsUserId = userId;
			data.paasOsTenantId= tenantId;
			$.post(url,data,function(json){
				load.close();
				if(json.code==0){
					var memOptions = json.data.memory;
					if(memOptions&& memOptions.length){
						for(var i=0; i<memOptions.length;i++){
							$("#redisMemory").append("<li>"+memOptions[i]+"</li>");
						}
						//获取可以选择的申请参数
					}
					var versionOptions = json.data.version;
					if(versionOptions&& versionOptions.length){
						for(var i=0; i<versionOptions.length;i++){
							$(".version").append("<option value='"+versionOptions[i]+"'>"+versionOptions[i]+"</option>");
						}
					}
					renderSelectParams()
				}else{
					common.tips("获取资源配额失败");
				}
			});
		}
		
		function renderSelectParams(){
			var memory = '${redisInfo.allocateMemInfo}';
			var foundMemory = false;
			$("#redisMemory").find(">li").each(function(){
				if($(this).text()==memory){
					$(this).attr("class","selected");
					foundMemory = true;
				}
			});
			if(!foundMemory){
				$("#redisMemory").append("<li class='selected'>"+memory+"</li>");
			}
			$("#redisMemory").find(">li").bind("click",function(){
				var ob = $(this);
				$("#redisMemory").find(">li").each(function(){	$(this).removeAttr("class");})
				ob.attr("class","selected");
			});
		}
		
		function addPaasRedis(){
			var data ={};
			data.redisId = '${redisInfo.redisId}';
			data.userId = userId;
			var instanceName = $.trim($("input[name='redisName']").val());
			if(instanceName == ''){
				common.tips("实例名称为空");
				return false;
			}
			data.instanceName = instanceName;
			var version = $(".version").find("option:selected").val();
			data.version = version;
			var tenantId = $("input[name='tenantId']").val();
			data.tenantId = tenantId;
			var mqMemory = $("#redisMemory").find(">li.selected").text();
			data.memory = mqMemory;
			var envType = $("#envType").find(">li.selected").attr("data");
			data.envType = envType;
			var tenantName = $.trim($("input[name='tenantName']").val());
			if(tenantName==''){
				common.tips("租户为空");
				return false;
			}
			if(mqMemory==''){
				common.tips("没有选择内存配额大小");
				return false;
			}
			var url = "${WEB_APP_PATH}/resource/redis/saveUpdate.do";
			var load = common.loading();
			$.post(url,data,function(json){
				load.close();
				if(json.code==0){
					common.goto("${WEB_APP_PATH}/resource/redis/index.do");
				}else{
					common.tips("修改redis实例失败"+json.msg);
				}
			});
		}
	</script>
	</template:replace>
</template:include>