--定时同步his用户信息表
CREATE TABLE lx_DATA_SYNC(
	id int NOT NULL,
	tableName varchar(20) NULL,
	maxRecordId varchar(10) NULL,
	scope varchar(20) NULL,
	updateTime datetime NULL,
	enabled int default 0,  --未启用
	PRIMARY KEY(id)
);
INSERT INTO lx_DATA_SYNC VALUES(1, 'MD_USER', -1, NULL, '2014-11-15 00:00:00', 0);
INSERT INTO lx_DATA_SYNC VALUES(2, 'PAT_ADVICE', -1, NULL, '2014-11-15 00:00:00', 0);
INSERT INTO lx_DATA_SYNC VALUES(3, 'PAT_CUREINFO', -1, NULL, '2014-11-15 00:00:00', 0);
INSERT INTO lx_DATA_SYNC VALUES(4, 'PAT_REVERSEINFO', -1, NULL, '2014-11-15 00:00:00', 0);

--用户信息表
CREATE TABLE lx_sys_user (
	user_id int IDENTITY,
	empl_code varchar(15),
	login_name varchar(10) NOT NULL,
	password varchar(32) NOT NULL,
	name varchar(20) NOT NULL,
	gender varchar(1),
	birthday datetime,
	phone varchar(20),
	email varchar(30),
	create_user_id int,
	create_datetime datetime,
	modify_user_id int,
	modify_datetime datetime,
	remark varchar(50),
	valid int NOT NULL,
	primary key(user_id),
	unique(login_name)
);

---护士关注信息表
CREATE TABLE lx_task_nurse_attention (
	id bigint IDENTITY,
	nurse_id varchar(10) NOT NULL,
	bed_code varchar(3) NOT NULL,
	primary key(id)
);
--用户指纹信息表
CREATE TABLE lx_sys_user_finger(
  fp_id bigint IDENTITY ,
  user_id varchar(30) NOT NULL,
  fp_feature varchar(2048) NOT NULL,
  sec_key varchar(50) NOT NULL,
  create_datetime datetime not null,
  primary key(fp_id)
);
--医嘱执行记录表
CREATE TABLE lx_order_exec (
	order_exec_id varchar(32) NOT NULL,
	order_group_id int NOT NULL,
	order_exec_type varchar(10)  NULL,
	inpatient_no varchar(10) NOT NULL,
	plan_datetime datetime NOT NULL,
	exec_datetime datetime,
	exec_nurse_code varchar(6),
	exec_nurse_name varchar(10),
	finish_datetime datetime,
	finish_nurse_code varchar(6),
	finish_nurse_name varchar(10),
	order_exec_barcode varchar(32),
	deliver_speed int,
	deliver_speed_unit varchar(10),
	approve_nurse_code varchar(6),
	approve_nurse_name varchar(10),
	primary key(order_exec_id)
);
--生命体征
CREATE TABLE lx_dict_measure_rule (
	rule_id int IDENTITY,
	rule_code varchar(10) NOT NULL,
	rule_name varchar(20) NOT NULL,
	rule_type varchar(10) NOT NULL,
	rule_value varchar(10) NOT NULL,
	rule_time varchar(100) NOT NULL,
	primary key(rule_id)
);
CREATE TABLE lx_dict_body_sign (
	item_code varchar(20) NOT NULL,
	item_name varchar(20) NOT NULL,
	item_unit varchar(10) NOT NULL,
	primary key(item_code)
);
CREATE TABLE lx_rec_body_sign_detail (
	body_sign_detail_id int IDENTITY,
	body_sign_mas_id int NOT NULL,
	item_code varchar(20),
	item_value varchar(100),
	item_name varchar(100),
	measure_note_code varchar(10),
	measure_note_name varchar(20),
	abnormal_flag tinyint NOT NULL DEFAULT (0),
	primary key(body_sign_detail_id)
) ;
CREATE TABLE lx_rec_body_sign_mas (
	body_sign_mas_id int IDENTITY,
	pat_id varchar(20) NOT NULL,
	pat_name varchar(10),
	bed_code varchar(10),
	inhosp_no varchar(20),
	dept_code varchar(4),
	cooled tinyint NOT NULL DEFAULT (0),
	rec_datetime datetime NOT NULL DEFAULT (getdate()),
	rec_nurse_code varchar(10) NOT NULL,
	rec_nurse_name varchar(20)  NULL,
	modify_datetime datetime NOT NULL DEFAULT (getdate()),
	modify_nurse_code varchar(10) NULL,
	modify_nurse_name varchar(20)  NULL,
	copy_flag char(1),
	remark varchar(50),
	primary key(body_sign_mas_id)
) ;

CREATE TABLE lx_rec_pat_event (
	rec_pat_event_id int IDENTITY,
	pat_id varchar(20) NOT NULL,
	pat_name varchar(10) NOT NULL,
	inhosp_no varchar(20),
	dept_code varchar(4),
	body_sign_mas_id int NOT NULL,
	event_datetime datetime NOT NULL ,
	rec_nurse_code varchar(6) NOT NULL,
	rec_nurse_name varchar(10),
	confirm_nurse_code varchar(6),
	confirm_nurse_name varchar(10),
	event_code varchar(10),
	event_name varchar(20),
	primary key(rec_pat_event_id)
);
CREATE TABLE lx_rec_skin_test (
	rec_skin_test_id int IDENTITY,
	pat_id varchar(20) NOT NULL,
	pat_name varchar(20) NOT NULL,
	inhosp_no varchar(20),
	dept_code varchar(4),
	test_nurse_code varchar(6) NOT NULL,
	test_result varchar(1) DEFAULT ('N'),
	test_datetime datetime DEFAULT (getdate()),
	drug_code varchar(12)  NULL,
	drug_name varchar(80) NOT NULL,
	order_group_id varchar(30),
	order_exec_id varchar(30),
	body_sign_mas_id int,
	drug_batch_no varchar(30),
	test_nurse_name varchar(10),
	approve_nurse_code varchar(6),
	approve_nurse_name varchar(10),
	primary key(rec_skin_test_id)
);
CREATE TABLE lx_pat_operation_status (
	id int IDENTITY(1,1) NOT NULL,
	operation_id varchar(20) NOT NULL,
	joint_operation_time datetime NULL,
	joint_operation_user varchar(50) NULL,
	send_operation_time datetime NULL,
	send_operation_user varchar(50) NULL,
	operation_status varchar(2) NOT NULL,
	patient_id varchar(20) NOT NULL,
	primary key(id)
);


 CREATE TABLE lx_ward_patrol (
	ward_patrol_id int IDENTITY,
	pat_id int NOT NULL,
	nurse_code varchar(6) NOT NULL,
	dept_code varchar(4) NOT NULL,
	tend_level char(1) NOT NULL  DEFAULT ((3)),
	patrol_date datetime,
	next_patrol_date datetime,
	primary key(ward_patrol_id)
) ;

--系统配置表
CREATE TABLE lx_system_config(
	id	int IDENTITY,
    config_id varchar(30) not null,   --配置ID
    config_value varchar(500) NOT NULL,   --配置内容
    config_type varchar(10) null, --配置所属类型  S为系统配置，U为属于用户，D为属于科室
    config_owner varchar(30) null,   --所属人, config_type为U时是用户ID，config_type为D时是科室ID，其他为Null
    valid_flag int,--是否有效 为1时有效，其他表示无效
    config_desc varchar(2000) null
  );
  
 --护理白板字典
  create table lx_white_board_item(
  	item_code varchar(30) primary key,
  	item_name varchar(50) not null,
  	item_desc varchar(500) null
  );
 --护理白板
create table lx_white_board(
  	dept_id varchar(30) not null,
  	item_code varchar(30) not null,
  	item_value varchar(800) not null,
  	operate_time date null,
  	show_date varchar(10) not null,
  	primary key (dept_id,item_code,show_date)
  );
  
insert into lx_system_config
  (config_id,
   config_value,
   config_type,
   config_owner,
   valid_flag,
   config_desc)
values
  ('tempInputItems',
   'temperature,pulse,heartRate,breath,bloodPress,otherOutput,totalInput,urine,stool,height,weight,event,remark,skintest,other',
   '',
   '',
   1,
   '【体征录入】的项目及项目顺序');

   insert into lx_system_config
  (config_id,
   config_value,
   config_type,
   config_owner,
   valid_flag,
   config_desc)
values
  ('tempInputTimeShow',
   'false',
   'S',
   '',
   1,
   '【体征录入】是否需要时间弹出框');

   insert into lx_system_config
  (config_id,
   config_value,
   config_type,
   config_owner,
   valid_flag,
   config_desc)
values
  ('tempInputTimeSelector',
   '01:00,03:00,05:00,07:00,09:00,11:00,13:00,15:00,17:00,19:00,21:00,23:00',
   'S',
   '',
   1,
   '【体征录入】时间选项');

   insert into lx_system_config
  (config_id,
   config_value,
   config_type,
   config_owner,
   valid_flag,
   config_desc)
values
  ('tempsheetItems',
   'totalInput,stool,urine,otherOutput,weight,skintest,other',
   'S',
   '',
   1,
   '三测得上各项目的显示顺序');

   insert into lx_system_config
  (config_id,
   config_value,
   config_type,
   config_owner,
   valid_flag,
   config_desc)
values
  ('hospitalName', '深圳市第三医院', 'S', '', 1, '医院名称');
insert into lx_system_config
  (config_id,
   config_value,
   config_type,
   config_owner,
   valid_flag,
   config_desc)
values
  ('canEditSignTime',
   'false',
   'S',
   '',
   1,
   '【体征修改】true：可以从时间弹出框设置；false：只能选');

insert into lx_white_board_item values ('total_patient','病人总数','病人总数');
insert into lx_white_board_item values ('crisis_patient','病危','病危');
insert into lx_white_board_item values ('hard_patient','病重','病重');
insert into lx_white_board_item values ('new_patient','新入病人','新入病人');
insert into lx_white_board_item values ('out_patient','出院病人','出院病人');
insert into lx_white_board_item values ('second_doctor','付班医生','付班医生');
insert into lx_white_board_item values ('day_nurse_a1','A1','日班护士A1');
insert into lx_white_board_item values ('day_nurse_a2','A2','日班护士A2');
insert into lx_white_board_item values ('day_nurse_a3','A3','日班护士A3');
insert into lx_white_board_item values ('day_nurse_a4','A4','日班护士A4');
insert into lx_white_board_item values ('day_nurse_b','日班护士B','日班护士B');
insert into lx_white_board_item values ('work_doctor','值班医生','值班医生');
insert into lx_white_board_item values ('work_nurse_p','P','值班护士P');
insert into lx_white_board_item values ('work_nurse_n','N','值班护士N');
insert into lx_white_board_item values ('work_nurse_f','辅','值班护士辅');
insert into lx_white_board_item values ('back_doctor','二线医生','二线医生');
insert into lx_white_board_item values ('back_nurse','二线护士长','二线护士长');

insert into lx_white_board_item values ('24hinout','24h出入量','24h出入量');
insert into lx_white_board_item values ('24hurine','24h尿量','24h尿量');
insert into lx_white_board_item values ('abdomaen','腹围','腹围');
insert into lx_white_board_item values ('wight','体重','体重');
insert into lx_white_board_item values ('bodysigh','生命体征','生命体征');
insert into lx_white_board_item values ('cinnamon','血糖监测','血糖监测');
insert into lx_white_board_item values ('important','重要提示','重要提示');
insert into lx_white_board_item values ('message','重要通知','重要通知');
insert into lx_white_board_item values ('other','其他','其他');

insert into  lx_dict_body_sign values('bloodPress','血压','mmHg');
insert into  lx_dict_body_sign values('breath','呼吸','次/分');
insert into  lx_dict_body_sign values('heartRate','心率','次/分');
insert into  lx_dict_body_sign values('height','身高','cm');
insert into  lx_dict_body_sign values('inTake','入量','ml');
insert into  lx_dict_body_sign values('otherOutput','其他出量','ml');
insert into  lx_dict_body_sign values('pulse','脉搏','次/分');
insert into  lx_dict_body_sign values('stool','大便','次');
insert into  lx_dict_body_sign values('temperature','体温','°C');
insert into  lx_dict_body_sign values('cooledTemperature','降温后体温','°C');
insert into  lx_dict_body_sign values('totalOutput','总出量','ml');
insert into  lx_dict_body_sign values('urine','尿量','ml');
insert into  lx_dict_body_sign values('weight','体重','kg');
insert into  lx_dict_body_sign values('other','其它','');
insert into  lx_dict_body_sign values('pain','疼痛','级');
insert into  lx_dict_body_sign values('oxygenSaturation','血氧饱和度','%');
insert into  lx_dict_body_sign values('bloodGlu','指测血糖','mmol/L');
insert into  lx_dict_body_sign values('abdominalCir','腹围','cm');
insert into lx_dict_body_sign values('totalInput','总入量','ml');
