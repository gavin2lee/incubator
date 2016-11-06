<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="template">
	<template:replace name="title">
		应用状态
	</template:replace>
	<template:replace name="content-body">   
          <div class="app_name">
                      <a href="alarm.html"><i class="icons go_back_ico"></i></a>
                      <p class="app_a">告警设置</p>
                      <em>&gt;</em>
                      <p class="app_a"><a href="${ WEB_APP_PATH }/templates">策略</a></p>
                  </div>
                  <div class="plate">
                      <div class="project_r">
                          <div class="r_block p20">
                          <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="#">
                                <i class="icons ico_title_list mr5"></i>策略管理
                            </a>
                        </h3>
                            <div class="title_tab">
	            <ul>
	                <li>
	                    <h5 class="f14">
	                        <a class="active_green" href="${ WEB_APP_PATH }/templates/0" >
	                            <i class="icons add_ico_yellow mr5"></i>添加策略
	                        </a>
	                    </h5>
	                </li>
	            </ul>
	        </div>
                        <div class="title_line"></div>
                    </div>
                              <div class="r_con p10_0" id="templateTable">
									<!-- 表格 -->
									
                              </div>
                          </div>
                      </div>
                  </div>
          
	</template:replace>
	 <template:replace name="bottom">
	     <template:super />	 
	     <script type="text/javascript">
	          	                       	                          
               function detail(id){       
				var dialog = common.dialogIframe("组内成员信息","${ WEB_APP_PATH }/templates/"+id+"/users",200,300,function(){
					dialog.close();
					common.forward('${WEB_APP_PATH}/templates');
				}); 
			 }
				     
         $(function(){
             $("#template_add_btn").click(function() {
             	var param = {
                	tplName : $.trim($("#tpl_name").val()),
                	createUser : "${HMUser.userName}"
                };
                var option = {
                	url : "${ WEB_APP_PATH }/templates",
                	data : JSON.stringify(param),
                	type : 'POST',
                	cache : false,
                	contentType: "application/json"
                };
                var ref = $.ajax(option);
                ref['done'](function(data){
                	if(data.code==1){
                	common.alert("保存失败！"+(data.msg||''),2);
                }else{
                	common.tips("保存成功！",1,function(){
                		location.href = "${ WEB_APP_PATH }/templates";
                		},2000);
                	}
                });
             });

         });
             function adjustHeight() {
                 var h = $(window).height();
                 var h2 = $(".head").height();
                 $(".plate").css("height", h - 99);
                 $(".left_sub_menu").css("height", h - h2);
             }
             $(document).ready(function () {
                         adjustHeight();
                     }
             )
             $(window).resize(function () {
                 adjustHeight();
             });
             $(".ser_bar2").hide();
             $(".ser-adv").click(function(){
                 $(".ser_bar2").toggle(200);
             });

			 $(function(){
				 $("#templateTable").load("${ WEB_APP_PATH }/templates/list");
			 });
         </script>
	     </template:replace>
</template:include>
