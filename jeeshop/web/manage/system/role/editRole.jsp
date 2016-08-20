<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resource/bootstrap/css/bootstrap.min.css"  type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resource/bootstrap/css/docs.css"  type="text/css">

<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/bootstrap/js/bootstrap.min.js"></script>

<%-- <%@ include file="/manage/system/common.jsp"%> --%>


<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/artDialog4.1.5/skins/chrome.css"> --%>
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/resource/artDialog4.1.5/artDialog.js"></script> --%>
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/resource/artDialog5.0/source/iframeTools.source.js"></script> --%>
	
<link rel="stylesheet" href="<%=request.getContextPath()%>/resource/zTree3.1/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/zTree3.1/js/jquery.ztree.all-3.1.min.js"></script>

<SCRIPT type="text/javascript">
	$(function(){
 		$("#add").add("#update").click(function(){
 			art.dialog.open('<%=request.getContextPath()%>/menu!toEdit.action',
 					{title: '个人信息',width:500, height:350,lock:true});	 			
 		});
	});
</SCRIPT>
<SCRIPT type="text/javascript">
		$(function(){
			var setting = {
					check: {
						enable: true,
						dblClickExpand: false
					},view: {
						fontCss: getFontCss
					},callback: {
						onClick: onClick
					}
			};
			function onClick(e,treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("treeDemo2");
				zTree.expandNode(treeNode);
			}
			
			function getFontCss(treeId, treeNode) {
				return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
			}
			
			loadMenusTree($("#id").val());
			
			//加载菜单树
			function loadMenusTree(id){
				$.ajax({
					url:"<%=request.getContextPath()%>/manage/menu!getMenusByPid.action?pid=0",
					type:"post",
					data:{id:id},
					dataType:"text",
					success:function(data, textStatus){
						var zNodes = eval('('+data+')');
						$.fn.zTree.init($("#treeDemo2"), setting, zNodes);
						
						$("#role_name").focus();
					},
					error:function(){
						alert("error");
					}
				});
			}
			
			//编辑角色
			$("#saveRoleBtn").click(function(){
				var roleName = $("#role_name").val();
				if(roleName==''){
					alert("角色名称不能为空！");
					return;
				}
				//jQuery.blockUI({ message: "处理中，请稍候...", css: {color:'#fff',border:'3px solid #aaa',backgroundColor:'#CC3300'},overlayCSS: { opacity:'0.0' }}); 
				
				$.blockUI({ message: "处理中，请稍候...",css: { 
		            border: 'none', 
		            padding: '15px', 
		            backgroundColor: '#000', 
		            '-webkit-border-radius': '10px', 
		            '-moz-border-radius': '10px', 
		            opacity: .5, 
		            color: '#fff' 
		        } }); 
				
				var ids = "";
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo2");
				var nodes = treeObj.getCheckedNodes(true);
				for(var i=0;i<nodes.length;i++){
					ids+=nodes[i].id+",";
				}
				
				$.ajax({
					url:"<%=request.getContextPath()%>/manage/role!save.action",
					type : "post",
					data : {
						privileges : ids,
						insertOrUpdate : $("#insertOrUpdate").val(),
						id : $("#id").val(),
						roleName : roleName,
						roleDesc : $("#role_desc").val(),
						role_dbPrivilege : $("#role_dbPrivilege").val(),
						status:$("#status").val()
					},
					dataType : "text",
					success : function(data) {
						// 						var zNodes = eval('('+data+')');
						// 						$.fn.zTree.init($("#treeDemo2"), setting, zNodes);
						if (data == 1) {
							// 							loadMenusTree();
							jQuery.unblockUI();
							alert("修改角色成功！");
	// 						document.form1.submit();
						} else {
							jQuery.unblockUI();
							alert("修改角色失败！");
						}
					},
					error : function() {
						jQuery.unblockUI();
						alert("修改角色失败！");
					}
				
				});
				return false;
			});
			
			
			//全部展开	
			//$("#expandAllBtn").bind("click", {type:"expandAll"}, expandNode);
			//$("#collapseAllBtn").bind("click", {type:"collapseAll"}, expandNode);
			$("#expandOrCollapseAllBtn").bind("click", {type:"expandOrCollapse"}, expandNode);
			$("#checkAllTrueOrFalse").bind("click", {type:"checkAllTrueOrFalse"}, expandNode);
		});
		
		var expandAllFlg = true;
		var checkAllTrueOrFalseFlg = true;
		function expandNode(e) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo2"),
			type = e.data.type,
			nodes = zTree.getSelectedNodes();
			

			if (type == "expandAll") {
				zTree.expandAll(true);
			} else if (type == "collapseAll") {
				zTree.expandAll(false);
			} else if (type == "expandOrCollapse") {
				zTree.expandAll(expandAllFlg);
				expandAllFlg = !expandAllFlg;
			} else if (type == "checkAllTrueOrFalse") {
				zTree.checkAllNodes(checkAllTrueOrFalseFlg);
				checkAllTrueOrFalseFlg = !checkAllTrueOrFalseFlg;
			} else {
				if (type.indexOf("All")<0 && nodes.length == 0) {
					alert("请先选择一个父节点");
				}
				var callbackFlag = $("#callbackTrigger").attr("checked");
				for (var i=0, l=nodes.length; i<l; i++) {
					zTree.setting.view.fontCss = {};
					if (type == "expand") {
						zTree.expandNode(nodes[i], true, null, null, callbackFlag);
					} else if (type == "collapse") {
						zTree.expandNode(nodes[i], false, null, null, callbackFlag);
					} else if (type == "toggle") {
						zTree.expandNode(nodes[i], null, null, null, callbackFlag);
					} else if (type == "expandSon") {
						zTree.expandNode(nodes[i], true, true, null, callbackFlag);
					} else if (type == "collapseSon") {
						zTree.expandNode(nodes[i], false, true, null, callbackFlag);
					}
				}
				
			}
		}
</SCRIPT>
</head>

<body>
	<s:form action="role" name="form1" theme="simple">
		<input id="insertOrUpdate" type="hidden"
			value='<s:property value="role.insertOrUpdate"/>' />
		
				<table class="table table-bordered" style="width: 500px;margin: auto;">
					<tr>
						<td colspan="2" style="background-color: #dff0d8;text-align: center;">
							<strong>角色编辑</strong>
						</td>
					</tr>
					<tr style="display: none;">
						<th>id</th>
						<td><s:hidden name="role.id" id="id" /></td>
					</tr>
					<tr>
						<th style="background-color: #dff0d8;text-align: center;">角色名称</th>
						<td style="text-align: left;"><s:if test="role.id==null">
								<s:textfield name="role.role_name" id="role_name"
									readonly="false" />
							</s:if> <s:else>
								<s:textfield name="role.role_name" id="role_name" />
							</s:else></td>
					</tr>
					<tr>
						<th style="background-color: #dff0d8;text-align: center;">角色描述</th>
						<td style="text-align: left;"><s:textfield
								name="role.role_desc" id="role_desc" /></td>
					</tr>
					<tr>
						<th style="background-color: #dff0d8;text-align: center;">数据库权限</th>
						<td style="text-align: left;">
							<s:select list="#{'select':'select','select,insert':'select,insert','select,insert,update':'select,insert,update','select,insert,update,delete':'select,insert,update,delete'}" 
							name="role.role_dbPrivilege" id="role_dbPrivilege"/>
						</td>
					</tr>
					<tr>
						<th style="background-color: #dff0d8;text-align: center;">状态</th>
						<td style="text-align: left;" >
							<s:select list="#{'y':'启用','n':'禁用'}" id="status" name="e.status"  cssClass="input-small" 
								listKey="key" listValue="value"  />
						</td>
					</tr>
					<tr>
						<th style="background-color: #dff0d8;text-align: center;">角色权限</th>
						<td>
							<div id="optionDiv">
								[<a id="expandOrCollapseAllBtn" href="#" title="展开/折叠全部资源" onclick="return false;">展开/折叠</a>]
								[<a id="checkAllTrueOrFalse" href="#" title="全选/全不选" onclick="return false;">全选/全不选</a>]
<!-- 								[<a id="expandAllBtn" href="#" title="全部节点展开" onclick="return false;">展开</a>] -->
<!-- 								[<a id="collapseAllBtn" href="#" title="全部节点折叠" onclick="return false;">折叠</a>] -->
							</div>
							<ul id="treeDemo2" class="ztree"></ul>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;" colspan="2">
							<s:submit id="saveRoleBtn" method="save" value="保存" cssClass="btn btn-primary"/>
<%-- 							<s:a method="save" cssClass="btn btn-success" id="saveRoleBtn"> --%>
<!-- 								<i class="icon-ok icon-white"></i> 保存 -->
<%-- 							</s:a> --%>
<!-- 							<button method="user!update.action" class="btn btn-success" id="saveRoleBtn"> -->
<!-- 								<i class="icon-ok icon-white"></i> 保存 -->
<!-- 							</button> -->
<%-- 							<s:submit method="back" value="返回" cssClass="btn btn-inverse"/> --%>
						</td>
					</tr>
				</table>
	</s:form>
</body>
</html>
