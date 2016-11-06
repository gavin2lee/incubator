<%@page import="com.harmazing.framework.util.WebUtil"%>
<%@page import="com.harmazing.framework.authorization.IUser"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%> 
<%
 IUser user = WebUtil.getUserByRequest(request);
 if(user.isAnonymous()){
	 response.sendRedirect(request.getAttribute("WEB_APP_PATH")+"/");
 }
%>
<template:include file="/common/template/simple.jsp">
	<template:replace name="title">
		PaaSOS
	</template:replace>
	<template:replace name="body"> 
	<c:import url="/common/include/header.jsp"></c:import>
	 <div class="app_content">
    <div class="content_frame">
        <div class="plate">
            <div class="project_r">
                <div class="r_block p20" style="padding:10px 20px;">
                    <div class="r_con">
                        <div class="details_nr">
                            <div class="mirro_list mirro_list2">
                                <div class="cluster_block_three cluster_block_one text-center active">
                                    <div class="cluster_block_con">
                                        <div class="cluster-header">
                                            <h3 class="cluster_name">组织信息</h3>
                                        </div>
                                        <div class="cluster-content cluster-content_3 m-ovresource" style="padding: 15px 0;">
                                            <div class="ovresource_ct" style="width: 32.5%; padding-top: 15px;">
                                                <dl>
                                                    <dt><a href="${ WEB_APP_PATH }/manager/tenant/list.do" class="txt-bright link-line">${TenantName}</a></dt>
                                                    <dd>组织名称</dd>
                                                </dl>
                                                <dl>
                                                    <dt><a href="${ WEB_APP_PATH }/project/index.do" class="txt-safe link-line">${Tenantcount}</a></dt>
                                                    <dd>项目数</dd>
                                                </dl>
                                            </div>
                                            <div class="ovresource_ct" style="width: 31.5%; padding-left: 2%; border-left: 1px solid #eaeaea;">
                                                <h3 class="ovresource_tt">主机
                                                    <small>Host</small>
                                                </h3>
                                                <div class="ovresource_status"> <i
                                                        class="icons ico_normal mr5"></i><span>在线</span><span><a class="txt-bright link-line" href="#">${ empty host ? "-":host.online } </a></span>
                                                </div>
                                                <div class="ovresource_status"><i
                                                        class="icons ico_warning mr5"></i><span>异常</span><span><a class="txt-safe link-line" href="#">${ hostoutline } </a></span>
                                                </div>
                                            </div>
                                            <div class="ovresource_ct" style="width: 31%; padding-left: 2%; border-left: 1px solid #eaeaea;">
                                                <h3 class="ovresource_tt">镜像
                                                    <small>Image</small>
                                                </h3>
                                                <div class="ovresource_status"><span>项目镜像</span><span><a class="txt-safe link-line" href="#">${ empty Image ? "-": Image.preset } </a></span>
                                                </div>
                                                <div class="ovresource_status"><span>基础镜像</span><span><a class="txt-bright link-line" href="${ WEB_APP_PATH }/manager/baseimage/list.do">${ empty Image ? "-": Image.base } </a></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <h2 class="u-cardtitle">项目管理</h2>
                    <div class="g-row">
                        <div class="g-col g-col-4">
                            <div class="m-card m-card-smh">
                                <div class="m-ovresource">
                                    <div class="ovresource_mn">
                                        <div class="oversource_left">
                                            <div class="indicator3 in_color" id="indicatorContainer"></div>
                                        </div>
                                        <div class="ovresource_ct">
                                            <h3 class="ovresource_tt">项目<small>Project</small>
                                            </h3>
                                            <div class="ovresource_status"><span> OpenBridge-app/api</span><a href="${ WEB_APP_PATH }/project/index.do" class="txt-bright link-line">${ openbridge }</a>
                                            </div>
                                            <div class="ovresource_status"><span>预置应用</span><a href="${ WEB_APP_PATH }/project/index.do" class="txt-safe link-line">${ store }</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="g-col g-col-4">
                            <div class="m-card m-card-smh">
                                <div class="m-ovresource">
                                    <div class="ovresource_mn">
                                        <div class="oversource_left">
                                            <div class="indicator3 in_color2" id="indicatorContainer2"></div>
                                        </div>
                                        <div class="ovresource_ct">
                                            <h3 class="ovresource_tt">部署
                                                <small>Deploy</small>
                                            </h3>
                                            <div class="ovresource_status"><span><i
                                                    class="icons ico_normal mr5"></i>运行中</span><a href="${ WEB_APP_PATH }/manager/deploy/list.do" class="txt-bright link-line">${countrun}</a>
                                            </div>
                                            <div class="ovresource_status"><span><i
                                                    class="icons ico_warning mr5"></i>停止</span><a href="${ WEB_APP_PATH }/manager/deploy/list.do" class="txt-safe link-line">${countstop}</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="g-col g-col-4" style="display: none;">
                            <div class="m-card m-card-smh">
                                <div class="m-ovresource">
                                    <div class="ovresource_mn">
                                        <div class="oversource_left">
                                            <div class="indicator3 in_color3" id="indicatorContainer3"></div>
                                        </div>
                                        <div class="ovresource_ct">
                                            <h3 class="ovresource_tt">实例
                                                <small>Instance</small>
                                            </h3>
                                            <div class="ovresource_status"><span><i
                                                    class="icons ico_normal mr5"></i>正常</span><a href="#" class="txt-bright link-line">0</a>
                                            </div>
                                            <div class="ovresource_status"><span><i
                                                    class="icons ico_warning mr5"></i>异常</span><a href="#" class="txt-safe link-line">0</a>
                                            </div>

                                        </div>
                                        <div style="display: none;">
                                            <a class="u-btn ovresource_btn" href="#"
                                               onclick="_gaq.push(['_trackEvent', 'summary', 'Clusters', 'create']);">创建集群</a>
                                        </div>
                                        <a class="ovresource_next"><i class="u-icon u-icon-next"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <h2 class="u-cardtitle">资源服务</h2>
                    <div class="g-row">
                        <div class="g-col g-col-4">
                            <div class="m-card m-card-smh">
                                <div class="m-ovresource">
                                    <div class="ovresource_mn">
                                        <div class="oversource_left">
                                            <div class="indicator3 in_color4 indicatorContainer" ></div>
                                        </div>
                                        <div class="ovresource_ct">
                                            <h3 class="ovresource_tt">数据库<small>Mysql</small>
                                            </h3>
                                            <div class="ovresource_status">&nbsp;<i
                                                    class="icons ico_normal mr5"></i><span>运行</span><a href="${ WEB_APP_PATH }/resource/mysql/index.do" class="txt-bright link-line"><span id="runMysql"></span></a>
                                            </div>
                                            <div class="ovresource_status"><i
                                                    class="icons ico_warning mr5"></i><span>异常</span><a href="${ WEB_APP_PATH }/resource/mysql/index.do"  class="txt-safe link-line"><span id="stoppedMysql"></span></a>
                                            </div>
                                        </div>
                                        <div style="display: none;">
                                            <a class="u-btn ovresource_btn" href="#">创建集群</a>
                                        </div>
                                        <a class="ovresource_next"><i class="u-icon u-icon-next"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="g-col g-col-4" style="display: none;">
                            <div class="m-card m-card-smh">
                                <div class="m-ovresource">
                                    <div class="ovresource_mn">
                                        <div class="oversource_left">
                                            <div class="indicator3 in_color5 indicatorContainer2" ></div>
                                        </div>
                                        <div class="ovresource_ct">
                                            <h3 class="ovresource_tt">存储
                                                <small>Storage</small>
                                            </h3>
                                            <div class="ovresource_status"><span><i
                                                    class="icons ico_normal mr5"></i>正常</span><a href="#" class="txt-bright link-line">0</a>
                                            </div>
                                            <div class="ovresource_status"><span><i
                                                    class="icons ico_warning mr5"></i>异常</span><a href="#" class="txt-safe link-line">0</a>
                                            </div>

                                        </div>
                                        <div style="display: none;">
                                            <a class="u-btn ovresource_btn" href="#"
                                               onclick="_gaq.push(['_trackEvent', 'summary', 'Clusters', 'create']);">创建集群</a>
                                        </div>
                                        <a class="ovresource_next"><i class="u-icon u-icon-next"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="g-col g-col-4">
                            <div class="m-card m-card-smh">
                                <div class="m-ovresource">
                                    <div class="ovresource_mn">
                                        <div class="oversource_left">
                                            <div class="indicator3 in_color indicatorContainer3"></div>
                                        </div>
                                        <div class="ovresource_ct">
                                            <h3 class="ovresource_tt">消息中间件
                                                <small>RabbitMQ</small>
                                            </h3>
                                            <div class="ovresource_status"><span><i
                                                    class="icons ico_normal mr5"></i>正常</span><a href="${ WEB_APP_PATH }/resource/messagequeue/index.do"  class="txt-bright link-line"><span id="runMq"></span></a>
                                            </div>
                                            <div class="ovresource_status"><span><i
                                                    class="icons ico_warning mr5"></i>异常</span><a href="${ WEB_APP_PATH }/resource/messagequeue/index.do"  class="txt-safe link-line"><span id="stoppedMq"></span></a>
                                            </div>

                                        </div>
                                        <div style="display: none;">
                                            <a class="u-btn ovresource_btn" href="#"
                                               onclick="_gaq.push(['_trackEvent', 'summary', 'Clusters', 'create']);">创建集群</a>
                                        </div>
                                        <a class="ovresource_next"><i class="u-icon u-icon-next"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="g-col g-col-4">
                            <div class="m-card m-card-smh">
                                <div class="m-ovresource">
                                    <div class="ovresource_mn">
                                        <div class="oversource_left">
                                            <div class="indicator3 in_color2 indicator_redis" ></div>
                                        </div>
                                        <div class="ovresource_ct">
                                            <h3 class="ovresource_tt">高速缓存
                                                <small>Redis</small>
                                            </h3>
                                            <div class="ovresource_status"><span><i
                                                    class="icons ico_normal mr5"></i>正常</span><a href="${ WEB_APP_PATH }/resource/redis/index.do"  class="txt-bright link-line"><span id="runredis"></span></a>
                                            </div>
                                            <div class="ovresource_status"><span><i
                                                    class="icons ico_warning mr5"></i>异常</span><a href="${ WEB_APP_PATH }/resource/redis/index.do"  class="txt-safe link-line"><span id="stoppedredis"></span></a>
                                            </div>

                                        </div>
                                        <div style="display: none;">
                                            <a class="u-btn ovresource_btn" href="#"
                                               onclick="_gaq.push(['_trackEvent', 'summary', 'Clusters', 'create']);">创建集群</a>
                                        </div>
                                        <a class="ovresource_next"><i class="u-icon u-icon-next"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ WEB_APP_PATH }/assets/js/radialIndicator.js"></script>
<script type="text/javascript">
    function adjustHeight() {
        var h = $(window).height();
        $(".plate").css("height", h - 63);
    }
    $(document).ready(function () {
                adjustHeight();
            }
    )
    $(window).resize(function () {
        adjustHeight();
    });
</script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#indicatorContainer').radialIndicator({
            barColor: '#f8ee99',
            radius: 40,
            barWidth: 3,
            initValue: 0,
            roundCorner: true,
            percentage: true
        });
        var radialObj = $('#indicatorContainer').data('radialIndicator');
        //now you can use instance to call different method on the radial progress.
        //like
        var a=${openbridge};
        var b=${Tenantcount};
        var c =a*100/(b==0?1:b);
        radialObj.animate(c);
    });
    $(document).ready(function () {
        $('#indicatorContainer2').radialIndicator({
            barColor: '#87CEEB',
            radius: 40,
            barWidth: 3,
            initValue: 0,
            roundCorner: true,
            percentage: true
        });
        var radialObj = $('#indicatorContainer2').data('radialIndicator');
        //now you can use instance to call different method on the radial progress.
        //like
        var a=${countrun};
        var b=${countstop};
        
        var c =a*100/((a+b)==0?1:(a+b));
        radialObj.animate(c);
    });

    $(document).ready(function () {
        $('#indicatorContainer3').radialIndicator({
            barColor: '#f8ee99',
            radius: 40,
            barWidth: 3,
            initValue: 0,
            roundCorner: true,
            percentage: true
        });
        var radialObj = $('#indicatorContainer3').data('radialIndicator');
        //now you can use instance to call different method on the radial progress.
        //like
        radialObj.animate(40);
    });
    $(document).ready(function () {
    	var url="${WEB_APP_PATH}/resource/mysql/getInstances.do";
    	var runningCount1=0;
    	var stoppedCount1=0;
    	$.getJSON(url,function(json){
    		if(json.code==0){
    			if(!jQuery.isEmptyObject(json.data)){
    		         runningCount1=json.data.runningCount;
    		         stoppedCount1=json.data.stoppedCount;
    		         $("#runMysql").empty();
    		         $("#stoppedMysql").empty();
    		         $("#runMysql").append(runningCount1);
    		         $("#stoppedMysql").append(stoppedCount1);
    		         var radialObj = $('.indicatorContainer').data('radialIndicator');
    		         var c =runningCount1*100/((runningCount1+stoppedCount1)==0?1:(runningCount1+stoppedCount1));
    	             radialObj.animate(c);
    			}
    		}
    	});
        $('.indicatorContainer').radialIndicator({
            barColor: '#f8ee99',
            radius: 40,
            barWidth: 3,
            initValue: 0,
            roundCorner: true,
            percentage: true
        });
        
         $("#runMysql").append(runningCount1);
		 $("#stoppedMysql").append(stoppedCount1);
		 var radialObj = $('.indicatorContainer').data('radialIndicator');
		 var c =runningCount1*100/((runningCount1+stoppedCount1)==0?1:(runningCount1+stoppedCount1));
	     radialObj.animate(c);
        //var radialObj = $('.indicatorContainer').data('radialIndicator');
        //now you can use instance to call different method on the radial progress.
        //like
        //var c =runningCount*100/((runningCount+stoppedCount)==0?1:(runningCount+stoppedCount));
      //  radialObj.animate(c);
    });
    $(document).ready(function () {
        $('.indicatorContainer2').radialIndicator({
            barColor: '#f8ee99',
            radius: 40,
            barWidth: 3,
            initValue: 0,
            roundCorner: true,
            percentage: true
        });
        var radialObj = $('.indicatorContainer2').data('radialIndicator');
        //now you can use instance to call different method on the radial progress.
        //like
        radialObj.animate(30);
    });
    $(document).ready(function () {
    	
    	var url="${WEB_APP_PATH}/resource/messagequeue/getInstances.do";
    	var runningCount2=0;
    	var stoppedCount2=0;
    	$.getJSON(url,function(json){
    		if(json.code==0){
    			if(!jQuery.isEmptyObject(json.data)){
    			    runningCount2=json.data.runningCount;
    		        stoppedCount2=json.data.stoppedCount;   		
    		        $("#runMq").empty();
   		            $("#stoppedMq").empty();
    		        $("#runMq").append(runningCount2);
    		        $("#stoppedMq").append(stoppedCount2);
    		        var radialObj = $('.indicatorContainer3').data('radialIndicator');
    		        var c =runningCount2*100/((runningCount2+stoppedCount2)==0?1:(runningCount2+stoppedCount2));
    	            radialObj.animate(c);
    	          }
    		}
    	});
    	
        $('.indicatorContainer3').radialIndicator({
            barColor: '#f8ee99',
            radius: 40,
            barWidth: 3,
            initValue: 0,
            roundCorner: true,
            percentage: true
        });
         
         $("#runMq").append(runningCount2);
		 $("#stoppedMq").append(stoppedCount2);
		 var radialObj = $('.indicatorContainer3').data('radialIndicator');
		 var c =runningCount2*100/((runningCount2+stoppedCount2)==0?1:(runningCount2+stoppedCount2));
	     radialObj.animate(c);
        //var radialObj = $('.indicatorContainer3').data('radialIndicator');
        //now you can use instance to call different method on the radial progress.
        //like
      //  var c =runningCount*100/((runningCount+stoppedCount)==0?1:(runningCount+stoppedCount));
       // radialObj.animate(c);
    });
    
    $(document).ready(function () {
    	
    	var url="${WEB_APP_PATH}/resource/redis/getInstances.do";
    	var runningCount3=0;
    	var stoppedCount3=0;
    	$.getJSON(url,function(json){
    		if(json.code==0){
    			if(!jQuery.isEmptyObject(json.data)){
    		          runningCount3=json.data.runningCount;
    		          stoppedCount3=json.data.stoppedCount;
    		          $("#runredis").empty();
    		          $("#stoppedredis").empty();
    		          $("#runredis").append(runningCount3);
    		          $("#stoppedredis").append(stoppedCount3);
    		          var radialObj = $('.indicator_redis').data('radialIndicator');
    		          var c =runningCount3*100/((runningCount3+stoppedCount3)==0?1:(runningCount3+stoppedCount3));
    	              radialObj.animate(c);
    			}
    		}
    	});
    	
        $('.indicator_redis').radialIndicator({
            barColor: '#87CEEB',
            radius: 40,
            barWidth: 3,
            initValue: 0,
            roundCorner: true,
            percentage: true
        });
         $("#runredis").html(runningCount3);
		 $("#stoppedredis").html(stoppedCount3);
		 var radialObj = $('.indicator_redis').data('radialIndicator');
		 var c =runningCount3*100/((runningCount3+stoppedCount3)==0?1:(runningCount3+stoppedCount3));
	     radialObj.animate(c);
        //now you can use instance to call different method on the radial progress.
        //like
        
    });
</script>
		
	</template:replace>
</template:include>