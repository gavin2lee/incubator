<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="tenant">
	<template:replace name="title">
		组织管理
	</template:replace>
	<template:replace name="content-body">
	<style>
	.specList{
		line-height:24px;
	}
	</style>
	<div class="app_name">
            <a href="resource_plan.html"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="../tenant/list.do">环境管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../tenant/list.do">组织管理</a></p>
            <em>&gt;</em>

            <p class="app_a">组织资源</p>
        </div>
        <div class="plate">
            <div class="project_r">
                <!--项目概述开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="#">
                                <i class="icons ico_title_list mr5"></i>组织资源
                            </a>
                        </h3>
                        <div class="title_line"></div>
                    </div>
                    <div class="r_con p10_0">
                        <div class="form_control p20">
                            <div class="form_block">
                                <label>环境</label>
                                <div class="specList">
                                    <ul class="u-flavor" id="envUl">
                                        <c:forEach items="${envs }" var="env">
	                                        <li code="${env.key }">${env.value }</li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                            <div class="form_block" id="nodesDiv">
                                <label>可用主机</label>
                                <div class="specList">
                                <c:if test="${empty nodes }">无</c:if>
                                    <ul>
                                    <c:forEach items="${nodes }" var="node">
                                    
                                        <li class="specCard" envtype="${node.envType }">
                                            <div class="spec_up">
                                                <p><a href="${WEB_APP_PATH }/manager/node/detail.do?name=${node.name }">${node.name }</a></p>
                                            </div>
                                            <div class="spec_down">
                                                <p><span>${empty node.cpu ? 0 : node.cpu} 核</span> CPU</p>

                                                <p><span>${empty node.ram ? 0 : node.ram} MB</span> 内存</p>

                                            </div>
                                            <!-- <div class="spec_btm">
                                                <p class="text-center"><input type="text" class="spinnerExample"/>
                                                </p>
                                            </div> -->
                                        </li>
                                    </c:forEach>
                                    </ul>
                                </div>
                            </div>
                            <div class="form_block">
                                <label>可用镜像</label>
                                <div class="specList">
                                <c:if test="${empty builds }">无</c:if>
                                    <ul>
                                    <c:forEach items="${builds }" var="build">
                                        <li class="specCard specCard_sl">
                                            <div class="spec_up">
                                                <i style="margin: 10px 0; display: inline-block;"><img src="${WEB_APP_PATH }/paas/file/view.do?filePath=${build.iconPath}" alt="${build.name }" width="40" height="40"></i>
                                            </div>
                                            <div class="spec_down">
                                                <p><a href="${WEB_APP_PATH }/manager/baseimage/view.do?id=${build.id }">${build.name}</a></p>
<%--                                                 <p><a href="${WEB_APP_PATH }/manager/baseimage/download.do?id=${build.id }" title="下载">${build.fileName}</a></p> --%>
                                                <p>${build.version }</p>
                                            </div>
                                        </li>
                                    </c:forEach>
                                    </ul>
                                </div>
                            </div>
                            <!-- <div class="form_block mt10">
                                <label>&nbsp;</label>
                                <button class="btn btn-default btn-yellow f16  mt10"><i class="ico_check"></i>确 定
                                </button>
                            </div> -->
                        </div>
                    </div>
                </div>
                <!--项目概述结束-->
            </div>
		</div>
		<script>
		$(function(){
			$("#envUl li").click(function(){
				$("#envUl li").removeClass("selected");
				$(this).addClass("selected");
				var code = $(this).attr("code");
				$("#nodesDiv li").each(function(){
					var envtype = $(this).attr("envtype");
					if(envtype == code){
						$(this).show();
					}else{
						$(this).hide();
					}
				})
			});
			$("#envUl li:first").click();
			/* $("li.specCard").click(function(){
				if($(this).hasClass("checked")){
					$(this).parent().find("li").removeClass("checked");
				}else{
					$(this).addClass("checked");
				}
			}); */
		})
		</script>
	</template:replace>
</template:include>