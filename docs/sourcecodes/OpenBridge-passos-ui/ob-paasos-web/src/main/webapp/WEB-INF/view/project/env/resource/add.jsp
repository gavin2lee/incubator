<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="title">
		申请资源
	</template:replace>
	<template:replace name="body">
 		<%--
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
				申请资源
			</h4>
		</div>
		<div class="clearfix"></div> --%>
		<div class="r_block resourse_block">
			<div class="r_title">
		        <div class="title_tab text-center">
		            <ul>
		            	<c:forEach items="${resources}" var="entry" varStatus="st">
		                <li>
		                    <h5 class="f14">
		                        <a class="resTitle ${ st.index == 0 ? ' active' : '' }" href="javascript:void(0)" onclick="switchTab(this,'${ entry.key }')">
		                            <c:choose>
										<c:when test="${ entry.key eq 'database' }">
											关系数据库
										</c:when>
										<c:when test="${ entry.key eq 'cache' }">
											高速缓存
										</c:when>
										<c:when test="${ entry.key eq 'storage' }">
											存储
										</c:when>
										<c:when test="${ entry.key eq 'mq' }">
											消息中间件
										</c:when>
										<c:when test="${ entry.key eq 'custom' }">
											自定义资源
										</c:when>
										<c:otherwise>
											${ entry.key }
										</c:otherwise>
									</c:choose> 
		                        </a>
		                    </h5>
		                </li>
		                </c:forEach>
		            </ul>
		        </div>
		        <div class="title_line"></div>
		    </div>
		    <c:forEach items="${resources}" var="entry" varStatus="st">
		    <div class="r_con p10_0 resCatagory" id="${ entry.key }" style="${ st.index == 0 ? '' : 'display:none;' }">
		    	<c:forEach items="${ entry.value }" var="row">
		    		<div class="sl_block sl_block_two">
			            <div class="sl_content sl_content_zypz border01">
			                <div class="sl_zs">
			                    <div class="sl_zs_img">
			                        <img style="width:64px;height: 64px;" src="${ hfn:urlPath(row.icon,pageContext.request) }" / >
			                        <p class="sl_zs_img_name">${ row.name }</p> 
			                    </div>
			                    <div class="sl_zs_con">
			                        <p class="sl_zs_con_text">${ row.summary }</p>
			                    </div>
			                </div>
			                <div class="sl_zs_con_btn pull-right">
			                    <button class="btn btn-default btn_sm" onclick="location.href='${ WEB_APP_PATH }/project/env/resource/apply.do?projectId=${project.projectId}&resId=${row.id}&envId=${env.envId}'">申请</button>
<%-- 			                    <button class="btn btn-default btn_sm" onclick="location.href='${ hfn:urlPath(row.help,pageContext.request) }'">帮助</button> --%>
			                </div>
			            </div>
			        </div>
		    	</c:forEach>
		    </div>
		    </c:forEach>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script>
			function switchTab(obj,tab){
				$('.resTitle').removeClass('active');
				$(obj).addClass('active');
				$('.resCatagory').hide();
				$("#"+tab).show();
			}
		</script>
	</template:replace>
</template:include>