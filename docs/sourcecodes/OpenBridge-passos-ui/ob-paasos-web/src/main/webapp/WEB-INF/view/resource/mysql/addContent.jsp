<%@ page language="java" pageEncoding="UTF-8"%>

<div class="plate">
          <div class="project_r">
              <!--创建数据库实例开始-->
              <div class="r_block p20">
                  <div class="r_title">
                      <h3 class="f14 h3_color h3_btm_line blue">
                          <a href="javascript:void(0);">
                              <i class="icons ico_title_list mr5"></i>创建数据库实例
                          </a>
                      </h3>

                      <div class="title_line"></div>
                  </div>
                  <div class="r_con p10_0">
                      <div class="form_control p20">
                          <div class="form_block">
                              <label>实例名称</label>
                              <input type="text" name="instanceName" maxlength="30"><em>*</em>
                          </div>
                          <div class="form_block">
                              <label>MySQL版本</label>
                              <div class="specList">
                               <ul class="u-flavor" id="version">
                               </ul>
                           </div>
                          </div>
                          <div class="form_block">
                              <label>环境类别</label>
                              <div class="specList">
                               <ul class="u-flavor" id="envType">
                               	<c:forEach items="${envTypes }" var="item">
                                	<li data="${item.key}">${item.value}</li>	                                
                                </c:forEach>
                               </ul>
                           </div>
                          </div>
                          <div class="form_block">
                              <label>服务模式</label>
                              <div class="specList">
                                  <ul class="u-flavor" id="mysqlType">
                                      <!--Regular list-->
                                      <li class="selected" data="mysql">单机</li>
<!--                                         <li class="" data="mysqlShare">共享</li> -->
                                      <li class="" data="mysqlCluster">集群</li>
                                  </ul>
                                  <div class="jq_set" style="display:none;" id="mysqlTypeAttach">
                                      <span class="mr5"><input class="w100" name="mysqlTypeAttachMain" type="text" value="1" readonly="readonly"> 主</span>
                                      <span class="mr10"><input class="w100" name="mysqlTypeAttachBackup" type="text" value="1" placeHolder="1或 2"> 从</span>
                                  </div>
                              </div>
                          </div>
                          <div class="form_block">
                              <label>内存大小</label>
                              <div class="specList">
                                  <ul class="u-flavor" id="mysqlMemory">
                                  </ul>
                              </div>
                          </div>
                          <div class="form_block">
                              <label>存储空间</label>
                              <div class="specList">
                                  <ul class="u-flavor" id="mysqlStorage">
                                  </ul>
                              </div>
                          </div>
<!--                           <div class="form_block"> -->
<!--                               <label>备份方式</label> -->
<!--                               <div class="specList"> -->
<!--                                   <ul class="u-flavor" id="mysqlBackup"> -->
<!--                                       <li class="selected" data="false">不启用备份</li> -->
<!--                                       <li class="" data="true">启用备份</li> -->
<!--                                   </ul> -->
<!--                                   <div class="jq_set" style="display:none;" id="mysqlBackupDetail"> -->
<!--                                       <span class="mr5">周<input class="w150" name="mysqlBackupDay" type="text" value="" placeHolder="0:周日，1-6:周一到周六"></span> -->
<!--                                       <span class="mr10"><input class="w150" name="mysqlBackupTime" type="text" value="" placeHolder="17:00是下午5点，24小时制格式">点</span> -->
<!--                                       <span style="color:red;">周可以是0-6的组合，如024代码周日，周二，周四都进行备份</span> -->
<!--                                   </div> -->
<!--                               </div> -->
<!--                           </div> -->
                          <div class="form_block">
                              <label>描述信息</label>
                              <div class="specList">
                              <textarea rows="6" cols="53" id="resDesc" maxlength="200"></textarea>
                              </div>
                          </div>
                          <div class="form_block">
                              <label>租户组织</label>
                              <div class="specList">
                              	<span>${tenantName}</span>
                              </div>
                          </div>
                          <div class="form_block">
                              <label>创建者</label>
                              <div class="specList">
                              	<span>${userName}</span>
                              </div>
                          </div>
                          <div class="form_block mt10">
                              <label>&nbsp;</label>
                              <button class="btn btn-default btn-yellow f16  mt10" onclick="addPaasMysql()"><i class="ico_check"></i>创 建
                              </button>
                          </div>
                      </div>
                  </div>
              </div>
              <!--创建数据库实例结束-->
          </div>
      </div>	
      <script>
	
	$(function(){
		var options='${options}';
		if(options){
			try{
				var jsonOptions = JSON.parse(options);
				var memOptions = jsonOptions.memory;
				var storageOptions = jsonOptions.storage;
				if(memOptions&& memOptions.length){
					for(var i=0; i<memOptions.length;i++){
						$("#mysqlMemory").append("<li>"+memOptions[i]+"</li>");
					}
					bindLiEleClickByParent('mysqlMemory');
				}
				if(storageOptions&& storageOptions.length){
					for(var i=0; i<storageOptions.length;i++){
						$("#mysqlStorage").append("<li>"+storageOptions[i]+"</li>");
					}
					bindLiEleClickByParent('mysqlStorage');
				}
				var versionOptions = jsonOptions.version;
				if(versionOptions && versionOptions.length){
					for(var i=0; i<versionOptions.length;i++){
						$("#version").append("<li>"+versionOptions[i]+"</li>");
					}
					bindLiEleClickByParent('version');
				}
			}catch(e){
				common.tips("获取资源配额失败");
			}
		}
		$("#mysqlType").find(">li").bind("click",function(){
			var ob = $(this);
			$("#mysqlType").find(">li").each(function(){$(this).removeAttr("class");})
			ob.attr("class","selected");
			if(ob.text()=='集群'){
				$("#mysqlTypeAttach").removeAttr("style");
			}else{
				$("#mysqlTypeAttach").css("display","none");
			}
		});
// 		$("#mysqlBackup").find(">li").bind("click",function(){
// 			var ob = $(this);
// 			$("#mysqlBackup").find(">li").each(function(){$(this).removeAttr("class");})
// 			ob.attr("class","selected");
// 			if(ob.attr("data")=='true'){
// 				$("#mysqlBackupDetail").removeAttr("style");
// 			}else{
// 				$("#mysqlBackupDetail").css("display","none");
// 			}
// 		});
		bindLiEleClickByParent('envType');
	});
	
	function bindLiEleClickByParent(parentId){
		var parentEle= $("#"+parentId);
		parentEle.find(">li").bind("click",function(){
			var ob = $(this);
			parentEle.find(">li").each(function(){	$(this).removeAttr("class");})
			ob.attr("class","selected");
		});
		parentEle.find(">li:first").attr("class","selected");
	}
	
	function addPaasMysql(){
		var data ={};
		var instanceName = $.trim($("input[name='instanceName']").val());
		if(instanceName == ''){
			common.tips("实例名称为空");
			return false;
		}
		data.instanceName = instanceName;
		var version = $("#version").find(">li.selected").text();
		data.version = version;
		var mysqlType = $("#mysqlType").find(">li.selected").attr("data");
		data.type = mysqlType;
		var mysqlTypeAttach={};
		if(mysqlType=='mysqlCluster'){
			mysqlTypeAttach.main = $("input[name='mysqlTypeAttachMain']").val();
			mysqlTypeAttach.back = $("inpuat[name='mysqlTypeAttachBackup']").val();
		}
		data.TypeAttach = mysqlTypeAttach;
// 		var mysqlBack = $("#mysqlBackup").find(">li.selected").attr("data");
// 		var backupPolicy = {};
// 		if(mysqlBack=='false'){
// 			backupPolicy.policy="false";
// 		}else{
// 			var backupDay = $("#mysqlBackupDetail").find("input[name='mysqlBackupDay']").val();
// 			var filterDay = /[0-6]{1,*}/;
// 			if(!filterDay.test(backupDay)){
// 				common.tips("备份时间设置有误");
// 				return false;
// 			}
// 			backupPolicy.policy=backupDay;
// 			var backupTime= $("#mysqlBackupDetail").find("input[name='mysqlBackupTime']").val();
// 			var filterTime = /^(([0-1]\d)|(2[0-4])):[0-5]\d$/;
// 			if(!filterTime.test(backupTime)){
// 				common.tips("备份时间设置有误");
// 				return false;
// 			}
// 			backupPolicy.time=backupTime;
// 		}
// 		data.backup= backupPolicy;
		var mysqlMemory = $("#mysqlMemory").find(">li.selected").text();
		data.memory = mysqlMemory;
		var mysqlStorage = $("#mysqlStorage").find(">li.selected").text();
		data.storage = mysqlStorage;
		var envType = $("#envType").find(">li.selected").attr("data");
		data.envType = envType;
		data.resDesc = $("#resDesc").val();
		if(mysqlType=='mysql'||mysqlType=='mysqlCluster'){
			if(mysqlMemory==''|| mysqlStorage==''){
				common.tips("没有选择内存和存储空间的配额大小");
				return false;
			}
		}
		var url = "${WEB_APP_PATH}/resource/mysql/save.do";
		var load = common.loading();
		$.post(url,data,function(json){
			load.close();
			if(json.code==0){
				common.goto("${WEB_APP_PATH}/resource/mysql/index.do");
			}else{
				common.tips("添加数据库实例失败\n"+json.msg,null,null,200000);
			}
		});
	}
</script>	
