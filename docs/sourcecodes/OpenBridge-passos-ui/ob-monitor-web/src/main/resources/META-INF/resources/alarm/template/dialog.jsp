<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
body {
	overflow: auto;
}
</style>
		<div class="tem_nr">
			<div class="tem_form mb10">
				<div class="form-inline">
					<label class="mr10">监控项类别：</label>
					<!-- 
                                            <input class="w150 mr10" type="text" id="metric" data-toggle="tooltip" data-placement="top" title="监控项名，如：df.bytes.free.percent">
                                        	 -->
					<select class="form-control" id="monitorItemType">
						<option value="cpu">cpu</option>
						<option value="df">df</option>
						<option value="disk">disk</option>
						<option value="kernel">kernel</option>
						<option value="load">load</option>
						<option value="mem">mem</option>
						<option value="net">net</option>
					</select> <label class="mr10">监控项名</label> <select class="form-control"
						id="monitorItemName">
						<option value="cpu.busy">cpu.busy</option>
						<option value="cpu.cnt">cpu.cnt</option>
						<option value="cpu.guest">cpu.guest</option>
						<option value="cpu.idle">cpu.idle</option>
						<option value="cpu.iowait">cpu.iowait</option>
						<option value="cpu.irq">cpu.irq</option>
						<option value="cpu.nice">cpu.nice</option>
						<option value="cpu.softirq">cpu.softirq</option>
						<option value="cpu.steal">cpu.steal</option>
						<option value="cpu.system">cpu.system</option>
						<option value="cpu.user">cpu.user</option>
					</select>
				</div>
				<div class="form-inline">
					<label class="mr10">条件：</label> <input class="mr10"
						style="width: 50px;" type="text" id="func" value="all(#3)"
						data-toggle="tooltip" data-placement="top" title="判断方法，如：all(#3)">
					<select class="form-control" id="op">
						<option value="==">==</option>
						<option value="!=">!=</option>
						<option value="<">&lt;</option>
						<option value="<=">&lt;=</option>
						<option value=">">&gt;</option>
						<option value=">=">&gt;=</option>
					</select> <input class="mr10" style="width: 50px;" type="text"
						id="right_value" value="1" data-toggle="tooltip"
						data-placement="top" title="报警阈值">
				</div>
				<div class="form-inline">
					<label class="mr10">最大报警次数：</label>
					<!-- 
                                            <input class="w150 mr10" type="text" id="max_step" value="3" data-toggle="tooltip" data-placement="top" title="持续故障情况下，连续报警次数">
                                       		 -->
					<select class="form-control" id="max_step">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">9</option>
						<option value="10">10</option>
					</select>
				</div>
				<div class="form-inline">
					<label class="mr10">报警级别：</label>
					<!--  
                                            <input class="w150 mr10" type="text" id="priority" value="0" data-toggle="tooltip" data-placement="top" title="故障等级，填写1~5的数字">
                                        	-->
					<select class="form-control" id="alarmLive">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select>
				</div>
			</div>
		</div>

	</template:replace>
	<template:replace name="bottom">
		<template:super />
		<script>
			//获得母窗体的参数
			var myData = frameElement.myData;
			//用于返回给母窗体的封装值变量
			var returnValue = {};
			//加载时初始化各参数
			var metricAvailableTags = [ "cpu.busy", "cpu.cnt", "cpu.guest",
					"cpu.idle", "cpu.iowait", "cpu.irq", "cpu.nice",
					"cpu.softirq", "cpu.steal", "cpu.system", "cpu.user",
					"df.bytes.free", "df.bytes.free.percent", "df.bytes.total",
					"df.bytes.used", "df.bytes.used.percent", "df.inodes.free",
					"df.inodes.free.percent", "df.inodes.total",
					"df.inodes.used", "df.inodes.used.percent",
					"disk.io.avgqu-sz", "disk.io.avgrq_sz", "disk.io.await",
					"disk.io.ios_in_progress", "disk.io.msec_read",
					"disk.io.msec_total", "disk.io.msec_weighted_total",
					"disk.io.msec_write", "disk.io.read_bytes",
					"disk.io.read_merged", "disk.io.read_requests",
					"disk.io.read_sectors", "disk.io.svctm", "disk.io.util",
					"disk.io.write_bytes", "disk.io.write_merged",
					"disk.io.write_requests", "disk.io.write_sectors",
					"kernel.maxfiles", "kernel.maxproc", "load.15min",
					"load.1min", "load.5min", "mem.memfree",
					"mem.memfree.percent", "mem.memtotal", "mem.memused",
					"mem.memused.percent", "mem.swapfree",
					"mem.swapfree.percent", "mem.swaptotal", "mem.swapused",
					"mem.swapused.percent", "net.if.in.bytes",
					"net.if.in.compressed", "net.if.in.dropped",
					"net.if.in.errors", "net.if.in.fifo.errs",
					"net.if.in.frame.errs", "net.if.in.multicast",
					"net.if.in.packets", "net.if.out.bytes",
					"net.if.out.carrier.errs", "net.if.out.collisions",
					"net.if.out.compressed", "net.if.out.dropped",
					"net.if.out.errors", "net.if.out.fifo.errs",
					"net.if.out.packets", "net.if.total.bytes",
					"net.if.total.dropped", "net.if.total.errors",
					"net.if.total.packets", "net.port.listen", "proc.num" ];
			$(function() {
				if(!$.isEmptyObject(myData)){
					//设置监控项类别
					var my = myData['monitorItemName'];
					var myMaxAlertTimes = myData['maxAlertTimes'];
					var alertLive = myData['alertLive'];
					var myType = myData['monitorItemName'].split(".")[0];
					var myItemId = myData['itemId'];
					//由于有固定的格式
					var myFirstCondition = myData['condition'].split(")")[0] + ")";
					var mySecondCondition, myThirdCondition;
					//(后面的
					var myTempReg = myData['condition'].split(")")[1];
					if (myTempReg[1] == '=') {
						//说明操作符有两个的
						mySecondCondition = myTempReg[0] + myTempReg[1];
						if (myTempReg.length == 3) {
							myThirdCondition = myTempReg[2];
						} else if (myTempReg.length == 4) {
							myThirdCondition = myTempReg[2] + myTempReg[3];
						}
					} else {
						//只有一个操作符
						mySecondCondition = myTempReg[0];
						if (myTempReg.length == 2) {
							myThirdCondition = myTempReg[1];
						} else if (myTempReg.length == 3) {
							myThirdCondition = myTempReg[1] + myTempReg[2];
						}
					}

					$("#monitorItemType").val(myType);
					//当type初始化后，就有名字了,就可以初始化第二项列表了
					var type = myType;
	        		var myHtml = "";
					for(var i = 0;i < metricAvailableTags.length;i++){
						if(metricAvailableTags[i].startWith(type)){
							myHtml = myHtml+ "<option value=\""+metricAvailableTags[i]+"\">"+metricAvailableTags[i]+"</option>";
						}
					}
					$("#monitorItemName").html(myHtml);

					
					$("#monitorItemName").val(myData['monitorItemName']);
					$("#func").val(myFirstCondition);
					$("#op").val(mySecondCondition);
					$("#right_value").val(myThirdCondition);
					$('#max_step').val(myMaxAlertTimes);
					$('#alarmLive').val(alertLive);

				}


				//添加点击事件。
				$("#monitorItemType").change(function(){
            		var type = $(this).children('option:selected').val();
            		var myHtml = "";
					for(var i = 0;i < metricAvailableTags.length;i++){
						if(metricAvailableTags[i].startWith(type)){
							myHtml = myHtml+ "<option value=\""+metricAvailableTags[i]+"\">"+metricAvailableTags[i]+"</option>";
						}
					}
					$("#monitorItemName").html(myHtml);
                });

                //提交事件
                $("#strategy_new_btn").click(function(){

                	var regx = /(^[1-9][0-9]$)|(^100&)|(^[1-9]$)$/ ;
                	if(!regx.test($.trim($("#right_value").val()))){
						alert("表达式右边的值只能为1-99的数字");
						return false;
                    }
                    var myMetric = $("#monitorItemName").children('option:selected').val();
                    var myMaxStep =  $("#max_step").children('option:selected').val();
                    var myPriority =  $("#alarmLive").children('option:selected').val();
                    
                    var param = {
                    	'metric': myMetric,
                        'maxStep': myMaxStep,
                        'priority': myPriority,
                        'op': $.trim($("#op").val()),
                        'rightValue': $.trim($("#right_value").val()),
                        'tplId': $.trim($("#tpl_id").val()),
                        'tags': $.trim($("#tags").val()),
                        'func': $.trim($("#func").val()),
                        'note': $.trim($("#note").val()),
                        'runBegin': $.trim($("#run_begin").val()),
                        'runEnd': $.trim($("#run_end").val()),
                        'id':myItemId
                    };

                    var option = {
                    	url : "${ WEB_APP_PATH }/strategies/modify",
                    	data : JSON.stringify(param),
                    	type : 'POST',
                    	cache : false,
                    	contentType: "application/json"
                    };
                    var ref = $.ajax(option);
                    ref['done'](function(data){
                    	if(data.code==1){
                    	common.alert("修改失败！"+(data.msg||''),2);
                    }else{
                    	common.tips("修改成功！",1,frameElement.callback,2000);
                    	}
                    });
                });
			});
			//关闭窗口时，获得各个空的值。
			function dialogClose(){
				 var myMetric = $("#monitorItemName").children('option:selected').val();
                 var myMaxStep =  $("#max_step").children('option:selected').val();
                 var myPriority =  $("#alarmLive").children('option:selected').val();
                 
                 var param = {
                 	 'metric': myMetric,
                     'maxStep': myMaxStep,
                     'priority': myPriority,
                     'op': $.trim($("#op").val()),
                     'rightValue': $.trim($("#right_value").val()),
                     'func': $.trim($("#func").val())
                 };
                 if(myData){
					param.id=myData['itemId'];
                 }
                 return param;
			}
		</script>
	</template:replace>
</template:include>