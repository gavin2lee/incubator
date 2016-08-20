<%@page import="net.jeeshop.core.ManageContainer"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page session="false"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
</head>

<body>
	<s:form action="activity" namespace="/manage" method="post" theme="simple">
		<table class="table table-bordered">
			<tr>
				<td>活动ID</td>
				<td><s:textfield cssClass="input-small search-query" name="e.id"/></td>
<!-- 				<td>商品ID</td> -->
<%-- 				<td><s:textfield cssClass="input-small search-query" name="e.id"/></td> --%>
				<td style="text-align: right;">活动类型</td>
				<td style="text-align: left;">
					<s:select list="#{'':'','c':'促销活动','j':'积分兑换','t':'团购活动'}" id="activityType" name="e.activityType"  
					cssClass="input-small" listKey="key" listValue="value"/>
				</td>
				<td style="text-align: right;">优惠方式</td>
				<td style="text-align: left;">
					<s:select list="#{'':'','r':'减免','d':'折扣','s':'双倍积分'}" id="discountType" name="e.discountType"  
					cssClass="input-small" listKey="key" listValue="value"/>
				</td>
				<td>状态</td>
				<td><s:select cssStyle="width:100px;"
						list="#{'':'','y':'显示','n':'不显示'}"
						name="e.status" id="status" listKey="key" listValue="value" /></td>
			</tr>
			<tr>
				<td colspan="18">
					<button method="activity!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
					</button>
					<s:a method="toAdd" cssClass="btn btn-success">
						<i class="icon-plus-sign icon-white"></i> 添加
					</s:a>
					<button method="activity!deletes.action" class="btn btn-danger" onclick="return submitIDs(this,'确定删除选择的记录?');">
						<i class="icon-remove-sign icon-white"></i> 删除
					</button>
					
					<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
						<%@ include file="/manage/system/pager.jsp"%>
					</div>
				</td>
			</tr>
		</table>
		
		<table class="table table-bordered table-hover">
			<tr style="background-color: #dff0d8">
				<th width="20"><input type="checkbox" id="firstCheckbox" /></th>
				<th nowrap="nowrap">活动ID</th>
				<th>活动名称</th>
				<th style="width: 80px;">活动类型</th>
				<th style="width: 80px;">优惠方式</th>
				<th>活动明细</th>
				<th style="width: 50px;">状态</th>
				<th style="width: 50px;">操作</th>
			</tr>
			<s:iterator value="pager.list">
				<tr>
					<td><input type="checkbox" name="ids"
						value="<s:property value="id"/>" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="id" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="name" /></td>
					<td nowrap="nowrap">&nbsp;
						<s:if test="activityType.equals(\"c\")">
							<span class="badge badge-info">促销活动</span>
						</s:if>
						<s:elseif test="activityType.equals(\"j\")">
							<span class="badge badge-success">积分兑换</span>
						</s:elseif>
						<s:elseif test="activityType.equals(\"t\")">
							<span class="badge">团购活动</span>
						</s:elseif>
						<s:else>
							异常
						</s:else>
					</td>
					<td nowrap="nowrap">&nbsp;
						<s:if test="activityType.equals(\"c\")">
							<s:if test="discountType.equals(\"r\")">
								减免
							</s:if>
							<s:elseif test="discountType.equals(\"d\")">
								折扣
							</s:elseif>
							<s:elseif test="discountType.equals(\"s\")">
								双倍积分
							</s:elseif>
							<s:else>
								异常
							</s:else>
						</s:if>
					</td>
					<td nowrap="nowrap">
						活动时间：<s:property value="startDate" /> ~ <s:property value="endDate" />
						<s:if test="expire">
							<span class="label label-danger" style="background-color:Red;">活动已到期</span>
						</s:if>
						<br>
						商品ID：<s:property value="productID" /><br>
						<s:if test="exchangeScore!=0">
							兑换积分：<s:property value="exchangeScore" /><br>
						</s:if>
						
						<s:if test="activityType.equals(\"t\")">
							最低团购人数：<s:property value="minGroupCount" /></br>
							团购价：<s:property value="tuanPrice" /></br>
						</s:if>
					</td>
					<td>&nbsp;
						<s:if test="status.equals(\"y\")">
							<img alt="显示" src="<%=request.getContextPath() %>/resource/images/action_check.gif">
						</s:if>
						<s:else>
							<img alt="不显示" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">
						</s:else>
					</td>
					<td nowrap="nowrap">
						<s:a href="activity!toEdit.action?e.id=%{id}">编辑</s:a>
					</td>
				</tr>
			</s:iterator>
			<tr>
				<td colspan="16" style="text-align: center;"><%@ include
						file="/manage/system/pager.jsp"%></td>
			</tr>
		</table>
	</s:form>
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	function deleteSelect() {
		if ($("input:checked").size() == 0) {
			return false;
		}
		return confirm("确定删除选择的记录?");
	}
</script>
</body>
</html>
