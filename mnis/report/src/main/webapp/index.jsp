<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>demo</title>
		<link rel="stylesheet" type="text/css" href="./resources/style/normalize.css">
		<link rel="stylesheet" type="text/css" href="./resources/js/easyui/themes/bootstrap/easyui.css">
		<link rel="stylesheet" type="text/css" href="./resources/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="./resources/style/smartMenu.css">
		<link rel="stylesheet" type="text/css" href="./resources/style/demo.css">
		<script type="text/javascript" src="./resources/js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="./resources/js/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="./resources/js/jquery-validate.js"></script>
		<script type="text/javascript" src="./resources/js/jquery-smartMenu-min.js"></script>
		<script type="text/javascript" src="./resources/js/commons.js"></script>
		<script type="text/javascript" src="./resources/js/documents.js"></script>
	</head> 
	<body>
		<iframe id="printFrame" src="" style="display:none;width:800px;height:600px;"></iframe>
		<div class="process"></div>
		<div id="wrapper" class="easyui-layout wrapper">
			<div data-options="region:'west',collapsible:false,title:''" style="width:220px;padding-left:5px;">
				
				<form action="upload" method="post" enctype="multipart/form-data" style="height:90px;margin-left: -5px;">
				<div class="top-tool">
					<div class="upload-box">
						<div><input type="file" id="temFile" name="temFile"></div>
						<div>
						名称：<input type="text" id="fName" name="fName">
						<input type="submit" value="上传" style="height: 20px;line-height: 16px;margin-left:10px;"></div>
					</div>
					<div class="upload-type">
						科室：<select id="dept_code" name="dept_code">
							<option value="1">科室一</option>
							<option value="2">科室二</option>
						</select>
						科室：<select id="doc_type" name="doc_type">
							<option value="1">临床报告</option>
							<option value="2">护理记录</option>
						</select>
					</div>
				</div>
				</form>
				<div class="mid-tree">
					<ul id="statement" class="easyui-tree"></ul>
					<!--<ul id="editList" class="easyui-tree"></ul>-->
				</div>
			</div>
			<div data-options="region:'east',collapsible:false,title:''" style="width:100px;padding:10px;">
				<div class="action-tool">
					<div class="action-box">
						<!-- <input type="text" list="statement_list"> -->
						<input type="button" id="addNewLine" value="新增">
						<input type="button" id="save" value="保存">
						<input type="button" id="print" value="打印">
						<datalist id="statement_list">
						    <option label="" value="无" />
							<option label="" value="报表行1" />
							<option label="" value="报表行2" />
							<option label="" value="报表行3" />
							<option label="" value="报表行4" />
						</datalist>
					</div>
				</div>
			</div>
			<div data-options="region:'center',title:''">
				<div class="viewBox" id="viewBox">
					<iframe id="viewDoc" src="" frameborder=0 scrolling=no></iframe>
				</div>
				<div class="editBox" id="editBox"></div>
				<div id="dataElm" style="display:none;"></div>
				<div id="mm" class="easyui-menu" style="width:120px;">
					<div onclick="parser.removeTreeNode()" data-options="iconCls:'icon-remove'">移除该条记录</div>
				</div>
			</div>
		<script type="text/javascript" src="./resources/js/main.js"></script>
	</body>
</html>
