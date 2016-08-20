<%@ page contentType="text/html; charset=UTF-8"%>

<!-- UJian Button BEGIN <div class="ujian-hook"></div>-->
<!-- <script type="text/javascript" src="http://v1.ujian.cc/code/ujian.js?type=slide&fade=1&uid=1880230"></script> -->
<!-- <a href="http://www.ujian.cc" style="border:0;"><img src="http://img.ujian.cc/pixel.png" alt="友荐云推荐" style="border:0;padding:0;margin:0;" /></a> -->
<!-- UJian Button END -->

<style>
	
	/* IndexBottom */
.IndexBottom{margin-top:10px;height: 200px;background: #f9f9f9;border: 1px solid #d9d9d9;}
.IndexBottom dl{float: left;display: inline;width: 110px;margin-top: 22px;margin-left: 43px;}
.IndexBottom dt{margin-left: 5px;font-family: "Microsoft YaHei",\5fae\8f6f\96c5\9ed1,\5b8b\4f53;font-size: 16px;}
.IndexBottom dd{margin-top: 4px;}
.IndexBottom dd span{display: block;margin-top: 4px;}
.IndexBottom dd span a{background: url(../i/index_sprite.png) no-repeat -170px -336px;padding-left: 18px;+background-position: -170px -338px;_background-position: -170px -336px;}
.IndexBottom dd span a:hover{background: url(../i/index_sprite.png) no-repeat -141px -316px;+background-position: -141px -318px;_background-position: -141px -316px;}
.IndexBottom .aboutjy{margin-left: 68px;}
.IndexBottom .IndexBottom-help{margin-left: 112px;}
</style>
	<div id="footer">
		<div class="container" style="margin-top: 10px;padding-top: 10px;">
			<div class="IndexBottom">
				<div class="row" style="margin-top: 10px;">
					<%application.setAttribute("newCatalogs", SystemManager.newCatalogs);%>
					<s:iterator value="#application.newCatalogs" status="i" var="row">
						<div class="col-md-2">
							<div class="row" style="margin-bottom: 10px;"><strong><s:property escape="false" value="name" /></strong></div>
							<s:iterator value="news" status="i" var="newItem">
								<div class="row" style="line-height: 20px;">
									<a href="<%=request.getContextPath() %>/help/<s:property escape="false" value="code" />.html" target="_blank">
										<s:property escape="false" value="title" />
									</a>
								</div>
							</s:iterator>
						</div>
					
					</s:iterator>
				</div>
			</div>
			
			<br>
			<!-- 友情链接 -->
			<%
			request.setAttribute("navigations", SystemManager.navigations);
			%>
			<div class="row" style="margin-top: 0px;text-align: center;">
				<div class="col-md-12">
					<div style="text-align: center;margin: auto;">
						<%request.setAttribute("navigations", SystemManager.navigations);%>
						<s:iterator value="#request.navigations" status="ii" var="item2">
							<div style="float: left;margin: 5px;">
								<a href="http://<s:property escape="false" value="http"/>" target="_blank"><s:property escape="false" value="name" /></a>
							</div>
						</s:iterator>
					</div>
				</div>
			</div>
			
			<div class="row" style="margin-top: 5px;display: inline;">
				<div class="col-md-3">
				</div>
				<div class="col-md-5">
					<p style="text-align: center;"><%=SystemManager.systemSetting.getIcp() %>
						<a target="_blank" href="http://www.aliyun.com/"><img src="http://gtms01.alicdn.com/tps/i1/T1W6.aFbFbXXcZj_6s-96-18.png" alt="运行在阿里云" /></a>
					</p>
				</div>
				<div class="col-md-1">
					<!-- cnzz站点统计 -->
					<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1000234875'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s22.cnzz.com/z_stat.php%3Fid%3D1000234875%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script>
				</div>
				<div class="col-md-3">
				</div>
			</div>
		</div>
	</div>

<%@ include file="/resource/common_js.jsp"%>
<%@ include file="fixed.jsp"%>
<!-- QQ登陆 -->
<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" charset="utf-8" data-appid="101020775" data-callback="true"></script>
<script>
$(function() {
	$("#myshopMenuPPP").hover(
		function(){
			$("#myshopMenu").show();
		},
		function(){
			$("#myshopMenu").hide();
		}		
	);
	$("img").lazyload({
		effect : "fadeIn"
	});
	
	try{
		console.log("_qqLoginCheck...");
		_qqLoginCheck();
		console.log("_qqLoginCheck...end");
	}catch(e){
		console.log("_qqLoginCheck异常="+e);
	}
});

function _qqLoginCheck(){
	if(QC.Login.check()){//如果已登录
		console.log("qq已登陆");
		try{
	    	QC.Login.getMe(function(openId, accessToken){
				//alert(["当前登录用户的", "openId为："+openId, "accessToken为："+accessToken].join("\n"));
				console.log(["当前登录用户的", "openId为："+openId, "accessToken为："+accessToken].join("\n"));
				//$("#qqLoginBtn").html("qq登陆了");
				notifySession("login",openId,accessToken);
			});
	    }catch(e){
	    	console.log("QC.Login.getMe异常="+e);
	    }
		//cccccc();
		//这里可以调用自己的保存接口
		//...
	}else{
		console.log("qq未登陆");
		//notifySession("loginOut");
	}
}

function cccccc(){
	QC.Login({btnId:"qqLoginBtn"}, function(oInfo, oOpts){
		console.log(oInfo.nickname+",figureurl="+oInfo.figureurl);//昵称、头像
		console.log("QQ登陆成功回调处理..");
	    var showQQStr = "";
		
	    try{
	    	QC.Login.getMe(function(openId, accessToken){
				//alert(["当前登录用户的", "openId为："+openId, "accessToken为："+accessToken].join("\n"));
				console.log(["当前登录用户的", "openId为："+openId, "accessToken为："+accessToken].join("\n"));
				//$("#qqLoginBtn").html("qq登陆了");
				notifySession("login",openId,accessToken);
			});
	    }catch(e){
	    	console.log("QC.Login.getMe异常="+e);
	    }
		
		showQQStr += "<span class=\"qc_item figure\">";
		//showQQStr = "<img src=\"http://qzapp.qlogo.cn/qzapp/101020775/70FDABDD0704836023BB468D9FF12025/30\" class=\"undefined\">";
		showQQStr += "<img src='"+oInfo.figureurl+"' class=\"undefined\">";
		showQQStr += "</span>";
		showQQStr += "<span class=\"qc_item nickname\" style=\"margin-left:6px;\">"+oInfo.nickname;
	    showQQStr += "</span>";
	    showQQStr += "<span class=\"qc_item logout\">";
	    showQQStr += "<a href=\"javascript:qqLoginOut();\" style=\"margin-left:6px;\">";
	    showQQStr += "退出</a>";
	    showQQStr += "</span>";
	    
	    $("#loginOrRegSpan").hide();
	    $("#myshopMenuPPP").show();
	    $("#qqLoginBtn").html(showQQStr);
	});
}

//QQ退出登陆
function qqLoginOut(){
	try{
		QC.Login.signOut();
	}catch(e){
    	console.log("qqLoginOut=QC.Login.signOut()异常="+e);
    }
	notifySession("loginOut",'','');
	$("#loginOrRegSpan").show();
	$("#myshopMenuPPP").hide();
	$("#qqLoginBtn").html("");
}

function notifySession(status,openId,accessToken,nickname){
	var _url = "/user/qqCallbackNotifySession.html?status="+status+"&openId="+openId+"&accessToken="+accessToken+"&nickname="+nickname;
	console.log("_url="+_url);
	$.ajax({
	  type: 'POST',
	  url: _url,
	  data: {},
	  success: function(data){
		  console.log("notifySession.data="+data);
	  },
	  dataType: "text",
	  error:function(er){
		  console.log("notifySession.er="+er);
	  }
	});
}
//搜索商品
function search(){
	var _key = $.trim($("#key").val());
	if(_key==''){
		return false;
	}
	$("#searchForm").submit();
}
</script>


	<!-- baidu fenxiang -->
<!-- 
<script type="text/javascript" id="bdshare_js" data="type=slide&amp;img=0&amp;pos=right&amp;uid=732516" ></script>
<script type="text/javascript" id="bdshell_js"></script>
<script type="text/javascript">
document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date()/3600000);
</script>
 -->
<!-- Baidu Button END -->