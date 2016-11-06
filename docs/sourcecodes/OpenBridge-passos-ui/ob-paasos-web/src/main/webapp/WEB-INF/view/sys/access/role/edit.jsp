<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
			.form_block {
				padding: 5px;
			}
			.form_block label {
				width: 100px;
			}	
			
			.tag{
				    display: inline-block;
				    position: relative;
				    font-size: 13px;
				    font-weight: normal;
				    vertical-align: baseline;
				    white-space: nowrap;
				    background-color: #91b8d0;
				    color: #FFF;
				    text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15);
				    padding: 4px 17px 5px 6px;
				    margin-bottom: 2px;
				    margin-right: 2px;
				    -webkit-transition: all 0.2s;
				    -o-transition: all 0.2s;
				    transition: all 0.2s;
			}
			.tags {
			    display: inline-block;
			    padding: 4px 6px 2px 6px;
			    color: #777777;
			    vertical-align: middle;
			    background-color: #FFF;
			    border: 1px solid #d5d5d5;
			}
			.btn-choose {
			    border-top-left-radius: 0px;
			    border-bottom-left-radius: 0px;
			    border: 1px solid #ccc;
			    border-left: 0px;
			    background-color: #f2f2f2;
			}
			
			.tags .tag .close {
			    font-size: 15px;
			    opacity: 1;
			    filter: alpha(opacity = 100);
			    color: #FFF;
			    text-shadow: none;
			    float: none;
			    position: absolute;
			    right: 0;
			    top: 0;
			    width: 18px;
			    text-align: center;
			}
			
				button.close {
			    padding: 0;
			    cursor: pointer;
			    background: transparent;
			    border: 0;
			    -webkit-appearance: none;
			}
		</style> 
		<form action="${WEB_APP_PATH}/sys/access/role/update.do" method="post" id="editRoleForm">
		<div class="r_block">
	        <div class="r_con p10_0">
	            <div class="form_control p20">
	                <div class="form_block">
	                    <label>角色名称</label>
	                    <input type="hidden" name="roleId" value="${ role.roleId }" />
	                    <input type="text" name="roleName" id="roleName" value="${ role.roleName }" style="width: 380px;" placeholder=" "><font style="color: red;">&nbsp;&nbsp;*</font>
	                </div>
	                <div class="form_block">
	                    <label>角色描述</label>
	                    <textarea rows="3" cols="60" name="roleDesc" id="roleDesc" style="width: 380px;height: 100px;">${ role.roleDesc }</textarea>
	                </div>
	                <div class="form_block">
	                	<div style="margin-top:20px;">
	                     	<label style="float:left;">包含人员</label>		
	                     	<div style="float:left;position: relative;width:400px;">			
	                     				
	                     		<div style="width: 80%;min-width: 100px;min-height:120px;border:solid 1px #ccc;" class="col-xs-10 col-sm-5 tags" id="user_Names"> 
	                     			<c:if test="${! empty role.users }">
	                     				<c:forEach items="${role.users }"  var="user">
					                     	<span class="tag" user-nid="${user.userId }" user-uid="${user.userName }">${user['userName'] }<button type="button" class="close" onclick="removeUser(this)">×</button></span>
					                     </c:forEach>
	                     			</c:if>
	                     		</div>
	                     		<button type="button" onclick="selectUser()" style="position:absolute; bottom:0px;top:0px;" class="btn btn-default btn-sm btn-yellow2">选择</button>
	                     	</div>
	                     	<div style="clear:both;"></div>
	                    </div>
	                	
	                   
	                   
	                </div> 
	                <div class="form_block mt10">
	                    <label>&nbsp;</label>
	                    <button type="button" class="btn btn-default btn-yellow f16  mt10" onclick="onUserEnter();"><i class="ico_check"></i>确 定</button>
	                </div>
	            </div>
	        </div>
	    </div>
		</form> 
	</template:replace>
	<template:replace name="bottom">
		<script>
		 $("body").css("overflow","auto");
		 function removeUser(ele){
			   $(ele).parent('.tag').remove();
		   }
		 function selectUser(){
		    	var params = {};
		    	params.query="all";
		    	params.userIds = '';
		    	var nids = [];
		    	$('#user_Names').find('.tag').each(function(index, dom){
					var nid = $(dom).attr('user-nid');
					nids.push(nid);
				});
		    	if(nids!=null)
				{
				for(var i=0;i<nids.length;i++)
					{
					params.userIds += nids[i]+';';
					
					}
				}
		    	params.userIds= params.userIds.substring(0,(params.userIds.length-1));
		    	params.userNames = '';
		    	var vids = [];
				$('#user_Names').find('.tag').each(function(index, dom){
					var vid = $(dom).attr('user-uid');
					vids.push(vid);
				});
				if(vids!=null)
					{
					for(var i=0;i<vids.length;i++)
						{
						params.userNames += vids[i]+';';
						
						}
					}
			    params.userNames= params.userNames.substring(0,(params.userNames.length-1));
		    	dialogUser(true,'userIds','user_Names',null,params);
		    }
		function dataValidation(){
	    	if($.trim($("#roleName").val())==""){
	    		top.common.alert("角色名称不能为空");
	    		return false;
	    	} 
	    	if($.trim($("#roleName").val().length)>20){
	    		top.common.alert("角色名称长度不能超过20");
	    		return false;
	    	} 
			if($.trim($("#roleDesc").val().length)>80){
	    		top.common.alert("角色描述长度不能超过80");
	    		return false;
	    	} 
	    	return true;
	    }
		function onUserEnter(){
			
			var form = $("#editRoleForm");
	    	var url = form.attr("action");
	    	var params = form.serialize();
	    	 var userIds = [];
	    	$('#user_Names').find('.tag').each(function(index,domEle){
	    		var userId = $(domEle).attr('user-nid');
	    		userIds.push(userId);
	    	});
	    	params = params+'&userIds='+userIds.join(';');
 
	    	if(dataValidation()){
		    	var loading = top.common.loading(); 
		    	$.getJSON(url,params,function(json){ 
	    			loading.close();
		    		if(json.code==0){ 
		    			top.common.tips("修改成功",1,function(){
		    				frameElement.callback();
		    			});
		    		}else{ 
		    			top.common.tips(json.msg);
		    		}
		    	});
	    	}
		}
		
		
		function dialogUser(multiple,valId,textId,cb,params){
			var dialog = top.common.dialogIframe("请选择用户",WEB_APP_PATH+"/sys/user/dialog.do?query="+params.query+"&multiple="+multiple,650,600,null,['确定','取消'],{
				btn1 :function(index,layero){
					var iwin = layero.find("iframe").get(0).contentWindow;
		        	if(iwin.dialogClose && typeof(iwin.dialogClose) === 'function'){
		        		var obj = (iwin.dialogClose());
		        		$('#user_Names').empty();
		        		var idObj = $("#"+valId);
		        		var textObj =  $("#"+textId);
		        		if(obj!=null&&obj.ids!=null){
		    				for(var i=0;i<obj.ids.length;i++){ 
		    					textObj.append('<span class="tag" user-nid="'+obj.ids[i]+'" user-uid="'+obj.texts[i]+'" >'+obj.texts[i]+'<button type="button" class="close" onclick="removeUser(this)">×</button></span>');	
		    				}
		        		}
		        	}	        
				},
				btn2 : function(){
				}
			}); 
		 
			dialog.getIFrame().params = params;
		} 
		
		
		</script>
	</template:replace>
</template:include>