<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/WEB-INF/view/resource/base.jsp" nav-left="nas">
	<template:replace name="title">
		PaaSOS--资源服务
	</template:replace>
	<template:replace name="content-path">
		<em>&gt;</em>
		<p class="app_a"> 数据存储 </p>
	</template:replace>
	<template:replace name="content-body">
		<div class="plate" style="height: 142px;">
            <div class="project_r">
                <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="${WEB_APP_PATH}/resource/nas/nfs/add.do" class="btn btn-default btn-oranger f16 mr20"> 创建实例 </a>
                    <em class="mr10 f14">|</em>
                    <span class="f14">NFS即网络文件系统，它允许网络中的计算机之间通过TCP/IP网络透明地共享资源，就像访问本地文件一样。
                    	</span>
                </div>
                <!--网络存储实例开始--> 
                <div class="r_block p20">
                    <div class="r_title">
                        <!-- <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="javascript:void(0);">
                                <i class="icons ico_title_list mr5"></i>我的NFS数据卷
                            </a>
                        </h3> -->
                         <h3 class="f14 h3_color h3_btm_line  ${param['tab'] == 'tenant' ? ' gray noactive' : ' blue '}">
	                            <a href="${param['tab'] == 'tenant' ? (requestScope.WEB_APP_PATH.concat('/resource/nas/nfs/index.do')) : 'javascript:void(0);'}">
	                                <i class="icons cy_ico mr5"></i>我的NFS数据卷
	                            </a>
	                        </h3>
	                        <h3 class="f14 h3_color h3_btm_line   ${param['tab'] == 'tenant' ? ' blue ' : ' gray noactive'}">
	                            <a href="${param['tab'] == 'tenant' ? 'javascript:void(0);' : (requestScope.WEB_APP_PATH.concat('/resource/nas/nfs/index.do?tab=tenant')) }">
	                                <i class="icons cy_ico mr5"></i>组织的NFS数据卷
	                            </a>
	                        </h3>
                        <div class="title_line"></div>
                        <div class="title_tab">
                        	<h5 class="f14">
                            	<a class="active_green" href="javascript:void(0);" onclick="queryNASsInfo()">
		<i class="icons refresh_ico_yellow mr5"></i>刷新</a>
                        	</h5>
                        </div>
                    </div>
	                <div id="naslist" class="r_con p10_0">
	                </div>
                </div>
                <!--网络存储实例结束-->
            </div>
        </div>
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script type="text/javascript" src="${ WEB_APP_PATH }/common/js/verify.js"></script>
		<script>
			var type = 'nfs';

			$(queryNASsInfo());
			
			function queryNASsInfo(pageSize, pageNo){
				
				var url= "${WEB_APP_PATH}/resource/nas/"+type+"/list.do";
				var params = {};
		    	params.listType = "${param['tab'] == 'tenant' ? 'tenant' : 'notenant'}";
				if(typeof pageSize !='undefined' && !isNaN(pageSize)){
					url = url +"?pageSize="+pageSize
				}
				if(typeof pageNo !='undefined' && !isNaN(pageNo)){
					url = url + "&pageNo="+pageNo
				}
				$("#naslist").html("<img src='"+WEB_APP_PATH+"/assets/images/loading.gif' />&nbsp; 正在加载数据中.....")
				.load(url,params,function(responseTxt,statusTxt,xhr){
					if(statusTxt=="error")
						common.alert("Error: "+xhr.status+": "+xhr.statusText);
					if(xhr.readyState==4 && xhr.status==200){
						var array = $(".nfsInfo");
						if(array && array.length >0){
							for(var i=0; i< array.length;i++){
								var tr = $(array[i]);
								queryPaasNasStatus(tr);
							}
						}
					}
				});
			}
			
			function queryPaasNasStatus(tr){
				return;
				var resId = tr.attr("data");
				var url = "${WEB_APP_PATH}/resource/nas/queryNasStatusInfo.do";
				$.get(url,{"resId":resId},function(json){
					var status = "";
					if(json.code==0){
						var status = json.data;
					}else{
						status = "暂时无法获取网络存储状态";
					}
					tr.find("."+resId).html(status);
				});
			}
			
			function gotoPage(pageSize, pageNo){
				queryNASsInfo(pageSize, pageNo)
			}
			
			function deletePaasNas(nfsId){
				var callback = new deleteResourceCallBack(nfsId);
				var verify = new Verify(callback,"网络存储");
				verify.pwdConfirm();
			}
			
			function deleteResourceCallBack(nfsId){
				this.nfsId = nfsId;
			}
			deleteResourceCallBack.prototype.exec = function(){
				var url = "${WEB_APP_PATH}/resource/nas/nfs/delete.do";
				var load = common.loading();
				var resId = this.nfsId;
				$.post(url,{"resId":resId},function(json){
					load.close();
					if(json.code==0){
						$(".nfsInfo").each(function(){
							if($(this).attr("data")==resId){
								$(this).remove();
							}
						});
					}else{
						common.tips("删除网络存储失败");
					}
				});
			}
			function showNSAInfo(resId){
				var url ="${WEB_APP_PATH}/resource/nas/nfs/info.do?nasId="+resId;
				common.dialogIframe( '网络存储实例',url,500,400);
			}
			function PaasNasGrant(nfsId){
				var url ="${WEB_APP_PATH}/resource/nas/GrantAccess.do";
				var data ={};
				data.nfsId = nfsId;
				var load = common.loading();
				$.post(url,data,function(json){
					load.close();
					if(json.code==0){
						common.tips("共享存储授权成功");
					}else{
						common.tips("共享存储授权操作失败");
					}
				})
			}
			function PaasNasAttach(nfsId){
				var url ="${WEB_APP_PATH}/resource/nas/attach.do";
				var data ={};
				data.nfsId = nfsId;
				var load = common.loading();
				$.post(url,data,function(json){
					load.close();
					if(json.code==0){
						var html = '<div style="margin-top:20px;margin-left:20px;">&nbsp;&nbsp;&nbsp;&nbsp;服务器：<select id="mountServer"></select></div>\
							<div style="margin-top:20px;margin-left:20px;">挂载目录：\
								<input type="text" style="width:80px;text-align:right;border-right:0px;padding-right:0px;background-color:#fff;" readonly="readonly" value="/dev/mount_"/>\
								<input type="text" style="width:100px;text-align:left;border-left:0px;padding-left:0px;" id="mountPath" /></div>\
							<div style="margin-top:20px;margin-left:120px;"><button id="saveMountBtn" type="button" class="btn btn-info btn-right">确认</button></div>';
						var serverData = json.data;
						if(!serverData){
							common.tips("没有服务器可以挂载");
							return false;
						}
						var dialog = common.dialog({
							type: 1,
							content:html,
							title:"挂载存储卷",
							area: ['420px', '240px']
						});
						for(var serverName in serverData){  
		                   var serverId = serverData[serverName];
		                   $("#mountServer").append("<option value='"+serverId+"'>"+serverName+"</option>");
		                } 
						$("#saveMountBtn").click(function(){
							dialog.close();
							common.tips("暂不开放该功能");
						});
					}else{
						common.tips("无法获取挂载服务器信息");
					}
				})
			}
		</script>
	</template:replace>
</template:include>

