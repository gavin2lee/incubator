<%@page import="net.jeeshop.core.front.SystemManager"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page import="net.jeeshop.services.front.news.bean.News"%>
<%@page import="net.jeeshop.core.FrontContainer"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<%-- <script src="<%=request.getContextPath() %>/resource/js/superSlide/jquery.pack.js"></script> --%>
<%-- <script src="<%=request.getContextPath() %>/resource/js/superSlide/jquery.SuperSlide.js"></script> --%>
</head>

<body>
<!-- 首页通知切换卡 -->
<style type="text/css">
		/* css 重置 */
/* 		*{margin:0; padding:0; list-style:none; } */
/* 		body{ background:#fff; font:normal 12px/22px 宋体;  } */
/* 		img{ border:0;  } */
/* 		a{ text-decoration:none; color:#333;  } */
/* 		a:hover{ color:#1974A1;  } */


		/* 本例子css */
		.slideTxtBox{ width:100%; border:1px solid #ddd; text-align:left;  }
		.slideTxtBox *{margin:0; padding:0; list-style:none;}
		.slideTxtBox .hd{ height:30px; line-height:30px; background:#f4f4f4; padding:0 20px; border-bottom:1px solid #ddd;  position:relative;  }
		.slideTxtBox .hd ul{ float:left; position:absolute; left:20px; top:-1px; height:32px;   }
		.slideTxtBox .hd ul li{ float:left; padding:0 15px; cursor:pointer;  }
		.slideTxtBox .hd ul li.on{ height:30px;  background:#fff; border:1px solid #ddd; border-bottom:2px solid #fff; }
		.slideTxtBox .bd ul{ padding:15px;  zoom:1;  }
/* 		.slideTxtBox .bd li{ height:24px; line-height:24px;    */
/* 			display: block; */
/* 			width: 200px; */
/* 			overflow: hidden; /*注意不要写在最后了*/ */
/* 			white-space: nowrap; */
/* 			-o-text-overflow: ellipsis; */
/* 			text-overflow: ellipsis; */
/* 		} */
		.slideTxtBox .bd li .date{ float:right; color:#999;  }
		#productdetailDiv img{max-width: 670px;}
		</style>


		<div class="slideTxtBox">
			<div class="hd">
				<ul><li>商品介绍</li><li>商品评论</li></ul>
			</div>
			<div class="bd">
				<ul>
					<!-- 商品参数 -->
					<div class="row">
						<div class="col-md-12">
							<s:iterator value="e.parameterList" status="i" var="product">
								<div class="col-md-4" style="margin-bottom: 5px;padding-right: 2px;">
									<b><s:property escape="false" value="name"/> : </b><s:property escape="false" value="value"/><br>
								</div>
							</s:iterator>
						</div>
					</div>
					<br>
					
					<!-- 商品HTML信息 -->
					<div class="row">
						<div class="col-xs-12">
							<div style="border: 0px solid; text-align: left;" id="productdetailDiv">
<%-- 								<s:property value="e.productHTML" escapeHtml="false"/> --%>
								
								<%String productHTMLUrl = request.getAttribute("productHTMLUrl").toString();%>
								<jsp:include flush="true" page="<%=productHTMLUrl %>"></jsp:include>
							</div>
						</div>
					</div>
				</ul>
				<ul>
				<!-- 评论 -->
					<%
					String _commentTypeCode = SystemManager.commentTypeCode;
					request.setAttribute("_commentTypeCode",_commentTypeCode);
					%>
					<s:if test="#request._commentTypeCode.equals(\"default\")">
						<!-- 系统评论 -->
<%-- 						<s:if test="!(#request.commentPager.list==null or #request.commentPager.list.size==0)"> --%>
<!-- 							<div class="row"> -->
<!-- 								<div class="col-xs-12"> -->
<!-- 									<a href="#">好评</a> -->
<!-- 									<a href="#">中评</a> -->
<!-- 									<a href="#">差评</a> -->
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 							<hr> -->
<%-- 						</s:if> --%>
						
						<div class="row">
							<div class="col-xs-12">
								<s:iterator value="#request.commentPager.list" status="i" var="comment">
									<ul class="media-list">
									  <li class="media">
									    <span class="pull-left" href="#">
									      <img class="media-object err-product" style="width: 50px;height: 50px;border: 0px;" alt="" src="http://myshopxx.oss.aliyuncs.com/attached/image/20140304/1393900153455_3.jpg">
									      (金牌会员)
									    </span>
									    <div class="media-body">
									      <h4 class="media-heading"><s:property escape="false" value="nickname"/></h4>
									      <s:property escape="false" value="content"/>
									      
									      <s:if test="reply!=null and reply!=''">
										      <div class="media">
										      	<span class="pull-left" href="#">
											      <img class="media-object err-product" style="width: 50px;height: 50px;border: 0px;" alt="" src="http://myshopxx.oss.aliyuncs.com/attached/image/20140304/1393900153455_3.jpg">
											      (店小二)
											    </span>
											    <div class="media-body">
											    	<h4 class="media-heading" style="color:color:#f50">[店小二]回复：</h4>
										      		<s:property escape="false" value="reply"/>
											    </div>
										      </div>
									      </s:if>
									    </div>
									  </li>
									</ul>

								</s:iterator>
							</div>
							<div class="row" style="text-align: right;">
								<div class="col-xs-12">
									<s:if test="!(#request.commentPager.list==null or #request.commentPager.list.size==0)">
										<%@ include file="pager.jsp"%>
									</s:if>
									<s:else>
										该商品暂无评论！
									</s:else>
								</div>
							</div>
						</div>
					</s:if>
					<s:elseif test="#request._commentTypeCode.equals(\"duoshuo\")">
						<div class="row">
							<div class="col-xs-12">
								<%@ include file="duoshuo.jsp"%>
							</div>
						</div>
					</s:elseif>
				</ul>
			</div>
		</div>
</body>
</html>
