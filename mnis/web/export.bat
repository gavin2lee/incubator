@shift
@echo off
title 联影医疗移动护理信息系统安装中...
mode con cols=100 lines=9999
color 3f
cls
set sname_sql=localhost
:connsql
echo.
echo *******************************
echo 您即将连接到SQL Server...
echo 服务器地址是：%sname_sql%
echo 登录名是：sa
echo *******************************
echo.
goto changesql 

:setsql
echo.
set /P sname_sql=请输入您想连接的SQL Server服务器地址：
goto checksql

:changesql
echo 是否要连接到%sname_sql%这台SQL Server数据库？
set /P csql=“y”连接到这台SQL Server数据库，“n”更换服务器地址，输入其它字符将退出程序[y/n] 
if "%csql%"=="y" (
goto checksql
) else (
if "%csql%"=="n" (
goto setsql ) else (
goto cend
)
)

:checksql
echo.
set /P pwdsa=请输入%sname_sql%数据库管理员sa用户的密码：
osql -S%sname_sql% -Usa -P%pwdsa% -Q
if %ERRORLEVEL% == 0 (
echo 【已经正确连接到%sname_sql%这台SQL Server数据库...】
goto runsql
) else (
goto errorend
)

:runsql

echo 【下面将导出系统的初始化数据...】

bcp MNIS_V2.dbo.sys_user_session out .\db\data_bak\dbo.sys_user_session  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user_role out .\db\data_bak\dbo.sys_user_role  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user_group out .\db\data_bak\dbo.sys_user_group  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user_finger out .\db\data_bak\dbo.sys_user_finger  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user out .\db\data_bak\dbo.sys_user  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_role_permission out .\db\data_bak\dbo.sys_role_permission  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_role_group out .\db\data_bak\dbo.sys_role_group  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_role out .\db\data_bak\dbo.sys_role  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_permission out .\db\data_bak\dbo.sys_permission  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_operate out .\db\data_bak\dbo.sys_operate  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_module out .\db\data_bak\dbo.sys_module  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_group out .\db\data_bak\dbo.sys_group  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_employee_serve out .\db\data_bak\dbo.sys_employee_serve  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_employee out .\db\data_bak\dbo.sys_employee  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_dept out .\db\data_bak\dbo.sys_dept  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_skin_test out .\db\data_bak\dbo.rec_skin_test  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_pat_event out .\db\data_bak\dbo.rec_pat_event  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_shift out .\db\data_bak\dbo.rec_nurse_shift  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_record_mas out .\db\data_bak\dbo.rec_nurse_record_mas  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_record_detail out .\db\data_bak\dbo.rec_nurse_record_detail  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_item_mas out .\db\data_bak\dbo.rec_nurse_item_mas  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_item_detail out .\db\data_bak\dbo.rec_nurse_item_detail  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_lab_test_mas out .\db\data_bak\dbo.rec_lab_test_mas  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_lab_test_detail out .\db\data_bak\dbo.rec_lab_test_detail  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_inspection_mas out .\db\data_bak\dbo.rec_inspection_mas  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_inspection_detail out .\db\data_bak\dbo.rec_inspection_detail  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_infu_monitor out .\db\data_bak\dbo.rec_infu_monitor  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_diag out .\db\data_bak\dbo.rec_diag  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_body_sign_mas out .\db\data_bak\dbo.rec_body_sign_mas  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_body_sign_detail out .\db\data_bak\dbo.rec_body_sign_detail  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_allergy out .\db\data_bak\dbo.rec_allergy  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.poc_bindings out .\db\data_bak\dbo.poc_bindings  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.phone_bindings out .\db\data_bak\dbo.phone_bindings  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.pat_hosp out .\db\data_bak\dbo.pat_hosp  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.pat_base out .\db\data_bak\dbo.pat_base  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.order_item out .\db\data_bak\dbo.order_item  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.order_group out .\db\data_bak\dbo.order_group  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.order_exec out .\db\data_bak\dbo.order_exec  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.item_price out .\db\data_bak\dbo.item_price  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.criticalvalue_info out .\db\data_bak\dbo.criticalvalue_info  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.bed out .\db\data_bak\dbo.bed  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.ass_nurse_record_dept out .\db\data_bak\dbo.ass_nurse_record_dept  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.ass_nurse_item_dept out .\db\data_bak\dbo.ass_nurse_item_dept  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.ward_patrol out .\db\data_bak\dbo.ward_patrol  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.vital_sign_info out .\db\data_bak\dbo.vital_sign_info  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.task_nurse_attention out .\db\data_bak\dbo.task_nurse_attention  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.hosp_duty out .\db\data_bak\dbo.hosp_duty  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"

bcp MNIS_V2.dbo.DATA_SOURCE out .\db\data_bak\dbo.DATA_SOURCE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DEPARTMENT_TEMPALTE out .\db\data_bak\dbo.DEPARTMENT_TEMPALTE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_ITEM_OPTION out .\db\data_bak\dbo.DOC_ITEM_OPTION  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_ITEM out .\db\data_bak\dbo.DOC_TABLE_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_ITEM_OPTION out .\db\data_bak\dbo.DOC_TABLE_ITEM_OPTION  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_PATTERN out .\db\data_bak\dbo.DOC_TABLE_PATTERN  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_PATTERN_ITEM out .\db\data_bak\dbo.DOC_TABLE_PATTERN_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.METADATA out .\db\data_bak\dbo.METADATA  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.METADATA_OPTION out .\db\data_bak\dbo.METADATA_OPTION  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOCUMENT out .\db\data_bak\dbo.NURSE_DOCUMENT  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_DATA out .\db\data_bak\dbo.NURSE_DOC_DATA  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_DATA_HIS out .\db\data_bak\dbo.NURSE_DOC_DATA_HIS  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_ITEM out .\db\data_bak\dbo.NURSE_DOC_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_OPERATE out .\db\data_bak\dbo.NURSE_DOC_OPERATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.OPERATE out .\db\data_bak\dbo.OPERATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.PAGER out .\db\data_bak\dbo.PAGER  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE out .\db\data_bak\dbo.TABLE_TEMPLATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE_ITEM out .\db\data_bak\dbo.TABLE_TEMPLATE_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE_PATTERN out .\db\data_bak\dbo.TABLE_TEMPLATE_PATTERN  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE_PATTERN_ITEM out .\db\data_bak\dbo.TABLE_TEMPLATE_PATTERN_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TEMPLATE out .\db\data_bak\dbo.TEMPLATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TEMPLATE_ITEM out .\db\data_bak\dbo.TEMPLATE_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TEMPLATE_OPERATE out .\db\data_bak\dbo.TEMPLATE_OPERATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.VERIFY_POLICY out .\db\data_bak\dbo.VERIFY_POLICY  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"

set pwdsa=
echo.
goto succend

:succend
echo 已完成导出！
echo 感谢您选用联影医疗软件！
echo.
goto end
:errorend
echo 安装程序没有正确运行，查看错误提示，并尝试按照提示解决这次的安装问题，如果问题依然存在，请发送错误信息到zehua.xing@united-imaging.com，联影医疗将提供专业的技术支持。
echo 安装程序即将退出...
echo.
goto end
:cend
echo 无效的输入！
echo 您已退出安装程序...
echo.
goto end

:end
pause
