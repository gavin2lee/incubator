<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="tenant">
	<template:replace name="title">
		组织管理
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

            <p class="app_a"><a href="../tenant/list.do">环境管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="../tenant/list.do">组织管理</a></p>
            <em>&gt;</em>

            <p class="app_a"><a href="#nogo">${not empty param.id ? '编辑' : '添加'}组织</a></p>
        </div>
        <div class="plate">
            <div class="project_r">
                <div class="r_block p20">
                    <div class="r_block">
                        <div class="r_title">
                            <h3 class="f14 h3_color h3_btm_line">${not empty param.id ? '编辑' : '<i class="icons add_ico_blue mr5"></i>添加'}组织</h3>

                            <div class="title_line"></div>
                        </div>
                        <div class="r_con p10_0">
                            <div class="form_control p20">
                            <form id="createForm">
                            	<input type="hidden" id="tenantId" value="${tenant.tenantId }"/>
                                <div class="form_block">
                                    <label>组织名称</label>
                                    <input type="text" id="tenantName" name="tenantName" value="${tenant.tenantName }"><em style="color:red" >*</em>
                                </div>
                                <div class="form_block">
                                    <label>组织成员</label>
                                    <input type="hidden" name="memIds" id="memIds" value="<c:forEach items="${tenant.member }" var="mem"
										varStatus="status">${mem.userId }<c:if test="${!status.last }">;</c:if></c:forEach>"/>

                                    <div id="memNames" name="memNames" style="width: 324px; height: 100px; border: 1px solid #eaeaea; display: inline-block; background-color: white; padding: 3px 5px;">
									<c:forEach items="${tenant.member }" var="mem"
										varStatus="status">
										${mem.userName }
										<c:if test="${!status.last }">;</c:if>
									</c:forEach>
									</div><button onclick="selectUser()" type="button" style="padding: 3px 5px;width:50px; line-height: 24px;cursor: pointer;display: inline-block; vertical-align: top; height: 108px;" class="btn btn-default btn-yellow2">选择
                                    </button>
                                    <em style="color:red">*</em>
                                </div>
                                <div class="form_block">
                                    <label>管理员</label>
                                    <select  id="admin" name="admin">
                                    <option value="">--请选择--</option>
                                    <c:forEach items="${tenant.member }" var="mem"
										varStatus="status">
										<option value="${mem.userId }" <c:if test="${mem.userId==tenant.admin.userId }">selected</c:if>>${mem.userName }</option>
									</c:forEach>
                                    </select>
                                </div>
                                <div class="form_block">
                                    <label>预置应用分配</label>
                                   	<input type="hidden" name="presetNames" id="presetIds" 
                                   		value="<c:forEach items="${tenant.presetList }" var="item" varStatus="status">${item.presetId}<c:if test="${!status.last}">;</c:if></c:forEach>"/>
                                    <div id="storeId" name="storeNames" style="width: 324px; height: 100px; border: 1px solid #eaeaea; display: inline-block; background-color: white; padding: 3px 5px;">
										<c:forEach items="${tenant.presetList }" var="item" varStatus="status">${item.appName }<c:if test="${!status.last }">;</c:if></c:forEach>
									</div>
									<button onclick="selectStore()" type="button" style="padding: 3px 5px;width:50px; line-height: 24px;cursor: pointer;display: inline-block; vertical-align: top; height: 108px;" class="btn btn-default btn-yellow2">选择
                                    </button>
                                    <em style="color:red">*</em>
                                </div>
                                <div class="form_block">
                                	<label>资源配额</label>
                                	<div class="specList" >
							            <ul class="u-flavor">
							            	<c:forEach items="${firstTenantItemCategory}" var="tenantItem">
						            			<li onclick="switchTab($(this).attr('type'));" type="${tenantItem.code}">
													${tenantItem.codeDisplay }
								                </li>
							            	</c:forEach>
							            </ul>
			        				</div>
                                </div>
                                <c:forEach items="${firstTenantItemCategory}" var="firstTenantItem">
                                	<c:forEach items="${secondTenantItemCategory}" var="secondTenantItem">
                                		<c:if test="${secondTenantItem.parentCode == firstTenantItem.code }">
	                                		<div class="specList" item="item" type="${firstTenantItem.code }" style="display:block">
			                                	<ul style="margin-left: 9px">
			                                    	<li class="specCard specCard_max">
			                                    		<div class="spec_up">
			                                    			<p>${secondTenantItem.codeDisplay }</p>
			                                            </div>
			                                            <div class="spec_down">
				                                            <c:forEach items="${thridTenantItemCategory}" var="thridTenantItem">
				                                            	<c:if test="${thridTenantItem.parentCode == secondTenantItem.code }">
				                                            		<p>
				                                            			<span>${thridTenantItem.codeDisplay }</span>
				                                            			<input categoryType="${firstTenantItem.code}" subCategoryType="${secondTenantItem.code}" itemLookupType="${thridTenantItem.code }" class="w100 mr10" type="text" value="${thridTenantItem.defaultValue }">
				                                            			<span class="w30 text-left">${thridTenantItem.units }</span>
				                                            		</p>
				                                            	</c:if>
				                                            </c:forEach>
			                                            </div>
			                                       </li>
			                                	</ul>
			                                </div>
		                                </c:if>
                                	</c:forEach>
                                </c:forEach>
                                <div class="form_block">
                                    <label>描述</label>
                                    <textarea id="description" name="description">${tenant.description }</textarea>
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
                <!--创建应用部署结束-->
            </div>
        </div>
        <%@ include file="/common/include/validation_header.jsp"%>
        <script>
        function selectUser(){
	    	var params = {};
	    	params.query="all";
	    	params.userIds = $("#memIds").val();
	    	params.userNames = $("#memNames").html();
	    	common.dialogUser(true,'memIds','memNames',function(){
	    		var userIds = $("#memIds").val().split(";");
		    	var userNames = $("#memNames").html().split(";");
		    	var str = '<option>--请选择--</option>';
		    	$.each(userIds,function(i,v){
		    		str += "<option value='"+v+"'>"+userNames[i]+"</option>";
		    	});
	    		$("#admin").html(str);
	    	},params,{
	    		isFilterExist : true,
	    		table : "sys_tenant_relation",
	    		tenantId : $("#tenantId").val()
	    	});
	    }
        
        function switchTab(type){
        	$("li[type="+type+"]").addClass("selected");
        	$("li[type!="+type+"]").removeClass("selected");
			$(".specList[type="+type+"][item=item]").css("display","block");
			$(".specList[type!="+type+"][item=item]").css("display","none");
        }
        
        function selectStore(){
        	var dialog = common.dialogIframe("预置应用","${WEB_APP_PATH}/project/listRelation.do?multiple=true&type=store",600,620,null,['确定','取消'],{
				btn1 : function(index,layero){
					var iwin = layero.find("iframe").get(0).contentWindow;
					if(!(iwin.dialogClose && typeof(iwin.dialogClose) === 'function')){
						return ;
					}
					var obj = (iwin.dialogClose());
					var storeHtml = "";
					var presetIdsVal = "";
					$.each(obj,function(_index,item){
						if(_index == obj.length - 1){
							storeHtml = storeHtml + item.name;
							presetIdsVal = presetIdsVal + item.id;
						}else{
							storeHtml = storeHtml + item.name + ";";
							presetIdsVal = presetIdsVal + item.id + ";";
						}
					});
					//如果一行开头有分号，则清除
					if(storeHtml.startWith(";")){
						storeHtml = storeHtml.substr(1,storeHtml.length-1);
					}
					$("#storeId").html(storeHtml);
					$("#presetIds").val(presetIdsVal);
				},
				btn2 : function(){
					
				}
			});
        	/* var presetIds = {
        		id : $("#presetIds").val(),
        		name : $("#storeId").html()
        	}; */
        	var presetIds = [];
        	var nameArr = $("#storeId").html().trim().split(";");
        	$.each($("#presetIds").val().split(";"),function(index,item){
        		presetIds.push({
        			id : item,
        			name : nameArr[index]
        		});
        	});
        	dialog.getIFrame().params = presetIds;
        }
        
        $(function(){
        	
        	var tenantQuotaJson = ${tenantQuotaJson};
        	if(tenantQuotaJson){
        		$.each(tenantQuotaJson,function(index,item){
        			$("input[categoryType="+item.categoryType+"][subCategoryType="+item.subCategoryType+"][itemLookupType="+item.itemLookupType+"]").val(item.quota);
        		});
        	}
        	switchTab("deploy");
        	$("#createForm").validate({
        		rules : {
        			tenantName : {
        				notBlank : true,
        				byteRangeLength : [1,30]
        			},
        			memNames : {
        				notBlank : true
        			}
        		},
        		messages : {
        			
        		}
        	});
        	function validate(){
        		if(!$("#createForm").valid()){
        			return false;
        		}
        		if(!$("#tenantName").val()){
        			common.alert("请输入组织名称!");
        			return false;
        		}
        		if(!$("#memIds").val()){
        			common.alert("请选择组织成员!");
        			return false;
        		}
        		var quotaFlag = true;
        		var quotaArr = $("input[categorytype][subcategorytype][itemlookuptype]");
        		var reg = /^[0-9]*[1-9][0-9]*$/;
        		for(var i = 0;i<quotaArr.length;i++){
        			if(!$(quotaArr[i]).val() || !reg.test($(quotaArr[i]).val())){
        				quotaFlag = false;
        				break;
        			}
        		}
				if(!quotaFlag){
					common.alert("请输入正整数配额!");
        			return false;
				}
        		return true;
        	}
        	
        	$("#submitBtn").click(function(){
        		if(!validate()){
        			return;
        		}
        		var member = [];
//         		var memNames = $("#memNames").html().split(";");
        		$.each($("#memIds").val().split(";"),function(i,v){
        			member.push({
        				userId : v
        			});
        		});
        		
        		var presetList = [];
        		$.each($("#presetIds").val().split(";"),function(index,item){
        			presetList.push({
        				presetId : item
        			});
        		});
        		
        		var param = {
           			tenantId : $("#tenantId").val(),
           			tenantName : $("#tenantName").val(),
           			description : $("#description").val(),
           			admin : {
           				userId : $("#admin").val()
           			},
           			member : member,
           			tenantQuotaList : packageQuotaJson(),
           			presetList : presetList
           		};
       			var option = {
					url : "${ WEB_APP_PATH }/manager/tenant/saveOrUpdate.do",
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
       					location.href = "${ WEB_APP_PATH }/manager/tenant/list.do";
       						},2000);
       				  }
   				});
        			
        	});
        	
        	function packageQuotaJson(){
        		var quotaJson = new Array();
        		$.each($("input[categorytype][subcategorytype][itemlookuptype]"),function(index,item){
        			var itemJson = {
        					categorytype : $(item).attr("categorytype"),
        					subcategorytype : $(item).attr("subcategorytype"),
        					itemlookuptype : $(item).attr("itemlookuptype"),
        					quota : $(item).val()
        			};
        			quotaJson.push(itemJson);
        		});
        		return quotaJson;
        	}
        })
        </script>
	</template:replace>
</template:include>

