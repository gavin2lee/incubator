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
				<c:forEach items="${ user }" var="row" varStatus="index">
	               <tr>	                 
	                   <td style="width:200px;text-align: center"><span class="userName">${ row.userName }</span></td>	                  
	               </tr>
	            </c:forEach>	          
	        </table> 	           
		   </div>
		</div>
	</template:replace>	  	
</template:include>