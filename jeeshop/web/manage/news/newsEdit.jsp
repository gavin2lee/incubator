<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
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
	
	<s:form action="news" namespace="/manage" theme="simple" name="form" id="form" >
<%-- 		<s:hidden name="type"/> --%>
		<s:hidden name="e.type"/>
		<input type="hidden" value="<s:property value="e.catalogID"/>" id="catalogID"/>
		<table class="table table-bordered">
			<tr>
				<td colspan="2" style="text-align: center;">
					<s:if test="e.id=='' or e.id==null">
<%-- 						<s:submit method="insert" value="新增" cssClass="btn btn-primary"/> --%>
						
						<button method="news!insert.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 新增
						</button>
						
					</s:if> 
					<s:else>
						文章ID：<span class="badge badge-success"><s:property value="e.id"/></span>
<%-- 						<s:submit action="news" method="update" value="保存" cssClass="btn btn-primary btnCCC" onclick="doSubmitFunc(this)"/> --%>
						<button method="news!update.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 保存
						</button>
						
				
						<s:if test="e.status.equals(\"y\")">
<%-- 							<s:submit method="down" value="不显示" cssClass="btn btn-warning" onclick="return confirm(\"确定不显示此文章吗?\");"/> --%>
							<s:a action="news" method="down" cssClass="btn btn-warning" onclick="return confirm(\"确定不显示此文章吗?\");">
							<s:param name="e.id" value="e.id"/>
							<i class="icon-arrow-down icon-white"></i> 不显示</s:a>
						</s:if>
						<s:else>
<%-- 							<s:submit method="up" value="显示" cssClass="btn btn-warning" onclick="return confirm(\"确定显示此文章吗?\");"/> --%>
							<s:a action="news" method="up" cssClass="btn btn-warning" onclick="return confirm(\"确定显示此文章吗?\");">
							<s:param name="e.id" value="e.id"/>
							<i class="icon-arrow-up icon-white"></i> 显示</s:a>
						</s:else>
						
						<s:if test="e.type.equals(\"notice\")">
							<a class="btn btn-info" target="_blank" href="<%=SystemManager.systemSetting.getWww()%>/news/<s:property value="e.id"/>.html">
							<i class="icon-eye-open icon-white"></i> 查看</a>
						</s:if>
						<s:elseif test="e.type.equals(\"help\")">
							<a class="btn btn-info" target="_blank" href="<%=SystemManager.systemSetting.getWww()%>/help/<s:property value="e.code"/>.html">
							<i class="icon-eye-open icon-white"></i> 查看</a>
						</s:elseif>
						<a target="_blank" href="<%=SystemManager.systemSetting.getWww()%>/freemarker!create.action?method=staticNewsByID&id=<s:property value="e.id"/>" class="btn btn-warning">
						<i class="icon-refresh icon-white"></i> 静态化</a>
						
					</s:else>
				</td>
			</tr>
			<tr style="background-color: #dff0d8">
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>文章内容编辑 </strong>
				</td>
			</tr>
			<tr style="display: none;">
				<td>id</td>
				<td><s:hidden name="e.id" label="id" /></td>
			</tr>
			<s:if test="e.type.equals(\"help\")">
				<tr>
					<td style="text-align: right;">类别</td>
					<td>
						<%
						application.setAttribute("catalogs", SystemManager.catalogsArticle);
						%>
						<select onchange="catalogChange(this)" name="e.catalogID" id="catalogSelect" data-rule="类别:required;catalogSelect;">
							<option></option>
							<s:iterator value="#application.catalogs">
								<option pid="0" value="<s:property value="id"/>"><font color='red'><s:property value="name"/></font></option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">文章code</td>
					<td style="text-align: left;"><s:textfield name="e.code" data-rule="文章编码:required;code;length[1~25];remote[news!unique.action]" 
							id="code" /><br>
						(例如：[新手帮助]的编码为xsbz，或者输入别的字符，但是必须唯一，最好不要使用中文。)		
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">顺序</td>
					<td style="text-align: left;"><s:textfield name="e.order1" data-rule="顺序:integer;order1;length[1~5];" 
							id="order1" /></td>
				</tr>
			</s:if>
			<tr>
				<td style="text-align: right;width: 80px;">标题</td>
				<td style="text-align: left;"><s:textfield name="e.title" style="width: 80%;" id="title" 
				data-rule="标题:required;title;length[1~45];"/></td>
			</tr>
			<tr>
				<td style="text-align: right;">内容</td>
				<td style="text-align: left;">
					<s:textarea name="e.content" style="width:100%;height:400px;visibility:hidden;" id="content"
					data-rule="内容:required;content;"></s:textarea>
				</td>
			</tr>
		</table>
	</s:form>
<script type="text/javascript">
	$(function() {
		//$("#title").focus();
		selectDefaultCatalog();
		
		var ccc = $("#insertOrUpdateMsg").html();
		console.log("insertOrUpdateMsg="+insertOrUpdateMsg);
		if(ccc!='' && ccc.trim().length>0){
			$("#insertOrUpdateMsg").slideDown(1000).delay(1500).slideUp(1000);
		};
	});
	
	//function abc(){
		//return $('#form').trigger("validate");
	//}
	
	/*
	$("#form").validator({
		rules: {
	        remote: function(element){
	            return $.ajax({
	                url: 'news!unique.action',
	                type: 'post',
	                data: $(form).serialize(),
	                dataType: 'json',
	                success: function(d){
	                	console.log("remote.d="+d);
	                    window.console && console.log(d);
	                }
	            });
	        }
	    },
	    fields: {
	        'code': 'required; remote;'
	    },
		valid:function(form){
			this.isAjaxSubmit = false;
			console.log(this.isValid);
			
			console.log("submit...");
		}
	});*/
		
	function doSubmitFunc(obj){
			var m = $(obj).attr("name");
			console.log(m);
			console.log(m.split(":")[1]+".action");
			
			$("#form").on("valid.form", function(e, form){
				var _formAction = $("#form").attr("action");
				var aa = _formAction.substring(0,_formAction.lastIndexOf("/")+1);
				console.log(aa);
				
				var lastFormAction = aa + m.split(":")[1]+".action";
				$("#form").attr("action",lastFormAction);
				
				console.log($("#form").attr("action"));
				console.log(this.isValid);
				//form.submit();
			});
	}
	
	
	
	function doSubmitFuncByLink(obj){
		var _href = $(obj).attr("href");
		var _form = $("#form");
		_form.attr("action",_href);
		
		console.log("_href="+_href);
		
		$("#form").on("valid.form", function(e, form){
			console.log("this.isValid="+this.isValid);
			
			
			//_form.submit();
		});
		//_form.submit();
		return false;
	}

	function selectDefaultCatalog(){
		var _catalogID = $("#catalogID").val()+"";//alert(_catalogID);
		if(_catalogID!='' && _catalogID>0){//alert("_catalogID="+_catalogID);
			$("#catalogSelect").val(_catalogID);
		}
	}
</script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/kindeditor-4.1.7/themes/default/default.css" />
<script charset="utf-8" src="<%=request.getContextPath() %>/resource/kindeditor-4.1.7/kindeditor-min.js"></script>
<script charset="utf-8" src="<%=request.getContextPath() %>/resource/kindeditor-4.1.7/lang/zh_CN.js"></script>
<script>
	var editor;
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="e.content"]', {
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
