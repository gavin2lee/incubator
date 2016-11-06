<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../../manager.jsp" active="user">
	<template:replace name="title">
		PaaSOS--用户管理
	</template:replace>
<template:replace name="content-path">
             <em>&gt;</em>
            <p class="app_a"> <a href="${WEB_APP_PATH}/sys/user/page.do">用户管理</a> </p>
    </template:replace>
	<template:replace name="detail">
		<div class="p20"> 	
        <div class="r_title">
	        <h3 class="f14 h3_color h3_btm_line blue">
	            <a href="javascript:void(0)">
	                <i class="icons cy_ico  mr5"></i>用户列表
	            </a>
	        </h3>
	
	        <div class="title_tab">
	            <ul>
	                <li>
	                    <h5 class="f14">
	                        <a class="active_green" href="javascript:void(0)" onclick="addUser()">
	                            <i class="icons add_ico_yellow mr5"></i>添加用户
	                        </a>
	                    </h5>
	                </li>
	            </ul>
	        </div>
	        <div class="title_line"></div>
	    </div>
				<div class="r_con p10_0 ">
					关键字：<input type="text" id="keyWord" value="${keyWords}" placeholder="输入关键字查找..">
							<a class="btn btn-yellow2 btn-sm js-search"
								href="javascript:SearchInfo();">查询</a>
			 <table class="table_ob">
              <thead> <tr>
                   <th>用户名</th>
                   <th>登陆ID</th>
                   <th>邮箱</th>
                   <th>手机</th>
                   <th>创建时间</th>
                   <th>是否启用</th>
                   <th>操作</th>
               </tr></thead>
               <tbody>
			<c:forEach items="${ pageData }" var="row" varStatus="index">
               <tr>
                   <td>${ row.userName }</td>
                   <td>${ row.loginName }</td>
                   <td>${ row.email }</td>
                   <td>${ row.mobile }</td>
                   <td><fmt:formatDate value="${row.createTime}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
                   <td>${ row.activate ? "是" : "否" }
                     <input type="hidden" value="${ row.userId }"  class="ace">
                   	<input type="hidden" name="activate" value="${row.activate}"/>
							<c:choose>
								<c:when test="${row.activate}">
									<a  class="btn btn-xs btn-warning">
										停用
									</a>
								</c:when>
								<c:otherwise>
									<a  class="btn btn-xs btn-success">
										启用
									</a>
								</c:otherwise>
							</c:choose>	</td>
                   <td>
						<div class="hidden-sm hidden-xs">
												
							<a  href="javascript:void(0)"  onclick="editUser('${row.userId}')" >编辑</a>
							<%-- <a class="btn btn-xs btn-danger" attrId="${row.userId}">删除</a> --%>
							<a href="javascript:void(0)"  onclick="editPwd('${row.userId}')" >
								修改密码
							</a>
						</div></td>
               </tr>
			</c:forEach>
			</tbody>
           </table>
           <ui:pagination data="${ pageData }" href="/sys/user/page.do?keyWords=${keyWords }" id="user"></ui:pagination>
           </div>
				<script>
					var active_class = 'active';
					$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
						var th_checked = this.checked;//checkbox inside "TH" table header
						$(this).closest('table').find('tbody > tr').each(function(){
							var row = this;
							if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
							else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
						});
					}); 
					
					//common function					
					function ajaxQuery(url,data){
						var load = common.loading();
						$.post(url,data,function(json){
							if(json.code == 0){
								common.tips("操作成功",1,function(){
										var formalurl = location.href;
										common.goto(formalurl);
									});
							
							}else{
								loading.close();
				    			common.tips(json.msg);
							}
						},'json');
					}
					//单个用户删除
					$(function() {
						$(".btn-danger").bind('click',
							function() {
								var data = {};
								data.userId =$(this).attr('attrId');
								common.confirm("你确定删除该用户信息吗？",function(isOk){
									if(isOk){ 
										var url = "${ WEB_APP_PATH }/sys/user/delete.do";
										ajaxQuery(url,data);
									}
								});
						});
					});
					
					//查询用户
							function SearchInfo() {
								if(!$.trim($("#keyWord").val())){
									common.goto('${ WEB_APP_PATH }/sys/user/page.do');
									return false;
								}
								common.goto('${ WEB_APP_PATH }/sys/user/page.do?keyWords='+$.trim($("#keyWord").val()));
							}
				
					//新增用户
			
				function addUser(){ 
					var dialog = common.dialogIframe("添加用户","${WEB_APP_PATH}/sys/user/add.do",600,450,function(){
						dialog.close();
						common.forward('${WEB_APP_PATH}/sys/user/page.do');
					}); 
				}
			 function editUser(userId){ 
					var dialog = common.dialogIframe("修改用户","${WEB_APP_PATH}/sys/user/modify.do?userId="+userId,600,450,function(){
						dialog.close();
						common.forward('${WEB_APP_PATH}/sys/user/page.do');
					}); 
				}
			 function editPwd(userId){ 
					var dialog = common.dialogIframe("修改用户密码","${WEB_APP_PATH}/sys/user/modifypwd.do?userId="+userId,600,300,function(){
						dialog.close();
						common.forward('${WEB_APP_PATH}/sys/user/page.do');
					}); 
				}
					//改变用户状态
					$(function() {
						$(".btn-success,.btn-warning").bind('click',
							function() {
								var data = {};
								data.userId =$(this).closest("tr").find(".ace").val();
								data.currentStatus = $(this).closest("tr").find(":input[name='activate']").val();
								var url = "${ WEB_APP_PATH }/sys/user/updateUserStatus.do";
								ajaxQuery(url,data);
						});
					});
					</script>
				<Br><Br><Br><Br><Br><Br><Br><Br>
		</div>
	</template:replace>
</template:include>
