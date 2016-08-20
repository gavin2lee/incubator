<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
<script type="text/javascript">
	$(function() {
		$("#title").focus();
		//$('#cc').combotree('setValue', "随遇而安随遇而安随遇而安随遇而安随遇而安");
	});

	function onSubmit() {
		//if($("#pid").val()==''){
			//var t = $('#cc').combotree('tree');	// get the tree object
			//var n = t.tree('getSelected');		// get selected node
			//if(!n || !n.id){
				//alert("请选择父亲类别");
				//return false;
			//}
			//$("#pid").val(n.id);
		//}
		
		if ($.trim($("#name").val()) == "") {
			alert("名称不能为空!");
			$("#title").focus();
			return false;
		}
	}
</script>
</head>

<body>
	<s:form action="catalog" namespace="/manage" theme="simple" id="form" name="form">
		<input id="catalogID" value="<s:property value="e.pid"/>" style="display: none;"/>
		<input id="catalogID_currentID" value="<s:property value="e.id"/>" style="display: none;"/>
		<s:hidden name="e.type" id="type"/>
		
		<table class="table table-bordered" style="width: 95%;margin: auto;">
			<tr style="background-color: #dff0d8">
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>编辑分类</strong>
					
					<s:if test="e.type.equals(\"p\")">
						<span class="badge badge-important">商品分类</span>&nbsp;<strong>
					</s:if>
					<s:else>
						<span class="badge badge-success">文章分类</span>&nbsp;<strong>
					</s:else>
				</td>
			</tr>
			<tr style="display: none;">
				<td>id</td>
				<td><s:hidden name="e.id" label="id" /></td>
			</tr>
			<s:if test="e.id=='' or e.id==null">
				<tr>
			</s:if>
			<s:else>
				<tr>
			</s:else>
				<td style="text-align: right;">大类</td>
				<td style="text-align: left;">
<!-- 							<input id="cc" class="easyui-combotree"  -->
<%-- data-options="url:'<%=request.getContextPath() %>/manage/catalog/catalog!getRootWithTreegrid.action?e.type=<s:property value="e.type"/>',method:'get',required:true" style="width:200px;"> --%>
					<%
					request.setAttribute("catalogs", null);
					%>
					<s:if test="e.type!=null and e.type.equals(\"p\")">
					<%
						request.setAttribute("catalogs", SystemManager.catalogs);
					%>
					</s:if>
					<s:elseif test="e.type!=null and e.type.equals(\"a\")">
					<%
						request.setAttribute("catalogs", SystemManager.catalogsArticle);
					%>
					</s:elseif>
					<s:else>
					<%
// 								try{
// 									throw new NullPointerException();
// 								}catch{Exception e}{
// 									throws e;
// 								}
							%>
					</s:else>
					<select onchange="catalogChange(this)" name="e.pid" id="catalogSelect">
						<option></option>
						<s:iterator value="#request.catalogs">
							<option pid="0" value="<s:property value="id"/>"><font color='red'><s:property value="name"/></font></option>
							<s:iterator value="children">
								<option value="<s:property value="id"/>">&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="name"/></option>
							</s:iterator>
						</s:iterator>
					</select>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">名称</td>
				<td style="text-align: left;"><s:textfield name="e.name" id="name" data-rule="名称;required;name;" size="20" maxlength="20" 
						/></td>
			</tr>
			<tr>
				<td style="text-align: right;">编码</td>
				<td style="text-align: left;">
<!-- 							<input type="button" onclick="getCode()" value="自动获取" class="btn btn-default"/> -->
					<s:textfield name="e.code" data-rule="编码;required;code;length[1~45];remote[catalog!unique.action]" size="45" maxlength="45" id="code" /></td>
<%-- 						<td style="text-align: left;"><s:textfield name="e.code" data-rule="编码;required;code;" size="20" maxlength="20" id="code" /></td> --%>
			</tr>
			<tr>
				<td style="text-align: right;">顺序</td>
				<td style="text-align: left;"><s:textfield name="e.order1" data-rule="顺序;required;integer;order1;" size="20" maxlength="20" 
						id="order1" /></td>
			</tr>
			
			<s:if test="e.type!=null and e.type.equals(\"p\")">
				<tr>
					<td style="text-align: right;">是否在导航条显示</td>
					<td style="text-align: left;">
						<s:select list="#{'n':'否','y':'是'}" id="showInNav" name="e.showInNav" listKey="key" listValue="value"  />
					</td>
				</tr>
			</s:if>
			<tr>
				<td colspan="2" style="text-align: center;"><s:if test="e.id=='' or e.id==null">
<%-- 								<s:submit method="insert" value="新增" cssClass="btn btn-primary"/> --%>
<%-- 								<s:a method="insert" cssClass="btn btn-success"> --%>
<!-- 									<i class="icon-plus-sign icon-white"></i> 新增 -->
<%-- 								</s:a> --%>
						
						<button method="catalog!insert.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 新增
						</button>
					</s:if> 
					<s:else>
<%-- 								<s:submit method="update" value="保存" cssClass="btn btn-primary"/> --%>
<%-- 								<s:a method="update" cssClass="btn btn-success"> --%>
<!-- 									<i class="icon-ok icon-white"></i> 保存 -->
<%-- 								</s:a> --%>
						
						<button method="catalog!update.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 保存
						</button>
					</s:else>
<%-- 							<s:submit method="back" value="返回" cssClass="btn btn-inverse"/> --%>
				</td>
			</tr>
		</table>
	</s:form>
	
<script type="text/javascript">
$(function(){
	selectDefaultCatalog();
	
	$("#name").blur(function(){
		getCode();
	});
});
function selectDefaultCatalog(){
	var _catalogID = $("#catalogID").val();
	console.log("selectDefaultCatalog._catalogID="+_catalogID);
	//if(_catalogID!='' && _catalogID>0){
		$("#catalogSelect").val(_catalogID);
	//}
}

function catalogChange(obj){
	var _pid = $(obj).find("option:selected").attr("pid");
	console.log("_pid="+_pid);
	if(!(_pid && _pid==0)){
		alert("不能选择子类!");
		selectDefaultCatalog();
		return false;
	}
}

function getCode(){
	var _name = $("#name").val();
	//var _url = "catalog!autoCode.action?e.name="+_name;
	var _url = "catalog!autoCode.action";
	$.ajax({
	  type: 'POST',
	  url: _url,
	  data: {"e.name":_name},
	  dataType:"text",
	  //async:false,
	  success: function(data){
		  if(!data){return null;}
		  console.log("data="+data);
		  $("#code").val(data);
	  },
	  error:function(){
		  console.log("加载数据失败，请联系管理员。");
	  }
	});
}
	
</script>
	
</body>
</html>
