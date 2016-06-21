<?php include template('header','admin');?>
<script type="text/javascript" src="./statics/js/goods/jquery.md5.js" ></script>
<script type="text/javascript" src="./statics/js/template.js" ></script>
<script type="text/javascript" src="./statics/js/goods/goods_add.js" ></script>
<div class="fixed-nav layout">
			<ul>
				<li class="first">商品设置</li>
				<li class="spacer-gray"></li>
				<li>1.填写基本信息</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li class="goods-step">2.设置商品规格</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>3.上传商品图册</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>4.编辑商品类型</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>5.完善商品详情</li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav goods-spec">
		<form action="<?php echo url('goods_add',array('step' => 1,'id' => $_GET['id']))?>" method="POST" name="goods_spec" data-validate='true' data-handkey="goods_spec">
			<div class="margin-tb">
				<a class="button bg-main" href="javascript:;" id="setSpec">编辑规格</a>
				<a class="button margin-left bg-main" href="javascript:;" id="modifySpec">批量修改</a>
				<a class="button margin-left bg-main" href="javascript:;" id="delAll">清空规格</a>
			</div>
			<div class="table resize-table border clearfix">
				<div class="tr border-none spec-name">
					<div class="th" data-width="10">
						<div class="td-con">商品标签</div>
					</div>
					<div class="th" data-width="20">
						<div class="td-con">商品货号</div>
					</div>
					<div class="th" data-width="15">
						<div class="td-con">商品条码</div>
					</div>
					<div class="th" data-width="15">
						<div class="td-con">商品规格</div>
					</div>
					<div class="th" data-width="10">
						<div class="td-con">销售价格</div>
					</div>
					<div class="th" data-width="10">
						<div class="td-con">市场价格</div>
					</div>
					<div class="th" data-width="10">
						<div class="td-con">库存</div>
					</div>
					<div class="th" data-width="10">
						<div class="td-con">操作</div>
					</div>
				</div>
				<input type="hidden" name="spu_sn" value="<?php echo $info['sn']?>">
				<div id="goods_spec"></div>
				<script id="spec_template" type="text/html">
				<%var i = 0 %>
				<%for(var item in templateData){%>
                <%item = templateData[item]%>
				<div class="tr spec-list" data-id='<%=item['spec_md5']%>'>
					<div class="icon td">
						<a class="ico ico_promotion <%if(item['status_ext'] != 1){ %>cancel<% }%>" data-value="1" href="javascript:;" title="点击取消促销"></a>
						<a class="ico ico_special <%if(item['status_ext'] != 2){ %>cancel<% }%>" data-value="2" href="javascript:;" title="点击取消特卖"></a>
						<a class="ico ico_new <%if(item['status_ext'] != 3){ %>cancel<% }%>" data-value="3" href="javascript:;" title="点击取消新品"></a>
						<a class="ico ico_reco <%if(item['status_ext'] != 4){ %>cancel<% }%>" data-value="4" href="javascript:;" title="点击取消推荐"></a>
						<input type="hidden" name="status_ext[<%=item['spec_md5']%>]" value="<%=item['status_ext']%>">
					</div>
					<div class="td">
						<div class="td-con"><input class="input sn" name="sn[<%=item['spec_md5']%>]" type="text" value="<%=item['sn']%>" /></div>
					</div>
					<div class="td">
						<div class="td-con"><input class="input" type="text" name="barcode[<%=item['spec_md5']%>]" value="<%=item['barcode']%>" /></div>
					</div>
					<div class="td spec_info">
						<div class="td-con text-left"><span><%=item['spec_str']%></span></div>
						<input type="hidden" name="spec_str[<%=item['spec_md5']%>]" value="<%=item['spec_str']%>"/>
						<%for(var n in item['spec']){%>
						<%var result = item['spec'][n]%>
						<input type="hidden" class="specs" name="spec[<%=item['spec_md5']%>][]" value='{"id":"<%=result.id%>","name":"<%=result.name%>","value":"<%=result.value%>","style":"<%=result.style%>","color":"<%=result.color%>","img":"<%=result.img%>"}'>
						<%}%>
					</div>
					<div class="td">
						<div class="td-con"><input class="input" type="text" nullmsg="销售价格必须为数字" datatype="price" name="shop_price[<%=item['spec_md5']%>]" value="<%=item['shop_price']?item['shop_price']:'0.00'%>" /></div>
					</div>
					<div class="td">
						<div class="td-con"><input class="input" type="text" nullmsg="市场价格必须为数字" datatype="price" name="market_price[<%=item['spec_md5']%>]" value="<%=item['market_price']?item['market_price']:'0.00'%>" /></div>
					</div>
					<div class="td">
						<div class="td-con"><input class="input" type="text" nullmsg="库存必须为数字" datatype="n" name="number[<%=item['spec_md5']%>]" value="<%=item['number']?item['number']:'0'%>" /></div>
					</div>
					<div class="td">
						<input class="hidden" data-id="<%=item['sku_id']%>" name="sku_id[<%=item['spec_md5']%>]" type="text" value="<%=item['sku_id']%>" />
						<a href="javascript:;" class="delSpec">删除</a>
					</div>
				</div>
				<%i++;%>
				<%}%>
			</script>
			<script id="spec_desc" type="text/html">
				<input type="hidden" name="spec_str[<%=spec%>]" value="<%=spec_str%>"/>
				<%for(var item in spec_desc){%>
				<%var result = spec_desc[item]%>
				<input type="hidden" name="spec[<%=spec%>][]" value='{"id":"<%=result.id%>","name":"<%=result.name%>","value":"<%=result.value%>","style":"<%=result.style%>","color":"<%=result.color%>","img":"<%=result.img%>"}'>
				<%}%>
			</script>
		</div>
			<div class="margin-tb">
				<input type="submit" class="button bg-main" name="dosubmit" id="goods_spec_sub" value="下一步" />
				<a class="button margin-left bg-gray" data-back="false" id="back">上一步</a>
			</div>
		</form>
		<script>
		$(function(){
			$(".table").resizableColumns();
			init_template();
			$(window).resize();
			$('input[name="sn[]"]').eq(0).val($('input[name=spu_sn]').val()+'-1');
			<?php if (isset($info['skus'])): ?>
				var info = <?php echo json_encode($info['skus']) ?> ;
				var goodsRowHtml = template('spec_template', {'templateData': info});
				$('#goods_spec').html(goodsRowHtml); 
			<?php endif ?>
		})
			var url = "<?php echo url('goods_add',array('id'=>$_GET['id'],'step'=>1))?>";
			var back = "<?php echo url('goods_add',array('id'=>$_GET['id']))?>";
			var goods_spec = $("[name=goods_spec]").Validform({
				ajaxPost:true,
				beforeSubmit:function(curform){
					var spec_str = $('[name ^="spec_str"]').serialize();
					$('[name ^="spec_str"]').remove();
					$(curform).append('<input type="hidden" name="str_spec" value="' + spec_str + '">');
					
					var sn = $('[name^="sn"]').serialize();
					$('[name^="sn"]').remove();
					$(curform).append('<input type="hidden" name="sn" value="' + sn + '">');
					
					var barcode = $('[name^="barcode"]').serialize();
					$('[name^="barcode"]').remove();
					$(curform).append('<input type="hidden" name="barcode" value="' + barcode + '">');

					var status_ext = $('[name^="status_ext"]').serialize();
					$('[name^="status_ext"]').remove();
					$(curform).append('<input type="hidden" name="status_ext" value="' + status_ext + '">');

					var shop_price = $('[name^="shop_price"]').serialize();
					$('[name^="shop_price"]').remove();
					$(curform).append('<input type="hidden" name="shop_price" value="' + shop_price + '">');

					var market_price = $('[name^="market_price"]').serialize();
					$('[name^="market_price"]').remove();
					$(curform).append('<input type="hidden" name="market_price" value="' + market_price + '">');

					var number = $('[name^="number"]').serialize();
					$('[name^="number"]').remove();
					$(curform).append('<input type="hidden" name="number" value="' + number + '">');

					var sku_id = $('[name^="sku_id"]').serialize();
					$('[name^="sku_id"]').remove();
					$(curform).append('<input type="hidden" name="sku_id" value="' + sku_id + '">');

					var spec = $('[name^=spec]').serialize();
					$('[name^=spec]').remove();
					$(curform).append('<input type="hidden" name="spec" value="' + spec + '">');

				},
				callback:function(result) {
					if(result.status == 1){
						var nexturl = "<?php echo url('goods_add',array('id'=>$_GET['id'],'step'=>2))?>";
						window.location = nexturl;
					}else{
						alert(result.message);
					}
				}
			});
			$('#back').bind('click',function(){
				var data = submitBefore();
				$.post(url,data,function(ret){
					if(ret.status == 1){
						var nexturl = "<?php echo url('goods_add',array('id'=>$_GET['id']))?>";
						window.location = nexturl;
					}else{
						alert(result.message);
					}
				},'json')
			});
			/*function auto_submit() {
				var data = submitBefore();
				$.post(url,data,function(ret){},'json');
			}
			setInterval("auto_submit()",30000);*/
			function submitBefore(){
				var spec_str = $('[name ^="spec_str"]').serialize();
				var sn = $('[name^="sn"]').serialize();
				var barcode = $('[name^="barcode"]').serialize();
				var status_ext = $('[name^="status_ext"]').serialize();
				var shop_price = $('[name^="shop_price"]').serialize();
				var market_price = $('[name^="market_price"]').serialize();
				var number = $('[name^="number"]').serialize();
				var sku_id = $('[name^="sku_id"]').serialize();
				var spec = $('.specs').serialize();
				var data = {str_spec:spec_str,sn:sn,barcode:barcode,status_ext:status_ext,shop_price:shop_price,market_price:market_price,number:number,sku_id:sku_id,spec:spec};
				return data;
			}
			var selectedItem = <?php echo $info['extra'] ? $info['extra'] : '[]';?> ;
			var defaultProductNo = "<?php echo 'NU'.time().rand(10,99)?>";
			if($('input[name="spu_sn"]').val() == ''){
				$('input[name="sn[]"]').eq(0).val(defaultProductNo+'-1');
				$('input[name=spu_sn]').val(defaultProductNo);
			}
			var	productNo = $('input[name="spu_sn"]').val();
			var n = 1;
			$('.table .tr:last-child').addClass("border-none");
			 $(".icon a.ico").live('click',function(){
		     	if(!$(this).hasClass("cancel")){
		     		$(this).addClass("cancel");
		     	}else{
		     		$(this).removeClass("cancel").siblings(".ico").addClass("cancel");
		     		$(this).parent().find('input').val($(this).attr('data-value'));
		     	}
		     });
			function init_template() {
				var goodsRowHtml = template('spec_template', {'templateData': [[]]});
				$('#goods_spec').html(goodsRowHtml); 
			}
			$('#setSpec').live('click',function(){
				var url = "<?php echo url('goods_spec_pop',array(id=>$_GET['id']))?>";
				var productNu = selectedItem ? productNo+'-1' :$('input[name="sn[]"]').eq(0).val();
				top.dialog({
					url: url,
					title: '加载中...',
					width: 681,
					data : selectedItem,
					onclose: function () {
						if(this.returnValue){
							var	addSpecObject = this.returnValue;
							//开始遍历规格
							var specValueData = {}
							var specData = {};
							var selectedNewItem = [];
							var selectType = 0;
							addSpecObject.each(function() {
								if ($(this).hasClass('new-prop') == true) {	// 如果是全选则排除添加属性这个<li>
									return true;
								}
								var data_id = $(this).attr('data-id');
								var data_name = $(this).attr('data-name');
								var data_value = $(this).attr('data-value');
								var data_style = $(this).attr('data-style');
								var data_color = $(this).attr('data-color');
								var data_img = $(this).attr('data-img');
								selectType = $(this).attr('data-type') ? $(this).attr('data-type') : 0;
								if (typeof(specValueData[data_id]) == 'undefined') {
									specValueData[data_id] = [];
								}
								specValueData[data_id].push({
									'value':data_value,
									'img':data_img,
									'color':data_color
								});
								specData[data_id] = {
									'id': data_id,
									'name': data_name,
									'style': data_style,
								};
								selectedNewItem.push({
									'id': data_id,
									'value': data_value,
									'color': data_color,
									'img':data_img,
									'style':data_style
								});
							});
							selectedItem = selectedNewItem;
							//生成货品的笛卡尔积
							var specMaxData = descartes(specValueData, specData);
							//从表单中获取默认商品数据
							var productJson = {};
							productJson['sn'] = productNu;
							//获取当前页面货号最大值
							var sn_num = [],
								sn_length = $('.sn').length;
							$('.sn').each(function(i,item){
								var num = $(item).val().split('-');
								sn_num.push(num[1]);
							})
							var max_sn_num = Math.max.apply(null, sn_num);
							//生成最终的货品数据
							var productList = [];
							for (var i = 0; i < specMaxData.length; i++) {
								var productItem = {};
								productItem['spec_array'] = specMaxData[i];
								for (var index in productJson) {
									//自动组建货品
									if (index == 'sn') {
										//值为空时设置默认货号
										if (productJson[index] == '') {
											productJson[index] = productNo;
										}
										if (productJson[index].match(/(?:\-\d*)$/) == null) {
											//正常货号生成
											productItem['sn'] = productJson[index] + '-' + (i + 1);
										}else{
											//货号已经存在则替换
											productItem['sn'] = productJson[index].replace(/(?:\-\d*)$/, '-' + (i + 1));
										}
									} else {
										productItem[index] = productJson[index];
									}
								}
								productList.push(productItem);
							}
							var selectArr = [];
							$.each(productList,function(i,item){
								var spec = spec_string  = specJson = '';
								for(var j = 0;j < (item.spec_array.length);j++){
									spec_string += item.spec_array[j].name + ':' +item.spec_array[j].value +' ';
									spec_md5 = $.md5(spec_string);
								}
								productList[i]['spec'] = item.spec_array;
								productList[i]['spec_str'] = spec_string;
								productList[i]['spec_md5'] = spec_md5;
								selectArr.push(spec_md5);
							});
							if(selectType == 1){
								if(productList.length != 0){
									init_template();
								}
								var goodsRowHtml = template('spec_template', {'templateData': productList});
								$('#goods_spec').html(goodsRowHtml); 
							}else{
								var specArr = [];
								$('.spec-list').each(function(){
									specArr.push($(this).attr('data-id'));
								})
								var select_diff = chaji_array(selectArr,specArr);
								var spec_diff = chaji_array(specArr,selectArr);
								if(spec_diff.length == 0 && select_diff.length == 0){
									$.each(productList,function(i,item){
									var spec_desc = template('spec_desc',{'spec_desc':item.spec,'spec':item.spec_md5,'spec_str':item.spec_str});
									$('.tr[data-id="'+ item.spec_md5 +'"]').find('.spec_info').find('input').remove();
									$('.tr[data-id="'+ item.spec_md5 +'"]').find('.spec_info').append(spec_desc);
									})
								}
								//新增
								if(select_diff.length > 0){
									var speclist = [];
									var t = max_sn_num + 1;
									$.each(productList,function(i,item){
										var spec = specJson = '';
										for(var j = 0;j < (item.spec_array.length);j++){
											spec += item.spec_array[j].name + ':' +item.spec_array[j].value +' ';
											spec_md5 = $.md5(spec);
										}
										if($.inArray(spec_md5, select_diff) > -1){
											productList[i].sn = productNo + '-' + t;
											speclist.push(productList[i]);
											t++;
										}
									});
									var goodsRowHtml = template('spec_template', {'templateData': speclist});
									$('#goods_spec').append(goodsRowHtml); 
								}
								//删减
								if(spec_diff.length > 0){
									$.each(spec_diff,function(i,item){
										var del_id = $("div[data-id='"+ item +"']").find('input[name="_sku_ids[]"]').val();
										$('.table').append('<input type="hidden" name="del_sku_ids[]" value="'+del_id+'">');
										$("div[data-id='"+ item +"']").remove();
									})
								}
							}
							$(window).resize();
 						}
					}
				})
				.showModal();
			});
			
			$("#modifySpec").live('click',function(){
				top.dialog({
					url: "<?php echo url('goods_spec_modify')?>",
					title: '加载中...',
					width: 400,
					data : $('[name=spu_sn]').val(),
					onclose: function(){
						if(this.returnValue){
							_sn 		=  this.returnValue[0];
							_shop_price_change 		= this.returnValue[1];
		    				_market_price_change 	= this.returnValue[2];
		    				_goods_number_change 	= this.returnValue[3];
		    				var num_reg = /^[-\+]?\d*$/;
		    				var price_reg = /^[-\+]?\d+(\.\d{2})?$/;
		    				if(!(num_reg.test(_goods_number_change)) || !(price_reg.test(_shop_price_change)) || !(price_reg.test(_market_price_change))){
		    					alert('请输入正确的数字!');
		    					return false;
		    				}
		    				//货号
		    				var i = 1;
		    				$('[name=spu_sn]').val(_sn);
		    				$('[name^="sn"]').each(function(index,data){
		    					sn = _sn + '-' + i;
		    					i++;
		    					$(this).val(sn);
		    				})
		    				//销售价
		    				$('[name^="shop_price"]').each(function(index,data){
		    					num = Number($(this).val()) + Number(_shop_price_change);
		    					num = num < 0 ? 0 : num;
		    					$(this).val(num.toFixed(2));
		    				})
		    				//市场价
		    				$('[name^="market_price"]').each(function(index,data){
		    					num = Number($(this).val()) + Number(_market_price_change);
		    					num = num < 0 ? 0 : num;
		    					$(this).val(num.toFixed(2));
		    				})
		    				//库存
		    				$('[name^="number"]').each(function(index,data){
		    					num = parseInt($(this).val()) + parseInt(_goods_number_change);
		    					num = num < 0 ? 0 : num;
		    					$(this).val(num);
		    				})
						}
					}
				}).showModal();
			});
			
			function chaji_array(arr1,arr2){
				var arr3 = [];
				for (var i = 0; i < arr1.length; i++) {
			        var flag = true;
			        for (var j = 0; j < arr2.length; j++) {
			            if (arr2[j] == arr1[i]) {
			                flag = false;
			            }
			        }
			    	if (flag) {
			            arr3.push(arr1[i]);
			        }
			    }
				return arr3;
			}
			//笛卡儿积组合
			function descartes(list, specData) {
				//parent上一级索引;count指针计数
				var point = {};
				var result = [];
				var pIndex = null;
				var tempCount = 0;
				var temp = [];
				//根据参数列生成指针对象
				for (var index in list) {
					if (typeof list[index] == 'object') {
						point[index] = {
							'parent': pIndex,
							'count': 0
						}
						pIndex = index;
					}
				}
				//单维度数据结构直接返回
				if (pIndex == null) {
					return list;
				}
				//动态生成笛卡尔积
				while (true) {
					for (var index in list) {
						tempCount = point[index]['count'];
						temp.push({
							"id": specData[index].id,
							"name": specData[index].name,
							"style":specData[index].style,
							"color":list[index][tempCount].color,
							"img":list[index][tempCount].img,
							"value": list[index][tempCount].value
						});
					}
					//压入结果数组
					result.push(temp);
					temp = [];
					//检查指针最大值问题
					while (true) {
						if (point[index]['count'] + 1 >= list[index].length) {
							point[index]['count'] = 0;
							pIndex = point[index]['parent'];
							if (pIndex == null) {
								return result;
							}

							//赋值parent进行再次检查
							index = pIndex;
						} else {
							point[index]['count'] ++;
							break;
						}
					}
				}
			}
			$('.delSpec').live('click',function(){
				$(this).parent().parent('.tr').remove();
			})
			function delAll(){
				init_template();
				$('input[name="sn[]"]').eq(0).val($('input[name=spu_sn]').val()+'-1');
				selectedItem = [];
				$(window).resize();
			}
			$('#delAll').live('click',function(){
				$('.table').children('.spec-list').remove();
				delAll();
			})
		</script>
	</body>
</html>
