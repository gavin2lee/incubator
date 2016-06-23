<?php include template('header','admin');?>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">文章管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
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
					<p>- 文章区别于站点帮助，可在文章列表页点击查看</p>
				</div>
			</div>
			<div class="hr-gray"></div>
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a href="<?php echo url('add') ?>"><i class="ico_add"></i>添加</a>
					<div class="spacer-gray"></div>
					<a data-message="是否确定删除所选？" href="<?php echo url('delete')?>" data-ajax='id'><i class="ico_delete"></i>删除</a>
					<div class="spacer-gray"></div>
				</div>
			</div>
			<div class="table resize-table check-table treetable border clearfix">
				<div class="tr border-none">
					<div class="th check-option" data-resize="false">
						<input id="check-all" type="checkbox" />
					</div>
					<div class="th" data-width="10"><span class="td-con">排序</span></div>
					<div class="th" data-width="35"><span class="td-con">标题</span></div>
					<div class="th" data-width="20"><span class="td-con">文章分类</span></div>
					<div class="th" data-width="15"><span class="td-con">发布时间</span></div>
					<div class="th" data-width="10"><span class="td-con">显示</span></div>
					<div class="th" data-width="10"><span class="td-con">操作</span></div>
				</div>
				<?php foreach ($article as $key => $value) {?>
				<div class="tr" data-tree-id="<?php echo $value['id']?>">
					<div class="td check-option"><input type="checkbox" name="id" value="<?php echo $value['id']?>" /></div>
					<div class="td">
						<div class="td-con">
							<div class="double-click">
								<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
								<input name="sort" class="input double-click-edit text-ellipsis" type="text" value="<?php echo $value['sort']?>" />
							</div>
						</div>
					</div>
					<div class="td">
						<div class="td-con">
							<div class="double-click">
								<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
								<input name="title" class="input double-click-edit text-ellipsis" type="text" value="<?php echo $value['title']?>" />
							</div>
						</div>
					</div>
					<div class="td">
						<span class="td-con"><?php echo $value['category']?></span>
					</div>
					<div class="td">
						<span class="td-con"><?php echo $value['dataline']?></span>
					</div>
					<div class="td">
					<?php if($value['display']==0){?>
						<a class="ico_up_rack cancel" href="javascript:;" title="点击关闭"></a>
					<?php }else{?>
					    <a class="ico_up_rack" href="javascript:;" title="点击关闭"></a>
					<?php }?>
					</div>
					<div class="td">
						<span class="td-con"><span class="td-con"><a href="<?php echo url('edit',array('id'=>$value['id']))?>">编辑</a>&nbsp;&nbsp;&nbsp;<a href="<?php echo url('delete',array('id[]'=>$value['id']))?>" data-confirm="是否确认删除？">删除</a></span></span>
					</div>
				</div>
				<?php }?>
			</div>
				<div class="paging padding-tb body-bg clearfix">
					<?php echo $pages;?>
					<div class="clear"></div>
				</div>
		</div>
		<script>
			$(window).load(function(){
				$(".table").resizableColumns();
				$(".table").treetable();
				$('.table .tr:last-child').addClass("border-none");
				//启用与关闭
				$(".table .ico_up_rack").bind('click',function(){
					if(!$(this).hasClass("cancel")){
						$(this).addClass("cancel");
						$(this).attr("title","点击显示");
					}else{
						$(this).removeClass("cancel");
						$(this).attr("title","点击关闭");
					}
				});
			})
			//ajax编辑文章信息
			var ajax_edit = "<?php echo url(ajax_edit)?>";
			$("input[name=title]").bind('blur',function(){
			   var title=$(this).val();
			   var id = $(this).parents('.tr').attr('data-tree-id');
			   $.post(ajax_edit,{'id':id,'title':title},function(data){
				   if(data == 1){
					   return true;
				   }else{
					   return false;
				   }
			   });
			})
			$("input[name=sort]").bind('blur',function(){
				var sort = $(this).val();
				var id = $(this).parents('.tr').attr('data-tree-id');
				$.post(ajax_edit,{'id':id,'sort':sort},function(data){
					if(data == 1){
						return true;
					}else{
						return false;
					}
				})
			})
			$(".ico_up_rack").bind('click',function(){
				var display=$(this).attr('class') == 'ico_up_rack' ? 0:1;
				var id= $(this).parents('.tr').attr('data-tree-id');
				$.post(ajax_edit,{'id':id,'display':display},function(data){
					if(data == 1){
						return true;
					}else{
						return false;
					}
					
				})
			})
		</script>
	</body>
</html>
