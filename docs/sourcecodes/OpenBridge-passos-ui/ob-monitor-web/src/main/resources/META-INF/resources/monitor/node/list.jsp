<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", -10);
%>
<template:include file="../base.jsp" nav-left="node">
	<template:replace name="title">
		节点管理
	</template:replace>
	<template:replace name="content-body">
		<div class="app_name">
            <a href="${WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="../tenant/list.do">环境管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../node/list.do">节点管理</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
                <div class="r_block p20">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line blue mr10">
                                <a href="#">
                                    <i class="icons cy_ico mr5"></i>节点管理
                                </a>
                            </h3>
                            <div class="title_line"></div>
                            <div class="title_tab">
	                        <ul>
	                            <li>
	                                <h5 class="f14">
	                                    <a class="active_green" href="javascript:void(0);" id="viewAll">
	                                        <i class="icons edit_ico_green mr5"></i>查看资源情况
	                                    </a>
	                                </h5>
	                            </li>
	                        </ul>
                        </div>
                        </div>
                        <div class="r_con p10_0">
                            <table class="table_ob" id="dataTable">
                                <thead>
                                <tr>
                                    <th><input type="checkbox" id="checkAll"/>&nbsp;&nbsp;&nbsp;主机名称</th>
                                    <th>IP</th>
                                    <th>CPU总量（个）</th>
                                    <th>内存总量（MB）</th>
                                    <th>环境</th>
                                    <th>类型</th>
                                </tr>
                                </thead>
                                <tbody>
								<c:forEach items="${nodes }" var="node">
								<c:if test="${not empty node.id }">
								<tr>
                                    <td>
                                        <input  endpoint="${node.ip }"   type="checkbox" class="_row">&nbsp;&nbsp;&nbsp;
                                    	${node.name }
                                    </td>
                                    <td>${node.ip }</td>
                                    <td>${node.cpu }</td>
                                    <td>${node.ram }</td>
                                    <td>${envs[node.nodeEvnType] }</td>
                                    <td data-type="${node.nodeType }">
                                    <c:choose>
                                    	<c:when test="${node.nodeType eq 'runtime'}">
                                    		项目部署
                                    	</c:when>
                                    	<c:when test="${node.nodeType eq 'mysql'}">
                                    		数据库
                                    	</c:when>
                                    	<c:when test="${node.nodeType eq 'redis'}">
                                    		高速缓存
                                    	</c:when>
                                    	<c:when test="${node.nodeType eq 'rabbitmq'}">
                                    		消息中间件
                                    	</c:when>
                                    	<c:otherwise>
                                    	${node.nodeType}
                                    	</c:otherwise>
                                    </c:choose>
                                    </td>
                                </tr>
                                </c:if>
								</c:forEach>
								<c:forEach items="${nodes }" var="node">
								<c:if test="${empty node.id }">
								<tr>
                                    <td>
                                    	<input endpoint="${node.ip }"   type="checkbox" class="_row">&nbsp;&nbsp;&nbsp;
						                 ${node.name }
                                    </td>
                                    <td>${node.ip }</td>
                                    <td>${node.cpu }</td>
                                    <td>${node.ram }</td>
                                    <td data-type="${node.nodeEvnType }">
                                    <c:choose>
                                    	<c:when test="${node.nodeEvnType eq 'live'}">
                                    		生产环境
                                    	</c:when>
                                    	<c:when test="${node.nodeEvnType eq 'test'}">
                                    		测试环境
                                    	</c:when>
                                    	<c:otherwise>
                                    	${node.nodeEvnType}
                                    	</c:otherwise>
                                    </c:choose></td>
                                    <td data-type="${node.nodeType }">
                                    <c:choose>
                                    	<c:when test="${node.nodeType eq 'runtime'}">
                                    		项目部署
                                    	</c:when>
                                    	<c:when test="${node.nodeType eq 'mysql'}">
                                    		数据库
                                    	</c:when>
                                    	<c:when test="${node.nodeType eq 'redis'}">
                                    		高速缓存
                                    	</c:when>
                                    	<c:when test="${node.nodeType eq 'rabbitmq'}">
                                    		消息中间件
                                    	</c:when>
                                    	<c:otherwise>
                                    	${node.nodeType}
                                    	</c:otherwise>
                                    </c:choose>
                                    </td>
                                </tr>
                                </c:if>
								</c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <!--创建应用部署结束-->
            </div>

            <script>

            function jsSearch(){
            	$("#dataTable tbody tr").each(function(){
            		var et = $(this).find("td:eq(6)").html();
            		var nt = $(this).find("td:eq(7)").attr("data-type");
            		var nn = $(this).find("td:eq(0) a").html();
            		if(envType && envType!=et){
            			$(this).hide();
            			return;
            		}
            		if(nodeType && nodeType!=nt){
            			$(this).hide();
            			return;
            		}
            		if(nodeName && nn.indexOf(nodeName)==-1){
            			$(this).hide();
            			return;
            		}
            		$(this).show();
            	})
            }
            $(function(){
            	$("ul.u-flavor li").live('click',function(){
            		$(this).addClass("selected");
            		$(this).siblings().removeClass("selected");
            	});
            	$("select.js-search").change(jsSearch);
            	$("a.js-search").click(jsSearch);
            	$("#nodeNameFilter").bind('keyup',function(e){
            		if(e.keyCode==13){
            			jsSearch();
            		}
            	});
            })
            
            $(function(){
            	jQuery.extend({
            		whenArray : function(subordinate /* , ..., subordinateN */){
            			var i = 0,
            			resolveValues = arguments[0].slice(0),
            			length = resolveValues.length,

            			// the count of uncompleted subordinates
            			remaining = length !== 1 || ( subordinate && jQuery.isFunction( subordinate.promise ) ) ? length : 0,

            			// the master Deferred. If resolveValues consist of only a single Deferred, just use that.
            			deferred = remaining === 1 ? subordinate : jQuery.Deferred(),

            			// Update function for both resolve and progress values
            			updateFunc = function( i, contexts, values ) {
            				return function( value ) {
            					contexts[ i ] = this;
            					values[ i ] = arguments.length > 1 ? slice.call( arguments ) : value;
            					if ( values === progressValues ) {
            						deferred.notifyWith( contexts, values );

            					} else if ( !(--remaining) ) {
            						deferred.resolveWith( contexts, values );
            					}
            				};
            			},

            			progressValues, progressContexts, resolveContexts;

            		// add listeners to Deferred subordinates; treat others as resolved
	            		if ( length > 1 ) {
	            			progressValues = new Array( length );
	            			progressContexts = new Array( length );
	            			resolveContexts = new Array( length );
	            			for ( ; i < length; i++ ) {
	            				if ( resolveValues[ i ] && jQuery.isFunction( resolveValues[ i ].promise ) ) {
	            					resolveValues[ i ].promise()
	            						.done( updateFunc( i, resolveContexts, resolveValues ) )
	            						.fail( deferred.reject )
	            						.progress( updateFunc( i, progressContexts, progressValues ) );
	            				} else {
	            					--remaining;
	            				}
	            			}
	            		}
	
	            		// if we're not waiting on anything, resolve the master
	            		if ( !remaining ) {
	            			deferred.resolveWith( resolveContexts, resolveValues );
	            		}
	
	            		return deferred.promise();
            		}
            	});
            	$("#dataTable tbody tr").click(function(e){
            		if(e.target.nodeName=='INPUT'){
            			return ;
            		}
            		var chb = $(this).find("td [type=checkbox]");
            		chb.attr("checked",chb.attr("checked")=="checked" ? false : true);
            	})
            	$("#checkAll").click(function(){
            		$("#dataTable tbody tr [type=checkbox]").attr("checked",$(this).attr("checked")=="checked" ? true : false);
            	});
        		$('#viewAll').bind('click',function(){
        			if($('#dataTable').find('._row:checked').size()==0){
        				common.tips('请选择节点');
        				return ;
        			}
        			var a = [];
        			$('#dataTable').find('._row:checked').each(function(domIndex,domEle){
        				var _endpoint =$(domEle).attr('endpoint');
        				var _info = {
        						endpoint : _endpoint,
        						key : _endpoint
        				};
        				a.push(_info);
        			});
        			common.forward('${ WEB_APP_PATH }/monitor/node/view?source=node&data='+JSON.stringify(a));
        		});
            });
            </script>
	</template:replace>
</template:include>