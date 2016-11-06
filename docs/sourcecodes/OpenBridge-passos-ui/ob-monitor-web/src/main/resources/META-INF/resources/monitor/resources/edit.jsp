<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="resources">
	<template:replace name="title">
		节点组管理
	</template:replace>
	<template:replace name="content-body">
		<div class="app_name">
			<a href="#"><i class="icons go_back_ico"></i></a>

			<p class="app_a">智能监控</p>
			<em>&gt;</em>

			<p class="app_a">
				<a href="groups">资源监控</a>
			</p>
			<em>&gt;</em>
			<p class="app_a">${group.grpName}</p>
		</div>
		<div class="plate">
			<div class="project_r">
				<div class="tem_con p20">
					<div class="r_title">
						<h3 class="f14 h3_color h3_btm_line blue mr10">
							<a href="#"> <i class="icons add_ico mr5"></i>节点组
							</a>
						</h3>
						<div class="title_line"></div>
					</div>
					<div class="app_details">
						<div class="app_active_num">
							<p style="margin-bottom: 5px;">
								<span><b>&nbsp;&nbsp;&nbsp;&nbsp;节点组名称：</b> <input
									id="hostgrpNm" type="text" placeholder="输入节点组名称"
									value="${group.grpName}"> </span>
							</p>
						</div>
						<input type="hidden" id="id" value="${group.id }" />
					</div>
					<div class="r_con p10_0">
						<div class="g-row">
							<div class="tem_con">
								<div class="tem_con p10_20" style="height: auto;">
									<table id="host_table" class="table_ob">
										<thead>
											<tr class="sub_td_title sub_td_title2">
												<th><b>节点名称</b></th>
											</tr>
										</thead>
										<tbody id="userDefinedTbl">
											<c:forEach items="${ groupHosts }" var="row">
												<tr>
													<input type="hidden" name="hostid" id="hostid"
														value="${row.id}">
													<td name='hostNames'>${row.hostname}</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							<div class="tem_con">
								<div class="tem_con p10_20" style="height: auto;">
									<c:forEach items="${ templates }" var="template"
										varStatus="index">
										<div class="tem_con">
											<div class="xq_block">
												<h5>策略名称:&nbsp;&nbsp;${template.tplName}</h5>
											</div>
											<table class="table_ob" id="strategy_table">
												<thead>
													<tr>
														<th>监控项名</th>
														<th>条件</th>
														<th>最大报警次数</th>
														<th>报警级别</th>					
													</tr>
												</thead>
												<tbody id="DefinedTbl">
													<c:if test="${not empty template.strategies }">
														<c:forEach items="${ template.strategies }" var="app"
															varStatus="index">
															<tr>
																<td name="metricName">${app.metric}</td>
																<td><span id="op" name="op">${app.op} </span>
                                                                <input class="mr10" style="width: 50px;" type="text" name="rightValue"
																	id="right_value" value="${app.rightValue}"></input></td>
																<td><input class='mr10' style='width: 50px'
																	type='text' id='max_step' name='maxStep'
																	value="${app.maxStep}"></input></td>
																<td><select class="form-control" id="alarmLive" name="alarmLive">
																		<option value="0"
																			<c:if test='${app.priority == "0"}'> selected='selected' </c:if>>0</option>
																		<option value="1"
																			<c:if test='${app.priority == "1"}'> selected='selected' </c:if>>1</option>
																		<option value="2"
																			<c:if test='${app.priority == "2"}'> selected='selected' </c:if>>2</option>
																		<option value="3"
																			<c:if test='${app.priority == "3"}'> selected='selected' </c:if>>3</option>
																		<option value="4"
																			<c:if test='${app.priority == "4"}'> selected='selected' </c:if>>4</option>
																		<option value="5"
																			<c:if test='${app.priority == "5"}'> selected='selected' </c:if>>5</option>
																</select></td>
															</tr>
														</c:forEach>
													</c:if>
												</tbody>
											</table>
											<c:if test="${empty template.strategies }">
												<p class="no-content f14" id="tpl">暂无策略项</p>
											</c:if>
										</div>
									</c:forEach>
								</div>
							</div>
							<div class="tem_con">
							<input type="hidden" name="teamId" id="teamId" value="${team.id}" />
							<label class="mr10">&nbsp;&nbsp;&nbsp;&nbsp;报警接收人：</label>
							 
                                    <input type="hidden" name="memIds" id="memIds" value="<c:forEach items="${team.member }" var="mem"
										varStatus="status">${mem.userId }<c:if test="${!status.last }">;</c:if></c:forEach>"/>

                                    <div id="memNames" name="memNames" style="width: 324px; height: 70px; border: 1px solid #eaeaea; display: inline-block; background-color: white; padding: 3px 5px;">
									<c:forEach items="${team.member }" var="mem"
										varStatus="status">
										${mem.userName }
										<c:if test="${!status.last }">;</c:if>
									</c:forEach>
									</div><button onclick="selectUser()" type="button" style="padding: 3px 5px;width:50px; line-height: 15px;cursor: pointer;display: inline-block; vertical-align: top; height: 77px;" class="btn btn-default btn-yellow2">选择
                                    </button>
							</div>
						</div>

					</div>
					<div style="padding: 20px 0px">
						<a href="javascript:void(0)" class="btn btn-yellow f16 mt10"
							onclick="saveHostgroup()">保存节点组</a>
					</div>
				</div>
			</div>
		</div>
		<script>
		 function selectUser(){
	    	var params = {};
	    	params.query="all";
	    	params.userIds = $("#memIds").val();
	    	params.userNames = $("#memNames").html();
	    	dialogUser(true,'memIds','memNames',function(){
	    		var userIds = $("#memIds").val().split(";");
		    	var userNames = $("#memNames").html().split(";");
		    	var str = '<option>--请选择--</option>';
		    	$.each(userIds,function(i,v){
		    		str += "<option value='"+v+"'>"+userNames[i]+"</option>";
		    	});
	    		$("#admin").html(str);
	    	},params);
	    }
	    function dialogUser(multiple,valId,textId,cb,params){
        	var url = WEB_APP_PATH+"/sys/user/dialog.do?query="+params.query+"&multiple="+multiple;
        	var dialog = top.common.dialogIframe("请选择用户",url,650,600,null,['确定','取消'],{
        		btn1 :function(index,layero){
        			var iwin = layero.find("iframe").get(0).contentWindow;
                	if(iwin.dialogClose && typeof(iwin.dialogClose) === 'function'){
                		var obj = (iwin.dialogClose());
                		if(obj!=null&&obj.ids!=null){
            				var idObj = $("#"+valId);
            				var textObj = $("#"+textId);
            				if(multiple){
            					idObj.val(obj.ids.join(";")); 
            					if(textObj.is(":input")){
            						textObj.val(obj.texts.join(";"));
            					}else{
            						textObj.html(obj.texts.join(";"));
            					}
            				}else{
        	    				for(var i=0;i<obj.ids.length;i++){ 
        	    					if(i==obj.ids.length-1){
        	        					idObj.val(obj.ids[i]); 
        	        					if(textObj.is(":input")){
        	        						textObj.val(obj.texts[i]);
        	        					}else{
        	        						textObj.html(obj.texts[i]);
        	        					}
        	    					}
        	        			}
            				}
                		}
                	}
                	if(cb!=null)
                		cb();
        		},
        		btn2 :function(index,layero){
        			
        		}
        	}); 
        	dialog.getIFrame().params = params;
        } 
			$(function() {
				$("#insert_strategy_btn").click(function() {
									var td = "<td name='metric'><input class='mr10' style='width:100px' type='text' name='metricName' id='metricName'></input></td>";
									td += "<td><input class='mr10' style='width: 50px;' type='text' id='func' name='func' value='all(#3)'"
											+ "data-toggle='tooltip' data-placement='top' title='判断方法，如：all(#3)'></input>"
											+ "<select class='form-control' id='op' name='op'>"
											+ "<option value='=='>==</option>"
											+ "<option value='!='>!=</option>"
											+ "<option value='<'>&lt;</option>"
											+ "<option value='<='>&lt;=</option>"
											+ "<option value='>'>&gt;</option>"
											+ "<option value='>='>&gt;=</option></select>"
											+ "<input class='mr10' style='width: 50px;' type='text' id='rightValue' name='rightValue'"
						   + "value='1'  title='报警阈值'></input></td>";
									td += "<td><input class='mr10' style='width: 50px' type='text' id='maxStep' name='maxStep'></input></td>";
									td += "<td><select class='form-control' id='alarmLive' name='alarmLive'>"
									        + "<option value='0'>0</option>"
											+ "<option value='1'>1</option>"
											+ "<option value='2'>2</option>"
											+ "<option value='3'>3</option>"
											+ "<option value='4'>4</option>"
											+ "<option value='5'>5</option></select></td>";
									var td_delete = "<td><a class='delete_btn f18 btn btn-yellow2 btn-sm ser-adv' style='padding:3px 12px;' href='#'>-</a></td>";
									var tr = $("<tr>")
									var table = $("#DefinedTbl");
									tr.append(td).append(td_delete);
									table.append(tr);
									$('#tpl').hide();
								});
				$(document).on("click", ".delete_btn", function() {
					$(this).parent().parent().remove();
				});
			});
			function saveHostgroup() {
				var trs = $("#DefinedTbl").find(">tr");
				var groupName = $("#hostgrpNm").val();
				var id = $("#id").val();
				var defineData = [];
				for (var i = 0; i < trs.length; i++) {
					var tr = $(trs[i]);
					var nm = $.trim(tr.find("td[name='metricName']").html());
					//var func = $.trim(tr.find("input[name='func']").val());
					var op = $.trim(tr.find("span[name='op']").html());
					var rightValue = $.trim(tr.find("input[name='rightValue']").val());
					var maxStep = $.trim(tr.find("input[name='maxStep']").val());
					var alarmLive = $.trim(tr.find("select[name='alarmLive']").val());
					var data={};
					data.nm=nm;
					//data.func=func;
					data.op=op;
					data.rightValue=rightValue;
					data.maxStep=maxStep;
					data.alarmLive=alarmLive;
					defineData.push(data);
				}
             	var member = [];
        		$.each($("#memIds").val().split(";"),function(i,v){
        			member.push({
        				userId : v
        			});
        		});

				var load = common.loading();
				var url="${ WEB_APP_PATH }/monitor/resources/save";
				$.post(url,{"data":JSON.stringify(defineData),"users":JSON.stringify(member),"groupName":groupName,"id":id},function(json){
					load.close();
					if(json.code==0){
						common.tips("保存成功！",1,function(){
	       					location.href = "${ WEB_APP_PATH }/monitor/resources/index";
	       						},2000);
					}else{
						common.tips("保存失败",2);
					}
				});
            }
		</script>
	</template:replace>
</template:include>