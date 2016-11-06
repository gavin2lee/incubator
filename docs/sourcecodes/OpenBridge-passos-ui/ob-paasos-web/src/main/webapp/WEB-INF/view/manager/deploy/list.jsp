<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", -10);
%>
<template:include file="../base.jsp" nav-left="deploy">
	<template:replace name="title">
		部署列表
	</template:replace>
	<template:replace name="content-body">
		<div class="app_name">
			<a href="${WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

			<p class="app_a">
				<a href="../tenant/list.do">环境管理</a>
			</p>
			<em>&gt;</em>

			<p class="app_a">
				<a href="../deploy/list.do">部署列表</a>
			</p>
		</div>
		<div class="plate">
			<div class="project_r">
				<div class="r_block p20">
					<div class="r_title">
						<h3 class="f14 h3_color h3_btm_line blue mr10">
							<a href="#"> <i class="icons cy_ico mr5"></i>部署列表
							</a>
						</h3>
						<div class="title_line"></div>
					</div>
					<div class="r_con p10_0">
						<div class="ser_bar mt10 mb10">
							<select name="projectType" id="projectType">
								<option value="">--项目来源--</option>
								<option value="app">APPFactory</option>
								<option value="api">APIManager</option>
								<option value="store">预置应用</option>
							</select> <select id="tenantFilter" class="js-search">
								<option value="">--租户--</option>
								<c:forEach items="${tenant }" var="tenant" varStatus="st">
									<option value="${tenant.tenantId }">${tenant.tenantName }</option>
								</c:forEach>
							</select> <select id="envFilter" class="js-search">
								<option value="">--环境类型--</option>
								<option value="test">测试环境</option>
								<option value="live">生产环境</option>
							</select> 
							<c:if test="${envMark!=null}">
								<select id="envMark" class="js-search">
									<option value="all">--环境标签--</option>
									<option value="">--默认--</option>
									<c:forEach items="${envMark}" var="row">
										<optgroup label="${row.key}">
											<c:if test="${! empty row.value}">
												<c:forEach items="${row.value}" var="row1">
													<option value="${row1.code }">${row1.name }</option>
												</c:forEach>
											</c:if>
										</optgroup>
									</c:forEach>
								</select>
							</c:if>
							<select id="statusFilter" class="js-search">
								<option value="">--状态--</option>
								<option value="10">运行中</option>
								<option value="1">停止</option>
							</select> <input type="hidden" name="userId" id="userId" value="${userId}" />
							<input type="text" id="userName" placeholder="选择部署人查找.." style="width:100px" readonly="readonly" ></input>
								<a class="btn btn-yellow2 btn-sm js-search"
								href="javascript:selectUser();">选择</a>
							<input type="text" id="keyword" placeholder="输入关键字查找..">
							<a class="btn btn-yellow2 btn-sm js-search"
								href="javascript:SearchInfo(1,10);">查询</a>
						</div>
						<div class="r_con p10_0" id="list_view"></div>
					</div>
				</div>
			</div>
		</div>

	</template:replace>
	<template:replace name="bottom">
		<template:super />
		<script>
			var pageNo = 1, pageSize = 10;
			function SearchInfo(pageNo, pageSize) {
				var params = {};
				params.pageNo = pageNo;
				params.pageSize = pageSize;
				params.userId=$("#userId").val();
				params.projectType = $("#projectType").val();
				params.tenantId = $("#tenantFilter").val();
				params.envType = $("#envFilter").val();
				params.status = $("#statusFilter").val();
				params.keyword = $("#keyword").val();
				params.envMark = $("#envMark").val();
				var url = "${WEB_APP_PATH}/manager/deploy/table.do";
				$("#list_view")
						.load(
								url,
								params,
								function() {
									var _r = [];
									var project_id = null;
									$(this).find('tr[deploy-id]').each(
											function(domIndex, domEle) {
												var _status = $(domEle).attr(
														'deploy-status');
												if (_status == null
														|| _status != "10") {
													return true;
												}
												project_id = $(domEle).attr(
														'project-id');
												var _id = $(domEle).attr(
														'deploy-id');
												_r.push(_id);
											});
									if (_r.length == 0) {
										return;
									}
									var def = $
											.ajax({
												url : '${WEB_APP_PATH}/manager/deploy/status.do',
												type : 'POST',
												data : {
													deployIds : _r.join(',')
												},
												cache : false,
												dataType : 'json'
											});
									def['done']
											(function(msg) {
												if (msg.code != 0
														|| msg.data == null
														|| msg.data.list == null
														|| msg.data.list.length == 0) {
													return;
												}
												for (var i = 0; i < msg.data.list.length; i++) {
													var _info = msg.data.list[i];
													var _did = _info['deployId'];
													var _pid = _info['projectId'];
													var _result = [];
													for (var j = 0; j < _info.pods.length; j++) {
														var _status = _info.pods[j].status;
														if (_status == null
																|| _status != 'running') {
															_result
																	.push('<span style="color:;">'
																			+ _status
																			+ '</span>');
															continue;
														}
														for (var v = 0; v < _info.pods[j].container.length; v++) {
															var __status = _info.pods[j].container[v].status;
															if (__status == null
																	|| __status != 'running') {
																_result
																		.push('<span style="color:#407cbd;">'
																				+ __status
																				+ '</span>');
																continue;
															}
															_result
																	.push('<span style="color:#407cbd;">'
																			+ __status
																			+ '</span>');
														}
													}
													if (_result.length > 0) {
														$(
																'#'
																		+ _did
																		+ '_status')
																.html(
																		'<a href="${WEB_APP_PATH}/project/deploy/view.do?deployId='
																				+ _did
																				+ '&projectId='
																				+ _pid
																				+ '&tab=instance">('
																				+ _result
																						.join('|')
																				+ ')<a>');
													}

												}
											});
								});
			}
			$(function() {
				SearchInfo(pageNo, pageSize);
			});
			function changeKeyword(evt) {
				evt = evt | event;
				if (event.keyCode == 13) {
					SearchInfo(pageNo, pageSize);
				}
			}
			function stopdeploy(deployId) {
				$.post("stop.do", {
					deployId : deployId
				}, function(json) {
					if (json.code == 0) {
						common.tips("应用部署正在后台停止中......", 1, function() {
							window.location.reload();
						});
					} else {
						common.alert("停止失败！" + json.msg, 2);
					}
				});
			}
			function deploy(deployId) {
				$.post("deploy.do", {
					deployId : deployId
				}, function(json) {
					if (json.code == 0) {
						common.tips("应用部署正在后台启动中......", 1, function() {
							window.location.reload();
						});
					} else {
						common.alert("启动失败！" + json.msg, 2);
					}
				});
			}
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
				var dialog = top.common
						.dialogIframe(
								"请选择用户",
								url,
								650,
								600,
								null,
								[ '确定', '取消' ],
								{
									btn1 : function(index, layero) {
										var iwin = layero.find("iframe").get(0).contentWindow;
										if (iwin.dialogClose
												&& typeof (iwin.dialogClose) === 'function') {
											var obj = (iwin.dialogClose());
											if (obj != null && obj.ids != null) {
												var idObj = $("#" + valId);
												var textObj = $("#" + textId);
												textObj.empty();
												idObj
														.val(obj.ids[obj.ids.length - 1]);
												if (textObj.is(":input")) {
													textObj
															.val(obj.texts[obj.ids.length - 1]);
												} else {
													textObj
															.html(obj.texts[obj.ids.length - 1]);
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
		</script>
	</template:replace>
</template:include>