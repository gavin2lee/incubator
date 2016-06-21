<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>后台管理登陆 - 海盗云商</title>
		<link type="text/css" rel="stylesheet" href="<?php echo __ROOT__;?>statics/css/haidao.css" />
		<link type="text/css" rel="stylesheet" href="<?php echo __ROOT__;?>statics/css/admin.css" />
		<script type="text/javascript" src="<?php echo __ROOT__;?>statics/js/jquery-1.7.2.min.js" ></script>
		<!--<script type="text/javascript" src="<?php echo __ROOT__;?>statics/js/haidao.form.js" ></script>-->
	</head>
	<body>
		<div class="login-wrapper">
			<div class="left fl">
				<span class="logo"><img src="<?php echo __ROOT__;?>statics/images/login_logo.png" /></span>
				<p class="margin-big-top"><a class="text-sub" href="http://www.haidao.la/">海盗云商系统</a> 是 <a class="text-sub" href="http://www.haidao.la/">迪米盒子</a>公司旗下以电子商务为基础的专业网店建站系统，专注电子商务运营服务。</p>
			</div>
			<div class="right fr">
                <form action="<?php echo url('login');?>" onsubmit="return submit_check()" method="POST" data-layout="rank">
				<div class="form-box form-layout-rank border-bottom-none clearfix">
					<?php echo Form::input('text', 'username', '', '用户名：', ''); ?>
					<?php echo Form::input('password', 'password', '', '密&emsp;码：'); ?>
				</div>
				<div class="login-btn margin-top">
                    <input type="submit" name="dosubmit" class="button bg-main" value="确定" />
				</div>
                </form>
			</div>
		</div>
		<script>
			$('.form-group:last-child').css({zIndex:"3"});
			function submit_check(){
				var $user=$("input[name=username]").val();
				var $pass=$("input[name=password]").val();
				if(!$user || !$pass){
					return false;
				}
			}
		</script>
	</body>
</html>
