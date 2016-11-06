<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<div class="m_message" id="divInfo" >
        <div class="form_control p20">
            <div class="form_block form_block-xsm">
                <label class="label-sm">邮箱：</label>
                <div class="specList">
                    <p class="f14" style="line-height: 26px;">${ user.email }</p>
                </div>
            </div>
            <div class="form_block form_block-xsm">
                <label class="label-sm">电话：</label>
                <div class="specList">
                    <p class="f14" style="line-height: 26px;">${ user.mobile }</p>
                </div>
            </div>
        </div>
    </div>
    </template:replace>
    </template:include>