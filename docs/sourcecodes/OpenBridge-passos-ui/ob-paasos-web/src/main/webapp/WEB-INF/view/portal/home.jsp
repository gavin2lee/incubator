    <%@page import="com.harmazing.framework.util.WebUtil" %>
        <%@page import="com.harmazing.framework.authorization.IUser" %>
        <%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
        <%@ include file="/common/header.jsp" %>
            <% response.addHeader("login", "true"); %>
        <template:include file="/common/template/simple.jsp">
            <template:replace name="title">
                PaaSOS
            </template:replace>
            <template:replace name="body">
<c:import url="/common/include/header.jsp"></c:import>
<!------APP部署内容------->
<div class="app_content">
    <div class="content_frame">
        <div class="plate plate_con plate_index plate_index_pas">
            <div class="app_wel_block">
                <div class="app_wel_text">
                    <p>欢迎进入PaaSOS</p>
                    <h2>您的企业级资源平台</h2>
                </div>
                <div class="app_wel_quick">
                    <div class="quick_block">
                        <div class="quick_ico">
                            <i class="q_ico01"></i>
                        </div>
                        <div class="quick_nr">
                            <dl>
                                <dt>项目管理</dt>
                                <dd>
                                    <p>可进行多容器编排灵活使用</p>
                                    <p>支持多种开发语言</p>
                                </dd>
                            </dl>
                            <a href="../project/index.do">马上查看所有项目 ></a>
                        </div>
                    </div>
                    <div class="quick_block">
                        <div class="quick_ico">
                            <i class="q_ico02"></i>
                        </div>
                        <div class="quick_nr">
                            <dl>
                                <dt>资源服务</dt>
                                <dd>
                                    <p>从镜像可以一键生成对应的容器服务</p>
                                    <p>快速投入应用</p>
                                </dd>
                            </dl>
                            <a href="../resource/mysql/index.do">马上要知道 ></a>
                        </div>
                    </div>
                    <div class="quick_block">
                        <div class="quick_ico">
                            <i class="q_ico03"></i>
                        </div>
                        <div class="quick_nr">
                            <dl>
                                <dt>环境管理</dt>
                                <dd>
                                    <p>全方面了解我创建与使用的容器资源</p>
                                    <p>全方面管理与监控</p>
                                </dd>
                            </dl>
                            <a href="../manager/tenant/list.do">去看看 ></a>
                        </div>
                    </div>
                    <div class="quick_block">
                        <div class="quick_ico">
                            <i class="q_ico04"></i>
                        </div>
                        <div class="quick_nr">
                            <dl>
                                <dt>预置应用</dt>
                                <dd>
                                    <p>管理数据库、快速缓存、负载均衡、<br>消息队列等与我相关的基础服务资源
                                    </p>
                                </dd>
                            </dl>
                            <a href="../store/app/list.do">去看看 ></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--子产品切换菜单开始-->
    <div id="modal" class="st-menu" style="display: none;">
        <h2>OpenBridge</h2>
            <ul>
                <li>
                    <a href="http://www.openbridge.cn"><span>Portal</span></a>

                </li>
                <li>
                    <a href="#"><span>IaaSOS</span></a>

                </li>
                <li>
                    <a href="#"><span>PaaSOS</span></a>

                </li>
                <li>
                    <a href="http://demo.openbridge.cn/api/portal/home.do"><span>APIManager</span></a>

                </li>
                <li>
                    <a class="" href="#"><span>BizNavi</span></a>

                </li>
            </ul>
        <div class="close_slider"><a  href="javascript:$.pageslide.close()">×</a></div>
    </div>
    <!--子产品菜单切换结束-->
</div>
            </template:replace>
            <template:replace name="bottom">
                <script type="text/javascript">
                function adjustHeight() {
                var h = $(window).height();
                var h2 = $(".app_wel_block").height();
                var h3 = $(".head").height();
                $(".plate").css("height", h - h3);
                $(".app_wel_block").css("margin-top", ((h - h3 - h2) / 2));
                }
                $(document).ready(function () {
                     adjustHeight();
                }
                )
                $(window).resize(function () {
                adjustHeight();
                });
                
                $(document).ready(function () {
                    $('.quick_block').mousemove(function () {
                        $(this).addClass("active");//可以自己修改速度
                    });
                    $('.quick_block').mouseleave(function () {
                        $(this).removeClass("active");
                    });               
                });
                </script>
            </template:replace>
        </template:include>