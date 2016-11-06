<%@ page language="java" pageEncoding="UTF-8"%>
<style>
.row-data td {
	width: 150px;
}

.row-data input, .row-data select {
	width: 130px;
}

#deploy_area .table_creat th {
	width: 120px;
}

.hidden {
	display: none;
}

.title_content .active {
	display: block;
}

.row-data input, .row-data select {
	width: 90%;
}

.table_creat th {
	width: 140px;
}

.table_creat {
	width: 500px;
}

.table_creat .operator {
	width: 80px;
}

#envVariable .table_creat th {
	width: 210px;
}

#envVariable .table_creat .operator {
	width: 80px;
}
#healthcheck .view-td{
	display: none;
}
</style>
<c:set var="isAllModify"  value="false"></c:set>
<div class="title_tab title_tab_item title_tab_item_pas text-center">
	<ul>
		<li>
			<h5 class="f14">
				<a class="active" href="#">基础信息</a>
			</h5>
		</li>
		<li>
			<h5 class="f14">
				<a class="" href="#">配置信息</a>
			</h5>
		</li>

		<li>
			<h5 class="f14">
				<a id="instance" class="" href="#">实例信息</a>
			</h5>
		</li>
	</ul>
	<div class="title_line"></div>

</div>
<div class="title_content r_con p10_0 bg_white">
	<div class="hidden form_control p20 active ">
		<div class="form_block">
			<label>部署名称</label> <span> <c:choose>
					<c:when test="${isAllModify }">
						<input value="${paasProjectDeploy.deployName}" type="text"
							name="deployName">
					</c:when>
					<c:otherwise>
                                    			${paasProjectDeploy.deployName}
                                    	</c:otherwise>
				</c:choose>
			</span>
		</div>
		<div class="form_block">
			<label>部署状态</label> <span class="zt_span"> <c:choose>
					<c:when test="${paasProjectDeploy.status ==1  }">
						<i class="pas_ico zt_ico waiting_ico"></i>停止
				                    		</c:when>
					<c:when test="${paasProjectDeploy.status ==5  }">
						<i class="pas_ico zt_ico waiting_ico"></i>启动中
				                    		</c:when>
					<c:when test="${paasProjectDeploy.status ==6  }">
						<i class="pas_ico zt_ico waiting_ico"></i>变更中
				                    		</c:when>
					<c:when test="${paasProjectDeploy.status ==7  }">
						<!-- 删除kubectl操作 -->
						<i class="pas_ico zt_ico waiting_ico"></i>停止中
				                    		</c:when>
					<c:when test="${paasProjectDeploy.status ==10  }">
						<i class="pas_ico zt_ico done_ico"></i>运行中
				                    		</c:when>
					<c:when test="${paasProjectDeploy.status ==11  }">
						<i class="pas_ico zt_ico fail_ico"></i>删除失败
				                    		</c:when>
					<c:when test="${paasProjectDeploy.status ==0  }">
						<i class="pas_ico zt_ico fail_ico"></i>启动失败
				                    		</c:when>
				</c:choose>
			</span>
		</div>
		<div class="form_block">
			<label>部署镜像</label> <span>
				${fn:substring(poi.imageName,fn:indexOf(poi.imageName,'/'),-1) }
			</span>
		</div>
		<div class="form_block">
			<label>外部访问</label> <span> ${empty paasProjectDeploy.publicIp?"非公开":"公开"}
			</span>
		</div>
		<div class="form_block">
			<label>环境类型</label> <span> <c:forEach items="${envTypes }"
					var="item">
					<c:if test="${item.key==paasProjectDeploy.envType}">${item.value}</c:if>
				</c:forEach>
			</span>
		</div>
		<div class="form_block">
			<label>内部地址</label> <span>${paasProjectDeploy.serviceIp }</span>
		</div>
		<div class="form_block">
			<label>外部地址</label> <span>${paasProjectDeploy.publicIp }</span>
		</div>
		<div key="replicas" class="form_block targetField" id="computeConfig"
			targetField="computeConfig">
			<label>部署规格</label>
			<div class="specList">
				<ul id="specList_info">
					
				</ul>
			</div>
		</div>
		<div class="form_block">
			<label>描述</label> <span>${paasProjectDeploy.description}</span>
		</div>
	</div>
	<div class="hidden form_control p20">
		<div key="mount" class="form_block targetField datagrid" id="volumn"
			targetField="volumn">
			<label>数据卷</label>
			<div class="specList">
				<table class="table_creat">
					<tr>
						<th>类型</th>
						<th>挂载点</th>
						<th>容量</th>
						<%-- <c:if test="${paasProjectDeploy.status==10}">
							<th style="width: 50px;" class="operator text-center"><a
								template="volumn"
								class="addVolumn btn btn-default btn-yellow btn-sm1"
								href="javascript:void(0);"><em class="f14">+</em></a> <a
								style="display: none;" template="volumn"
								class="addRow btn btn-default btn-yellow btn-sm btn-xxs"
								href="javascript:void(0);"><em class="f14">+</em></a></th>
						</c:if> --%>
					</tr>
					<c:if test="${ ! empty pdv }">
						<c:forEach items="${pdv}" var="row">
							<%-- <c:choose>
								<c:when test="${paasProjectDeploy.status==10}">
									<tr class="row-data">
										<td><select name="type" class="u-ipt row-td">
												<option ${row.type=="emptyDir"?"selected='selected'":"" }
													value="emptyDir">emptyDir</option>
												<option ${row.type=="hostPath"?"selected='selected'":"" }
													value="hostPath">hostPath</option>
												<option ${row.type=="nfs"?"selected='selected'":"" }
													value="nfs">nfs</option>
												<option ${row.type=="iscsi"?"selected='selected'":"" }
													value="iscsi">iscsi</option>
												<option ${row.type=="flocker"?"selected='selected'":"" }
													value="flocker">flocker</option>
										</select> <input type="hidden" value="${row.volumnId }"
											class="u-ipt row-td" name="volumnId" /> <input type="hidden"
											value='${row.allocateContent }' class="u-ipt row-td"
											name="allocateContent" /></td>
										<td><input name="mount" type="text" class="u-ipt row-td"
											value="${row.mount }"></td>
										<td><input name="capacity" type="text"
											class="u-ipt row-td" value="${row.capacity }"></td>
										<td class="text-center"><a
											class="deleteRow btn btn-default btn-yellow btn-sm"
											href="javascript:void(0);"><em class="f14">-</em></a></td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td>${row.type }</td>
										<td>${row.mount }</td>
										<td>${row.capacity }</td>
									</tr>
								</c:otherwise>
							</c:choose> --%>
							<tr>
								<td>${row.type }</td>
								<td>${row.mount }</td>
								<td>${row.capacity }</td>
							</tr>
						</c:forEach>
					</c:if>
				</table>
			</div>
		</div>
		<div key="key" class="form_block targetField datagrid"
			id="envVariable" targetField="envVariable">
			<label>环境变量</label>
			<div class="specList">
				<table class="table_creat">
					<tr>
						<th>key</th>
						<th>value</th>
					</tr>
					<c:if test="${ ! empty pde }">
						<c:forEach items="${pde}" var="row">
							<tr>
								<td>${row.key }</td>
								<td>${row.value }</td>
							</tr>
						</c:forEach>
					</c:if>
				</table>
			</div>
		</div>
		<div key="targetPort" class="form_block targetField datagrid"
			id="ports" targetField="ports">
			<label>应用端口</label>
			<div class="specList">
				<table class="table_creat">
					<tr>
						<th>协议</th>
						<th>容器端口</th>
						<th>服务端口</th>
						<th>关键字</th>
					</tr>
					<c:if test="${ ! empty pbs }">
						<c:forEach items="${pbs}" var="row">
							<tr>
								<td>${row.portProtocol }</td>
								<td>${row.targetPort }</td>
								<td>${row.nodePort }</td>
								<td>${row.portKey }</td>
							</tr>
						</c:forEach>
					</c:if>
				</table>
			</div>
		</div>
		<c:if test="${! empty paasProjectDeploy.health }">
		<div  class="form_block"  id="healthcheck">
        <label>健康检查</label>
        <div class="specList">
            <table class="table_creat">
                <tr>
                			<th>检查类型</th>
                			<th>延迟启动(秒)</th>
                			<th>超时时间(秒)</th>
                    <th style="text-align: left;width:300px;"></th>
                </tr>
                <c:forEach items="${paasProjectDeploy.health }" var="row">
                <tr class="row-level">
                			<td><span style="width:100px;">${fn:toLowerCase(row.type)=='livenessprobe'?'检测失败就重启' :'检测失败就屏蔽'}</span></td>
                			<td><div style="width:80px;">${row.initialDelaySeconds }</div></td>
                			<td><div style="width:80px;">${row.timeoutSeconds }</div></td>
                    <td style="text-align: left;">
                    		<c:forEach items="${row.handlers }" var="row1">
	                    	<div style="width:300px;" class="row-level2">
	                    		<label style="width: 60px;padding-left: 3px;"><span>${fn:toLowerCase(row1.type)=='httpget'?'HTTP':'TCP'}</span></label>
	                    		<span>
	                    			<c:if test="${! empty row1.port }">
	                    								端口:<span>${row1.port }</span>
	                    			</c:if>
	                    			<c:if test="${! empty row1.path }">
	                    								路径:<span>${row1.path }</span>
	                    			</c:if>
	                    		</span>
	                    	</div>
	                    	</c:forEach>
                    </td>
                </tr>
                </c:forEach>
            </table>
        </div>
    </div>
    </c:if>
		<%-- <c:if test="${paasProjectDeploy.status==10}">
			<div style="margin-top: 40px; padding-left: 220px;">
				<a href="javascript:void(0);" id="addVolumn_button"
					class="btn btn-default btn-yellow f16  mt10"><i
					class="ico_check"></i>创 建</a>
			</div>
		</c:if> --%>
	</div>

	<div attrid="${paasProjectDeploy.deployId }"
		class="hidden form_control p20" id="podInformation">
		${paasProjectDeploy.status ==10 ?"数据正在获取中........":"部署不属于运行状态不能获取实例信息"}
	</div>
</div>
<link href="${WEB_APP_PATH}/assets/css/jquery.spinner.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${WEB_APP_PATH}/assets/js/jquery.spinner.js"></script>

<script type="text/html" id="view_spec_tmp">
{$
 		<li id="spec_{%spec%}" class="specCard checked">
						<div class="spec_up">
							<p>
								$}
									var m = '';
									if(spec=='ms'){
										m='微型';
									}
									else if(spec=='s'){
										m='小型';
									}
									else if(spec=='m'){
										m='中型';
									}
									else{
										m = spec;
									}
								{$
								{%m%}

								<input class="row-td" type="hidden" value="{%spec%}" name="spec">
							</p>
						</div>
						<div class="spec_down">
							<p>
								<span>{%cpu%}核<input class="row-td" type="hidden" value="1"
									name="{%cpu%}"></span> CPU
							</p>

							<p>
								<span>{%memory%}<input class="row-td" type="hidden"
									value="{%memory%}" name="memory"></span> 内存
							</p>
						</div>
						<div class="spec_btm">
							<p class="text-center">
								<input name="replicas" type="text" class="row-td spinnerExample" />
							</p>
						</div>
					</li>
$}
</script>

<script>
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
	        function arriveRender(data,element){
	    		var publicIp = data.data.publicIp;
	    		var serviceIp = data.data.serviceIp;
	    		var deployId = data.data.deployId;
	    		var webssh = data.data.webssh;
	    		var ports = data.data.ports;
	    		var p = 8080;
	    		if(ports!=null){
	    			if(ports.length==1){
	    				p = ports[0].nodePort;
	    			}
	    			else{
	    				for(var i=0 ; i<ports.length;i++){
	    					if((ports[i].portKey!=null && ports[i].portKey.toUpperCase()=='HTTP') || ports[i].portProtocol.toUpperCase()=='HTTP'){
	    						p = ports[i].nodePort;
	    						break ;
	    					}
	    				}
	    			}
	    		}
	    		var a = [];
	    		a.push('<div class="pods-wd">');
	    		a.push('<table class="table_ob details_nr">');
	    		a.push('<thead><tr>');
	    		a.push('<th >');
	    		a.push('实例名称 ');
	    		a.push('</th>');
	    		a.push('<th >');
	    		a.push('所在主机');
	    		a.push('</th>');
	    		a.push('<th >');
	    		a.push('部署镜像');
	    		a.push('</th>');
	    		a.push('<th >');
	    		a.push('启动时间');
	    		a.push('</th>');
				a.push('<th>');
				a.push('实例状态/容器状态');
	    		a.push('</th>');
				a.push('<th>');
				a.push('容器日志');
	    		a.push('</th>');
	    		a.push('<th>');
				a.push('事件');
	    		a.push('</th>');
	    		a.push('<th style="display:none;">');
				a.push('访问');
	    		a.push('</th>');
				a.push('<th>');
				a.push('控制台');
	    		a.push('</th>');
	    		a.push('</tr></thead>');
	    		a.push('<tbody>');
	    		for(var i=0;i<data.data.items.length;i++){
	    			var item = data.data.items[i];
	    			var podName = item.metadata.name;
	    		//	var containName = item.status.
	    			var hostIp = item.status.hostIP;
	    			var podIp = item.status.podIP;
	    			var containName = item.status.containerStatuses[0]!=null?item.status.containerStatuses[0].name:'';
	    			var containId = item.status.containerStatuses[0]!=null?item.status.containerStatuses[0].containerID:'';
	    			var image = item.status.containerStatuses[0]!=null?item.status.containerStatuses[0].image:'';
	    			var startTime = item.status.startTime;
	    			var phase = item.status.phase;
	    			var containStatus = item.status.containerStatuses[0]!=null?item.status.containerStatuses[0].state:null;
	    			var cphase = [];
	    			if(containStatus!=null){
	    				for(var k in containStatus){
		    				if(containStatus[k].reason!=null){
		    					cphase.push(k+'('+containStatus[k].reason+')');
		    				}
		    				else{
		    					cphase.push(k);
		    				}
		    			}
	    			}
	    			
	    			
	    			a.push('<tr>')
	    			a.push('<td>');
	        		a.push(podName);
	        		a.push('</td>');
	        		a.push('<td>');
	        		a.push(hostIp);
	        		a.push('</td>');
	        		a.push('<td>');
	        		a.push(image);
	        		a.push('</td>');
	        		a.push('<td>');
	        		if(startTime!=null){
	        			var d = new Date(startTime);
		        		a.push(d.Format("yyyy-MM-dd hh:mm:ss"));
	        		}
	        		
	        		a.push('</td>');
					a.push('<td>');
					a.push(phase+'/'+cphase.join(','));
	        		a.push('</td>');
					a.push('<td>');
					a.push('<a deployid="'+deployId+'" attrid="'+podName+'" class="log-button" href="javascript:void(0);">查看</a>');
	        		a.push('</td>');
	        		a.push('<td>');
					a.push('<a deployid="'+deployId+'" attrid="'+podName+'" class="event-button" href="javascript:void(0);">查看</a>');
	        		a.push('</td>');
	        		a.push('<td style="display:none;">');
					a.push('<a href="http://'+hostIp+':'+p+'" target="_blank">访问</a>');
	        		a.push('</td>');
					a.push('<td>');
					var s = webssh.replace("podName",podName);
					s = s.replace("containerId",containName);
					a.push('<a href="javascript:common.dialogIframe(\'SSH控制台\',\''+s+'\',$(window).width()*0.8,$(window).height()*0.8);void(0);" >进入</a>');
	        		a.push('</td>');
	        		a.push('</tr>');
	    		}
	    		a.push('</tbody>');
	    		a.push('</table>');
	    		a.push('</div>');
	    		$('#podInformation').html(a.join(''));
	    	}
        	
        	$(function(){
        		$('#addVolumn_button').bind('click',function(){
        			var r = [];
					$("#volumn").find('.row-data').each(function(domIndex,domEle){
						var j = {};
						$(domEle).find('.row-td').each(function(domIndex1,domEle1){
	 						var _name = $(domEle1).attr('name');
	 						var _v  = $(domEle1).val();
	 						if(_name==null || _v ==null || _v==''){
	 							return true;
	 						}
	 						j[_name] = _v;
	 					});
						r.push(j);
					});
					var _data = {};
					_data['deployId']='${paasProjectDeploy.deployId }';
					_data['volumn'] =  r ;
					var option = {
 							url : '${WEB_APP_PATH}/project/deploy/addVolumn.do',
 							type : 'POST',
 							data : JSON.stringify(_data),
 							cache : false,
 							dataType : 'json',
 							contentType : 'application/json',
 					};
					
					var load = common.loading('更新中......');
					var def = $.ajax(option);
 					def['done'](function(json){
 						load.close();
 						if(json.code==0){
 							common.tips(json.code==0?'部署需要重启生效......':json.msg,1,function(){
 			    				window.location.reload();
 			    			});
			    		}else{
			    			common.tips(json.msg);
			    		}
 					});
        		});
				$('#podInformation').bind('reload',function(){
        			var _deployId = $(this).attr('attrId');
        			var param = {
        					projectId : "${project.projectId}",
        					deployId : _deployId
        			};
        			var option = {
 							url : '${WEB_APP_PATH}/project/deploy/getPods.do',
 							type : 'POST',
 							data : param,
 							cache : false,
 							dataType : 'json'
 					};
        			var _self = _self;
 					var def = $.ajax(option);
 					def['done'](function(json){
 						if(json.code==0){
 							arriveRender(json,_self);
			    		}else{
			    			var a = [];
			    			a.push('<div>'+json.msg+'</div>');
			    			a.push('<div><a href="javascript:$(\'#podInformation\').trigger(\'reload\');void(0);" class="btn btn-default btn-oranger f16 mr20"> 重新获取 </a></div>')
			    			
			    			$('#podInformation').html(a.join(''));
			    			
			    		}
 					});
        		});
				<c:if test="${paasProjectDeploy.status ==10  }">
				$('#podInformation').trigger('reload');
				</c:if>
        		
        		$('.title_tab').children('ul').find('a').bind('click',function(){
        			var $p = $(this).parents('.title_tab');
        			$p.find('a').removeClass('active');
        			$(this).addClass('active');
        			var idex = $p.find('a').index(this);
        			var $c = $p.nextAll('.title_content');
        			$c.children().removeClass('active');
        			$c.children(':eq('+idex+')').addClass('active');
        		});
        		$('.specCard').bind('click',function(){
        			$('.specCard').removeClass('checked');
        			$(this).addClass('checked');
        		});
        		$('#computeConfig').find('button').bind('click',function(){
        			return false;
        		});


				var tab = '${tab}';
				if(tab!=null && tab!=''){
					$('#'+tab).trigger('click');
				}
        		
				//数据初始化

				
				var computeConfig = '${paasProjectDeploy.computeConfig}';
				computeConfig =eval('('+computeConfig+')');
				var h = (new Tmpl($('#view_spec_tmp').html())).render(computeConfig)
				$('#specList_info').html(h);
				$('.spinnerExample').spinner({});
				$('.spinner').find('button').attr('disabled','disabled');
        	});
        </script>
        <template:include file="footer.jsp"></template:include>
        
         