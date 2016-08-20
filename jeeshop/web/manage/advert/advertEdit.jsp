<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/resource/My97DatePicker/WdatePicker.js"></script>
<style>
#insertOrUpdateMsg{
border: 0px solid #aaa;margin: 0px;position: fixed;top: 0;width: 100%;
background-color: #d1d1d1;display: none;height: 30px;z-index: 9999;font-size: 18px;color: red;
}
.btnCCC{
	background-image: url("../img/glyphicons-halflings-white.png");
	background-position: -288px 0;
}
</style>
</head>

<body>
<div class="navbar navbar-inverse" >
	<div id="insertOrUpdateMsg">
		<s:property value="#session.insertOrUpdateMsg"/>
		<%request.getSession().setAttribute("insertOrUpdateMsg", "");//列表页面进行编辑文章的时候,需要清空信息 %>
	</div>
</div>
	
<s:form action="advert" namespace="/manage" theme="simple" name="form1">
			<table class="table table-bordered" style="width: 95%;margin: auto;">
				<tr>
					<td colspan="2" style="text-align: center;">
						<s:if test="e.id=='' or e.id==null">
							<button method="advert!insert.action" class="btn btn-success">
								<i class="icon-ok icon-white"></i> 新增
							</button>
						</s:if> 
						<s:else>
							<button method="advert!update.action" class="btn btn-success">
								<i class="icon-ok icon-white"></i> 保存
							</button>
						</s:else>
				</tr>
				<tr style="background-color: #dff0d8">
					<td colspan="2" style="background-color: #dff0d8;text-align: center;">
						<strong>广告内容编辑 </strong>
					</td>
				</tr>
				<tr style="display: none;">
					<td>id</td>
					<td><s:hidden name="e.id" label="id" /></td>
				</tr>
				<tr>
					<td style="text-align: right;">类型</td>
					<td style="text-align: left;">
						<s:select list="#{'index_top':'index_top','index_right_top':'index_right_top','index_right_bottom':'index_right_bottom','newslist_right_top':'newslist_right_top','newslist_right_bottom':'newslist_right_bottom','flashlist_right_top':'flashlist_right_top','flashlist_right_bottom':'flashlist_right_bottom','advert_login_page':'advert_login_page','advert_register_page':'advert_register_page'}" 
						id="style" name="e.code"  cssClass="input-medium" 
									listKey="key" listValue="value"  />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">标题</td>
					<td style="text-align: left;"><s:textfield name="e.title" data-rule="标题:required;title;length[1~45];" 
							id="title" /></td>
				</tr>
				<tr>
					<td style="text-align: right;">备注</td>
					<td style="text-align: left;"><s:textfield name="e.remark" 
							id="remark" /></td>
				</tr>
				<tr>
					<td style="text-align: right;">状态</td>
					<td style="text-align: left;">
						<s:select list="#{'y':'启用','n':'禁用'}" 
					name="e.status" id="status" listKey="key" listValue="value" />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">是否优先使用图集</td>
					<td style="text-align: left;">
						<s:select list="#{'y':'优先','n':'不优先'}" 
					name="e.useImagesRandom" id="useImagesRandom" listKey="key" listValue="value" />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">日期时间范围</td>
					<td style="text-align: left;">
							<input id="d4311" class="Wdate search-query" type="text" name="e.startdate"
							value="<s:property value="e.startdate" />"
							onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')||\'2020-10-01\'}'})"/>
							~ 
							<input id="d4312" class="Wdate search-query" type="text" name="e.enddate" 
							value="<s:property value="e.enddate" />"
							onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'2020-10-01'})"/>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">内容</td>
					<td style="text-align: left;">
						<s:textarea name="e.html" style="width:100%;height:300px;visibility:hidden;" data-rule="内容:required;content;"></s:textarea>
					</td>
				</tr>
			</table>
</s:form>

<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/kindeditor-4.1.7/themes/default/default.css" />
<script charset="utf-8" src="<%=request.getContextPath() %>/resource/kindeditor-4.1.7/kindeditor-all.js"></script>
<script charset="utf-8" src="<%=request.getContextPath() %>/resource/kindeditor-4.1.7/lang/zh_CN.js"></script>

<script type="text/javascript">
	$(function() {
		$("#title").focus();
		
		var ccc = $("#insertOrUpdateMsg").html();
		console.log("insertOrUpdateMsg="+insertOrUpdateMsg);
		if(ccc!='' && ccc.trim().length>0){
			$("#insertOrUpdateMsg").slideDown(1000).delay(1500).slideUp(1000);
		};
	});
	
	var editor;
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="e.html"]', {
			allowFileManager : true
		});
		K('input[name=getHtml]').click(function(e) {
			alert(editor.html());
		});
		K('input[name=isEmpty]').click(function(e) {
			alert(editor.isEmpty());
		});
		K('input[name=getText]').click(function(e) {
			alert(editor.text());
		});
		K('input[name=selectedHtml]').click(function(e) {
			alert(editor.selectedHtml());
		});
		K('input[name=setHtml]').click(function(e) {
			editor.html('<h3>Hello KindEditor</h3>');
		});
		K('input[name=setText]').click(function(e) {
			editor.text('<h3>Hello KindEditor</h3>');
		});
		K('input[name=insertHtml]').click(function(e) {
			editor.insertHtml('<strong>插入HTML</strong>');
		});
		K('input[name=appendHtml]').click(function(e) {
			editor.appendHtml('<strong>添加HTML</strong>');
		});
		K('input[name=clear]').click(function(e) {
			editor.html('');
		});
	});
</script>
</body>
</html>
