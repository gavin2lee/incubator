<%@ page language="java" pageEncoding="UTF-8"%>
<c:if test="${not empty param.dialog && param.dialog  }">
	<script>
			$(function(){
				var context = $("#"+listContainerId);
				context.before('<a class="btn btn-default btn-oranger f16 mr20" href="javascript:openResourceAddDialog(\'mysql\');">创建</a>');
				$("th:last",context).remove();
				$("tr",context).each(function(){
					$(this).find("td:last").remove();
				})
				__CurTr = {id:"",name:"",env:[]};
				$("tr:gt(0)",context).click(function(){
					$("tr.cur",context).css("background","white").removeClass("cur");
					$(this).css("background","orange").addClass("cur");
					var curTr = $(this);
					var id = curTr.attr("id");
					var allocateContent = $("#allocateContent_"+id).html();
					if(typeof allocateContent === 'string'){
						allocateContent = $.parseJSON(allocateContent);
					}
					__CurTr = {
						name : curTr.attr("data-name"),
						id : id,
						env : setEnv(allocateContent)
					}
				})
			})
			</script>
</c:if>