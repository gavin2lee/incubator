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
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
.aCss {
	overflow: hidden;
	word-break: keep-all;
	white-space: nowrap;
	text-overflow: ellipsis;
	text-align: left;
	font-size: 12px;
}
</style>
</head>

<body>
	<s:form action="advert" namespace="/manage" method="post" theme="simple">
				<table class="table table-bordered">
					<tr>
						<td>广告标题</td>
						<td><s:textfield cssClass="input-medium search-query" name="e.title"/></td>
						<td>类型</td>
						<td>
							<s:select list="#{'index_top':'index_top','index_right_top':'index_right_top','index_right_bottom':'index_right_bottom','newslist_right_top':'newslist_right_top','newslist_right_bottom':'newslist_right_bottom','flashlist_right_top':'flashlist_right_top','flashlist_right_bottom':'flashlist_right_bottom'}" 
									id="style" name="e.code"  cssClass="input-medium" headerKey="" headerValue=""
												listKey="key" listValue="value"  />
						</td>
						<!-- 
						<td>时间范围</td>
						<td>
							<input id="d4311" class="Wdate search-query input-small" type="text" name="e.createtime"
							value="<s:property value="e.createtime" />"
							onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')||\'2020-10-01\'}'})"/>
							~ 
							<input id="d4312" class="Wdate search-query input-small" type="text" name="e.createtimeEnd" 
							value="<s:property value="e.createtimeEnd" />"
							onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'2020-10-01'})"/>
						</td>
						 -->
					</tr>
					<tr>
						<td colspan="16">
<%-- 							<s:submit method="selectList" value="查询" cssClass="btn btn-primary"> --%>
<%-- 								<s:param name="btnSelect" value="1">1</s:param> --%>
<%-- 							</s:submit> --%>
<%-- 							<s:a method="selectList" cssClass="btn btn-primary"> --%>
<!-- 								<i class="icon-search icon-white"></i> 查询 -->
<%-- 							</s:a> --%>
							<button method="advert!selectList.action" class="btn btn-primary" onclick="selectList(this)">
								<i class="icon-search icon-white"></i> 查询
							</button>
						
<%-- 							<s:submit method="toAdd" action="advert" value="添加" cssClass="btn btn-success" /> --%>
							<s:a method="toAdd" cssClass="btn btn-success">
								<i class="icon-plus-sign icon-white"></i> 添加
							</s:a>
						
<%-- 							<s:submit method="deletes" onclick="return deleteSelect('确定删除选择的记录?');" value="删除" cssClass="btn btn-danger" /> --%>
<%-- 							<s:a method="deletes" cssClass="btn btn-danger" onclick="return deleteSelect('确定删除选择的记录?');"> --%>
<!-- 								<i class="icon-remove-sign icon-white"></i> 删除 -->
<%-- 							</s:a> --%>
							<button method="advert!deletes.action" class="btn btn-danger" onclick="return submitIDs(this,'确定删除选择的记录?');">
								<i class="icon-remove-sign icon-white"></i> 删除
							</button>
							
<%-- 							<s:submit method="updateInBlackList" onclick="return updateInBlackList();" value="加入黑名单" cssClass="btn btn-warning" /> --%>
<%-- 							<s:submit method="syncCache" value="显示" onclick="return deleteSelect('确定让选择的记录审核通过?这样选择的记录将会出现在门户上。');" cssClass="btn btn-warning" /> --%>
<%-- 							<s:submit method="notSync" value="不显示" cssClass="btn btn-warning" onclick="return deleteSelect('执行该操作后,选择的记录将不会出现在门户上。确定要执行?');"/> --%>
						
							<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
								<%@ include file="/manage/system/pager.jsp"%>
							</div>
						</td>
					</tr>
				</table>

				<table class="table table-bordered table-hover">
					<tr class="success">
						<td width="20px"><input type="checkbox" id="firstCheckbox" /></td>
						<td>广告标题</td>
						<td width="180px">code</td>
						<td width="180px">有效日期范围</td>
						<td width="80px">状态</td>
						<td width="80px">图集优先</td>
						<td width="50px">操作</td>
					</tr>
					
					<s:iterator value="pager.list">
						<tr>
							<td><input type="checkbox" name="ids"
								value="<s:property value="id"/>" /></td>
							<td class="aCss">
							  <s:a href="advert!toEdit.action?e.id=%{id}" 
										><s:property value="title"/></s:a>
							</td>
							<td>&nbsp;<s:property value="code" /></td>
							<td>&nbsp;<s:property value="startdate" /> ~ <s:property value="enddate" /></td>
<%-- 							<td>&nbsp;<s:property value="status" /></td> --%>
							<td>&nbsp;
								<s:if test="status.equals(\"y\")">
									<img alt="显示" src="<%=request.getContextPath() %>/resource/images/action_check.gif">
								</s:if>
								<s:else>
									<img alt="不显示" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">
								</s:else>
							</td>
							<td>&nbsp;
								<s:if test="useImagesRandom.equals(\"y\")">
									<img alt="显示" src="<%=request.getContextPath() %>/resource/images/action_check.gif">
								</s:if>
								<s:else>
									<img alt="不显示" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">
								</s:else>
							</td>
							<td ><s:a href="advert!toEdit.action?e.id=%{id}">编辑</s:a></td>
						</tr>
					</s:iterator>

					<tr>
						<td colspan="7" style="text-align: center;font-size: 12px;"><%@ include
								file="/manage/system/pager.jsp"%></td>
					</tr>
				</table>
				
				<div class="alert alert-info" style="text-align: left;font-size: 14px;margin: 2px 0px;">
					图标含义：<BR>
					<img alt="显示" src="<%=request.getContextPath() %>/resource/images/action_check.gif">：显示到门户上
					<img alt="不显示" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">：不显示到门户上
				</div>

	</s:form>

</body>
</html>
