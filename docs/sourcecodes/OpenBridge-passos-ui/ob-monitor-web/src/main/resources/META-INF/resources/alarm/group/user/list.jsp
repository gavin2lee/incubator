<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
			body {
				overflow: auto;
			}
		</style>
		<div class="form_control"  id="userTable">			
			<div class="choose_table_list" >
			<table class="table_ob" >
	               <tr>
	                   <th style="width:130px;">用户名</th>
	                   <th style="width:150px;">登陆名</th>
	                   <th>邮箱</th>
	                   <th>电话</th>
	               </tr>
				<c:forEach items="${ page }" var="row" varStatus="index">
	               <tr>	                 
	                   <td><span class="userName">${ row.name }</span></td>
	                   <td>${ row.loginName }</td>
	                   <td>${ row.email }</td>
	                   <td>${ row.phone }</td>
	               </tr>
	            </c:forEach>	          
	        </table> 
	         <!-- 分页开始-->
	              <c:if test="${empty page || fn:length(page)==0 }">
                  <p style="line-height: 30px; text-align: center; font-size: 14px;">没有相关内容。</p>
				  </c:if>
				  <c:if test="${not empty page}">
					 <ui:pagination data="${page }" href="javascript:loadItems(!{pageNo},!{pageSize});" id="userPage"></ui:pagination>
				  </c:if>                           
                 <!-- 分页结束 -->   
		   </div>
		</div>
	</template:replace>
	  
	  <template:replace name="bottom">
	     <template:super />	  
	       <script type="text/javascript">
	           var pageNo = 1,pageSize=10;
			   function loadItems(pageNo,pageSize){
			      var url=window.location.href;
				  pageNo = pageNo;
				  pageSize = pageSize;
				  $("#userTable").load(url,{
					pageNo : pageNo,
					pageSize :pageSize
				}); 
			   }
	        </script>
	  </template:replace>
</template:include>