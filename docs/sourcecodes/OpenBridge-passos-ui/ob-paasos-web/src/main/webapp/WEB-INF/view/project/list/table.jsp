<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="details_nr">
    <table class="table_ob">
        <thead>
         <tr>
             <th>项目来源</th>
             <th>项目名称</th>
             <th>项目编码</th>
             <th>创建时间</th>
             <th>创建人</th>
         </tr>
        </thead>
        <tbody>
        	<c:forEach items="${ pageData }" var="app" varStatus="index">
		         <tr>
		         	<td>
		         		<c:choose>
                       		<c:when test="${app.projectType=='api'}">
                       			APIManager
                       		</c:when>
                       		<c:when test="${app.projectType=='app'}">
                       			APPFactory
                       		</c:when>
                       		<c:when test="${app.projectType=='store'}">
                       			预置应用
                       		</c:when>
                       		<c:otherwise>
                       		${app.projectType}
                       		</c:otherwise>
                       	</c:choose>
                     </td>
		             <td><a href="${WEB_APP_PATH}/project/overview/index.do?projectId=${app.id}">${app.name}</a></td>
		             <td>${app.projectCode}</td>	
		             <td><fmt:formatDate value="${app.createDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
		             <td>${app.createUser}</td>
		         </tr>
	        </c:forEach>
        </tbody>
    </table>
    <ui:pagination data="${ pageData }" href="javascript:loadMyApp(!{pageNo},!{pageSize})" id="apps"></ui:pagination>
</div>