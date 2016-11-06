<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="monitor">
	<template:replace name="title">
		资源监控
	</template:replace>
	<template:replace name="content-body">
		<style>
			.pods-wd{
				margin: 5px;
			}
			.pods-wd td{
				padding: 5px;
			}
			.log-area label{
				display: inline-block;
				width: 130px;
				padding-right: 10px;
				text-align: right;
			}
			.log-area div{
				margin-top: 10px;
			}
			.log-area{
				padding: 10px;
			}
		</style>
	     <div class="app_name">
            <a href="${ WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="${ WEB_APP_PATH }/project/index.do?projectId=${project.projectId}">项目管理</a></p>
            <em>&gt;</em>

            <p class="app_a">
            	<a href="${ WEB_APP_PATH }/project/monitor/index.do?projectId=${project.projectId}">
            	${project.projectName }&nbsp;
            	</a>
            </p>
            <em>&gt;</em>

            <p class="app_a">监控</p>
        </div>
        <div class="plate">
            <div class="project_r">
                
                <!--项目概述开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="#">
                                <i class="icons add_ico mr5"></i>监控管理
                            </a>
                        </h3>
                        <div class="title_line"></div>
                        <div class="title_tab">
	                        <ul>
	                            <li>
	                                <h5 class="f14">
	                                    <a class="active_green" href="javascript:void(0);" id="viewAll">
	                                        <i class="icons edit_ico_green mr5"></i>查看
	                                    </a>
	                                </h5>
	                            </li>
	                        </ul>
                        </div>
                    </div>
                    <!--来源2-->
                    <div class="r_con p10_0">
                        <div class="details_nr">
                            <table class="table_ob table_blue"  id="podList">
                                <tbody>
	                                <tr>
	                                    <th>部署名称</th>
	                                    <th>所属主机</th>
	                                    <th>CPU</th>
	                                    <th>内存</th>
	                                    <th>状态</th>
	                                    <th>开启时间</th>
	                                </tr>
	                                <c:if test="${! empty projectDeploys }">
	                                	<c:forEach items="${projectDeploys}"  var="row">
	                                		<tr>
			                                    <td colspan="6">&nbsp;<em class="f16"> <a href="#">-</a> </em> <a href="javascript:void(0);">${row.deployName}-${row.envName }(${row.envType })</a></td>
			                                </tr>
			                                <c:if test="${! empty podList.items }">
			                                	<c:forEach items="${podList.items}"  var="row1"  varStatus="st">
			                                		<c:if test="${row1.metadata.labels['serviceId']==row.deployId }">
			                                			<tr class="sub_td">
						                                    <td>
						                                    
						                                    	<input endpoint="${row1.status.hostIP}"   key="${row1.metadata.name}"
						                                    		dockerid="${fn:replace(row1.status.containerStatuses[0].containerID,'docker://','')}"  type="checkbox" class="_row">
						                                    	 
						                                    	&nbsp;&nbsp;<a class="_info" href="javascript:void(0);">${row1.metadata.name}</a>
						                                    </td>
						                                    <td>${row1.status.hostIP}</td>
						                                    <td>
						                                    	<c:if test="${row1.spec.containers[0].resources.requests !=null }">
						                                    		${row1.spec.containers[0].resources.requests.cpu.amount}
						                                    	</c:if>
						                                    </td>
						                                    <td>
						                                    	<c:if test="${row1.spec.containers[0].resources.requests !=null }">
						                                    		${row1.spec.containers[0].resources.requests.memory.amount}
						                                    	</c:if>
						                                    </td>
						                                    <td>
						                                    	<span class="text-green2">${row1.status.phase }
						                                    		<c:if test="${! empty row1.status.containerStatuses[0].state.running }"> 
						                                    			/running
						                                    		</c:if>
						                                    		<c:if test="${! empty row1.status.containerStatuses[0].state.terminated }"> 
						                                    			/terminated
						                                    		</c:if>
						                                    		<c:if test="${! empty row1.status.containerStatuses[0].state.waiting }"> 
						                                    			/waiting
						                                    		</c:if>
						                                    	</span>
						                                    </td>
						                                    <td>
						                                    	<c:set var="s2" value="${fn:replace(row1.status.startTime, 'Z', ' +0000')}" />
						                                    	<fmt:parseDate value="${s2 }"  var="isoDate" pattern="yyyy-MM-dd'T'HH:mm:ss zzzz">
						                                    	</fmt:parseDate>
						                                    	<fmt:formatDate value="${isoDate }"  pattern="yyyy-MM-dd HH:mm:ss"/>
						                                    </td>
						                                </tr>
			                                		</c:if>
			                                	</c:forEach>
			                                </c:if>
	                                	</c:forEach>
	                                </c:if>
                                </tbody>
                             </table>
                        </div>
                    </div>
                    <!--来源2结束-->
                </div>
                <!--项目概述结束-->
            </div>

        </div>

        <script>
     // 对Date的扩展，将 Date 转化为指定格式的String 
		// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
		// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
		// 例子： 
		// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
		// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
			Date.prototype.Format = function(fmt){ //author: meizz 
				var o = { 
				    "M+" : this.getMonth()+1,                 //月份 
				    "d+" : this.getDate(),                    //日 
				    "h+" : this.getHours(),                   //小时 
				    "m+" : this.getMinutes(),                 //分 
				    "s+" : this.getSeconds(),                 //秒 
				    "q+" : Math.floor((this.getMonth()+3)/3), //季度 
				    "S"  : this.getMilliseconds()             //毫秒 
				  }; 
				  if(/(y+)/.test(fmt)) 
				    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
				  for(var k in o) 
				   	 if(new RegExp("("+ k +")").test(fmt)) 
				  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
				  return fmt; 
			};
        	$(function(){
        		$('#viewAll').bind('click',function(){
        			if($('#podList').find('._row:checked').size()==0){
        				common.tips('请选择容器');
        				return ;
        			}
        			var a = [];
        			$('#podList').find('._row:checked').each(function(domIndex,domEle){
        				var _endpoint =$(domEle).attr('endpoint');
        				var _dockerid =$(domEle).attr('dockerid');
        				var _key = $(domEle).attr('key');
        				var _info = {
        						endpoint : _endpoint,
        						dockerid : _dockerid,
        						key : _key
        				};
        				a.push(_info);
        			});
        			common.forward('${ WEB_APP_PATH }/project/monitor/view.do?projectId=${project.projectId}&data='+JSON.stringify(a));
        		});
        		$('._info').bind('click',function(){
        			$('#podList').find('._row:checked').removeAttr('checked');
        			$(this).parent().find('._row').attr('checked','true');
        			$('#viewAll').trigger('click');
        		});
        	});
        </script>
	</template:replace>
</template:include>