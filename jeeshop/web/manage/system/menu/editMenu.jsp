<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
</head>

<body>
<div style="text-align: center; border: 0px solid #999;margin: auto;">
	<div style="text-align: center; border: 0px solid #999;
		margin: auto;margin-top: 150px;">
		<s:form action="menu" namespace="/manage" theme="simple">
				<table>
					<tr style="display: none;">
						<th>id</th>
						<td><s:hidden name="menu.rid"/></td>
					</tr>
					<tr style="display: none;">
						<th>pid</th>
						<td><s:hidden name="menu.pid"/></td>
					</tr>
					<tr>
						<th>url</th>
						<td>
							<s:textfield name="menu.url" readonly="false"/>
						</td>
					</tr>
					<tr>
						<th>name</th>
						<td>
							<s:textfield name="menu.name" readonly="false"/>
						</td>
					</tr>
					<tr><td></td>
						<td>
						    <s:submit method="save" value="save"/>
						    <s:submit method="back" value="back"/>
						</td>
					</tr>
				</table>

		</s:form>
	</div>
</div>
</body>
</html>
