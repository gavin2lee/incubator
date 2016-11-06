	<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>

<div class="r_con p10_0">
	<div class="details_nr">
		<div class="mirro_list">
				<c:if test="${empty pageData }">
					<div style="text-align: center;"><p class="text-center p20">暂无配置内容</p></div>
				</c:if>
        <c:if test="${! empty pageData }">
        		<c:forEach items="${pageData }" var="row">
        			<div class="cluster_block_three active row-td pos-rel">
                        <div class="cluster-header">
                            <h3 class="cluster_name">
                            	<a id="${row.deploy_id}_title" deploy-id="${row.deploy_id}" deploy-status="${row.status}" href="${WEB_APP_PATH}/project/deploy/view.do?deployId=${row.deploy_id }&projectId=${row.project_id}&deployCode=${row.deploy_code }">
                            	${row.deploy_name }
                            	</a>
                            </h3>
					<div class="cluster-right pull-right" style="margin-top:2px;">
					<button class="btn btn-more"><i class="icons ico_more2"></i></button>
					<div class="list_sub_menu">
					<div class="blank_grid">&nbsp;</div>
					<ul class="list_menu">
					<div class="arrow-up arrow_pos"></div>
					   <li>
					      <c:if test="${row.status == 1 || row.status == 0 }">
						     <a attrid="${row.deploy_id }" href="javascript:void(0);" class="deploy-button f12"><span class="fa fa-play mr10" aria-hidden="true"></span>启动</a>
					      </c:if>
					   </li>
					   <li>
					      <c:if test="${row.status == 10}">
						     <a attrid="${row.deploy_id }" href="javascript:void(0);" class="redeploy-button f12"><span class="fa fa-play mr10" aria-hidden="true"></span>重启</a>
					      </c:if>
					   </li>
					   <li>
					      <c:if test="${row.status == 1 || row.status == 0 }">
						     <a attrid="${row.deploy_id }" href="javascript:void(0);" class="modify-button f12"><span class="fa fa-pencil mr10" aria-hidden="true"></span>修改</a>
					      </c:if>
					   </li>
					   <li>
					      <c:if test="${row.status == 10 || row.status == 11 }">
						     <a attrid="${row.deploy_id }" href="javascript:void(0);" class="stop-button f12"><span class="fa fa-stop mr10" aria-hidden="true"></span>停止</a>
					      </c:if>
					   </li>
					   <li>
					<c:if test="${row.status != 5 && row.status != 6 && row.status != 7}">
						<a attrid="${row.deploy_id }" href="javascript:void(0);" class="delete-button f12"><span class="fa fa-trash mr10" aria-hidden="true"></span>删除</a>
					</c:if></li>
					   <li><a data_key="${row.deploy_id }" href="javascript:void(0);" class="os_log_button f12"><span class="fa fa-book mr10" aria-hidden="true"></span>日志</a></li>
					<li>
					<c:if test="${row.status == 10}">
						<a data_key="${row.deploy_id }" href="javascript:void(0);" class="os_monitor_button f12"><span class="fa fa-bolt mr10" aria-hidden="true"></span>监控</a>
					</c:if>
					</li>
					</ul>
					</div>
					</div>
                        </div>
                        <div class="cluster-content cluster-content2">
                            <ul>
                                <li>
                                    <label>内部地址</label>
                                    <p>${row.service_ip }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${row.status == 10}">
                                   	 <a class="visit-button" attrid="${row.deploy_id }"  href="javascript:void(0);">如何访问？</a>
                                    </c:if>
                                    </p>
                                </li>
                                <li style="display:none;">
                                    <label>外地地址</label>
                                    <p>${row.public_ip }&nbsp;</p>
                                </li>
                                <li>
                                    <label>环境类型</label>
                                    <p>
                                    	<c:forEach items="${envTypes }" var="item">
                                    		<c:if test="${item.key==row.env_type}">${item.value}</c:if>                             
			                             </c:forEach>
                                   &nbsp;</p>
                                </li>
                                <li>
                                    <label>部署规格</label>
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
                                    
                                </li>
                                <li>
                                    <label>部署状态</label>
                                    <p>
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
                                    	<span id="${row.deploy_id }_status">
                                    	
                                    	</span>
                                    </p>
                                </li>
                                <li>
                                    <label>部署镜像</label>
                                    <p>${fn:substring(row.image_name,fn:indexOf(row.image_name,'/'),-1) }</p>
                                </li>
                            </ul>
                        </div>
                    </div>
        		</c:forEach>     
             </c:if>                   
        </div>
	</div>
	<c:if test="${! empty pageData }">
    	<ui:pagination data="${ pageData }" href="javascript:loadMyApp(!{pageNo},!{pageSize})" id="apps" moreThanOne="true"></ui:pagination>
    </c:if>
</div>
		<script type="text/javascript">
		//    隐藏按钮JS
		$(document).ready(function () {
		$(".cluster-right").hover(function () {
		$(this).addClass("open");
		}, function () {
		$(this).removeClass("open");
		});
		});
		</script>