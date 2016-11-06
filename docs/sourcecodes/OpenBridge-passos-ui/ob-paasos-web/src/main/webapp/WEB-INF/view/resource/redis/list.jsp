<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<table class="table_ob" id="cacheList">
    <tbody>
	    <tr>
	        <th>实例名称</th>
	        <th>Redis版本</th>
	        <th>租户组织</th>
	        <th>创建者</th>
	        <th>创建时间</th>
	        <th>环境</th>
	        <th>来源</th>
	        <th>参数</th>
	        <th style="width:80px;">状态</th>
	        <th width="200px">操作</th>
	    </tr>
<c:forEach items="${ pageData }" var="row"> 
	    <tr class="redisInfo" data ="${row.redisId }" id="${row.resId }" data-name="${row.redisName }">
	        <td>
	        	<p id="allocateContent_${row.resId }" style="display:none;">${row.allocateContent }</p>
	        	${row.redisName}
	        </td>
	        <td>${row.version}</td>
	        <td>
	        	${row.attach['tenantName']}</td>
	        <td>${row.attach['createrName']}</td>
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
	        <td>${row.allocateEnvInfo }内存</td>
	        <td class="${row.redisId }">
			</td>
	        <td>
	        	<a href="javascript:void(0);" onclick="deletePaasRedis('${row.redisId}')">删除</a>&nbsp;&nbsp;
	        	<a href="javascript:void(0);" onclick="showRedisInfo('${row.redisId}')">信息</a>
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
		<c:if test="${!param.dialog }"><p class="no-content f14">你还没有任何实例，现在就<a href="/os/resource/redis/add.do">创建第一个</a>吧</p></c:if>
	</c:otherwise>
</c:choose>
<c:if test="${not empty param.dialog && param.dialog  }">
<script>
function setEnv(allocateContent){
	var env = [];
	var prefix = "PAASOS_REDIS_";
	if(allocateContent &&　allocateContent.connection){
		env.push({
			key : prefix+'HOST_IP' ,
			value : allocateContent.connection.externalIP
		});
		env.push({
			key : prefix+'HOST_PORT' ,
			value : allocateContent.connection.externalPort
		});
	}
	return env;
}
var listContainerId = 'cacheList';

</script>
</c:if>
<%@include file="../listContentCommon.jsp"%>