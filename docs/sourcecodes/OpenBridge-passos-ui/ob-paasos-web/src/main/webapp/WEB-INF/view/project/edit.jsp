<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="base.jsp" nav-left="overview">
	<template:replace name="title">
		项目管理
	</template:replace>
	<template:replace name="content-body">
		<style>
			#projectform .formdiv{
				display: none;
			}
			.project_r #projectform .active{
				display: block;
			}
		</style>
		<div class="app_name">
            <a href="proiect_list.html"><i class="icons go_back_ico"></i></a>

            <p class="app_a">项目管理</p>
            <em>&gt;</em>

            <p class="app_a">我的项目</p>
        </div>
        <div class="plate">
            <div class="project_r">
                <!--创建项目开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line"><i class="icons add_ico_blue mr5"></i>编辑项目</h3>

                        <div class="title_line"></div>
                    </div>
                    <div class="r_con p10_0">
                        <div class="form_control p20">
                            <div class="form_block">
                                <label>项目来源</label>

                                <div id="specList" class="specList">
                                    <p class="mb10">
                                    	<c:if test="${project.projectType=='api'}">
                                    		<span class="w200"><input checked="checked" target="form_api" type="radio" name="iCheck" >&nbsp;&nbsp;OpenBridge API</span>
                                    	</c:if>
                                    	<c:if test="${project.projectType=='app'}">
                                    		<span class="w200"><input checked="checked" target="form_app" type="radio" name="iCheck">&nbsp;&nbsp;OpenBridge APP</span>
                                    	</c:if>
                                    	<c:if test="${project.projectType=='store'}">
                                    		<span class="w150"><input checked="checked" target="form_store" type="radio" name="iCheck">&nbsp;&nbsp;预置应用</span>
                                    	</c:if>
                                    </p>
                                </div>
                            </div>
							<div id="projectform">
								<div class="${project.projectType=='api' ? 'active':''} formdiv"  id="form_api">
	                                <form action="">
	                                	<div class="form_block">
			                                <label>项目编码</label>
											<div>
			                                    <input type="text"  value="${project.projectCode }" name="projectCode" maxlength="15"  readonly="readonly" />
			                                </div>
			                            </div>
			                            <div class="form_block">
		                                	<input type="hidden"  name="projectId" value="${project.projectId }">
		                                	<input type="hidden"  name="projectType" value="api">
		                                	<label style="float:left;">关联服务</label>		
					                     	<div style="float:left;position: relative;width:400px;">			
					                     		<div targetShow="projectName"  targetName="businessId"  style="width: 264px;min-height:30px;border:solid 1px #ccc;" class="col-xs-10 col-sm-5 tags" > 
					                     			<c:if test="${project.projectType=='api'}">
					                     				<span class="tag" user-nid="${project.businessId }" user-uid="${project.projectName}">${project.projectName}<button type="button" class="close" onclick="removeSelect(this)">×</button></span>
					                     			</c:if>
					                     		</div>
					                     		<!-- <button type="button"  style="position:absolute; bottom:0px;top:0px;left:275px;" class="btn btn-default btn-sm btn-yellow2 selectrelation api">选择</button> -->
					                     	</div>
					                     	<div style="clear:both;"></div>
				                     	</div>
				                     	<div class="form_block">
			                                <label>项目描述</label>
											<div>
												<textarea rows="5" cols="50" name="description" maxlength="200">${project.description}</textarea>
			                                </div>
			                            </div>
	                                </form>
	                            </div>
                            	
                            	<div class="${project.projectType=='app' ? 'active':''} formdiv" id="form_app">
	                                <form action="">
	                                	<div class="form_block">
			                                <label>项目编码</label>
											<div>
			                                    <input type="text"  value="${project.projectCode }" name="projectCode" maxlength="15" readonly="readonly" />
			                                </div>
			                            </div>
	                                	<div class="form_block"  >
		                                	<input type="hidden"  name="projectType" value="app">
		                                	<input type="hidden"  name="projectId" value="${project.projectId }">
		                                	<label style="float:left;">关联应用</label>		
					                     	<div style="float:left;position: relative;width:400px;">			
					                     		<div targetShow="projectName"  targetName="businessId" style="width: 264px;min-height:30px;border:solid 1px #ccc;" class="col-xs-10 col-sm-5 tags" > 
					                     			<c:if test="${project.projectType=='app'}">
					                     				<span class="tag" user-nid="${project.businessId }" user-uid="${project.projectName}">${project.projectName}<button type="button" class="close" onclick="removeSelect(this)">×</button></span>
					                     			</c:if>
					                     		</div>
					                     		<!-- <button type="button"  style="position:absolute; bottom:0px;top:0px;left:275px;" class="btn btn-sm btn-yellow2 selectrelation app">选择</button> -->
					                     	</div>
					                     	<div style="clear:both;"></div>
				                     	</div>
				                     	<div class="form_block">
			                                <label>项目描述</label>
											<div>
												<textarea rows="5" cols="50" name="description" maxlength="200">${project.description}</textarea>
			                                </div>
			                            </div>
	                                </form>
	                            </div>
	                            
	                            <div class="${project.projectType=='store' ? 'active':''} formdiv" id="form_store">
	                                <form action="">
	                                	<div class="form_block">
			                                <label>项目编码</label>
											<div>
			                                    <input type="text"  value="${project.projectCode }" name="projectCode" maxlength="15"/>
			                                </div>
			                            </div>
	                                	<div class="form_block">
		                                	<input type="hidden"  name="projectId" value="${project.projectId }">
		                                	<input type="hidden"  name="projectType" value="store">
		                                	<label style="float:left;">关联应用</label>		
					                     	<div style="float:left;position: relative;width:400px;">			
					                     		<div targetShow="projectName"  targetName="businessId" style="width: 264px;min-height:30px;border:solid 1px #ccc;" class="col-xs-10 col-sm-5 tags" > 
					                     			<c:if test="${project.projectType=='store'}">
					                     				<span class="tag" user-nid="${project.businessId }" user-uid="${project.projectName}">${project.projectName}<button type="button" class="close" onclick="removeSelect(this)">×</button></span>
					                     			</c:if>
					                     		</div>
					                     		<button type="button"  style="position:absolute; bottom:0px;top:0px;left:275px;" class="btn btn-default btn-sm btn-green selectrelation store">选择</button>   
					                     	</div>
					                     	<div style="clear:both;"></div>
		                                </div>
		                                <div class="form_block">
			                                <label>项目描述</label>
											<div>
												<textarea rows="5" cols="50" name="description" maxlength="200">${project.description}</textarea>
			                                </div>
			                            </div>
	                                </form>
	                            </div>
                            </div>
                            <div class="form_block mt10">
                                <label>&nbsp;</label>
                                <a href="javascript:void(0);" class="btn btn-default btn-yellow f16  mt10" id="createRelation"><i class="ico_check"></i>保存</a>
                            </div>
                        </div>
                    </div>
                </div>
                <!--创建项目结束-->
            </div>
        </div>
		
 		<script>
 			$(function(){
 				$('#createRelation').bind('click',function(){
 					var filter= /^[-a-z0-9]*[a-z0-9]+$/g;
 					var projectCode ;
 					$.each($("input[name=projectCode]"),function(index,item){
 						if($(item).val()){
 							projectCode = $(item).val();
 						};
 					})
 					
 					if(!projectCode){
 						common.tips("项目编码不能为空！");
 						return false;
 					}else if(projectCode.length >55){
 						common.tips("项目编码长度不能超过55！");
 						return false;
 					}
 					if(!filter.test(projectCode)){
 						common.tips('项目编码只能为数字和小写字母和中划线(不能以中划线结尾)');
 						return false;
 					}
 					
 					var $form = $('#projectform').find('.active');
 					var param = {};
 					$form.find(':input').each(function(domIndex,domEle){
 						var _name = $(domEle).attr('name');
 						var _v  = $(domEle).val();
 						if(_name==null || _v ==null || _v==''){
 							return true;
 						}
 						param[_name] = _v;
 					});
 					$form.find('.tags').each(function(domIndex,domEle){
 						var _name = $(domEle).attr('targetName');
 						var _show = $(domEle).attr('targetShow');
 						var a = [];
 						var b = [];
 						$(domEle).find('.tag').each(function(domIndex1,domEle1){
 							a.push($(domEle1).attr('user-nid'));
 							b.push($(domEle1).attr('user-uid'));
 						});
 						param[_name] = a.join(',');
 						param[_show] = b.join(',');
 					});
 					var option = {
 							url : '${WEB_APP_PATH}/project/save.do',
 							type : 'POST',
 							data : param,
 							cache : false,
 							dataType : 'json'
 					};
 					var load = common.loading('保存中......');
 					var def = $.ajax(option);
 					def['done'](function(json){
 						load.close();
 						if(json.code==0){
			    			common.tips("成功修改应用",1,function(){
				    			common.forward("${WEB_APP_PATH}/project/overview/index.do?projectId=${project.projectId}");
			    			});
			    		}else{
			    			load.close();
			    			common.tips(json.msg);
			    		}
 					});
 				});
 				$('#specList').find('input[name=iCheck]').bind('click',function(){
 					var target = $(this).attr('target');
 					$('#projectform').find('.active').removeClass('active');
 					$('#'+target).addClass('active');
 				});
 				
 				$('#form_api,#form_app,#form_store').find('.selectrelation').bind('click',function(){
 					var titile = '';
 					if($(this).hasClass('app')){
 						title = '关联应用';
 					}
 					else if($(this).hasClass('api')){
 						title = '关联服务';
 					}
 					else if($(this).hasClass('store')){
 						title = '预置应用';
 					}
 					var type = '';
 					if($(this).hasClass('app')){
 						type = 'app';
 					}
 					else if($(this).hasClass('api')){
 						type = 'api';
 					}
 					else if($(this).hasClass('store')){
 						type = 'store';
 					}
 					var relationIds = [];
 					$(this).parent().find('.tag').each(function(domIndex,domEle){
 						var j = {};
 						j['id'] = $(domEle).attr('user-nid');
 						relationIds.push(j);
 					});
 					var _self =this;
 					var dialog = common.dialogIframe(title,"${WEB_APP_PATH}/project/listRelation.do?multiple=false&type="+type,600,620,null,['确定','取消'],{
 						btn1 : function(index,layero){
 							var iwin = layero.find("iframe").get(0).contentWindow;
 							if(!(iwin.dialogClose && typeof(iwin.dialogClose) === 'function')){
 								return ;
 							}
 							var obj = (iwin.dialogClose());
 							var textObj = $(_self).parent().children('.tags');
 							textObj.empty();
 							if(obj!=null){
 								var a = [];
 								for(var i=0;i<obj.length;i++){ 
 			    					textObj.append('<span class="tag" user-nid="'+obj[i].id+'" user-uid="'+obj[i].name+'" >'+obj[i].name+'<button type="button" class="close" onclick="removeSelect(this)">×</button></span>');	
 			    					a.push(obj[i].id);
 			    				}
 			        		}
 						},
 						btn2 : function(){
 							
 						}
 					});
 					dialog.getIFrame().params = relationIds;
 				});
 			});
 		</script>
	</template:replace>
</template:include>