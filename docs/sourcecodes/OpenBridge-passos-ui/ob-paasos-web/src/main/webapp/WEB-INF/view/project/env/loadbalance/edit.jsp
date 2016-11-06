<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
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
			action="${WEB_APP_PATH}/project/env/loadbalance/update.do?projectId=${project.projectId}&envId=${env.envId}"
			method="post">
			<input type="hidden" name="confId" value="${ loadbalance.confId }">
			<div class="r_block">
				<div class="r_con p10_0">
					<div class="form_control p20">
						<div class="form_block">
							<label>负载名称</label> <input type="text"  id="nginxConfigName" name="nginxConfigName"
								style=" " placeholder=" " value="${ loadbalance.nginxConfigName }">
						</div>
						<div class="form_block" id="nginxarea">
						
							<label>访问域名<span style="display: inline-block;padding-left: 10px;"><a style="font-size:22px;" title="新增域名" href="javascript:void(0)" id="addUrl">+</a></span></label> 
							<div>
								<input value="${ loadbalance.url }" type="text" name="domain"  id="domain" style=" " placeholder=" "><span style="color:red">*无需带http</span>
							</div>
							<c:if test="${! empty loadbalance.slaveDomain }">
								<c:forEach items="${loadbalance.slaveDomain}" var="row">
									<div style="margin-top:5px;padding-left:130px;">
										<input value="${row }" type="text" name="slaveDomain" style=" " placeholder=" ">&nbsp;&nbsp;
										<a  style="font-size:22px;" title="删除域名" class="delete-url" href="javascript:void(0);">-</a>
									</div>
								</c:forEach>
							</c:if>
						</div>
						<div class="form_block">
							<label >同步部署代理:</label>
							<div >
								<label style="padding-left: 0px;"><input style="width: auto;"  ${loadbalance.synDeployIP !=null and loadbalance.synDeployIP=='true' ? 'checked="checked"' : '' } class="synDeployIP" type="radio" name="synDeployIP" value="true" />&nbsp;&nbsp;开启
									</label>
								<label>
									<input style="width: auto;"  ${ (empty loadbalance.synDeployIP) or loadbalance.synDeployIP=='false' ? 'checked="checked"' : '' } class="synDeployIP" type="radio" name="synDeployIP" value="false" />&nbsp;&nbsp;不开启</label> 
							</div>
						</div>
						<input type="hidden" name="versionId" value="${loadbalance.versionId }"/>
						<input type="hidden" name="versionCode" value="${loadbalance.versionCode }"/>
						<div class="form_block" style="display: none;">
							<label>开启SSL:</label>
							<div>
								<label onclick="changeSLL(true)" style="padding-left: 0px;">
									<input type="radio" name="activeSSL" value="true" style="width: auto;" ${ loadbalance.isSupportSSL ? 'checked="checked"' :'' } />&nbsp;&nbsp;开启
									</label>
								<label onclick="changeSLL(false)">
									<input type="radio" name="activeSSL" value="false" ${ not loadbalance.isSupportSSL ? 'checked="checked"' :'' } style="width: auto;" />&nbsp;&nbsp;不开启</label> 
							</div>
						</div>
						<div class="" style="clear: both;"></div>
						<div class="form_block" id="sslcsrDiv" style="display: none;">
							<input type="hidden" name="isSupportSSL"
								value="${ loadbalance.isSupportSSL }" />
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
						<div class="form-group" style="display:none;">
							<label class="col-xs-3">跨域ajax请求:</label>
							<div class="col-xs-9">
								<label>
									<input type="radio" name="domainCross" value="true" ${ loadbalance.domainCross ? 'checked="checked"' :'' }/>&nbsp;&nbsp;&nbsp;&nbsp;支持
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
								<label>
									<input type="radio" name="domainCross" value="false" ${ not loadbalance.domainCross ? 'checked="checked"' :'' } />&nbsp;&nbsp;&nbsp;&nbsp;不支持</label> 
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
									<c:forEach items="${loadbalance.confList}" var="confItem">
										<tr>
											<td><input type='text' name='ip' class=' ' value="${ confItem.ip }" /></td>
											<td><input type='text' name='port' class=' ' value="${ confItem.port }" /></td>
											<td><input type='text' name='weight' class=' ' value="${ confItem.weight }" /></td>
											<td align='center'><a href='javascript:void(0)' onclick='removeRow(this)' title='删除本行'>-</a></td>
										</tr>
									</c:forEach>
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
			//nginx support SSL
			
		function dataValidation(){
			if($.trim($("#nginxConfigName").val())==""){
	    		top.common.alert("负载名称不能为空");
	    		return false;
	    	}
			var domain = $.trim($("#domain").val());
			if(domain==""){
	    		top.common.alert("访问域名不能为空");
	    		return false;
	    	}else{
	    		var patrn=/^[0-9a-zA-Z][0-9a-zA-Z-]{0,62}\.([0-9a-zA-Z][0-9a-zA-Z-]{0,62}\.)*[a-zA-Z]{2,4}(:[0-9]{2,5})?$/;
	    		if (!patrn.test(domain)){
	    			top.common.alert("域名格式有误！");
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
			
	    	return true;
	    }
			$(function(){
				var activeSLL='${loadbalance.isSupportSSL}';
				if(activeSLL=='true'){
					var sslCrtId = '${loadbalance.sslCrtId}'.split("|");
					var sslCrt = {};
					sslCrt.attId = sslCrtId[0];
					sslCrt.attPath = sslCrtId[1];
					sslCrt.attName = sslCrtId[2];
					showSSLInfo(sslCrt,"sslCrt");
					var sslKeyId = '${loadbalance.sslKeyId}'.split("|"); 
					var sslKey = {};
					sslKey.attId = sslKeyId[0];
					sslKey.attPath = sslKeyId[1];
					sslKey.attName = sslKeyId[2];
					showSSLInfo(sslKey,"sslKey"); 

					$("#sslcsrDiv").show();
				}
			});
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
					else{
						top.common.alert(json['msg']);
						removeSLLAttach(divId);
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
