<%@ page language="java" pageEncoding="UTF-8"%>
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
				.addRow{
					padding: 7px 12px 7px;
				}
			</style>
	     
        
        <div class="plate">
        	<form id="deploy_area">
            <div class="project_r">
                <div class="r_block p20">
                    <div class="r_block">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line"><i class="icons add_ico_blue mr5"></i>创建应用部署</h3>

                            <div class="title_line"></div>
                        </div>
                        <div class="r_con p10_0">
                            <div class="form_control p20">
                                <div class="form_block">
                                    <label>部署名称</label>
                                 		 <input type="hidden" name="envId" value="${envId}"/>
                                    <input type="hidden" name="projectId"  value="${project.projectId }"/>
                                    <input type="text"  name="deployName" maxlength="20"><em>*</em>
                                </div>
                                <div class="form_block" style="display: none;">
                                    <label>业务编码</label>
                                	<input type="text"  name="deployCode"  />
                                </div>
                                <c:if test="${project.projectType !='store' }">
                                <div class="form_block">
                                    <label>部署镜像</label>
                                    <select name="buildId"  id="buildId">
                                        <option value="">--请选择镜像--</option>
                                        <c:if test="${! empty paasProjectBuilds }">
                                        	<c:forEach items="${paasProjectBuilds }" var="row">
                                        		<option versionid="${row.versionId }" value="${row.buildId }">${row.buildName }:${row.buildTag }</option>
                                        	</c:forEach>
                                        </c:if>
                                    </select>
                                </div>
                                </c:if>
                                
                                <div class="form_block" style="display: none;">
                                    <label>外部访问</label>
                                	<ul class="u-flavor"  name="publicPort">
	                                	<li data="1">是</li>
	                                	<li data="2">否</li>
	                                </ul>
                                </div>
                                <div class="form_block">
                                    <label>环境类别</label>
                                    
                                    	<ul class="u-flavor"   name="envType">
		                                	<c:forEach items="${envTypes }" var="item">
			                                	<li class="${env.envType == item.key ? 'selected' : ''} " data="${item.key}">${item.value}</li>	                                
			                                </c:forEach>
		                                </ul>
                                   
                                </div>
                                <div class="form_block" style="display: none;">
                                	 <input type="hidden" name="restartPolicy" value="always"/>
                                </div>
                                <div class="form_block"  style="display: none;">
                                    <label>服务IP</label>
                                    <input type="text"  name="serviceIp">
                                </div>
                                <div class="form_block"  style="display: none;">
                                    <label>外部访问IP</label>
                                    <input type="text"  name="publicIp">
                                </div>
                                <div key="replicas" class="form_block targetField"  id="computeConfig"  targetField="computeConfig">
                                    <label>部署规格</label>
																				<div class="specList">
                                        <ul>
                                            <li class="specCard">
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
                                            <li class="specCard checked">
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
                                            <li class="specCard">
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
<%--                                 <c:if test="${project.projectType !='store'}"> --%>
                                <div key="mount" class="form_block targetField datagrid" id="volumn"  targetField="volumn">
                                    <label>数据卷</label>
                                    <div class="specList">
                                        <table class="table_creat">
                                            <tr>
                                                <th >类型</th>
                                                <th >挂载点</th>
                                                <th  >容量</th>
																										<th style="width:50px;display: none;" class="operator text-center">
                                                	<a  template="volumn" class="addVolumn btn btn-default btn-yellow btn-sm1" href="javascript:void(0);"><em class="f14">+</em></a>
                                                	<a style="display:none;" template="volumn" class="btn-sm addRow btn btn-default btn-yellow btn-xxs " href="javascript:void(0);"><em class="f14">+</em></a>
                                                </th>
                                            </tr>
                                          		<c:if test="${!empty dr.volumns }">
                                          			<c:forEach items="${dr.volumns}" var="row">
                                          				<tr class="row-data">
	                                                <td>
	                                                	<select disabled="disabled"   readonly="readonly" name="type" class="u-ipt row-td">
	                                                		<option ${fn:toLowerCase(row.type) eq 'emptydir' ? 'selected="selected"':''} value="emptyDir">emptyDir</option>
	                                                		<option ${fn:toLowerCase(row.type) eq 'hostpath' ? 'selected="selected"':''} value="hostPath">hostPath</option>
	                                                		<option ${fn:toLowerCase(row.type) eq 'nfs' ? 'selected="selected"':''} value="nfs">nfs</option>
	                                                		<option ${fn:toLowerCase(row.type) eq 'iscsi' ? 'selected="selected"':''} value="iscsi">iscsi</option>
	                                                		<option ${fn:toLowerCase(row.type) eq 'flocker' ? 'selected="selected"':''} value="flocker">flocker</option>
	                                                	</select>
																												<input type="hidden" value="${row.volumnId}" class="u-ipt row-td" name="volumnId"/>
																												<input type="hidden" value="${row.allocateContent}" class="u-ipt row-td" name="allocateContent"/>
	                                                </td>
                                                	<td><input disabled="disabled"   readonly="readonly" name="mount" value="${row.mount}" type="text" class="u-ipt row-td"></td>
                                                	<td><input disabled="disabled"   readonly="readonly" name="capacity" value="${row.capacity }" type="text" class="u-ipt row-td"></td>
	                                                <td class="text-center" style="display: none;">
		                                                <a class="deleteRow btn btn-default btn-yellow btn-sm1" href="javascript:void(0);"><em class="f14">-</em></a>
	                                                </td>
                                            			</tr>
                                          			</c:forEach>
                                          		</c:if>
                                        </table>
                                    </div>
                                </div>
<%--                                </c:if> --%>
                                <c:if test="${project.projectType =='store'}">
                                <div class="form_block targetField datagrid">
                                	<label>依赖资源</label>
	                                <div id="configTableModel" style="display:none;">
																			<table class="table_creat config-table" id="dependencyResources">
																				<tr>
																					<th style="width: 10%;">序号</th>
																					<th>类型</th>
																					<th>说明</th>
																					<th>初始化动作</th>
																					<th>操作</th>
																				</tr>
																				<tbody id="configBody">
																		
																				</tbody>
																			</table>
																		</div>
                                </div>
                                </c:if>
                                <div key="key"  class="form_block targetField datagrid" id="envVariable"  targetField="envVariable">
                                    <label>环境变量</label>
                                    <div class="specList">
                                        <table class="table_creat">
                                            <tr>
                                                <th >Key</th>
                                                <th >Value</th>
                                                <th style="width:50px;display: none;" class="operator text-center">
                                                	<a template="envVariable"  class="btn-sm1 addRow btn btn-default btn-yellow btn-xxs" href="javascript:void(0);"><em class="f14">+</em></a>
                                                </th>
                                            </tr>
                                            	<c:if test="${! empty dr.envs }">
                                          			<c:forEach items="${dr.envs}" var="row">
                                          				<tr class="row-data"  id="${row.key }">
                                                	<td>
																												<input disabled="disabled"   readonly="readonly" value="${row.key }" name="key" type="text" class="u-ipt row-td">
																												<input value="${row.resourceId }" name="resourceId" type="hidden" class="u-ipt row-td">
																											</td>
                                                	<td><input disabled="disabled"   readonly="readonly" value="${row.value}" name="value" type="text" class="u-ipt row-td"></td>
                                               		<td class="text-center" style="display: none;">
		                                                <a class="deleteRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">-</em></a>
                                                	</td>
                                            			</tr>								
                                          			</c:forEach>
                                          		</c:if>										
                                        </table>
                                    </div>
                                </div>
                                <c:if test="${project.projectType !='store' }">
                                <div key="targetPort"  class="form_block targetField datagrid"  id="ports"  targetField="ports">
                                    <label>应用端口</label>
                                    <div class="specList">
                                        <table class="table_creat">
                                            <tr>
                                            	<th >协议</th>
                                            
                                                <th >容器端口</th>
                                                <th style="display:none;">服务端口</th>
                                                <th >关键字</th>
                                                
                                                <th style="display:none;">备注</th>
                                                <th style="display:none;">
                                                	<a template="ports"   class="addRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">+</em></a>
                                                </th>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                </c:if>
                                
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
                                            <tr class="row-level">
                                            			<td style='text-align: center;'><input name="type" type="checkbox" class="probe row-td" value="livenessProbe"></td>
                                            			<td>检测失败就重启</span></td>
                                            			<td><input style="width:40px;" class="row-td" value="" name="initialDelaySeconds" type="text"></td>
                                            			<td><input style="width:40px;" class="row-td" value="" name="timeoutSeconds" type="text"></td>
                                                <td style="text-align: left;">
                                                	<div style="width:380px;" class="row-level2">
                                                		<label style="width:60px;padding-left: 3px;"><input name="type" class="row-td handlerType"  type="checkbox" value="httpGet">&nbsp;&nbsp;HTTP</label>
                                                		<div>
                                                			<span><label style="width: 50px;padding-left: 3px;">端口</label><input class="row-td"  name="port" style="width:40px;" type="text"></span>
                                                			<span><label style="width: 50px;padding-left: 3px;">路径</label><input class="row-td"  name="path" style="width:90px;" type="text"></span>
                                                		</div>
                                                		<div style="clear:both;">
                                                		</div>
                                                	</div>
                                                	<div style="width:300px;margin-top:15px;" class="row-level2">
                                                		<label style="width:60px;padding-left: 3px;"><input class="row-td handlerType"  name="type" type="checkbox" value="tcpSocket">&nbsp;&nbsp;TCP</label>
                                                		<div>
                                                			<div><label style="width: 50px;padding-left: 3px;">端口</label><input class="row-td"  name="port" style="width:40px;" type="text"></div>
                                                		</div>
                                                	</div>
                                                </td>
                                            </tr>
                                            <tr class="row-level">
                                                <td style='text-align: center;'><input name="type" type="checkbox" class="probe row-td" value="readinessProbe"></td>
                                            			<td>检测失败就屏蔽</td>
                                            			<td><input style="width:40px;" class="row-td" value="" name="initialDelaySeconds" type="text"></td>
                                            			<td><input style="width:40px;" class="row-td" value="" name="timeoutSeconds" type="text"></td>
                                                <td style="text-align: left;">
                                                	<div style="width:380px;" class="row-level2">
                                                		<label style="width:60px;padding-left: 3px;"><input name="type" class="row-td handlerType"  type="checkbox" value="httpGet">&nbsp;&nbsp;HTTP</label>
                                                		<div>
                                                			<span><label style="width: 50px;padding-left: 3px;">端口</label><input class="row-td"  name="port" style="width:40px;" type="text"></span>
                                                			<span><label style="width: 50px;padding-left: 3px;">路径</label><input class="row-td"  name="path" style="width:90px;" type="text"></span>
                                                		</div>
                                                		<div style="clear:both;">
                                                		</div>
                                                	</div>
                                                	<div style="width:300px;margin-top:15px;" class="row-level2">
                                                		<label style="width:60px;padding-left: 3px;"><input class="row-td handlerType"  name="type" type="checkbox" value="tcpSocket">&nbsp;&nbsp;TCP</label>
                                                		<div>
                                                			<div><label style="width: 50px;padding-left: 3px;">端口</label><input class="row-td"  name="port" style="width:40px;" type="text"></div>
                                                		</div>
                                                	</div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                
                                
                                <div class="form_block">
                                    <label>描述</label>
                                    <textarea name="description" rows="3" cols="67" maxlength="400"></textarea>
                                </div>
                             
                                
                                
                                <div class="form_block mt10">
                                    <label>&nbsp;</label>
                                    <a href="javascript:void(0);" id="save_button" class="btn btn-default btn-yellow f16  mt10"><i class="ico_check"></i>创 建</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--创建应用部署结束-->
            </div>
			</form>
        </div>
        <template:include file="footer.jsp"></template:include>
        <link href="${WEB_APP_PATH}/assets/css/jquery.spinner.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="${WEB_APP_PATH}/assets/js/jquery.spinner.js"></script>        
       
		<script id="template_envVariable" type="text/html">
											<tr class="row-data">
                                                <td>
													<input name="key" disabled="disabled" readonly="readonly"  type="text" class="u-ipt row-td">
													<input name="resourceId" type="hidden" class="u-ipt row-td">
												</td>
                                                <td><input disabled="disabled" readonly="readonly"  name="value" type="text" class="u-ipt row-td"></td>
                                                <td class="text-center" style="display: none;">
                                                
	                                                <a class="deleteRow btn btn-default btn-yellow btn-sm1" href="javascript:void(0);"><em class="f14">-</em></a>
                                                </td>
                                            </tr>
		</script>
		<script id="template_ports" type="text/html">
									<tr class="row-data">
                                                <td><input disabled="disabled"  readonly="readonly"    name="portProtocol" type="text" class="u-ipt2 row-td"></td>
                                                <td ><input disabled="disabled"  readonly="readonly"   name="targetPort"  type="text" class="u-ipt row-td"></td>
                                                <td style="display:none;"><input name="nodePort" type="text" class="u-ipt2 row-td"></td>
                                                <td><input disabled="disabled"   readonly="readonly"  name="portKey" type="text" class="u-ipt2 row-td"></td>
                                                <td style="display:none;"><input disabled="disabled"  readonly="readonly"  name="portRemark" type="text" class="u-ipt2 row-td"></td>
                                                <td style="display:none;">
                                                	<a class="deleteRow btn btn-default btn-yellow btn-sm1" href="javascript:void(0);"><em class="f14">-</em></a>
                                                </td>
                                            </tr>
		</script>
		
        <script>
        	$('.spinnerExample').spinner({});
        	var __layerObj,__dialog,__target,__CurTr={id:""},
        		resourceConfigBody=$("#configTableModel #configBody"),
        		resourceConfig='';
        	
        	var openResourceDialog = function(target,type){//选择资源弹出框，处理回调
        		 var t = $(target).prev();
				 var oldId = t.length>0 ? t.attr("id") : "";
        		 __layerObj = common.dialogHtml("选择资源",'<div id="resourceDialog" style="padding:10px;"></div>', 900, $(window).height()*0.9,['确定'],{
        			btn1:function(){ 
        				$(target).prev().remove();
        				//设置资源 环境变量
        				if(oldId){//删除老 的 环境变量
	       					$('#envVariable tr:gt(0)').each(function(){
	       						var input = $(this).find("input[name='resourceId']");
	       						if(input.length>0 && input.val()==oldId){
	       							$(this).remove();
	       						}
	       					})
        				}
        				var nasInitTd = $(target).parent().prev();//初始化列的td
        				if(type=='nas'){
        					nasInitTd.html('无');
        				}
            			if(__CurTr.id){
            				$(target).before('<span style="padding-right: 15px;" id="'+__CurTr.id+'">'+__CurTr.name+'</span>');
            				if(__CurTr.env){
		            			if(type=='nas'){//nas需要输入挂载点
		            				var volumnHtml = '<input type="hidden" class="row-td" name="capacity" value="'+__CurTr.env.capacity+'">';
// 		            				volumnHtml += '<input type="hidden" name="allocateContent" value="'+__CurTr.env.allocateContent+'">';
		            				volumnHtml += '<input type="hidden"  class="row-td" name="volumnId" value="'+__CurTr.id+'">';
		            				volumnHtml += '挂载点：<input type="text"   class="row-td" name="mount" style="width:40px;"/>';
// 		            				volumnHtml += '容&nbsp;&nbsp;&nbsp;量：<input type="text" name="capacity" style="width:40px;"/>';
		            				nasInitTd.html(volumnHtml).data('allocateContent',__CurTr.env.allocateContent);
		            			}else{
	            					$.each(__CurTr.env,function(i,v){
	            						v.resourceId = __CurTr.id;
	            						$('#envVariable').find('.addRow').trigger('click',v);
	            					});
		            			}
            				}
            			}
            			__layerObj.close();
        			}});
       			 __dialog = $("#resourceDialog");
       			toResourceList(target,type);
        	}
        	
        	
        	var openResourceAddDialog = function(type){//创建资源弹出框
        		var layerObj = common.dialogHtml("创建资源",'<div id="resourceAddDialog" style="padding:10px;overflow:auto;"></div>', 900, $(window).height()*0.9,['关闭'],{
        			btn1:function(){ 
        				layerObj.close();
        				toResourceList(__target,type);
        			}});
        		$("#resourceAddDialog").load(WEB_APP_PATH+"/resource/"+type+"/add.do",{
        			dialog : true
        		});
        	}
        	
        	
        	var toResourceList = function(target,type){//到资源列表
       			__target = target;
        		__dialog.load(WEB_APP_PATH+"/resource/"+type+"/list.do?listType=tenant",{
        			dialog : true
        		});
        	}
        	
        	
        	
        	var mysqlInit  = function(id,number,_this){//数据库初始化弹出框
        		var executeSql = function(){//运行sql 文件
					this.ing = false;
					this.execute = function(){
						if(this.ing){
							common.alert("正在执行中！",0);
							return false;
						}
						var _this = this;
						_this.ing = true;
						var loadObj = common.loading();
						$.post(WEB_APP_PATH+"/resource/mysql/executeSql.do",{
    						storeId : id,
    						number : number,
    						url : resourceDiv.find("input[name='url']").val(),
    						username : resourceDiv.find("input[name='username']").val(),
    						password : resourceDiv.find("input[name='password']").val()
    					},function(json){
    						_this.ing = false;
    						loadObj.close();
    						if(json.code==0){
    							common.tips('执行成功',1,null,1000);
    						}else{
//     							common.alert('执行失败！'+json.msg,2);
    							common.dialogHtml('执行失败','<div style="padding:10px;word-break: break-word;"><pre>'+json.msg+'</pre></div>',700,400);
    						}
    					});
					}
					return this;
				}();
				
        		var resourceSpan = $(_this).parent("td").next().find("span");
        		var resourceId = resourceSpan.length>0 ? resourceSpan.attr("id") : "";
        		var url = WEB_APP_PATH+"/store/app/viewInitFile.do?id="+id+"&number="+number;
        		common.dialogHtml("数据库初始化",'<div id="initFileDialog" style="padding:10px;overflow:auto;height:55%;border-bottom:1px solid #ccc;"><pre id="fileContent"></pre></div><div id="resConnectionDiv" style="padding:10px;"></div>', 900, $(window).height()*0.9,['运行','关闭'],{
        			yes:function(index,context){
        				if(resourceId){
        					if(needDatabase){//需要选择一个库database
        						if(!$("#schemetas").val()){
        							common.alert('请选择一个database。',0);
        							return false;
        						}
        					}else{
// 	        					alert("已复制脚本到剪切板，将跳转至mysql管理界面执行sql。");
        					}
        					
        					executeSql.execute();
        				}else{
        					common.alert("请选择数据库资源后，再运行初始化脚本！",0);
        				}
        				return false;
        			}});
        		
        		var resourceDiv = $("#resConnectionDiv");
        		var needDatabase = true;
        		
        		var loadScript  = function(){
	        		$("#fileContent").load(url,null,function(data){//加载脚本内容
	        			if(resourceId && data){
	        				var cd = data.match(/create\s+database/i);
	        				if(cd && cd.length>0){
	        					needDatabase = false;
	        				}else{//加载数据库database信息，初始化数据库操作
	        					resourceDiv.append('<p>请选择database：<select id="schemetas"/>&nbsp;&nbsp;创建新库：<input type="text" name="databaseName"/>&nbsp;&nbsp;<a href="javascript:void(0);" id="addDbBtn">创建</a></p>');
	        					$.get(WEB_APP_PATH+"/resource/mysql/getSchemeta.do",{
	        						url : resourceDiv.find("input[name='url']").val(),
	        						username : resourceDiv.find("input[name='username']").val(),
	        						password : resourceDiv.find("input[name='password']").val()
	        					},function(json){
	        						var optStr = '<option value="">--请选择--</option>';
	        						if(json){
	        							$.each(json,function(i,v){
	        								optStr += '<option>'+v+'</option>';
	        							})
	        						}
	        						$("#schemetas").html(optStr);
	        					})
	        					$("#schemetas").change(function(){//切换库时改变url 
	        						var database = $(this).val();
	        						var url = resourceDiv.find("input[name='url']").val();
	        						var splits = url.split('?');
	        						url = splits[0]+"/"+database+"?"+splits[1];
	        						resourceDiv.find("input[name='url']").val(url);
	        					})
	        					$("#addDbBtn").click(function(){//创建库
	        						var _this = this;
	        						$(this).hide();
	        						var databaseName = resourceDiv.find('input[name="databaseName"]').val()
	        						if(databaseName){
	        							$.post(WEB_APP_PATH+"/resource/mysql/createDatabase.do",{
	        								databaseName : databaseName,
	        								url : resourceDiv.find("input[name='url']").val(),
	    	        						username : resourceDiv.find("input[name='username']").val(),
	    	        						password : resourceDiv.find("input[name='password']").val()
	        							},function(json){
	        								$(_this).show();
	        								resourceDiv.find('input[name="databaseName"]').val('');
	        								if(json.code==0){
	        									$("#schemetas").append('<option selected>'+databaseName+'</option>');
	        								}else{
	        									common.alert("创建失败！",2);
	        								}
	        							});
	        						}
	        					});
	        				}
	        				//设置样式
	        				$("#initFileDialog").css("height","55%");
	        				resourceDiv.find("p :gt(0)").css({
        						'line-height' : '40px'
        					});
	        				resourceDiv.find("p label").css({
	        					width : '50px',
	        				    display : 'inline-block'
	        				});
	        			}
	        		});
        		}
        		
        		if(!resourceId){
        			loadScript();
        			resourceDiv.html("请选择数据库资源后，运行初始化脚本。");
        		}else{
        			var resourceName = resourceSpan.html();
        			resourceDiv.html("<p style='line-height:25px;'>当前选择数据库："+resourceName+"</p>");
        			//展示连接信息
        			$.get(WEB_APP_PATH+"/resource/mysql/getMysqlInfo.do",{mysqlId:resourceId},function(json){
        				if(json && json.allocateContent){
        					/* resourceDiv.data("connection",{
        						url : 
        					}) */
        					var conn = JSON.parse(json.allocateContent).connection;
        					var url = "jdbc:mysql://"+conn.externalIP+":"+conn.externalPort+"?useUnicode=true&characterEncoding=UTF-8";
        					resourceDiv.append('<p><label>url：</label><input type="text" name="url" value="'+url+'"/></p>');
        					resourceDiv.append('<p><label>用户名：</label><input type="text" name="username" value="'+conn.account+'"/></p>');
        					resourceDiv.append('<p><label>密码：</label><input type="text" name="password" value="'+conn.password+'"/></p>');
        					loadScript();
        				}
        			})
        		}
        		
        	}
        	
        	
        	
        	var getResourceConfig = function(){//获取申请后的依赖资源，如果有nas，返回数据卷数组
        		var volumn = [];
        		resourceConfigBody.find("tr").each(function(i){
        			var id = $(this).find("td:last span").attr("id");
        			var name = $(this).find("td:last span").html();
        			var type = $(this).find("td:eq(1)").attr("data-type");
        			resourceConfig[i].resource = {
        				id : id,
        				name : name
        			}
        			if(type == 'nas'){
        				var td3 = $(this).find("td:eq(3)");
        				var volumnId = td3.find("input[name='volumnId']").val();
        				var mount = td3.find("input[name='mount']").val();
        				var capacity = td3.find("input[name='capacity']").val();
        				var allocateContent = td3.data('allocateContent');
        				volumn.push({
        					type : 'nfs',
        					mount : mount,
        					capacity : capacity,
        					volumnId : volumnId,
        					allocateContent : allocateContent
        				})
        				$('#envVariable').find('.addRow').trigger('click',{
        					key : "PAASOS_NFS_PATH",
        					value : mount,
        					resourceId : volumnId
        				});
        			}
        		})
        		return volumn;
        	}
        	
        	
        	
        	
        	
        	$(function(){
        		var checkResource = function(){
        			if(!$("#buildId").val()){
						common.alert("请选择镜像！");
						return false;
					}
					return true;
        		};
				<c:if test="${project.projectType == 'store'}">
					if(!'${storeApp.imageId}'){
						common.alert("没有构建镜像！",function(){
							window.history.back();
						});
					}

					
					
					//渲染依赖资源
					resourceConfig = ${resourceConfig};
					if(resourceConfig){
// 						resourceConfig = $.parseJSON(resourceConfig);
						var configCon='';
						$.each(resourceConfig,function(i,v){
				    		configCon += '<tr>';
				    		configCon += '<td>'+v.number+'</td>';
				    		var type = "";
				    		var initAction = "无";
				    		var resType = "";
				    		var resId = '';
				    		var resName = '';
				    		if(v.type=='db'){
				    			type = "数据库";
				    			resType = "mysql";
				    			if(typeof v.initAction.file != 'undefined' && v.initAction.file){//数据库初始化脚本
				    				initAction = '<a href="javascript:void(0);" onclick="mysqlInit(\'${project.businessId}\','+v.number+',this);">'+v.initAction.file.fileName+'</a>';
				    			}
				    			resId = '${resourcesMap["db"].paasosResId}';
				    			resName = '${resourcesMap["db"].attach["node"].name}';
				    		}else if(v.type=='mq'){
				    			type = "消息中间件";
				    			resType = "messagequeue";
				    			resId = '${resourcesMap["mq"].paasosResId}';
				    			resName = '${resourcesMap["mq"].attach["node"].name}';
				    		}else if(v.type=='cache'){
				    			type = "高速缓存";
				    			resType = "redis";
				    			resId = '${resourcesMap["cache"].paasosResId}';
				    			resName = '${resourcesMap["cache"].attach["node"].name}';
				    		}else if(v.type=='nas'){
				    			type = "NAS存储";
				    			resType = "nas";
				    			resId = '${resourcesMap["nas"].paasosResId}';
				    			resName = '${resourcesMap["nas"].attach["node"].name}';
				    		}
				    		configCon += '<td data-type="'+resType+'">'+type+'</td>';
				    		configCon += '<td>'+v.desc+'</td>';
				    		configCon += '<td>'+initAction+'</td>';
				    		configCon += '<td><span style="padding-right: 15px;" id="'+resId+'">'+resName+'</span></td>';
// 			    			configCon += '<td><a href="javascript:void(0);" onclick="openResourceDialog(this,\''+resType+'\');">申请</a></td>';
				    		configCon += '</tr>';
				    	})
				    	resourceConfigBody.html(configCon);
						$("#configTableModel").show();
					}
					
					checkResource = function(){//校验依赖资源是否全部申请
						var check = true;
						resourceConfigBody.find("tr").each(function(i){
							if($(this).find("td:last span").length==0){
								common.alert('依赖资源 '+(i+1)+' 尚未申请！',2);
								check = false;
								return false;
							}
							/* if($(this).find("td:eq(1)").attr("data-type")=='nas'){
								if(!$.trim($(this).find("input[name='mount']").val())){
									common.alert('请为NAS资源设置挂载点！',2);
									check = false;
									return false;
								}
							} */
						})
						return check;
					}
				</c:if>

        		$('#buildId').bind('change',function(){
        			
        			
        		//	$('#envVariable').find('.row-data[id^=MT_]').remove();//日志收集用的  URL对应调用的方法 值针对dubbo
						//	$('#envVariable').find('.row-data[id^=IT_]').remove();//日志收集用的 URL对应接口id
						//	$('#envVariable').find('.row-data[id^=PI_]').remove();//日志收集用的 URL对应接口id
						//	$('#envVariable').find('#VERSION_ID').remove();//日志收集用的 URL对应接口id
							$('#envVariable').find('.row-data[resourceid^=version_]').remove();
							$('#ports').find('.deleteRow').trigger('click');
							if($(this).val()==null || $(this).val()==''){
		        		return ;
		        			}
						//	$('#ports').find('.row-data').remove();
		        	var versionId = $(this.options[this.selectedIndex]).attr('versionid');

		        	
        			

        			var vo = {
    	        			url : '${WEB_APP_PATH}/project/deploy/version.do',
    	 							type : 'POST',
    	 							data : {
   	 	        					'projectId' : '${project.projectId}',
   	 	        					'versionId' : versionId,
   	 	        					'envId' : '${env.envId}',
   	 	        					'envType' : '${env.envType}'
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
        						$('#ports').find('.addRow').trigger('click',[{
        							'portProtocol' : msg[i].protocol==null?'':msg[i].protocol,
        							'targetPort' : msg[i].port==null?'':msg[i].port,
        							'nodePort' : msg[i].nodePort==null?'':msg[i].nodePort,
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
					if(!checkResource())	return false;

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
		//			console.log(d);
		//			return ;
					var $form = $('#deploy_area');
					var param = {};
// 					param.volumn = getResourceConfig();
					param.resourceConfig = JSON.stringify(resourceConfig);
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
 							contentType : 'application/json'
 					};
					
 					var load = common.loading('保存中......');
 					var def = $.ajax(option);
 					def['done'](function(json){
 						if(json.code==0){
 							if("${isDialog }"=='true'){
 								load.close();
 								toDeployView(json.data);
 							}else{
				    			common.tips("应用部署正在后台创建中......",1,function(){
					    			common.forward("${ WEB_APP_PATH }/project/env/index.do?projectId=${project.projectId}");
				    			});
 							}
			    		}else{
			    			load.close();
			    			common.alert(json.msg,null,null,200000);
			    		}
 					});
				});
        	});
        </script>
 				