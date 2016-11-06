<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="overview">
	<template:replace name="title">
		项目概述
	</template:replace>
	<template:replace name="content-body">
	     <div class="app_name">
            <a href="${ WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="${ WEB_APP_PATH }/project/index.do">项目管理</a></p>
            <em>&gt;</em>

            <p class="app_a">
            	<a href="${ WEB_APP_PATH }/project/overview/index.do?projectId=${project.projectId}">
            	${project.projectName }&nbsp;
            	</a>
            </p>
            <em>&gt;</em>

            <p class="app_a">项目概述</p>
        </div>
        <div class="plate">
            <div class="project_r">
                
                <!--项目概述开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="#">
                                <i class="icons ico_title_list mr5"></i>我的应用
                            </a>
                        </h3>

                        <div class="title_line"></div>
                        
                        <div class="title_tab"><ul>
                        <!-- 
							<li>
                                <h5 class="f14">
                                    <a class=" " href="javascript:void(0)" >
                                        <i class="icons delete_ico_green mr5"></i>删除
                                    </a>
                                </h5>
                            </li>
                             -->
                            <li>
                                <h5 class="f14">
                                    <a class="active_green" href="${ WEB_APP_PATH }/project/edit.do?projectId=${project.projectId}">
                                        <i class="icons edit_ico_green mr5"></i>编辑
                                    </a>
                                    <c:if  test="${!(project.projectType eq 'app' || project.projectType eq 'api') }">
                                    <a class="active_green delete_button" href="javascript:void(0);">
                                        <i class="icons delete_ico_yellow mr5"></i>删除
                                    </a>
                                    </c:if>
                                </h5>
                            </li>
                        </ul></div>
                    </div>
                    <!--来源1-->
                    <div class="r_con p10_0">
                        <div class="details_nr">
                            <div class="form_control p20 form_details">
                                <div class="form_block">
                                    <label>项目来源</label>

                                    <p class="form_text">
                                    	<c:choose>
                                    		<c:when test="${project.projectType=='api'}">
                                    			APIManager
                                    		</c:when>
                                    		<c:when test="${project.projectType=='app'}">
                                    			APPFactory
                                    		</c:when>
                                    		<c:when test="${project.projectType=='store'}">
                                    			预置应用
                                    		</c:when>
                                    		<c:otherwise>
                                    			
                                    		</c:otherwise>
                                    	</c:choose>
                                    	&nbsp;
                                    </p>
                                </div>
                                <div class="form_block">
                                    <label>项目名称</label>

                                    <p class="form_text">
                                    	${project.projectName }&nbsp;
                                    </p>
                                </div>
                                <div class="form_block">
                                    <label>项目编码</label>

                                    <p class="form_text">
                                    	${project.projectCode }&nbsp;
                                    </p>
                                </div>
                                 <div class="form_block">
                                    <label>创建人</label>

                                    <p class="form_text">
                                    	${project.createUserName }&nbsp;
                                    </p>
                                </div>
                                <div class="form_block">
                                    <label>项目租户</label>
                                    <p class="form_text">
                                    	${project.tenantName }&nbsp;
                                    </p>
                                </div>
                                <div class="form_block">
                                    <label>项目描述</label>
                                    <p class="form_text">
                                    	${project.description }&nbsp;
                                    </p>
                                </div>
                                <c:if test='${project.projectType=="api" || project.projectType=="app" }'>
                                	<div style="display: none;" class="form_block">
	                                    <label>项目标识</label>
	
	                                    <p class="form_text">
	                                        <i class="badge">API</i> <i class="badge">Project01</i> <i class="badge">API</i>
	                                    </p>
	                                </div>
                                </c:if>
                                <c:if test='${project.projectType=="source" }'>
                                	<div class="form_block">
	                                    <label>源码地址</label>
	
	                                    <p class="form_text">http://github.com/abc</p>
	                                </div>
	                                <div class="form_block">
	                                    <label>关联项目</label>
	
	                                    <p class="form_text">project</p>
	                                </div>
	                                <div class="form_block">
	                                    <label>关联项目</label>
	
	                                    <p class="form_text">project</p>
	                                </div>
	                                <div class="form_block">
	                                    <label>用户名</label>
	
	                                    <p class="form_text">abc</p>
	                                </div>
	                                <div class="form_block">
	                                    <label>密码</label>
	
	                                    <p class="form_text">
	                                        **********
	                                    </p>
	                                </div>
                                </c:if>
                                
                            </div>
                        </div>
                    </div>
                </div>
                <!--项目概述结束-->
            </div>

        </div>
        <script>
        	$(function(){
        		function onDelApp(){
        			var _data = {};
        			_data.password=$.trim($(".pwd").val()); 
    				if(_data.password.length  <=0){
    					common.alert("请输入当前用户的密码",2);
    					return;
    				}
    				_data.projectId = '${project.projectId}';
    				_data.projectType = '${project.projectType}';
        			var option = {
							url : '${WEB_APP_PATH}/project/delete.do',
							type : 'POST',
							data : _data,
							cache : false,
							dataType : 'json'
					};
        			var load = common.loading('删除中......');
					var def = $.ajax(option);
					def['done'](function(json){
						if(json.code==0){
		    				common.tips("成功删除应用",1,function(){
			    				common.forward("${WEB_APP_PATH}/project/index.do");
		    				});
			    		}else{
			    			load.close();
			    			common.tips(json.msg);
			    		}
					});
        		}
        		$('.delete_button').bind('click',function(){
        			var _html = "<div style='padding:20px;'>请输入密码：<input class='pwd' type='password' onkeypress='if(event.keyCode==13) {onDelApp(\"${project.projectId}\");return false;}' name='j_password' autocomplete='off'/></div>";
    				var dialog = common.dialogHtml("删除项目需要验证您的密码",_html,400,180,['确定','取消'],{
    					btn1 :function(index,layero){
    						onDelApp('${project.projectId}');
    					},
    					btn2 :function(index,layero){
    						
    					}
    				})
				});
        		
					
					
        	});
        </script>
	</template:replace>
</template:include>