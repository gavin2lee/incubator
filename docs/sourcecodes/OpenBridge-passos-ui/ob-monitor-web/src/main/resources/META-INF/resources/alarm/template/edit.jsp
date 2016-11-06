<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="template">
	<template:replace name="title">
		项目概述
	</template:replace>
	<template:replace name="content-body">
		<style>
        	.row-data td{
        		width: 150px;
        	}
        	.row-data input,.row-data select{
        		width: 130px;
        	}
        </style>
        <div class="app_name">
                    <a href="alarm.html"><i class="icons go_back_ico"></i></a>

                    <p class="app_a">告警设置</p>
                    <em>&gt;</em>

                    <p class="app_a"><a href="${ WEB_APP_PATH }/templates">策略</a></p>
                </div>
                <input type="hidden" id="tpl_id" value="${ template.id }">
                <div class="plate">
                    <div class="project_r">
                        <div class="r_block p20">
                            <div class="tem_con">
                                <div class="xq_block">
                                    <h5>策略组</h5>
                                </div>
                                <div class="tem_nr">
                                    <div class="form-inline">
                                        <label class="mr10">名称：</label>
                                        <input class="w150 mr10" type="text" id="tpl_name" value="${template.tplName}">
                                        
                                    </div>
                                    <div class="form-block" style="padding-left: 500px">
                                            <label class="mr10">报警接收组：</label>
                                            <input  class="mr10" type="text" id="uic" value="${template.action.uic}" disabled="disabled"/>
                                            <input  class="mr10" type="hidden" id="uicId" value="${template.action.id}" disabled="disabled"/>
                                            <button onclick="selectReceiveTeam()" type="button" style="width:50px; line-height: 15px;cursor: pointer;display: inline-block; vertical-align: top; height: 32px;padding: 3px 5px;" class="btn btn-default btn-yellow2">选择
                                    </button>
                                    </div>
                                </div>
                            </div>
                            <div class="tem_con">
                            <div class="xq_block">
                                    <h5>策略项</h5>
                                </div>
                            

                                <table class="table_ob table_story">
                                    <thead>
                                    <tr>
                                        <th>监控项名</th>
                                        <th>条件</th>
                                        <th>最大报警次数</th>
                                        <th>报警级别</th>
                                        <th><li>
						                         <h5 class="f14">
						                             <a class="active_green" href="#" id="strategy_new_btn">
						                             <i class="icons add_ico_green mr5"></i>
						                                 新建策略
						                             </a>
						                         </h5>
						                     </li>
						                </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:if test="${not empty template.strategies }">
                                        <c:forEach items="${ template.strategies }" var="app" varStatus="index">
                                        <tr class="myEditClass">
                                            <td>${app.metric}</td>
                                            <td>${app.func}${app.op}${app.rightValue}</td>
                                            <td>${app.maxStep}</td>
                                            <td>${app.priority}</td>
                                            <td><a class="strategy_delete_btn mr10" href="#" name="${app.id}">删除</a>
                                            <a class="strategy_modify_btn mr10" href="#" name="${app.id}">修改</a>
                                            </td>
                                        </tr>
                                        </c:forEach>
                                    </c:if>
                                    </tbody>
                                </table>
                                <c:if test="${empty template.strategies }">
                                     <p class="no-content f14">暂无策略项</p>
                                </c:if>
                            </div>
                                   <div class="app_active_num f14 alert alert_min">
                                    <p>
                                        <span>ps:报警级别（&lt;3: 既发短信也发邮件 &gt;=3: 只发邮件）保存后生效</span>
                                    </p>
                                </div>
                            <div class="tem_con">
							<button id="tpl_saveAll_btn" class="btn btn-yellow btn-sm" href="#" onclick="mySubmit()" >保存</button>
							</div>
                        </div>
                    </div>
                </div> 
	</template:replace>
	<template:replace name="bottom">
		<template:super />
		<script>
        	
        
	    	//用于保存总的最终提交的表单参数。
	    	var myParams = {}; 	
        	//用于保存接收组的值
        	var mySelectTeam = 0;
        
        	//用于修改是后穿参数。
        	var myData = {};
        	
            var metricAvailableTags = ["cpu.busy","cpu.cnt","cpu.guest","cpu.idle","cpu.iowait","cpu.irq","cpu.nice",
                              	        "cpu.softirq","cpu.steal","cpu.system","cpu.user","df.bytes.free","df.bytes.free.percent",
                              	        "df.bytes.total","df.bytes.used","df.bytes.used.percent","df.inodes.free","df.inodes.free.percent",
                              	        "df.inodes.total","df.inodes.used","df.inodes.used.percent","disk.io.avgqu-sz","disk.io.avgrq_sz",
                              	        "disk.io.await","disk.io.ios_in_progress","disk.io.msec_read","disk.io.msec_total",
                              	        "disk.io.msec_weighted_total","disk.io.msec_write","disk.io.read_bytes","disk.io.read_merged",
                              	        "disk.io.read_requests","disk.io.read_sectors","disk.io.svctm","disk.io.util","disk.io.write_bytes",
                              	        "disk.io.write_merged","disk.io.write_requests","disk.io.write_sectors","kernel.maxfiles",
                              	        "kernel.maxproc","load.15min","load.1min","load.5min","mem.memfree","mem.memfree.percent",
                              	        "mem.memtotal","mem.memused","mem.memused.percent","mem.swapfree","mem.swapfree.percent",
                              	        "mem.swaptotal","mem.swapused","mem.swapused.percent","net.if.in.bytes","net.if.in.compressed",
                              	        "net.if.in.dropped","net.if.in.errors","net.if.in.fifo.errs","net.if.in.frame.errs",
                              	        "net.if.in.multicast","net.if.in.packets","net.if.out.bytes","net.if.out.carrier.errs",
                              	        "net.if.out.collisions","net.if.out.compressed","net.if.out.dropped","net.if.out.errors",
                              	        "net.if.out.fifo.errs","net.if.out.packets","net.if.total.bytes","net.if.total.dropped",
                              	        "net.if.total.errors","net.if.total.packets","net.port.listen","proc.num"
                                  		];
        	
        	$(function(){        		
                $(document).on("click", ".strategy_delete_btn", function(){
                	if (!confirm("确认要删除?")){
						return;
                 	}
                	//先暂时不提交，而仅仅是在页面删除
                	//将修改和增加的集合更改
                	var deleteId = $(this).attr("name");
                	$(this).parent().parent().remove();
                    
                });
                
                //修改操作
                $(document).on("click", ".strategy_modify_btn", function(){
                	//获得对象
                	myData.monitorItemName = $(this).parent().parent().children("td").eq(0).text();
                	myData.condition = $(this).parent().parent().children("td").eq(1).text();
                    myData.maxAlertTimes = $(this).parent().parent().children("td").eq(2).text();
                    myData.alertLive = $(this).parent().parent().children("td").eq(3).text();
                    
                    myData.itemId = $(this).attr("name");
                    var myThis = this;
                	
                	var url = WEB_APP_PATH+"/alarm/template/dialog.jsp";
                	
                	var dialog = common.dialogIframe("修改策略项",url,650,600,null,['确定','取消'],{
                		btn1:function(index,layero){
                			
                			//确定按钮，仅仅更改页面显示不提交数据。
                			var iwin = layero.find("iframe").get(0).contentWindow;
                			if(iwin.dialogClose && typeof(iwin.dialogClose) === 'function'){
                        		//获得子窗体dialogClose的返回值。
                				var obj = (iwin.dialogClose());
                        		//由于是修改，所以仅仅需要重设值就可以了
                        		if(obj == null){
                        			return;
                        		}
                        		//仅仅修改节点值
                        		
                        		$(myThis).parent().parent().children("td").eq(0).html(obj['metric']);
                        		$(myThis).parent().parent().children("td").eq(1).html(obj['func']+obj['op']+obj['rightValue']);
                        		$(myThis).parent().parent().children("td").eq(2).html(obj['maxStep']);
                        		$(myThis).parent().parent().children("td").eq(3).html(obj['priority']);                        		
                        	}
                		},
                		btn2:function(index,layero){
                			//取消按钮
                			
                		}
                	});
                	dialog.getIFrame().myData = myData;
                	
                });
                
                
                
                $("#strategy_new_btn").click(function(){
                	//把myData清空
                	myData = {};
                	var myThis = this;
                	var url = WEB_APP_PATH+"/alarm/template/dialog.jsp";
                	
                	var dialog = common.dialogIframe("修改策略项",url,650,600,null,['确定','取消'],{
                		btn1:function(index,layero){
                			
                			//确定按钮，仅仅更改页面显示不提交数据。
                			var iwin = layero.find("iframe").get(0).contentWindow;
                			if(iwin.dialogClose && typeof(iwin.dialogClose) === 'function'){
                        		//获得子窗体dialogClose的返回值。
                				var obj = (iwin.dialogClose());
                        		//由于是增加，所以需要新加一行
                        		if(obj == null){
                        			return;
                        		}
                        		//新增加一行但不提交
                        		//凭借html
                        		var myHtml = "<tr class=\"myEditClass\"><td>"+obj.metric+"</td><td>"+obj.func+obj.op+obj.rightValue+"</td><td>"+obj.maxStep+"</td><td>"+obj.priority+"</td><td><a class=\"strategy_delete_btn mr10\" href=\"#\" name=\"-1\">删除</a><a class=\"strategy_modify_btn mr10\" href=\"#\" name=\"-1\" >修改</a></td></tr>";
                        		
                        		//追加
                        		$(myThis).parent().parent().parent().parent().parent().parent().children("tbody").eq(0).append(myHtml);
                        	}
                		},
                		btn2:function(index,layero){
                			//取消按钮
                		}
                	});
                	dialog.getIFrame().myData = myData;
                }); 
        	});
  
                
                //用于选择用户组
		function selectReceiveTeam(){
	    	var params = {};
	    	params.query="all";
			params.teamId = $("#uic").val();
	    	dialogUser(false,'memIds','memNames',function(){
	    	},params);
	    }

        function dialogUser(multiple,valId,textId,cb,params){
        	var url = WEB_APP_PATH+"/teams/listTeam?query="+params.query+"&multiple="+multiple;
        	var dialog = top.common.dialogIframe("请选择用户",url,650,600,null,['确定','取消'],{
        		btn1 :function(index,layero){
        			var iwin = layero.find("iframe").get(0).contentWindow;
                	if(iwin.dialogClose && typeof(iwin.dialogClose) === 'function'){
                		var obj = (iwin.dialogClose());
                		
                		if(obj!=null){
                			mySelectTeam = obj;
							$("#uic").val(obj.name);
							$("#uicId").val(obj.id);
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
        
        //用于提交按钮事件。
        function mySubmit(){
        	if(!$("#tpl_name").val().trim()){
        		common.alert("策略组名不为空！");
        	}
        	var loading = common.loading();
        	var myArray = new Array();
        	//遍历table，把所有现有元素存起来。
        	
        	$(".myEditClass").each(function(i,element){
        		var t = $(this).children("td").eq(0).text();
        		var tempObj = {};
        		tempObj.metric=$(this).children("td").eq(0).text();
        		tempObj.maxStep=$(this).children("td").eq(2).text();
        		tempObj.priority=$(this).children("td").eq(3).text();
        		tempObj.id=$(this).children("td").eq(4).children("a").eq(0).attr("name");
      
        		var tempArrayData = $(this).children("td").eq(1).text();

        		//条件需要拆分
        		//由于有固定的格式
				var func = tempArrayData.split(")")[0] + ")";
				var op, right_value;
				//(后面的
				var myTempReg = tempArrayData.split(")")[1];
				if (myTempReg[1] == '=') {
					//说明操作符有两个的
					op = myTempReg[0] + myTempReg[1];
					if (myTempReg.length == 3) {
						right_value = myTempReg[2];
					} else if (myTempReg.length == 4) {
						right_value = myTempReg[2] + myTempReg[3];
					}
				} else {
					//只有一个操作符
					op = myTempReg[0];
					if (myTempReg.length == 2) {
						right_value = myTempReg[1];
					} else if (myTempReg.length == 3) {
						right_value = myTempReg[1] + myTempReg[2];
					}
				}
				tempObj.func=func;
				tempObj.op=op;
				tempObj.rightValue=right_value;
				tempObj.tplId=$("#tpl_id").val();
				//
				tempObj.tags=$.trim($("#tags").val());
				tempObj.note=$.trim($("#note").val());
				tempObj.runBegin=$.trim($("#run_begin").val());
				tempObj.runEnd=$.trim($("#run_end").val());
				myArray[i]=tempObj;
        	});
        	
        	//准备数据。
        	myParams.tplName = $("#tpl_name").val().trim();
        	myParams.uic = $("#uic").val();
        	myParams.strategys = myArray;
        	var dto = {"tplName":myParams.tplName,"uic":myParams.uic,"strategys":myArray,"tplId":$("#tpl_id").val(),"actionId":$("#uicId").val()};

        	//提交数据
            var option = {
            		url : "${ WEB_APP_PATH }/strategies/editStrategies",
                 	data : JSON.stringify(dto),
                 	type : 'POST',
                 	cache : false,
                 	contentType: "application/json"
                };
                var ref = $.ajax(option);
                ref['done'](function(data){
                	loading.close();
                	if(data.code==1){
                	common.alert("保存失败！"+(data.msg||''),2);
                	return;
                }else{
                	common.tips("保存成功！",1,function(){
                		location.href = "${ WEB_APP_PATH }/templates/"+data.data;
                		},2000);
                	}
                });
        }
        
        </script>
	</template:replace>
</template:include>