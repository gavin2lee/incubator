create table task_nurse_attention(id int primary key,nurse_id varchar2(30) not null,bed_code varchar2(30) not null)

CREATE TABLE sys_user_finger(
  fp_id int primary key,
  user_id varchar2(30) NOT NULL,
  fp_feature varchar2(2048) NOT NULL,
  sec_key varchar2(50) NOT NULL,
  create_datetime DATE not null);
	
	create sequence lx_sequence1
increment by 1
start with 1
nomaxvalue
nocycle
cache 10;

CREATE TABLE dict_measure_rule(
  rule_id int primary key,
  rule_code varchar2(10) NOT NULL,
  rule_name varchar2(20) NOT NULL,
  rule_type varchar2(10) NOT NULL,
  rule_value varchar2(10) NOT NULL,
  rule_time varchar2(100) NOT NULL);
  
  
  CREATE TABLE order_exec(
  order_exec_id varchar2(30) primary key,
  order_group_id varchar2(30) NOT NULL,
  plan_datetime date NOT NULL,
  exec_datetime date not NULL,
  exec_nurse_code varchar2(20) NULL,
  exec_nurse_name varchar(30) NULL,
  finish_datetime date NULL,
  finish_nurse_code varchar2(20) NULL,
  finish_nurse_name varchar2(30) NULL,
  approve_nurse_code varchar2(20) NULL,
  approve_nurse_name varchar2(30) NULL
  );