$(function(){
	$('.content').height( $(window).height() - $('.top-tools').height() );
	$("#info-tab").datagrid({
		fit:true,
		fitColumns:true,
		singleSelect : true
	})

})