<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="form_control form_control_setting p20">
                            <div class="form_block">
                                <label>SVN地址</label>
                                <div class="form-right">
                                  <input type="text" style="width: 400px;" value="${ config['api.svn.url'] }" name="config['api.svn.url']" />
                                <p class="help_text" ><span>项目源码的地址,配置如
			                    <em>http://127.0.0.1:88/svn1</em>
			                     其中IP及端口地址为实际svn部署的服务器IP及端口</span></p>
                                </div>
                            </div>
                            <div class="form_block">
                                <label>SVN服务器 SSH IP</label>
                                <div class="form-right">
                                    <input type="text"  style="width: 400px;" value="${ config['api.svn.hostip'] }" name="config['api.svn.hostip']" />
                                <p class="help_text">实际svn部署的服务器IP</p>
                                </div>
                            </div>
                        </div>
                        <div class="line3"></div>