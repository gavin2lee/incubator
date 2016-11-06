<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="baseimage">
	<template:replace name="title">
		基础镜像
	</template:replace>
	<template:replace name="content-body">
	<style>
.mb10{
	position: relative;
}
.mb10 .file-selector{
    position: absolute;
    left: 0;
    width: 75px;
    padding: 5px 12px;
}
#ports tr th:last-child,#ports tr td:last-child{
	text-align:right;
	width: 40px;
}
#tenantsDiv input,#toAll{
	float:left;
	margin-top:6px;
}
.tenant label{
	padding-left:10px!important;
}
#tenantsDiv{
	padding-left : 200px;
}
em{
	padding: 0 10px;
}
.tab-handler{
	cursor : pointer;
} 
.form_block textarea{
	width: 588px;
	height: 150px;
}
</style>
		<link rel="stylesheet" href="../../assets/css/editormd/editormd.css" />
        <div class="app_name">
            <a href="proiect_list.html"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="../tenant/list.do">环境管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../baseimage/list.do">基础镜像</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="#nogo">${empty build.id ? '创建' : '编辑'}镜像</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
                <div class="r_block p20">
                    <div class="r_block">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line">${empty build.id ? '<i class="icons add_ico_blue mr5"></i>创建' : '编辑'}镜像</h3>
<!--                             <h3 class="f14 h3_color h3_btm_line tab-handler red" index="1"><a href="#nogo">基本信息</a></h3> -->
<!--                             <h3 class="f14 h3_color h3_btm_line tab-handler" index="2"><a href="#nogo">其他信息</a></h3> -->
                            <div class="title_line"></div>
                        </div>
                        <div class="title_tab title_tab_item title_tab_item_pas text-center">
	                        <ul>
	                            <li>
	                                <h5 class="f14">
	                                    <a class="active tab-handler" index="1" href="#nogo">镜像信息</a>
	                                </h5>
	                            </li>
	                            <li>
	                                <h5 class="f14">
	                                    <a class="tab-handler" href="#nogo" index="2" id="tab2">镜像授权</a></h5>
	                            </li>
	                        </ul>
	                        <div class="title_line"></div>
	                    </div>
                        <div class="r_con p10_0">
                            <div class="form_control p20" style="padding-top: 0px;">
                            <form id="createForm" action="saveOrUpdate.do" method="post" enctype="multipart/form-data">
                            	<input type="hidden" name="id"  value="${build.id }"/>
                            	<div id="tab-first">
	                            	<div class="form_block">
	                                    <label>镜像图标</label>
	                                    <p id="appLogo" path='${build.iconPath}'/>
	                                </div>
	                                <div class="form_block">
	                                    <label>镜像名称</label>
	                                    <input type="text" name="name" id ="name" value="${build.name }"><em style="color: red;">*</em>
	                                </div>
	                                <div class="form_block">
	                                    <label>镜像版本</label>
	                                    <input  type="text" name="version" id="version" value="${build.version }"><em style="color: red;">*</em>
	                                </div>
	                                <input type="hidden" name="filePath" value="${build.filePath }"/>
	                                <div class="form_block" style="padding-bottom: 20px;">
	                                    <label>镜像内容</label>
	                                    <div class="specList">
	                                        <p class="mb10" style="display: none;"><span class="w200"><input type="radio" name="fileType" value="zip" checked>&nbsp;&nbsp;包含dockerfile的zip文件</span>
	                                            <span class="w200"><input type="radio" value="tar.gz" name="fileType">&nbsp;&nbsp;上传已构建好的tar.gz</span></p>
	                                    	<p style="height:25px;" id="buildFileP" class="file-container"></p>
	                                    </div>
	                                </div>
	                                <div class="form_block">
	                                    <label>Dockerfile</label>
	                                    <textarea cols="53" rows="5" name="dockerfile" required>${build.dockerfile }</textarea><em style="">*</em>
	                                </div>
	                                <%-- <div class="form_block">
	                                    <label>启动命令</label>
	                                    <input type="text" name="command" value="${build.command }">
	                                </div>
	                                <div class="form_block">
	                                    <label>工作目录</label>
	                                    <input type="text" name="workDir" value="${build.workDir }">
	                                </div> --%>
	                                <div class="form_block">
	                                    <label>镜像端口</label>
	                                    <div class="specList">
	                                        <table class="table_creat" id="ports" style="width:600px;">
	                                            <tr>
	                                                <th style="width: 100px;">协议</th>
	                                                <th style="width: 100px;">端口</th>
	                                                <th style=" ">关键字</th>
	                                                <th style=" ">说明</th>
	                                                <th><a class="btn btn-default btn-yellow btn-sm btn-add" href="javascript:void(0)" title="添加"><em class="f14">+</em></a> </th>
	                                            </tr>
	                                        </table>
	                                    </div>
	                                </div>
	                                <div class="form_block">
	                                    <label>镜像描述</label>
	                                    <div  id="editormdDiv" style="display:inline-block;">
	                                    	<textarea name="description">${build.description }</textarea>
	                                    </div>
	                                </div>
	                                <div class="form_block mt10">
	                                    <label>&nbsp;</label>
	                                    <button type="button" id="nextBtn" class="btn btn-default btn-yellow f16  mt10" onclick="toNext()"><i class="ico_check"></i>下一步</button>
	                                </div>
	                            </div>
	                            <div id="tab-second" style="display:none;">
	                                
	                                <div class="form_block">
	                                    <label>授权组织</label>
	                                    <p style="height:30px;" class="tenant">
	                                    	<input type="checkbox" id="toAll"/><label for="toAll">任何组织</label>
	                                    </p>
	                                    <div class="tenant" id="tenantsDiv" >
	                                    
	                                    </div>
	                                </div>
	                                <div class="form_block mt10">
	                                    <label>&nbsp;</label>
	                                    <button type="button" id="submitBtn" class="btn btn-default btn-yellow f16  mt10" onclick="saveOrUpdate()"><i class="ico_check"></i>保存</button>
	                                </div>
                                </div>
                            </form>
                            </div>
                        </div>
                    </div>
                <!--创建镜像结束-->
            </div>
        </div>
        <%@ include file="/common/include/fileupload_header.jsp"%>
        <%@ include file="/common/include/validation_header.jsp"%>
        <script src="../../assets/js/editormd/editormd.min.js"></script>
        <script>
        function saveOrUpdate(){
        	//校验
        	if(!$("#createForm").valid())	return false;
        	var param = $("#createForm").serializeArray();
        	var data = {};
        	$.each(param,function(i,v){
        		data[v.name] = v.value
        	})
        	var patrn=/^[A-Za-z0-9]*$/g; 
        	var code=$.trim($("#name").val());
        	if(!code){
        	 common.alert("镜像名称不能为空！",2);
       		 return false;
       		  }
        	 if (!patrn.exec(code)){
        		 common.alert("镜像名称只能包含英文字母和数字！",2);
        		 return false;
        		  }
        	if(!$.trim($("#version").val())){
        		common.alert("镜像版本不能为空！",2);
        		return false;
        	}
        	//获取图片信息
        	data.iconPath = $("#appLogo").data("data").filePath;
        	//获取上传文件的路径
        	var file = $("#buildFileP").data("data");
        	if(!file){
//         		common.alert("请上传镜像文件！",2);
//         		return false;
        	}else{
	        	data.filePath = file.filePath;
        	}
        	data.fileData = JSON.stringify(file);
        	//端口信息
        	//获取端口信息
        	try{
//        		data.ports = getPorts();
				var ports = getPorts();
        		for(var i = 0;i<ports.length ;i++){
        			var portKey = ports[i]["portKey"];
        			var regex = new RegExp("^[a-z]+$");
        			if(!regex.test(portKey)){
        				common.alert("应用端口关键字格式不正确,只能输入小写字母");
        				return false;
        			}
        		}
        		data.ports = JSON.stringify(ports);
        	}catch(e){
        		return false;
        	}
        	$("#createForm").append("<input type='hidden' name='ports' value='"+data.ports+"'/>");
        	//授权信息
        	var tenantIds = '';
        	if($("#toAll").attr("checked")){
        		tenantIds = $("#toAll").attr("id");
        	}else{
        		var foo = [];
        		$("#tenantsDiv input[type='checkbox']:checked").each(function(){
        			foo.push($(this).attr("id"));
        		})
        		tenantIds = foo.join(",");
        	}
        	data.tenantIds = tenantIds;
        	$("#createForm").append("<input type='hidden' name='tenantIds' value='"+tenantIds+"'/>");
//         	$("#createForm").submit();
			$("#submitBtn").hide();
			var loadObj = common.loading();
			$.post($("#createForm").attr("action"),data,function(json){
				loadObj.close();
				if(json.code==0){
					common.tips("保存成功！",1,function(){
						if('${param.id}'){
							location.href = "list.do";
						}else{
							location.href = "list.do";
						}
					},1000);
   	            }else{
   	            	common.alert("保存失败！"+(json.msg||''),2);
   	            	$("#submitBtn").show();
   	            }
			})
        	return false;
        }
        
        __tenants=[];
        
        function toNext(){
        	var path = $("#appLogo").data("data").filePath;
        	if(path && (path.lastIndexOf(".png") == -1 && path.lastIndexOf(".jpg") == -1 && path.lastIndexOf(".jpeg") == -1 && path.lastIndexOf(".gif") == -1 )){
        		common.alert("图片格式异常");
        		return;
        	}
       		$('#tab2').click();
        }
       	
        $(function(){
        	$("#createForm").validate({
        		rules : {
        			name : {
        				notBlank : true,
        				byteRangeLength : [1,30],
        				lettersAndNumber :　true
        			},
        			dockerfile:{
        				notBlank : true
        			},
        			version : {
        				notBlank : true,
        				isIdentifyName :　true,
        				remote : {
        					url : "checkNameAndVersionExist.do",
        					data :  {
    							name : function(){
            						return $("#name").val();
            					},
        						version : function(){
            						return $("#version").val();
            					},
            					id : function(){
            						return '${param.id}'
            					}
    						}
        				}
        			}
        		},
        		messages : {
        			version :  {
        				remote :　"此名称的此版本已被使用"
        			}
        		}
        	});
        	var fileType = '${build.fileType}';
        	if(fileType && fileType=='tar.gz'){
        		$(".specList :radio").eq(1).attr("checked","checked");
        	}
        	
        	var fileData = $.parseJSON('${build.fileData}');
//         	initFile($("#buildFileP"),fileData,null,"上传文件只支持ZIP格式，并且ZIP里面根目录必须有一个Dockerfile文件",true);
//         	if(!'${param.id}')	$("#buildFileP a.edit").click();
//         	initImage($("#appLogo"),$("#appLogo").attr("path"));
        	initFileUpload(fileData,'${param.id}');
        	
        	var ports = '${build.ports}';
        	if(ports){
        		$.each($.parseJSON(ports),function(i,v){
        			addPort(v);
        		})
        	}
        	var tenantIds = '${build.tenantIds}';
        	$("#toAll").click(function(){
        		if($(this).attr("checked")){
        			$("#tenantsDiv").hide();
        		}else{
        			$("#tenantsDiv").show();
        		}
        	});
        	if('${param.id}'=='' || tenantIds==$("#toAll").attr("id")){
        		$("#toAll").attr("checked",true);
       			$("#tenantsDiv").hide();
        	}
        	$.get(WEB_APP_PATH+"/manager/tenant/getTenants.do",{},function(data){
           		__tenants = data;
           		var bar = tenantIds.split(",");
           		var str='';
            	$.each(__tenants,function(i,v){
            		var isChecked = '';
            		if($.inArray(v.tenantId,bar)!=-1){
            			isChecked = ' checked="checked" ';
            		}
            		str += '<input type="checkbox" id="'+v.tenantId+'"'+isChecked+'/><label for="'+v.tenantId+'">'+v.tenantName+'</label>';
            	})
            	$("#tenantsDiv").html(str);
           	});
        	
        	var testEditor = editormd("editormdDiv", {
                width   : "80%",
                height  : 700,
                path    : "../../assets/js/editormd/lib/",
                watch :true,
                toolbarIcons : function() {
                	return ["undo", "redo", "|", 
                			"bold", "del", "italic", "quote", "|", 
                       		"h1", "h2", "h3", "h4", "h5", "h6", "|",
                       		"list-ul", "list-ol", "hr","code-block", "|",
                       		"link", "table",  "|", "watch", "preview", "fullscreen", "clear"]
                }
            });
        	
        })
        </script>
	</template:replace>
</template:include>