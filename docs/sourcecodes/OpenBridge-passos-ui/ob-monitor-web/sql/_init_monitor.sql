create view sys_user as select * from paasos.sys_user;
create view sys_department as select * from paasos.sys_department;
create view sys_func as select * from paasos.sys_func;
create view sys_group as select * from paasos.sys_group;
create view sys_login_logs as select * from paasos.sys_login_logs;
create view sys_role as select * from paasos.sys_role;
create view sys_role_func as select * from paasos.sys_role_func;
create view sys_tenant as select * from paasos.sys_tenant;
create view sys_tenant_relation as select * from paasos.sys_tenant_relation;
create view sys_user_dept as select * from paasos.sys_user_dept;
create view sys_user_group as select * from paasos.sys_user_group;
create view sys_user_role as select * from paasos.sys_user_role;
create view os_nginx_host as select * from paasos.os_nginx_host;



alter table team add creator_user varchar(32);
alter table rel_team_user add user_id varchar(32);
alter table grp add grp_type varchar(4) NOT NULL DEFAULT '0';
alter table team add type varchar(4) NOT NULL DEFAULT '0';
alter table tpl add type varchar(4) NOT NULL DEFAULT '0';