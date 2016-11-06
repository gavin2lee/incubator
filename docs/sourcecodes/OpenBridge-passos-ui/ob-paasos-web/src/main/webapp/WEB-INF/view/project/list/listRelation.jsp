<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
			body {
				overflow: auto;
			}
		</style>
		<div class="form_control">
			<div class="form_block choose_item">
				<label style="width:56px; font-size:12px;">关键字：</label><input type="text" id="keyword" name="keyword" value="${keyword }"/><button class="btn btn-sm btn-yellow2" id="search">搜索</button>
			</div>
			<div class="choose_table_list">
			<table class="table_ob" id="userTable">
				<thead>
					<c:choose>
						<c:when test="${param['type']=='app' || param['type']=='api'}">
							<tr>
							<!-- api和app的表头 -->
			                   <th style="width:50px;"></th>
			                   <th style="width:130px;">项目名</th>
			                   <th style="width:150px;">负责人</th>
			                   <th style="width:150px;">创建时间</th>
			               </tr>
						</c:when>	
						<c:when test="${ not empty param['multiple'] and param['multiple'] eq 'true' and param['type']=='store'}">
							<tr>
								<!-- 预置应用的表头，有多选 -->
			                   <th style="width:50px;text-align: center;"><input type="checkbox" id="checkAll"/></th>
			                   <th style="width:150px;">预置应用名称</th>
			                   <th style="width:150px;"></th>
			                   <th style="width:150px;">创建时间</th>
			               </tr>
						</c:when>
						<c:when test="${ !(not empty param['multiple'] and param['multiple'] eq 'true') and param['type']=='store'}">
							<tr>
								<!-- 预置应用表头，只能单选。 -->
			                   <th style="width:50px;text-align: center;"></th>
			                   <th style="width:150px;">预置应用名称</th>
			                   <th style="width:150px;"></th>
			                   <th style="width:150px;">创建时间</th>
			               </tr>
						</c:when>
						<c:otherwise>
						
						</c:otherwise>
					</c:choose>
	             </thead> 
	             <tbody>
				 	<c:forEach items="${ pageData }" var="row" varStatus="index">
		               <tr>
		                   <td style="text-align:center;">
		                   		<c:choose>
									<c:when test="${ not empty param['multiple'] and param['multiple'] eq 'true' }">
										<input id="row-${ row.id }"  class="userChk" type="checkbox" value="${ row.id }"   data-name="${ row.appName }" style="width: auto;" />
									</c:when>
									<c:otherwise>
										<input id="row-${ row.id }"  class="userChk" type="radio" name="userId" value="${ row.id }"  data-name="${ row.appName }"  data-code="${row.projectCode}" style="width: auto;" />
									</c:otherwise>
								</c:choose>
		                   </td>
		                    <td style="width:130px;">${ row.appName }</td>
							<td style="width:150px;">${ row.leaderUser }</td>
			                <td> ${fn:substring(row.createTime,0,19)} </td>
		               </tr>
	            	</c:forEach>
	            </tbody>
	        </table> 
	        <c:choose>
	       <c:when test="${not empty pageData }">
			<ui:pagination data="${ pageData }" href="javascript:loadRelation(!{pageNo},!{pageSize})" id="grid" moreThanOne="true"></ui:pagination>
		</c:when>
	  <c:otherwise>
		<c:if test="${!param.dialog }"><p class="no-content f14">没有可关联的应用</p></c:if>
	</c:otherwise>
</c:choose>
		</div>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script>
		    var multiple="${param['multiple']}";
			var params = frameElement.params;
			var ref = {};
			if(params == null){
				params = [];
			}
			for(var i=0;i<params.length;i++){
				ref[params[i].id] = params[i];
			}
			function dialogClose(){
				var resultParams = [];
				for(var _attr in ref){
					if(ref[_attr]){
						resultParams.push(ref[_attr]);
					}					
				}
				return resultParams;
			}
			function loadRelation(pageNo,pageSize){
				$('#search').trigger('click',[pageNo,pageSize])
			}

			/*
			用于选择主要功能代码封装
			*/
			function doChecked(_v,myThis,myParams,myRef){
				//选中
				var j = {};
				j['id'] = _v;
				if(multiple==null || multiple=='false'){
					myParams = [];
					myRef = {};
				}
				for(var k in $(myThis).get(0).attributes){
					if($(myThis).get(0).attributes[k].nodeType==null || $(myThis).get(0).attributes[k].nodeType!=2){
						continue ;	
					}
					var _name = $(myThis).get(0).attributes[k].name;
					var _value  = $(myThis).get(0).attributes[k].value;
					if(_value==null){
						_value ='';
					}
					if(_name==null || _name.indexOf('data-')<0){
						continue ;
					}
					var k = _name.substr('data-'.length);
					j[k] = _value;
				}
				myParams.push(j);
				myRef[_v]=j;
				//由于是复制一份，所以需要重新给ref赋值。
				ref = myRef;
			}
			
			/*
			用于取消选择主要功能代码封装
			*/
			function doUnChecked(_v,myThis,myParams,myRef){
				//去除
				myRef[_v]=null;
				
				//add by luoan 
				//for the defect PAASOS-326
				//用于从堆栈中删除特定的一个元素。
				var tempParams = [];
				myParams.forEach(function(elt, i) {
					//定义一个数组变量
					if(elt['id'] != _v){
						tempParams.push(elt);
					}
				});
				myParams = tempParams;
				//交给自身页面变量
				params = myParams;
				//交给母页面变量
				frameElement.params = myParams;
			}
			
			$(function(){
				//增加全选按钮
            	$("#checkAll").click(function(){
            		var ch = $(this).attr("checked")=="checked";
            		$("#userTable tr:gt(0) [type=checkbox]").each(function(i,v){
                		if(ch&&!this.checked){
							//如果是全选，则要操作并没有选择的
							
							$(this).trigger('click',[false,true]);
                    	}else if(!ch&&this.checked){
							//如果是全部取消，则要操作已经选择的
							
                    		$(this).trigger('click',[false,false]);
                        }

            		});
            	});
				
				$('#search').bind('click',function(event,pageNo,pageSize){
					pageNo = pageNo==null?1:pageNo;
					pageSize = pageSize==null?10:pageSize;
					var fetchUrl = "${WEB_APP_PATH}/project/listRelation.do?multiple=${param['multiple']}&type=${param['type']}&keyword="+$.trim($("#keyword").val())+"&pageNo="+pageNo+"&pageSize="+pageSize;
					
					if(frameElement.passStatus){
						fetchUrl = fetchUrl+"&status="+frameElement.passStatus;
					}
					common.goto(fetchUrl);
				});
				$('.userChk').bind('click',function(event,isInit,isAll){
					//标志变量
					//0：表是初始值，1：表是全选按钮点击时，
					//2：表是取消全选按钮点击时
					//如果
					var target = 0;
					if(isInit!=null && isInit){
						//初始化执行的点击事件
						return ;
					}
					var _v = $(this).val();

					if(isAll!=null && isAll){
						target = 1;
					}else if(isAll!=null && !isAll){
						target = 2;
					}


					//如果属于全选级别的，选完就退出
					if(target == 1){
						//全部选中
						doChecked(_v, this, params, ref);
						return;
					}else if(target == 2){
						//全部取消
						//debugger;
						doUnChecked(_v, this, params,ref);
						
						return;
					}


					
					if(this.checked){
						doChecked(_v, this, params, ref);
						
					}
					else if(!this.checked){
						doUnChecked(_v, this, params,ref);
					}

					
				});
				
				for(var i=0;i<params.length;i++){
					var _id = params[i].id;
					$('#row-'+_id).trigger('click',[true]);
				}
			});
		</script>
	</template:replace>
</template:include>