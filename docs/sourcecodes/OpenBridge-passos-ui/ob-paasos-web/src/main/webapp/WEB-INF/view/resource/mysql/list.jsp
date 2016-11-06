<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<table class="table_ob" id="mysqlList">
    <tbody>
	    <tr>
	        <th>实例名称</th>
	        <th>MySQL版本</th>
	        <th>租户组织</th>
	        <th>创建者</th>
	        <th>创建时间</th>
	        <th>环境</th>
	        <th>来源</th>
	        <th>参数</th>
	        <th>类型</th>
	        <th style="width:80px;">状态</th>
	        <th width="200px">操作</th>
	    </tr>
<c:forEach items="${ pageData }" var="row"> 
	    <tr class="mysqlInfo" data ="${row.resId }" id="${row.mysqlId }" data-name="${row.instanceName }">
	        <td>
	        	<p id="allocateContent_${row.mysqlId }" style="display:none;">${row.allocateContent }</p>
	        	${row.instanceName}
	        </td>
	        <td>${row.version }</td>
	        <td>
	        	${row.attach['tenantName']}</td>
	        <td>${row.attach['createrName'] }</td>
	        <td><fmt:formatDate value="${ row.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	        <td>
	        	<c:choose>
	        		<c:when test="${row.envType eq 'test' }">测试</c:when>
	        		<c:when test="${row.envType eq 'live' }">生产</c:when>
	        		<c:otherwise></c:otherwise>
	        	</c:choose>
	        </td>
	        <td>
	        	<c:choose>
	        		<c:when test="${row.applyType eq 'api' }">APIManager</c:when>
	        		<c:when test="${row.applyType eq 'app' }">APPFactory</c:when>
	        		<c:otherwise>其他</c:otherwise>
	        	</c:choose>
	        </td>
	        <td><p>${row.allocateMemInfo }内存 </p><p> ${row.allocateStorageInfo }存储</p></td>
	        <td>
	        	<c:choose>
	        		<c:when test="${row.mysqlType eq 'mysql'}">单机</c:when>
	        		<c:when test="${row.mysqlType eq 'mysqlShare'}">共享</c:when>
	        		<c:when test="${row.mysqlType eq 'mysql'}">集群</c:when>
	        		<c:otherwise>未知</c:otherwise>
	        	</c:choose>
	        	
	        </td>
	        <td class="${row.mysqlId }">
			</td>
	        <td>
	        	<a href="javascript:void(0)" onclick="openMysqlAdminUi('${mysqlAdminUrlBase}/rdb/mysql/ui/${row.resId}')">管理</a>&nbsp;&nbsp;
<%-- 	        	<a href="javascript:void(0);" onclick="importPaasMysql('${row.mysqlId}')">导入</a>&nbsp;&nbsp; --%>
<!-- 	        	<a href="#">导出</a>&nbsp;&nbsp; -->
	        	<a href="javascript:void(0);" onclick="deletePaasMysql('${row.mysqlId}')">删除</a>&nbsp;&nbsp;
	        	<a href="javascript:void(0);" onclick="showMySQLInfo('${row.mysqlId}')">信息</a>
	        </td>
	    </tr>
</c:forEach>
    </tbody>
</table>
<c:choose>
	<c:when test="${not empty pageData }">
		<ui:pagination data="${ pageData }" id="ss" href="javascript:gotoPage(!{pageSize},!{pageNo})"></ui:pagination>
	</c:when>
	<c:otherwise>
		<c:if test="${!param.dialog }"><p class="no-content f14">你还没有任何实例，现在就<a href="/os/resource/mysql/add.do">创建第一个</a>吧</p></c:if>
	</c:otherwise>
</c:choose>
<c:if test="${not empty param.dialog && param.dialog  }">
<script>
function setEnv(allocateContent){
	var env = [];
	var prefix = "PAASOS_MYSQL_";
	if(allocateContent &&　allocateContent.connection){
		env.push({
			key : prefix+'HOST_IP' ,
			value : allocateContent.connection.clusterIP
		});
		env.push({
			key : prefix+'HOST_PORT' ,
			value : allocateContent.connection.port
		});
		env.push({
			key : prefix+'USERNAME' ,
			value : allocateContent.connection.account
		});
		env.push({
			key : prefix+'PASSWORD' ,
			value : allocateContent.connection.password
		});
		env.push({
			key : prefix+'DATABASE' ,
			value : allocateContent.connection.database||''
		})
	}
	return env;
}
var listContainerId = 'mysqlList';

</script>
</c:if>
<%@include file="../listContentCommon.jsp"%>