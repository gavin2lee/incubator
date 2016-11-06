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
		<form action="${WEB_APP_PATH}/project/env/rename.do?projectId=${project.projectId}" method="post" id="editEnvForm">
	    <div class="r_block">
	        <div class="r_con p10_0">
	            <div class="form_control p20">
	                <input type="hidden" name="envId" value="${ env.envId }">
	                <div class="form_block">
	                    <label>环境类型</label>
	                    <span>
	                    	<c:choose>
                        		<c:when test="${ env.envType eq 'dev'}">
                        			开发环境
                        		</c:when>
                        		<c:when test="${ env.envType eq 'test'}">
                        			测试环境
                        		</c:when>
                        		<c:when test="${ env.envType eq 'live'}">
                        			生产环境
                        		</c:when>
                        	</c:choose>
                        </span>
	                </div>
	                <div class="form_block">
	                    <label>环境名称</label>
	                    <input name="envName" value="${env.envName}" id="envName" style="width: 180px;" placeholder="请输入环境名称"><font style="color: red;">&nbsp;&nbsp;*</font>
	                </div> 
	                
	                <c:choose>
			            	<c:when test="${defaultEnvmark == null}">
			            	<div class="form_block"> 
			            		<label >环境标签</label>
			            		<div >
			            		<select id="envMark" name="envMark" style="width: 180px;">
			            			<option value="">---请选择---</option>
			            			<c:forEach items="${evnmark}" var="row">
			            				<option ${(! empty env.envMark) and env.envMark== row.code ? 'selected="selected"':'' } value="${row.code }">${row.name }</option>
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
	                    <button type="button" class="btn btn-default f16  mt10" onclick="onUserEnter();"><i class="ico_check"></i>确 定</button>
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
				var form = $("#editEnvForm");
		    	var url = form.attr("action");
		    	var params = form.serialize();
		    	if(dataValidation()){
		    		var loading = top.common.loading(); 
			    	$.getJSON(url,params,function(json){ 
		    			loading.close();
			    		if(json.code==0){ 
			    			top.common.tips("成功环境配置",1,function(){
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