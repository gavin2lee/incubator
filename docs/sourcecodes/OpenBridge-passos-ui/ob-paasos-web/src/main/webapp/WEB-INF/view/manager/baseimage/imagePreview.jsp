<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>

<div class="r_con p10_0">
	<div class="details_nr">
		<div class="mirro_list">
             <c:if test="${! empty page }">
        		<c:forEach items="${page }" var="image">
        			<div class="cluster_block_three active pos-rel">
                        <div class="cluster-header">
                            <h3 class="cluster_name f12">
                            	<a href="view.do?id=${image.id }">${image.name }</a>
                            </h3>
                    <div class="cluster-right pull-right" style="margin-top:2px;">
                    <button class="btn btn-more"><i class="icons ico_more2"></i></button>
                    <div class="list_sub_menu">
                    <div class="blank_grid">&nbsp;</div>
                    <ul class="list_menu">
                    <div class="arrow-up arrow_pos"></div>
                    <li>
                       <c:if test="${image.status==1 || image.status==0 || image.status==10 }">
                         <a class="build-button f12" href="javascript:buildImage('${image.id }');"><span class="fa fa-magic mr10" aria-hidden="true"></span>构建</a>
                       </c:if>
                    </li>
                    <%-- 							<c:if test="${image.status==1 || image.status==0 }"> --%>
                    <li><a class="modify-button f12" href="javascript:updateImage('${image.id }');"><span class="fa fa-pencil mr10" aria-hidden="true"></span>修改</a></li>
                    <%-- 							</c:if> --%>
                    <li><c:if test="${image.status!=5 && image.status!=6 }">
                    <a class="delete-button f12" href="javascript:deleteImage('${image.id }');"><span class="fa fa-trash mr10" aria-hidden="true"></span>删除</a>
                </c:if></li>
                    <li><c:if test="${image.status!=1 && image.status!=6}">
                    <a class="log-button f12" href="javascript:viewLog('${image.id }');"><span class="fa fa-book mr10" aria-hidden="true"></span>日志</a>
                </c:if></li>
                    </ul>
                    </div>
                        </div>
                    </div>
                        <div class="img-container" style="float: left;padding: 10px;">
                        	<img src="${WEB_APP_PATH }/paas/file/view.do?filePath=${image.iconPath}">
                       	</div>
                        <div class="cluster-content cluster-content2" style="float:left;width:250px;">
                            <ul>
                                <li>
                                    <label>镜像名称</label>
                                    <p>${image.name }</p>
                                </li>
                                <li>
                                    <label>项目版本</label>
                                    <p>${image.version }</p>
                                </li>
                                <li>
                                    <label>构建文件</label>
                                    <c:choose>
											<c:when test="${not empty image.fileName}"> <p>${image.fileName }</p></c:when>
											<c:otherwise><p>无</p></c:otherwise>
									</c:choose>
                                   
                                </li>
                                <li>
                                    <label>状态</label>
                                    <p>
                                    	<span class="zt_span">
                                    	<c:choose>
											<c:when test="${image.status == 0}"><i class="pas_ico zt_ico fail_ico mt5"></i>构建失败</c:when>
											<c:when test="${image.status == 1}"><i class="pas_ico zt_ico doing_ico mt5"></i>未构建</c:when>
											<c:when test="${image.status == 5}"><i class="pas_ico zt_ico doing_ico mt5"></i>构建中</c:when>
											<c:when test="${image.status == 6}"><i class="pas_ico zt_ico doing_ico mt5"></i>删除中</c:when>
											<c:when test="${image.status == 10}"><i class="pas_ico zt_ico done_ico mt5"></i>构建成功</c:when>
											<c:when test="${image.status == 11}"><i class="pas_ico zt_ico fail_ico mt5"></i>删除失败</c:when>
										</c:choose>
                                    	</span>
                                    </p>
                                </li>
                                <%-- <li>
                                    <label>镜像信息</label>
                                    <p>project/${row.build_name }:${row.build_tag }</p>
                                </li> --%>
                            </ul>
                        </div>
                    </div>
        		</c:forEach>     
             </c:if>                   
        </div>
	</div>
	<c:if test="${not empty page}">
		<ui:pagination data="${page }"
			href="javascript:loadItems(!{pageNo},!{pageSize});"
			id="baseImagePage"></ui:pagination>
	</c:if>
</div>
    <script type="text/javascript">
    //    隐藏按钮JS
    $(document).ready(function () {
       $(".cluster-right").hover(function () {
         $(this).addClass("open");
           }, function () {
         $(this).removeClass("open");
       });
    });
    </script>