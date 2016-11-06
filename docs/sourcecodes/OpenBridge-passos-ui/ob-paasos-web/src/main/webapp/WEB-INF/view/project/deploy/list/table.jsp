<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="r_con p10_0">
	<div class="details_nr">
		<table class="table_ob">
			<tbody>
				<tr>
                    <th>部署名称</th>
                    <th>内部地址</th>
                    <th>外地地址</th>
                    <th>环境类型</th>
                    <th>部署规格</th>
                    <th>部署状态</th>
                    <th>部署镜像</th>
                </tr>
                <c:if test="${! empty pageData }">
                	<c:forEach items="${pageData }" var="row">
                		<tr>
		                    <td>
		                    	<a href="${WEB_APP_PATH}/project/deploy/view.do?deployId=${row.deploy_id }&projectId=${row.project_id}">
		                    	${row.deploy_name }
		                    	</a>
		                    </td>
		                    <td>
		                    	${row.service_ip }
		                    </td>
		                    <td>
		                    	${row.public_ip }
		                    </td>
		                    <td>
		                    	${row.env_type }
		                    </td>
		                    <td>
		                    	<p id="${row.deploy_id }_cf">${row.compute_config }&nbsp;</p>
                                 	<script>
	                                 	(function(){
	                            			var cc = '${row.compute_config }';
	                            			cc = eval('('+cc+')');
	                            			var s=[];
	                            			var spec = {
	                            					'ms' : '微型',
	                            					's' : '小型',
	                            					'm' : '中型',
	                            					'l' : '大型'
	                            			};
	                            			s.push(spec[cc.spec]+":");
	                            			s.push('cpu个数:'+cc.cpu);
	                            			s.push('内存:'+cc.memory);
	                            			s.push('复本:'+cc.replicas);
	                            			var m = s.join(' ');
	                            			var _id = '${row.deploy_id }_cf';
	                            			$('#'+_id).html(m);
	                            		})();
                                 	</script>
		                    </td>
		                    <td>
		                    	<span class="zt_span">
		                    	<c:choose>
		                    		<c:when test="${row.status ==1  }">
		                    			<i class="pas_ico zt_ico waiting_ico mt5"></i>停止
		                    		</c:when>
		                    		<c:when test="${row.status ==5  }">
		                    			<i class="pas_ico zt_ico waiting_ico mt5"></i>启动中
		                    		</c:when>
		                    		<c:when test="${row.status ==6  }">
		                    			<i class="pas_ico zt_ico waiting_ico mt5"></i>变更中
		                    		</c:when>
		                    		<c:when test="${row.status ==7  }"><!-- 删除kubectl操作 -->
		                    			<i class="pas_ico zt_ico waiting_ico mt5"></i>停止中
		                    		</c:when>
		                    		<c:when test="${row.status ==10  }">
		                    			<i class="pas_ico zt_ico done_ico mt5"></i>运行中
		                    		</c:when>
		                    		<c:when test="${row.status ==11  }">
		                    			<i class="pas_ico zt_ico fail_ico mt5"></i>删除失败
		                    		</c:when>
		                    		<c:when test="${row.status ==0  }">
		                    			<i class="pas_ico zt_ico fail_ico mt5"></i>启动失败
		                    		</c:when>
		                    	</c:choose>
		                    	</span>
		                    </td>
		                    <td>
		                    	${fn:substring(row.image_name,fn:indexOf(row.image_name,'/'),-1) }
		                    </td>
		                 </tr>
                	</c:forEach>
                </c:if>
            </tbody>
        </table>
    </div>
    <c:if test="${! empty pageData }">
    	<ui:pagination data="${ pageData }" href="javascript:loadMyApp(!{pageNo},!{pageSize})" id="apps" moreThanOne="true"></ui:pagination>
    </c:if>
</div>