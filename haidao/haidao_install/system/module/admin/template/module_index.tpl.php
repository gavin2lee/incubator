<?php include template('header','admin');?>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">模块管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
				<li><a class="current" href="<?php echo url('index', array('enabled' => '1', 'install' => 1))?>">已启用模块</a></li>
				<li><a href="<?php echo url('index', array('enabled' => '0', 'install' => 1))?>">未启用模块</a></li>
				<li><a href="<?php echo url('index', array('install' => 0))?>">未安装模块</a></li>
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
					<p>- 添加商品时可选择商品分类，用户可根据分类查询商品列表</p>
					<p>- 点击分类名前“+”符号，显示当前分类的下级分类</p>
					<p>- 对分类作任何更改后，都需要到 设置 -> 清理缓存 清理商品分类，新的设置才会生效</p>
				</div>
			</div>
			<div class="hr-gray"></div>
			<div class="table resize-table paging-table high-table border margin-top clearfix">
				<div class="tr">
					<span class="th" data-width="85">
						<span class="td-con">模块名称</span>
					</span>
					<span class="th" data-width="15">
						<span class="td-con">操作</span>
					</span>
				</div>
				<?php foreach ($modules as $identifier => $module): ?>
				<div class="tr">
					<div class="td">
						<div class="td-con td-pic text-left">
							<span class="pic"><img src="<?php echo $module['icon']?>" /></span>
							<span class="title text-ellipsis txt"><?php echo $module['name'] ?>（版本号：v<?php echo $module['version'];?>）&emsp;&emsp;&emsp;&emsp;<em class="text-main">发现新版本 v1.1</em></span>
							<span class="icon">
								<em class="text-main">简介：</em><?php echo $module['description']; ?>
							</span>
						</div>
					</div>
					<div class="td">
						<span class="td-con double-row">
							<a href="">禁用</a> |
							<a href="">更新</a>
						</span>
					</div>
				</div>
				<?php endforeach ?>
				<div class="paging padding-tb body-bg clearfix">
					<?php echo $pages; ?>
					<div class="clear"></div>
				</div>
			</div>
		</div>
		<script>
			$('.table').resizableColumns();
			$('.paging-table').fixedPaging();
		</script>
	</body>
</html>
