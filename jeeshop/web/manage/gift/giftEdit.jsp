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
	
	<s:form action="gift" namespace="/manage" theme="simple" name="form" id="form" >
<%-- 		<s:hidden name="type"/> --%>
		<s:hidden name="e.type"/>
		<input type="hidden" value="<s:property value="e.catalogID"/>" id="catalogID"/>
		<table class="table table-bordered">
			<tr>
				<td colspan="2" style="text-align: center;">
					<s:if test="e.id=='' or e.id==null">
						<button method="gift!insert.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 新增
						</button>
						
					</s:if> 
					<s:else>
						<button method="gift!update.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 保存
						</button>
					</s:else>
				</td>
			</tr>
			<tr style="background-color: #dff0d8">
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>商品赠品编辑 </strong>
				</td>
			</tr>
			<tr style="display: none;">
				<td>id</td>
				<td><s:hidden name="e.id" label="id" /></td>
			</tr>
			<tr>
				<td style="text-align: right;width: 80px;">赠品名称</td>
				<td style="text-align: left;"><s:textfield name="e.giftName" style="width: 80%;" id="giftName" 
				data-rule="赠品名称:required;giftName;length[1~100];"/></td>
			</tr>
			<tr>
				<td style="text-align: right;width: 80px;">赠品价值</td>
				<td style="text-align: left;"><s:textfield name="e.giftPrice" id="giftPrice" 
				data-rule="赠品价格:required;giftPrice;length[1~100];"/></td>
			</tr>
			<tr>
				<td style="text-align: right;width: 80px;">状态</td>
				<td style="text-align: left;">
					<s:select name="e.status" id="status" list="#{'up':'已上架','down':'已下架'}"></s:select>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">主图</td>   
				<td style="text-align: left;" colspan="3">
					<input type="button" name="filemanager" value="浏览图片" class="btn btn-success"/>
					<s:textfield type="text" id="picture" name="e.picture" ccc="imagesInput" style="width: 600px;" 
					data-rule="小图;required;picture;"/>
					<s:if test="e.picture!=null">
						<a target="_blank" href="<%=SystemManager.systemSetting.getImageRootPath()%><s:property value="e.picture" />">
							<img style="max-width: 50px;max-height: 50px;" alt="" src="<%=SystemManager.systemSetting.getImageRootPath()%><s:property value="e.picture" />">
						</a>
					</s:if>
				</td>
			</tr>
			
			<s:if test="e.createAccount!=null">
				<tr>
					<td style="text-align: right;">添加</td>
					<td style="text-align: left;">
						添加人：<s:property value="e.createAccount"/><br>
						添加时间：<s:property value="e.createtime"/><br>
					</td>
				</tr>
			</s:if>
			
			<s:if test="e.updateAccount!=null">
				<tr>
					<td style="text-align: right;">最后修改</td>
					<td style="text-align: left;">
						修改人：<s:property value="e.updateAccount"/><br>
						修改时间：<s:property value="e.updatetime"/><br>
					</td>
				</tr>
			</s:if>
		</table>
	</s:form>
	
	<span id="pifeSpan" class="input-group-addon" style="display:none"><%=SystemManager.systemSetting.getImageRootPath()%></span>
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/kindeditor-4.1.7/themes/default/default.css" />
	<script charset="utf-8" src="<%=request.getContextPath() %>/resource/kindeditor-4.1.7/kindeditor-min.js"></script>
	<script charset="utf-8" src="<%=request.getContextPath() %>/resource/kindeditor-4.1.7/lang/zh_CN.js"></script>

<script type="text/javascript">
	
	$(function() {
		var ccc = $("#insertOrUpdateMsg").html();
		console.log("insertOrUpdateMsg="+insertOrUpdateMsg);
		if(ccc!='' && ccc.trim().length>0){
			$("#insertOrUpdateMsg").slideDown(1000).delay(1500).slideUp(1000);
		};
	});
		
	function selectDefaultCatalog(){
		var _catalogID = $("#catalogID").val()+"";//alert(_catalogID);
		if(_catalogID!='' && _catalogID>0){//alert("_catalogID="+_catalogID);
			$("#catalogSelect").val(_catalogID);
		}
	}
</script>

<script>
KindEditor.ready(function(K) {
	var editor = K.editor({
		fileManagerJson : '<%=request.getContextPath() %>/resource/kindeditor-4.1.7/jsp/file_manager_json.jsp'
	});
	K('input[name=filemanager]').click(function() {
		var imagesInputObj = $(this).parent().children("input[ccc=imagesInput]");
		editor.loadPlugin('filemanager', function() {
			editor.plugin.filemanagerDialog({
				viewType : 'VIEW',
				dirName : 'image',
				clickFn : function(url, title) {
					//K('#picture').val(url);
					//alert(url);
					imagesInputObj.val(url);
					editor.hideDialog();
					clearRootImagePath(imagesInputObj);//$("#picture"));
				}
			});
		});
	});
	
});

//删除图片主路径
function clearRootImagePath(picInput){
	var _pifeSpan = $("#pifeSpan").text();
	var _imgVal = picInput.val();
	console.log("1===>_imgVal = "+_imgVal);
	//if(_imgVal && _imgVal.length>0 && _imgVal.indexOf(_pifeSpan)==0){
		//picInput.val(_imgVal.substring(_pifeSpan.length));
		console.log("2===>"+_imgVal.indexOf("/attached/"));
		picInput.val(_imgVal.substring(_imgVal.indexOf("/attached/")));
		
	//}
}

</script>

</body>
</html>
