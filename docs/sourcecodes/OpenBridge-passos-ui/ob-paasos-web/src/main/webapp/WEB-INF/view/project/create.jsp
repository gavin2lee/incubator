<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp" nav-header="project">
	<template:replace name="title">
		项目管理
	</template:replace>
	<template:replace name="content">
		<style>
			#projectform .formdiv{
				display: none;
			}
			.project_r #projectform .active{
				display: block;
			}
		</style>
		<div class="app_name">
            <a href="${WEB_APP_PATH }"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="index.do">项目管理</a></p>
            <em>&gt;</em>

            <p class="app_a">我的项目</p>
        </div>
        <div class="plate">
            <div class="project_r">
                <!--创建项目开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line"><i class="icons add_ico_blue mr5"></i>创建项目</h3>

                        <div class="title_line"></div>
                    </div>
                    <div class="r_con p10_0">
                        <div class="form_control p20">
							<c:set var="projectType"  value="${ empty projectType ? 'api' : projectType }"></c:set>
                            <div class="form_block">
                                <label>项目来源</label>

                                <div id="specList" class="specList">
                                    <p class="mb10">
                                    	<span class="w150"><input ${projectType=='api' ? 'checked="checked"':''}  target="form_api" type="radio" name="iCheck" checked>&nbsp;&nbsp;OpenBridge API</span>
                                     <span class="w150"><input ${projectType=='app' ? 'checked="checked"':''}  target="form_app" type="radio" name="iCheck">&nbsp;&nbsp;OpenBridge APP</span>
                                    	<span class="w150"><input ${projectType=='store' ? 'checked="checked"':''}  target="form_store" type="radio" name="iCheck">&nbsp;&nbsp;预置应用</span>
                                    	<!-- 
                                    	<span class="w150"><input ${projectType=='source' ? 'checked="checked"':''}  target="form_source" type="radio" name="iCheck">&nbsp;&nbsp;源码项目</span>
                                    	 -->
                                    	</p>
                                </div>
                            </div>
                            
                            
                            
							<div id="projectform">
								<div class="${projectType=='api' ? 'active':''} formdiv"  id="form_api">
	                                <form action="">
	                                	<div class="form_block">
			                                <label>项目编码</label>
											<div>
			                                    <input type="text" name="projectCode" readonly="readonly" maxlength="15" /> <em color="red">*</em>
		                                        <p style="margin-top:10px; margin-left:200px;">全局唯一的项目编码，<em style="color:red">只能是数字和小写字母,不能超过15个字符</em>，项目构建的Docker镜像会使用这个编码。</p>
			                                </div>
			                            </div>
	                                	<div class="form_block">
		                                	<input type="hidden"  name="projectType" value="api">
		                                	<label style="float:left;">关联服务</label>		
					                     	<div style="float:left;position: relative;width:400px;">			
					                     		<div targetShow="projectName" targetName="businessId"  style="width: 264px;min-height:30px;border:solid 1px #ccc;" class="col-xs-10 col-sm-5 tags" > 
					                     		</div>
					                     		<button type="button"  style="position:absolute; bottom:0;top:0;left:275px;" class="btn btn-default btn-sm btn-yellow2 selectrelation api">选择</button>
					                     	</div>
					                     	<div style="clear:both;"></div>
				                     	</div>
				                     	<div class="form_block">
			                                <label>项目描述</label>
											<div>
												<textarea rows="5" cols="50" name="description" maxlength="200"></textarea>
			                                </div>
			                            </div>
	                                </form>
	                            </div>
                            	
                            	<div class="${projectType=='app' ? 'active':''} formdiv" id="form_app">
	                                <form action="">
	                                	<div class="form_block">
			                                <label>项目编码</label>
											<div>
			                                    <input type="text" name="projectCode"  readonly="readonly"  maxlength="15"/> <em color="red">*</em>
		                                        <p style="margin-top:10px; margin-left:200px;">全局唯一的项目编码，<em style="color:red">只能是数字和小写字母,不能超过15个字符</em>，项目构建的Docker镜像会使用这个编码。</p>
			                                </div>
			                            </div>
	                                	<div class="form_block">
		                                	<input type="hidden"  name="projectType" value="app">
		                                	<label style="float:left;">关联应用</label>		
					                     	<div style="float:left;position: relative;width:400px;">			
					                     		<div targetShow="projectName"  targetName="businessId" style="width: 264px;min-height:30px;border:solid 1px #ccc;" class="col-xs-10 col-sm-5 tags" > 
					                     		</div>
					                     		<button type="button"  style="position:absolute; bottom:0px;top:0px;left:275px;" class="btn btn-default btn-sm btn-yellow2 selectrelation app">选择</button>   
					                     	</div>
					                     	<div style="clear:both;"></div>
					                     </div>
					                     <div class="form_block">
			                                <label>项目描述</label>
											<div>
												<textarea rows="5" cols="50" name="description" maxlength="200"></textarea>
			                                </div>
			                            </div>
	                                </form>
	                            </div>
	                            
	                            <div class="${projectType=='store' ? 'active':''} formdiv" id="form_store">
	                                <form action="">
	                                	<div class="form_block">
			                                <label>项目编码</label>
											<div>
			                                    <input type="text" name="projectCode"  maxlength="15" /> <em color="red">*</em> 
												<p style="margin-top:10px; margin-left:200px;">全局唯一的项目编码，<em style="color:red">只能是数字和小写字母,不能超过15个字符</em>，项目构建的Docker镜像会使用这个编码。</p>
			                                </div>
			                            </div>
	                                	<div class="form_block">
		                                	<input type="hidden"  name="projectType" value="store">
		                                	<label style="float:left;">关联应用</label>		
					                     	<div style="float:left;position: relative;width:400px;">			
					                     		<div targetShow="projectName"  targetName="businessId" style="width: 264px;min-height:30px;border:solid 1px #ccc;" class="col-xs-10 col-sm-5 tags" > 
					                     		</div>
					                     		<button type="button"  style="position:absolute; bottom:0px;top:0px;left:275px;" class="btn btn-default btn-sm btn-yellow2 selectrelation store">选择</button>   
					                     	</div>
					                     	<div style="clear:both;"></div>
					                    </div>
					                    <div class="form_block">
			                                <label>项目描述</label>
											<div>
												<textarea rows="5" cols="50" name="description" maxlength="200"></textarea>
			                                </div>
			                            </div>
	                                </form>
	                            </div>
	                            
	                            
	                            <div class="${projectType=='source' ? 'active':''} formdiv" id="form_source">
	                                <form action="">
	                                	<div class="form_block">
			                                <label>项目编码</label>
																				 <div>
																				 		<input type="hidden"  name="projectType" value="source">
																						<input type="text" name="projectCode"  maxlength="15" /> <em color="red">*</em> 
																						<p style="margin-top:10px; margin-left:200px;">全局唯一的项目编码，<em style="color:red">只能是数字和小写字母,不能超过15个字符</em>，项目构建的Docker镜像会使用这个编码。</p>
			                                </div>
			                            	</div>
	                                	<div class="form_block">
																				<label>源码来源</label>	
																				<div class="specList">
			                                    <ul class="u-flavor">
			                                        <!--Regular list-->
			                                        <li class="selected" value="Github">Github</li>
			                                        <li class="" value="GitLab">GitLab</li>
			                                    </ul>
			                                </div>	
					                    				</div>
					                    				<div class="form_block">
				                                <label>源码配置</label>
				                                <div class="specList">
				                                    <dl class="dl_label">
				                                        <dt>开发语言</dt>
				                                        <dd>
				                                            <span><label><input name="devlanguage" value="java" type="checkbox">Java</label></span>
				                                            <span><label><input name="devlanguage" value="php" type="checkbox">PHP</label></span>
				                                        </dd>
				                                    </dl>
				                                    <dl class="dl_label">
				                                        <dt>项目类型</dt>
				                                        <dd>
				                                            <span><label><input name="buildType" type="checkbox">Maven</label></span>
				                                            <span><label><input name="buildType" type="checkbox">Gradle</label></span>
				                                        </dd>
				                                    </dl>
				                                </div>
				                            </div>
				                            <div class="form_block">
				                                <label>仓库授权</label>
				
				                                <div class="specList">
				                                  <input type="text"  id="projectRepository" name="projectRepository"> <a href="javascript:void(0);" id="grant">授权</a>
				                                </div>
				                            </div>
					                    				<div class="form_block">
			                                <label>项目描述</label>
																				 <div>
																						<textarea rows="5" cols="50" name="description" maxlength="200"></textarea>
			                                </div>
			                             </div>
	                                </form>
	                            </div>
	                            
                            </div>
                            <div class="form_block mt10">
                                <label>&nbsp;</label>
                                <a href="javascript:void(0);" class="btn btn-default btn-yellow f16  mt10" id="createRelation"><i class="ico_check"></i>创 建</a>
                            </div>
                        </div>
                    </div>
                </div>
                <!--创建项目结束-->
            </div>
        </div>
		
 		<script>
			var appId = '2987d5e49e1362849a3191dc388dc178960ce58544f5a76ec29f112a3f885d78';
			var redirectUrl = 'http://192.168.31.22:8288/os';
			String.prototype.endWith=function(str){
				if(str==null||str==""||this.length==0||str.length>this.length)
					return false;
				if(this.substring(this.length-str.length)==str)
					return true;
				else
					return false;
				return true;
			}

			
	 		
 			$(function(){
 				$('.u-flavor').find('li').bind('click',function(){
					$(this).parent().children().removeClass('selected');
					$(this).addClass('selected');
        		});
				$('.u-flavor').each(function(domIndex,dom){
					$(dom).find('li:first').trigger('click');
        		});

				$('#grant').bind('click',function(){
					var ry = $('#projectRepository').val();
					ry=$.trim(ry);
					if(ry==null || ry==''){
						common.tips("请选择仓库地址！");
 						return false;
					}
					var url = '';
					if(ry.endWith('/')){
						url = ry+'oauth/authorize';
					}
					else{
						url = ry+'/oauth/authorize';
					}
						var iWidth=720;                          //弹出窗口的宽度; 
			     var iHeight=600;                         //弹出窗口的高度; 
					 var iTop = (window.screen.availHeight - 30 - iHeight) / 2; 
					 var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; 
					url = url+'?client_id='+appId+'&redirect_uri='+redirectUrl+'&response_type=code&state=your_unique_state_hash';
					window.open (url,'授权','height='+iHeight+',width='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no') 
				});

 				
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
 						if(json.code==0){
			    			common.tips("成功创建应用",1,function(){
				    			common.forward("${WEB_APP_PATH}/project/index.do");
			    			});
			    		}else{
			    			load.close();
			    			common.alert(json.msg);
			    		}
 					});
 				});
 				$('#specList').find('input[name=iCheck]').bind('click',function(){
 					$.each($("input[name=projectCode]"),function(index,item){
 						$(item).val("");
 					});
 					var target = $(this).attr('target');
 					$('#projectform').find('.active').removeClass('active');
 					$('#'+target).addClass('active');
 				});
 				
 				$('#form_api,#form_app,#form_store').find('.selectrelation').bind('click',function(){
 					//var title = $(this).hasClass('app') ? '关联应用' : ($(this).hasClass('api')? '关联服务' : '关联');
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
 					//var type = $(this).hasClass('app') ? 'app' : ($(this).hasClass('api')? 'api' : '');
 					var relationIds = [];
 					$(this).parent().find('.tag').each(function(domIndex,domEle){
 						var j = {};
 						j['id'] = $(domEle).attr('user-nid');
 						relationIds.push(j);
 					});
 					var _self =this;
 					var dialog = common.dialogIframe(title,"${WEB_APP_PATH}/project/listRelation.do?status=10&multiple=false&type="+type,600,620,null,['确定','取消'],{
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
 			    					if(type!="store"){ 			    						
 			    					 projectCode=type+obj[i].code;
 			    					$("input[name=projectCode]").val(projectCode); 
 			    					}
 			    				}
 			        		}
 						},
 						btn2 : function(){
 							
 						}
 					});
 					dialog.getIFrame().params = relationIds;
 					//按照上文，写死了，为10，也就是只有部署成功才行。
 					dialog.getIFrame().passStatus = 10;
 				});
 			});
 		</script>
	</template:replace>
</template:include>