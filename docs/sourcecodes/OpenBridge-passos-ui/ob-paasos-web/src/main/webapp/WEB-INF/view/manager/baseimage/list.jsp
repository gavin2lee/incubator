<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="baseimage">
	<template:replace name="title">
		基础镜像
	</template:replace>
	<template:replace name="content-body">
		<div class="app_name">
            <a href="${WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="../tenant/list.do">环境管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../baseimage/list.do">基础镜像</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
                <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="add_image.do" class="btn btn-default btn-oranger f16 mr20">创建镜像</a>
                      <em class="mr10 f14">|</em>  
                      <span class="f14">基础镜像是应用程序运行的基础环节，开发人员开发的程序需要基于基础镜像去运行。</span> 
                </div>
                <!--项目概述开始-->
                <div class="r_block p20">
                    <div class="tab_title">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line blue">
                                <a href="#">
                                    <i class="icons ico_title_list mr5"></i>基础镜像
                                </a>
                            </h3>
                            <div class="title_tab title_tab_radio">
	                            <ul>
	                                <li>
	                                    <h5 class="f14">
	                                        <input ${ empty viewType || viewType=="preview" ? "checked=true" : ""  } value="preview"  type="radio" name="viewType" class="mr10" id="viewTypePreview"><label for="viewTypePreview"> 预览视图</label>
	                                    </h5>
	                                </li>
	                                <li>
	                                    <h5 class="f14">
	                                        <input ${ (! empty viewType) && viewType=="table" ? "checked=true" : ""  } value="table" type="radio" name="viewType" class="mr10" id="viewTypeTable"><label for="viewTypeTable"> 列表视图</label></h5>
	                                </li>
	                            </ul>
	                        </div>
                            <div class="title_line"></div>
                        </div>
                    </div>
                    <div class="r_con p10_0" id="imageTable">
                        
                    </div>
                </div>
                <!--项目概述结束-->
            </div>
         </div>
         
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script>
			
			var pageNo = 1,pageSize=9;
			function loadItems(pageNo,pageSize){
				pageNo = pageNo;
				pageSize = pageSize;
				var viewType = $("input[name='viewType']:checked").val();
				$("#imageTable").load("imageTable.do",{
					pageNo : pageNo,
					pageSize :pageSize,
					viewType : viewType
				});
			}
			$(function(){
				$("input[name='viewType']").change(function(){
					$(this).val() == "preview" ? loadItems(1,9) : loadItems(1,10);
				});
				$("input[name='viewType']:checked").val() == "preview" ? loadItems(1,9) : loadItems(1,10);
			});
		</script>
		
		<script>
		function deleteImage(id){
			common.confirm('确定要删除此镜像吗？',function(res){
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
		function buildImage(id){
			$.post("build.do",{
				id : id
			},function(json){
				if(json.code==0){
					loadItems(pageNo,pageSize);
					viewLog(id);
				}else{
					common.alert("构建失败！"+json.msg,2);
				}
			});
		}
		function updateImage(id){
			location.href = "add_image.do?id="+id;
		}
		</script>
	</template:replace>
</template:include>