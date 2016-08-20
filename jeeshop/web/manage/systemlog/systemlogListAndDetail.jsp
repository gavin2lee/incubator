<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page session="false"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
</head>

<body style="font-size: 12px;">
		<div style="text-align: center; border: 0px solid red; margin: auto;">
			<div
				style="text-align: right; border: 0px solid red; margin: auto; padding: 10px;">
				<s:iterator value="pager.list">
					<div>
						<div style="text-align: left;"><h4><s:property value="title" escapeHtml="false"/></h4></div>
						<div><s:property value="content" escapeHtml="false"/></div>
					</div>
				</s:iterator>
			</div>
		</div>
</body>
</html>
