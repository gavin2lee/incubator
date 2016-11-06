<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="form_control form_control_setting p20">
                            <div class="form_block">
                                <label>Jenkins地址</label>
                                <div class="form-right">
                                <input type="text" style="width: 400px;" value="${ config['jenkins.serverUri'] }" name="config['jenkins.serverUri']"  />
                           <p class="help_text" ><span>Jenkins的地址,配置如
			                         <em>http://127.0.0.1:88/</em>
			                          其中IP及端口地址为实际Jenkins部署的服务器IP及端口</span>
			               </p>
			               </div>
                            </div>
                        </div>
              <div class="line3"></div>
