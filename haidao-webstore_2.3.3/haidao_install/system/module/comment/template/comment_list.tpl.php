<?php include template('header','admin');?>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">评价列表<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav">
			<form action="<?php echo __APP__ ?>" method='get'>
			<input type="hidden" name="m" value="comment">
			<input type="hidden" name="c" value="admin">
			<input type="hidden" name="a" value="index">
			<div class="member-comment-search clearfix">
				<div class="form-box form-layout-rank clearfix border-bottom-none">
					<?php echo Form::input('calendar', 'starttime', $_GET['starttime'], '评论时间', '', array('format' => 'YYYY-MM-DD')); ?>
					<?php echo Form::input('calendar', 'endtime', $_GET['endtime'], '~', '', array('format' => 'YYYY-MM-DD')); ?>
					<?php $item = array('屏蔽', '显示','所有');krsort($item);?>
					<?php echo Form::input('select', 'is_shield', isset($_GET['is_shield']) ? $_GET['is_shield'] : 2, '评论状态', '', array('items' => $item)); ?>
					<?php echo Form::input('text', 'keyword', $_GET['keyword'], '搜索', '', array('placeholder'=> '请输入会员名搜索用户评论')); ?>
				</div>
				<input class="button bg-sub fl" type="submit" name="dosubmit" value="查询">
			</div>
			</form>
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a data-message="是否确定删除所选？" href="<?php echo url('delete')?>" data-ajax='id'><i class="ico_delete"></i>删除</a>
					<div class="spacer-gray"></div>
					<!--<a href=""><i class="ico_out"></i>导出</a>
					<div class="spacer-gray"></div>-->
				</div>
			</div>
			<div class="table-wrap resize-table">
				<div class="table resize-table paging-table check-table high-table border clearfix">
					<div class="tr">
						<span class="th check-option" data-resize="false">
							<span><input id="check-all" type="checkbox" /></span>
						</span>
						<span class="th" data-width="45">
							<span class="td-con">评价商品</span>
						</span>
						<span class="th" data-width="10">
							<span class="td-con">评分</span>
						</span>
						<span class="th" data-width="15">
							<span class="td-con">会员账号</span>
						</span>
						<span class="th" data-width="15">
							<span class="td-con">评价时间</span>
						</span>
						<span class="th" data-width="5">
							<span class="td-con">显示</span>
						</span>
						<span class="th" data-width="10">
							<span class="td-con">操作</span>
						</span>
					</div>
					<?php foreach ($lists as $i => $r): ?>
						<?php
						$moods = array(
							'positive' => '好评',
							'neutral' => '中评',
							'negative' => '差评'
						);
						?>
					<div class="tr">
						<span class="td check-option"><input type="checkbox" name="id" value="<?php echo $r['id'] ?>" /></span>
						<div class="td">
							<div class="td-con td-pic text-left">
								<span class="pic"><a target="_blank" href="<?php echo url('goods/index/detail',array('sku_id' => $r['sku_id']))?>"><img src="<?php echo $r['_sku']['thumb'] ?>" /></a></span>
								<span class="title text-ellipsis txt"><a target="_blank" href="<?php echo url('goods/index/detail',array('sku_id' => $r['sku_id']))?>"><?php echo $r['_sku']['sku_name'] ?></a></span>
								<span class="icon">
									<?php foreach ($r['_sku']['spec'] as $spec): ?>
									<em class="text-main"><?php echo $spec['name'] ?>：</em><?php echo $spec['value'] ?>&nbsp;
									<?php endforeach ?>
								</span>
							</div>
						</div>
						<div class="td"><span class="td-con"><?php echo $moods[$r['mood']] ?></span></div>
						<div class="td"><span class="td-con"><?php echo $r['username'] ?></span></div>
						<div class="td"><span class="td-con"><?php echo date('Y-m-d', $r['datetime']) ?></span></div>
						<div class="td">
						<?php if($r['is_shield'] == 1){?>
							<a class="ico_up_rack" data-id="<?php echo $r['id']?>" href="javascript:;" title="点击屏蔽评价"></a>
						<?php }else{?>
							<a class="ico_up_rack cancel" data-id="<?php echo $r['id']?>" href="javascript:;" title="点击显示评价"></a>
							<?php }?>
						</div>
						<div class="td">
							<span class="td-con"><a href="javascript:;" data-id="<?php echo $r['id'] ?>" data-event="reply">回复</a>&nbsp;&nbsp;&nbsp;
							<a data-confirm="是否确定删除？" href="<?php echo url('delete', array('id[]' => $r['id'])) ?>">删除</a></span>
						</div>
						<div class="clear"></div>
						<div class="layout comments-list">
							<p class="text-main comment-title"><b>评价：</b>
							<div class="commentinfo text-ellipsis">
								<p><?php echo $r['content'] ?></p>
							</div>

							<ul class="imgs">
								<?php foreach ($r['imgs'] as $img): ?>
								<li><a class="pic-center margin-right" href="<?php echo $img ?>" target="_blank"><img src="<?php echo $img ?>" alt=""></a></li>
								<?php endforeach ?>
							</ul>
							
							<p class="text-red reply-title"><b>回复：</b></p>
							<div class="replyinfo text-ellipsis">
								<?php if ($r['reply_content']): ?>
									<p><?php echo $r['reply_content'] ?></p>
								<?php else: ?>
									<p>-</p>
								<?php endif ?>
							</div>
						</div>
					</div>
					<?php endforeach ?>
					<div class="paging padding-tb body-bg clearfix">
						<?php echo $pages; ?>
						<div class="clear"></div>
					</div>
				</div>
			</div>
		</div>
		<script>
			$('.form-group').each(function(i){
				$(this).addClass('group'+(i+1));
				$(this).find(".box").addClass("margin-none");
			});
			$(window).load(function(){
				$(".table").resizableColumns();
				$(".paging-table").fixedPaging();
			})

			$(function(){

				$("[data-event=reply]").click(function() {
					var $this = $(this);
					var d = dialog({
					    title: '回复',
					    width: 320,
					    okValue: "确定",
					    content: '<textarea id="popup-input" class="textarea layout padding border-none" style="height: 150px;" placeholder="请输入对买家评论的回复"></textarea>',
					    ok: function () {
					        var params = {
					        	id:$this.data('id'),
					        	reply_content:$('#popup-input').val()
					        }
					        if(params.reply_content.length < 1) {
					        	d.title('回复内容不能为空');
					        	return false;
					        }
					        $.post("<?php echo url('reply') ?>", params, function(ret){
					        	if(ret.status == 1) {
					        		d.title(ret.message);
					        		setTimeout(function(){
					        			window.top.main_frame.location.reload();
					        		},1000);
					        	} else {
					        		d.title(ret.message);
					        		return false;
					        	}
					        }, 'json')
					        return false;
					    },
					    cancelValue: "取消",
					    cancel: function(){

					    }
					});
					d.showModal();
				})
				$(".table .ico_up_rack").live('click',function(){
					var _this = $(this);
					var url = "<?php echo url('change_status')?>";
					$.post(url,{id:_this.data('id')},function(ret){
						console.log(ret.status)
						if(ret.status == 1){
							if(!_this.hasClass("cancel")){
								_this.addClass("cancel");
								_this.attr("title","点击显示评论");
							}else{
								_this.removeClass("cancel");
								_this.attr("title","点击屏蔽评论");
							}
						}else{
							alert(ret.message);
						}
					},'json')
					
				});
			})
		</script>
	</body>
</html>
