<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="build">
	<template:replace name="title">
		项目概述
	</template:replace>
	<template:replace name="content-body">
		<style>
			.row-data td{
				width: 150px;
			}
			.row-data input,.row-data select{
				width: 130px;
			}
		</style>
	     <div class="app_name">
            <a href="${WEB_APP_PATH }"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="${WEB_APP_PATH }/project/index.do">项目管理</a></p>
            <em>&gt;</em>

            <p class="app_a">
            	<a href="${WEB_APP_PATH }/project/overview/index.do?projectId=${param.projectId}">${project.projectName }&nbsp;</a>
            </p>
            <em>&gt;</em>
            <p class="app_a">
            	<a href="${WEB_APP_PATH }/project/build/index.do?projectId=${param.projectId}">项目镜像</a>
            </p>
            <em>&gt;</em>

            <p class="app_a">创建构建镜像</p>
        </div>
        <div class="plate">
            <div class="project_r">
            	<form id="build_area">
            	<input type="hidden" name="projectId" value="${project.projectId }"/>
            	<input type="hidden" name="imageName"  id="imageName" />
                <div class="r_block p20">
                	<c:if test="${project.projectType !='api' && project.projectType!='app' }">
	                    <div class="r_block">
	                        <div class="r_title">
	                            <h3 class="f14 h3_color h3_btm_line"><i class="icons add_ico_blue mr5"></i>创建构建镜像</h3>
	                            <div class="title_line"></div>
	                        </div>
	                        <div class="r_con p10_0">
	                            <div class="form_control p20">
	                                <div class="form_block">
	                                    <label>构建名称</label>
	                                    <input type="text"><em>*</em>
	                                </div>
	                                <div class="form_block">
	                                    <label>构建内容</label>
	                                    <div class="specList">
	                                        <p class="mb10"><span class="w200"><input type="radio" name="iCheck" checked>&nbsp;&nbsp;包含dockerfile的zip文件</span>
	                                            <span class="w200"><input type="radio" name="iCheck">&nbsp;&nbsp;上传已构建好的Jar.gz</span></p>
	                                        <p class="mb10"><input type="text"><button class="btn btn-default btn-yellow2 btn-sm">选择</button></p>
	                                    </div>
	                                </div>
	                                <div class="form_block">
	                                    <label>构建tag</label>
	                                    <!--<input class="w100" type="text" value="yihecloud/tomcat" disabled readonly="readonly">-->
	                                    <input class="w200" type="text">
	                                </div>
	                                <div class="form_block">
	                                    <label>启动命令</label>
	                                    <input type="text">
	                                </div>
	                                <div class="form_block">
	                                    <label>工作目录</label>
	                                    <input type="text">
	                                </div>
	                                <div class="form_block">
	                                    <label>构建端口</label>
	                                    <div class="specList">
	                                        <table id="portList" class="table_creat">
	                                            <tr>
	                                                <th>端口号</th>
	                                                <th>类型</th>
	                                                <th colspan="2">说明</th>
	                                            </tr>
	                                            
	                                        </table>
	                                    </div>
	                                </div>
	                                <div class="form_block mt10">
	                                    <label>&nbsp;</label>
	                                    <button class="btn btn-default btn-yellow f16  mt10"><i class="ico_check"></i>创 建</button>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                  </c:if>
                    <!--基于OB构建-->
                    <c:if test="${project.projectType =='api' || project.projectType == 'app' }">
	                    <div class="r_block">
	                        <div class="r_title">
	                            <h3 class="f14 h3_color h3_btm_line"><i class="icons add_ico_blue mr5"></i>创建构建（基于ob构建）</h3>
	                            <div class="title_line"></div>
	                        </div>
	                        <div class="r_con p10_0">
	                            <div class="form_control p20">
	                                <div class="form_block">
	                                    <label>项目来源</label>
	                                    <c:choose>
	                                    	<c:when test="${project.projectType =='api'  }">
	                                    		<input type="text" class="disabled" value="APIManager"  disabled>
	                                    	</c:when>
	                                    	<c:otherwise>
	                                    		<input type="text" class="disabled" value="APPFactory"  disabled>
	                                    	</c:otherwise>
	                                    </c:choose>
	                                </div>
	                                <div class="form_block">
	                                    <label>项目版本</label>
	                                    <select id="buildVersion" name="versionId">
	                                        <option value="">---请选择---</option>
	                                        <c:if test="${ ! empty buildVersion }">
	                                        	<c:forEach items="${buildVersion}" var="row">
	                                        		<c:if test="${fn:length(row.files)>0 }">
	                                        			<option value="${row.versionId }">${row.versionName }</option>
	                                        		</c:if>
	                                        	</c:forEach>
	                                        </c:if>
	                                    </select>
	                                </div>
	                                <div class="form_block"  >
	                                    <label>构建文件</label>
	                                    <select id="buildFile" name="filePath">
	                                        <option value="">---请选择---</option>
	                                    </select>
	                                    
	                                </div>
	                                <div class="form_block"  style="display: none;">
	                                    <label>构建名称</label>
	                                    <input type="text" name="buildName"  value="${project.projectCode }">
	                                </div>
	                                <div class="form_block" style="display: none;">
	                                    <label>构建tag</label>
	                                    <!--<input class="w100" type="text" value="yihecloud/tomcat" disabled readonly="readonly">-->
	                                    <input id="buildTag" class="w200" type="text" name="buildTag"  readonly="readonly">
	                                </div>
	                                <div class="form_block">
	                                    <label>镜像端口</label>
	                                    <div class="specList">
	                                        <table id="portList" class="table_creat">
	                                            <tbody><tr>
	                                                <th style="width: 150px;">端口号</th>
	                                                <th style="width: 150px;">关键字</th>
	                                                <th style="width: 150px;">协议</th>
	                                                <th style="width: 150px;display:none;" >说明</th>
	                                                <th style="display:none;">
	                                                	<a class="addRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">+</em></a>
	                                                </th>
	                                            </tr>
	                                            
	                                        </tbody></table>
	                                    </div>
	                                </div>
	                                <div class="form_block mt10">
	                                    <label>&nbsp;</label>
	                                    <a id="save_button" href="javascript:void(0);" class="btn btn-default btn-yellow f16  mt10"><i class="ico_check"></i>创 建</a>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                 </c:if>
                </div>
                <!--创建构建结束-->
            	</form>
            </div>
        </div>
        
        <script type="text/html" id="portRow">
			<tr class="row-data">
				<td><input disabled="disabled"  name="port" type="text" class="u-ipt row-td"></td>
				<td><input disabled="disabled" name="key" type="text" class="u-ipt row-td"></td>
				<td>
					<select disabled="disabled" name="protocol" class="u-ipt2 row-td">
						<option value="tcp">TCP</option>
						<option value="udp">UDP</option>
					</select>
				</td>
	            <td style="display:none;"><input disabled="disabled" name="remark" type="text" class="u-ipt2 row-td"></td>
	            <td style="display:none;">
	             	<a class="deleteRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">-</em></a>
				</td>
			</tr>
		</script>
        <script>
        	$(function(){
        		$('.addRow').live('click',function(event,data){
        			var $ptbody = $(this).parents('#portList').children('tbody');
        			$ptbody.append($('#portRow').html());
        			if(data!=null){
        				$('#portList').find('.row-data:last').find(':input').each(function(domIndex,domEle){
							var _n = $(domEle).attr('name');
							if(_n==null || _n==''){
								return true;
							}
							$(domEle).val(data[_n]==null ? '' : data[_n]);
						});
        			}
        		});
				$('.deleteRow').live('click',function(){
					var $tr = $(this).parents('.row-data');
        			var $ptr = $tr.parent();
        			$tr.remove();
        		});
				
				var bvs = [];
				<c:if test="${! empty buildVersionStr}">
					bvs = eval('('+'${buildVersionStr}'+')');
				</c:if>
				var ref = {};
				var refImage = {};
				var refPorts = {};
				for(var i=0;i<bvs.length;i++){
					var versionId = bvs[i].versionId;
					ref[versionId] =  bvs[i].files;
					
					refImage[versionId] = bvs[i].imageInfo.imageName;
					refPorts[versionId] = bvs[i].imageInfo.ports;
				}
				
				$('#buildVersion').bind('change',function(){
					var _v = $(this).val();
					var _code = $(this.options[this.selectedIndex]).html();
					if(_v==null || _v==''){
						$('#buildFile').html('<option value="">---请选择---</option>');
						$('#buildFile').trigger('change');
						$('#imageName').val();
						return ;
					}
					var _in = refImage[_v];
					if(_in!=null){
						$('#imageName').val(_in);
					}
					
					var _r = ref[_v];
					if(_r==null){
						$('#buildFile').html('<option value="">---请选择---</option>');
						$('#buildFile').trigger('change');
						return ;
					}
					$('#buildFile').html('<option value="">---请选择---</option>');
					for(var i=0;i<_r.length;i++){
						$('#buildFile').append('<option value="'+_r[i].filePath+'"  filename="'+_r[i].fileName+'"  buildno="'+_r[i].buildNo+'">'+_code+"-"+_r[i].buildNo+'</option>');
					}
					$('#buildFile').trigger('change');
				});
				
				$('#buildFile').bind('change',function(){
					if($(this).val()==''){
						$('#buildTag').val('');
						return ;
					}
					var _f = $(this.options[this.selectedIndex]).text();
					$('#buildTag').val(_f);
				});
				
				function validateForm(){
					if(!$("#buildVersion").val()){
						common.alert("请选择项目版本！");
						return false;
					}
					if(!$("#buildFile").val()){
						common.alert("请选择构建文件！");
						return false;
					}
					return true;
				}
				
				$('#save_button').bind('click',function(){
					if(!validateForm()){
						return ;
					}
					var $form = $('#build_area');
					var param = {};
					$form.find(':input:not(.row-td)').each(function(domIndex,domEle){
 						var _name = $(domEle).attr('name');
 						var _v  = $(domEle).val();
 						if(_name==null || _v ==null || _v==''){
 							return true;
 						}
 						param[_name] = _v;
 					});
					if($('#portList').size()>0){
						var r = [];
						$('#portList').find('.row-data').each(function(domIndex,domEle){
							var j = {};
							$(domEle).find('.row-td').each(function(domIndex1,domEle1){
		 						var _name = $(domEle1).attr('name');
		 						var _v  = $(domEle1).val();
		 						if(_name==null || _v ==null || _v==''){
		 							return true;
		 						}
		 						j[_name] = _v;
		 					});
							if(j.port!=null && j.port!=''){
								r.push(j);
							}
						});
						param.port = JSON.stringify(r);
					}
					if($('#buildVersion').size()>0){
						var sel = $('#buildVersion').get(0);
						var versionCode = sel.options[sel.selectedIndex].text;
						param.versionCode=versionCode;
					}
					if($('#buildFile').size()>0){
						var sel = $('#buildFile').get(0);
						var fileName = $(sel.options[sel.selectedIndex]).attr('filename');
						param.fileName=fileName;
						
						var buildNo = $(sel.options[sel.selectedIndex]).attr('buildno');
						param.buildNo = buildNo;
					}
					
					var option = {
 							url : '${WEB_APP_PATH}/project/build/save.do',
 							type : 'POST',
 							data : param,
 							cache : false,
 							dataType : 'json'
 					};
 					var load = common.loading('保存中......');
 					var def = $.ajax(option);
 					def['done'](function(json){
 						if(json.code==0){
			    			common.tips("后台镜像正在构建中......",1,function(){
				    			common.forward("${ WEB_APP_PATH }/project/build/index.do?projectId=${project.projectId}");
			    			});
			    		}else{
			    			load.close();
			    			common.tips(json.msg);
			    		}
 					});
				});
				
				<c:if test="${ ! empty imagePort}">
					<c:forEach items="${imagePort}" var="row">
						$('.addRow').trigger('click',[{
							port : '${row.port}',
							key : '${row.key}',
							protocol : '${row.protocol}',
							remark : '${row.remark}'
						}]);
					</c:forEach>
				</c:if>
        	});
        </script>
	</template:replace>
</template:include>
