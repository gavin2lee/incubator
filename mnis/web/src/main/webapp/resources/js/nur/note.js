var list = new Array();// 存放orderGroupNo
var nurseId;
var nurseName;
var deptId;
var length = 0; //用于控制大屏幕刷新

$(function() {
	$("#send").click(function() {
		var message = $('#message').val();
		if(!message || /^\s+$/.test(message)){
			$("#msgNotice").html('请输入内容！');
			$("#msgNotice").removeClass('notice-success');
			$("#msgNotice").addClass('notice-error');
			$("#msgNotice").show();
			setTimeout(function(){
				$("#msgNotice").fadeOut("slow");
			},1500);
			return false;
		}
		if(message.indexOf('\n')>0){
			message = message.replace(/\n/g," ");
		}
		var url = ay.contextPath + '/nur/task/white/add.do';	
		var strDate = new Date().format("yyyy-MM-dd hh:mm:ss");
		var whiteBoardRecord = "{\"msgText\": \""+message+"\",\"nurseId\": \""+nurseId+"\",\"nurseName\": \""+nurseName+"\",\"time\": \""+strDate+"\",\"receiveMsgArea\":\"D\",\"receiveMsgId\":\""+deptId+"\"}";
		getMessage();
		$.post(url, {			
			whiteBoardRecord : whiteBoardRecord			
		}, function(result) {
			try {
				$("#msgNotice").html('发送成功！');
				$("#msgNotice").removeClass('notice-error');
				$("#msgNotice").addClass('notice-success');
				$("#msgNotice").show();
				setTimeout(function(){
					$("#msgNotice").fadeOut("slow",function(){
						$('#message').val('');
					});
				},1500);
			} catch (e) {
				$.messager.alert('提示', e);
			}
		});
		
	});

	$('#edit').click(function() {
		$('#edit-treatment-items').css("display", "block");
	});

	$('#telEdit').click(function() {
		$('#edit-useful-tel').dialog('open');
		getPhoneNum();
	});

	$('#pationCancelBtn').click(function() {
		$('#edit-treatment-items').css("display", "none");
	});

	$('#pationSaveBtn').click(function() {
		$('#edit-treatment-items').css("display", "none");
		var url = ay.contextPath + '/nur/nursingMemo/setBedCode.do?';
		for ( var i = 0; i < list.length; i++) {
			var str = "#" + list[i];
			$.post(url, {
				treatCode : list[i],
				bedCode : $(str).val()
			}, function(result) {
				try {

				} catch (e) {
					$.messager.alert('提示', e);
				}
			});
		}
		getTreatMentItem();
	});

	/*$('#inHospital').click(function() {
		var strDate = new Date().format("yyyy-MM-dd");
		getHospitalInfor(null,strDate);
	});*/
	$(window).click(function(){
		$('.top-tools-box .btn-text').each(function(){
			if($(this).hasClass('ofh')){

			}
			else{
				$(this).addClass('ofh');
			}
		});
	})
});

$(document).ready(
		function() {
			getCAresult();										
			var btnSur = $(".btn-sur"), btnLea = $('.btn-lea'),btnInHosp = $(".btn-inHosp");	
			$('.btn-text p').each(
				function() {
				$(this).click(function() {
					if ($(this).find('span.circle2').length) {
						return false;
					} else {
						$(".itemChecked").removeClass('itemChecked')
						//$(".itemIcon [class='itemChecked']").removeClass('itemChecked');
						$('.btn-text p').find('span.circle2').each(
							function() {$(this).removeClass('circle2');
						});
						$(this).find('span').addClass('circle2');
						if ('operateTime' == $(this).parent().prev().attr('id')) {
							$('#operateTime').empty();
							$('#operateTime').append("手术（"+ $(this).text()+ "）<i></i>");
							if('今日'==$(this).text()){
								var strDate = new Date().format("yyyy-MM-dd");
								getHospitalInfor('shoushu',strDate);
							}else{	
								var strDate = new Date();
								strDate.setDate(strDate.getDate()-1);
								getHospitalInfor('shoushu',strDate.format("yyyy-MM-dd"));
							}
							$(this).parent().next().addClass('itemChecked');
						} 
						else if('inHospital' == $(this).parent().prev().attr('id')){
							$('#inHospital').empty();
							$('#inHospital').append("入院（"+ $(this).text()+ "）<i></i>");
							if('今日'==$(this).text()){
								var strDate = new Date().format("yyyy-MM-dd");
								getHospitalInfor(null,strDate);
							}else{	
								var strDate = new Date();
								strDate.setDate(strDate.getDate()-1);
								//getHospitalInfor('shoushu',strDate.format("yyyy-MM-dd"));
								getHospitalInfor(null,strDate.format("yyyy-MM-dd"));
							}
							$(this).parent().next().addClass('itemChecked');
						}
						else {
							$('#outTime').empty();
							$('#outTime').append("出院（"+ $(this).text()+ "）<i></i>");
							if('今日'==$(this).text()){
								var strDate = new Date().format("yyyy-MM-dd");
								getHospitalInfor('cy',strDate);
							}else{		
								var strDate = new Date();
								strDate.setDate(strDate.getDate()-1);
								getHospitalInfor('cy',strDate.format("yyyy-MM-dd"));
//										$('#admitHospital').datagrid('loadData',{total : 0,rows : []});
							}
							$(this).parent().next().addClass('itemChecked');
						}
					}
				})
			});
			/*$(".btn-text").click(){
				if ($(this).hasClass('ofh')) {
					$(this).removeClass('ofh');
					if (!btnSur.hasClass('ofh') || ) {
						btnSur.addClass('ofh');
					}
				} else {
					$(this).addClass('ofh');
				}
			}*/
			btnInHosp.click(function() {
				if ($(this).hasClass('ofh')) {
					$(this).removeClass('ofh');
					if (!btnSur.hasClass('ofh') || !btnLea.hasClass('ofh') ) {
						btnSur.addClass('ofh');
						btnLea.addClass('ofh');
					}
				} else {
					$(this).addClass('ofh');
				}
				return false;
			});
			btnSur.click(function() {
				if ($(this).hasClass('ofh')) {
					$(this).removeClass('ofh');
					if (!btnInHosp.hasClass('ofh') || !btnLea.hasClass('ofh') ) {
						btnInHosp.addClass('ofh');
						btnLea.addClass('ofh');
					}
				} else {
					$(this).addClass('ofh');
				}
				return false;
			});
			btnLea.click(function() {
				if ($(this).hasClass('ofh')) {
					$(this).removeClass('ofh');
					if (!btnSur.hasClass('ofh') || !btnInHosp.hasClass('ofh') ) {
						btnSur.addClass('ofh');
						btnInHosp.addClass('ofh');
					}
				} else {
					$(this).addClass('ofh');
				}
				return false;
			});

});

function getMessage() {
	var url = ay.contextPath + '/nur/task/white/list.do?nurseId='+nurseId+'&deptId='+deptId;
	$.get(url, function(result) {
		try {
			$('#allMessage').empty();
			var i = 0;
			var hour;
			var min;
			var date = result['lst'];
			var flag = false;
			if(length != date.length){
				length = date.length;
				flag = true;
				$('#messageList').empty();
			}
			for ( var p in date) {
				var d = new Date(date[i]['time']);
				d.getHours() < 10 ? hour = "0" + d.getHours() : hour = d
						.getHours();
				d.getMinutes() < 10 ? min = "0" + d.getMinutes() : min = d
						.getMinutes();
				$('#allMessage').append(
						"<p><span class='timeandpeople'><em>" + hour + ":"
								+ min + "</em><strong>" + date[i]['nurseName']
								+ "：</strong></span>" + date[i]['msgText']
								+ "</p>");
				$('#allMessage').scrollTop($('#allMessage')[0].scrollHeight);
				if(flag){
					$('#messageList').append(
							"<li><p><span class='timeandpeople'><em>" + hour + ":"
									+ min + "</em><strong>" + date[i]['nurseName']
									+ "：</strong></span>" + date[i]['msgText']
									+ "</p></li>");
				}				
				i++;
			}
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});
	 setTimeout("getMessage()", 2000);
}

function getTreatMentItem() {
	
	var url = ay.contextPath
			+ '/exeOrderSummary/querySummary.do';
	$.get(url, function(result) {
		try {
			if(result.rslt=='0'){
				var html = "";
				$.each(result.lst,function(idx,category){
					html = html + "<p class='title'><span></span>"+category.categroyName+"</p>";
					$.each(category.list,function(idx,item){						
						var bedHtml = "";
						for(var i=0;i<item.bedCodeList.length;i++){
							if(bedHtml!=""){
								bedHtml += ",";
							} 
							bedHtml += item.bedCodeList[i]+"床";
						}
						//bedHtml+='</font>'
						html = html + "<p>"+item.itemName+(item.freqCode==null?"":item.freqCode)+":" +"<br><font style='color:#41be2c'>"+bedHtml +"</font><p>";
					});					
				});
				$('#treatmentItem').html(html);
	
//			}
//			$('#treatmentItem').empty();
//			$('#tableBody').empty();
//			var date = result.lst;
//			for ( var i = 0; i < date.length; i++) {
//				var str = "<dl><dt>" + date[i].treatProjectName + "</dt>";
//				var detailItems = date[i].detailItems;
//				for ( var j = 0; j < detailItems.length; j++) {
//					str = str + "<dd><i></i>" + detailItems[j].treatName + "：<span>";
//					var x = 0;
//					for (; x < detailItems[j].bedId.length - 1; x++) {
//						str = str + detailItems[j].bedId[x] + "、"
//					}
//					str = str + detailItems[j].bedId[x] + "</span></dd>";
//				}
//				str = str + "</dl>";
//				$('#treatmentItem').append(str);
//			}
//			list = new Array();
//			var z = 0;
//			for ( var i = 0; i < date.length; i++) {
//				var detailItems = date[i].detailItems;
//				var str = "<tr><td><span class='text'>"
//						+ date[i].treatProjectName
//						+ "</span></td><td></td></tr>";
//				str = str + "<tr>";
//				var x = 1;
//				for ( var j = 0; j < detailItems.length; j++, x++) {
//					if (((x - 1) != 0) && ((x - 1) % 2) == 0) {
//						str = str + "<tr>";
//					}
//					list[z++] = detailItems[j].treatCode;
//					str = str + "<td><span class='text'>"
//							+ detailItems[j].treatName + ":</span><input id='"
//							+ detailItems[j].treatCode
//							+ "' type='text' value='";
//					var h = 0;
//					for (; h < detailItems[j].bedId.length - 1; h++) {
//						str = str + detailItems[j].bedId[h] + "、";
//					}
//					str = str + detailItems[j].bedId[h] + "'></td>";
//					if (x % 2 == 0) {
//						str = str + "</tr>";
//					}
//				}
//				$('#tableBody').append(str);
			}
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});
	setTimeout("getTreatMentItem()", 600000);
}

function getPhoneNum() {
	var url = ay.contextPath + '/nur/duty/list.do?deptId='+deptId;
	$.get(url, function(result) {
		try {
			var date = result.lst;
			var str = "<p>";
			for ( var i = 0; i < date.length; i++) {
				if ('d_dc' == date[i].type) {
					$('#d_dc').val(date[i].userName);
					$('#d_dc_tel').val(date[i].tel);
				} else if ('d_nr' == date[i].type) {
					$('#d_nr').val(date[i].userName);
					$('#d_nr_tel').val(date[i].tel);
				} else if ('d_nL' == date[i].type) {
					$('#d_nL_tel').val(date[i].tel);
				} else if ('h_nd' == date[i].type) {
					$('#h_nd_tel').val(date[i].tel);
				} else if ('h_pt' == date[i].type) {
					$('#h_pt_tel').val(date[i].tel);
				} else if ('h_op' == date[i].type) {
					$('#h_op_tel').val(date[i].tel);
				} else if ('h_ad' == date[i].type) {
					$('#h_ad_tel').val(date[i].tel);
				}
			}
			$('#tel-info').empty();
			var str = "<p><span>值班医生：" + $('#d_dc').val() + "</span>"
					+ "<span>电话：" + $('#d_dc_tel').val() + "</span>"
					+ "<span>值班护士：" + $('#d_nr').val() + "</span>"
					+ "<span>电话：" + $('#d_nr_tel').val() + "</span>"
					+ "</p>"
					+ "<p><span>值班护士长电话：" + $('#d_nL_tel').val() + "</span><span>护理部电话：" + $('#h_nd_tel').val() + "</span>"
					+ "<span>保卫科电话：" + $('#h_pt_tel').val() + "</span>"
					+ "<span>手术室电话：" + $('#h_op_tel').val() + "</span>"
					+ "<span>行政值班电话：" + $('#h_ad_tel').val() + "</span></p>";
			$('#tel-info').append(str);
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});
}

function setPhoneNum() {
	var url = ay.contextPath + '/nur/duty/setDuty.do';
	$.post(url, {
		zbys : $('#d_dc').val(),
		zbysdh : $('#d_dc_tel').val(),
		zbhs : $('#d_nr').val(),
		zbhsdh : $('#d_nr_tel').val(),
		zbhszdh : $('#d_nL_tel').val(),
		hlbdh : $('#h_nd_tel').val(),
		bwkdh : $('#h_pt_tel').val(),
		sssdh : $('#h_op_tel').val(),
		xzzbdh : $('#h_ad_tel').val()
	}, function(result) {
		try {
			getPhoneNum();
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});
}

function getHospitalInforToBig(type,strDate,id){
	if(type == null){		
		var url = ay.contextPath+ '/nur/task/queryOrderByEvent.do?deptId='+deptId+'&queryDate='+strDate;
	}else{
		url = ay.contextPath+ '/nur/task/queryOrderByEvent.do?deptId='+deptId+'&queryDate='+strDate+"&type="+type+"&flag=1";
	}
	$.get(url, function(result) {
		try {
			var date = result.lst;
			var str='';
			for(var i=0; i<date.length; i++){
				if(type == 'shoushu'){
					str = str + '<tr><td>'+date[i].bedCode+'</td><td>'+date[i].patientName+'</td><td>'+date[i].surgery+'</td><td>'+date[i].status+'</td><td>'+date[i].date+'</td><td></td></tr>';									
				}else if(type == 'cy'){
					str = str + '<tr><td>'+date[i].bedCode+'</td><td>'+date[i].patientName+'</td><td>'+date[i].diagName+'</td><td>'+date[i].date+'</td><td></td></tr>';
				}else{
					str = str + '<tr><td>'+date[i].bedCode+'</td><td>'+date[i].patientName+'</td><td>'+date[i].diagName+'</td><td></td></tr>';				
				}
			}
			if(type == null){
				$('.ruyuan').append(str);
			}else if('shoushu' == type){
				$('.shoushu').append(str);
			}else{
				$('.chuyuan').append(str);
			}			
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});
}

function getHospitalInfor(type,strDate) {	
	var url ;
	if(type==null){
		url = ay.contextPath+ '/nur/task/queryOrderByEvent.do?deptId='+deptId+'&queryDate='+strDate;
	}else{
		url = ay.contextPath+ '/nur/task/queryOrderByEvent.do?deptId='+deptId+'&queryDate='+strDate+"&type="+type;
	}	
	$.get(url, function(result) {
		try {
			if('shoushu' == type){
				var _data = result.lst;
				$('#admitHospital').datagrid({
					data : _data,
					fit : true,
					singleSelect : true,
					columns : [ [ {
						field : 'bedCode',
						title : '床号',
						width : 115
					}, {
						field : 'patientName',
						title : '姓名',
						width : 230
					},  {
						field : 'surgery',
						title : '手术名称',
						width : 250
					}, {
						field : 'status',
						title : '手术状态',
						width : 250
					},{
						field : 'nur_log_book',
						title : '护理交班',
						width : 250
					} ] ]
				});
			}else if('cy' == type){
				var _data = result.lst;
				$('#admitHospital').datagrid({
					data : _data,
					fit : true,
					singleSelect : true,
					columns : [ [ {
						field : 'bedCode',
						title : '床号',
						width : 170
					}, {
						field : 'patientName',
						title : '姓名',
						width : 295
					},  {
						field : 'diagName',
						title : '诊断',
						width : 315
					}, {
						field : 'nur_log_book',
						title : '护理交班',
						width : 315
					} ] ]
				});
			}else{
				var _data = result.lst;
				$('#admitHospital').datagrid({
					data : _data,
					fit : true,
					singleSelect : true,
					columns : [ [ {
						field : 'bedCode',
						title : '床号',
						width : 170
					}, {
						field : 'patientName',
						title : '姓名',
						width : 295
					},  {
						field : 'diagName',
						title : '入院诊断',
						width : 315
					}, {
						field : 'nur_log_book',
						title : '护理交班',
						width : 315
					} ] ]
				});
			}			
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});
}

function getCAresult() {
	var url = ay.contextPath + "/nur/nursingMemo/getCAresult.do";
	$.get(url, function(result) {
		try {
			var date = result;
			if("0" == date[0]) {
				$("#telEdit").hide();
				$("#edit").hide();
			}
			nurseId = date[1];
			nurseName = date[2];
			deptId = date[3];
			getInformation();
			getPhoneNum();
			getMessage();
			getTreatMentItem();
			var strDate = new Date().format("yyyy-MM-dd");
			getHospitalInfor(null,strDate);
			getHospitalInforToBig(null,strDate,'#hospitalinfo1');
			getHospitalInforToBig('shoushu',strDate,'#hospitalinfo2');
			getHospitalInforToBig('cy',strDate,'#hospitalinfo3');			
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});
}

function getInformation() {
	var url = ay.contextPath
			+ "/nur/patientGlance/queryDeptSummary.do?workUnitType=1&workUnitCode="+deptId;
	$.get(url, function(result) {
		try {
			var date = result.lst.workUnitStatistics;
			var str = "<p>" + "<span class='text'>现有患者：</span><font>"+(date.inpatientCount-date.emptyBedCount)
					+ "</font>人"
					+ "<span class='text'>入院：</span><font>" + date.newPatientCount + "</font>人" 
					+ "<span class='text'>出院：</span><font>" + date.dischargeCount + "</font>人" 
					+ "<span class='text'>转入：</span><font>" + date.transInPatientCount + "</font>人" 
					+ "<span class='text'>转出：</span><font>" + date.transOutPatientCount + "</font>人" 
					+ "<span class='text'>手术：</span><font>" + date.inSurgeryPatientCount + "</font>人" 
					+ "<span class='text'>病重患者：</span><font>" + date.seriousPatientCount +"</font>人"
					+"<span class='text'>病危患者：</span><font>" + date.criticalPatientCount +"</font>人"
					+ "<span class='text'>分娩：</span><font>"+date.inDeliveryPatientCount + "</font>人" 
					+ "<span class='text'>死亡：</span><font>"+date.deadPatientCount + "</font>人" 
					+ "</p>";
			$('#information').append(str);
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});
}

function autoScroll(obj, ul_bz) {
	if($('#messageList').height()>$('#messageDiv').height()){
		$(obj).find(ul_bz).animate({
			marginTop : "-15px"
		}, 500, function() {
			$(this).css({
				marginTop : "0px"
			}).find("li:first").appendTo(this);
		});
	}	
}
setInterval('autoScroll("#messageDiv", "#messageList")', 3000);

Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}

/*var speedhq=60;
function Marqueehq(){
	if($('#listBox').height()>$('#dateList').height()){
		if(dateList.scrollTop==listBox.offsetHeight){
			dateList.scrollTop=0;
		}else{
			dateList.scrollTop++;
		}
		$('#listBox').css("display", "block");
	}else{
		$('#listBox').css("display", "none");
	}
}
var MyMarhq=setInterval('Marqueehq',speedhq);
dateList.onmouseover=function() {clearInterval(MyMarhq)}
dateList.onmouseout=function() {MyMarhq=setInterval(Marqueehq,speedhq)}*/

/*
*设置节点自适应宽高度
*/
function fixSize(pNode,cNode,arr){
	var pN = $("#"+pNode),
		cN = $("#"+cNode);
	console.log(pN+','+cN);
	for(var i=0; i<arr.length;i++){
		cN.css(arr[i][0],parseInt(pN.css(arr[i][0]))-arr[i][1]);
	}
}

/*
* 保存/取消 科室常用电话修改
*/
function saveTel(){
	setPhoneNum();
	$('#edit-useful-tel').dialog('close');
}
function cancelSave(){
	$('#edit-useful-tel').dialog('close');
}