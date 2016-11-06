<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="group">
	<template:replace name="title">
		项目概述
	</template:replace>
	<template:replace name="content-body">
		<style>
			.row-data td{
				width: 150px;
			}
			.row-data input,.row-data select{
				width: 130px;
			}
		</style>
	    <div class="app_name">
            <a href="alarm.html"><i class="icons go_back_ico"></i></a>

            <p class="app_a">告警设置</p>
            <em>&gt;</em>

            <p class="app_a"><a href="alarm.html">节点组</a></p> <em>&gt;</em>

            <p class="app_a">新增节点组</p>
        </div>
        <div class="plate">
             <div class="project_r">
                 <div class="r_block p20">
                     <div class="r_title">
                         <h3 class="f14 h3_color h3_btm_line blue mr10">
                             <a href="alarm_manager.html">
                                 <i class="icons add_ico mr5"></i>节点组
                             </a>
                         </h3>


                         <div class="title_line"></div>
                         <!--<div class="title_tab">-->
                             <!--<h5 class="f14">-->
                                 <!--<a class="active_green" href="javascript:void(0);" onclick="queryMysqlsInfo()">-->
                                     <!--<i class="icons add_ico_yellow mr5"></i>添加节点组</a>-->
                             <!--</h5>-->
                         <!--</div>-->
                     </div>
                     <div class="app_details">
                         <div class="app_active_num">
                             <p style="margin-bottom: 5px;">
                                 <span><b>节点组名称：</b>
                                     <input id="hostgrpNm" type="text" placeholder="输入节点组名称"> </span>
                             </p>
                         </div>
                     </div>
                     <div class="r_con p10_0">
                         <div class="g-row">
                             <div class="g-col g-col-6">
                                 <div class="m-card p10_20"  style="height: auto;">
                                     <div class="sl_tit f14">策略</div>
                                     <table  id="temp_table" class="table_ob">
                                         <thead >
                                         <tr>
                                             <th>策略名称</th>
                                             <th>创建人</th>
                                             <th>报警接收组</th>
                                             <th><a class="f18 btn btn-yellow2 btn-sm ser-adv" style="padding:3px 9px;"
                                                    href="#"  id="insert_temp_btn">+</a></th>
                                         </tr>
                                         </thead>
                                         <tbody id="teamDefinedTbl"></tbody>
                                     </table>
                                     <p class="no-content-sm f14" id="tpl">暂无策略项，点击添加一个</p>
                                 </div>
                             </div>
                             <div class="g-col g-col-6">
                                 <div class="m-card p10_20" style="height: auto;">
                                     <div class="sl_tit f14">节点</div>
                                     <table id="host_table" class="table_ob">
                                         <thead>
                                             <tr class="sub_td_title sub_td_title2">
                                                 <th>名称</th>
                                                 <th><a class="f18 btn btn-yellow2 btn-sm ser-adv" style="padding:3px 9px;"
                                                        href="#" id="insert_host_btn">+</a></th>
                                             </tr>
                                              </thead>
                                             <tbody id="userDefinedTbl">
                                         </tbody>
                                     </table>
                                      <p class="no-content-sm f14"  id="host">暂无节点，点击添加一个</p>
                                    <!--  <div id="apps" class="table_page" style="margin: 10px 5px 0; ">
                                         <div class="pull-right pages_mun">
                                             <ul>
                                                 <li class="active"><a href="#">1</a></li>
                                                 <li><a href="#">2</a></li>
                                             </ul>
                                         </div>
                                         <div class="pull-left pages_info"><span> 页码 1/2 共 13</span></div>
                                     </div> -->
                                 </div>
                             </div>
                         </div>
                     </div>
                       <div style="padding:20px 0px"><a href="javascript:void(0)" class="btn btn-yellow f16 mt10" onclick="saveHostgroup()">保存节点组</a></div>
                 </div>
             </div>
        </div>
        <script>
        	$(function(){
        	    /* $("#insert_host_btn").click(function() {
                    var td_select = "<td><select name='hostKey'>"+
                                        "<c:if test='${ ! empty hosts}'>"+
                                            "<c:forEach items='${hosts}' var='row'>"+
                                                "<option value='${row.id}'>${row.hostname}</option>"+
                                            "</c:forEach>"+
                                        "</c:if>"+
                                    "</select></td>"
                	var td_delete = "<td><a class='delete_host_btn f18 btn btn-yellow2 btn-sm ser-adv' style='padding:3px 12px;' href='#'>-</a></td>";
                	var tr = $("<tr>")
                	var table = $("#host_table tr:last");
                	tr.append(td_select).append(td_delete);
                	table.after(tr);
                }); */
                $(document).on("click", ".delete_host_btn", function(){
                    $(this).parent().parent().remove();
                });
        	});
        	
        	   $(function(){
           	    $("#insert_host_btn").click(function(){
           	    var params = {};
           	    params.Ids = "";
           	    params.hostNames="";
           	    var trs = $("#userDefinedTbl").find(">tr");
           	    for(var i=0; i< trs.length;i++){
           	    	var tr=$(trs[i]);
           	    	var id = $.trim(tr.find("input[name='hostid']").val());
           	    	var hostNames = $.trim(tr.find("td[name='hostNames']").html());
           	    	params.Ids += id+';';
           	    	params.hostNames+=hostNames+';';
           	    }
           	    params.Ids= params.Ids.substring(0,(params.Ids.length-1));
           	    params.hostNames= params.hostNames.substring(0,(params.hostNames.length-1));
               	var url = "${WEB_APP_PATH}/groups/host/list?multiple=true";
               		var dialog = top.common.dialogIframe("选择节点",url,650,600,null,['确定','取消'],{
       					btn1 : function(index,layero){ 
       						var iwin = layero.find("iframe").get(0).contentWindow;
       			        	if(iwin.dialogClose && typeof(iwin.dialogClose) === 'function'){
       			        		var obj = (iwin.dialogClose());
       			        		$('#userDefinedTbl').empty();
       			        		if(obj!=null){
       			        			$('#host').hide();
       			        			for(var i=0;i<obj.texts.length;i++){
       			        				var td="<td name='hostNames'>"+obj.texts[i]+"</td>";
       			        				var td_delete = "<td><a class='delete_host_btn f18 btn btn-yellow2 btn-sm ser-adv' style='padding:3px 12px;' href='#'>-</a></td>";
       			        				var tr = $("<tr name='id'>"+"<input type='hidden' name='hostid' id='hostid' value="+obj.ids[i]+">")
       			                    	var table = $("#userDefinedTbl");
       			                    	tr.append(td).append(td_delete);
       			                    	table.append(tr);
       			        			}
       			        		}
       			        	}
       					},
       					btn2 : function(){
       					}
       				});
               	    dialog.getIFrame().params = params;
           	    });
               });
        	function adjustHeight() {
                    var h = $(window).height();
                    var h2 = $(".head").height();
                    $(".plate").css("height", h - 99);
                    $(".left_sub_menu").css("height", h - h2);
                }
                $(document).ready(function () {
                            adjustHeight();
                        }
                )
                $(window).resize(function () {
                    adjustHeight();
                });
                $(".ser_bar2").hide();
                $(".ser-adv").click(function () {
                    $(".ser_bar2").toggle(200);
                })
                function saveHostgroup(){
                	var trs = $("#userDefinedTbl").find(">tr");
                	var groupName=$("#hostgrpNm").val();
                	var defineData=[];
                	for(var i=0; i< trs.length;i++){
                		var tr = $(trs[i]);
                		var key= $.trim(tr.find("input[name='hostid']").val());
						defineData.push(key);
                	}
                	  var trstpl = $("#teamDefinedTbl").find(">tr");
                	  var defineDataTpl=[];
                	  for(var i=0; i< trstpl.length;i++){
                  		var trtpl = $(trstpl[i]);
                  		var id = $.trim(trtpl.find("input[name='tplid']").val());
                  		defineDataTpl.push(id);
                  	}
                	var load= common.loading();
					var dto = {"hostIds":defineData,"tplIds":defineDataTpl,"groupName":groupName,"createUser":"${HMUser.userId}"};
					var option = {
	                    	url : "${ WEB_APP_PATH }/groups",
	                    	data : JSON.stringify(dto),
	                    	type : 'POST',
	                    	cache : false,
	                    	contentType: "application/json"
	                    };
				   var ref = $.ajax(option);
				   ref['done'](function(data){
					   load.close();
					   if(data.code==500){
	                    	common.alert("保存失败！"+(data.msg||''),2);
	                    }else{
	                    	common.tips("保存成功！",1,function(){
	                    		location.href = "${ WEB_APP_PATH }/groups"
	                    		},2000);
	                    	}
	                    });
                }
                $(function(){
            	    $("#insert_temp_btn").click(function(){
            	    var params = {};
            	    params.Ids = "";
            	    params.tplNames="";
            	    params.createUsers="";
            	    params.teamNames="";
            	    var trs = $("#teamDefinedTbl").find(">tr");
            	    for(var i=0; i< trs.length;i++){
            	    	var tr=$(trs[i]);
            	    	var id = $.trim(tr.find("input[name='tplid']").val());
            	    	var tplNames = $.trim(tr.find("td[name='tplNames']").html());
            	    	var createUsers = $.trim(tr.find("td[name='createUsers']").html());
            	    	var teamNames = $.trim(tr.find("td[name='teamNames']").html());
            	    	params.Ids += id+';';
            	    	params.tplNames+=tplNames+';';
            	    	params.createUsers+=createUsers+';';
            	    	params.teamNames+=teamNames+';';
            	    }
            	    params.Ids= params.Ids.substring(0,(params.Ids.length-1));
            	    params.tplNames= params.tplNames.substring(0,(params.tplNames.length-1));
            	    params.createUsers= params.createUsers.substring(0,(params.createUsers.length-1));
            	    params.teamNames= params.teamNames.substring(0,(params.teamNames.length-1));
                	var url = "${WEB_APP_PATH}/templates/list?multiple=true";
                		var dialog = top.common.dialogIframe("选择策略",url,650,600,null,['确定','取消'],{
        					btn1 : function(index,layero){ 
        						var iwin = layero.find("iframe").get(0).contentWindow;
        			        	if(iwin.dialogClose && typeof(iwin.dialogClose) === 'function'){
        			        		var obj = (iwin.dialogClose());
        			        		$('#teamDefinedTbl').empty();
        			        		if(obj!=null){
        			        			$('#tpl').hide();
        			        			for(var i=0;i<obj.texts.length;i++){
        			        				var td="<td name='tplNames'>"+obj.texts[i]+"</td>";
        			        				td+="<td name='createUsers'>"+obj.creates[i]+"</td>";
        			        				td+="<td name='teamNames'>"+obj.teams[i]+"</td>";
        			        				var td_delete = "<td><a class='delete_host_btn f18 btn btn-yellow2 btn-sm ser-adv' style='padding:3px 12px;' href='#'>-</a></td>";
        			        				var tr = $("<tr name='id'>"+"<input type='hidden' name='tplid' id='tplid' value="+obj.ids[i]+">")
        			                    	var table = $("#teamDefinedTbl");
        			                    	tr.append(td).append(td_delete);
        			                    	table.append(tr);
        			        			}
        			        		}
        			        	}
        					},
        					btn2 : function(){
        					}
        				});
                	    dialog.getIFrame().params = params;
            	    });
                });
        </script>
	</template:replace>
</template:include>
