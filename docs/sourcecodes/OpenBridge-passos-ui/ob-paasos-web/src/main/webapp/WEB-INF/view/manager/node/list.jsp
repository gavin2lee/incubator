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
                <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="javascript:addNode();" class="btn btn-oranger f16 mr20"> 添加节点 </a>
                      <em class="mr10 f14">|</em>  
                    <span class="f14">节点是集群内的物理计算单元，可以是一个 VM 或 物理机器。 </span>  
                </div>
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
		                    <div class="app_active_num alert fade in" style="padding: 3px 10px;">
		                         <p>
		                         	<span>统计信息：</span>  <span><b>CPU总数：</b> ${countMaps.cpuCounts } 核</span>
		                         	<em>|</em>
		                         	<span><b>内存大小：</b><fmt:formatNumber type="number" value="${countMaps.menmoryCounts/1024}" pattern="0.00" maxFractionDigits="2"/> GB</span>
		                         	<em>|</em>
		                            <span><b>运行实例总数：</b> ${countMaps.instanceCount } 个</span>
		                            <em>|</em>
		                            <span><b>集群节点总数：</b> ${ fn:length(nodes) } 个</span>
		                         </p>
		                    </div>
                            <div class="ser_bar mt10 mb10">
								<select id="envTypeFilter" class="js-search">
									<option value="">--环境类型--</option>
									<c:forEach items="${envs }" var="env" varStatus="st">
                                    <option value="${env.value }">${env.value }</option>
                                    </c:forEach>
								</select>                            
								<select id="nodeTypeFilter" class="js-search">
									<option value="">--节点类型--</option>
									<c:forEach items="${node_types }" var="type" varStatus="st">
                                    <option value="${type.value.name }">
                                    <c:choose>
                                    	<c:when test="${type.value.name eq 'runtime'}">
                                    		项目部署
                                    	</c:when>
                                    	<c:when test="${type.value.name eq 'mysql'}">
                                    		数据库
                                    	</c:when>
                                    	<c:when test="${type.value.name eq 'redis'}">
                                    		高速缓存
                                    	</c:when>
                                    	<c:when test="${type.value.name eq 'rabbitmq'}">
                                    		消息中间件
                                    	</c:when>
                                    	<c:otherwise>
                                    	${type.value.name}
                                    	</c:otherwise>
                                    </c:choose>
                                   </option>
                                    </c:forEach>
								</select>                            
	                            <input type="text" id="nodeNameFilter" placeholder="输入主机名查找..">
	                            <a class="btn btn-yellow2 btn-sm js-search" href="javascript:void(0);">查询</a> 
	                            <%--<span>统计信息  CPU总数：${countMaps.cpuCounts },内存大小: <fmt:formatNumber type="number" value="${countMaps.menmoryCounts/1024}" pattern="0.00" maxFractionDigits="2"/> GB,运行实例总数: ${countMaps.instanceCount }</span>--%>
                            </div>
                            <table class="table_ob" id="dataTable">
                                <thead>
                                <tr>
                                    <th><input type="checkbox" id="checkAll"/>&nbsp;&nbsp;&nbsp;主机名称</th>
                                    <th>IP</th>
                                    <th>CPU总量（个）</th>
                                    <th>内存总量（MB）</th>
                                    <th>运行实例（个）</th>
                                    <th>状态</th>
                                    <th>环境</th>
<!--                                     <th>组织</th> -->
                                    <th>类型</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
								<c:forEach items="${nodes }" var="node">
								<c:if test="${not empty node.id }">
								<tr>
                                    <td>
                                        <input  endpoint="${node.ip }"   type="checkbox" class="_row">&nbsp;&nbsp;&nbsp;
                                    	<a href="javascript:viewNode('${node.name }');">${node.name }</a>
                                    </td>
                                    <td>${node.ip }</td>
                                    <td class="resourceProgress" max="${node.cpu}" endpoint="${node.ip}" counter="cpu.busy"></td>
                                    <td class="resourceProgress" max="${node.ram}" endpoint="${node.ip}" counter="mem.memused"></td>
                                    <td>${node.instanceCount }</td>
                                    <td><span class="${node.status=='离线' ? 'txt-prompt' : ''} ${node.status=='在线' ? 'txt-safe' : ''}">${node.status }</span></td>
                                </tr>
                                </c:if>
								</c:forEach>
<!-- 								<tr><td colspan="6"><b>未管理的节点</b></td></tr> -->
								<c:forEach items="${nodes }" var="node">
								<c:if test="${empty node.id }">
								<tr>
                                    <td>
                                    	<input endpoint="${node.ip }"   type="checkbox" class="_row">&nbsp;&nbsp;&nbsp;
						                 <a href="javascript:viewNode('${node.name }');">${node.name }</a>
                                    </td>
                                    <td>${node.ip }</td>
                                    <td class="resourceProgress" max="${node.cpu}" endpoint="${node.ip}" counter="cpu.busy"></td>
                                    <td class="resourceProgress" max="${node.ram}" endpoint="${node.ip}" counter="mem.memused"></td>
                                    <td><a href="detail.do?name=${node.name }&tab=instance">${node.instanceCount }</a></td>
                                    <td><span class="${node.status=='离线' ? 'txt-prompt' : ''} ${node.status=='在线' ? 'txt-safe' : ''}">${node.status }</span></td>
                                    <td>${envs[node.nodeEvnType] }</td>
<%--                                     <td>${node.tenant.tenantName }</td> --%>
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
                                    <td><a href="javascript:configDialog('${node.name }','${node.nodeEvnType }','${node.orgId }','${node.nodeType }');">配置</a></td>
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
            <div id="addContent" style="display:none;">
	            <div style="padding:10px;line-height:35px;">
		            <p>请把以下脚本拷贝到目标机器上执行</p>
		            <textarea style="width:98%;height:229px;" readonly>${installScript }</textarea>
	            </div>
            </div>
            <div id="labelContent" style="display:none;">
	            <div class="r_con p10_0">
	            	<input type="hidden" value="" id="name"/>
                    <div class="form_control p20">
                        <div class="form_block">
                            <label>环境类型</label>
                            <div class="specList">
                                <ul class="u-flavor env-ul">
                                    <c:forEach items="${envs }" var="env" varStatus="st">
                                    <li code="${env.key }">${env.value }</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <div class="form_block">
                            <label>节点类型</label>
                            <div class="specList">
                                <ul class="u-flavor type-ul">
                                    <c:forEach items="${node_types }" var="type" varStatus="st">
                                    <li code="${type.key }">
                                    <c:choose>
                                    	<c:when test="${type.value.name eq 'runtime'}">
                                    		项目部署
                                    	</c:when>
                                    	<c:when test="${type.value.name eq 'mysql'}">
                                    		数据库
                                    	</c:when>
                                    	<c:when test="${type.value.name eq 'redis'}">
                                    		高速缓存
                                    	</c:when>
                                    	<c:when test="${type.value.name eq 'rabbitmq'}">
                                    		消息中间件
                                    	</c:when>
                                    	<c:otherwise>
                                    	${type.value.name }
                                    	</c:otherwise>
                                    </c:choose></li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <div class="form_block">
                            <label>租户关联</label>
                            <select style="width: 200px;" id="orgId">
                                <option value="">--请选择--</option>
                                <c:forEach items="${tenants }" var="tenant">
                                 <option value="${tenant.tenantId }">${tenant.tenantName }</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            
            <script>
            function viewNode(nodeName){
            	location.href = "detail.do?name="+nodeName;
            }
            function addNode(){
            	var layerObj = common.dialogHtml('添加节点',$("#addContent").html(),600,400,['关闭']);
            }
            function configDialog(name,envtype,tenantId,nodeType){
            	$("#orgId option").removeAttr("selected");
            	if(tenantId){
	            	$("#orgId option[value='"+tenantId+"']").attr("selected",true);
            	}else{
            		$("#orgId option:first").attr("selected",true);
            	}
            	var id = "labelDialog";
            	var con = $("#labelContent").clone().attr("id",id);
            	var context = con;
            	if(envtype){
            		$("ul.env-ul li",context).each(function(){
            			if($(this).attr("code")==envtype){
            				$(this).addClass("selected");
            			}
            		});
            	}else{
            		$("ul.env-ul li",context).first().addClass("selected");
            	}
            	if(nodeType){
            		$("ul.type-ul li",context).each(function(){
            			if($(this).attr("code")==nodeType){
            				$(this).addClass("selected");
            			}
            		});
            	}else{
            		$("ul.type-ul li",context).first().addClass("selected");
            	}
            	$("#name",context).val(name);
            	var getParam = function(){
            		context = $("#labelDialog");
        			var params = {
                			name : $("#name",context).val(),
                			envType : $(".env-ul li.selected",context).attr("code"),
                			nodeType : $(".type-ul li.selected",context).attr("code"),
                			orgId : $("#orgId",context).val()
                	};
        			return params;
            	}
            	var str = "<div id='"+id+"'>"+con.html()+"</div>";
            	var layerObj = common.dialogHtml('节点配置',str,600,400,['确定','关闭'],{
            		btn1:function(){ 
            			var params = getParam();
                    	$.post("saveOrUpdate.do",params,function(data){
                    		if(data.code==1){
                				common.alert("保存失败！"+(data.msg||''),2);
                			}else{
                				common.tips("保存成功！",1,function(){
                					location.href = "list.do";
                				},1000);
                			}
                    	});
                    	return false;
         			},
         			btn2:function(){ 
         			}
            	});
            	
            }
            function jsSearch(){
            	var envType = $("#envTypeFilter").val();
            	var nodeType = $("#nodeTypeFilter").val();
            	var nodeName = $("#nodeNameFilter").val();
            	$("#dataTable tbody tr").each(function(){
// 	            	debugger;
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
        			common.forward('${ WEB_APP_PATH }/project/monitor/view.do?source=node&data='+JSON.stringify(a));
        				});
            });
            </script>
            <script type="text/javascript" src="${WEB_APP_PATH}/assets/js/host/resources.js"></script>
            <script>
            $(function(){
								ResourceProgress.init({
									'webapp' : '${WEB_APP_PATH}',
									'parentId' : 'dataTable'
								});
							});
            </script>   
	</template:replace>
</template:include>