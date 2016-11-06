<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp">
	<template:replace name="content"> 
	<style type="text/css">
	.form_block {
    padding: 10px;
}

.form_block label {
    width: 170px;
    text-align: left;
    display: inline-block;
    font-size: 14px;
    padding-left: 30px;
    color: #555;
    float: left;
    line-height: 24px;
    margin-top: 1px;
    background: transparent;
}

textarea, select {
    border: 1px solid #eaeaea;
    padding: 3px 5px;
}

input {
    border: 1px solid #eaeaea;
    padding: 3px 5px;
    line-height: 24px;
    width: 260px;
}

input[type=checkbox], input[type=radio] {
    width: auto;
}

.form_block span {
    display: inline-block;
    line-height: 24px;
}
.w100 {
    width: 100px;
}

.w150 {
    width: 150px;
}
.btn {
    padding: 8px 25px;
    cursor: pointer;
}

.btn_sm {
    padding: 4px 12px;
    cursor: pointer;
}
.btn-sm {
    padding: 7px 12px 7px;
    cursor: pointer;
}
.btn-yellow2 {
    color: #e9b525;
    border: 1px solid #eaeaea;
    background: #eaeaea;
}

a.btn-yellow2 {
    color: #e9b525;
}

.btn-yellow2:hover {
    color: #fff;
    border: 1px solid #e9b525;
    background: #e9b525;
}
input, textarea, select {
    transition: border linear 0.2s, box-shadow linear 0.2s;
}

input, textarea, select {
    display: inline-block;
    color: #555555;
    background-color: #ffffff;
    border: 1px solid #e1e8ed;
    border-radius: 2px;
}

input:focus,
textarea:focus,
select:focus {
    border-color: rgba(233, 181, 37, 0.5);
    outline: 0;
    outline: thin dotted \9; /* IE6-9 */
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(233, 181, 37, 0.3);
}
.help_text {
    margin-top: 10px;
}

.help_text span {
    line-height: 22px;
}

.help_text span em {
    color: #f00;
}


.pages_left {
    line-height: 24px;
    margin-top: 10px;
    margin-left: 3px;
}

.tab_title {
    width: 100%;
    height: auto;
    text-align: center;
}

.title_tab_item {
    float: none;
    position: relative;
    padding-top: 10px;
}

.title_tab_item ul li {
    float: none;
    margin: 0 25px;
    display: inline-block;
    position: relative;
}

.title_tab_item h5 a {
    line-height: 36px;
    position: relative;
    top: 1px;
    font-size: 15px;
}

/*tab*/

.title_tab_item_pas {

}

.title_tab_item_pas ul li {
    float: none;
    margin: 0 15px;
    display: inline-block;
    position: relative;
    font-size: 13px;
}

.title_tab_item_pas h5 a {
    /*border-top: 2px solid #adadad;*/
    float: left;
    line-height: 29px;
    position: relative;
    z-index: 1;
    color: #777;
    margin-top: 2px;
    background: #fff;
    line-height: 36px;
    top: 1px;
    font-size: 14px;
    padding: 0 30px;
    border-bottom: 0;
    background: transparent;
}

.title_tab_item_pas h5 a.active {
    border-top: 2px solid #e9b525;
    color: #e9b525;
    border-bottom: 0;
    background: #f9f9f9;
    border-left: 1px solid #eaeaea;
    border-right: 1px solid #eaeaea;
}

.title_tab_item_pas h5 a:hover {
    border-top: 2px solid #e9b525;
    color: #e9b525;
    border-bottom: 0;
    background: #f9f9f9;
    border-left: 1px solid #eaeaea;
    border-right: 1px solid #eaeaea;
}

.r_title {
    height: 30px;
    position: relative;
}

.r_title h3 {
    border-bottom: 1px solid #2283c5;
    float: left;
    margin-left: 0;
    line-height: 29px;
    position: relative;
    z-index: 9;
    margin-right: 20px;
}

.r_title h3.gray {
    border-bottom: 0 solid #2283c5;
}

.h3_color {
    color: #4d8eba;
}

.r_title h3.gray {
    color: #a7a7a7;
}

.title_line {
    background: #e8e7ed;
    height: 1px;
    position: absolute;
    bottom: 0;
    width: 100%;
}

.h3_btm_line {
    border-bottom: 1px solid #000;
}
.form-right {
    margin-left: 200px;
}
	</style>
	       <div class="left_sub_menu left_sub_menu2">
	           <ul> 
	               <li>
	               	  <a ${ param['active'] eq 'config' ? ' class="select" ' : '' } href="${WEB_APP_PATH}/sys/config/view.do">
	                   	<i class="left_menu_ico l_ico03"></i>系统配置
	                    </a>
	               </li> 
	           </ul>
	       </div>
	       <div class="right_body">
		       <div class="app_name">
		           <a href="javascript:void(0)"><i class="icons go_back_ico"></i></a>
		           <p class="app_a">系统配置</p>
		           <template:block name="content-path">
			
			      </template:block>
		        </div>
	        	<div class="plate">
		            <div class="project_r "> 
						<template:block name="detail"></template:block>
					</div>
				</div>
			</div>
	</template:replace>
</template:include>
