<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/WEB-INF/view/resource/base.jsp" nav-left="mysql">
	<template:replace name="title">
		PaaSOS--资源服务
	</template:replace>
	<template:replace name="content-path">
		<em>&gt;</em>
		<p class="app_a"> 数据库 </p>
	</template:replace>
	<template:replace name="content-body">
		<div class="plate" style="height: 142px;">
            <div class="project_r">
                <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="${WEB_APP_PATH}/resource/mysql/add.do" class="btn btn-default btn-oranger f16 mr20"> 创建实例 </a>
                    <em class="mr10 f14">|</em>
                    <span class="f14">数据库的实现采用MySQL，MySQL是全球最受欢迎的开源数据库，大型互联网公司都采用更为灵活的 MySQL构建了成熟的大规模数据库集群。</span>
                </div>
                <!--数据库实例开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <!-- <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="javascript:void(0);">
                                <i class="icons ico_title_list mr5"></i>我的数据库实例
                            </a>
                        </h3> -->
                         <h3 class="f14 h3_color h3_btm_line  ${param['tab'] == 'tenant' ? ' gray noactive' : ' blue '}">
	                            <a href="${param['tab'] == 'tenant' ? (requestScope.WEB_APP_PATH.concat('/resource/mysql/index.do')) : 'javascript:void(0);'}">
	                                <i class="icons cy_ico mr5"></i>我的数据库实例
	                            </a>
	                        </h3>
	                        <h3 class="f14 h3_color h3_btm_line   ${param['tab'] == 'tenant' ? ' blue ' : ' gray noactive'}">
	                            <a href="${param['tab'] == 'tenant' ? 'javascript:void(0);' : (requestScope.WEB_APP_PATH.concat('/resource/mysql/index.do?tab=tenant')) }">
	                                <i class="icons cy_ico mr5"></i>组织的数据库实例
	                            </a>
	                        </h3>
                        <div class="title_line"></div>
                        <div class="title_tab">
                        	<h5 class="f14">
                            	<a  class="active_green" href="javascript:void(0);" onclick="queryMysqlsInfo()">
		<i class="icons refresh_ico_yellow mr5"></i>刷新</a>
                        	</h5>
                        </div>
                    </div>
                    <div id="databaselist" class="r_con p10_0">
                     </div>
                </div>
                <!--数据库实例结束-->
            </div>
        </div>
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script type="text/javascript" src="${ WEB_APP_PATH }/common/js/verify.js"></script>
		<script type="text/javascript" src="${ WEB_APP_PATH }/common/js/resource_event.js"></script>
	<script>
		$(queryMysqlsInfo());
		
		function queryMysqlsInfo(pageSize, pageNo){
			var url= "${WEB_APP_PATH}/resource/mysql/list.do";
			var params = {};
	    	params.listType = "${param['tab'] == 'tenant' ? 'tenant' : 'notenant'}";
			if(typeof pageSize !='undefined' && !isNaN(pageSize)){
				url = url +"?pageSize="+pageSize
			}
			if(typeof pageNo !='undefined' && !isNaN(pageNo)){
				url = url + "&pageNo="+pageNo
			}
			$("#databaselist").html("<img src='"+WEB_APP_PATH+"/assets/images/loading.gif' />&nbsp; 正在加载数据中.....")
			.load(url,params,function(responseTxt,statusTxt,xhr){
				if(statusTxt=="error")
					common.alert("Error: "+xhr.status+": "+xhr.statusText);
				if(xhr.readyState==4 && xhr.status==200){
					var array = $(".mysqlInfo");
					if(array && array.length >0){
						for(var i=0; i< array.length;i++){
							var tr = $(array[i]);
							queryPaasMysqlStatus(tr);
						}
					}
				}
			});
		}
		
		function queryPaasMysqlStatus(tr){
			var resId = tr.attr("data");
			var mysqlId = tr.attr("id");
			if(resId==''){
				tr.find("."+mysqlId).empty().html("创建中");
				return;
			}
			tr.find("."+mysqlId).empty().html("<img src='"+WEB_APP_PATH+"/assets/images/loading.gif' />");
			var url = "${WEB_APP_PATH}/resource/mysql/queryMysqlStatusInfo.do";
			$.get(url,{"resId":mysqlId},function(json){
				var data = {};
				if(json.code==0){
					var data = json.data;
				}else{
					data.status = "无法获取";
				}
				renderPaasMysqlStatus(data,mysqlId)
			});
		}
		
		function gotoPage(pageSize, pageNo){
			queryMysqlsInfo(pageSize, pageNo)
		}
		
		function deletePaasMysql(mysqlId,tenantId,userId){
			var callback = new deleteResourceCallBack(mysqlId);
			var verify = new Verify(callback,"MySQL");
			verify.pwdConfirm();
		}
		
		function deleteResourceCallBack(mysqlId){
			this.mysqlId = mysqlId;
		}
		deleteResourceCallBack.prototype.exec = function(){
			var url = "${WEB_APP_PATH}/resource/mysql/delete.do";
			var load = common.loading();
			$.post(url,{"resId":this.mysqlId},function(json){
				load.close();
				if(json.code==0){
					common.goto("${WEB_APP_PATH}/resource/mysql/index.do");
				}else{
					common.tips("删除数据库实例失败");
				}
			});
		}
		function openMysqlAdminUi(url){
			common.dialogIframe('数据库管理',url,900,600);
		}
		function openPhpMyAdmin(phpMyAdminUrl,mysqlClusterIP){
			if($.trim(phpMyAdminUrl)!=''){
				var url = $.trim(phpMyAdminUrl);
				if(url.indexOf('index.php')>0){
					url = url+"?hosts="+mysqlClusterIP;
				}else{
					if(url.substring(url.length-1)=='/'){
						url = url+"index.php?hosts="+mysqlClusterIP;
					}else{
						url = url+"/index.php?hosts="+mysqlClusterIP;
					}
				}
				window.open(url);
			}
		}
		
		function showMySQLInfo(mysqlId){
			var url = "${WEB_APP_PATH}/resource/mysql/info.do?mysqlId="+mysqlId;
			common.dialogIframe( 'MySQL实例',url,500,510);
		}
		
		function importPaasMysql(mysqlId){
			var url = "${WEB_APP_PATH}/resource/mysql/import.do?mysqlId="+mysqlId;
			common.dialogIframe("Execute SQL File",url,200,200);
		}
		
		function renderPaasMysqlStatus(data,mysqlId){
			var tr = $("."+mysqlId).closest("tr");
			tr.find(">td:last").find(".action_zt").remove();
			tr.find(">td:last").find(".action_event").remove();
			if(data.status=='运行中'){
				tr.find("."+mysqlId).empty().html("<span class='text-green2'>"+data.status+"</span>");
				tr.find(">td:last").append('&nbsp;&nbsp;<a class="action_zt" href="javascript:void(0)" class="btn btn-link yellow" onclick="stopPaasMysql(\''+mysqlId+'\')" title="停止运行mysql">停止</a>');
			}else if(data.status=='已停止'){
				tr.find("."+mysqlId).empty().html("<span class='text-yellow zt_span'>"+data.status+"</span>");
				tr.find(">td:last").append('&nbsp;&nbsp;<a class="action_zt" href="javascript:void(0)" class="btn btn-link yellow" onclick="startPaasMysql(\''+mysqlId+'\')" title="启动mysql">启动</a>');
			}else if(data.status=='启动中'){
				tr.find("."+mysqlId).empty().html("<span class='txt-safe'>"+data.status+"</span>");
	 		}else{
				tr.find("."+mysqlId).empty().html(data.status);
			}
			if(data.podName && data.namespace){
				tr.find(">td:last").append('&nbsp;&nbsp;<a class="action_event" href="javascript:void(0)" class="btn btn-link yellow" onclick="showResEvent(\''+data.podName+'\',\''+data.namespace+'\')" title="查看事件">事件</a>');
			}
		}
		
		function startPaasMysql(resId){
			var load = common.loading();
			var url = "${WEB_APP_PATH}/resource/mysql/startMysql.do";
			$.post(url,{"resId":resId},function(json){
				load.close();
				var data ={};
				if(json.code==0){
					data.status= "启动中"
					renderPaasMysqlStatus(data,resId)
				}else{
					common.tips("启动mysql失败");
				}
			});
		}
		
		function stopPaasMysql(resId){
			var load = common.loading();
			var url = "${WEB_APP_PATH}/resource/mysql/stopMysql.do";
			$.post(url,{"resId":resId},function(json){
				load.close();
				var data ={};
				if(json.code==0){
					data.status= "已停止"
					renderPaasMysqlStatus(data,resId)
				}else{
					common.tips("停止mysql失败");
				}
			});
		}
		
	</script>
	</template:replace>
</template:include>
	
