<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="tenant">
	<template:replace name="title">
		组织管理
	</template:replace>
	<template:replace name="content-body">
		<div class="app_name">
            <a href="${WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="../tenant/list.do">环境管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../tenant/list.do">组织管理</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
                <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="add_tenant.do" class="btn btn-oranger f16 mr20"> 添加组织</a>
                      <em class="mr10 f14">|</em> 
                     <span class="f14">在该页面上可以管理组织信息。列表、添加、修改、配置、删除组织信息。 </span>  
                </div>
                <!--项目概述开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="#">
                                <i class="icons ico_title_list mr5"></i>组织管理
                            </a>
                        </h3>
                        <div class="title_line"></div>
                    </div>
                    <div class="r_con p10_0" id="tenantTable">
                        
                    </div>
                </div>
                <!--项目概述结束-->
            </div>

        </div>
  		<script>
			function deleteTenant(id){
				common.confirm('确定要删除此组织吗？',function(res){
					if(res){
						$.post("delete.do",{
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
			function updateTenant(id){
				location.href = "add_tenant.do?id="+id;
			}
			var pageNo = 1,pageSize=10;
			function loadItems(pageNo,pageSize){
				pageNo = pageNo;
				pageSize = pageSize;
				$("#tenantTable").load("tenantTable.do",{
					pageNo : pageNo,
					pageSize :pageSize
				},function(){
					checkNamespace();
				});
			}
			
			function checkNamespace(){
				$.get("getNamespaces.do",{},function(data){
					if(data && data instanceof Array){
						$("#tenantTable tr:gt(0)").each(function(){
							var tenantId = $(this).attr("id");
							var tenantName = $(this).find("td:first").text();
							var hasNamespace = false;
							$.each(data,function(i,v){
// 								debugger;
								if(v.metadata.name==tenantId && v.metadata.annotations && v.metadata.annotations.tenantname== tenantName){
									hasNamespace = true;
									return false;
								}
							});
							if(!hasNamespace){
								$(this).find("td:last").append("<a href='javascript:synchroNamespace(\""+tenantId+"\",\""+tenantName+"\");'>同步Namespace</a>");
							}
						})
					}
				})
			}
			
			function synchroNamespace(id,name){
				$.post("synchroNamespace.do",{
					name : name,
					id : id
				},function(json){
					if(json.code==0){
						common.tips("同步成功！",1,function(){
							loadItems(pageNo,pageSize);
						},1000);
					}else{
						common.alert("同步失败！"+json.msg,2);
					}
				});
			}
			$(function(){
				loadItems(1,10);
			});
		</script>
	</template:replace>
</template:include>