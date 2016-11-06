<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<style>

</style>
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
            <p class="app_a"><a href="#nogo">节点详情</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
                <!-- <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="pass_creat_yy.html" class="btn btn-default btn-oranger f16 mr20"> 编辑 </a>
                    <em class="mr10 f14">|</em>
                    <span class="f14">在该页面您可以部署容器应用，查看已经部署的应用列表，启动、停止、配置应用参数。<a href="#" class="btn btn-link yellow">如何创建应用？
                        &gt;</a></span>
                </div> -->
                <!--项目概述开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line ">
                            <a href="detail.do?name=${param.name }"> <i class="icons cy_ico mr5"></i>节点信息</a>
                        </h3>
<!--                         <h3 class="f14 h3_color h3_btm_line blue"> -->
<!--                             <a href="javascript:void(0);" onclick="showInstanceList(this);">实例信息</a> -->
<!--                         </h3> -->

                        <div class="title_line"></div>
                    </div>
                    <div class="title_tab title_tab_item title_tab_item_pas text-center">
                        <ul>
                            <li>
                                <h5 class="f14">
                                    <a class="${!(param.tab=='base' || empty param.tab) ? '' : 'active' } tab-handler" index="1" href="detail.do?name=${param.name }">基本信息</a>
                                </h5>
                            </li>
                            <li>
                                <h5 class="f14">
                                    <a class="${param.tab=='instance' ? 'active' : ''} tab-handler" href="#nogo" onclick="showInstanceList(this);" index="2">实例信息</a></h5>
                            </li>
                            <li>
                                <h5 class="f14">
                                    <a class="${param.tab=='monitor' ? 'active' : ''} tab-handler" href="#nogo" onclick="showMonitorInfo(this);" index="3">监控信息</a></h5>
                            </li>
                        </ul>
                        <div class="title_line"></div>
                    </div>
                    <!--来源1-->
                    <div class="r_con p10_0">
                        <div class="details_nr">
                            <div class="form_control p20 form_details" id="detailsDiv">
                                <c:if test="${empty param.tab || param.tab=='detail' }">
                                	<c:import url="detail.jsp"></c:import>
                                </c:if>
                                <c:if test="${param.tab=='instance' }">
                                	<c:import url="instanceList.jsp"></c:import>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <!--来源1结束-->
                </div>
                <!--项目概述结束-->
            </div>

        </div>
        <script>
        function showInstanceList(tar){
        	location.href = "detail.do?tab=instance&name=${param.name}";
        	/* var name = '${param.name}';
        	$("h3.red").removeClass("red");
        	$(tar).parent().removeClass("blue").addClass("red");
        	$("#detailsDiv").load("instanceList.do",{name:name}); */
        }
        var nodeIp = '${node.ip}';
        function showMonitorInfo(tar){
        	var name = '${param.name}';
        	$("h3.tab-handler").removeClass("active");
        	$(tar).addClass("active");
        	
        	var param = [{
				endpoint : nodeIp,
				key : nodeIp
			}];
        	common.ajaxLoading($("#detailsDiv"));
        	$("#detailsDiv").load("${ WEB_APP_PATH }/project/monitor/view.do",{
        		source : "node",
        		isInfo : true,
        		data : JSON.stringify(param)
        	});
        }
        </script>
	</template:replace>
	
</template:include>