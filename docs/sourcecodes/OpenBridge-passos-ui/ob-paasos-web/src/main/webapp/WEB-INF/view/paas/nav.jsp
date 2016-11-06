<%@ page language="java" pageEncoding="UTF-8"%>
<h3 class="f14 h3_color h3_btm_line blue">
    <a href="#">
        <i class="icons ico_title_list  mr5"></i>资源配置
    </a>
</h3>
<div class="title_tab">
    <ul>
        <li>
            <h5 class="f14 ${ param['tab'] eq 'instances' ? 'blue' : 'gray' }">
                <a class="active_green" href="${WEB_APP_PATH}/paas/nginx/page.do">
				      <i class="icons cy_ico mr5"></i>Nginx 实例
				</a>
            </h5>
        </li>
    </ul>
</div>
<div class="title_tab">
    <ul>
        <li>
            <h5 class="f14 ${ param['tab'] eq 'hosts' ? 'blue' : 'gray' }">
                <a class="active_green" href="${WEB_APP_PATH}/paas/nginx/host.do">
			        <i class="icons cy_ico mr5"></i>Nginx 服务器
			    </a>
            </h5>
        </li>
    </ul>
</div>
<div class="title_line"></div>