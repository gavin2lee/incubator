<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>

<div class="details_nr">
	<table class="table_ob" >
		<thead>
			<tr>
			    <th>项目来源</th>
				<th>项目名称</th>
				<th>租户</th>
				<th>部署人</th>
				<th>部署名称</th>
				<th>部署环境</th>
				<th>创建时间</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${! empty pageData }">
				<c:forEach items="${pageData }" var="row">
					<tr deploy-id="${row.deploy_id}" deploy-status="${row.status}"  project-id="${row.project_id}">
					<td>
		         		<c:choose>
                       		<c:when test="${row.project_type=='api'}">
                       			APIManager
                       		</c:when>
                       		<c:when test="${row.project_type=='app'}">
                       			APPFactory
                       		</c:when>
                       		<c:when test="${row.project_type=='store'}">
                       			预置应用
                       		</c:when>
                       		<c:otherwise>
                       		${row.project_type}
                       		</c:otherwise>
                       	</c:choose>
                     </td>
						<td><a
							href="${WEB_APP_PATH}/project/overview/index.do?projectId=${row.project_id}">${row.project_name }</a></td>
						<td>${row.tenant_name }</td>
						<td>${row.user_name }</td>
						<td>${row.deploy_name }</td>
						<td>
							<a href="${WEB_APP_PATH}/project/env/index.do?projectId=${row.project_id}&envId=${row.env_id}">
							<c:choose>
								<c:when test="${row.env_type eq 'test' }">
									测试环境</c:when>
								<c:when test="${row.env_type eq 'live'  }">
									生产环境</c:when>
							</c:choose>
							(<c:if test="${! empty row.env_mark }">
								${hfn:staticMethod('com.harmazing.openbridge.paas.util.EnvMarkUtil','get',row.env_mark)}-</c:if>${row.env_name})
							</a>
						</td>
						<td><fmt:formatDate value="${row.create_time}"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td><span class="zt_span"><c:choose>
								<c:when test="${row.status ==1  }">
									<i class="pas_ico zt_ico waiting_ico mt5"></i>停止</c:when>
								<c:when test="${row.status ==5  }">
									<i class="pas_ico zt_ico waiting_ico mt5"></i>启动中</c:when>
								<c:when test="${row.status ==6  }">
									<i class="pas_ico zt_ico waiting_ico mt5"></i>变更中</c:when>
								<c:when test="${row.status ==7  }">
									<i class="pas_ico zt_ico waiting_ico mt5"></i>停止中</c:when>
								<c:when test="${row.status ==10  }">
									<i class="pas_ico zt_ico done_ico mt5"></i>运行中</c:when>
								<c:when test="${row.status ==11  }">
									<i class="pas_ico zt_ico fail_ico mt5"></i>删除失败</c:when>
								<c:when test="${row.status ==0  }">
									<i class="pas_ico zt_ico fail_ico mt5"></i>启动失败</c:when>
							</c:choose>
							</span>
                                    	<span id="${row.deploy_id }_status" >
                                    	
                                    	</span></td>
						<td><c:if test="${row.status == 1 || row.status == 0 }">
								<a href="javascript:deploy('${row.deploy_id }');"
									class="deploy-button btn btn-yellow btn_sm f12">启动</a>
							</c:if> <c:if test="${row.status == 10 || row.status == 11 }">
								<a href="javascript:stopdeploy('${row.deploy_id }');"
									class="stop-button btn btn-yellow btn_sm f12">停止</a>
							</c:if></td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
	<c:if test="${empty pageData || fn:length(pageData)==0 }">
		<p style="line-height: 30px; text-align: center; font-size: 14px;">没有相关内容。</p>
	</c:if>
	<c:if test="${not empty pageData}">
		<ui:pagination data="${pageData }"
			href="javascript:SearchInfo(!{pageNo},!{pageSize});" id="deploylist"></ui:pagination>
	</c:if>
</div>