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
	<s:form action="comment" namespace="/manage" method="post" theme="simple">
				<table class="table table-bordered">
					<tr>
						<!-- 
						<td style="text-align: right;">评论等级</td>
						<td style="text-align: left;">
							<s:select list="#{0:'好评',1:'中评',2:'差评'}" id="star" name="e.star"  cssClass="input-medium" 
								headerKey="" headerValue=""
								listKey="key" listValue="value"  />
						</td>
						 -->
						<td style="text-align: right;">商品编号</td>
						<td style="text-align: left;">
							<s:textfield name="e.productID" cssClass="input-medium" />
						</td>
						
						<td style="text-align: right;">会员账号</td>
						<td style="text-align: left;">
							<s:textfield name="e.account" cssClass="input-medium" />
						</td>
						
						<td style="text-align: right;">订单编号</td>
						<td style="text-align: left;">
							<s:textfield name="e.orderID" cssClass="input-medium" />
						</td>
						
						<td style="text-align: right;">是否显示</td>
						<td style="text-align: left;">
							<s:select list="#{'':'','y':'是','n':'否'}" id="status" name="e.status"  cssClass="input-medium" 
								listKey="key" listValue="value"  />
						</td>
					</tr>
					<tr>
						<td colspan="16">
<%-- 							<s:submit method="selectList" value="查询" cssClass="btn btn-primary"> --%>
<!-- 								<i class="icon-search"></i> -->
<%-- 							</s:submit> --%>
<%-- 							<s:a method="selectList" cssClass="btn btn-primary"> --%>
<!-- 								<i class="icon-search icon-white"></i> 查询 -->
<%-- 							</s:a> --%>
							<button method="comment!selectList.action" class="btn btn-primary" onclick="selectList(this)">
								<i class="icon-search icon-white"></i> 查询
							</button>
								
							<button method="comment!updateStatusY.action" class="btn btn-warning" onclick="return submitIDs(this,'确定显示指定的记录吗？');">
								<i class="icon-arrow-up icon-white"></i> 显示
							</button>
							<button method="comment!updateStatusN.action" class="btn btn-warning" onclick="return submitIDs(this,'确定不显示指定的记录吗？');">
								<i class="icon-arrow-down icon-white"></i> 不显示
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
						<th style="display: none;">编号</th>
						<th nowrap="nowrap">商品编号</th>
						<th nowrap="nowrap">会员账号</th>
						<th nowrap="nowrap">订单编号</th>
						<th>评论内容</th>
						<th nowrap="nowrap">评论日期</th>
						<th nowrap="nowrap">评论星级</th>
						<th nowrap="nowrap">是否显示</th>
						<th nowrap="nowrap">操作</th>
					</tr>
					<s:iterator value="pager.list">
						<tr>
							<td><input type="checkbox" name="ids"
								value="<s:property value="id"/>" /></td>
							<td style="display: none;">&nbsp;<s:property value="id" /></td>
							<td>
<%-- 								&nbsp;<a target="_blank" href="product!toEdit.action?e.id=<s:property value="productID" />"><s:property value="productID" /></a> --%>
								<a target="_blank" href="<%=SystemManager.systemSetting.getWww()%>/product/<s:property value="productID" />.html"><s:property value="productID" /></a>
							</td>
							<td>
								<s:a target="_blank" href="account!show.action?account=%{account}"><s:property value="account" />
								</s:a>
							</td>
							<td>&nbsp;
								<a target="_blank" href="order!toEdit.action?e.type=show&e.id=<s:property value="orderID" />"><s:property value="orderID" /></a>
							</td>
							<td width="500px">&nbsp;
								<s:property value="content" /><br>
								
								<s:if test="reply!=null and reply!=''">
									<span style="color:#f50">【已回复】</span>：<s:property value="reply" escape="false"/>
								</s:if>
							</td>
							<td nowrap="nowrap">&nbsp;<s:property value="createdate" /></td>
							<td>&nbsp;<s:property value="star" /></td>
							<td>&nbsp;
								<s:if test="status.equals(\"y\")">
									<img alt="显示" src="<%=request.getContextPath() %>/resource/images/action_check.gif">
								</s:if>
								<s:else>
									<img alt="不显示" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">
								</s:else>
							</td>
							<td nowrap="nowrap">
								<s:if test="reply==null">
									<s:a href="comment!toEdit.action?e.id=%{id}">回复</s:a>
								</s:if>
								<s:else>
									<s:a href="comment!toEdit.action?e.id=%{id}&update=y">修改回复</s:a>
								</s:else>
							</td>
						</tr>
					</s:iterator>

					<tr>
						<td colspan="17" style="text-align: center;"><%@ include
								file="/manage/system/pager.jsp"%></td>
					</tr>
				</table>
	</s:form>
</body>
</html>
