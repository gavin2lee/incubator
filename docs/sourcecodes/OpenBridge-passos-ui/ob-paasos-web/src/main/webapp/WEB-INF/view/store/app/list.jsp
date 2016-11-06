<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp" nav-header="store">
	<template:replace name="title">
		预置应用
	</template:replace>
	<template:replace name="content">
	<style>
	.config-table{
		width:100%;
	}
	#createProject{
		padding:10px;
	}
	</style>
        <div class="plate">
            <div class="project_r">
            <auth:validator value="functionValidator(functionId=paasos.store.manager)">
                <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="add_app.do" class="btn btn-default btn-oranger f16 mr20"><em>+</em> 添加预置应用</a>
                     <em class="mr10 f14">|</em>
                     <span class="f14">在该页面上可以查看企业已经开发好的应用程序，应用管理员可以上传预置应用。 </span>
                </div></auth:validator>
                <div class="r_block p20">
                    <div class="tab_title">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line blue">
                                <a href="javascript:void(0);">
                                    <i class="icons ico_title_list mr5"></i>预置应用
                                </a>
                            </h3>
                            <div class="title_tab title_tab_radio">
			                    <ul>
			                        <li>
			                            <h5 class="f14">
			                                <input ${ empty viewType || viewType=="table" ? "checked=true" : ""  } value="table" type="radio" name="viewType" class="mr10" id="viewTypeTable"> <label for="viewTypeTable">列表视图</label></h5>
			                        </li>
			                        <li>
			                            <h5 class="f14">
			                                <input ${ (! empty viewType) && viewType=="preview" ? "checked=true" : ""  } value="preview"  type="radio" name="viewType" class="mr10" id="viewTypePreview"> <label for="viewTypePreview">预览视图</label>
			                            </h5>
			                        </li>
			                    </ul>
			                </div>
                            <div class="title_line"></div>
                        </div>
                    </div>
                    <div class="ser_bar mt10">
						<span style="margin-left:15px">
							<lable>关键字：</lable>
							<input name="projectCode" id="keyword" style="width: 170px" type="text" placeholder="搜索.." onkeypress="changeKeyword(event)">
							<a class="btn btn-sm btn-yellow2" href="javascript:loadItems(1,9);">查询</a>
							<a class="btn btn-sm btn-yellow2" href="javascript:resetData();">重置</a>
						</span>
					</div>
                    <div class="r_con p10_0" id="imageTable">
                        
                    </div>
                </div>
                <!--项目概述开始-->
                <div class="r_block p20" style="padding-top:0px;"> 
                    <div class="r_con p10_0">
                        <div class="details_nr" id="imageTable">
                            
                        </div>
                    </div>
                </div>
                <!--项目概述结束-->
            </div>
            <div id="createProjectModel" style="display:none;">
				<div class="r_block p20" >
					<div class="r_title">
						<h3 class="f14 h3_color h3_btm_line">
							<i class="icons add_ico_blue mr5"></i>创建项目
						</h3>
				
						<div class="title_line"></div>
					</div>
					<div class="r_con p10_0">
						<div class="form_control p20">
				
							<div id="projectform">
								<div class="${projectType=='api' ? 'active':''} formdiv"
									id="form_api">
									<form action="" id="createProjectForm">
										<input type="hidden" name="projectType" value="store"/>
										<input type="hidden" name="businessId" id="businessId" value=""/>
										<div class="form_block">
											<label>项目名称</label>
											<div>
												<input type="text" name="projectName"  id="projectName"  maxlength="10" /> <font color="red">*</font>
											</div>
										</div>
										<div class="form_block">
											<label>项目编码</label>
											<div>
												<input type="text" name="projectCode"  id="projectCode" maxlength="15" /> <font color="red">*</font>
												项目编码是一个项目的代号，只能是数字和字母。
											</div>
										</div>
									</form>
								</div>
				
				
							</div>
							<div class="form_block mt10">
								<label>&nbsp;</label> <a href="javascript:void(0);"
									class="btn btn-default btn-yellow f16  mt10" id="createProjectBtn" onclick="createProject();"><i
									class="ico_check"></i>确定</a>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="projectTableModel" style="display:none;">
				<table class="table_creat config-table" >
					<tr>
						<th>名称</th>
						<th>创建人</th>
						<th>创建时间</th>
					</tr>
					<tbody id="projectBody">
			
					</tbody>
				</table>
			</div>
            <script type="text/javascript" src="${WEB_APP_PATH }/assets/js/image/build.js"></script>
            <script src="${WEB_APP_PATH }/assets/js/store/presetApp.js"></script>
            <script>
            var pageNo = 1,pageSize=9;
			function loadItems(pageNo,pageSize){
				pageNo = pageNo;
				pageSize = pageSize;
				var viewType = $("input[name='viewType']:checked").val();
				$("#imageTable").load("items.do",{
					pageNo : pageNo,
					pageSize : pageSize,
					viewType : viewType,
					keyword : $("#keyword").val()
				});
			}
			$(function(){
				$("input[name='viewType']").change(function(){
					loadItems(1,9);
				});
				loadItems(1,9);
			});
			
			function changeKeyword(evt){
 				evt = evt | event;
		    	if(event.keyCode==13) {
		    		loadItems(1,9);
		    	}
 			}
			
			function resetData(){
 				$("#keyword").val("");
 				window.location.reload();
 			}
			
            </script>
	</template:replace>
</template:include>