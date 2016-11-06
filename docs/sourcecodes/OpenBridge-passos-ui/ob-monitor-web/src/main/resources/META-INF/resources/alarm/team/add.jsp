<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="team">
	<template:replace name="title">
		用户组管理
	</template:replace>
	<template:replace name="content-body">
<!------passos内容------->
		<style>
			.form_block input,.form_block select{
				width:385px;
			}
			.form_block textarea{
				width:485px;
				height: 100px;
			}
		</style>
        <div class="app_name">
            <a href="${WEB_APP_PATH }"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="../group/index">告警设置</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../team/list">用户组</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="#nogo">${not empty param.id ? '编辑' : '添加'}用户组</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
                <div class="r_block p20">
                    <div class="r_block">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line">${not empty param.id ? '编辑' : '<i class="icons add_ico_blue mr5"></i>添加'}用户组</h3>

                            <div class="title_line"></div>
                        </div>
                        <div class="r_con p10_0">
                            <div class="form_control p20">
                            <form id="createForm">
                            	<input type="hidden" id="id" value="${team.id }"/>
                                <div class="form_block">
                                    <label>名称</label>
                                    <input type="text" id="name" name="name" style="width: 374px;" value="${team.name }"><em style="color:red" >*</em>
                                </div>
                                <div class="form_block">
                                    <label>简介</label>
                                    <textarea id="resume" name="resume" style="width: 374px;">${team.resume }</textarea>
                                </div>
                                <div class="form_block">
                                    <label>成员</label>
                                    <input type="hidden" name="memIds" id="memIds" value="<c:forEach items="${team.member }" var="mem"
										varStatus="status">${mem.userId }<c:if test="${!status.last }">;</c:if></c:forEach>"/>

                                    <div id="memNames" name="memNames" style="width: 324px; height: 70px; border: 1px solid #eaeaea; display: inline-block; background-color: white; padding: 3px 5px;">
									<c:forEach items="${team.member }" var="mem"
										varStatus="status">
										${mem.userName }
										<c:if test="${!status.last }">;</c:if>
									</c:forEach>
									</div><button onclick="selectUser()" type="button" style="padding: 3px 5px;width:50px; line-height: 15px;cursor: pointer;display: inline-block; vertical-align: top; height: 77px;" class="btn btn-default btn-yellow2">选择
                                    </button>
                                    <em style="color:red">*</em>
                                </div>
                                <div class="form_block mt10">
                                    <label>&nbsp;</label>
                                    <a javascript:void(0); class="btn btn-default btn-yellow f16  mt10" id="submitBtn"><i class="ico_check"></i>提交
                                    </a>
                                </div>
                            </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
        function selectUser(){
	    	var params = {};
	    	params.query="all";
	    	params.userIds = $("#memIds").val();
	    	params.userNames = $("#memNames").html();
	    	dialogUser(true,'memIds','memNames',function(){
	    		var userIds = $("#memIds").val().split(";");
		    	var userNames = $("#memNames").html().split(";");
		    	var str = '<option>--请选择--</option>';
		    	$.each(userIds,function(i,v){
		    		str += "<option value='"+v+"'>"+userNames[i]+"</option>";
		    	});
	    		$("#admin").html(str);
	    	},params);
	    }

        function dialogUser(multiple,valId,textId,cb,params){
        	var url = WEB_APP_PATH+"/sys/user/dialog.do?query="+params.query+"&multiple="+multiple;
        	var dialog = top.common.dialogIframe("请选择用户",url,650,600,null,['确定','取消'],{
        		btn1 :function(index,layero){
        			var iwin = layero.find("iframe").get(0).contentWindow;
                	if(iwin.dialogClose && typeof(iwin.dialogClose) === 'function'){
                		var obj = (iwin.dialogClose());
                		if(obj!=null&&obj.ids!=null){
            				var idObj = $("#"+valId);
            				var textObj = $("#"+textId);
            				if(multiple){
            					idObj.val(obj.ids.join(";")); 
            					if(textObj.is(":input")){
            						textObj.val(obj.texts.join(";"));
            					}else{
            						textObj.html(obj.texts.join(";"));
            					}
            				}else{
        	    				for(var i=0;i<obj.ids.length;i++){ 
        	    					if(i==obj.ids.length-1){
        	        					idObj.val(obj.ids[i]); 
        	        					if(textObj.is(":input")){
        	        						textObj.val(obj.texts[i]);
        	        					}else{
        	        						textObj.html(obj.texts[i]);
        	        					}
        	    					}
        	        			}
            				}
                		}
                	}
                	if(cb!=null)
                		cb();
        		},
        		btn2 :function(index,layero){
        			
        		}
        	}); 
        	dialog.getIFrame().params = params;
        } 
        	function validate(){
        		if(!$("#name").val()){
        			common.alert("请输入用户组名称!");
        			return false;
        		}
        		/* if(!$("#memIds").val()){
        			common.alert("请选择用户组成员!");
        			return false;
        		} */
        		return true;
        	}
        	
        	$("#submitBtn").click(function(){
        		if(!validate()){
        			return;
        		}
        		var member = [];
        		$.each($("#memIds").val().split(";"),function(i,v){
        			member.push({
        				userId : v
        			});
        		});
        		var param = {
           			id : $("#id").val(),
           			name : $("#name").val(),
           			resume : $("#resume").val(),
           			member : member
           		};
       			var option = {
					url : "${ WEB_APP_PATH }/teams/saveOrUpdate",
					data : {
						'json' : JSON.stringify(param)
					},
					type : 'POST',
					cache : false,
					dataType : 'json'
       			};
       			var ref = $.ajax(option);
       			ref['done'](function(data){
       				if(data.code==1){
           			common.alert("保存失败！"+(data.msg||''),2);
           		}else{
           			common.tips("保存成功！",1,function(){
       					location.href = "${ WEB_APP_PATH }/teams";
       						},2000);
       				  }
   				});		
        	});
        	
        </script>
	</template:replace>
</template:include>

