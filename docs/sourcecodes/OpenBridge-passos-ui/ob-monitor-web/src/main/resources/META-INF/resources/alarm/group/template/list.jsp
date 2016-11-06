<%@ page language="java" pageEncoding="UTF-8"%>
 <%@ include file="/common/header.jsp"%>  
<template:include file="/common/template/dialog.jsp">
	<template:replace name="title">
		项目概述
	</template:replace>
	<template:replace name="body" >
		<style>
        	.row-data td{
        		width: 150px;
        	}
        	.row-data input,.row-data select{
        		width: 130px;
        	}
        	        	
			.tem_con_1 {
			    background: #fff none repeat scroll 0 0;
			    margin-bottom: 0px;
			    padding: 5px;
			}
			.tem_con_2 {
			    background: #fff none repeat scroll 0 0;
			    margin-bottom: 5px;
			    padding: 7px;
			}
        </style>
                <%-- <input type="hidden" id="tpl_id"  value="${ page.id }"> --%>
                <div class="plate" id="templateTable">
                    <div class="project_r">
                        <div class="r_block p20" >
                            <c:forEach items="${ page }" var="template"  varStatus="index"> 
                            <div class="tem_con_1">
                                 <div class="xq_block"> 
                                    <h5>策略组名称:&nbsp;&nbsp;${template.tplName}</h5><!-- <h4></h4> -->
                                 </div>                      
                            </div>
                            <div class="tem_con_2">                             
                                <table class="table_ob table_story">
                                    <thead>
                                    <tr>
                                        <th style="width:15%">监控项名</th>
                                        <th style="width:15%">条件</th>
                                        <th style="width:20%">最大报警次数</th>
                                        <th style="width:15%">报警级别</th>
                                        <th style="width:35%">策略项描述</th>
                                     </tr>
                                    </thead>
                                    <tbody>
                                    <c:if test="${not empty template.strategies }">
                                        <c:forEach items="${ template.strategies }" var="app" varStatus="index">
                                        <tr>
                                            <td>${app.metric}</td>
                                            <td>${app.func}${app.op}${app.rightValue}</td>
                                            <td>${app.maxStep}</td>
                                            <td>${app.priority}</td>
                                            <td>${app.note} </td>
                                        </tr>
                                        </c:forEach>
                                    </c:if>
                                    </tbody>
                                </table>
					               
                                <c:if test="${empty template.strategies }">
                                     <p class="no-content f14">暂无策略项</p>
                                </c:if>
                            </div>                             
                            </c:forEach>         
                            <!-- 分页开始-->
						              <c:if test="${empty page || fn:length(page)==0 }">
                                         <p style="line-height: 30px; text-align: center; font-size: 14px;">没有相关内容。</p>
									  </c:if>
									  <c:if test="${not empty page}">
										 <ui:pagination data="${page }" href="javascript:loadItems(!{pageNo},!{pageSize});" id="templatePage"></ui:pagination>
									  </c:if>                           
					                 <!-- 分页结束 -->             
                        </div>
                    </div>
                </div>
	</template:replace>
	<template:replace name="bottom">
	     <template:super />	  
	       <script type="text/javascript">
			           var pageNo = 1,pageSize=2;
					   function loadItems(pageNo,pageSize){
					      var url=window.location.href;
						  pageNo = pageNo;
						  pageSize = pageSize;
						  $("#templateTable").load(url,{
							pageNo : pageNo,
							pageSize :pageSize
						}); 
					   }
	        </script>
	  </template:replace>
</template:include>