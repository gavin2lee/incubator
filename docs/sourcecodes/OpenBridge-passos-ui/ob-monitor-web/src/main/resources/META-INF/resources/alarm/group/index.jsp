<%@ page language="java"    contentType="text/html; charset=utf-8"  %>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="group">
	<template:replace name="title">
		节点组
	</template:replace>
	<template:replace name="content-body">
          <div class="app_name">
              <a href="alarm.html"><i class="icons go_back_ico"></i></a>

              <p class="app_a">告警设置</p>
              <em>&gt;</em>

              <p class="app_a">节点组</p>
          </div>
          <div class="plate">
              <div class="project_r">
                 <!--  <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                      <a href="groups/create" class="btn btn-oranger f16 mr20"><em>+</em> 添加节点组</a>
                      <em class="mr10 f14">|</em>
                      <span class="f14">在该页面上可以管理PAASOS监控的多个节点组信息，管理每个节点组的策略和节点。</span>
                  </div> -->
                  <!--集群管理开始-->
                  <div class="r_block p20">
                      <div class="r_title">
                          <h3 class="f14 h3_color h3_btm_line blue">
                              <a href="#">
                                  <i class="icons ico_title_list mr5"></i>节点组管理
                              </a>
                          </h3>
                          <div class="title_tab">
	            <ul>
	                <li>
	                    <h5 class="f14">
	                        <a class="active_green" href="groups/create" >
	                            <i class="icons add_ico_yellow mr5"></i>添加节点组
	                        </a>
	                    </h5>
	                </li>
	            </ul>
	        </div>
                          <div class="title_line"></div>
                      </div>
                      <div class="r_con p10_0">
                          <div class="details_nr" id="groupTable">
                              <div class="mirro_list">
                              <c:forEach items="${ page }" var="app" varStatus="index">
                              
                                   <div class="cluster_block_three text-center active pos-rel">
                                    <div class="cluster-header">
                                        <h3 class="cluster_name">${ app.grpName }</h3>
                                        <div class="cluster-right pull-right">
                                            <button class="btn btn-more"><i class="icons ico_more2"></i></button>
                                            <div class="list_sub_menu">
                                                <div class="blank_grid">&nbsp;</div>
                                                <ul class="list_menu">
                                                    <div class="arrow-up arrow_pos"></div>
                                                    <li><a href="groups/${ app.id }"><span class="fa fa-pencil mr10"
                                                                                           aria-hidden="true"></span>维护</a>
                                                    </li>
                                                    <li><a href="javascript:deleteGrp('${app.id }');"><span class="fa fa-trash mr10"
                                                                          aria-hidden="true"  id="group_delete_btn" name="${ app.id }"></span>删除</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                   
                                   
                                  <%-- <div class="cluster_block_three text-center active">
                                      <div class="cluster-header">
                                          <h3 class="cluster_name">${ app.grpName }</h3>
                                          <div class="cluster-right pull-right" style="margin-top: 8px;">
                                            <a href="groups/${ app.id }" class="btn btn-yellow btn-sm mr10 f12">维护</a>
                                            <a href="javascript:deleteGrp('${app.id }');" class="btn btn-yellow btn-sm f12" id="group_delete_btn" name="${ app.id }">删除</a>
                                          </div>
                                      </div>
                                       --%>
                                      
                                      <div class=" cluster-content  cluster-content_3 pb20">
                                          <dl>
                                              <dt><a href="#" onclick="detailTemplate(${ app.id },${ app.countTemplates })"  class="txt-bright link-line">${ app.countTemplates }</a></dt>
                                              <dd>策略</dd>
                                          </dl>
                                          <dl>
                                              <dt><a href="#"  onclick="viewHost(${ app.id},${ app.countHosts })" class="txt-safe link-line">${ app.countHosts }</a></dt>
                                              <dd>节点</dd>
                                          </dl>
                                          <dl>
                                              <dt><a href="#"    onclick="detailUser(${ app.id},${ app.countUsers } )" class="txt-green link-line">${ app.countUsers }</a></dt>
                                              <dd>用户数</dd>
                                          </dl>
                                      </div>
                                      <div class="cluster-footer"><span>创建人：${ app.createUser }</span></div>
                                  </div>
                              </c:forEach>
                              </div>
                              <!-- 分页开始-->
						     <c:if test="${empty page || fn:length(page)==0 }">
						       <p style="line-height: 30px; text-align: center; font-size: 14px;">没有相关内容。</p>
							 </c:if>
							<c:if test="${not empty page}">
								<ui:pagination data="${page }" href="javascript:loadItems(!{pageNo},!{pageSize});" id="groupPage"></ui:pagination>
						    </c:if>                           
                              <!-- 分页结束 -->                              
                          </div>
                      </div>
                  </div>
                  <!--集群管理结束-->
              </div>
         </div>               
	</template:replace>
	<template:replace name="bottom">
	      <template:super />	  
         <script type="text/javascript">
           function detailUser(id,count){
                if(count<=0){
                  common.alert("该组还没有用户！");
                  return ;
                }
				var dialog = common.dialogIframe("用户详细信息","${WEB_APP_PATH}/groups/"+id+"/users",500,500,function(){
					dialog.close();
					common.forward('${WEB_APP_PATH}/templates');
				}); 
			 }
			 
			   function detailTemplate(id,count){
			    if(count<=0){
                  common.alert("该组还没有策略！");
                  return ;
                }
				var dialog = common.dialogIframe("策略详细信息","${WEB_APP_PATH}/groups/"+id+"/templates",600,600,function(){
					dialog.close();
					common.forward('${WEB_APP_PATH}/templates');
				}); 
			 }
			   function viewHost(id,count){
				   if(count<=0){
		                  common.alert("该组还没有节点！");
		                  return ;
		                }
		        	 var dialog = common.dialogIframe("节点信息","${WEB_APP_PATH}/monitor/resources/node/list?id="+id,650,600,function(){
							dialog.close();
							common.forward('${WEB_APP_PATH}/templates');
						}); 
		         }
		         
		      var pageNo = 1,pageSize=6;
			 function loadItems(pageNo,pageSize){
		            var url="groups";
			      //alert(url);
				  pageNo = pageNo;
				  pageSize = pageSize;
				  url+="?pageNo="+pageNo+"&pageSize="+pageSize;
				  window.location.href=url;
			}
			
           function deleteGrp(id){
				common.confirm('确定要删除此节点组吗？',function(res){
					if(res){
						$.post("groups/delete",{
							id : id
						},function(json){
							if(json.code==0){
								common.tips("删除成功！",1,function(){
									 window.location.reload();
								},1000);
							}else{
								common.alert("删除失败！"+json.msg,2);
							}
						});
					}
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
             
               $(".cluster-right").hover(function () {
                  $(this).addClass("open");
               }, function () {
                    $(this).removeClass("open");
               });
         </script>
         </template:replace>
</template:include>