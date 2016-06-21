<?php include template('header','admin');?>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">友情链接<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
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
					<p>- 友情链接可添加多个，部分模板不支持图片友链。</p>
				</div>
			</div>
			<div class="hr-gray"></div>
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a href="<?php echo url('add')?>"><i class="ico_add"></i>添加</a>
					<div class="spacer-gray"></div>
					<a data-message="是否确定删除所选？" href="<?php echo url('delete')?>" data-ajax='id'><i class="ico_delete"></i>删除</a>
					<div class="spacer-gray"></div>
				</div>
			</div>
			<div class="table resize-table paging-table check-table clearfix">
				<div class="tr">
					<span class="th check-option" data-resize="false">
						<span><input id="check-all" type="checkbox" /></span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">排序</span>
					</span>
					<span class="th" data-width="20">
						<span class="td-con">链接名称</span>
					</span>
					<span class="th" data-width="50">
						<span class="td-con">链接地址</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">新窗口打开</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">操作</span>
					</span>
				</div>
				<?php foreach($friendlink as $key => $value){?>
				<div class="tr">
					<span class="td check-option"><input type="checkbox" name="id" value="<?php echo $value['id']?>" /></span>
						<span class="td">
						<span class="td-con">
							<div class="double-click">
								<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
								<input name="sort" class="input double-click-edit text-ellipsis text-center" type="text" value="<?php echo $value['sort']?>" />
							</div>
						</span>
					</span>
					<span class="td ident">
						<span class="ident-show">
							<em class="ico_pic_show"></em>
							<div class="ident-pic-wrap">
								<img src="<?php echo $value['logo']?>" />
							</div>
						</span>
						<div class="double-click">
							<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
							<input class="input double-click-edit text-ellipsis" type="text" value="<?php echo $value['name']?>" name="name"/>
						</div>
					</span>
					<span class="td">
						<span class="td-con">
							<div class="double-click">
								<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
								<input name="url" class="input double-click-edit text-ellipsis" type="text" value="<?php echo $value['url']?>" />
							</div>
						</span>
					</span>
					<span class="td">
					<?php if($value['target'] == 0){?>
						<a class="ico_up_rack cancel" href="javascript:;" title="点击取消"></a>
					<?php }else{?>
						<a class="ico_up_rack" href="javascript:;" title="点击取消"></a>
					<?php }?>						
					</span>
					<span class="td">
						<span class="td-con"><a href="<?php echo url('edit',array('id'=>$value['id']))?>">编辑</a>&nbsp;&nbsp;&nbsp;
						<a href="<?php echo url('delete',array('id[]'=>$value['id']))?>" data-confirm="是否确认删除？">删除</a></span>
					</span>
				</div>
				<?php }?>
				<div class="paging padding-tb body-bg clearfix">
					<?php echo $pages;?>
					<div class="clear"></div>
				</div>
			</div>
		</div>
		<script>
			$(".table").resizableColumns();
			$(".paging-table").fixedPaging();
			//启用与关闭
			$(".table .ico_up_rack").bind('click',function(){
				if(!$(this).hasClass("cancel")){
					$(this).addClass("cancel");
					$(this).attr("title","点击打开");
				}else{
					$(this).removeClass("cancel");
					$(this).attr("title","点击取消");
				}
			});
			//ajax_edit编辑
			var ajax_edit = '<?php echo url('ajax_edit')?>';
			$("input[name=name]").live('blur',function(){
				var id=$(this).parents(".tr").find("input[type=checkbox]").val();
				var name = $(this).val();
				$.post(ajax_edit,{'id':id,'name':name},function(data){
					if(data == 1){
						return true;
					}else{
						return false;
					}
				})
			})
			$("input[name=url]").live('blur',function(){
				var id = $(this).parents(".tr").find("input[type=checkbox]").val();
				var url = $(this).val();
				$.post(ajax_edit,{'id':id,'url':url},function(data){
					if(data == 1){
						return true;
					}else{
						return false;
					}
				})
			})
			$("input[name=sort]").live('blur',function(){
				var id = $(this).parents(".tr").find("input[type=checkbox]").val();
				var sort = $(this).val();
				$.post(ajax_edit,{'id':id,'sort':sort},function(data){
					if(data == 1){
						return true;
					}else{
						return false;
					}
				})
			})
			$(".ico_up_rack").live('click',function(){
				var id = $(this).parents(".tr").find("input[type=checkbox]").val();
				var target = $(this).attr('class') == 'ico_up_rack' ? 1:0;
				$.post(ajax_edit,{'id':id,'target':target},function(data){
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
