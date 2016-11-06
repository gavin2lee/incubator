alter table os_project_deploy change ext_business_env cpu_ double comment 'cpu 默认单位为个';
alter table os_project_deploy change ext_business_id memory_ double comment '内存 默认单位为m';

alter table os_resource_mysql add cpu_ double;
alter table os_resource_mysql add memory_ double;
alter table os_resource_mysql add storage_ double;
 
alter table os_resource_rabbitmq add cpu_ double;
alter table os_resource_rabbitmq add memory_ double;
alter table os_resource_rabbitmq add storage_ double;	

alter table os_resource_redis add cpu_ double;
alter table os_resource_redis add memory_ double;
alter table os_resource_redis add storage_ double;	
alter table os_resource_redis modify allocate_content varchar(2000);

alter table os_resource_nas add cpu_ double;
alter table os_resource_nas add memory_ double;
alter table os_resource_nas add storage_ double;	
alter table os_resource_nas modify allocate_content varchar(2000);
