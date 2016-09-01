<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta charset="UTF-8">
<title>出错了</title>
<link rel="shortcut icon" HREF="<%=request.getContextPath()%>/resources/img/favicon.ico">
</head>
<body class="commonBody">
<table class="commonWrapTable">
	<tr>
		<td style="text-align:center;">
            <table align="center">
            	<tr>
            		<td class="confirmTd">
            			<img src="<%=request.getContextPath()%>/resources/img/error.gif"/>	
            		</td>
					<td style="width:20px;"></td>
            		<td class="tdNowrap">
            			<%Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");%>
            			<b class="tdRed">Error:<%=throwable%></b>
						<br>
						<%	
							if (throwable != null) {
								StackTraceElement[] stackTraceElements = throwable.getStackTrace();
								for (int i = 0; i < stackTraceElements.length; i++) {						
									out.print(stackTraceElements[i].toString() + "\n");
								}
							}
						%>
            		</td>
            	</tr>
            </table>
		</td>
	</tr>
</table>
</body>
</html>  