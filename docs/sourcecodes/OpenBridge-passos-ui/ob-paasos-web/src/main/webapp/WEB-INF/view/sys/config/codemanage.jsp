<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="./base.jsp" active="codemanage">
	<template:replace name="title">
		系统参数配置
	</template:replace>
	<template:replace name="detail-info">
		<style type="text/css">
.table-bordered {
	width: 90%;
}

.table-bordered td {
	padding: 5px;
}

.table td textarea {
	width: 90%;
}

.table td input {
	width: 90%;
}
</style>
		<form action="${WEB_APP_PATH}/sys/config/codemanagesave.do"
			enctype="application/x-www-form-urlencoded" method="post">
			<div class="r_block">
				<div class="r_con p10_0">
					<c:import url="./conf/build.jsp"></c:import>
					<c:import url="./conf/registry.jsp"></c:import>
					
					<div class="form_control form_control_setting"
						style="padding: 0 20px;">
						<div class="form_block mt10">
							<label>&nbsp;</label>
							<div>
								<button type="submit" class="btn btn-sm btn-yellow2"
									style="width: 200px; height: 40px;">保 存</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<br>
		<br>
	</template:replace>
</template:include>