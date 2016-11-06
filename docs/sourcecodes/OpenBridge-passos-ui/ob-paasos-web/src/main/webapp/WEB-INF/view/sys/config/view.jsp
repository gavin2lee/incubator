<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="./base.jsp"  active="view" >
	<template:replace name="title">
		系统参数配置
	</template:replace> 
	<template:replace name="detail-info">
		<form action="${WEB_APP_PATH}/sys/config/save.do" enctype="application/x-www-form-urlencoded" method="post">
			<div class="form_control form_control_setting p20">
				<div class="form_block">
					<label>PAASOS 访问地址</label>
					<div class="form-right">
						<input type="text" style="width: 400px;"
							value="${ config['paasos.web.url'] }"
							name="config['paasos.web.url']" />
					</div>
				</div>
				<div class="form_block">
					<label>APP Factory访问地址</label>
					<div class="form-right">
						<input type="text" style="width: 400px;"
							value="${ config['paasos.app.url'] }"
							name="config['paasos.app.url']" />
					</div>
				</div>
				<div class="form_block">
					<label>API Manager访问地址</label>
					<div class="form-right">
						<input type="text" style="width: 400px;"
							value="${ config['paasos.api.url'] }"
							name="config['paasos.api.url']" />
					</div>
				</div>
				<div class="form_block">
					<label>API 调用认证地址</label>
					<div class="form-right">
						<input type="text" style="width: 400px;"
							value="${ config['api.auth.server'] }"
							name="config['api.auth.server']" />
					</div>
				</div>
				<div class="form_block">
					<label>认证密钥</label>
					<div class="form-right">
						<input type="text" style="width: 400px;"
							value="${ config['paasos.token.secret'] }"
							name="config['paasos.token.secret']" />
						<p class="help_text">
							<span>用于 PAASOS 、 API Manager 、 APP Factory
								系统之间认证的关键安全参数，三个系统的该参数需要配置必需相同。<br />该密钥参数为长度100以内的字符。<br />该内容不要轻易更改，更改会导致所有在线用户下线。<br />
							</span>
						</p>
					</div>
				</div>
			</div>
			<div class="line3"></div>


			<div class="form_control form_control_setting"
				style="padding: 0 20px;">
				<div class="form_block mt10">
					<label>&nbsp;</label>
					<div>
						<button type="submit" class="btn btn-sm btn-yellow2"
							style="width: 200px; height: 40px;">保 存</button>
					</div>
				</div>
			</div>
		</form>
		<br><br>
	</template:replace>
</template:include>
