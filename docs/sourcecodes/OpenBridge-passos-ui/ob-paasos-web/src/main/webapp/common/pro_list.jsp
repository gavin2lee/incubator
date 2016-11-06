<%@ page language="java" pageEncoding="UTF-8"%>
<!doctype html>
<%@page import="com.harmazing.framework.util.ConfigUtil"%>
<html lang="en">
<head>
    <link rel="stylesheet" href="../assets/css/layout.css"/>
    <link rel="stylesheet" href="../assets/css/welcom.css"/>
</head>
<div id="secondary" class="st-menu2" >
    <h2>
    <a target="_blank" href="/home/portal/home.do">
    <i class="ob_logo"></i>
    </a>
    </h2>
    <%--<ul>--%>
        <%--<li>--%>
            <%--<a target="_blank"  href="<%= ConfigUtil.getConfigString("paasos.app.url") %>"><span>APPFactory</span></a>--%>

        <%--</li>--%>
        <%--<li>--%>
            <%--<a target="_blank"  href="<%= ConfigUtil.getConfigString("paasos.api.url") %>"><span>APIManager</span></a>--%>

        <%--</li>--%>
        <%--<li>--%>
            <%--<a target="_blank"  href="<%= ConfigUtil.getConfigString("paasos.web.url") %>"><span>PaaSOS</span></a>--%>

        <%--</li>--%>
    <%--</ul>--%>

    <ul>
    <li>
    <p>
    <a target="_blank"  href="<%= ConfigUtil.getConfigString("paasos.app.url") %>">
    <i class="s_app_logo"></i>
    </a>
    </p>

    <div class="r_li green">
    <dl>
    <a target="_blank"  href="<%= ConfigUtil.getConfigString("paasos.app.url") %>">
    <dt>APPFactory</dt>
    <dd>轻应用管理平台</dd>
    </a>
    </dl>
    <span><a href="http://doc.openbridge.cn/app/v3/" target="_blank">[说明文档]</a></span>
    </div>

    </li>
    <li>
    <p><a target="_blank"  href="<%= ConfigUtil.getConfigString("paasos.api.url") %>"><i class="s_api_logo"></i></a></p>

    <div class="r_li oranger">
    <dl>
    <a target="_blank"  href="<%= ConfigUtil.getConfigString("apiManagerUrl") %>">
    <dt>APIManager</dt>
    <dd>微服务管理平台</dd>
    </a>

    </dl>
    <span><a href="http://doc.openbridge.cn/api/v3/" target="_blank">[说明文档]</a></span>
    </div>
    </li>
    <li>
    <p><a target="_blank" href="<%= ConfigUtil.getConfigString("paasos.web.url") %>"><i class="s_os_logo"></i></a></p>

    <div class="r_li yellow">
    <dl>
    <a target="_blank" href="<%= ConfigUtil.getConfigString("paasos.web.url") %>">
    <dt>PaaSOS</dt>
    <dd>平台资源管理</dd>
    </a>
    </dl>
    <span><a href="http://doc.openbridge.cn/os/v3/" target="_blank">[说明文档]</a></span>
    </div>
    </li>
    </ul>

    <div class="feed_bak_btn">
    <a href="http://www.openbridge.cn/index/feedback"  target="_blank" class="btn btn-default" ><i class="feed_ico"></i>意见反馈</a>
    </div>
</div>