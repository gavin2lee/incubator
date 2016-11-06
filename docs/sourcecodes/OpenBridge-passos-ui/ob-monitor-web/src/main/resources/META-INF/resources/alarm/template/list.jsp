<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../../common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
		body {
			overflow: auto;
		}
       </style>
		<div class="form_control" id="templatesTable">
				<div class="form_block choose_item">
				<label style="width:56px; font-size:12px;">关键字：</label><input type="text" id="keyword" name="keyword" value="${keyword }"/><button class="btn btn-default btn-sm btn-green" id="search" onclick="search()">搜索</button>
			</div> 
			<div class="choose_table_list">
				<table class="table_ob" id="userTable">
					<tr>
						<th style="width: 50px;"></th>
						<th>名称</th>
						<th>创建人</th>
						<th>报警接收组</th>
						<th>操作</th>
					</tr>
					<c:forEach items="${ pageData }" var="row" varStatus="index">
						<tr>
							<td style="text-align: center;">
							<td><a href="templates/${row.id}">${row.tpl_name}</a></td>
							<td><span class="createUser">${ row.create_user }</span></td>
							<td><a href="javascript:detail(${row.id});"  onclick="void(0)"  alt="查看该组成员！">${row.team_name}</a></td>
							<td><a class="mr10" href="templates/${row.id}">修改</a><a class="template_delete_btn mr10"  href="#" name="${ row.id }">删除</a></td>
						</tr>
					</c:forEach>
				</table>				 
		      <ui:pagination data="${ pageData }" href="javascript:loadItems(!{pageNo},!{pageSize});" id="host"></ui:pagination>
					</div>
				</div>
			</template:replace>
			<template:replace name="bottom">
				<template:super />
		<script>
			var keyWord = "";
			
		    var multiple="${param['multiple']}";
			var params = frameElement.params;
			if(params == null){
				params = {};
			}
			var ids = null;
			if(params.Ids==null||$.trim(params.Ids) == ""){
				ids = [];
			}else{
				ids = params.Ids.split(";"); 
			}
			var names = null;
			if(params.tplNames==null||$.trim(params.tplNames) == ""){
				names = [];
			}else{
				names = params.tplNames.split(";"); 
			}
			var creates = null;
			if(params.createUsers==null||$.trim(params.createUsers) == ""){
				creates = [];
			}else{
				creates = params.createUsers.split(";"); 
			}
			var teams = null;
			if(params.teamNames==null||$.trim(params.teamNames) == ""){
				teams = [];
			}else{
				teams = params.teamNames.split(";"); 
			}
			$(function() {

	             $(document).on("click", ".template_delete_btn", function(){
	            	 var _this=$(this);
	            	 common.confirm('确定要删除此策略吗？',function(res){
	            	 if(res){
	                  var option = {
	                  	url : 'templates/'+_this.attr("name"),
	                  	type : 'DELETE',
	                  	cache : false
	                  };
	                  var ref = $.ajax(option);
	                  var flag = true;
	                  ref['done'](function(data){
	                  	if(data.code==1){
	                  	common.alert("删除失败！"+(data.msg||''),2);
	                  	flag = false;
	                  }else{
	                  	common.tips("删除成功！",1,function(){
	                  		//location.href = "${ WEB_APP_PATH }/templates";
	                  		},2000);
	                  	}
	                  });
	                  if(flag)_this.parent().parent().remove();
	            	   }
	                 });
	             });


				

				if(multiple){
					if(ids.length > 0){
						var rows = $("#userTable").find("tr");
						for(var i=0;i<rows.length;i++){
							if(i==0){}
							else{ 
								var row = rows[i];
								var chb = $(".userChk",row);
								for(var j=0;j<ids.length;j++){
									if(chb.val()==ids[j]){
										chb.attr("checked","checked");
									}
								}
							}
						} 
					}
				}
			});
			function onCheckClick(obj){ 
				var xobj = $(obj);
				var isExt = false;
				var xi = 0;
				for(var i=0;i<ids.length;i++){
					if(ids[i]==xobj.val()){
						isExt = true;
						xi = i;
						break;
					}
				}
				if(xobj.is(":checked")){
					if(isExt){
						
					}else{
						ids[ids.length] = xobj.val();
						names[names.length] = xobj.attr("data-name");
						creates[creates.length] = xobj.attr("data-create");
						teams[teams.length] = xobj.attr("data-team");
					}
				}else{
					if(isExt){
						ids.splice(xi,1);
						names.splice(xi,1);
						creates.splice(xi,1);
						teams.splice(xi,1);
					}else{
						
					}
				}
				frameElement.params = {};
				frameElement.params.Ids = ids.join(";");
				frameElement.params.tplNames = names.join(";");
				frameElement.params.createUsers = creates.join(";");
				frameElement.params.teamNames = teams.join(";");
			}
			   function detail(id){       
							var dialog = common.dialogIframe("组内成员信息","${ WEB_APP_PATH }/templates/"+id+"/users",200,300,function(){
								dialog.close();
								common.forward('${WEB_APP_PATH}/templates');
							}); 
				 }
				  
            
			function dialogClose(){
				if(multiple){
					var obj = {};
					obj.ids = ids;
					obj.texts = names;
					obj.creates=creates;
					obj.teams=teams;
					return obj;
				}else{
					var obj = {};
					obj.ids = [];
					obj.texts = [];
					obj.creates=[];
					obj.teams=[];
					var rows = $("#userTable").find("tr");
					for(var i=0;i<rows.length;i++){
						if(i==0){}
						else{  
							var row = rows[i];
							var chb = $(":checked",row);
							if(chb && chb.length > 0){
								obj.ids.push(chb.val());
								obj.texts.push($(row).find(".tplName").text());
								obj.creates.push($(row).find(".createUser").text());
								obj.teams.push($(row).find(".teamName").text());
							}
						}
					} 
					return obj;
				}
			}

            function  getUsersByTid(id){                   
                var d=common.loading();                       
                $.ajax({
                  url:"${ WEB_APP_PATH }/templates/"+id+"/users",
                  data:null,
                  type:'GET',
                  success:function(data){
                  	  d.close();       
                      var message=data.data.data;
                      //alert(message.length);
                      var html='';
                       if(message.length>0){
                        html+=""
                       html+='<tbody style="text-indent:50px;">';
                       for(var i=0;i<message.length;i++){
                           html+='<tr> <td>'+message[i].userName+  '</td></tr>';                    
                        }     
                        html+='</tbody>'  ;               
                        $("#uid_table").html(html);     
                        popCenterWindow();                                                                                                                                                                                                                              
                       }else{
                        alert("该组无成员！");
                       }                           
                  },
                 contentType: "application/json"              
                });         
             }
            //跳转页面,页面加载，按页面号
            function loadItems(pageNo,pageSize){
            	 keyWord = $.trim($("#keyword").val());
				 $("#templatesTable").load("templates/list?multiple=&keyword="+$.trim($("#keyword").val())+"&pageNo="+pageNo+"&pageSize="+pageSize); 
				 $("#keyword").val(keyWord);
			}

			//按关键字来搜索
			function search(){
				keyWord = $.trim($("#keyword").val());
				$("#templatesTable").load("${ WEB_APP_PATH }/templates/list?multiple=${param['multiple']}&keyword="+$.trim($("#keyword").val())); 
				$("#keyword").val(keyWord);
			}

		</script>
	</template:replace>
</template:include>