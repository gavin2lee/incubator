<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
.form_block {
	padding: 5px;
}

.form_block label {
	width: 100px;
}

.specList {
	margin-left: 150px;
}

.u-flavor {
	padding-bottom: 5px;
	height: auto;
	overflow: hidden;
	padding-left: 1px;
}
</style>
		<div class="plate">
			<div class="project_r">
				<!--网络存储信息开始-->
				<div class="form_control">
					<div class="form_block">
						<input type="hidden" name="instanceId" value="${nasInfo.nasId}" />
						<label>实例名称</label>
						<div class="specList">
							<input type="text" readonly="readonly"
								value="${nasInfo.instanceName}" />
						</div>
					</div>
					<div class="form_block">
						<label>环境类型</label>
						<div class="specList">
							<ul class="u-flavor">
								<c:forEach items="${envTypes }" var="item">
									<li class="${nasInfo.envType eq item.key ? 'selected' : '' }">${item.value}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<div class="form_block">
						<label>存储类型</label>
						<div class="specList">
							<c:choose>
								<c:when test="${nasInfo.nasType eq 'NFS'}">
									<span>NFS 共享存储</span>
								</c:when>
								<c:when test="${nasInfo.nasType eq 'VOLUME'}">
									<span>ISCSI卷存储</span>
								</c:when>
							</c:choose>
						</div>
					</div>
					<div class="form_block">
						<label>存储空间</label>
						<div class="specList">
							<ul class="u-flavor">
								<li class="selected">${nasInfo.allocateStorageInfo}</li>
							</ul>
						</div>
					</div> 
					<div class="form_block">
						<label>挂载路径信息</label>
						<div class="specList">
							<textarea style="width: 300px; height: 90px;"
								readonly="readonly">${nasInfo.nfsInfo}</textarea>
						</div>
					</div> 
					<div class="form_block">
						<label>租户组织</label>
						<div class="specList">
							<span>${nasInfo.attach['tenantName'] }</span>
						</div>
					</div>
					<div class="form_block">
						<label>创建人</label>
						<div class="specList">
							<span>${nasInfo.attach['createrName'] }</span>
						</div>
					</div>
				</div>
				<!--网络存储实例信息结束-->
			</div>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<template:super />
		<script>
			var resId = '${nasInfo.nasId}';
			var nasType = '${nasInfo.nasType}';

			$(function() {
				//queryNasStatus();
				//queryNasGrangAccessInfo();
			});

			function queryNasStatus() {
				$("#nasStatus")
						.empty()
						.append(
								'<img src="${WEB_APP_PATH}/assets/images/loading.gif" />');
				var url = "${WEB_APP_PATH}/resource/nas/queryNasStatusInfo.do";
				$.get(url, {"resId" : resId}, function(json) {
					var status = "";
					if (json.code == 0) {
						var status = json.data;
					} else {
						status = "暂时无法获取网络存储状态";
					}
					renderPaasNasStatus(status)
				});
			}

			function renderPaasNasStatus(status) {
				var obj = $("#nasStatus");
				obj.empty().append(status);
				obj
						.append('&nbsp;&nbsp;&nbsp;&nbsp;<a class="action_zt" href="javascript:void(0)" class="btn btn-link yellow" onclick="queryNasStatus()" title="重新查询NAS状态">刷新</a>');
			}

			function queryNasGrangAccessInfo() {
				if (nasType == 'NFS') {
					var url = "${WEB_APP_PATH}/resource/nas/queryNasAccessIps.do";
					$.post(url, {"resId" : resId}, function(json) {
						if (json.code == 0) {
							var data = json.data.list;
							if (data && data.length) {
								var html = '';
								for (var i = 0; i < data.length; i++) {
									var ipInfo = data[i];
									html = html + ipInfo.ip + "\r\n";
								}
								$("#GrantInfo").find("textarea").text(html);
								$("#GrantInfo").removeAttr("style");
							}
						}
					});

				}
			}
		</script>
	</template:replace>
</template:include>

