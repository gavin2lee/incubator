<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="resources">
	<template:replace name="title">
		资源监控
	</template:replace>
	<template:replace name="content-body">
		<div class="app_name">
			<a href="#"><i class="icons go_back_ico"></i></a>

			<p class="app_a">智能监控</p>
			<em>&gt;</em>

			<p class="app_a">资源监控</p>
		</div>
		<div class="plate">
			<div class="project_r">
				<div class="r_block p20">
					<div class="r_title">
						<h3 class="f14 h3_color h3_btm_line blue">
							<a href="#"> <i class="icons ico_title_list mr5"></i>节点组管理
							</a>
						</h3>
						<div class="title_tab"></div>
						<div class="title_line"></div>
					</div>
					<div class="r_con p10_0">
						<div class="details_nr">
							<div class="mirro_list">
								<c:forEach items="${ groups }" var="app" varStatus="index">
									<div class="cluster_block_three text-center active">
										<div class="cluster-header">
											<h3 class="cluster_name">${ app.grpName }</h3>
											<div class="cluster-right pull-right"
												style="margin-top: 8px;">
												<a href="${ WEB_APP_PATH }/monitor/resources/edit?id=${ app.id }"
													class="btn btn-yellow btn-sm mr10 f12">维护</a>
											</div>
										</div>
										<div class="cluster-content cluster-content_3 pb20">
											<dl>
												<dt>
													<a href="#"
														onclick="detailTemplate(${ app.id },${ app.countTemplates })"
														class="txt-bright link-line">${ app.countTemplates }</a>
												</dt>
												<dd>策略</dd>
											</dl>
											<dl>
												<dt>
													<a href="javascript:viewHost(${ app.id })"
														class="txt-safe link-line">${ app.countHosts }</a>
												</dt>
												<dd>节点</dd>
											</dl>
											<dl>
												<dt>
													<a href="#"
														onclick="detailUser(${ app.id},${ app.countUsers } )"
														class="txt-safe link-line">${ app.countUsers }</a>
												</dt>
												<dd>人数</dd>
											</dl>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</div>
				</div>
				<!--集群管理结束-->
			</div>
		</div>
		<script type="text/javascript">
         function detailTemplate(id,count){
			    if(count<=0){
            common.alert("该组还没有策略！");
            return ;
          }
				var dialog = common.dialogIframe("策略详细信息","${WEB_APP_PATH}/groups/"+id+"/templates",650,600,function(){
					dialog.close();
					common.forward('${WEB_APP_PATH}/monitor/resources/index');
				}); 
			 }
         function viewHost(id){
        	 var dialog = common.dialogIframe("节点信息","${WEB_APP_PATH}/monitor/resources/node/list?id="+id,650,600,function(){
					dialog.close();
					common.forward('${WEB_APP_PATH}/monitor/resources/index');
				}); 
         }
         function detailUser(id,count){
             if(count<=0){
               common.alert("该组还没有用户！");
               return ;
             }
				var dialog = common.dialogIframe("用户详细信息","${WEB_APP_PATH}/groups/"+id+"/users",650,300,function(){
					dialog.close();
					common.forward('${WEB_APP_PATH}/monitor/resources/index');
				}); 
			 }
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
             })
         </script>
	</template:replace>
</template:include>