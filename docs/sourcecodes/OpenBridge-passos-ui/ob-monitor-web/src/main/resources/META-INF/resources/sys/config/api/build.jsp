<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="form_control form_control_setting p20">
                            <div class="form_block">
                                <label>编译构建 服务器</label>
                                <div class="form-right">
                                <input type="text" style="width: 400px;" value="${ config['build.hostip'] }" name="config['build.hostip']"  />
                           <p class="help_text" ><span>Maven编译构建的服务器IP</span>
			               </p>
			               </div>
                            </div>
                        </div>
              <div class="line3"></div>
