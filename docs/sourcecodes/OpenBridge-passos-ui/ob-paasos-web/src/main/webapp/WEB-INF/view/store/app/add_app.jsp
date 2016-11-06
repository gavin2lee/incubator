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
                        <div class="title_tab title_tab_item title_tab_item_pas text-center">
	                        <ul>
	                            <li>
	                                <h5 class="f14">
	                                    <a class="active tab-handler" index="1" href="#nogo">基本信息</a>
	                                </h5>
	                            </li>
	                            <li>
	                                <h5 class="f14">
	                                    <a class="tab-handler" href="#nogo" index="2" id="tab2">其他信息</a></h5>
	                            </li>
	                        </ul>
	                        <div class="title_line"></div>
	                    </div>
                        <div class="r_con p10_0">
                            <div class="form_control p20">
                            <form id="createForm" action="saveOrUpdate.do" method="post" enctype="multipart/form-data">
                            	<input type="hidden" name="id"  value="${presetApp.id }"/>
                            	<div id="tab-first">
	                                <div class="form_block">
	                                    <label>应用图标</label>
	                                    <p id="appLogo" path='${presetApp.iconPath}'/>
	                                </div>
	                                <div class="form_block">
	                                    <label>应用名称</label>
	                                    <input type="text" name="appName" id="appName" value="${presetApp.appName }" maxlength="30"><em>*</em>
	                                </div>
	                                <div class="form_block">
	                                    <label>镜像名称</label>
	                                    <input type="text" name="name" id="name" value="${presetApp.name }" maxlength="30"><em>*</em>
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
	                                    <input  type="text" name="version" id="version" value="${presetApp.version }" required maxlength="20"><em>*</em>
	                                </div>
	                                <div class="form_block">
	                                    <label>Dockerfile</label>
	                                    <textarea cols="93" rows="15" name="dockerfile" required>${presetApp.dockerfile }</textarea><em style="">*</em>
	                                </div>
	                                <div class="form_block">
	                                    <label>启动命令</label>
	                                    <input type="text" name="command" value="${presetApp.command }" maxlength="128">
	                                </div>
	                                <div class="form_block">
	                                    <label>工作目录</label>
	                                    <input type="text" name="workDir" value="${presetApp.workDir }" maxlength="200">
	                                </div>
	                                <div class="form_block">
	                                    <label>应用描述</label>
	                                    <textarea cols="93" rows="5" name="description" maxlength="450">${presetApp.description }</textarea>
	                                </div>
	                                <div class="form_block">
	                                    <label>详细说明</label>
	                                    <div id="editormdDiv" style="display:inline-block;">
	                                    	<textarea cols="93" rows="15" name="documentation" required>${presetApp.documentation }</textarea>
	                                	</div>
	                                	<em style="">*</em>
	                                </div>
	                                <div class="form_block mt10">
	                                    <label>&nbsp;</label>
	                                    <button id="nextBtn" type="button" class="btn btn-default btn-yellow f16  mt10" onclick="$('#tab2').click();"><i class="ico_check"></i>下一步</button>
	                                </div>
	                            </div>
	                            <div id="tab-second" style="display:none;">
	                                <div class="form_block tab-second">
	                                    <label>应用端口</label>
	                                    <div class="specList">
	                                        <table class="table_creat config-table" id="ports">
	                                            <tr>
	                                                <th>类型</th>
	                                                <th>端口号</th>
	                                                <th>关键字</th>
	                                                <th>说明</th>
	                                                <th><a class="btn btn-default btn-yellow btn-sm btn-add" href="javascript:void(0)" title="添加"><em class="f14">+</em></a> </th>
	                                            </tr>
	                                        </table>
	                                    </div>
	                                </div>
	                                <div class="form_block tab-second">
	                                    <label>依赖资源</label>
	                                    <div class="specList">
	                                        <table class="table_creat config-table" id="dependencyResources">
	                                            <tr>
	                                                <th>序号</th>
	                                                <th>类型</th>
	                                                <th>说明</th>
	                                                <th>初始化动作</th>
	                                                <th><a class="btn btn-default btn-yellow btn-sm btn-add-res" href="javascript:void(0)" title="添加"><em class="f14">+</em></a> </th>
	                                            </tr>
	                                        </table>
	                                    </div>
	                                </div>
	                                <div class="form_block mt10">
	                                    <label>&nbsp;</label>
	                                    <button id="submitBtn" type="button" class="btn btn-default btn-yellow f16  mt10" onclick="saveOrUpdate()"><i class="ico_check"></i>保存</button>
	                                </div>
	                            </div>
                            </form>
                            </div>
                        </div>
                    </div>
                <!--创建应用结束-->
            </div>
        </div>
        <%@ include file="/common/include/fileupload_header.jsp"%>
        <%@ include file="/common/include/validation_header.jsp"%>
        <script src="../../assets/js/editormd/editormd.min.js"></script>
        <script>
        //提交
        function saveOrUpdate(){
        	//校验
        	if(!$("#createForm").valid())	return false;
        	var param = $("#createForm").serializeArray();
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
        	//获取资源信息
        	var dependency = [];
        	$("#dependencyResources tr:gt(0)").each(function(){
        		var res = {
            			number : $(this).find("td:first").html(),
            			type : $(this).find("select").val(),
            			desc : $(this).find("input").val(),
            			initAction : {}
            		};
        		if(res.type=='db'){
        			var file =　$(this).find("td:eq(3)").data("data");
        			if(file)	res.initAction.file = file;
        		}
        		dependency.push(res);
        	})
        	data.config = JSON.stringify(dependency);
        	$("#createForm").append("<input type='hidden' name='config' value='"+data.dependency+"'/>");
        	
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
       
        //增加资源
        function addDependencyResource(dependency){
        	dependency = $.extend(true,{
				number : $("#dependencyResources tr").length,
				type : '',
				desc : '',
				initAction : {}
			},dependency);
        	$("#dependencyResources").append('<tr>										   '+
    				'	<td>'+dependency.number+'</td>				      '+
    				'	<td><select class="u-ipt res-type-sel"><option value="db">数据库</option><option value="mq">消息中间件</option>'+
    				'	<option value="cache">高速缓存</option><option value="nas">NAS存储</option></select></td>				      '+
    				'	<td><input type="text" class="u-ipt2" value="'+dependency.desc+'"/></td>				     '+
    				'	<td class="file-container"></td>'+
    				'	<td><a class="btn btn-default btn-yellow btn-close btn-sm btn-del" href="javascript:void(0);">    '+
    				'	<em class="f14"> × </em></a> </td>					      '+
    				'</tr>										  ');
        	$("#dependencyResources tr:last select").val(dependency.type);
        	var initActionHtml  = '<span>无</span>';
        	$("#dependencyResources tr:last").data("data",dependency);
        	resInit($("#dependencyResources tr:last td:eq(3)"),dependency.initAction.file);
        }
        
        //展示资源
        function resInit(tar,file){
        		if(tar.parent().find("td:eq(1) select").val()=='db'){
        			initFile(tar,file,['.sql']);
        		}else{
        			tar.html("<span>无</span>");
        		}
        }
        //页面内容初始化
        function init(){
        	//应用内容初始化
        	var fileData = $.parseJSON('${presetApp.fileData}');
        	//内容 文件类型初始化
        	var fileType = '${presetApp.fileType}';
        	if(fileType && fileType=='tar.gz'){
        		$(".specList :radio").eq(1).attr("checked","checked");
        	}
        	//端口初始化
        	var ports = '${presetApp.ports}';
        	if(ports){
        		$.each($.parseJSON(ports),function(i,v){
        			addPort(v);
        		})
        	}
        	//资源初始化 
        	var config = '${presetApp.config}';
        	if(config){
        		$.each($.parseJSON(config),function(i,v){
        			addDependencyResource(v);
        		})
        	}
//         	initFile($("#buildFileP"),fileData);
//         	if(!'${param.id}')	$("#buildFileP a.edit").click();
//         	initImage($("#appLogo"),$("#appLogo").attr("path"));
        	initFileUpload(fileData,'${param.id}');
        }
        $(function(){
        	$("#createForm").validate({
        		rules : {
        			name : {
        				notBlank : true,
        				byteRangeLength : [1,30],
        				lettersAndNumber :　true
        			},
        			appName : {
        				notBlank : true,
        				byteRangeLength : [1,30]
        			},
        			dockerfile:{
        				notBlank : true
        			},
//         			upfile : {
//         				accept : ".zip,.tar.gz"
//         				required : true
//         			},
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
        	
        	//资源类型改变处理
        	$("#dependencyResources .res-type-sel").live('change',function(){
        		var tar = $(this).parent().parent().find("td:eq(3)");
        		resInit(tar,tar.data("data"));
        	});
        	
        	init();
        	
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
                       		"link", "table",  "|", "watch",  "preview", "fullscreen", "clear"]
                }
            });
       	 	
        })
        </script>
	</template:replace>
</template:include>