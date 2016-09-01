var nodeStr ='<div class="nurMsgBox"><span class="msgBoxHover"></span><div class="block-items note" id="note" title="护理小白板"></div></div>';
$(document.body).append(nodeStr);
var currDate   = new Date();
var currentDay = currDate.getDate();
var recordDay  = currDate.format("yyyy-MM-dd");
$("#noticeMsgDate").val(recordDay);
$("#taskListDate").val(recordDay);
$("#speaker").click(function() {

	$("#msgPrompt").show();		
});

// 点击关闭按钮，消息弹出框隐藏
$("#close").click(function() {
	$("#msgPrompt").hide();
});

$("#note").click(function(){
	var url = ay.contextPath + '/nur/nurseWhiteBoard/noteShow.do';
//	var url = ay.contextPath + '/nur/nurseWhiteBoard/nurseWhiteBoardShowMain.do';
//	var url = ay.contextPath + '/nur/nursingMemo/oldNoteMain.do';
	window.open(url) ;
});

$("#noticeMsg, #taskList").click(function() {
	if($(this).attr('id')=="noticeMsg"){
		if($("#content1").hasClass('hide')){
			$("#content1").removeClass('hide');
			$("#content2").addClass('hide');
		}
	}
	else{
		if($("#content2").hasClass('hide')){
			$("#content2").removeClass('hide');
			$("#content1").addClass('hide');
		}
	}
	$(this).addClass("click");
	$(this).siblings("span.bg").removeClass("click");
});

// 通知消息日期选择按钮
$("#content1 .chooseDateBtn").click(function(){
	var cDate;
	if($(this).hasClass('arrow_l')){
		cDate = new Date($("#noticeMsgDate").val());
		day = cDate.getDate()-1;
		cDate.setDate(day);
		$("#noticeMsgDate").val(cDate.getFullYear()+'-'+(cDate.getMonth()+1)+'-'+cDate.getDate());
	}
	else{
		cDate = new Date($("#noticeMsgDate").val());
		day = cDate.getDate()+1;
		if(day > currentDay){
			return ;
		}
		cDate.setDate(day);
		$("#noticeMsgDate").val(cDate.getFullYear()+'-'+(cDate.getMonth()+1)+'-'+cDate.getDate());
	}
});
$("#content2 .chooseDateBtn").click(function(){
	var cDate;
	if($(this).hasClass('arrow_l')){
		cDate = new Date($("#taskListDate").val());
		day = cDate.getDate()-1;
		cDate.setDate(day);
		$("#taskListDate").val(cDate.getFullYear()+'-'+(cDate.getMonth()+1)+'-'+cDate.getDate());

	}
	else{
		cDate = new Date($("#taskListDate").val());
		day = cDate.getDate()+1;
		cDate.setDate(day);
		$("#taskListDate").val(cDate.getFullYear()+'-'+(cDate.getMonth()+1)+'-'+cDate.getDate());

	}
});
//getMessage();
function getMessage() {
	// remove for not user information
//	var deptId = $("#deptId").val();
//	var nurseId = $("#nurseId").val();
//	var dateTime = $("#noticeMsgDate").val();
//	var url = ay.contextPath + '/nur/task/notice/list.do?nurseId='+nurseId+'&deptId='+deptId+'&date='+dateTime;
//	console.log(url)
//	$.get(url, function(result) {
//		try {
//			$('#showMessage').empty();
//			var hour;
//			var min;
//			var date = result.lst;
//			var str = "<ul>";			
//			for(var i=0; i<date.length;i++){
//				var d = new Date(date[i].time);
//				d.getHours() < 10 ? hour = "0" + d.getHours() : hour = d.getHours();
//				d.getMinutes() < 10 ? min = "0" + d.getMinutes() : min = d.getMinutes();
//				str = "<li><span class='fl time'>"+hour+":"+min+"</span><span class='smsg fl' title='"+date[i].msgText+"'>"+date[i].nurseName+"： "+date[i].msgText+"</span></li>"
//				$('#showMessage').prepend(str);
//			}
//		} catch (e) {
//			$.messager.alert('提示', e);
//		}
//		setTimeout("getMessage()", 3000);
//	});
}




