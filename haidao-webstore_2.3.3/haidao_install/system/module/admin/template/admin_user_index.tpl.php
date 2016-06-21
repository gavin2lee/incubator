<?php include template('header','admin');?>
	<body>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">团队管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
				<li><a class="current" href="javascript:;"></a></li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav">
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a href="<?php echo url('add')?>"><i class="ico_add"></i>添加</a>
					<div class="spacer-gray"></div>
					<a data-message="是否确定删除所选？" href="<?php echo url('del')?>" data-ajax='id'><i class="ico_delete"></i>删除</a>
					<div class="spacer-gray"></div>
				</div>
			</div>
			<div class="table resize-table check-table border clearfix">
				<div class="tr">
					<div class="th check-option" data-resize="false">
						<input id="check-all" type="checkbox" />
					</div>
					<span class="th" data-width="30">
						<span class="td-con">用户名</span>
					</span>
					<span class="th" data-width="20">
						<span class="td-con">所属分组</span>
					</span>
					<span class="th" data-width="25">
						<span class="td-con">最后登录时间</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">共计登录次数</span>
					</span>
					<span class="th" data-width="15">
						<span class="td-con">操作</span>
					</span>
				</div>
				<?php foreach($data as $k=>$v):?>
				<div class="tr">
					<div class="td check-option">
						<?php if($v['id']==1):?>
							-
						<?php else:?>
							<input type="checkbox" name="id" value="<?php echo $v['id']?>" />
						<?php endif;?>
					</div>
					<span class="td">
						<span class="td-con"><?php echo $v['username']?></span>
					</span>
					<span class="td">
						<span class="td-con">
						<?php if($v['id']==1):?>
							超级管理员
						<?php else:?>
							<?php echo isset($v['group_name'])?$v['group_name']:'-'?>
						<?php endif;?>
						</span>
					</span>
					<span class="td">
						<span class="td-con"><?php echo date('Y-m-d H:i:s',$v['last_login_time'])?></span>
					</span>
					<span class="td">
						<span class="td-con"><?php echo $v['login_num']?></span>
					</span>
					<span class="td">
						<span class="td-con">
						<?php if($v['id']==1):?>
							管理员不允许操作
						<?php else:?>
							<a href="<?php echo url('edit',array('id'=>$v['id']))?>">编辑</a>&nbsp;&nbsp;&nbsp;<a data-confirm="是否确定删除？" href="<?php echo url('del',array('id'=>$v['id']))?>">删除</a>
						<?php endif;?>
						</span>
					</span>
				</div>
				<?php endforeach;?>
			</div>
		</div>
		<script>
			$('.table').resizableColumns();
			$(function(){
				//双击编辑
				$('.double-click-edit').on('blur',function(){
					$.post(save_title_url,{id:$(this).attr('data-id'),title:""+$(this).val()+""},function(data){
					})
				})
			})
		</script>
	</body>
</html>
