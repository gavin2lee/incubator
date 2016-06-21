/**
 * 后台添加商品js
 */
function save_back(url,back){
	$('#back').bind('click',function(){
		var data = $('form').serialize();
		$.post(url,data,function(ret){
			if(ret.status == 1){
				window.location = back;
			}
		},'json');
	})
}
function auto_save(url){
	var data = $('form').serialize();
	$.post(url,data,function(ret){},'json');
}