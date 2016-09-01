------ H2,SqlServer(2005)通用 ------
CREATE TABLE dict_exce_resources (
	dict_exce_resources_id int IDENTITY(0, 1) NOT NULL,
	resources_key varchar(40) NULL,
	resources_value varchar(100) NULL,
	parent_id int NULL,
	module_coder varchar(10) NULL,
	is_multis_select int NULL
);

CREATE TABLE dict_sche_duty_item (
	duty_item_id int IDENTITY(1, 1) NOT NULL,
	duty_item_name varchar(8) NULL,
	duty_item_code varchar(2) NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	remark varchar(80) NULL
);

CREATE TABLE exam_ultrasound_info (
	exam_ultrasound_info_id int IDENTITY(0, 1) NOT NULL,
	his_patient_id varchar(10) NOT NULL,
	type varchar(6) NOT NULL,
	type_code varchar(2) NOT NULL,
	location varchar(10) NOT NULL,
	location_code varchar(2) NOT NULL,
	exam_datetime datetime NOT NULL,
	exam_doctor_id varchar(10) NOT NULL,
	exam_fee decimal(9, 2) NOT NULL,
	hint varchar(30) NULL,
	observe varchar(200) NULL,
	remark varchar(80) NULL
);

CREATE TABLE exam_ultrasound_img (
	exam_ultrasound_img_id int IDENTITY(0, 1) NOT NULL,
	exam_ultrasound_info_id int NOT NULL,
	url varchar(100) NULL
);

CREATE TABLE dict_tend_records (
	tend_records_id int NOT NULL,
	tend_records_name varchar(20) NULL,
	parent_id int NULL,
	is_leave varchar(1) NOT NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	remark varchar(80) NULL
);

CREATE TABLE dict_vital_sign (
	sign_id int IDENTITY(1, 1) NOT NULL,
	item_name varchar(10) NULL,
	item_unit varchar(8) NULL,
	item_code varchar(20) NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	remark varchar(80) NULL
);

CREATE TABLE dict_tend_eval (
	tend_eval_id int IDENTITY(1, 1) NOT NULL,
	tend_eval_name varchar(20) NULL,
	tend_eval_code varchar(10) NULL,
	level tinyint NOT NULL,
	parent_id int NULL,
	tend_eval_type tinyint NOT NULL,
	dept_id smallint NULL,
	active_flag tinyint NOT NULL,
	create_user_id int NOT NULL,
	modify_user_id int NOT NULL,
	create_datetime datetime NOT NULL,
	modify_datetime datetime NOT NULL,
	remark varchar(80) NULL
);

CREATE TABLE i_infusion_check (
	infusion_check_id int IDENTITY(1, 1) NOT NULL,
	check_nurse_id int NULL,
	drop_num int NULL,
	is_exeception tinyint NULL,
	patient_name varchar(20) NULL,
	check_nurse_name varchar(8) NULL,
	his_patient_id varchar(10) NULL,
	check_datetime datetime NULL,
	exception_msg varchar(100) NULL,
	exec_datetime datetime NULL,
	handle_msg varchar(40) NULL
);

CREATE TABLE dict_tend_item (
	tend_item_id int IDENTITY(1, 1) NOT NULL,
	tend_item_class varchar(1) NULL,
	tend_item_type varchar(1) NULL,
	tend_item_name varchar(20) NULL,
	tend_item_code varchar(12) NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	remark varchar(80) NULL
);

CREATE TABLE i_infusion_check_detail (
	infusion_check_detail_id int IDENTITY(1, 1) NOT NULL,
	drug_name varchar(50) NULL,
	dose_per decimal(9, 3) NULL,
	dose_unit varchar(5) NULL,
	infusion_check_id int NULL,
	drug_spec varchar(40) NULL,
	his_order_id varchar(16) NULL,
	order_group_no int NULL
);

CREATE TABLE i_dict_bed_info (
	bed_id int NOT NULL,
	patient_area varchar(10) NULL,
	dept_id smallint NULL,
	use_datetime datetime NULL,
	bed_type varchar(10) NULL,
	bed_fees decimal(6, 2) NULL,
	bed_code varchar(10) NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	remark varchar(80) NULL
);

CREATE TABLE order_exec_list (
	order_list_id int IDENTITY(1, 1) NOT NULL,
	order_id int NULL,
	order_no int NULL,
	patient_id int NULL,
	status_flag tinyint NULL,
	drug_id int NULL,
	order_group_no int NULL,
	doctor_id int NULL,
	min_unit_per decimal(9, 3) NULL,
	exec_dose_per decimal(9, 3) NULL,
	dose_per decimal(9, 3) NULL,
	create_datetime datetime NULL,
	order_type varchar(2) NULL,
	min_unit varchar(5) NULL,
	dose_type_code varchar(6) NULL,
	order_type_code varchar(14) NULL,
	dose_type_name varchar(16) NULL,
	his_order_id varchar(16) NULL,
	drug_spec varchar(40) NULL,
	order_freq varchar(30) NULL,
	freq_code varchar(10) NULL,
	freq_name varchar(30) NULL,
	drug_name varchar(46) NULL,
	order_name varchar(100) NULL,
	print_flag tinyint NULL,
	exec_order_dept_id smallint NULL,
	order_dept_id smallint NULL,
	dose_type_id int NULL,
	patient_bed_id int NULL,
	exec_user_id int NULL,
	parent_id int NULL,
	min_unit_dose decimal(9, 3) NULL,
	exec_datetime datetime NULL,
	print_datetime datetime NULL,
	plan_datetime datetime NULL,
	patient_name varchar(20) NULL,
	dose_name varchar(10) NULL,
	order_note varchar(50) NULL,
	remark varchar(80) NULL
);

CREATE TABLE order_exec_drug_detail (
	exec_detail_id int IDENTITY(1, 1) NOT NULL,
	exec_master_id int NULL,
	order_group_no int NULL,
	his_order_id varchar(16) NULL,
	drug_name varchar(46) NULL,
	dose_per decimal(9, 3) NULL,
	dose_unit varchar(5) NULL,
	drug_spec varchar(40) NULL
);

CREATE TABLE i_order_exec (
	order_id int IDENTITY(1, 1) NOT NULL,
	order_no int NULL,
	patient_id int NULL,
	status_flag tinyint NULL,
	order_type_id int NULL,
	drug_id int NULL,
	order_group_no int NULL,
	doctor_id int NULL,
	min_unit_per decimal(9, 3) NULL,
	exec_dose_per decimal(9, 3) NULL,
	dose_per decimal(9, 3) NULL,
	create_datetime datetime NULL,
	order_type varchar(2) NULL,
	min_unit varchar(5) NULL,
	dose_type_code varchar(6) NULL,
	order_type_code varchar(14) NULL,
	dose_type_name varchar(16) NULL,
	his_order_id varchar(16) NULL,
	drug_spec varchar(40) NULL,
	order_freq varchar(30) NULL,
	frequency_name varchar(30) NULL,
	drug_name varchar(46) NULL,
	order_name varchar(100) NULL,
	print_flag tinyint NULL,
	self_pay_flag tinyint NULL,
	exec_order_dept_id smallint NULL,
	order_dept_id smallint NULL,
	dose_type_id int NULL,
	patient_bed_id int NULL,
	exec_user_id int NULL,
	parent_id int NULL,
	min_unit_dose decimal(9, 3) NULL,
	exec_datetime datetime NULL,
	print_datetime datetime NULL,
	plan_datetime datetime NULL,
	patient_name varchar(20) NULL,
	exec_type varchar(10) NULL,
	order_item_id int NULL,
	order_item_name varchar(8) NULL,
	dose_name varchar(10) NULL,
	order_note varchar(50) NULL,
	his_bed_id varchar(10) NULL,
	stop_datetime datetime NULL,
	stop_note varchar(16) NULL,
	stop_user_id int NULL,
	remark varchar(80) NULL
);

CREATE TABLE i_patient_info (
	patient_id int NOT NULL,
	bed_id int NULL,
	his_patient_id varchar(10) NULL,
	patient_code varchar(14) NULL,
	patient_name varchar(20) NULL,
	gender varchar(1) NULL,
	age tinyint NULL,
	charge_type varchar(8) NULL,
	diagnosis varchar(12) NULL,
	patient_condition varchar(12) NULL,
	doctor_id int NULL,
	nursing_level tinyint NULL,
	allergy_drug varchar(40) NULL,
	adverse_effect varchar(40) NULL,
	admit_datetime datetime NULL,
	operating_datetime datetime NULL,
	in_hospital_id varchar(14) NULL,
	prepay_fee decimal(9, 2) NULL,
	medical_record_no varchar(14) NULL,
	insurance_code varchar(18) NULL,
	weight decimal(5, 2) NULL,
	height decimal(5, 2) NULL,
	hospital_stay_days tinyint NULL,
	baby_flag tinyint NULL,
	contact_way varchar(15) NULL,
	family_address varchar(40) NULL,
	remark varchar(80) NULL
);

CREATE TABLE i_lab_master (
	lab_master_id int NOT NULL,
	status varchar(1) NULL,
	execute_datetime datetime NULL,
	execute_dept_id varchar(4) NULL,
	in_hospital_id varchar(14) NULL,
	name_phonetic varchar(16) NULL,
	patient_age tinyint NULL,
	patient_id int NULL,
	patient_name varchar(20) NULL,
	patient_sex varchar(2) NULL,
	print_flag tinyint NULL,
	clinic_diag varchar(80) NULL,
	report_checker varchar(8) NULL,
	reporter varchar(8) NULL,
	request_datetime datetime NULL,
	request_dept_id varchar(4) NULL,
	order_doctor_id int NULL,
	order_doctor_name varchar(8) NULL,
	result_report_datetime datetime NULL,
	received_datetime datetime NULL,
	sample_datetime datetime NULL,
	specimen varchar(10) NULL,
	specimen_notes varchar(20) NULL,
	test_cause varchar(20) NULL,
	test_subject varchar(20) NULL,
	working_id varchar(10) NULL
);

CREATE TABLE sys_group_module (
	group_role_id int NULL,
	module_id int NULL
);

CREATE TABLE sche_duty_records (
	sche_duty_id int NOT NULL,
	sche_master_id int NULL,
	duty_user_id int NULL,
	mon_duty_item_id int NULL,
	tue_duty_item_id int NULL,
	wed_duty_item_id int NULL,
	thu_duty_item_id int NULL,
	fri_duty_item_id int NULL,
	sat_duty_item_id int NULL,
	sun_duty_item_id int NULL
);

CREATE TABLE s_sys_user_department (
	his_user_id varchar(7) NOT NULL,
	his_dept_id varchar(4) NOT NULL
);

CREATE TABLE sche_duty_master (
	sche_master_id int NOT NULL,
	master_year smallint NULL,
	master_week_no tinyint NULL,
	dept_id smallint NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	duty_date_range varchar(21) NULL,
	remark varchar(80) NULL
);

CREATE TABLE i_lab_result (
	lab_result_id int NOT NULL,
	lab_result_name varchar(20) NULL,
	lab_result_code varchar(16) NULL,
	lab_master_id int NULL,
	result varchar(20) NULL,
	result_unit varchar(20) NULL,
	normal_flag varchar(1) NULL,
	ref_ranges varchar(20) NULL,
	test_datetime datetime NULL,
	instrument_name varchar(16) NULL,
	instrument_code varchar(16) NULL
);

CREATE TABLE order_exec_drug_master (
	exec_master_id int IDENTITY(1, 1) NOT NULL,
	his_patient_id varchar(10) NULL,
	patient_name varchar(20) NULL,
	order_type_code varchar(2) NULL,
	order_type varchar(12) NULL,
	order_exec_type varchar(10) NULL,
	exec_his_user_id varchar(7) NULL,
	speed varchar(12) NULL,
	exec_datetime datetime NULL,
	order_freq varchar(30) NULL,
	usage_code varchar(4) NULL,
	usage_name varchar(20) NULL,
	finish_datetime datetime NULL,
	finish_user_id varchar(20) NULL
);

CREATE TABLE sys_group_role (
	role_id int NOT NULL,
	role_name varchar(16) NULL,
	role_value varchar(30) NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	remark varchar(80) NULL
);

CREATE TABLE sys_user_group (
	user_id int NULL,
	group_id int NULL
);

CREATE TABLE s_dict_department (
	dept_id smallint IDENTITY(1, 1) NOT NULL,
	dept_name varchar(16) NULL,
	his_dept_id varchar(4) NULL,
	dept_short varchar(10) NULL,
	order_num smallint NULL,
	parent_id smallint NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	dept_path varchar(32) NULL,
	remark varchar(80) NULL
);

CREATE TABLE sys_group_role_rela (
	group_id int NULL,
	role_id int NULL
);

CREATE TABLE tend_eval_records (
	patient_id int NOT NULL,
	in_hospital_id varchar(14) NULL,
	sub_datetime datetime NOT NULL,
	dept_id int NULL,
	sub_user_id int NULL,
	records varchar(200) NULL
);

CREATE TABLE sys_user_online (
	user_online_id int IDENTITY(1, 1) NOT NULL,
	user_id int NOT NULL,
	token varchar(32) NULL,
	login_datetime datetime NULL,
	call_datetime datetime NULL
);

CREATE TABLE s_sys_users (
	user_id int IDENTITY(1, 1) NOT NULL,
	login_name varchar(8) NULL,
	passwd varchar(32) NULL,
	user_name varchar(10) NULL,
	age tinyint NULL,
	gender varchar(1) NULL,
	his_user_id varchar(7) NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	email varchar(28) NULL,
	login_ip varchar(15) NULL,
	remark varchar(80) NULL
);

CREATE TABLE s_barcode_info (
	bar_id int IDENTITY(1, 1) NOT NULL,
	bar_flag varchar(1) NULL,
	redis_key varchar(32) NULL,
	bar_value varchar(32) NULL,
	create_datetime datetime NULL,
	his_patient_id varchar(10) NULL,
	t_id int NULL
);

CREATE TABLE vital_sign_info (
	vital_sign_info_id int IDENTITY(0, 1) NOT NULL,
	exec_nurse varchar(20) NOT NULL,
	exec_time datetime NOT NULL,
	patient_id varchar(20) NOT NULL,
	temperature varchar(20) NULL,
	temperature_value varchar(10) NULL,
	temperature_flag smallint NULL,
	temperature_unit varchar(10) NULL,
	pluse varchar(20) NULL,
	pluse_value varchar(10) NULL,
	pluse_unit varchar(10) NULL,
	hr varchar(20) NULL,
	hr_value varchar(10) NULL,
	hr_unit varchar(10) NULL,
	breath varchar(20) NULL,
	breath_value varchar(10) NULL,
	breath_unit varchar(10) NULL,
	bp varchar(20) NULL,
	bp_value varchar(10) NULL,
	bp_unit varchar(10) NULL,
	in_take varchar(20) NULL,
	in_take_value varchar(10) NULL,
	in_take_unit varchar(10) NULL,
	urine varchar(20) NULL,
	urine_value varchar(10) NULL,
	urine_unit varchar(10) NULL,
	other_out_put varchar(20) NULL,
	other_out_put_value varchar(10) NULL,
	other_out_put_unit varchar(10) NULL,
	total_out_put varchar(20) NULL,
	total_out_put_value varchar(10) NULL,
	total_out_put_unit varchar(10) NULL,
	shit varchar(20) NULL,
	shit_value varchar(10) NULL,
	shit_unit varchar(10) NULL,
	height varchar(20) NULL,
	height_value varchar(10) NULL,
	height_unit varchar(10) NULL,
	weight varchar(20) NULL,
	weight_value varchar(10) NULL,
	weight_unit varchar(10) NULL,
	skin_test varchar(20) NULL,
	skin_test_value varchar(30) NULL,
	other_value varchar(50) NULL,
	event varchar(50) NULL,
	event_value varchar(50) NULL,
	remark_value varchar(100) NULL,
	in_patient_num varchar(20) NULL
);

CREATE TABLE tend_record_detail (
	tend_reco_item_id int NOT NULL,
	tend_reco_master_id int NOT NULL,
	tend_reco_item_class varchar(1) NULL,
	tend_reco_item_name varchar(20) NULL,
	tend_reco_item_type varchar(1) NULL,
	item_value varchar(20) NULL
);

CREATE TABLE tend_record_master (
	tend_reco_master_id int IDENTITY(1, 1) NOT NULL,
	patient_id int NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	remark varchar(80) NULL
);

CREATE TABLE sysdiagrams (
	diagram_id int IDENTITY(1, 1) NOT NULL,
	name nvarchar(128) NOT NULL,
	principal_id int NOT NULL,
	version int NULL,
	definition varbinary(MAX) NULL
);

CREATE TABLE tend_eval_detail (
	tend_eval_detail_id bigint IDENTITY(1, 1) NOT NULL,
	tend_eval_master_id int NULL,
	tend_eval_item_key varchar(20) NULL,
	tend_eval_value varchar(150) NULL
);

CREATE TABLE tend_item_detail (
	tend_item_id int NOT NULL,
	tend_item_master_id int NOT NULL,
	tend_item_class varchar(1) NULL,
	tend_item_type varchar(1) NULL,
	tend_item_name varchar(20) NULL
);

CREATE TABLE tend_item_master (
	tend_item_master_id int IDENTITY(1, 1) NOT NULL,
	patient_id varchar(11) NULL,
	active_flag tinyint NULL,
	create_user_id varchar(11) NULL,
	modify_user_id varchar(11) NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	remark varchar(80) NULL
);

CREATE TABLE sys_notepad (
	note_id int IDENTITY(1, 1) NOT NULL,
	operate_datetime datetime NULL,
	user_id int NULL,
	note_content varchar(255) NULL
);

CREATE TABLE tend_eval_master (
	tend_eval_master_id int IDENTITY(1, 1) NOT NULL,
	dept_id varchar(4) NULL,
	patient_id varchar(10) NULL,
	bed_id varchar(14) NULL,
	in_hospital_id varchar(14) NULL,
	report_checker varchar(14) NULL,
	create_user_id int NOT NULL,
	create_datetime datetime NOT NULL
);

CREATE TABLE tend_records_rowitem (
	tend_records_rowitem_id int IDENTITY(0, 1) NOT NULL,
	tend_records_row_id int NULL,
	item_data_source int NULL,
	manal_item_name varchar(20) NULL,
	tend_records_id int NULL,
	value varchar(20) NULL,
	unit varchar(5) NULL
);

CREATE TABLE tend_records_row (
	tend_records_row_id int IDENTITY(0, 1) NOT NULL,
	tend_records_id int NULL,
	patient_id int NULL,
	create_datetime datetime NULL,
	description varchar(500) NULL,
	user_id int NULL
);

CREATE TABLE tend_records_title (
	tend_records_title_id int IDENTITY(0, 1) NOT NULL,
	tend_records_name varchar(20) NULL
);

CREATE TABLE sys_groups (
	group_id int IDENTITY(1, 1) NOT NULL,
	group_name varchar(16) NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	remark varchar(80) NULL
);

CREATE TABLE vital_sign_records (
	sign_rec_id int IDENTITY(1, 1) NOT NULL,
	item_value varchar(10) NULL,
	record_datetime datetime NULL,
	record_user_id int NULL,
	patient_id int NULL,
	sign_item_id int NULL,
	modify_datetime datetime NULL,
	modify_user_id int NULL,
	remark varchar(80) NULL
);

CREATE TABLE sys_modules (
	module_id int NOT NULL,
	module_name varchar(20) NULL,
	module_value varchar(16) NULL,
	module_logo varchar(10) NULL,
	order_num tinyint NULL,
	parent_module_id int NULL,
	active_flag tinyint NULL,
	create_user_id int NULL,
	modify_user_id int NULL,
	create_datetime datetime NULL,
	modify_datetime datetime NULL,
	module_path varchar(20) NULL,
	remark varchar(80) NULL
);

CREATE TABLE tend_records_dept (
	tend_records_title_id int NOT NULL,
	dept_id int NOT NULL
);

