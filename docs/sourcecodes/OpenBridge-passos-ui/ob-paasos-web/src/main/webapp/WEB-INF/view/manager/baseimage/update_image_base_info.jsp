<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="baseimage">
	<template:replace name="title">
		节点管理
	</template:replace>
	<template:replace name="content-body">
	<style>
.mb10{
	position: relative;
}
.mb10 .file-selector{
    position: absolute;
    left: 0;
    width: 75px;
    padding: 5px 12px;
}
#ports tr th:last-child,#ports tr td:last-child{
	text-align:right;
	width: 40px;
}
#tenantsDiv input,#toAll{
	float:left;
}
.tenant label{
	padding-left:10px!important;
}
#tenantsDiv{
	padding-left : 200px;
}
em{
	padding: 0 10px;
}
.tab-handler{
	cursor : pointer;
} 
</style>
        <div class="app_name">
            <a href="proiect_list.html"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="../tenant/list.do">环境管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../baseimage/list.do">基础镜像</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="#nogo">${empty build.id ? '创建' : '编辑'}镜像</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
                <div class="r_block p20">
                    <div class="r_block">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line">${empty build.id ? '<i class="icons add_ico_blue mr5"></i>创建' : '编辑'}镜像</h3>
<!--                             <h3 class="f14 h3_color h3_btm_line tab-handler red" index="1"><a href="#nogo">基本信息</a></h3> -->
<!--                             <h3 class="f14 h3_color h3_btm_line tab-handler" index="2"><a href="#nogo">其他信息</a></h3> -->
                            <div class="title_line"></div>
                        </div>
                        <div class="r_con p10_0">
                            <div class="form_control p20" style="padding-top: 0px;">
                            <form id="createForm" action="updateBaseInfo.do" method="post" enctype="multipart/form-data">
                            	<input type="hidden" name="id"  value="${build.id }"/>
	                            	<div class="form_block">
	                                    <label>镜像图标</label>
	                                    <p id="appLogo" path='${build.iconPath}'/>
	                                </div>
	                                <div class="form_block">
	                                    <label>镜像名称</label>
	                                    <input disabled type="text" name="name" value="${build.name }"><em style="color: red;">*</em>只能包含英文字母和数字
	                                </div>
	                                <div class="form_block">
	                                    <label>镜像版本</label>
	                                    <input disabled type="text" name="version" value="${build.version }"><em style="color: red;">*</em>
	                                </div>
	                                <div class="form_block">
	                                    <label>Dockerfile</label>
	                                    <textarea style="width: 588px;height: 150px;" name="dockerfile" required>${build.dockerfile }</textarea><em style="">*</em>
	                                </div>
	                                <div class="form_block">
	                                    <label>镜像描述</label>
	                                    <textarea  style="width: 588px;height: 150px;" name="description">${build.description }</textarea>
	                                </div>
	                                
	                                <div class="form_block">
	                                    <label>授权组织</label>
	                                    <p style="height:30px;" class="tenant">
	                                    	<input type="checkbox" id="toAll"/><label for="toAll">任何组织</label>
	                                    </p>
	                                    <div class="tenant" id="tenantsDiv" >
	                                    
	                                    </div>
	                                </div>
                                <div class="form_block mt10">
                                    <label>&nbsp;</label>
                                    <button type="button" id="submitBtn" class="btn btn-default btn-yellow f16  mt10" onclick="saveOrUpdate()"><i class="ico_check"></i>保存</button>
                                </div>
                            </form>
                            </div>
                        </div>
                    </div>
                <!--创建镜像结束-->
            </div>
        </div>
        <%@ include file="/common/include/fileupload_header.jsp"%>
        <%@ include file="/common/include/validation_header.jsp"%>
        <script>
        function saveOrUpdate(){
        	//校验
        	if(!$("#createForm").valid())	return false;
        	var param = $("#createForm").serializeArray();
        	var data = {};
        	$.each(param,function(i,v){
        		data[v.name] = v.value
        	})
        	//获取图片信息
        	data.iconPath = $("#appLogo").data("data").filePath;
        	//授权信息
        	var tenantIds = '';
        	if($("#toAll").attr("checked")){
        		tenantIds = $("#toAll").attr("id");
        	}else{
        		var foo = [];
        		$("#tenantsDiv input[type='checkbox']:checked").each(function(){
        			foo.push($(this).attr("id"));
        		})
        		tenantIds = foo.join(",");
        	}
        	data.tenantIds = tenantIds;
        	$("#createForm").append("<input type='hidden' name='tenantIds' value='"+tenantIds+"'/>");
//         	$("#createForm").submit();
			$("#submitBtn").hide();
			var loadObj = common.loading();
			$.post($("#createForm").attr("action"),data,function(json){
				loadObj.close();
				if(json.code==0){
					common.tips("保存成功！",1,function(){
						location.href = "list.do";
					},1000);
   	            }else{
   	            	common.alert("保存失败！"+(json.msg||''),2);
   	            	$("#submitBtn").show();
   	            }
			})
        	return false;
        }
        
        __tenants=[];
       	
        $(function(){
        	$("#createForm").validate({
        		rules : {
        			dockerfile:{
        				notBlank : true
        			}
        		}
        	});
        	var fileType = '${build.fileType}';
        	if(fileType && fileType=='tar.gz'){
        		$(".specList :radio").eq(1).attr("checked","checked");
        	}
        	initImage($("#appLogo"),$("#appLogo").attr("path"));
        	var tenantIds = '${build.tenantIds}';
        	$("#toAll").click(function(){
        		if($(this).attr("checked")){
        			$("#tenantsDiv").hide();
        		}else{
        			$("#tenantsDiv").show();
        		}
        	});
        	if('${param.id}'=='' || tenantIds==$("#toAll").attr("id")){
        		$("#toAll").attr("checked",true);
       			$("#tenantsDiv").hide();
        	}
        	$.get(WEB_APP_PATH+"/manager/tenant/getTenants.do",{},function(data){
           		__tenants = data;
           		var bar = tenantIds.split(",");
           		var str='';
            	$.each(__tenants,function(i,v){
            		var isChecked = '';
            		if($.inArray(v.tenantId,bar)!=-1){
            			isChecked = ' checked="checked" ';
            		}
            		str += '<input type="checkbox" id="'+v.tenantId+'"'+isChecked+'/><label for="'+v.tenantId+'">'+v.tenantName+'</label>';
            	})
            	$("#tenantsDiv").html(str);
           	});
        })
        </script>
	</template:replace>
</template:include>