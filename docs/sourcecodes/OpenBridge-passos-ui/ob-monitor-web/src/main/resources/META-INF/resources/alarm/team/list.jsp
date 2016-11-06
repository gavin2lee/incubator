<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="team">
	<template:replace name="title">
		用户组管理
	</template:replace>
	<template:replace name="content-body">
		<div class="app_name">
            <a href="${WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="../group/index">告警设置</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../team/list">用户组</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
               <!--  <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="teams/add" class="btn btn-oranger f16 mr20"> 添加用户组</a>
                      <em class="mr10 f14">|</em> 
                     <span class="f14">在该页面上可以管理用户组信息。列表、添加、修改、删除用户组信息。 </span>  
                </div> -->
                <!--项目概述开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="#">
                                <i class="icons ico_title_list mr5"></i>用户组管理
                            </a>
                        </h3>
                            <div class="title_tab">
	            <ul>
	                <li>
	                    <h5 class="f14">
	                        <a class="active_green" href="teams/add" >
	                            <i class="icons add_ico_yellow mr5"></i>添加用户组
	                        </a>
	                    </h5>
	                </li>
	            </ul>
	        </div>
                        <div class="title_line"></div>
                    </div>
                    <div class="r_con p10_0" id="teamTable">
                        
                    </div>
                </div>
                <!--项目概述结束-->
            </div>

        </div>
  		<script>
			function deleteTeam(id){
				common.confirm('确定要删除此用户组吗？',function(res){
					if(res){
						$.post("teams/delete",{
							id : id
						},function(json){
							if(json.code==0){
								common.tips("删除成功！",1,function(){
									loadItems(pageNo,pageSize);
								},1000);
							}else{
								common.alert("删除失败！"+json.msg,2);
							}
						});
					}
				});
			}
			function updateTeam(id){
				location.href = "teams/add?id="+id;
			}
			function detail(id){
				var dialog = common.dialogIframe("组内成员信息","${WEB_APP_PATH}/teams/detail?userId="+id,350,200,function(){
					dialog.close();
					common.forward('${WEB_APP_PATH}/teams');
				}); 
			}
			var pageNo = 1,pageSize=10;
			function loadItems(pageNo,pageSize){
				pageNo = pageNo;
				pageSize = pageSize;
				 $("#teamTable").load("teams/table",{
					pageNo : pageNo,
					pageSize :pageSize
				}); 
			}

			$(function(){
				loadItems(1,10);
			});
		</script>
	</template:replace>
</template:include>