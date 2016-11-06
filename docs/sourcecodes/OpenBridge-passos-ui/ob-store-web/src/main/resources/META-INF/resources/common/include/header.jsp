<%@page import="com.harmazing.framework.authorization.IUser"%>
<%@page import="com.harmazing.framework.util.WebUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 		prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" 		prefix="fmt" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!------passos头部------->
<div class="app_top pas_top">
    <div class="head">
        <div class="app_logo sub_logo">
            <a href="${WEB_APP_PATH}/index.jsp"><img src="${WEB_APP_PATH}/assets/images/logo_pas.png" width="200" height="62"/></a>
        </div>
        <div class="nav_tips">
            <div class="news app_number">
                <a class="${ param['nav-header'] eq 'monitor' ? 'active' : '' }" href="${WEB_APP_PATH}/monitor/architecture/index">
                    <div class="news_img">
                        <i class="icons item_ico"></i>
                    </div>
                    <div class="title_name">智能监控</div>
                </a>
            </div>
            <div class="news">
                <a class="${ param['nav-header'] eq 'alarm' ? 'active' : '' }" href="${WEB_APP_PATH}/groups">
                    <div class="news_img">
                        <i class="icons app_ico"></i>
                    </div>
                    <div class="title_name">告警设置</div>
                </a>
            </div>
        </div>
        <div class="app_user">
            <div class="user_hits">
                <div class="nav_user">
                	<%
                	IUser user = WebUtil.getUserByRequest(request);
                	if(user == null || user.isAnonymous()){
                	%>
                	<ul class="un_login">
                	   <li>
                	       <a href="${ WEB_APP_PATH }/login.jsp">登录</a>
                	   </li>
                	</ul>
                	
                	<%
                	}else{
                	%>
                    <ul>
                        <li class="menu2" onMouseOver="this.className='menu1'" onMouseOut="this.className='menu2'">
                            <i class="icons user_ico"></i>

                            <p class="user_name"><%= user.getUserName() %></p>
                            <i class="icons arrow-btm"></i>

                            <div class="list">
                            	<br/>
                                <a href="${ WEB_APP_PATH }/sys/config/view.do">系统配置</a><br/>
                                <a href="${ WEB_APP_PATH }/logout.jsp">退出</a><br/>
                            </div>
                        </li>
                    </ul>
                    <% } %>
                </div>
            </div>
        </div>
    	<div class="ob_sub_pro_btn">
            <a href="javascript:$.pageslide({ direction: 'left', href: '${ WEB_APP_PATH }/common/pro_list.jsp' })">
                <i class="icons ob_sub_pro"></i>
            </a>
        </div>
    </div>
</div>
<script src="${ WEB_APP_PATH }/assets/js/jquery.pageslide.js"></script>
<script>
    /* Default pageslide, moves to the right */
    $(".first").pageslide();

    /* Slide to the left, and make it model (you'll have to call $.pageslide.close() to close) */
    $(".second").pageslide({ direction: "left", modal: true });
</script>
