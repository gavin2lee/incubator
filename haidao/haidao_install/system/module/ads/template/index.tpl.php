<?php include template('header','admin');?>
	<body>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">广告管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
				<li><a class="current" href="javascript:;">广告列表</a></li>
				<li><a href="<?php echo url('position_index')?>">广告位</a></li>
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
					<p>- 将广告位调用代码放入前台页面，将显示该广告位的广告</p>
					<p>- 温馨提示，创建广告之前需要添加一个广告位，广告位分为图片广告和文字广告</p>
				</div>
			</div>
			<div class="hr-gray"></div>
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a href="<?php echo url('add')?>"><i class="ico_add"></i>添加</a>
					<div class="spacer-gray"></div>
					<a data-message="是否确定删除所选？" href="<?php echo url('del')?>" data-ajax='id'><i class="ico_delete"></i>删除</a>
					<div class="spacer-gray"></div>
				</div>
			</div>
			<div class="table resize-table check-table paging-table border clearfix">
				<div class="tr border-none">
					<div class="th check-option" data-resize="false">
						<input id="check-all" type="checkbox" />
					</div>
					<div class="th" data-width="20"><span class="td-con">广告名称</span></div>
					<div class="th" data-width="15"><span class="td-con">所属广告位</span></div>
					<div class="th" data-width="10"><span class="td-con">类别</span></div>
					<div class="th" data-width="20"><span class="td-con">开始时间</span></div>
					<div class="th" data-width="20"><span class="td-con">结束时间</span></div>
					<div class="th" data-width="5"><span class="td-con">点击数</span></div>
					<div class="th" data-width="10"><span class="td-con">操作</span></div>
				</div>
				<?php foreach($ads as $k=>$v):?>
					<div class="tr">
					<div class="td check-option"><input type="checkbox" name="id" value="<?php echo $v['id']?>" /></div>
					<div class="td">
						<div class="td-con">
							<div class="double-click">
								<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
								<input class="input double-click-edit text-ellipsis" data-id='<?php echo $v['id']?>' type="text" value="<?php echo $v['title']?>" />
							</div>
						</div>
					</div>
					<div class="td">
						<span class="td-con text-left"><?php echo $v['position_name']?></span>
					</div>
					<div class="td">
						<span class="td-con"><?php echo $v['type_text']?></span>
					</div>
					<div class="td">
						<span class="td-con"><?php echo $v['startime_text']?></span>
					</div>
					<div class="td">
						<span class="td-con"><?php echo $v['endtime_text']?></span>
					</div>
					<div class="td">
						<span class="td-con"><?php echo $v['hist']?></span>
					</div>
					<div class="td">
						<span class="td-con"><span class="td-con"><a href="<?php echo url('edit',array('id'=>$v['id']))?>">编辑</a>&nbsp;&nbsp;&nbsp;
						<a data-confirm="是否确定删除？" href="<?php echo url('del',array('id'=>$v['id'],'formhash'=>FORMHASH))?>">删除</a>
						</span></span>
					</div>
				</div>
				<?php endforeach;?>
				
				<div class="paging padding-tb body-bg clearfix">
					<ul class="fr">
						<?php echo $pages?>
					</ul>
					<div class="clear"></div>
				</div>
			</div>
		</div>
		<script>
			var formhash='<?php echo FORMHASH?>';
			var del_url = "<?php echo url('del',array('formhash'=>FORMHASH))?>";
			var save_title_url = "<?php echo url('save_title')?>"
			$(window).load(function(){
				$(".table").resizableColumns();
				$(".table").fixedPaging();
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
			$(function(){
				//双击编辑
				$('.double-click-edit').on('blur',function(){
					$.post(save_title_url,{id:$(this).attr('data-id'),title:""+$(this).val()+"",formhash:""+formhash+""},function(data){
					})
				})
			})
		</script>
	</body>
</html>
