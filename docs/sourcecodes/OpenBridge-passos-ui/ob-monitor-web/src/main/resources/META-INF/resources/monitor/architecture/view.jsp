<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="build">
	<template:replace name="title">
		项目概述
	</template:replace>
	<template:replace name="content-body">
		<style>
			
		</style>
	     <div class="app_name">
            <a href="${WEB_APP_PATH }"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="${WEB_APP_PATH }/project/index.do">项目管理</a></p>
            <em>&gt;</em>

            <p class="app_a">
            	<a href="${WEB_APP_PATH }/project/overview/index.do?projectId=${param.projectId}">${project.projectName }&nbsp;</a>
            </p>
            <em>&gt;</em>
            <p class="app_a">
            	<a href="${WEB_APP_PATH }/project/build/index.do?projectId=${param.projectId}">项目镜像</a>
            </p>
            <em>&gt;</em>

            <p class="app_a">${paasProjectBuild.buildName }</p>
        </div>
        <div class="plate">
            <div class="project_r">
            	<form id="build_area">
            	<input type="hidden" name="projectId" value="${project.projectId }"/>
            	<input type="hidden" name="buildId" value="${paasProjectBuild.buildId }"/>
                <div class="r_block p20">
                	<c:if test="${project.projectType !='api' && project.projectType!='app' }">
	                    <div class="r_block">
	                        <div class="r_title">
	                            <h3 class="f14 h3_color h3_btm_line"><i class="icons add_ico_blue mr5"></i>查看构建镜像</h3>
	                            <div class="title_line"></div>
	                        </div>
	                        <div class="r_con p10_0">
	                            <div class="form_control p20">
	                                <div class="form_block">
	                                    <label>构建名称</label>
	                                    <input type="text" value="${paasProjectBuild.buildName }"><em>*</em>
	                                </div>
	                                <div class="form_block">
	                                    <label>构建内容</label>
	                                    <div class="specList">
	                                        <p class="mb10"><span class="w200"><input type="radio" name="iCheck" checked>&nbsp;&nbsp;包含dockerfile的zip文件</span>
	                                            <span class="w200"><input type="radio" name="iCheck">&nbsp;&nbsp;上传已构建好的Jar.gz</span></p>
	                                        <p class="mb10"><input type="text"><button class="btn btn-default btn-yellow2 btn-sm">选择</button></p>
	                                    </div>
	                                </div>
	                                <div class="form_block">
	                                    <label>构建tag</label>
	                                    <!--<input class="w100" type="text" value="yihecloud/tomcat" disabled readonly="readonly">-->
	                                    <input class="w200" type="text">
	                                </div>
	                                <div class="form_block">
	                                    <label>启动命令</label>
	                                    <input type="text">
	                                </div>
	                                <div class="form_block">
	                                    <label>工作目录</label>
	                                    <input type="text">
	                                </div>
	                                <div class="form_block">
	                                    <label>构建端口</label>
	                                    <div class="specList">
	                                        <table id="portList" class="table_creat">
	                                            <tr>
	                                                <th>端口号</th>
	                                                <th>类型</th>
	                                                <th>说明</th>
	                                                <th style="display: none;"></th>
	                                            </tr>
	                                            <tr class="row-data">
	                                                <td><input name="portNo"  type="text" class="u-ipt row-td"></td>
	                                                <td><input name="portType" type="text" class="u-ipt row-td"></td>
	                                                <td><input name="portDesc" type="text" class="u-ipt2 row-td"></td>
	                                                <td style="display: none;">
	                                                	<a class="addRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">+</em></a>
	                                                	<a class="deleteRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">-</em></a>
	                                                </td>
	                                            </tr>
	                                        </table>
	                                    </div>
	                                </div>
	                                <div class="form_block mt10">
	                                    <label>&nbsp;</label>
	                                    <button class="btn btn-default btn-yellow f16  mt10"><i class="ico_check"></i>创 建</button>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                  </c:if>
                    <!--基于OB构建-->
                    <c:if test="${project.projectType =='api' || project.projectType == 'app' }">
	                    <div class="r_block">
	                        <div class="r_title">
	                            <h3 class="f14 h3_color h3_btm_line"><i class="icons add_ico_blue mr5"></i>查看构建（基于ob构建）</h3>
	                            <div class="title_line"></div>
	                        </div>
	                        <div class="r_con p10_0">
	                            <div class="form_control p20">
	                            	<div class="form_block"  >
	                                    <label>构建名称</label>
	                                    <span>${paasProjectBuild.buildName }</span>
	                                </div>
	                                <div class="form_block">
	                                    <label>项目来源</label>
	                                    <c:choose>
	                                    	<c:when test="${project.projectType =='api'  }">
	                                    		<span>APIFactory<span>
	                                    	</c:when>
	                                    	<c:otherwise>
	                                    		<span>APPFactory<span>
	                                    	</c:otherwise>
	                                    </c:choose>
	                                </div>
	                               
	                                <div class="form_block"  >
	                                    <label>项目名称</label>
	                                    <span>
	                                    	${project.projectName }&nbsp;
	                                    </span>
	                                </div>
	                                <div class="form_block">
	                                    <label>项目版本</label>
	                                    <span>${paasProjectBuild.versionCode }</span>
	                                </div>
	                                <div class="form_block">
	                                    <label>构建文件</label>
	                                    <span>${paasProjectBuild.fileName }</span>
	                                </div>
	                                <div class="form_block">
	                                    <label>状态</label>
	                                    <span>
	                                    	<c:choose>
                                    			<c:when test="${ paasProjectBuild.deleteStatus != null && paasProjectBuild.deleteStatus==1}">
                                    				<i class="pas_ico zt_ico doing_ico mt5"></i>
                                    				删除中
                                    			</c:when>
                                    			<c:when test="${ paasProjectBuild.deleteStatus != null && paasProjectBuild.deleteStatus==2}">
                                    				<i class="pas_ico zt_ico fail_ico mt5"></i>
                                    				删除失败
                                    			</c:when>
                                    			<c:when test="${paasProjectBuild.status==1}">
                                    				<i class="pas_ico zt_ico waiting_ico mt5"></i>
                                    				未开始
                                    			</c:when>
                                    			<c:when test="${paasProjectBuild.status==5}">
                                    				<i class="pas_ico zt_ico doing_ico mt5"></i>
                                    				进行中
                                    			</c:when>
                                    			<c:when test="${paasProjectBuild.status==10}">
                                    				<i class="pas_ico zt_ico done_ico mt5"></i>
                                    				完成
                                    			</c:when>
                                    			<c:when test="${paasProjectBuild.status==0}">
                                    				<i class="pas_ico zt_ico fail_ico mt5"></i>
                                    				构建失败
                                    			</c:when>
                                    		</c:choose>
	                                    	&nbsp;
	                                    </span>
	                                </div>
	                                <div class="form_block">
	                                    <label>镜像信息</label>
	                                    <span>project/${paasProjectBuild.buildName }:${paasProjectBuild.buildTag }</span>
	                                </div>
	                                <div class="form_block">
	                                    <label>镜像端口</label>
	                                    <div class="specList">
	                                        <table id="portList" class="table_creat">
	                                            <tbody>
		                                            <tr>
		                                                <th style="width: 120px;">端口号</th>
		                                                <th style="width: 120px;">关键字</th>
		                                                <th style="width: 120px;">协议</th>
		                      						</tr>
	                      						</tbody>
	                                      </table>
	                                    </div>
	                                </div>
	                                
	                            </div>
	                        </div>
	                    </div>
	                 </c:if>
                </div>
                <!--创建构建结束-->
            	</form>
            </div>
        </div>
        
        <script id="imageport" type="text/html">
        	<tr>
			<td>{%=port%}</td>
			<td>{%=key%}</td>
			<td>{%=protocol%}</td>
			</tr>
        </script>
        <script>
        	$(function(){
        		var port = '${paasProjectBuild.port}';
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
        	});
        	
        </script>

	</template:replace>
</template:include>