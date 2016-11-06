<!doctype html>
<%@page import="com.harmazing.framework.util.ConfigUtil"%>
<html lang="en">
<head>
    <link rel="stylesheet" href="../assets/css/layout.css"/>
    <link rel="stylesheet" href="../assets/css/welcom.css"/>
</head>
<div id="secondary" class="st-menu" >
    <h2>OpenBridge</h2>
    <ul>
        <li>
            <a target="_blank"  href="/"><span>Openbridge Portal</span></a>

        </li>
        <li>
            <a target="_blank"  href="<%= ConfigUtil.getConfigString("paasos.app.url") %>"><span>APPFactory</span></a>

        </li>
        <li>
            <a target="_blank"  href="<%= ConfigUtil.getConfigString("paasos.api.url") %>"><span>APIManager</span></a>

        </li>
        <li>
            <a target="_blank"  href="<%= ConfigUtil.getConfigString("paasos.web.url") %>"><span>PaaSOS</span></a>

        </li>
    </ul>
</div>