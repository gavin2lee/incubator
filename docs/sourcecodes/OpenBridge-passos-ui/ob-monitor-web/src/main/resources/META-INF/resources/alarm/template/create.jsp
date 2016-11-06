<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="template">
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

                    <p class="app_a"><a href="${ WEB_APP_PATH }/templates">策略</a></p>
                </div>
                <div class="plate">
                    <div class="project_r">
                        <div class="r_block p20">
                            <div class="tem_con">
                                <div class="xq_block">
                                    <h5>模板基本信息</h5>
                                </div>
                                <div class="tem_nr">
                                    <div class="form-inline">
                                        <label class="mr10">name：</label><input class="w150 mr10" type="text"><button class="btn btn-yellow btn-sm" href="#">保存</button>
                                    </div>
                                </div>
                            </div>
                            <div class="tem_con">
                                <div class="xq_block">
                                    <h5>该模板中的策略列表
                                        <span class="pull-right">
                                            <a class="btn  btn-yellow btn-sm" href="#" id="strategy_create_btn">
                                                <em class="f14">+添加策略</em>
                                            </a>
                                        </span>
                                    </h5>
                                </div>
                                <div class="app_active_num f14 alert alert_min">
                                    <p>
                                        <span><b>max：</b>最大报警次数</span> <em>|</em>
                                        <span><b>P：</b>报警级别（&lt;3: 既发短信也发邮件 &gt;=3: 只发邮件）</span>
                                    </p>
                                </div>
                                <table class="table_ob table_story">
                                    <thead>
                                    <tr>
                                        <th>metric</th>
                                        <th>condition</th>
                                        <th>max</th>
                                        <th>P</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>cpu.idle</td>
                                        <td>all(#3)>=90</td>
                                        <td>3</td>
                                        <td>0</td>
                                        <td><a class="mr10" href="#">修改</a><a class="mr10" href="#">删除</a>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <p class="no-content f14">暂无策略项</p>
                            </div>
                            <!--添加策略弹层开始-->
                            <div class="m_message" id="strategy_div" style="display:none">
                                <div class="form_control p20">
                                    <div class="form_block form_block-sm">
                                        <label class="label-sm">metric</label>
                                        <div class="specList"><input type="text" name="metric"></div>
                                    </div>
                                    <div class="form_block form_block-sm">
                                        <label class="label-sm">condition</label>
                                        <div class="form-inline">
                                            all(#3)
                                            <select class="form-control" id="op">
                                                <option value="==">==</option>
                                                <option value="!=">!=</option>
                                                <option value="<">&lt;</option>
                                                <option value="<=">&lt;=</option>
                                                <option value=">">&gt;</option>
                                                <option value=">=">&gt;=</option>
                                            </select>
                                            <input class="mr10" type="text" id="right_value">
                                        </div>
                                    </div>
                                    <div class="form_block form_block-sm">
                                        <label class="label-sm">max</label>
                                        <div class="specList"><input type="text" id="max_step"></div>
                                    </div>
                                    <div class="form_block form_block-sm">
                                        <label class="label-sm">P</label>
                                        <div class="specList"><input type="text" id="priority"></div>
                                    </div>
                                    <div class="form_block form_block-sm mt10">
                                        <label>&nbsp;</label>
                                        <button id="strategy_new_btn" class="btn btn-yellow f16 mt10"><i class="ico_check"></i>创 建</button>
                                    </div>
                                </div>
                            </div>
                            <!--添加策略弹层结束-->

                            <div class="tem_con">
                                <div class="xq_block">
                                    <h5 class="text-green">策略添加/修改</h5>
                                </div>
                                <div class="tem_nr">
                                    <div class="tem_form mb10">
                                        <div class="form-inline">
                                            <label class="mr10">name：</label><input class="w150 mr10" type="text">
                                        </div>
                                        <div class="form-inline">
                                            <label class="mr10">Max：</label><input class="w150 mr10" type="text">
                                        </div>
                                        <div class="form-inline">
                                            <label class="mr10">P：</label><input class="w150 mr10" type="text">
                                        </div>
                                        <div class="form-inline">
                                            <label class="mr10">note：</label><input class="w150" type="text">
                                        </div>
                                    </div>
                                    <div class="tem_form mb10">
                                        <div class="form-inline">
                                            <label class="mr10">if：</label><input class="w150" type="text">
                                            <select>
                                                <option> ==</option>
                                            </select>
                                            <input class="mr10" type="text">
                                        </div>
                                    </div>
                                    <div class="tem_form mb10">
                                        <button class="btn btn-yellow" href="#">保存</button>
                                    </div>
                                </div>
                            </div>
                            <div class="tem_con">
                                <div class="xq_block">
                                    <h5 class="text-yellow">模板报警配置，对模板中的所有策略生效</h5>
                                </div>

                                <div class="tem_nr">
                                    <div class="tem_form mb10">
                                        <div class="form-block">
                                            <label class="mr10">报警接收组：</label>
                                            <input  class="mr10" type="text"><a href="#" class="btn  btn-sm btn-yellow2">选择组</a>
                                        </div>
                                    </div>

                                    <div class="tem_form mb10">
                                        <button class="btn btn-yellow" href="#">保存</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        <script>
        	$(function(){
        	    $("#insert_host_btn").click(function() {
                    var td_select = "<td><select>"+
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
                $("#strategy_create_btn").click(function(){
                    common.dialogDom("新增策略",$("#strategy_div"),600,400,{},null)
                });
                $("#strategy_new_btn").click(function(){
                    var sid = $("#current_sid").val();
                    $.post('/strategy', {
                        'sid': sid,
                        'metric': $.trim($("#metric").val()),
                        'tags': $.trim($("#tags").val()),
                        'max_step': $.trim($("#max_step").val()),
                        'priority': $.trim($("#priority").val()),
                        'note': $.trim($("#note").val()),
                        'func': $.trim($("#func").val()),
                        'op': $.trim($("#op").val()),
                        'right_value': $.trim($("#right_value").val()),
                        'run_begin': $.trim($("#run_begin").val()),
                        'run_end': $.trim($("#run_end").val()),
                        'tpl_id': $.trim($("#tpl_id").val())
                    }, function (json) {
                        handle_quietly(json, function () {
                            location.reload();
                        });
                    })
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
        </script>
	</template:replace>
</template:include>
