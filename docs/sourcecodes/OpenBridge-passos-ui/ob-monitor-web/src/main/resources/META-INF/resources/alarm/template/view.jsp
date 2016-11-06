<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="template">
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

            <p class="app_a"><a href="${ WEB_APP_PATH }/templates">策略</a></p><em>&gt;</em>

            <p class="app_a">1</p>
        </div>
        <div class="plate">
            <div class="project_r">
                <div class="r_block p20">
                    <div class="tem_con">
                        <div class="xq_block">
                            <h5>模板基本信息</h5>

                        </div>
                        <div class="app_active_num f14 alert alert_min">
                            <p>
                                <span><b>name：</b>1</span>
                            </p>
                        </div>
                    </div>
                    <div class="tem_con">
                        <div class="xq_block">
                            <h5>该模板中的策略列表</h5>

                        </div>
                        <div class="app_active_num f14 alert alert_min">
                            <p>
                                <span><b>max：</b>最大报警次数</span> <em>|</em> <span><b>P：</b>报警级别（&lt;3: 既发短信也发邮件 &gt;=3: 只发邮件）</span>
                                <em>|</em>
                                <span><b>run：</b> 生效时间，不指定就是全天生效 </span>
                            </p>

                        </div>
                        <table class="table_ob table_story">
                            <thead>
                            <tr>
                                <th>metric/tags/note</th>
                                <th>condition</th>
                                <th>max</th>
                                <th>P</th>
                                <th>run</th>
                            </tr>
                            </thead>
                            <tbody>

                            <tr>
                                <td>cpu.idle</td>
                                <td>all(#3)>=90</td>
                                <td>3</td>
                                <td>0</td>
                                <td>&nbsp;</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="tem_con">
                        <div class="xq_block">
                            <h5 class="text-yellow">模板报警配置，对模板中的所有策略生效</h5>
                        </div>
                        <div class="app_active_num f14 alert alert_min">
                            <p>
                                <span><b>uic：</b><a href="#"> adminteam </a></span>
                                <span class="pull-right"><a href="#">去UIC修改报警组内成员</a></span>
                            </p>

                        </div>

                    </div>

                </div>

            </div>

        </div>
	</template:replace>
</template:include>