delete from sys_role_func where role_id in (select role_id from sys_role where role_system = 'paasos');
delete from sys_role where role_system = 'paasos';
delete from sys_func where func_system = 'paasos';


INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`, `role_system`) VALUES ('paasos.manager', '环境管理员', '可以对基础镜像、节点及组织进行管理', 'paasos');
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`, `role_system`) VALUES ('paasos.store', '预置应用管理员', '对预置应用管理,预置应用的上传更新构建部署', 'paasos');
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`, `role_system`) VALUES ('passos.operations', '运维人员', '对系统的运行及资源节点进行监控', 'paasos');


INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.tenant.manager', '组织管理', '组织管理', 'paasos', '环境管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.node.manager', '节点管理', '节点管理', 'paasos', '环境管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.baseimage.manager', '基础镜像', '基础镜像', 'paasos', '环境管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.deploy.manager', '部署列表', '部署列表', 'paasos', '环境管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.store.manager', '预置应用上传更新', '预置应用上传更新', 'paasos', '预置应用管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.store.deploy', '预置应用构建部署', '预置应用构建部署', 'paasos', '预置应用管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.project.envtest', '测试环境部署', '测试环境部署', 'paasos', '项目管理'); 
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.project.envlive', '生产环境部署', '生产环境部署', 'paasos', '项目管理'); 
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.project.build', '项目构建', '项目构建', 'paasos', '项目管理'); 
 
 