<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<c:set var="fromdialog" value="${param._dialog!=null && param._dialog}" ></c:set>
<c:if test="${fromdialog}">
	<c:import url="/common/include/head.jsp"></c:import>
	<style>
		.table_ob .hover {
			background: gray;
			cursor: pointer;
		}
	</style>
</c:if>
<table class="table_ob" id="nasList">
    <tbody>
	    <tr>
	        <th>实例名称</th>
	        <th>租户组织</th>
	        <th>创建者</th>
	        <th>创建时间</th>
	        <th>来源</th>
	        <th>参数</th>
	        <th>环境</th>
	        <th style="display: none;">状态</th>
	        <c:if test="${!fromdialog}">
	        	<th width="200px">操作</th>
	        </c:if>
	    </tr>
		<c:forEach items="${ pageData }" var="row"> 
		    <tr data-export='${row.allocateContent }'  class="nfsInfo" data ="${row.nasId }" id="${row.nasId }"  data-name="${row.instanceName }" data-envinfo='${row.requestEnvInfo }'  data-nastype="${row.nasType }">
		        <td>
		        	<p id="allocateContent_${row.nasId }" style="display:none;">${row.allocateContent }</p>
		        	${row.instanceName}
		        </td>
		        <td>
		        	<input type="hidden" class="paasOsTenantId" value="${row.tenantId }" />
		        	<input type="hidden" class="paasOsUserId" value="${row.creater }" />
		        	${row.attach['tenantName']}</td>
		        <td>${row.attach['createrName']}</td>
		        <td><fmt:formatDate value="${ row.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		        <td>
		        	<c:choose>
		        		<c:when test="${row.applyType eq 'api' }">APIManager</c:when>
		        		<c:when test="${row.applyType eq 'app' }">APPFactory</c:when>
		        		<c:otherwise>其他</c:otherwise>
		        	</c:choose>
		        </td>
		        <td>${row.requestEnvInfo }</td>
		        
		        <td>
		       		<c:forEach items="${envTypes }" var="item">
					       	<c:if test="${item.key eq row.envType}">
					       	${item.value}
					       	</c:if> 
					</c:forEach>
				</td>
		        <td style="display: none;" class="${row.nasId }">
				</td>
				<c:if test="${!fromdialog}">
			        <td>
			        	<a href="javascript:void(0);" onclick="deletePaasNas('${row.nasId}','${row.tenantId }','${row.creater }')">删除</a>&nbsp;&nbsp;
			        	<%--
			        	<c:choose>
			        		<c:when test="${row.nasType eq 'NFS' }"> 
			        			<a href="javascript:void(0);" onclick="PaasNasGrant('${row.nasId}','${row.tenantId }','${row.creater }')">授权</a>&nbsp;&nbsp; 
			        		</c:when>
			        		<c:when test="${row.nasType eq 'VOLUME' }">
			        			<a href="javascript:void(0);" onclick="PaasNasAttach('${row.nasId}','${row.tenantId }','${row.creater }')">挂载</a>&nbsp;&nbsp;
			        		</c:when>
			        	</c:choose>
			        	 --%>
			        	<a href="javascript:void(0);" onclick="showNSAInfo('${row.nasId}')">信息</a>
			        </td>
		        </c:if>
		    </tr>
		</c:forEach>
    </tbody>
</table>
<c:choose>
	<c:when test="${not empty pageData }">
		<ui:pagination data="${ pageData }" id="ss" href="javascript:gotoPage(!{pageSize},!{pageNo})"></ui:pagination>
	</c:when>
	<c:otherwise>
		<c:if test="${param.dialog }"><p class="no-content f14">你还没有任何实例，现在就<a href="/os/resource/nas/${nasType}/add.do">创建第一个</a>吧</p></c:if>
	</c:otherwise>
</c:choose>
<c:if test="${fromdialog}">
	<script>
		$(function(){
			$('.table_ob').find('tr').not(':first').bind('mouseover',function(){
				$(this).addClass('hover');
			});
			$('.table_ob').find('tr').not(':first').bind('mouseout',function(){
				$(this).removeClass('hover');
			});
			$('.table_ob').find('tr').not(':first').bind('click',function(){
				var _id = $(this).attr('data');
				var _env = $(this).attr('data-envinfo');
				var _type = $(this).attr('data-nastype').toLowerCase();
				var _allocateContent = $(this).attr('data-export');
				var data = {
						volumnId : _id,
						capacity : _env,
						type : _type,
						allocateContent : _allocateContent
				};
				frameElement.callback(data);
			});
		});
	</script>
</c:if>
<c:if test="${not empty param.dialog && param.dialog  }">
<script>
function setEnv(allocateContent){
	var capacity = $("tr.cur td:eq(4)").html();
	var env = {
			allocateContent : JSON.stringify(allocateContent),
			capacity : capacity
	}
	return env;
}
var listContainerId = 'nasList';

</script>
</c:if>
<%@include file="../../listContentCommon.jsp"%>