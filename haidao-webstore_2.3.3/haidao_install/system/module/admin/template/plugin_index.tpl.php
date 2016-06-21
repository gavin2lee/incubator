<?php include template('header','admin');?>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">插件管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
				<li><a class="current" href="javascript:;">已启用插件</a></li>
				<li><a href="javascript:;">未启用插件</a></li>
				<li><a href="javascript:;">未安装插件</a></li>
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
					<span class="th" data-width="75">
						<span class="td-con">插件名称</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">作者</span>
					</span>
					<span class="th" data-width="15">
						<span class="td-con">操作</span>
					</span>
				</div>
				<div class="tr">
					<div class="td">
						<div class="td-con td-pic text-left">
							<span class="pic"><img src="./statics/images/default_no_upload.png" /></span>
							<span class="title text-ellipsis txt">微信登录（版本号：v1.0）&emsp;&emsp;&emsp;&emsp;<em class="text-main">发现新版本 v1.1</em></span>
							<span class="icon">
								<em class="text-main">简介：</em>通过微信直接登录即可
							</span>
						</div>
					</div>
					<div class="td">
						<span class="td-con">admin</span>
					</div>
					<div class="td">
						<span class="td-con double-row">
							<a href="">管理</a><br />
							<a href="">关闭</a>&nbsp;&nbsp;&nbsp;<a href="">更新</a>
						</span>
					</div>
				</div>
				<div class="tr">
					<div class="td">
						<div class="td-con td-pic text-left">
							<span class="pic"><img src="./statics/images/default_no_upload.png" /></span>
							<span class="title text-ellipsis txt">微信登录（版本号：v1.0）&emsp;&emsp;&emsp;&emsp;<em class="text-main">发现新版本 v1.1</em></span>
							<span class="icon">
								<em class="text-main">简介：</em>通过微信直接登录即可
							</span>
						</div>
					</div>
					<div class="td">
						<span class="td-con">admin</span>
					</div>
					<div class="td">
						<span class="td-con double-row">
							<a href="">管理</a><br />
							<a href="">关闭</a>&nbsp;&nbsp;&nbsp;<a href="">更新</a>
						</span>
					</div>
				</div>
				<div class="tr">
					<div class="td">
						<div class="td-con td-pic text-left">
							<span class="pic"><img src="./statics/images/default_no_upload.png" /></span>
							<span class="title text-ellipsis txt">微信登录（版本号：v1.0）&emsp;&emsp;&emsp;&emsp;<em class="text-main">发现新版本 v1.1</em></span>
							<span class="icon">
								<em class="text-main">简介：</em>通过微信直接登录即可
							</span>
						</div>
					</div>
					<div class="td">
						<span class="td-con">admin</span>
					</div>
					<div class="td">
						<span class="td-con double-row">
							<a href="">管理</a><br />
							<a href="">关闭</a>&nbsp;&nbsp;&nbsp;<a href="">更新</a>
						</span>
					</div>
				</div>
				<div class="paging padding-tb body-bg clearfix">
					<ul class="fr">
						<li>共22条数据</li>
						<li class="spacer-gray margin-lr"></li>
						<li>每页显示<input class="input radius-none" type="" name="" id="" value="5" />条</li>
						<li class="spacer-gray margin-left"></li>
						<li class="default-start"></li>
						<li class="default-prev"></li>
						<li class="start"><a href=""></a></li>
						<li class="prev"><a href=""></a></li>
						<li class="current">1</li>
						<li><a href="">2</a></li>
						<li><a href="">3</a></li>
						<li><a href="">4</a></li>
						<li><a href="">5</a></li>
						<li class="next"><a href=""></a></li>
						<li class="end"><a href=""></a></li>
						<li class="default-next"></li>
						<li class="default-end"></li>
					</ul>
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
