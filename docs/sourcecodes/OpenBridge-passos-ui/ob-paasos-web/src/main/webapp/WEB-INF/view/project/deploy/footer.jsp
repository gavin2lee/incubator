<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/html"  id="changeReplicasHtml">
	{$
	<div class="specCard" id="changeReplicas" style="width:100%;margin:0px;float:none;">
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
				{$
				{%m%}
			</p>
		</div>
		<div class="spec_down">
			<p><span>{%cpu%}核</span> CPU</p>
			<p><span>{%memory%}</span> 内存</p>
		</div>
		<div class="spec_btm">
			<p class="text-center"><input value="{%replicas%}" name="replicas"  type="text" class=" spinnerExample"/>
			</p>
		</div>
    </div>
	<div class="form_block mt10" style="text-align:center;">
				<a id="replicas-button" class="btn btn-default btn-yellow f16  mt10" href="javascript:void(0);">保存</a>
	</div>
	$}
</script>
<script id="template_volumn" type="text/html">
											<tr class="row-data">
                                                <td>
                                                	<select name="type" class="u-ipt row-td">
                                                		<option value="emptyDir">emptyDir</option>
                                                		<option value="hostPath">hostPath</option>
                                                		<option value="nfs">nfs</option>
                                                		<option value="iscsi">iscsi</option>
                                                		<option value="flocker">flocker</option>
                                                	</select>
													<input type="hidden"  class="u-ipt row-td" name="volumnId"/>
													<input type="hidden"  class="u-ipt row-td" name="allocateContent"/>
                                                </td>
                                                <td><input name="mount" type="text" class="u-ipt row-td"></td>
                                                <td><input name="capacity" type="text" class="u-ipt row-td"></td>
                                                <td class="text-center">
	                                                <a class="deleteRow btn btn-default btn-yellow btn-sm1" href="javascript:void(0);"><em class="f14">-</em></a>
                                                </td>
                                            </tr>					
		</script>
<script id="template_podEvent" type="text/html">
        	{$<div style="padding:10px;"><table class="table_ob details_nr" style="width:770px;table-layout:fixed;">
				<thead>
					<tr>
						<td style="width:170px;">FirstSeen</td>
						<td style="width:170px;">LastSeen</td>
						<td style="width:50px;">Count</td>
						<td style="width:100px;">From</td>
						<td style="width:150px;">SubobjectPath</td>
						<td style="width:100px;">Reason</td>
						<td style="width:170px;">Message</td>
					</tr>
				</thead>
				<tbody>$}
					for(var i=0;i<data.items.length;i++){
						{$
						<tr>
							<td>
								$}
									var d = new Date(data.items[i].firstTimestamp);
									d = 	d.format('yyyy-MM-dd hh:mm:ss');
								{$
								{%d%}
							</td>
							<td>
								$}
									var d1 = new Date(data.items[i].firstTimestamp);
									d1 = 	d1.format('yyyy-MM-dd hh:mm:ss');
								{$
								{%d1%}
							</td>
							<td>{%data.items[i].count%}</td>
							<td>
								$}
									if(data.items[i].source.component=='kubelet'){
										{$
											<div>kubelet {%data.items[i].source.host%}</div>
										$}
									}
									else{
										{$
											<div>{%data.items[i].source.component%}</div>
										$}
									}
							{$
							</td>
							<td><div style="word-break:break-all;white-space:normal; ">{%data.items[i].involvedObject.fieldPath%}</div></td>
							<td>{%data.items[i].reason%}</td>
							<td>{%data.items[i].message%}</td>
						</tr>$}
						
					}
				{$
				</tbody>
			</table></div>
			$}
        </script>
<script type="text/javascript">
	function modifyReplicas(layer,param,hasCurrentpage){
		var _replicas = $('#changeReplicas').find('input[name="replicas"]').val();
		var _projectId = '${project.projectId}';
		var _data = $.extend({},param,{
			projectId : _projectId,
			replicas : _replicas
		});
		var option={
				url : '${WEB_APP_PATH}/project/deploy/saveReplicas.do',
				type : 'post',
				data : _data,
				dataType : 'json',
				cache : false
		};
		var load = common.loading('更新中......');
		var def = $.ajax(option);
		def['done'](function(json){
			load.close();
			if(hasCurrentpage){
				layer.close();
				common.tips(json.code==0?'部署正在后台更新中......':json.msg,1,function(){
    				window.location.reload();
    			});
				return ;
			}
			if(json.code==0){
				common.tips("部署正在后台更新中......",1,function(){
    				layer.close();
    				common.forward("${ WEB_APP_PATH }/project/env/index.do?projectId=${project.projectId}");
    			});
    	}else{
				common.alert(json.msg);
			}
		});
	}
	
	$(function(){
		$('.addRow').die().live('click',function(event,data){
			var _area = $(this).attr('template');
			var $ptbody = $(this).parents('#'+_area).find('tbody:first');
			if($(this).attr('template-call')!=null){
				var call = $(this).attr('template-call');
				var param = $(this).attr('template-param');
				var resultParam = [];
				if(param !=null && param !=''){
					resultParam.push(param);
				}
				if(arguments.length>1){
					for(var i=1;i<arguments.length;i++){
						resultParam.push(arguments[i]);
					}
				}
				$ptbody.append(eval(call+'(resultParam)'));
			}
			else{
				$ptbody.append($('#template_'+_area).html());
			}
			if(data!=null){
				if(data.key!=null && data.key!=''){
					$('#'+_area).find('.row-data:last').attr('id',data.key);
					$('#'+_area).find('.row-data:last').attr('resourceid',data.resourceId);
				}
				$('#'+_area).find('.row-data:last').find(':input').each(function(domIndex,domEle){
					var _n = $(domEle).attr('name');
					if(_n==null || _n==''){
						return true;
					}
					$(domEle).val(data[_n]==null ? '' : data[_n]);
				});
			}
		});
		$('.deleteRow').die().live('click',function(){
			if($(this).hasClass('parentTr')){
				$(this).parent('td').parent('tr').remove();
			}
			else{
				var $tr = $(this).parents('.row-data');
				var $ptr = $tr.parent();
				$tr.remove();
			}
		});
		$('.log-button').die().live('click',function(){
			var _deployId = $(this).attr('deployid');
			var _podName = $(this).attr('attrId');
			var param = {
					projectId : "${project.projectId}",
					podName : _podName,
					deployId : _deployId
			};
			var option = {
						url : '${WEB_APP_PATH}/project/deploy/getPodLog.do',
						type : 'POST',
						data : param,
						cache : false,
						dataType : 'json'
				};
				var load = common.loading('获取容器日志信息......');
				var def = $.ajax(option);
				def['done'](function(json){
					load.close();
					if(json.code==0){
						var cd = common.dialog({
		        			title:'容器日志信息',
		        			type: 1,
		        			area: [800+'px', 500+'px'],
		       			    fix: false, //不固定
		       			    maxmin: false,
		       			    btn:[],
		       			    content: '<div style="padding:10px;"><pre style="word-break: break-word;">'+json.data+'</pre></div>'
		        		});
	    		}else{
	    			common.alert(json.msg);
	    		}
			});
		});
		$('.event-button').die().live('click',function(){
			var _deployId = $(this).attr('deployid');
			var _podName = $(this).attr('attrId');
			var namespace = $(this).attr('name-space');
			var param = {
					projectId : "${project.projectId}",
					podName : _podName,
					deployId : _deployId,
					namespace : namespace
			};
			var option = {
						url : '${WEB_APP_PATH}/project/deploy/getPodEvent.do',
						type : 'POST',
						data : param,
						cache : false,
						dataType : 'json'
				};
				var load = common.loading('获取pod事件日志信息......');
				var def = $.ajax(option);
				def['done'](function(json){
					load.close();
					if(json.code==0){
						var cd = common.dialog({
		        			title:'事件日志信息',
		        			type: 1,
		        			area: [$(window).width()*0.75+'px', $(window).height()*0.8+'px'],
		       			    fix: false, //不固定
		       			    maxmin: false,
		       			    btn:[],
		       			    content: (new Tmpl($('#template_podEvent').html())).render(json)
		        		});
	    		}else{
	    			common.alert(json.msg);
	    		}
				});
		});
		$('.modify-button').die().live('click',function(){
			var deployId = $(this).attr('attrId');
			common.forward("${ WEB_APP_PATH }/project/deploy/edit.do?projectId=${project.projectId}&deployId="+deployId);
		});
		$('.redeploy-button').die().live('click',function(event,changeVariable){
			var _target =event.target;
			var hasCurrentpage=$(this).hasClass('currentpage');
			var _deployId = $(this).attr('attrId');
			var _projectId = '${project.projectId}';
			var option={
					url : '${ WEB_APP_PATH }/project/deploy/redeploy.do',
					type : 'post',
					data : {
						deployId : _deployId,
						projectId : _projectId
					},
					dataType : 'json',
					cache : false
			};
			var _type = 'true';
			if(_type=='true'){
				option = $.extend(true,option,{
					data : {
						'variableChangeTip' : 'true'
					}
				});
			}
			if(changeVariable!=null){
				option = $.extend(true,option,{
					data : {
						'changeVariable' : changeVariable,
						'variableChangeTip' : 'false'
					}
				});
			}
			var load = common.loading('部署中......');
			var def = $.ajax(option);
			def['done'](function(json){
				load.close();
				if(json.code==-2){
					common.confirm("部署的环境变量已经发生改变，是否使新用的环境变量部署应用?",function(isOk){
						$(_target).trigger('click',[isOk]);
					});
					return ;
				}
				if(hasCurrentpage){
					common.tips(json.code==0?'应用部署正在后台重启中,会经历停止和启动过程,请等待......':json.msg,1,function(){
	    				window.location.reload();
	    			});
					return ;
				}
				if(json.code==0){
	    			common.tips("应用部署正在后台重启中,会经历停止和启动过程,请等待......",1,function(){
	    				common.forward("${ WEB_APP_PATH }/project/env/index.do?projectId=${project.projectId}");
	    			},5000);
	    		}else{
    				common.alert(json.msg);
    			}
			});
		});
		$('.deploy-button').die().live('click',function(event,changeVariable){
			var _target =event.target;
			var hasCurrentpage=$(this).hasClass('currentpage');
			var _deployId = $(this).attr('attrId');
			var _projectId = '${project.projectId}';
			var option={
					url : '${ WEB_APP_PATH }/project/deploy/deploy.do',
					type : 'post',
					data : {
						deployId : _deployId,
						projectId : _projectId
					},
					dataType : 'json',
					cache : false
			};
			var _type = 'true';
			if(_type=='true'){
				option = $.extend(true,option,{
					data : {
						'variableChangeTip' : 'true'
					}
				});
			}
			if(changeVariable!=null){
				option = $.extend(true,option,{
					data : {
						'changeVariable' : changeVariable,
						'variableChangeTip' : 'false'
					}
				});
			}
			var load = common.loading('部署中......');
			var def = $.ajax(option);
			def['done'](function(json){
				load.close();
				if(json.code==-2){
					common.confirm("部署的环境变量已经发生改变，是否使新用的环境变量部署应用?",function(isOk){
						$(_target).trigger('click',[isOk]);
					});
					return ;
				}
				if(hasCurrentpage){
					common.tips(json.code==0?'应用部署正在后台启动中......':json.msg,1,function(){
	    				window.location.reload();
	    			});
					return ;
				}
				if(json.code==0){
	    			common.tips("应用部署正在后台启动中......",1,function(){
	    				common.forward("${ WEB_APP_PATH }/project/env/index.do?projectId=${project.projectId}");
	    			});
	    		}else{
    				common.alert(json.msg);
    			}
			});
		});
		$('.delete-button').live('click',function(){
			var _this = this;
			common.confirm("确定要删除该部署吗？",function(res){
				if(res){
					var _deployId = $(_this).attr('attrId');
					var param = {
							projectId : "${project.projectId}",
							deployId : _deployId
					};
					var option = {
								url : '${WEB_APP_PATH}/project/deploy/delete.do',
								type : 'POST',
								data : param,
								cache : false,
								dataType : 'json'
						};
					var load = common.loading('删除中......');
					var def = $.ajax(option);
					def['done'](function(json){
						load.close();
						if(json.code==0){
			    			common.tips("应用部署正在后台删除中......",1,function(){
			    				common.forward("${ WEB_APP_PATH }/project/env/index.do?projectId=${project.projectId}");
			    			});
		    			}else{
		    			
		    				common.alert(json.msg);
		    			}
					});
				}
			});
		});
		$('.stop-button').die().live('click',function(){
			var hasCurrentpage=$(this).hasClass('currentpage');
			var _deployId = $(this).attr('attrId');
			var param = {
					projectId : "${project.projectId}",
					deployId : _deployId
			};
			var option = {
						url : '${WEB_APP_PATH}/project/deploy/stop.do',
						type : 'POST',
						data : param,
						cache : false,
						dataType : 'json'
				};
			var load = common.loading('停止中......');
			var def = $.ajax(option);
			def['done'](function(json){
				load.close();
				if(hasCurrentpage){
					common.tips(json.code==0?'应用部署正在后台停止中......':json.msg,1,function(){
	    				window.location.reload();
	    			});
					return ;
				}
				if(json.code==0){
	    			common.tips("应用部署正在后台停止中......",1,function(){
	    				common.forward("${ WEB_APP_PATH }/project/env/index.do?projectId=${project.projectId}");
	    			});
    			}else{
    				common.alert(json.msg);
    			}
			});
		});
		$('.replicas-button').die().live('click',function(){
			var hasCurrentpage=$(this).hasClass('currentpage');
			var _deployId = $(this).attr('attrId');
			var _projectId = '${project.projectId}';
			var option={
					url : '${ WEB_APP_PATH }/project/deploy/deployInfo.do',
					type : 'get',
					data : {
						deployId : _deployId,
						projectId : _projectId
					},
					dataType : 'json',
					cache : false
			};
			var load = common.loading('获取部署信息......');
			var def = $.ajax(option);
			def['done'](function(json){
				load.close();
				if(json.code==0){
	    			var cf = json.data.computeConfig;
	    			cf = eval('('+cf+')');
	    			var cd = common.dialog({
	        			title:'扩容信息',
	        			type: 1,
	        			area: [250+'px', 320 +'px'],
	       			    fix: false, //不固定
	       			    maxmin: false,
	       			    btn:[],
	       			    content: (new Tmpl($('#changeReplicasHtml').html())).render(cf)
	        		});
	    			$('#changeReplicas').find('.spinnerExample').spinner({});
	    			$('#changeReplicas').find('input[name="replicas"]').val(cf.replicas);
	    			$('#replicas-button').bind('click',function(){
	    				modifyReplicas(cd,{
	    					deployId : _deployId
	    				},true);
	    			});
	    		}else{
    				common.alert(json.msg);
    			}
			});
		});
	
		$('.addVolumn').bind('click',function(){
			var d = common.dialogIframe('存储','${ WEB_APP_PATH }/resource/nas/nfs/list.do?type=NFS&_dialog=true',900,400,function(param){
				d.close();
				$('#volumn').find('.addRow').trigger('click',[param]);
			},{},{});
		});
	});
	
	
</script>
