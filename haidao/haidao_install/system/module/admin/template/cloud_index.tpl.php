<?php include template('header','admin');?>
		<div class="content padding-big">
			<?php if(!isset($cloud)):?>
			<div class="warn-info border bg-white margin-top padding-lr">
				<i class="warn-info-ico ico_warn margin-right"></i>您还未绑定您的海盗云平台，请先绑定账号后才可使用云平台功能！
			</div>
			<?php endif?>
			<div class="margin-top">
				<div class="fl layout">
					<table cellpadding="0" cellspacing="0" class="border bg-white layout">
						<tbody>
							<tr class="bg-gray-white line-height-40 border-bottom">
								<th class="text-left padding-big-left">系统状态</th>
							</tr>
							<tr class="border">
								<td class="line-height-40 padding-big-left padding-big-right">
									<b class="cloud-label">绑定账号：</b>

										<?php if(isset($cloud) && $cloud):?>
										<?php echo $cloud['username']?>&emsp;&emsp;<a class="text-main" href="javascript:reBind();">重新绑定</a>
										<?php else:?>
										<a class="text-main" href="javascript:reBind();">立即绑定</a>
										<?php endif;?>

								</td>
							</tr>
							<tr class="border">
								<td class="line-height-40 padding-big-left">
									<b class="cloud-label">云平台通信：</b>
									<span id = 'cloud_status'>
									正在检测...
									</span>
								</td>
							</tr>
							<tr class="border">
								<td class="line-height-40 padding-big-left">
									<!--<b class="cloud-label">插件系统：</b><i class="ico_error"></i> 失败-->
									<b class="cloud-label">插件系统：</b><i class="ico_right"></i> 内测中，敬请期待......
								</td>
							</tr>
							<tr class="border">
								<td class="line-height-40 padding-big-left">
									<!--<b class="cloud-label">模块系统：</b><i class="ico_right"></i> 正常-->
									<b class="cloud-label">模块系统：</b><i class="ico_right"></i> 内测中，敬请期待......
								</td>
							</tr>
						</tbody>
					</table>
					<table cellpadding="0" cellspacing="0" class="margin-top border bg-white layout">
						<tbody>
							<tr class="bg-gray-white line-height-40 border-bottom">
								<th class="text-left padding-big-left">系统状态</th>
							</tr>
							<tr class="border">
								<td class="line-height-40 padding-big-left padding-big-right">
									<b class="cloud-label">站点URL：</b>
									<?php if(isset($cloud) && $cloud):?>
									<span class="text-normal">http://<?php echo $cloud['domain']?></span>
									&emsp;&emsp;&emsp;<a class="text-main" href="<?php echo url('update')?>">同步站点信息</a>
									<?php else:?>
										未绑定云平台
									<?php endif;?>
								</td>
							</tr>
							<tr class="border">
								<td class="line-height-40 padding-big-left">
									<b class="cloud-label">站点通信Token：</b>
									<?php if(isset($cloud) && $cloud):?>
									<?php echo substr_replace($cloud['token'],'****',4,24)?>  &emsp;（出于安全考虑，部分隐藏）
									<?php else:?>
										未绑定云平台
									<?php endif;?>
								</td>
							</tr>
							<tr class="border">
								<td class="line-height-40 padding-big-left">
									<b class="cloud-label">关闭站点：</b><?php echo $site_isclosed == 1 ? '否' : '是' ?>
								</td>
							</tr>
							<tr class="border">
								<td class="line-height-40 padding-big-left">
									<b class="cloud-label">服务器时间：</b><?php echo date_default_timezone_get().'&emsp;'.date('Y-m-d',time()); ?>
								</td>
							</tr>
							<tr class="border">
								<td class="line-height-40 padding-big-left">
									<b class="cloud-label">当前版本：</b>v<?php echo HD_VERSION ?>
								</td>
							</tr>
							<tr class="border">
								<td class="line-height-40 padding-big-left">
									<b class="cloud-label">授权状态：</b>
									<?php if(isset($cloud) && $cloud):?>
										<?php if($cloud['authorize'] == 0):?>
										<font color="#F74436">未授权</font>
										<?php else:?>
										<font color="#00BB9C">已授权</font>
										<?php endif;?>
									<?php else:?>
										<font color="#F74436">未授权</font>
									<?php endif;?>
								</td>
							</tr>
						</tbody>
					</table>
					<table cellpadding="0" cellspacing="0" class="margin-top border bg-white layout">
						<tbody>
							<tr class="bg-gray-white line-height-40 border-bottom">
								<th class="text-left padding-big-left">应用中心</th>
							</tr>
							<tr>
								<td>
									<div class="text-left today-sales layout border-top border-white fl" style="padding:0 20px;height:65px;line-height:64px;background-color:#fbfbfb;">
                                        <span class="fl">您有 <b class="text-main">0</b> 款应用可升级&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="text-main" href="">详情</a></span>
                                    </div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<script>
			window.cloud = <?php echo isset($cloud)?1:0?>;
			function reBind(){
				top.dialog({
					url: '<?php echo url('bulid')?>',
					title: '云平台绑定',
					width: 560,
					onclose: function () {
						if(this.returnValue){
							window.location.reload(true);
						}
					}
				})
				.showModal();
			}
			$(function(){
				var url = "<?php echo url('admin/cloud/getcloudstatus',array('formhas'=>FORMHASH))?>";
				var dom = $('#cloud_status');
				$.getJSON(url,'',function(data){
					if(data.status == 2){
						dom.html('未绑定云平台');
						return;
					}else if(data.status == 1){
						dom.html('<i class="ico_right"></i> 成功');
						return;
					}else if(data.status == 0){
						dom.html('<i class="ico_error"></i> 失败');
						return;
					}
				})
			})
		</script>
	</body>
</html>
