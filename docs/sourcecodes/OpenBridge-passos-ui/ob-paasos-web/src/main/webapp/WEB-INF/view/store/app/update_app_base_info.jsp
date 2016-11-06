<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="presetApp">
	<template:replace name="title">
		预置应用
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
.config-table{
	width:80%;
	font-size : 12px;
}
.config-table tr th:last-child,.config-table tr td:last-child{
	text-align:right;
	width: 40px;
}
em{
	padding: 0 10px;
}
</style>

    <link rel="stylesheet" href="../../assets/css/editormd/editormd.css" />
	
	
        <div class="app_name">
            <a href="list.do"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="list.do">预置应用</a></p>
            <em>&gt;</em>


            <p class="app_a"><a href="#nogo">${empty presetApp.id ? '创建' : '编辑'}预置应用</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
                <div class="r_block p20">
                    <div class="r_block">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line">${empty presetApp.id ? '<i class="icons add_ico_blue mr5"></i>创建' : '编辑'}预置应用</h3>
<!-- 								<h3 class="f14 h3_color h3_btm_line tab-handler red" index="1"><a href="#nogo">基本信息</a></h3> -->
<!--                             	<h3 class="f14 h3_color h3_btm_line tab-handler" index="2"><a href="#nogo">其他信息</a></h3> -->
                            <div class="title_line"></div>
                        </div>
                        <div class="r_con p10_0">
                            <div class="form_control p20">
                            <form id="baseInfoForm" action="updateBaseInfo.do" method="post" enctype="multipart/form-data">
                            	<input type="hidden" name="id"  value="${presetApp.id }"/>
                                <div class="form_block">
                                    <label>应用图标</label>
                                    <p id="appLogo" path='${presetApp.iconPath}'/>
                                </div>
                                <div class="form_block">
                                    <label>应用名称</label>
                                    <input type="text" name="appName" id="appName" value="${presetApp.appName }"><em>*</em>
                                </div>
                                <div class="form_block">
                                    <label>镜像名称</label>
                                    <input disabled type="text" name="name" id="name" value="${presetApp.name }"><em>*</em>
                                </div>
                                <div class="form_block">
                                    <label>应用内容</label>
                                    <div class="specList">
                                        <p class="mb10"  style="display: none;"><span class="w200">
                                        	<input type="radio" name="fileType" value="zip" checked>&nbsp;&nbsp;包含dockerfile的zip文件</span>
                                            <span class="w200"><input type="radio" name="fileType" value="tar.gz">&nbsp;&nbsp;上传已构建好的tar.gz</span>
                                        </p>
                                   		<p style="height:25px;" id="buildFileP" class="file-container"></p>
                                    </div>
                                </div>
                                <div class="form_block">
                                    <label>应用版本</label>
                                    <!--<input class="w100" type="text" value="yihecloud/tomcat" disabled readonly="readonly">-->
                                    <input disabled type="text" name="version" id="version" value="${presetApp.version }" required><em>*</em>
                                </div>
                                <div class="form_block">
                                    <label>Dockerfile</label>
                                    <textarea cols="83" rows="15" name="dockerfile" required>${presetApp.dockerfile }</textarea><em style="">*</em>
                                </div>
                                <div class="form_block">
                                    <label>应用描述</label>
                                    <textarea cols="83" rows="5" name="description">${presetApp.description }</textarea>
                                </div>
                                <div class="form_block">
                                    <label>详细说明</label>
                                    <%-- <span name="documentation" id="preview" required class="g-b g-b--t1of2 split split-preview" style="width:80% ">${presetApp.documentation }</span><em style="">*</em> --%>
                                    <div  id="editormdDiv" style="display:inline-block;">
                                    	<textarea cols="83" rows="15" name="documentation" required>${presetApp.documentation }</textarea>
                                	</div><em style="">*</em>
                                </div>
                                <div class="form_block mt10">
                                	<!-- <button id="submitBtn" type="button" class="btn btn-default btn-yellow f16  mt10" onclick="openMarkDownEdit()"><i class="ico_check"></i>markdown编辑说明</button> -->
                                    <label>&nbsp;</label>
                                    <button id="submitBtn" type="button" class="btn btn-default btn-yellow f16  mt10" onclick="saveOrUpdate()"><i class="ico_check"></i>保存</button>
                                </div>
                            </form>
                            </div>
                        </div>
                    </div>
                <!--创建应用结束-->
            </div>
        </div>
        <script src="../../assets/js/editormd/editormd.min.js"></script>
        <style>
        .editormd {
         	width: 70%;
        }
        </style>
        <%@ include file="/common/include/fileupload_header.jsp"%>
        <%@ include file="/common/include/validation_header.jsp"%>
        <script>
        //提交
        function saveOrUpdate(){
        	//校验
        	if(!$("#baseInfoForm").valid())	return false;
        	var param = $("#baseInfoForm").serializeArray();
        	var documentation = {};
        	documentation.value = $("textarea[name=documentation]").html();
        	documentation.name= "documentation";
        	param.push(documentation);
        	var data = {};
        	$.each(param,function(i,v){
        		data[v.name] = v.value
        	})
        	//获取图片信息
        	data.iconPath = $("#appLogo").data("data").filePath;
        	
        	//获取上传文件的路径
        	var file = $("#buildFileP").data("data");
        	if(!file){
        		common.alert("请上传文件！",2);
        		return false;
        	}else{
	        	data.filePath = file.filePath;
        	}
        	data.fileData = JSON.stringify(file);
//         	$("#baseInfoForm").submit();
			$("#submitBtn").hide();
			var loadObj = common.loading();
			$.post($("#baseInfoForm").attr("action"),data,function(json){
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
       
        /* function openMarkDownEdit(){
        	var dialog = common.dialogIframe("MarkDown","${WEB_APP_PATH}/html/editormd.html",$(window).width() - 100,$(window).height() - 50,null,['完成','取消'],{
				btn1 : function(index,layero){
					var description = $(layero.find("iframe").get(0)).contents().find("#preview").html();
					$("span[name=documentation]").html(description);
				},
				btn2 : function(){}
			});
        } */
        
        $(function(){
        	$("#baseInfoForm").validate({
        		rules : {
        			dockerfile : {
        				notBlank : true
        			}
        		}
        	});
        	
//        	initImage($("#appLogo"),$("#appLogo").attr("path"));
			var fileData = $.parseJSON('${presetApp.fileData}');
  			initFileUpload(fileData,'${param.id}');      	
        	var testEditor = editormd("editormdDiv", {
                width   : "80%",
                height  : 700,
                path    : "../../assets/js/editormd/lib/",
                watch :true,
                toolbarIcons : function() {
                	return ["undo", "redo", "|", 
                			"bold", "del", "italic", "quote", "|", 
                       		"h1", "h2", "h3", "h4", "h5", "h6", "|",
                       		"list-ul", "list-ol", "hr","code-block", "|","watch",
                       		"link", "table",  "|",  "watch", "preview", "fullscreen", "clear"]
                }
            });
        })
        </script>
	</template:replace>
</template:include>