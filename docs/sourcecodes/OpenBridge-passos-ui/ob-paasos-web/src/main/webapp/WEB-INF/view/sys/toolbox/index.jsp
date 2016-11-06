<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../../sys/manager.jsp" active="toolbox">
	<template:replace name="title">
		工具箱
	</template:replace>
	<template:replace name="content-path">
		<em>&gt;</em>
		<p class="app_a">
			<a href="${WEB_APP_PATH}/sys/toolbox/index.do">工具箱</a>
		</p>
	</template:replace>
	<template:replace name="detail">
		<form id="form">
			<div class="plate_index_pas plate">
				<div class="app_wel_block">
					<div class="app_wel_quick">
						<div class="quick_block" style="width: 35%">
							<div class="quick_nr">
								<dl>
									<dt>数据检测</dt>
									<dd></dd>
								</dl>
								<a href="#">马上检测所有项目 ></a>
							</div>
						</div>
						<div class="quick_block" style="width: 35%">
							<div class="quick_nr">
								<dl>
									<dt>用户穿越</dt>
									<dd>
										<input type="hidden" name="userId" id="userId"
											value="${userId}" />
										<span id="userName"></span>
										<span style="position: relative; top: 2px; cursor: pointer;"onclick="goUser()" id="goUser"> 
										<a class="btn btn-default btn-sm btn-yellow2">GO</a>
									</dd>
								</dl>
								<a href="javascript:selectUser()">选择用户进行穿越 ></a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</template:replace>
	<template:replace name="bottom">
		<template:super />
		<script type="text/javascript">
			function reload() {
				var url = "${WEB_APP_PATH}/sys/toolbox/reload.do";
				$.getJSON(url, function(json) {
					if (json.code == 0) {
						common.tips("刷新成功", 1, function() {
						});
					} else {
						common.tips(json.msg);
					}
				});
			};
			function selectUser() {
				var params = {};
				params.query = "all";
				dialogUser(false, 'userId', 'userName', null, params);
			}

			function dialogUser(multiple, valId, textId, cb, params,
					isFilterExist) {
				var url = WEB_APP_PATH + "/sys/user/dialog.do?query="
						+ params.query + "&multiple=" + multiple;
				//是否过滤用户
				if (isFilterExist) {
					url = url + "&table=" + isFilterExist.table + "&tenantId="
							+ isFilterExist.tenantId;
				}
				var dialog = top.common.dialogIframe("请选择用户",url,650,600,null,[ '确定', '取消' ],{
									btn1 : function(index, layero) {
										var iwin = layero.find("iframe").get(0).contentWindow;
										if (iwin.dialogClose&& typeof (iwin.dialogClose) === 'function') {
											var obj = (iwin.dialogClose());
											if (obj != null && obj.ids != null) {
												var idObj = $("#" + valId);
												var textObj = $("#" + textId);
												textObj.empty();
												idObj.val(obj.ids[obj.ids.length - 1]);
														if (textObj.is(":input")) {
															textObj.val(obj.texts[obj.ids.length - 1]);
														} else {
															textObj.html(obj.texts[obj.ids.length - 1]);
														}
												}
										}
										if (cb != null)
											cb();
									},
									btn2 : function(index, layero) {
									}
								});
				dialog.getIFrame().params = params;
			}
			function goUser(){
				var userId=$("#userId").val();
				if(!$.trim(userId)){
					common.tips("请先选择用户");
					return false;
				}
				var url = "${WEB_APP_PATH}/sys/toolbox/changeuser.do?userId="+userId;
				$.getJSON(url, function(json) {
					if (json.code == 0) {
						common.tips("穿越成功", 1, function() {
							common.forward('${WEB_APP_PATH}/project/index.do');
						});
					} else {
						common.tips(json.msg);
					}
				});
			}
		</script>
	</template:replace>
</template:include>