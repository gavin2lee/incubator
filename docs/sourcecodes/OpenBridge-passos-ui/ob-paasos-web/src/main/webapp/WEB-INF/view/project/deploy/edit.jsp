<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp"  nav-left="deploy">
	<template:replace name="title">
		项目概述
	</template:replace>
	<template:replace name="content-body">
			<style>
		
				.row-data input,.row-data select{
					width: 90%;
				}
				.table_creat th{
					width: 140px;
				}
				.table_creat {
					width: 500px;
				}
				.table_creat .operator{
					width: 80px;
				}
				#envVariable .table_creat th{
					width: 210px;
				}
				#envVariable .table_creat .operator{
					width: 80px;
				}
				#envVariable{
					position: relative;
				}
			</style>
	     <div class="app_name">
	     	<c:set var="isAllModify"  value="${(paasProjectDeploy.status==1 || paasProjectDeploy.status==0) ? true : false }"></c:set>
            <a href="proiect_list.html"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="${ WEB_APP_PATH }/project/index.do">项目管理</a></p>
            <em>&gt;</em>
			
            <p class="app_a">
            	<a href="${ WEB_APP_PATH }/project/overview/index.do?projectId=${project.projectId}">
            	${project.projectName }&nbsp;
            	</a>
            </p>
            <em>&gt;</em>

            <p class="app_a">修改项目部署</p>
        </div>
        
        <div class="plate">
        	<form id="deploy_area">
            <div class="project_r">
                <div class="r_block p20">
                    <div class="r_block">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line"><i class="icons add_ico_blue mr5"></i>修改项目部署</h3>

                            <div class="title_line"></div>
                        </div>
                        <div class="r_con p10_0">
                            <div class="form_control p20">
                                <div class="form_block">
                                    <label>部署名称</label>
                                    <input type="hidden" name="tenantId"  value="1"/>
                                    <input type="hidden" name="projectId"  value="${project.projectId }"/>
                                     <input type="hidden" name="deployId"  value="${paasProjectDeploy.deployId }"/>
                                    <c:choose>
                                    	<c:when test="${isAllModify }">
                                    		<input value="${paasProjectDeploy.deployName}" type="text"  name="deployName">
                                    	</c:when>
                                    	<c:otherwise>
                                    			${paasProjectDeploy.deployName}
                                    	</c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="form_block"  style="display: none;">
                                    <label>业务编码</label>
                                	<c:choose>
                                    	<c:when test="${isAllModify }">
                                    		<input value="${paasProjectDeploy.deployCode}" type="text"  name="deployCode">
                                    	</c:when>
                                    	<c:otherwise>
                                    			${paasProjectDeploy.deployCode}
                                    	</c:otherwise>
                                    </c:choose>
                                </div>
                                <c:if test="${project.projectType !='store' }">
                                <div class="form_block">
                                    <label>部署镜像</label>
                                    <c:choose>
                                    	<c:when test="${isAllModify }">
                                    		<select name="buildId"  id="buildId">
		                                        <option value="">--请选择镜像--</option>
		                                        <c:if test="${ ! empty paasProjectBuilds }">
		                                        	<c:forEach items="${paasProjectBuilds }" var="row">
		                                        		<option versionid="${row.versionId }" ${paasProjectDeploy.buildId!=null && paasProjectDeploy.buildId==row.buildId ? "selected=\"selected\"" : ""}  value="${row.buildId }">${row.buildName }:${row.buildTag }</option>
		                                        	</c:forEach>
		                                        </c:if>
		                                    </select>
                                    	</c:when>
                                    	<c:otherwise>
                                    			${paasProjectBuild.buildName}
                                    	</c:otherwise>
                                    </c:choose>
                                </div>
                                </c:if>
                                <c:if test="${project.projectType =='store' }">
                                	<input type="hidden" name="imageId" value="${paasProjectDeploy.imageId }"/>
                                </c:if>
                                <div class="form_block" style="display: none;">
                                    <label>外部访问</label>
                                    
                                    <c:choose>
                                    	<c:when test="${isAllModify }">
                                    		<ul class="u-flavor"  name="publicPort">
			                                	<li data="1"  ${(empty paasProjectDeploy.publicIp)? "" : "class=\"selected\""}>是</li>
			                                	<li data="2"  ${(empty paasProjectDeploy.publicIp)?  "class=\"selected\"" : ""} >否</li>
			                                </ul>
                                    
                                    	</c:when>
                                    	<c:otherwise>
                                    			<span>${empty paasProjectDeploy.publicIp?"非公开":"公开"}</span>
                                    	</c:otherwise>
                                    </c:choose>
                                	
                                </div>
                                <div class="form_block">
                                    <label>环境类型</label>
                                    <c:choose>
                                    	<c:when test="${isAllModify }">
                                    		<ul class="u-flavor"   name="envType">
			                                	<c:forEach items="${envTypes }" var="item">
				                                	<li ${(paasProjectDeploy.envType==item.key)? "class=\"selected\"":""} data="${item.key}">${item.value}</li>	                                
				                                </c:forEach>
			                                </ul>
	                                   </c:when>
                                    	<c:otherwise>
                                    			<span>
                                    				<c:forEach items="${envTypes }" var="item">
			                                    		<c:if test="${item.key==paasProjectDeploy.envType}">${item.value}</c:if>                             
						                             </c:forEach>
                                    			</span>
                                    	</c:otherwise>
                                    </c:choose>
                                    	
                                </div>
                                <div class="form_block" style="display: none;">
                                	 <input type="hidden" name="restartPolicy" value="always"/>
                                </div>
                                <div class="form_block"  style="display: none;">
                                    <label>服务IP</label>
                                    <input type="text"  name="serviceIp"  value="${paasProjectDeploy.serviceIp }">
                                </div>
                                <div class="form_block"  style="display: none;">
                                    <label>外部访问IP</label>
                                    <input type="text"  name="publicIp" value="${paasProjectDeploy.publicIp }">
                                </div>
                                <div key="replicas" class="form_block targetField"  id="computeConfig"  targetField="computeConfig">
                                    <label>部署规格</label>
									<div class="specList">
                                        <ul>
                                            <li id="spec_ms" class="specCard">
                                                <div class="spec_up">
                                                    <p>微型<input class="row-td" type="hidden" value="ms" name="spec"></p>
                                                </div>
                                                <div class="spec_down">
                                                    <p><span>1/4核<input class="row-td"  type="hidden" value="0.25" name="cpu"></span> CPU</p>

                                                    <p><span>512MB<input class="row-td"  type="hidden" value="512M" name="memory"></span> 内存</p>
                                                </div>
                                                <div class="spec_btm">
                                                    <p class="text-center"><input name="replicas" type="text" class="row-td spinnerExample"/>
                                                    </p>
                                                </div>
                                            </li>
                                            <li id="spec_s"  class="specCard checked">
                                                <div class="spec_up">
                                                    <p>小型<input class="row-td"   type="hidden" value="s" name="spec"></p>
                                                </div>
                                                <div class="spec_down">
                                                    <p><span>1/2核<input class="row-td"   type="hidden" value="0.5" name="cpu"></span> CPU</p>

                                                    <p><span>1GB<input class="row-td"   type="hidden" value="1G" name="memory"></span> 内存</p>

                                                </div>
                                                <div class="spec_btm">
                                                    <p class="text-center"><input name="replicas"  type="text" class="row-td spinnerExample"/>
                                                    </p>
                                                </div>
                                            </li>
                                            <li id="spec_m" class="specCard">
                                                <div class="spec_up">
                                                    <p>中型<input class="row-td"  type="hidden" value="m" name="spec"></p>
                                                </div>
                                                <div class="spec_down">
                                                    <p><span>1核<input class="row-td"  type="hidden" value="1" name="cpu"></span> CPU</p>

                                                    <p><span>2GB<input class="row-td"  type="hidden" value="2G" name="memory"></span> 内存</p>

                                                </div>
                                                <div class="spec_btm">
                                                    <p class="text-center"><input name="replicas"  type="text" class="row-td spinnerExample"/>
                                                    </p>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div key="mount" class="form_block targetField datagrid" id="volumn"  targetField="volumn">
                                    <label>数据卷</label>
                                    <div class="specList">
                                        <table class="table_creat">
                                            <tr>
                                                <th>类型</th>
                                                <th>挂载点</th>
                                                <th >容量</th>
                                                <th class="operator" style="display:none;">
                                                	<a  template="volumn" class="addVolumn btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">+</em></a>
                                                	<a style="display:none;" template="volumn" class="addRow btn btn-default btn-yellow btn-sm btn-xxs" href="javascript:void(0);"><em class="f14">+</em></a>
                                                </th>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <div key="key"  class="form_block targetField datagrid" id="envVariable"  targetField="envVariable">
                                    <label>环境变量</label>
                                    <div class="specList">
                                        <table class="table_creat">
                                            <tr>
                                                <th>Key</th>
                                                <th>Value</th>
                                                <th  class="operator"  style="display:none;">
                                                	<a template="envVariable"  class="addRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">+</em></a>
                                                </th>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <div key="targetPort"  class="form_block targetField datagrid"  id="ports"  targetField="ports">
                                    <label>应用端口</label>
                                    <div class="specList">
                                        <table class="table_creat">
                                            <tr>
                                            	<th>协议</th>
                                                <th>容器端口</th>
                                                <th>服务端口</th>
                                                <th>关键字</th>
                                                <th  class="operator"   style="display: none;">
                                                	<a template="ports"  class="addRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">+</em></a>
                                                </th>
                                             </tr>
                                        </table>
                                    </div>
                                </div>
                                <div  class="form_block"  id="healthcheck">
                                    <label>健康检查</label>
                                    <div class="specList">
                                        <table class="table_creat">
                                            <tr>
                                            			<th style='text-align: center;'><span style='width:40px'>启用</span></th>
                                            			<th><span style="width:100px;">检查类型</span></th>
                                            			<th>延迟(秒)</th>
                                            			<th>超时(秒)</th>
                                                <th style="text-align: left;width:300px;">检测方式</th>
                                            </tr>
                                            <c:set var="livenessProbe" ></c:set>
                                            <c:set var="readinessProbe" ></c:set>
                                            <c:forEach items="${paasProjectDeploy.health }" var="row">
                                            		<c:choose>
                                            			<c:when test="${row.type=='livenessProbe' }">
                                            				<c:set var="livenessProbe" value="${row }"></c:set>
                                            			</c:when>
                                            			<c:when test="${row.type=='readinessProbe' }">
                                            				<c:set var="readinessProbe" value="${row }"></c:set>
                                            			</c:when>
                                            			<c:otherwise></c:otherwise>
                                            		</c:choose>
                                            </c:forEach>
                                            <tr class="row-level">
                                            			<td style='text-align: center;'><input name="type" ${(! empty livenessProbe) ? "checked='checked'" : '' }  type="checkbox" class="probe row-td" value="livenessProbe"></td>
                                            			<td>检测失败就重启</td>
                                            			<td><input style="width:80px;" class="row-td" value="${empty livenessProbe ? '' : livenessProbe.initialDelaySeconds}" name="initialDelaySeconds" type="text"></td>
                                            			<td><input style="width:80px;" class="row-td" value="${empty livenessProbe ? '' : livenessProbe.timeoutSeconds}" name="timeoutSeconds" type="text"></td>
                                                
                                                <c:set var="httpGet"></c:set>
		                                            <c:set var="tcpSocket"></c:set>
		                                            <c:if test="${! empty livenessProbe }">
		                                            <c:forEach items="${livenessProbe.handlers }" var="row1">
		                                            		<c:choose>
		                                            			<c:when test="${row1.type=='httpGet' }">
		                                            				<c:set var="httpGet" value="${row1 }"></c:set>
		                                            			</c:when>
		                                            			<c:when test="${row1.type=='tcpSocket' }">
		                                            				<c:set var="tcpSocket" value="${row1 }"></c:set>
		                                            			</c:when>
		                                            			<c:otherwise></c:otherwise>
		                                            		</c:choose>
		                                            </c:forEach>
		                                            </c:if>
                                                <td style="text-align: left;">
                                                	<div style="width:380px;" class="row-level2">
                                                			<label style="width: 60px;padding-left: 3px;"><input ${(! empty httpGet)?"checked='checked'" : '' } name="type" class="row-td handlerType"  type="checkbox" value="httpGet">&nbsp;&nbsp;HTTP</label>
                                                			<span><label style="width: 50px;padding-left: 3px;">端口</label><input value="${(empty httpGet) ? '':httpGet.port }" class="row-td"  name="port" style="width:40px;" type="text"></span>
                                                			<span><label style="width: 50px;padding-left: 3px;">路径</label><input value="${(empty httpGet) ? '':httpGet.path }" class="row-td"  name="path" style="width:90px;" type="text"></span>
                                                	</div>
                                                	<div style="width:380px;margin-top:15px;" class="row-level2">
                                                		<label style="width: 60px;padding-left: 3px;"><input ${(! empty tcpSocket)?"checked='checked'" : '' } class="row-td handlerType"  name="type" type="checkbox" value="tcpSocket">&nbsp;&nbsp;TCP</label>
                                                		<span><label style="width: 50px;padding-left: 3px;">端口</label><input value="${(empty tcpSocket)?'' : tcpSocket.port }" class="row-td"  name="port" style="width:40px;" type="text"></span>
                                                	</div>
                                                </td>
                                            </tr>
                                            <tr class="row-level">
                                            			<td style='text-align: center;'><input name="type" ${(! empty readinessProbe) ? "checked='checked'" : '' }  type="checkbox" class="probe row-td" value="readinessProbe"></td>
                                            			<td>检测失败就屏蔽</td>
                                            			<td><input style="width:80px;" class="row-td" value="${empty readinessProbe ? '' : readinessProbe.initialDelaySeconds}" name="initialDelaySeconds" type="text"></td>
                                            			<td><input style="width:80px;" class="row-td" value="${empty readinessProbe ? '' : readinessProbe.timeoutSeconds}" name="timeoutSeconds" type="text"></td>
                                                
                                                <c:set var="httpGet"></c:set>
		                                            <c:set var="tcpSocket"></c:set>
		                                            <c:if test="${! empty readinessProbe }">
		                                            <c:forEach items="${readinessProbe.handlers }" var="row1">
		                                            		<c:choose>
		                                            			<c:when test="${row1.type=='httpGet' }">
		                                            				<c:set var="httpGet" value="${row1 }"></c:set>
		                                            			</c:when>
		                                            			<c:when test="${row1.type=='tcpSocket' }">
		                                            				<c:set var="tcpSocket" value="${row1 }"></c:set>
		                                            			</c:when>
		                                            			<c:otherwise></c:otherwise>
		                                            		</c:choose>
		                                            </c:forEach>
		                                            </c:if>
                                                <td style="text-align: left;">
                                                	<div style="width:380px;" class="row-level2">
                                                		<label style="width: 60px;padding-left: 3px;"><input ${(! empty httpGet)?"checked='checked'" : '' } name="type" class="row-td handlerType"  type="checkbox" value="httpGet">&nbsp;&nbsp;HTTP</label>
                                                			<span><label style="width: 50px;padding-left: 3px;">端口</label><input value="${(empty httpGet) ? '':httpGet.port }" class="row-td"  name="port" style="width:40px;" type="text"></span>
                                                			<span><label style="width: 50px;padding-left: 3px;">路径</label><input value="${(empty httpGet) ? '':httpGet.path }" class="row-td"  name="path" style="width:90px;" type="text"></span>
                                                	</div>
                                                	<div style="width:380px;margin-top:15px;" class="row-level2">
                                                		<label style="width: 60px;padding-left: 3px;"><input ${(! empty tcpSocket)?"checked='checked'" : '' } class="row-td handlerType"  name="type" type="checkbox" value="tcpSocket">&nbsp;&nbsp;TCP</label>
                                                			<span><label style="width: 50px;padding-left: 3px;">端口</label><input value="${(empty tcpSocket)?'' : tcpSocket.port }" class="row-td"  name="port" style="width:40px;" type="text"></span>
                                                	</div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <div class="form_block">
                                    <label>描述</label>
                                    <c:choose>
                                    	<c:when test="${isAllModify }">
                                    		<textarea name="description" rows="3" cols="60" style="width:487px;">${paasProjectDeploy.description}</textarea>
                                    	</c:when>
                                    	<c:otherwise>
                                    			${paasProjectDeploy.description}
                                    	</c:otherwise>
                                    </c:choose>
                                    
                                </div>
                                <div class="form_block mt10">
                                    <label>&nbsp;</label>
                                    <c:choose>
                                    	<c:when test="${isAllModify }">
                                    		<a href="javascript:void(0);" id="save_button" class="btn btn-default btn-yellow f16  mt10"><i class="ico_check"></i>修改</a>
                                    	</c:when>
                                    	<c:otherwise>
                                    		
                                    	</c:otherwise>
                                    </c:choose>
                                    
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--创建应用部署结束-->
            </div>
			</form>
        </div>
        <link href="${WEB_APP_PATH}/assets/css/jquery.spinner.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${WEB_APP_PATH}/assets/js/jquery.spinner.js"></script>
        <template:include file="footer.jsp"></template:include>
        <script id="template_volumn" type="text/html">
											<tr class="row-data">
                                                <td>
                                                	<select name="type" class="u-ipt row-td">
                                                		<option value="emptyDir">emptyDir</option>
                                                		<option value="hostPath">hostPath</option>
                                                		<option value="nfs">nfs</option>
                                                		<option value="iscsi">iscsi</option>
                                                		<option value="flocker">flocker</option>
                                                	</select>
													<input type="hidden" " class="u-ipt row-td" name="volumnId"/>
													<input type="hidden" " class="u-ipt row-td" name="allocateContent"/>
                                                </td>
                                                <td><input name="mount" type="text" class="u-ipt row-td"></td>
                                                <td><input name="capacity" type="text" class="u-ipt row-td"></td>
                                                <td class="text-center" style="display:none;">
	                                                <a class="deleteRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">-</em></a>
                                                </td>
                                            </tr>					
		</script>
		<script id="template_envVariable" type="text/html">
											<tr class="row-data">
                                                <td>
													<input disabled="disabled" readonly="readonly"  name="key" type="text" class="u-ipt row-td">
													<input name="resourceId" type="hidden" class="u-ipt row-td">
												</td>
                                                <td><input disabled="disabled" readonly="readonly"  name="value" type="text" class="u-ipt row-td"></td>
                                                <td class="text-center" style="display: none;">
                                                
	                                                <a class="deleteRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">-</em></a>
                                                </td>
                                            </tr>
		</script>
		<script id="template_ports" type="text/html">
									<tr class="row-data">
                                                <td><input disabled="disabled"  name="portProtocol" type="text" class="u-ipt2 row-td"></td>
                                                <td ><input disabled="disabled" name="targetPort"  type="text" class="u-ipt row-td"></td>
                                                <td ><input disabled="disabled"  name="nodePort" type="text" class="u-ipt2 row-td"></td>
                                                <td><input disabled="disabled"   name="portKey" type="text" class="u-ipt2 row-td"></td>
                                                <td style="display:none;">
                                                	<a class="deleteRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">-</em></a>
                                                </td>
                                            </tr>
		</script>   
        <script>
        	$('.spinnerExample').spinner({});
        	$(function(){
            	var ref_port = {};
        		/*
        		$('.u-flavor').find('li').bind('click',function(){
        			$(this).parent().children().removeClass('selected');
        			$(this).addClass('selected');
        		});
        		*/	
        		function reloadEnv(){
        			var versionId = '';
            	if($('#buildId').size()!=0){
            		versionId = $($('#buildId').get(0).options[$('#buildId').get(0).selectedIndex]).attr('versionid');
                			}
        			
        			var vo = {
        	        	url : '${WEB_APP_PATH}/project/deploy/reloadEnv.do',
        	 					type : 'POST',
        	 					data : {
       	 	        			'projectId' : '${project.projectId}',
       	 	        			'deployId' : '${paasProjectDeploy.deployId }',
           	 	        	'versionId':versionId
        	 	        				}, 
        	 					cache : false,
        	 					dataType : 'json'
        		        	};
             var def1 = $.ajax(vo);
             def1['done'](function(json){
             	if(json.code==0){
                 	$('#envVariable').find('.row-data').remove();
                 	if(json.data !=null && json.data.envs!=null && json.data.envs.length>0){
											for(var i=0;i<json.data.envs.length;i++){
												$('#envVariable').find('.addRow').trigger('click',[json.data.envs[i]]);
											}
     									}
										$('#reloadEnv').remove();
    						}else{
    								common.alert(json.msg);
    							}
              				});
            			}
						function changeEnvs(){
							var vo = {
    	        		url : '${WEB_APP_PATH}/project/deploy/changeEnvs.do',
    	 						type : 'POST',
    	 						data : {
   	 	        				'projectId' : '${project.projectId}',
   	 	        				'deployId' : '${paasProjectDeploy.deployId }'
    	 	        				}, 
    	 						cache : false,
    	 						dataType : 'json'
    		        		};
            	var def1 = $.ajax(vo);
            	def1['done'](function(json){
            		if(json.code==0 && json.data){
               	$('#envVariable').find('.specList').append('<span id="reloadEnv" style="position:absolute;top:20px;left:730px;">该部署环境变量已经发生变化,是否需要替换新的环境变化？<a href="javascript:void(0);">替换</a></span>');
               	$('#reloadEnv').find('a').bind('click',function(){
               		reloadEnv();
                   					});
								}else{
								//	common.alert(json.msg);
								}
          					});
						}
        		changeEnvs();
        		$('#buildId').bind('change',function(){
        			//$('#ports').find('.row-data').remove();

        			//$('#envVariable').find('.row-data[id^=MT_]').remove();//日志收集用的  URL对应调用的方法 值针对dubbo
							//$('#envVariable').find('.row-data[id^=IT_]').remove();//日志收集用的 URL对应接口id
							//$('#envVariable').find('.row-data[id^=PI_]').remove();//日志收集用的 URL对应接口id
							//$('#envVariable').find('#VERSION_ID').remove();//日志收集用的 URL对应接口id
							$('#envVariable').find('.row-data[resourceid^=version_]').remove();
							$('#ports').find('.deleteRow').trigger('click');
		        	if($(this).val()==''){
        				return ;
        					}
        			var versionId = $(this.options[this.selectedIndex]).attr('versionid');
        			var vo = {
    	        			url : '${WEB_APP_PATH}/project/deploy/version.do',
    	 							type : 'POST',
    	 							data : {
   	 	        					'projectId' : '${project.projectId}',
   	 	        					'versionId' : versionId,
	   	 	        				'envId' : '${env.envId}',
		 	        					'envType' : '${env.envType}',
		 	        					'deployId' : '${paasProjectDeploy.deployId}'
    	 	        					}, 
    	 							cache : false,
    	 							dataType : 'json'
    		        		};
            	var def1 = $.ajax(vo);
            	def1['done'](function(msg){
    						var env = msg.data.envs;
          				if(env == null || env.length==0){
          					return ;
          						}
          			for(var i=0;i<env.length;i++){
          					$('#envVariable').find('.addRow').trigger('click',[env[i]]);
          						}
          					});
					
        			var buildId = $(this).val();
        			var def = $.ajax({
        				url : '${WEB_APP_PATH}/project/build/getPort.do',
        				dataType : 'json',
        				data : {
        					'projectId' : '${project.projectId}',
        					'buildId' : buildId
        						}, 
        				cache : false
        					});
        			def['done'](function(msg){
        				if(msg==null){
        					return ;
        						}
        				if((typeof  msg)==='string'){
        					msg = eval('('+msg+')')
        						}
        				if(msg!=null && msg.length>0){
        					
        					for(var i=0;i<msg.length;i++){
            					var _nodeport = msg[i].nodePort==null?'':msg[i].nodePort;
            					if((_nodeport==null || _nodeport=='') && ref_port[msg[i].port]!=null){
            						_nodeport = ref_port[msg[i].port];
                							}
        						$('#ports').find('.addRow').trigger('click',[{
        							'portProtocol' : msg[i].protocol==null?'':msg[i].protocol,
        							'targetPort' : msg[i].port==null?'':msg[i].port,
        							'nodePort' : _nodeport,
        							'portKey' : msg[i].key==null?'':msg[i].key,
        							'portRemark' : msg[i].remark==null?'':msg[i].remark
        						}]);
        					}
        				}
        			});
        		});
        		
        		
        		$('.specCard').bind('click',function(){
        			$('.specCard').removeClass('checked');
        			$(this).addClass('checked');
        		});
        		$('#computeConfig').find('button').bind('click',function(){
        			return false;
        		});
				
        		$('#healthcheck').find('.probe').bind('change',function(){
            	   	var $p = $(this).parents('.row-level');
            	   	if(!this.checked){
        						$p.find('input[type="text"]').val('');
        						$p.find('.handlerType').attr('checked',false);
        						return ;
        					}
            	   	$p.find('.row-td[name="initialDelaySeconds"]').val('300');
            	   	$p.find('.row-td[name="timeoutSeconds"]').val('60');

            	   	$p.find('.handlerType[value="httpGet"]').attr('checked',true);
            	   	$p.find('.handlerType[value="httpGet"]').trigger('change');
                    	});
        				$('#healthcheck').find('.handlerType').bind('change',function(){
        					var defaultPort = '8080';
        					var mapport = {};
        					$('#ports').find('tr.row-data').each(function(domIndex,domEle){
        						var protocol = $(domEle).find('input[name="portProtocol"]').val().toLowerCase();
        						var port = $(domEle).find('input[name="targetPort"]').val();
        						mapport[protocol] = port;
        					});
        					if(mapport['http']!=null){
        						defaultPort = mapport['http'];
        					}
        					else if(mapport['tcp']!=null){
        						defaultPort = mapport['tcp'];
        					}
        					
        					var $p = $(this).parents('.row-level2');
        					if(!this.checked){
        						$p.find('input[type="text"]').val('');
        						return ;
        					}
        					$p.find('.row-td[name="port"]').val(defaultPort);
        					if($p.find('.row-td[name="path"]').size()>0){
        						$p.find('.row-td[name="path"]').val('/index.jsp');
        					}
        					
        					var ep=$(this).parents('.row-level').find('.probe').get(0);
        					if(!ep.checked){
        						ep.checked = this.checked;
        						$(ep).trigger('change');
        					}
        				});
								
				$('#save_button').bind('click',function(){


					var d = [];
					var error = false;
					var reg = new RegExp("^[0-9]*$");  
					$('#healthcheck').find('.probe:checked').each(function(domIndex,domEle){
						var _info = {};
						var _name = 'type';
						var _value = $(domEle).val();
						_info[_name] = _value;
						var $parent = $(domEle).parents('.row-level');
						var ds = $parent.find('input[name="initialDelaySeconds"]').val();
						var dt = $parent.find('input[name="timeoutSeconds"]').val();
						if(ds==null || ds =='' || dt==null || dt==''){
							common.alert("选中的检查类别延迟启动和超时时间不能为空！");
							error = true;
							return false;
						}
						if(!reg.test(ds)){  
							common.alert("选中的检查类别延迟启动请输入数字！");
							error = true;
							return false;
					    } 
						if(!reg.test(dt)){  
							common.alert("选中的检查类别超时时间请输入数字！");
							error = true;
							return false;
					    } 
						_info['initialDelaySeconds'] = ds;
						_info['timeoutSeconds'] = dt;
						_info['handlers'] = [];
						$parent.find('.handlerType:checked').each(function(domIndex1,domEle1){
							var _result = {};
							_info['handlers'].push(_result);
							var _name = 'type';
							var _value = $(domEle1).val();
							_result[_name] = _value;
							var $pa = $(domEle1).parents('.row-level2');
							var _port = $pa.find('input[name="port"]').val();
							if(_port==null || _port==''){
								common.alert("选中的检查类别端口不能位空！");
								error = true;
								return false;
							}
							if(!reg.test(_port)){  
								common.alert("选中的检查类别端口请输入数字！");
								error = true;
								return false;
						    } 
							_result['port'] = _port;
							if($pa.find('input[name="path"]').size()>0){
								var _path = $pa.find('input[name="path"]').val();
								if(_path==null || _path==''){
									common.alert("选中的检查类别路径不能位空！");
									error = true;
									return false;
								}
								_result['path'] = _path;
							}
						});
						if(_info['handlers']==null||_info['handlers'].length==0){
							common.alert("选中的检查类别需要有处理方法！");
							error = true;
							return false;
						}
						d.push(_info);
					});
					if(error){
						return ;
					}

					
					var $form = $('#deploy_area');
					var param = {};
					$form.find(':input:not(.row-td)').each(function(domIndex,domEle){
						if($(domEle).attr('type')!=null && $(domEle).attr('type')=='radio' && !domEle.checked){
							
							return true;
						}
 						var _name = $(domEle).attr('name');
 						var _v  = $(domEle).val();
 						if(_name==null || _v ==null || _v==''){
 							return true;
 						}
 						param[_name] = _v;
 					});
					
					$('.targetField').each(function(domIndex2,domEle2){
						var _n = $(domEle2).attr('targetField');
						var _k = $(domEle2).attr('key');
						
						if(!$(domEle2).hasClass('datagrid')){
							var j = {};
							$(domEle2).find('.checked').find('.row-td').each(function(domIndex1,domEle1){
		 						var _name = $(domEle1).attr('name');
		 						var _v  = $(domEle1).val();
		 						if(_name==null || _v ==null || _v==''){
		 							return true;
		 						}
		 						j[_name] = _v;
		 					});
							param[_n] = JSON.stringify(j);
							return true;
						}
						
						var r = [];
						$(domEle2).find('.row-data').each(function(domIndex,domEle){
							var j = {};
							$(domEle).find('.row-td').each(function(domIndex1,domEle1){
		 						var _name = $(domEle1).attr('name');
		 						var _v  = $(domEle1).val();
		 						if(_name==null || _v ==null || _v==''){
		 							return true;
		 						}
		 						j[_name] = _v;
		 					});
							if(j[_k]!=null && j[_k]!=''){
								r.push(j);
							}
						});
						if(r.length!=0){
							if(_k=='mount'){
								param[_n] = r;
							}else if(_k=='targetPort'){
								param[_n] = r;
							}else if(_k=='key'){
								param[_n] = r;
							}else {
								param[_n] = JSON.stringify(r);
							}
						}
						
					});
					$('.u-flavor').each(function(domIndex,domEle){
						var _name = $(domEle).attr('name');
						var _v = $(domEle).find('.selected').attr('data');
						param[_name]=_v;
					});

					param.health = d;
					
					var option = {
 							url : '${WEB_APP_PATH}/project/deploy/save.do?projectId=${project.projectId}',
 							type : 'POST',
 							data : JSON.stringify(param),
 							cache : false,
 							dataType : 'json',
 							contentType : 'application/json',
 					};
					
 					var load = common.loading('保存中......');
 					var def = $.ajax(option);
 					def['done'](function(json){
 						if(json.code==0){
			    			common.tips("部署正在构建中......",1,function(){
			    				common.forward("${ WEB_APP_PATH }/project/env/index.do?projectId=${project.projectId}");
			    			});
			    		}else{
			    			load.close();
			    			common.tips(json.msg,null,null,200000);
			    		}
 					});
				});
				
				
				//数据初始化

				
				
				
				
				var computeConfig = '${paasProjectDeploy.computeConfig}';
				computeConfig =eval('('+computeConfig+')');
				$('.specCard').removeClass('checked');
				$('#spec_'+computeConfig.spec).addClass('checked');
				$('#spec_'+computeConfig.spec).find('input[name="replicas"]').val('${paasProjectDeploy.replicas}');
        		<c:if test="${ ! isAllModify}">
        			$('#computeConfig').find('.specCard:not(.checked)').remove();
        		</c:if>
        		
        		 <c:if test="${ ! empty pbs }">
        			<c:forEach items="${pbs}"  var="row">
        				ref_port['${row.targetPort}'] =  '${row.nodePort}';
        				$('#ports').find('.addRow').trigger('click',[{
        					'portProtocol' : '${row.portProtocol}',
        					'targetPort' : '${row.targetPort}',
        					'nodePort' : '${row.nodePort}',
        					'portKey' : '${row.portKey}',
        					'portRemark' : '${row.portRemark}'
        				}]);	
        			</c:forEach>
        		</c:if>
        		
        		<c:if test="${ ! empty pdv }">
	    			<c:forEach items="${pdv}"  var="row">
	    			$('#volumn').find('.addRow').trigger('click',[{
	    					'name' : '${row.name}',
	    					'type' : '${row.type}',
	    					'mount' : '${row.mount}',
	    					'capacity' : '${row.capacity}',
	    					'volumnId' : '${row.volumnId}',
	    					'allocateContent' : '${row.allocateContent}'
	    				}]);	
	    			</c:forEach>
	    		</c:if>
	    		
	    		<c:if test="${ ! empty pde }">
	    			<c:forEach items="${pde}"  var="row">
	    			$('#envVariable').find('.addRow').trigger('click',[{
	    					'key' : '${row.key}',
	    					'value' : "${row.value}",
	    					'resourceId' : '${row.resourceId}'
	    				}]);	
	    			</c:forEach>
	    		</c:if>
        	});
        </script>
        
	</template:replace>
</template:include>