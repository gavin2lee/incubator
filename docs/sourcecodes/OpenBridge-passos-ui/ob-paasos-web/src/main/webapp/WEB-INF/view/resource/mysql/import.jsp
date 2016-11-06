<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		 <div class="plate">
            <div class="project_r">
                <!--数据库执脚本页面开始-->
	            <form method="post" id="importSqlForm" action="${WEB_APP_PATH}/resource/mysql/executeImportSql.do" enctype="multipart/form-data">
	                <div class="r_block p20">
	                    <div class="r_con p10_0">
	                        <div class="form_control p20">
	                            <div class="form_block">
	                            	<input type="hidden" name="instanceId" value="${mysqlInfo.mysqlId}"/>
	                                <label>Sever</label>
	                                ${mysqlInfo.mysqlServer }
	                            </div>
	                            <div class="form_block">
	                                <label>DataBase</label>
	                                 ${mysqlInfo.instanceName }
	                            </div>
	                            <div class="form_block">
	                                <label>SQL脚本</label>
	                                <div class="specList">
	                                	 <input type="file" name="sqlFile" size="100" />
	                                </div>
	                            </div>
	                            <div class="form_block">
	                            </div>
	                            <div class="form_block mt10">
	                                <label>&nbsp;</label>
	                                <button class="btn btn-default btn-yellow f16  mt10" onclick="executeSqlForImport()"><i class="ico_check"></i>返回
	                                </button>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	            </form>
                <!--数据库执脚本页面结束-->
            </div>
        </div>
	</template:replace>
	<template:replace name="bottom">
		<script>
			var paasOsTenantId = '${mysqlInfo.tenantId}';
			var paasOsUserId = '${mysqlInfo.creater}';
			var mysqlId = '${mysqlInfo.mysqlId}';
			function executeSqlForImport(){
				var url = "${WEB_APP_PATH}/resource/mysql/executeImportSql.do";
				var fileName = $("#sqlFile").val();
				if(fileName=='' || fileName.indexof('.sql')<0){
					common.tips("请选择.sql文件");
					return false;
				}
				$("#importSqlForm").submit();
			}
		</script>
	</template:replace>
</template:include>
	
