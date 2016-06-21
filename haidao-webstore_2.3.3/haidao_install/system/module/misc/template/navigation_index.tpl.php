<?php include template('header','admin');?>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">导航管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
				<li><a href="<?php echo url('misc/focus/index')?>">首页幻灯片</a></li>
				<li><a class="current" href="javascript:;">导航设置</a></li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav">
			<div class="tips margin-tb">
				<div class="tips-info border">
					<h6>温馨提示</h6>
					<a id="show-tip" data-open="true" href="javascript:;">关闭操作提示</a>
				</div>
				<div class="tips-txt padding-small-top layout">
					<p>- 导航设置仅针对主导航的设置，不包含顶部导航及会员中心导航的设置。</p>
				</div>
			</div>
			<div class="hr-gray"></div> 
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a href="<?php echo url('add')?>"><i class="ico_add"></i>添加</a>
					<div class="spacer-gray"></div>
					<a href=""><i class="ico_delete"></i>删除</a>
					<div class="spacer-gray"></div>
				</div>
			</div>
			<div class="table resize-table check-table treetable border clearfix">
				<div class="tr border-none">
					<div class="th check-option" data-resize="false">
						<input id="check-all" type="checkbox" />
					</div>
					<div class="th" data-width="10"><span class="td-con">排序</span></div>
					<div class="th" data-width="10"><span class="td-con">导航名称</span></div>
					<div class="th" data-width="60"><span class="td-con">导航链接</span></div>
					<div class="th" data-width="10"><span class="td-con">新窗口打开</span></div>
					<div class="th" data-width="10"><span class="td-con">操作</span></div>
				</div>
				<?php foreach($navigation as $key => $value){?>
				<div class="tr" data-tree-id="1">
					<div class="td check-option"><input type="checkbox" name="id" value="<?php echo $value['id']?>" /></div>
					<div class="td">
						<div class="td-con">
							<div class="double-click">
								<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
								<input name="sort" class="input double-click-edit text-ellipsis text-center" type="text" value="<?php echo $value['sort']?>" />
							</div>
						</div>
					</div>
					<div class="td">
						<div class="td-con">
							<div class="double-click">
								<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
								<input name="name" class="input double-click-edit text-ellipsis text-center" type="text" value="<?php echo $value['name']?>" />
							</div>
						</div>
					</div>
					<div class="td">
						<div class="td-con">
							<div class="double-click">
								<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
								<input name="url" class="input double-click-edit text-ellipsis" type="text" value="<?php echo $value['url']?>" />
							</div>
						</div>
					</div>
					<div class="td">
					<?php if($value['target'] == 0){?>
						<a class="ico_up_rack cancel" href="javascript:;" title="点击关闭"></a>
					<?php }else{?>
						<a class="ico_up_rack" href="javascript:;" title="点击关闭"></a>
					<?php }?>
					</div>
					<div class="td">
						<span class="td-con"><span class="td-con">
							<a href="<?php echo url('edit',array('id'=>$value['id']))?>">编辑</a>&nbsp;&nbsp;&nbsp;
							<a href="<?php echo url('delete',array('id[]'=>$value['id']))?>" data-confirm="是否确认删除？">删除</a></span></span>
					</div>
				</div>
				<?php }?>
			</div>
		</div>
		<script>
			$(".table").resizableColumns();
			//ajax_edit编辑
			var ajax_edit = '<?php echo url("ajax_edit")?>';
			$("input[name=name]").live('blur',function(){
				var name = $(this).val();
				var id=$(this).parents(".tr").find("input[name=id]").val();
				$.post(ajax_edit,{"id":id,"name":name},function(data){
					if(data == 1){
						return true;
					}else{
						return false;
					}
				})
			})
			$("input[name=sort]").live('blur',function(){
				var sort = $(this).val();
				var id=$(this).parents(".tr").find("input[name=id]").val();
				$.post(ajax_edit,{"id":id,"sort":sort},function(data){
					if(data == 1){
						return true;
					}else{
						return false;
					}
				})
			})
			$("input[name=url]").live('blur',function(){
				var url = $(this).val();
				var id=$(this).parents(".tr").find("input[name=id]").val();
				$.post(ajax_edit,{"id":id,"url":url},function(data){
					if(data == 1){
						return true;
					}else{
						return false;
					}
				})
			})
			$(".ico_up_rack").live('click',function(){
				var target = $(this).attr('class') == 'ico_up_rack' ? 0:1;
				if(target == 1){
					$(this).attr('class','ico_up_rack');
				}else{
					$(this).attr('class','ico_up_rack cancel');
				}
				var id=$(this).parents(".tr").find("input[name=id]").val(); 
				$.post(ajax_edit,{"id":id,"target":target},function(data){
					if(data == 1){
						return true;
					}else{
						return false;
					}
				})
			})
			$(".table-work .border a:last").bind('click',function(){
				var ids = Array(); 
				var delete_focus = '<?php echo url('delete')?>';
				$("input[type=checkbox]").each(function(){
					if($(this).attr("checked") == 'checked'){
						ids.push($(this).val());
					}
				})
				$(this).attr('href',delete_focus+'&id[]='+ids);
			});
		</script>
	</body>
</html>
