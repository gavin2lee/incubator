<?php include template('header','admin');?>
	<body>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">权限管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav">
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a href="<?php echo url('add')?>"><i class="ico_add"></i>添加</a>
					<div class="spacer-gray"></div>
					<a data-message="是否确定删除所选？" href="<?php echo url('del')?>" data-ajax='id'><i class="ico_delete "></i>删除</a>
					<div class="spacer-gray"></div>
				</div>
			</div>
			<div class="table resize-table check-table border clearfix">
				<div class="tr">
					<span class="th check-option" data-resize="false">
						<span><input id="check-all" type="checkbox" /></span>
					</span>
					<span class="th" data-width="20">
						<span class="td-con">权限名</span>
					</span>
					<span class="th" data-width="50">
						<span class="td-con">权限描述</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">是否启用</span>
					</span>
					<span class="th" data-width="20">
						<span class="td-con">操作</span>
					</span>
				</div>
				<?php foreach($data as $k=>$v):?>
				<div class="tr">
					<div class="td check-option">
						<?php if($v['id']!=1):?>
						<input type="checkbox" name="id" value="<?php echo $v['id']?>" />
						<?php else:?>
							-
						<?php endif;?>
					</div>
					<span class="td">
						<span class="td-con"><?php echo $v['title']?></span>
					</span>
					<span class="td">
						<span class="td-con"><?php echo $v['description']?></span>
					</span>
					<span class="td">
						<?php if($v['id']!=1):?>
						<?php if($v['status']==1):?>
							<a class="ico_up_rack" href="javascript:;" title="点击启用/禁用角色" data-id="<?php echo $v['id'];?>"></a>
							<?php else:?>
							<a class="ico_up_rack cancel" href="javascript:;" title="点击启用/禁用角色" data-id="<?php echo $v['id'];?>"></a>
						<?php endif;?>
						<?php else:?>
							--
						<?php endif;?>
					</span>
					<span class="td">
						<?php if($v['id']==1):?>
						<span class="td-con">--</span>
						<?php else:?>
						<span class="td-con"><a href="<?php echo url('edit',array('id'=>$v['id']))?>">编辑</a>&nbsp;&nbsp;&nbsp;<a data-confirm="是否确定删除？" href="<?php echo url('del',array('id'=>$v['id']))?>">删除</a></span>
						<?php endif;?>
					</span>
				</div>
				<?php endforeach;?>
			</div>
		</div>
		<script>
			var status = true;
			var post_status_url="<?php echo url('ajax_status')?>";
			$(".table").resizableColumns();
			$(window).load(function(){
				$(".table .ico_up_rack").bind('click',function(){
					if(ajax_status($(this).attr('data-id'))=='true'){
						if(!$(this).hasClass("cancel")){
							$(this).addClass("cancel");
						}else{
							$(this).removeClass("cancel");
						}
					}
				});
				//改变状态
				function ajax_status(id){
					$.post(post_status_url,{'id':id,'formhash':formhash},function(data){
						if(data.status == 1){
							status =  true;
						}else{
							status =  false;
						}
					},'json');
					
					return status;
				}
			})
		</script>
	</body>
</html>
