<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/simple.jsp">
	<template:replace name="body">
		<c:import url="/common/include/header.jsp"></c:import>
		<!------APP部署内容------->
		<div class="app_content">
		    <div class="content_frame"> 
		        <template:block name="content"></template:block> 
		    </div>
		</div>
		<!------APP部署内容------->
		<c:import url="/common/include/footer.jsp"></c:import>
	</template:replace>
	<template:replace name="bottom">
		<script type="text/javascript">
		    function adjustHeight() {
		        var h = $(window).height(); 
		     
		       
		        
		        var hh = $(".head").height();
		        var conH = h - hh - 1;
		        $(".content_frame").css("height", conH);
		        $(".left_sub_menu").css("height", conH);
		        $(".right_body").css("height", conH);
				
		        if($(".app-search").size()==0){
		        	
		        }
		        if($(".plate").length>0){
		        	if($(".app-search").size()==0){
		        		$(".plate").css("height", conH - $(".app_name").height());
		        	}
		        	else{
		        		 var h3 = $(".app-search").height();
		 		        $(".plate").css("height", conH - h3 - 10);
		        	}
		        }
		    }
		    $(document).ready(function () {
		    	adjustHeight();
		    });
		    $(window).resize(function () {
		        adjustHeight();
		    });
		    function removeSelect(ele){
		    	$(ele).parent('.tag').remove();
		    }
		</script>
	</template:replace>
</template:include>
