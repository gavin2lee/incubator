function Verify(cb,name){
	this.cb=cb;
	this.name = name;
}
Verify.prototype = {
		pwdConfirm : function (){
			var html = '<div style="margin-top:20px;margin-left:20px;">用户密码：<input type="password" name="usrPwd" id="usrPwd" /></div>\
					<div style="margin-top:20px;margin-left:120px;"><button id="reVerConBtn" type="button" class="btn btn-info btn-right">确认</button></div>';
			var ddd = common.dialog({
				type: 1,
				content:html,
				title:"请输入用户密码以确认删除:  "+this.name,
				area: ['420px', '240px']
//				skin: 'layui-layer-demo'
			});
			var self = this;
			$("#reVerConBtn").click(function(){
				var validateResult=self.validatePwd($("#usrPwd").val());
				if(validateResult.valueOf()){
					self.cb.exec();//执行回调函数
				}else{
					common.alert("密码输入错误！",function(){});
				}
				ddd.close();
			});
		},
		
	validatePwd : function (pwd){
		var result = new Boolean();
		//同步执行以确保结果的可靠性
		var loadD = common.loading();
		$.ajax({
			type: "GET",
			url:WEB_APP_PATH+"/sys/user/validatePwd.do",
			data:{"pwd":pwd},
			dataType:"json",
			async:false,
			complete : function(xhr){
				loadD.close();
			},
			success:function(json){
				result = (json.code==0);
			}
		});	
		return result;
	}
}
$(document).ready(function(){
	if (!common) {
		var head = document.getElementsByTagName('head')[0];
        var script = document.createElement('script');
        script.src = "${ WEB_APP_PATH }/assets/js/common.js";
        script.type = 'text/javascript';
        head.appendChild(script);
	}
});