<%@ page language="java" pageEncoding="UTF-8"%>


<div class="plate">
	<div class="project_r">
		<!--创建redis实例开始-->
		<div class="r_block p20">
			<div class="r_title">
				<h3 class="f14 h3_color h3_btm_line blue">
					<a href="javascript:void(0);"> <i
						class="icons ico_title_list mr5"></i>创建redis实例
					</a>
				</h3>

				<div class="title_line"></div>
			</div>
			<div class="r_con p10_0">
				<div class="form_control p20">
					<div class="form_block">
						<label>实例名称</label> <input type="text" name="redisName"  maxlength="30"><em>*</em>
					</div>
					<div class="form_block">
						<label>Redis版本</label>
						<div class="specList">
							<ul class="u-flavor" id="version">
							</ul>
						</div>
					</div>
					<div class="form_block">
						<label>环境类型</label>
						<div class="specList">
							<ul class="u-flavor" id="envType">
								<c:forEach items="${envTypes }" var="item">
									<li data="${item.key}">${item.value}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<div class="form_block">
						<label>内存大小</label>
						<div class="specList">
							<ul class="u-flavor" id="redisMemory">
							</ul>
						</div>
					</div>
					<div class="form_block">
						<label>描述信息</label>
						<div class="specList">
							<textarea rows="6" cols="53" id="resDesc"></textarea>
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
						<button class="btn btn-default btn-yellow f16  mt10"
							onclick="addPaasRedis()">
							<i class="ico_check"></i>创 建
						</button>
					</div>
				</div>
			</div>
		</div>
		<!--创建redis实例结束-->
	</div>
</div>
<script>
		$(function(){
			var options = '${options}';
			if(options){
				try{
					var jsonOptions= JSON.parse(options);
					var memOptions = jsonOptions.memory;
					if(memOptions&& memOptions.length){
						for(var i=0; i<memOptions.length;i++){
							$("#redisMemory").append("<li>"+memOptions[i]+"</li>");
						}
						bindLiEleClickByParent('redisMemory');
					}
					var versionOptions = jsonOptions.version;
					if(versionOptions&& versionOptions.length){
						for(var i=0; i<versionOptions.length;i++){
							$("#version").append("<li>"+versionOptions[i]+"</li>");
						}
						bindLiEleClickByParent('version');
					}
				}catch(e){
					common.tips("获取资源配额失败");
				}
			}
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
		
		function addPaasRedis(){
			var data ={};
			var instanceName = $.trim($("input[name='redisName']").val());
			if(instanceName == ''){
				common.tips("实例名称为空");
				return false;
			}
			data.instanceName = instanceName;
			var version = $(".version").find("option:selected").val();
			data.version = version;
			var mqMemory = $("#redisMemory").find(">li.selected").text();
			data.memory = mqMemory;
			var envType = $("#envType").find(">li.selected").attr("data");
			data.envType = envType;
			data.resDesc = $("#resDesc").val();
			if(mqMemory==''){
				common.tips("没有选择内存配额大小");
				return false;
			}
			var url = "${WEB_APP_PATH}/resource/redis/save.do";
			var load = common.loading();
			$.post(url,data,function(json){
				load.close();
				if(json.code==0){
					common.goto("${WEB_APP_PATH}/resource/redis/index.do");
				}else{
					common.tips("添加redis实例失败\n"+json.msg,null,null,200000);
				}
			});
		}
	</script>