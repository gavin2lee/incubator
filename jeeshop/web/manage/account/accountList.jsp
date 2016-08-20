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
	<s:form action="account!selectList" namespace="/manage" method="post" theme="simple">
		<table class="table table-bordered">
			<tr>
				<td style="text-align: right;" nowrap="nowrap">账号</td>
				<td style="text-align: left;"><s:textfield name="e.account" cssClass="search-query input-small"
						id="account" /></td>
				<td style="text-align: right;" nowrap="nowrap">昵称</td>
				<td style="text-align: left;"><s:textfield name="e.nickname" cssClass="input-small"
						id="nickname" /></td>
				<td style="text-align: right;" nowrap="nowrap">会员等级</td>
				<td style="text-align: left;">
					<s:select list="#{'':'','R1':'普通会员','R2':'铜牌会员','R3':'银牌会员','R4':'金牌会员','R5':'钻石会员'}" id="rank" name="e.rank"  cssClass="input-medium" 
						listKey="key" listValue="value"  />
				<td style="text-align: right;" nowrap="nowrap">状态</td>
				<td style="text-align: left;">
					<s:select list="#{'':'','y':'已冻结','n':'未冻结'}" id="freeze" name="e.freeze"  cssClass="input-medium" 
						listKey="key" listValue="value"  />
				</td>
				<td style="text-align: right;" nowrap="nowrap">注册日期</td>
				<td style="text-align: left;" colspan="3" nowrap="nowrap">
					<input id="d4311" class="Wdate search-query input-small" type="text" name="e.startDate"
					value="<s:property value="e.startDate" />"
					onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')||\'2020-10-01\'}'})"/>
					~ 
					<input id="d4312" class="Wdate search-query input-small" type="text" name="e.endDate" 
					value="<s:property value="e.endDate" />"
					onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'2020-10-01'})"/>
						</td>
			</tr>
			<tr>
				<td colspan="28">
<%-- 					<s:submit method="selectList" value="查询" cssClass="btn btn-primary"/> --%>
<%-- 					<s:a method="selectList" cssClass="btn btn-primary"> --%>
<!-- 						<i class="icon-search icon-white"></i> 查询 -->
<%-- 					</s:a> --%>
					<button method="account!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
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
				<th nowrap="nowrap">登陆方式</th>
				<th nowrap="nowrap">帐号</th>
				<th nowrap="nowrap">昵称</th>
				<th nowrap="nowrap">会员等级</th>
				<th nowrap="nowrap">邮箱</th>
				<th nowrap="nowrap">注册日期</th>
				<th nowrap="nowrap">最后登录时间</th>
				<th nowrap="nowrap">最后登录IP</th>
				<th nowrap="nowrap">是否冻结</th>
<!-- 				<th width="150px">冻结时间</th> -->
				<th nowrap="nowrap">操作</th>
			</tr>
			<s:iterator value="pager.list">
				<tr>
					<td><input type="checkbox" name="ids"
						value="<s:property value="id"/>" /></td>
					<td nowrap="nowrap" align="center">
						<s:if test="accountType.equals(\"qq\")">
							<img alt="" src="<%=request.getContextPath() %>/resource/images/mini_qqLogin.png">
						</s:if>
						<s:elseif test="accountType.equals(\"sinawb\")">
							<img alt="" src="<%=request.getContextPath() %>/resource/images/mini_sinaWeibo.png">
						</s:elseif>
						<s:elseif test="accountType.equals(\"alipay\")">
							<span class="badge badge-warning">alipay</span>
						</s:elseif>
						<s:else>
							<span class="badge badge-warning">jeeshop</span>
						</s:else>
					</td>
					<td nowrap="nowrap">
						&nbsp;<s:property value="account" />
					</td>
					<td nowrap="nowrap">&nbsp;<s:property value="nickname" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="rankName" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="email" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="regeistDate" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="lastLoginTime" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="lastLoginIp" /></td>
					<td nowrap="nowrap">&nbsp;
						<s:if test="freeze.equals(\"y\")">
							<img alt="" src="<%=request.getContextPath() %>/resource/images/login.gif">
						</s:if>
						<s:elseif test="freeze.equals(\"n\")">
							
						</s:elseif>
						<s:else>
							异常
						</s:else>
					</td>
<!-- 					<td nowrap="nowrap">&nbsp; -->
<%-- 						<s:if test="freeze.equals(\"y\")"> --%>
<%-- 							<s:property value="freezeStartdate" />~<s:property value="freezeEnddate" /> --%>
<%-- 						</s:if>								 --%>
<!-- 					</td> -->
					<td nowrap="nowrap">
<%-- 						<s:a href="account!toEdit.action?e.id=%{id}">编辑</s:a> --%>
						<s:a target="_blank" href="account!show.action?account=%{account}">查看</s:a>
<%-- 						<s:a href="account!czmm.action?e.id=%{id}">重置密码</s:a> --%>
						<s:a href="account!toFreeze.action?e.id=%{id}">冻结</s:a>
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
	$(function() {
		function c1(f) {
			$(":checkbox").each(function() {
				$(this).attr("checked", f);
			});
		}
		$("#firstCheckbox").click(function() {
			if ($(this).attr("checked")) {
				c1(true);
			} else {
				c1(false);
			}
		});

	});
	function deleteSelect() {
		if ($("input:checked").size() == 0) {
			return false;
		}
		return confirm("确定删除选择的记录?");
	}
</script>
</body>
</html>
