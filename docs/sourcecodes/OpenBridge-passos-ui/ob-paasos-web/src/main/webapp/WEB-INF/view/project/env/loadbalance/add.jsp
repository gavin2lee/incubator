<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="title">
		添加负载均衡
	</template:replace>
	<template:replace name="body">
		<style type="text/css">
			body {
				overflow: auto;
			}
			.form_block {
				padding: 5px;
			}
			.form_block label {
				width: 100px;
			}	
			.form_block input {
				width: 280px;
			}
			.form_block table {
				width: 380px;
			}
			.form_block table input {
				width: 80%;
			} 
			.form_block table th,.form_block table td{
				border: 1px solid #ccc;
				padding: 5px;
			}
		</style>
		<form class=" " id="balanceForm" class=" "
			action="${WEB_APP_PATH}/project/env/loadbalance/save.do?projectId=${project.projectId}&envId=${env.envId}"
			method="post">

			<div class="r_block">
				<div class="r_con p10_0">
					<div class="form_control p20">
						<div class="form_block">
							<label>负载名称</label> <input type="text" name="nginxConfigName"  id="nginxConfigName"
								style=" " placeholder=" ">
						</div>
						<div class="form_block" id="nginxarea">
							<label>访问域名<span style="display: inline-block;padding-left: 10px;"><a style="font-size:22px;" title="新增域名" href="javascript:void(0)" id="addUrl">+</a></span></label> 
							<div>
								<input type="text" name="domain"  id="domain" style=" " placeholder=" "><span style="color:red">*无需带http</span>
							</div>
						</div>
						<div class="form_block">
							<label>同步部署代理:</label>
							<div>
								<label style="padding-left: 0px;"><input style="width: auto;" class="synDeployIP" type="radio" name="synDeployIP" value="true" />&nbsp;&nbsp;开启
								</label>
								<label>
									<input style="width: auto;" class="synDeployIP" type="radio" name="synDeployIP" value="false" checked="checked" />&nbsp;&nbsp;不开启</label> 
							</div>
						</div>
						<div class="form_block" style="display: none;">
							<label>开启SSL:</label>
							<div>
								<label onclick="changeSLL(true)" style="padding-left: 0px;">
									<input type="radio" name="activeSSL" value="true" style="width: auto;" />&nbsp;&nbsp;开启
									</label>
								<label onclick="changeSLL(false)">
									<input type="radio" name="activeSSL" value="false" checked="checked"  style="width: auto;" />&nbsp;&nbsp;不开启</label> 
							</div>
						</div>
						<div class="" style="clear: both;"></div>
						<div class="form_block" id="sslcsrDiv" style="display: none;">
							<input type="hidden" name="isSupportSSL"
								value="false" />
							<label>SSL证书:</label>
							<div style="float: left">
								<label style="padding-left: 0px;">证书.crt文件</label>
								<div id="sslCrt" style="float: left;"></div>
								<div style="clear: both;height: 1px;background: #eee"></div>
								<label style="padding-left: 0px;">证书.key文件</label>
								<div id="sslKey" style="float: left;"></div>
							</div>
						</div>
						<div class="" style="clear: both;"></div>
						<div class="form_block">
							<label>代理记录</label>
							<table class="">
								<thead>
									<tr style="height: 37px;">
										<th width="50%">IP</th>
										<th width="20%">端口</th>
										<th width="20%">权重</th>
										<th width="10%" style='text-align: center;'><a
											href="javascript:void(0)" onclick="addRow()">+</a></th>
									</tr>
								</thead>
								<tbody id="domainTr">

								</tbody>
							</table>
						</div>
						<div class="form_block mt10">
							<label>&nbsp;</label>
							<button type="button" class="btn btn-default f16  mt10"
								onclick="onUserEnter();">
								<i class="ico_check"></i>确 定
							</button>
						</div>
					</div>
				</div>
			</div>
			
		</form>
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script>
		function dataValidation(){
			if($.trim($("#nginxConfigName").val())==""){
	    		common.alert("负载名称不能为空");
	    		return false;
	    	}
			
			var domain = $.trim($("#domain").val());
			var patrn=/^[0-9a-zA-Z][0-9a-zA-Z-]{0,62}\.([0-9a-zA-Z][0-9a-zA-Z-]{0,62}\.)*[a-zA-Z]{2,4}(:[0-9]{2,5})?$/;
			if(domain==""){
	    		common.alert("访问域名不能为空");
	    		return false;
	    	}else{
	    		if (!patrn.test(domain)){
	    			common.alert("域名格式有误！");
	    			return false;
	    		}
	    	}

			var isError = false;
			$('#nginxarea').find('input[name="slaveDomain"]').each(function(domIndex,domEle){
				domain = $.trim($(domEle).val());
				if(domain==""){
		    	common.alert("设置多个域名不能有空值存在");
		    	isError = true;
		    	return false;
		    }else{
		    	if (!patrn.test(domain)){
		    		common.alert(domain+":域名格式有误！");
		    		isError = true;
		    		return false;
		    		}
		    	}
			});
			if(isError){
				return false;
			}
		    
			var balanceCount = $("#domainTr").find("tr").length;
	    	if(balanceCount==0){
	    		top.common.tips("没有添加代理记录");
	    		return false;
	    	}
	    	for(var i=0 ;i < balanceCount;i++){
	    		var dom = $("#domainTr tr")[i];
				var ip=$(dom).find("input:first").val();
				var reg1 = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;
				if(ip =="" || !reg1.test(ip)){
					top.common.tips("第"+(i+1)+"行IP地址格式不正确",function(){
						$(dom).find("input:first").focus();
					});
					return false;
				}
				var port=$(dom).find("input:eq(1)").val(); 
				if(port =="" || isNaN(port) || parseInt(port)<0 || parseInt(port)>65536){ 
					top.common.tips("第"+(i+1)+"行端口格式不正确",function(){
						$(dom).find("input:eq(1)").focus();
					});
					return false;
				}
				var weight=$.trim($(dom).find("input:eq(2)").val());
				var reg3 = /^[1-9](\d)*$/; 
				if(weight == "" || !reg3.test(weight)){ 
					top.common.tips("第"+(i+1)+"行权重格式不正确，权重为数字",function(){ 
						$(dom).find("input:eq(2)").focus();
					});
					return false;
				}
	    	}
	    	return true;
	    }
			function changeSLL(sslEnable){
				var hidden = $("input[name='isSupportSSL']");
				if(sslEnable){
					$("#sslcsrDiv").show();
					hidden.val("true");
					var crtSpan = $("#sslCrt").find("span").length;
					if(crtSpan==0){
						removeSLLAttach("sslCrt");
					}
					var keySpan = $("#sslKey").find("span").length;
					if(keySpan==0){
						removeSLLAttach("sslKey");
					}
				}else{
					$("#sslcsrDiv").hide();
					hidden.val("false");
				}
			}
			function addRow(){
				var row="<tr>"+
			  	"<td><input type='text' name='ip' class=' ' /></td>"+
			  	"<td><input type='text' name='port' class=' '/></td>"+
			  	"<td><input type='text' name='weight' class=' '/></td>"+
			  	"<td align='center'><a href='javascript:void(0)' onclick='removeRow(this)' title='删除本行'>-</a></td>"+
			  	"</tr>";
				$("#domainTr").append(row);
			}
			function removeRow(dom){
				$(dom).parent("td").parent("tr").remove();
			}
			function onUserCancel(){
				frameElement.dialog.close();
			}
			function onUserEnter(){
				var form = $("#balanceForm");
		    	var url = form.attr("action");
		    	var params = form.serialize();
		    	if(dataValidation()){
		    	var loading = top.common.loading(); 
		    	$.getJSON(url,params,function(json){ 
	    			loading.close();
		    		if(json.code==0){ 
		    			top.common.tips("成功环境配置",1,function(){
		    				frameElement.callback();
		    			});
		    		}else{ 
		    			top.common.alert(json.msg);
		    		}
		    	});
		    	}
			}
			function showSSLInfo(sslCrt,divId){
				if(sslCrt){
					var sslcrtDIV = $("#"+divId);
					sslcrtDIV.empty();
					sslcrtDIV.append("<input type='hidden' name='"+divId+"Id' value='"+sslCrt.attId+"|"+sslCrt.filePath+"|"+sslCrt.attName+"'>");
					sslcrtDIV.append("<input type='hidden' name='"+divId+"Name' value='"+sslCrt.attName+"'>");
					sslcrtDIV.append("<span style=''>"+sslCrt.attName+"</span>");
					sslcrtDIV.append("<span>&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+WEB_APP_PATH+"/file/download.do?fileId="+sslCrt.attId+"' target='_blank'>下载</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick='removeSLLAttach(\""+divId+"\")'>删除</a></span>");
				}
			}
			function removeSLLAttach(divId){ 
				var sslcrtDIV = $("#"+divId);
				sslcrtDIV.empty();
				var ifrm = $("<iframe id='ifr"+divId+"' src='about:blank' style='border:0px;width: 100%;height:30px;overflow: hidden;' scrolling='no' frameborder='no'></iframe>");
				sslcrtDIV.append(ifrm);
				document.getElementById("ifr"+divId).contentWindow.document.write("<form action='"+WEB_APP_PATH+"/file/upload.do' method='post' enctype='multipart/form-data'><input type='file' name='upfile' onchange='document.forms[0].submit();'/></form>");
				document.getElementById("ifr"+divId).callback = function(json){
					if(json.code == 0 && json.data){
						var attachItem = json.attachlist[0];
						var data ={};
						showSSLInfo(attachItem,divId);
					}
				};
			}

			$(function(){
				$('#nginxarea').delegate('.delete-url','click',null,function(){
					$(this).parent().remove();
				});
				$('#addUrl').bind('click',function(){
					$('#nginxarea').append('<div style="margin-top:5px;padding-left:130px;"><input type="text" name="slaveDomain" style=" " placeholder=" ">&nbsp;&nbsp;<a  style="font-size:22px;" title="删除域名" class="delete-url" href="javascript:void(0);">-</a></div>');
				});
				$('.synDeployIP').bind('change',function(){
					var v = $(this).val();
					if(v=='false'){
						$('#domainTr').empty();
						return ;
					}
					else{
						var ref = $.ajax({
							'url' : '${WEB_APP_PATH}/project/deploy/${env.envId}/deploy/ports.do',
							'type' : 'post',
							'dataType' : 'json',
							'cache' : false,
							'data' : {
								'envId' : '${env.envId}',
								'projectId' : '${project.projectId}'
							}
						});
						ref['done'](function(msg){
							if(msg.data==null || msg.data.ports==null || msg.data.ports.length==0){
								return ;
							}
							for(var i=0;i<msg.data.ports.length;i++){
								var p = msg.data.ports[i];
								addRow();
								var $p = $('#domainTr').children(':last');
								$p.find('input[name="ip"]').val(p.hostIp);
								$p.find('input[name="port"]').val(p.nodePort);
								$p.find('input[name="weight"]').val(1);
							}
						});
					}
				});
			});
		</script>
	</template:replace>
</template:include>