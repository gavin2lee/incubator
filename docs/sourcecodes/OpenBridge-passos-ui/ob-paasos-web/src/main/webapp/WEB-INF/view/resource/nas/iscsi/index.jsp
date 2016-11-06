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
                    <a href="${WEB_APP_PATH}/resource/nas/iacsi/add.do" class="btn btn-default btn-oranger f16 mr20"> 创建实例 </a>
                    <em class="mr10 f14">|</em>
                    <span class="f14">ISCSI 技术是一种储存技术，该技术是将现有SCSI接口与以太网络(Ethernet)技术结合。
                    	</span>
                </div>
                <!--网络存储实例开始--> 
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="javascript:void(0);">
                                <i class="icons ico_title_list mr5"></i>我的ISCSI数据卷
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
			var type = 'iscsi';

			$(queryNASsInfo());
			
			function queryNASsInfo(pageSize, pageNo){
				
				var url= "${WEB_APP_PATH}/resource/nas/"+type+"/list.do";
				if(typeof pageSize !='undefined' && !isNaN(pageSize)){
					url = url +"?pageSize="+pageSize
				}
				if(typeof pageNo !='undefined' && !isNaN(pageNo)){
					url = url + "&pageNo="+pageNo
				}
				$("#naslist").html("<img src='"+WEB_APP_PATH+"/assets/images/loading.gif' />&nbsp; 正在加载数据中.....")
				.load(url,function(responseTxt,statusTxt,xhr){
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
			 
			
			function gotoPage(pageSize, pageNo){
				queryNASsInfo(pageSize, pageNo)
			}
			
			function deletePaasNas(nfsId,tenantId,userId){
				var callback = new deleteResourceCallBack(nfsId,tenantId,userId);
				var verify = new Verify(callback,"网络存储");
				verify.pwdConfirm();
			}
			
			function deleteResourceCallBack(nfsId,tenantId,userId){
				this.nfsId = nfsId;
				this.tenantId = tenantId;
				this.userId = tenantId;
			}
			deleteResourceCallBack.prototype.exec = function(){
				var url = "${WEB_APP_PATH}/resource/nas/iscsi/delete.do";
				var load = common.loading();
				var resId = this.nfsId;
				$.post(url,{"resId":resId,"paasOsTenantId":this.tenantId,"paasOsUserId":this.userId},function(json){
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
				var url ="${WEB_APP_PATH}/resource/nas/iscsi/info.do?nasId="+resId;
				common.dialogIframe( '网络存储实例',url,500,400);
			} 
		</script>
	</template:replace>
</template:include>

