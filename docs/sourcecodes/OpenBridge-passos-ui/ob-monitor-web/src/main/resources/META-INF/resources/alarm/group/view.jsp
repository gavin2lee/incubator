<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="group">
	<template:replace name="title">
		项目概述
	</template:replace>
	<template:replace name="content-body">
		<style>
</style>
		<div class="app_name">
			<a href="alarm.html"><i class="icons go_back_ico"></i></a>

			<p class="app_a">告警设置</p>
			<em>&gt;</em>

			<p class="app_a">
				<a href="groups">集群</a>
			</p>
			<em>&gt;</em>

			<p class="app_a">dev</p>
		</div>
		<div class="plate">
			<div class="project_r">
				<div class="r_block p20">
					<div class="r_title">
						<h3 class="f14 h3_color h3_btm_line blue mr10">
							<a href="alarm_manager.html"> <i class="icons add_ico mr5"></i>节点组
							</a>
						</h3>


						<div class="title_line"></div>
					</div>
					<div class="app_details">
						<div class="app_active_num">
							<p style="margin-bottom: 5px;">
								<span><b>节点组名称：</b> <input id="hostgrpNm" type="text"
									placeholder="输入节点组名称"> </span>
							</p>
						</div>
					</div>
					<div class="r_con p10_0">
						<div class="g-row">
							<div class="g-col g-col-6">
								<div class="m-card p10_20" style="height: auto;">
									<div class="sl_tit f14">策略</div>
									<table class="table_ob">
										<thead>
											<tr>
												<th>策略名称</th>
												<th>创建人</th>
												<th>报警接收组</th>
												<th>操作</th>
												<th><a class="f18 btn btn-yellow2 btn-sm ser-adv"
													style="padding: 3px 9px;" href="#">+</a></th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td><a href="#">cpu2</a></td>
												<td>liyang</td>
												<td>ceshi</td>
												<td><a class="mr10" href="#">修改</a></td>
												<td><a class="f18 btn btn-yellow2 btn-sm ser-adv"
													style="padding: 3px 12px;" href="#">-</a></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="g-col g-col-6">
								<div class="m-card p10_20" style="height: auto;">
									<div class="sl_tit f14">节点</div>
									<table id="host_table" class="table_ob">
										<tbody id="userDefinedTbl">
											<tr class="sub_td_title sub_td_title2">
												<th>名称</th>
												<th><a class="f18 btn btn-yellow2 btn-sm ser-adv"
													style="padding: 3px 9px;" href="#" id="insert_host_btn">+</a></th>
											</tr>
										</tbody>
									</table>

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
        	    $("#insert_host_btn").click(function() {
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
                });
                $(document).on("click", ".delete_host_btn", function(){
                    $(this).parent().parent().remove();
                });
        	});
        </script>
	</template:replace>
</template:include>