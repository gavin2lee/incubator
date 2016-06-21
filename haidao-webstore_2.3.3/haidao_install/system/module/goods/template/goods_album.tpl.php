<?php include template('header','admin');?>
<link type="text/css" rel="stylesheet" href="./statics/js/upload/uploader.css" />
<script type="text/javascript" src="./statics/js/upload/uploader.js"></script>
<script type="text/javascript" src="./statics/js/goods/goods_add.js" ></script>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">商品设置</li>
				<li class="spacer-gray"></li>
				<li>1.填写基本信息</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>2.设置商品规格</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li class="goods-step">3.上传商品图册</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>4.编辑商品类型</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>5.完善商品详情</li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav">
		<form action="<?php echo url('goods_add',array('step' => 2,'id' => $_GET['id']))?>" method="POST" name="goods_album" data-validate='true' data-handkey="goods_spec">
			<div class="atlas-wrap">
				<div class="upload-pic-wrap border bg-white margin-big-top">
					<div class="title border-bottom bg-gray-white text-default">
						<b>默认图片（如商品多个规格图片一致，仅需上传默认图片；多个规格图片不一致，默认图片可不传）</b>
					</div>
					<div class="upload-pic-content clearfix">
					<?php if(!empty($info['spu_imgs'])){?>
					<?php foreach ($info['spu_imgs'] AS $url) {?>
						<div class="box">
							<img src="<?php echo $url?>" />
							<div class="operate">
								<i>×</i>
								<a href="javascript:;">默认主图</a>
								<input type="hidden" data-name="0" name="goodsphoto[0][]" value="<?php echo $url?>"/>
							</div>
						</div>
					<?php }?>
					<?php }?>
					<div class="loadpic" >
							<label class="load-button" data-id="0" id="upload_0"></label>
						</div>
					</div>
				</div>
				<?php if(!empty($info['extra']['specs'])){?>
				<div class="margin-big-top padding-top padding-left padding-right border border-gray border-dashed spec-right-body bg-white">
					<div class="wrap padding-none clearfix">
					<?php foreach ($info['extra']['specs'] as $k => $spec) {?>
						<a <?php if($k == $info['spec_id']){?>class="current"<?php }?> data-id="<?php echo $k?>" href="javascript:;"><?php echo $spec['name']?></a>
						<input type="hidden" name="spec_id" value="<?php echo $info['spec_id']?>" />
					<?php }?>
					</div>
				</div>
				<?php }?>
				<?php foreach ($info['extra']['specs'] as $key => $value) {?>
				<?php foreach ($value['value'] as $k => $spec) {?>
				<div class="upload-pic-wrap border bg-white margin-big-top <?php if($key != $info['spec_id']){?>hidden<?php }?> spec_atlas" data-id="<?php echo $key?>" data-md="<?php echo $value['spec_md5'][$k]?>">
					<div class="title border-bottom bg-gray-white text-default">
						<b>商品SKU规格：<?php echo $spec?></b>
					</div>
					<div class="upload-pic-content clearfix">
						<?php if(!empty($info['skus']['imgs'])){?>
						<?php foreach ($info['skus']['imgs'][$value['spec_md5'][$k]] AS $url) {?>
						<div class="box">
							<img src="<?php echo $url?>" />
							<div class="operate">
								<i>×</i>
								<a href="javascript:;">默认主图</a>
							</div>
							<input type="hidden" data-name="<?php echo $value['spec_md5'][$k]?>" name="goodsphoto[<?php echo $value['spec_md5'][$k]?>][]" value="<?php echo $url?>"/>
						</div>
						<?php }?>
						<?php }?>
						<div class="loadpic">
							<label class="load-button" data-id="<?php echo $value['spec_md5'][$k]?>" id="upload_<?php echo $value['spec_md5'][$k]?>"></label>
						</div>
					</div>
				</div>
				<?php }?>
				<?php }?>
			</div>
			<div class="margin-big-top">
				<input type="submit" class="button bg-main" name="dosubmit" id="goods_atlas" value="下一步" />
				<a class="button margin-left bg-gray" data-back="false" id="back">上一步</a>
			</div>
		</form>
		</div>
		<script>
			//点击
			var goods_album = $("[name=goods_album]").Validform({
				ajaxPost:true,
				callback:function(result) {
					if(result.status == 1){
						var nexturl = "<?php echo url('goods_add',array('step' => 3,'id' => $_GET['id']))?>";
						window.location = nexturl;
					}else{
						alert(result.message);
					}
				}
			});
			var url = "<?php echo url('goods_add',array('id'=>$_GET['id'],'step'=>2))?>";
			var back = "<?php echo url('goods_add',array('id'=>$_GET['id'],'step'=>1))?>";
			save_back(url,back);
			setInterval("auto_save(url)",30000);
			var _spec =  <?php echo $info['spec_id'] ? $info['spec_id'] : ($goods['spec_id'] ? $goods['spec_id'] : 0)?>;
			$(".spec-right-body a").click(function(){
				var $id = parseInt($(this).data('id'));
				if($(this).hasClass("current")){
					$(this).removeClass('current');
					$(".spec_atlas").addClass("hidden");
					$(this).parent().find('input').remove();
				}else{
					if(_spec == 0){
						_spec = $id;
					}
					if(_spec != $id){
						if(confirm("更改规格将清空该规格已上传的图片，是否确认更改？")){
							$(this).addClass('current').siblings().removeClass('current');
							var _input = '<input type="hidden" name="spec_id" value="' + $(this).data('id') + '"/>'
							$(this).parent().find('input').remove();
							$(this).parent().append(_input);
							$(".spec_atlas").each(function(){
								if(parseInt($(this).data("id")) == $id){
									$(this).removeClass("hidden");
								}else{
									$(this).addClass("hidden");
									$(this).find('.box').remove();
								}
							})
							_spec = $id;
						}
					}else{
						$(this).addClass('current').siblings().removeClass('current');
							var _input = '<input type="hidden" name="spec_id" value="' + $(this).data('id') + '"/>'
							$(this).parent().find('input').remove();
							$(this).parent().append(_input);
							$(".spec_atlas").each(function(){
								if(parseInt($(this).data("id")) == $id){
									$(this).removeClass("hidden");
								}else{
									$(this).addClass("hidden");
								}
							})
					}
				}
			})
			
			$(".box").live('mouseover',function(){
				$(this).children('.operate').show();
			}).live('mouseout',function(){
				$(this).children('.operate').hide();
			});
			
			$('.operate a').live('click',function(){
				if($(this).parents(".upload-pic-content").find('.box').length > 1 && !$(this).parents(".box").hasClass("set")){
					$(this).parents(".upload-pic-content").find('.box:first').before($(this).parents(".box"));
				}
				$(this).parents(".box").addClass('set').siblings().removeClass('set');
			});
			
			$('.operate i').live('click',function(){
				$(this).parents('.box').remove();
			});
			/*上传图片*/
			var imgs = $('.atlas-wrap').find('.load-button');
			var $progress = $('.upload-pic-content').find('.loading').hide();
			$.each($('.load-button'),function(index,val){
				var i = $(val).attr('data-id');
				var uploader = WebUploader.create({
			        auto:true,
			        fileVal:'upfile',
			        // swf文件路径
			        swf: './statics/js/upload/uploader.swf',
			        // 文件接收服务端。
			        server: "<?php echo url('upload')?>",
			        // 选择文件的按钮。可选
			        formData:{
			            img_id : i,
			            code : '<?php echo $info["extra"]["attachment_init"]; ?>'
			        },
			        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			        pick: {
			            id: '#upload_' + i,
			        },
			        accept:{
			            title: '图片文件',
			            extensions: 'jpg,jpeg,bmp,png',
			            mimeTypes: 'image/*'
			        },
			        thumb:{
			            width: '110',
			            height: '110'
			        },
			        chunked: false,
			        chunkSize:1000000,
			        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
			        resize: false
			    });
			   uploader.onUploadSuccess = function( file, response ) {
			   		$('#'+file.id).find('.loading').hide();
			   		var pickid = this.options.pick.id;
			    	var obj = eval("(" + response._raw + ")")
			        var result = obj.result;
			        if(result.url.length > 0) {
			        	var html =  '<img src="'+ result.url +'" />'
							+		'<div class="operate">'
							+		'<i>×</i>'
							+		'<a href="javascript:;">默认主图</a>'
							+		'</div>'
							+		'<input type="hidden" data-name="' + result.img_id + '" name="goodsphoto['+ result.img_id +'][]" value="'+ result.url +'"/>';
						$('#'+file.id).append(html);
			        }
			    }
			    uploader.onUploadError = function(file, reason) {
			        alert(reason);
			    }
			    uploader.onError = function( code ) {
			    	if(code == 'Q_TYPE_DENIED'){
			    		alert('图片类型被禁止！');
			    	}else if(code == 'Q_EXCEED_SIZE_LIMIT'){
			    		alert('图片大小超过限制！');
			    	}else{
		            	alert( '图片已在列表，请勿重复上传！');
			    	}
		        };
		        uploader.onUploadProgress = function(file, percentage) {
		        	
                };
		        uploader.onFileQueued = function(file) {
		        	var pickid = this.options.pick.id;
		        	var html = 		'<div class="box" id="' + file.id + '">'
		        			 +		'<div class="loading">'
							 +		'<em>上传中...</em>'
							 +		'<span></span>'
							 +		'</div>';
		        			 +		'</div>';
		            $(pickid).parent().before(html);
		        };
			})
		</script>
	</body>
</html>
