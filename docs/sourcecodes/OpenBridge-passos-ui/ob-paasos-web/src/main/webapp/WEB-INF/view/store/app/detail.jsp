<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp" nav-header="store">
	<template:replace name="title">
		预置应用
	</template:replace>
	<template:replace name="content">
	<script type="text/javascript" src="${WEB_APP_PATH }/assets/js/image/build.js"></script>
    <link rel="stylesheet" href="../../assets/css/editormd/editormd.preview.css" />
	<style>
	.table_creat tr th {
		line-height:20px;
	}
	.table_creat tr td{
		padding: 5px 10px;
	}
	</style>
		<div class="app_name">
            <a href="${WEB_APP_PATH }"><i class="icons go_back_ico"></i></a>
            <p class="app_a"><a href="list.do">预置应用</a></p>
            <em>&gt;</em>
            <p class="app_a"><a href="#nogo">${presetApp.appName }</a></p>
        </div>
        <div class="plate">
            <div class="r_con hj_block_pas">
                <div class="details_nr details_nr2">
                    <div class="d_nr_left">
                        <dl>
                            <dt><img src="${WEB_APP_PATH}/paas/file/view.do?filePath=${presetApp.iconPath}" width="80" height="80"></dt>
                        </dl>
                    </div>
                    <div class="d_nr_right">
                        <div class="app_details">
                            <div class="choose_version pull-right" style="margin-top:0px;top:15%;">
                                <label>下载地址：</label>
                                <span class="no_copy" id="pullScript">docker pull ${dockerRegistry}/store/${presetApp.name }:${presetApp.version }</span>
                                <a class="btn btn-default btn-yellow2 btn-sm f12" data-clipboard-text="docker pull store/${presetApp.name }:${presetApp.version }" href="javascript:void(0);" id="copy-button">复制</a>
                            </div>
                            <div class="app_active_num" style=" margin-right: 470px;">
                            	<p>
                            		<b>${presetApp.appName }</b>
									<span style="line-height: 30px;padding: 0 15px;font-weight:normal;"><b>状态：</b>
									<c:choose>
										<c:when test="${presetApp.status == 0}">构建失败</c:when>
										<c:when test="${presetApp.status == 1}">未构建</c:when>
										<c:when test="${presetApp.status == 5}">构建中</c:when>
										<c:when test="${presetApp.status == 6}">删除中</c:when>
										<c:when test="${presetApp.status == 10}">可部署</c:when>
										<c:when test="${presetApp.status == 11}">删除失败</c:when>
									</c:choose>
									<c:if test="${presetApp.status==0 || presetApp.status==10 || presetApp.status==11 }">
										<a class="mr5" href="javascript:viewLog('${presetApp.id }');">日志</a>
									</c:if>
									</span> <em>|</em>
									<span style="font-weight:normal;"><b>更新时间：</b><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${presetApp.updateTime }"/></span>
                            	</p>
                            	
<!--                                 <p> -->
<%--                                     <span><b>更新时间：</b><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${presetApp.updateTime }"/></span> <!-- <em>|</em> <span><b>版本数：</b>30个</span> --%>
<!--                                     <em>|</em> -->

<!--                                     <span><b>下载：</b> 20次 </span> <em>|</em> <span><b>收藏：</b> 11次</span> --> 
<!--                                 </p> -->
								<p style="padding-top:5px; line-height:22px;">
                            	${presetApp.description }
                            	</p>
                                <div class="action_btn">
<!--                                     <button class="btn btn-yellow f14 mt10"><i class="icon-favorite"></i> 收藏</button> -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="xq_con">
                    <div class="xq_block">
                        <h5>详细信息</h5>
                        <div class="m-table-inst_info">
                            <table class="m-table">
                                <tbody>
                                <tr>
                                    <td>
                                        <div class="ii_label">镜像名称</div>
                                        <div class="ii_value">${presetApp.name }</div>
                                    </td>
                                    <td>
                                        <div class="ii_label">创建时间</div>
                                        <div class="ii_value"><fmt:formatDate value="${presetApp.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                                    </td>
                                    <td>
                                        <div class="ii_label">版本</div>
                                        <div class="ii_value">${presetApp.version}</div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="ii_label">文件</div>
                                        <div class="ii_value"><a href="${WEB_APP_PATH }/store/app/download.do?id=${presetApp.id}">${presetApp.fileName }</a></div>
                                    </td>
                                    <td>
                                        <div class="ii_label">启动命令</div>
                                        <div class="ii_value">${presetApp.command}</div>
                                    </td>
                                    <td>
                                        <div class="ii_label">工作目录</div>
                                        <div class="ii_value">${presetApp.workDir}</div>
                                    </td>
                                </tr>
                                <tr>
                                	<td colspan="3">
                                	<div class="ii_label">端口</div>
                                    <div class="ii_value">
                                		<p id="ports" style="display:none;">${presetApp.ports }</p>
                                        <table class="table_creat config-table" id="dependencyResources" style="width:600px;">
                                            <tr>
                                                <th>端口号</th>
                                                <th>类型</th>
                                                <th>关键字</th>
                                                <th>说明</th>
                                            </tr>
                                            <tbody id="portsBody">
                                            
                                            </tbody>
                                        </table>
                                    </div>
                                	</td>
                                </tr>
                                <tr>
        <td colspan="3">
        <div class="ii_label">依赖资源</div>
        <div class="ii_value">
        <p id="config" style="display:none;">${presetApp.config }</p>
        <table class="table_creat config-table" id="dependencyResources" style="width:600px;">
        <tr>
        <th>序号</th>
        <th>类型</th>
        <th>说明</th>
        <th>初始化动作</th>
        </tr>
        <tbody id="configBody">

        </tbody>
        </table>
        </div>
        </td>
        </tr>
                                </tbody>
                            </table>
                        </div>

                    </div>
                    <div class="xq_block">
                        <h5>Dockerfile</h5>
                        <div class="m-table-inst_info p10">
                            <pre>${presetApp.dockerfile}</pre>
                        </div>
                    </div>
                    <div class="xq_block">
                        <h5>详细描述</h5>
                        <div  id="editormdDiv" style="padding: 5px 0px 1px 5px;">
                           	<textarea style="display: none;" cols="83" rows="15" name="documentation" required>${presetApp.documentation }</textarea>
                       	</div>
                    </div>
                </div>
            </div>
        </div>
        <script src="${WEB_APP_PATH }/assets/js/zeroclipboard/ZeroClipboard.min.js"></script>
       
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
        	//copy
        	var client = new ZeroClipboard( document.getElementById("copy-button") );
			client.on( "ready", function( readyEvent ) {
			  client.on( "aftercopy", function( event ) {
			    common.tips("已复制到剪贴板。",1);
			  } );
			} );
        	
        	var ports = $.parseJSON($("#ports").html());
        	var config = $.parseJSON($("#config").html());
        	var portsCon = '';
        	var configCon = '';
        	$.each(ports,function(i,v){
        		portsCon += '<tr>';
        		portsCon += '<td>'+v.portNo+'</td>';
        		portsCon += '<td>'+v.portType+'</td>';
        		portsCon += '<td>'+v.portKey+'</td>';
        		portsCon += '<td>'+v.portDesc+'</td>';
        		portsCon += '</tr>';
        	})
        	$("#portsBody").html(portsCon);
        	$.each(config,function(i,v){
        		configCon += '<tr>';
        		configCon += '<td>'+v.number+'</td>';
        		var type = "";
        		var initActionFile = "无";
        		if(v.type=='db'){
        			type = "数据库";
        			if(typeof v.initAction.file != 'undefined' && v.initAction.file){
        				initActionFile = '<a href="${WEB_APP_PATH}/store/app/downloadInitFile.do?id=${presetApp.id}&number='+v.number+'">'+v.initAction.file.fileName+'</a>';
        			}
        		}else if(v.type=='mq'){
        			type = "消息中间件";
        		}else if(v.type=='cache'){
        			type = "高速缓存";
        		}else if(v.type=='nas'){
        			type = "NAS存储";
        		}
        		configCon += '<td>'+type+'</td>';
        		configCon += '<td>'+v.desc+'</td>';
        		configCon += '<td>'+initActionFile+'</td>';
        		configCon += '</tr>';
        	})
        	$("#configBody").html(configCon);
        	
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
        })
        </script>
    </template:replace>
</template:include>