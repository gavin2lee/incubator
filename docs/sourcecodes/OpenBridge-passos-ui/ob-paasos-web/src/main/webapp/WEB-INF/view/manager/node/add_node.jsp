<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="node">
	<template:replace name="title">
		节点管理
	</template:replace>
	<template:replace name="content-body">
        <div class="app_name">
            <a href="resource_plan.html"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="../tenant/list.do">环境管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../node/list.do">节点管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="#nogo">${empty node.id ? '添加' : '编辑'}节点</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
                <div class="r_block p20">
                    <div class="r_block">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line">${empty node.id ? '<i class="icons add_ico_blue mr5"></i>添加' : '编辑'}节点</h3>

                            <div class="title_line"></div>
                        </div>
                        <div class="r_con p10_0">
                            <div class="form_control p20">
                            	<div class="form_block">
                                    <label>节点名称</label>
                                    <input type="text" id="name" value="${node.name }"><em>*</em>
                                </div>
                                <div class="form_block">
                                    <label>操作系统</label>
                                    <div class="specList">
                                        <ul class="u-flavor">
                                            <!--Regular list-->
                                            <li class="selected">CentOS 7</li>
<!--                                             <li class="">系统2</li> -->
                                        </ul>
                                    </div>
                                </div>
                                <div class="form_block">
                                    <label>环境类型</label>
                                    <div class="specList">
                                        <ul class="u-flavor env-ul">
                                            <c:forEach items="${envs }" var="env" varStatus="st">
                                            <li code="${env.key }">${env.value }</li>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </div>
                                <div class="form_block">
                                    <label>租户关联</label>
                                    <select style="width: 335px;" id="orgId">
                                        <option value="">--请选择--</option>
                                        <c:forEach items="${tenants }" var="tenant">
	                                        <option value="${tenant.tenantId }">${tenant.tenantName }</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_block mt10">
                                    <label>&nbsp;</label>
                                    <button class="btn btn-default btn-yellow f16  mt10" onclick="saveOrUpdate();"><i class="ico_check"></i>保存
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--创建应用部署结束-->
            </div>
        </div>
        <script>
        function saveOrUpdate(){
        	var params = {
        			name : $("#name").val(),
        			envType : $(".env-ul li.selected").attr("code"),
        			orgId : $("#orgId").val()
        	};
        	$.post("saveOrUpdate.do",params,function(data){
        		if(data.code==1){
    				common.alert("保存失败！"+(data.msg||''),2);
    			}else{
    				common.tips("保存成功！",1,function(){
    					location.href = "list.do";
    				},1000);
    			}
        	});
        }
        $(function(){
        	$("ul.u-flavor li").click(function(){
        		$(this).addClass("selected");
        		$(this).siblings().removeClass("selected");
        	});
        	var envType = '${node.envType}';
        	if(envType){
        		$("ul.env-ul li").each(function(){
        			if($(this).attr("code")==envType){
        				$(this).addClass("selected");
        			}
        		});
        	}else{
        		$("ul.env-ul li").first().addClass("selected");
        	}
        })
        </script>
	</template:replace>
</template:include>