<%@page import="com.harmazing.framework.authorization.IUser"%>
<%@page import="com.harmazing.framework.util.WebUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<!------APP部署头部------->
<div class="app_top">
    <div class="head">
        <div class="app_logo sub_logo">
            <a href="${ WEB_APP_PATH }/portal/index.do"><img src="${ WEB_APP_PATH }/assets/images/logo_app.png" width="200" height="62"/></a>
        </div>
        <div class="sub_logo_app" style="display: none;"><a href="${ WEB_APP_PATH }/portal/home.do"><img src="${ WEB_APP_PATH }/assets/images/app_logo.png" width="100" height="63"></a>
        </div>
<%--         <div class="app_button">
            <a class="bg_green" href="${ WEB_APP_PATH }/app/create.do"><i class="icons add_ico"></i>创建APP</a>
        </div> --%>
       
        <div class="app_user">
            <div class="user_hits">
                <div class="nav_user">
                	<%
                	IUser user = WebUtil.getUserByRequest(request);
                	if(user == null || user.isAnonymous()){
                	%>
                	<a href="${ WEB_APP_PATH }/login.jsp">登录</a>
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
