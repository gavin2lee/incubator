<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="baseimage">
	<template:replace name="title">
		基础镜像
	</template:replace>
	<template:replace name="content-body">
	<style>
	
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
	text-overflow: ellipsis;
    overflow: hidden;
}
#tenantsDiv{
	padding-left : 200px;
	width: 780px;
    height: auto;
    min-height: 100px;
}
	</style>
	<link rel="stylesheet" href="../../assets/css/editormd/editormd.preview.css" />
		<div class="app_name">
            <a href="pass_index.html"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="../tenant/list.do">环境管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../baseimage/list.do">基础镜像</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="#nogo">${build.name }</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
            	<form id="build_area">
            	<input type="hidden" name="projectId" value="${build.id}"/>
<%--             	<input type="hidden" name="buildId" value="${build.buildId }"/> --%>
                <div class="r_block p20">
	                    <div class="r_block">
	                        <div class="r_title">
	                            <h3 class="f14 h3_color h3_btm_line"><i class="icons add_ico_blue mr5"></i>${build.name }</h3>
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
		                                    <a class="tab-handler" href="#nogo" index="2">镜像授权</a></h5>
		                            </li>
		                        </ul>
		                        <div class="title_line"></div>
		                    </div>
	                        <div class="r_con p10_0">
	                            <div class="form_control p20">
	                            	<div id="tab-first">
		                            	<div class="form_block">
		                                    <label>镜像图标</label>
		                                    <p id="appLogo" path='${build.iconPath}'>
		                                    <div class="img-container"><img src="${WEB_APP_PATH }/paas/file/view.do?filePath=${build.iconPath}"></div>
		                                    </p>
		                                </div>
		                            	<div class="form_block"  >
		                                    <label>镜像名称</label>
		                                    <span>${build.name}</span>
		                                </div>
		                               
		                                <div class="form_block">
		                                    <label>镜像版本</label>
		                                    <span>${build.version }</span>
		                                </div>
		                                <div class="form_block">
		                                    <label>构建文件</label>
		                                    <span><a href="download.do?id=${build.id }">${build.fileName }</a></span>
		                                </div>
		                                <div class="form_block">
		                                    <label>状态</label>
		                                    <span>
		                                    	<c:choose>
	                                    			<c:when test="${build.status==6}">
	                                    				<i class="pas_ico zt_ico doing_ico "></i>
	                                    				删除中
	                                    			</c:when>
	                                    			<c:when test="${build.status==11}">
	                                    				<i class="pas_ico zt_ico fail_ico "></i>
	                                    				删除失败
	                                    			</c:when>
	                                    			<c:when test="${build.status==1}">
	                                    				<i class="pas_ico zt_ico waiting_ico "></i>
	                                    				未构建
	                                    			</c:when>
	                                    			<c:when test="${build.status==5}">
	                                    				<i class="pas_ico zt_ico doing_ico "></i>
	                                    				构建中
	                                    			</c:when>
	                                    			<c:when test="${build.status==10}">
	                                    				<i class="pas_ico zt_ico done_ico "></i>
	                                    				构建成功
	                                    			</c:when>
	                                    			<c:when test="${build.status==0}">
	                                    				<i class="pas_ico zt_ico fail_ico "></i>
	                                    				构建失败
	                                    			</c:when>
	                                    		</c:choose>
		                                    	&nbsp;
		                                    </span>
		                                </div>
		                                <div class="form_block"  >
		                                    <label>Dockerfile</label>
		                                    <span style="width:80%"><pre style="word-wrap: break-word;">${build.dockerfile}</pre></span>
		                                </div>
		                                <%-- <div class="form_block">
		                                    <label>启动命令</label>
		                                    <span>${build.command } </span>
		                                </div>
		                                <div class="form_block">
		                                    <label>工作目录</label>
		                                    <span>${build.workDir } </span>
		                                </div> --%>
		                                <div class="form_block">
		                                    <label>镜像端口</label>
		                                    <div class="specList">
		                                        <table id="portList" class="table_creat">
		                                            <tbody>
			                                            <tr>
			                                                <th style="width: 120px;">协议</th>
			                                                <th style="width: 120px;">端口号</th>
			                                                <th style="width: 120px;">关键字</th>
			                                                <th style="width: 120px;">描述</th>
			                      						</tr>
		                      						</tbody>
		                                      </table>
		                                    </div>
		                                </div>
		                                <div class="form_block"  >
		                                    <label>构建描述</label>
		                                    <div  id="editormdDiv" style="width:70%">
					                           	<textarea style="display: none;" cols="83" rows="15" name="documentation" required>${build.description}</textarea>
					                       	</div>
		                                </div>
	                                </div>
	                                <div id="tab-second" style="display:none;">
	                                
		                                <div class="form_block">
		                                    <label>授权组织</label>
		                                    <p style="height:30px;" class="tenant">
		                                    	<input type="checkbox" id="toAll" onclick="return false;"/><label for="toAll">任何组织</label>
		                                    </p>
		                                    <div class="tenant" id="tenantsDiv" >
		                                    
		                                    </div>
		                                </div>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
                </div>
                <!--创建构建结束-->
            	</form>
            </div>
        </div>
        
        <script id="imageport" type="text/html">
        	<tr>
			<td>{%=portType%}</td>
			<td>{%=portNo%}</td>
			<td>{%=portKey%}</td>
			<td>{%=portDesc%}</td>
			</tr>
        </script>
        <script src="../../assets/js/editormd/lib/marked.min.js"></script>
        <script src="../../assets/js/editormd/lib/prettify.min.js"></script>
        
        <script src="../../assets/js/editormd/lib/raphael.min.js"></script>
        <script src="../../assets/js/editormd/lib/underscore.min.js"></script>
        <script src="../../assets/js/editormd/lib/sequence-diagram.min.js"></script>
        <script src="../../assets/js/editormd/lib/flowchart.min.js"></script>
        <script src="../../assets/js/editormd/lib/jquery.flowchart.min.js"></script>
        <script src="../../assets/js/editormd/editormd.min.js"></script>
        <script>
        	$(function(){
        		var port = '${build.ports}';
        		if(port==null){
        			port = '[]';
        		}
        		port = eval('('+port+')');
        		for(var i=0;i<port.length;i++){
        			var _h = $('#imageport').html();
        			_h=_h.replace(/\{%=([\s\S]+?)%\}/g,function(){
        				var k = arguments[1];
        				if(port[i][k]==null){
        					return '';
        				}
        				return port[i][k];
        			});
        			$('#portList').children('tbody').append(_h);
        		}
        		var tenantIds = '${build.tenantIds}';
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
                		str += '<input onclick="return false;" type="checkbox" id="'+v.tenantId+'"'+isChecked+'/><label for="'+v.tenantId+'">'+v.tenantName+'</label>';
                	})
                	$("#tenantsDiv").html(str);
               	});
            	
            	var testEditormdView = editormd.markdownToHTML("editormdDiv", {
                    markdown        : $("textarea[name=documentation]").val() ,//+ "\r\n" + $("#append-test").text(),
                    //htmlDecode      : true,       // 开启 HTML 标签解析，为了安全性，默认不开启
                    htmlDecode      : "style,script,iframe",  // you can filter tags decode
                    //toc             : false,
                    tocm            : true,    // Using [TOCM]
                    //tocContainer    : "#custom-toc-container", // 自定义 ToC 容器层
                    //gfm             : false,
                    //tocDropdown     : true,
                    // markdownSourceCode : true, // 是否保留 Markdown 源码，即是否删除保存源码的 Textarea 标签
                    emoji           : true,
                    taskList        : true,
                    tex             : true,  // 默认不解析
                    flowChart       : true,  // 默认不解析
                    sequenceDiagram : true,  // 默认不解析
                });
            	
        	});
        	
        </script>

	</template:replace>
</template:include>