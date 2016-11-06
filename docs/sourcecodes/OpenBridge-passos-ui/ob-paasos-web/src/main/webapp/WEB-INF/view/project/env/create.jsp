<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
			.form_block label {
				width: 100px;
			}	
		</style>
		<!------新增版本内容-------> 
		<form onsubmit="return false;" action="${WEB_APP_PATH}/project/env/save.do?projectId=${project.projectId}" method="post" id="createEnvForm">
	    <div class="r_block">
	        <div class="r_con p10_0">
	            <div class="form_control p20">
	                <div class="form_block">
	                    <label>环境类型</label>
	                    <input type="hidden" name="envType" value="${ param['envType'] }">
	                    <span>
	                    	<c:choose> 
                        		<c:when test="${ param['envType'] eq 'test'}">
                        			测试环境
                        		</c:when>
                        		<c:when test="${ param['envType'] eq 'live'}">
                        			生产环境
                        		</c:when>
                        	</c:choose>
                        </span>
	                </div>
	                <div class="form_block">
	                    <label>环境名称</label>
	                    <input name="envName" style="width: 180px;" id="envName" placeholder="请输入环境名称" />
	                </div>
	                
	                <c:choose>
			            	<c:when test="${defaultEnvmark == null}">
			            	<div class="form_block"> 
			            		<label  >环境标签</label>
			            		<div>
			            		<select id="envMark" name="envMark" style="width: 180px;">
			            			<option value="">---请选择---</option>
			            			<c:forEach items="${evnmark}" var="row">
			            				<option value="${row.code }">${row.name }</option>
			            			</c:forEach>
			            		</select>
			            		</div> 
			             </div>
			            	</c:when>
			            	<c:otherwise>
			            		<input type="hidden" name="envMark" value="${defaultEnvmark}">
			            	</c:otherwise>
			            </c:choose>
	                
	                <div class="form_block mt10">
	                    <label>&nbsp;</label>
	                    <a href="javascript:void(0);" class="btn btn-default f16  mt10" onclick="onUserEnter();"><i class="ico_check"></i>确 定</a>
	                </div>
	            </div>
	        </div>
	    </div>
	    </form>
	    <!--新增版本内容结束-->
	</template:replace>
	<template:replace name="bottom">
		<script>
			function onUserCancel(){
				frameElement.dialog.close();
			}
			function dataValidation(){
		    	if($.trim($("#envName").val())==""){
		    		common.alert("环境名称不能为空");
		    		return false;
		    		}
		    	if($("#envMark").size()>0 && $.trim($("#envMark").val())==""){
		    		common.alert("环境标签不能为空");
		    		return false;
		    		}
		    	return true;
		    }
			
			function onUserEnter(){
				var form = $("#createEnvForm");
		    	var url = form.attr("action");
		    	var params = form.serialize();

		    	if(dataValidation()){
			    	var loading = top.common.loading(); 
			    	$.getJSON(url,params,function(json){ 
		    			loading.close();
		    			if(json.code==0){ 
			    			top.common.tips("配置环境成功",1,function(){
				    			frameElement.callback(true);
			    			});
			    		}else{  
							top.common.alert(json.msg);
			    		} 
			    	});
		    	}
			}
		</script>
	</template:replace>
</template:include>