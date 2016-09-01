SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [ass_nurse_item_dept] (
	[nurse_item_code] varchar(20) NOT NULL,
	[dept_code] varchar(10) NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [ass_nurse_record_dept] (
	[item_code] varchar(20) NOT NULL,
	[dept_code] varchar(10) NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [bed] (
	[bed_code] varchar(12) NOT NULL,
	[dept_code] varchar(10),
	[ward_code] varchar(10),
	[bed_type_code] char(1),
	[bed_type_name] varchar(10),
	[bed_price] decimal(10, 0),
	[use_datetime] datetime,
	[room_code] varchar(10),
	[ward_name] varchar(10),
	[dept_name] varchar(10),
	[bed_status] char(1) NOT NULL CONSTRAINT [DF_bed_bed_status] DEFAULT ('U'),
	[duty_nurse_id] varchar(10),
	[duty_nurse_name] varchar(10),
	[nurse_cell_code] varchar(4)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [criticalvalue_info] (
	[criticalvalue_info_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[recive_people] int NOT NULL,
	[bed_code] varchar(20) NOT NULL,
	[in_hosp_no] int NOT NULL,
	[send_people] varchar(20) NOT NULL,
	[send_time] datetime NOT NULL,
	[criticalvalue] varchar(30),
	[disponer] varchar(10),
	[dispone_datetime] datetime
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [DATA_SOURCE] (
	[REFID] varchar(32) NOT NULL,
	[DATA_SOURCE_TYPE] nvarchar(10),
	[SOURCE_NAME] nvarchar(60),
	[JSON_PATH] nvarchar(60),
	[PRAGRME] nvarchar(100),
	[REMARK] nvarchar(300),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [DEPARTMENT_TEMPALTE] (
	[REFID] varchar(32) NOT NULL,
	[DEPT_REFID] varchar(32),
	[NODE_TYPE] nvarchar(10),
	[NODE_NAME] nvarchar(60),
	[NODE_PARENT_REFID] varchar(32),
	[ORD] int,
	[TEMPLATE_TYPE] nvarchar(10),
	[NUMBER_TYPE] nvarchar(10),
	[TEMPLATE_REFID] varchar(32),
	[URL] nvarchar(100),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_body_sign] (
	[item_code] varchar(20) NOT NULL,
	[item_name] varchar(20) NOT NULL,
	[item_unit] varchar(10) NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_diag] (
	[seq_no] varchar(10) NOT NULL,
	[icd_code] varchar(50),
	[py_code] varchar(8),
	[wb_code] varchar(8),
	[icd_name] varchar(150),
	[infectious] tinyint DEFAULT ((0)),
	[cancerous] tinyint DEFAULT ((0)),
	[applied_gender] varchar(1) DEFAULT ('A'),
	[is_valid] tinyint DEFAULT ((1))
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_drug] (
	[drug_id] int IDENTITY,
	[drug_code] varchar(12) NOT NULL,
	[drug_name] varchar(80) NOT NULL,
	[py_code] varchar(18),
	[wb_code] varchar(18),
	[drug_type] char(1) NOT NULL CONSTRAINT [DF__dict_drug__drug___01142BA1] DEFAULT ('P'),
	[dose_base] decimal(12, 2) CONSTRAINT [DF__dict_drug__dose___02084FDA] DEFAULT (NULL),
	[dose_unit] varchar(4) CONSTRAINT [DF__dict_drug__dose___02FC7413] DEFAULT (NULL),
	[drug_spec] varchar(32) CONSTRAINT [DF__dict_drug__drug___03F0984C] DEFAULT (NULL),
	[pack_unit] varchar(4) CONSTRAINT [DF__dict_drug__pack___04E4BC85] DEFAULT (NULL),
	[pack_qty] int CONSTRAINT [DF__dict_drug__pack___05D8E0BE] DEFAULT (NULL),
	[min_use_unit] varchar(10) CONSTRAINT [DF__dict_drug__min_u__06CD04F7] DEFAULT (NULL),
	[min_bill_unit] varchar(16) CONSTRAINT [DF__dict_drug__min_b__07C12930] DEFAULT (NULL),
	[order_class_code] varchar(3) CONSTRAINT [DF__dict_drug__order__08B54D69] DEFAULT (NULL),
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__dict_drug__is_va__09A971A2] DEFAULT ((1)),
	[create_date] datetime CONSTRAINT [DF__dict_drug__creat__0A9D95DB] DEFAULT (NULL)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_freq] (
	[freq_code] varchar(10) NOT NULL,
	[freq_name] varchar(30) NOT NULL,
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__dict_freq__is_va__20C1E124] DEFAULT ((1)),
	[create_date] datetime NOT NULL CONSTRAINT [DF_dict_freq_create_date] DEFAULT (getdate())
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_lab_sample] (
	[lab_sample_code] varchar(20) NOT NULL,
	[lab_sample_name] varchar(30) NOT NULL,
	[priority] int NOT NULL CONSTRAINT [DF_dict_lab_sample_priority] DEFAULT ((1)),
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__dict_lab___is_va__787EE5A0] DEFAULT ((1)),
	[create_date] datetime NOT NULL CONSTRAINT [DF_dict_lab_sample_create_date] DEFAULT (getdate())
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_measure_note] (
	[measure_note_code] varchar(10) NOT NULL,
	[measure_note_name] varchar(20) NOT NULL,
	[in_nurse_shift] tinyint NOT NULL CONSTRAINT [DF__dict_meas__in_nu__7E37BEF6] DEFAULT ((0))
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_measure_rule] (
	[rule_id] int IDENTITY,
	[rule_code] varchar(10) NOT NULL,
	[rule_name] varchar(20) NOT NULL,
	[rule_type] varchar(10) NOT NULL,
	[rule_value] varchar(10) NOT NULL,
	[rule_time] varchar(100) NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_nurse_item] (
	[nurse_item_code] varchar(10) NOT NULL,
	[nurse_item_name] varchar(20) NOT NULL,
	[nurse_item_type_code] varchar(1) NOT NULL CONSTRAINT [DF_dict_nurse_item_nurse_item_type_code] DEFAULT ('B'),
	[nurse_item_type_name] varchar(20),
	[is_valid] tinyint NOT NULL CONSTRAINT [DF_dict_nurse_item_is_valid] DEFAULT ((1))
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_nurse_record] (
	[item_code] varchar(20) NOT NULL,
	[item_name] varchar(30) NOT NULL,
	[item_unit] varchar(10),
	[parent_code] varchar(20),
	[is_valid] tinyint NOT NULL CONSTRAINT [DF_dict_nurse_record_is_valid] DEFAULT ((1)),
	[priority] int
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_nurse_record_value] (
	[value_code] varchar(20) NOT NULL,
	[value_name] varchar(30) NOT NULL,
	[item_code] varchar(20) NOT NULL,
	[is_valid] tinyint NOT NULL CONSTRAINT [DF_dict_nurse_record_value_is_valid] DEFAULT ((1)),
	[seq_no] int NOT NULL CONSTRAINT [DF_dict_nurse_record_value_seq_no] DEFAULT ((1))
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_order_class] (
	[order_class_code] varchar(3) NOT NULL,
	[order_class_name] varchar(10) NOT NULL,
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__dict_orde__is_va__1BFD2C07] DEFAULT ((1)),
	[create_date] datetime NOT NULL CONSTRAINT [DF_dict_order_class_create_date] DEFAULT (getdate())
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_undrug] (
	[undrug_id] int IDENTITY,
	[undrug_code] varchar(12) NOT NULL,
	[undrug_name] varchar(80) NOT NULL,
	[py_code] varchar(18),
	[wb_code] varchar(18),
	[order_class_code] varchar(3) NOT NULL,
	[min_bill_unit] varchar(10),
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__dict_undr__is_va__0E6E26BF] DEFAULT ((1)),
	[create_date] datetime NOT NULL CONSTRAINT [DF__dict_undr__creat__0F624AF8] DEFAULT (getdate())
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_usage] (
	[usage_code] varchar(10) NOT NULL,
	[usage_name] varchar(30) NOT NULL,
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__dict_usag__is_va__276EDEB3] DEFAULT ((1)),
	[create_date] datetime NOT NULL CONSTRAINT [DF_dict_usage_create_date] DEFAULT (getdate())
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dict_ward] (
	[ward_code] varchar(4) NOT NULL,
	[ward_name] varchar(16) NOT NULL,
	[ward_name_py] varchar(8),
	[is_in_use] tinyint NOT NULL CONSTRAINT [DF_dict_ward_is_in_use] DEFAULT ((1)),
	[create_date] datetime NOT NULL CONSTRAINT [DF_dict_ward_create_date] DEFAULT (getdate())
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [DOC_ITEM_OPTION] (
	[REFID] varchar(32) NOT NULL,
	[DOC_ITEM_REFID] varchar(32),
	[R_NURSE_DOC_REFID] varchar(32) NOT NULL,
	[METADATA_OPTION_REFID] varchar(32),
	[METADATA_REFID] varchar(32),
	[OPTION_NAME] nvarchar(60),
	[OPTION_CODE] nvarchar(20),
	[SCORE] int,
	[OPTION_TYPE] nvarchar(10),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [DOC_TABLE_ITEM] (
	[REFID] varchar(32) NOT NULL,
	[DOC_ITEM_REFID] varchar(32) NOT NULL,
	[R_NURSE_DOC_REFID] varchar(32) NOT NULL,
	[TABLE_ITEM_REFID] varchar(32),
	[TABLE_REFID] varchar(32),
	[METADATA_REFID] varchar(32),
	[TITLE] nvarchar(60),
	[KEY_FLAG] char(1),
	[SHOW_FLAG] char(1),
	[COPY_FLAG] char(1),
	[ORD] int,
	[ALTERNATE_FLAG] char(1),
	[EMPTY_FLAG] char(1),
	[WIDTH] int,
	[HEIGHT] int,
	[READONLY_FLAG] char(1),
	[TIME_KEY_FLAG] char(1),
	[MIN_VALUE] numeric(10, 3),
	[MAX_VALUE] numeric(10, 3),
	[NORMAL_MIN_VALUE] numeric(10, 2),
	[NORMAL_MAX_VALUE] numeric(10, 2),
	[VERIFY_POLICY_CODE] nvarchar(100),
	[SEARCH_FLAG] char(1),
	[TEMPLATE_MENU_FLAG] char(1),
	[SAVE_FLAG] char(1),
	[EDIT_FLAG] char(1),
	[MUST_INPUT_FLAG] char(1),
	[NDA_FLAG] char(1),
	[METADATA_NAME] nvarchar(60),
	[METADATA_SIMPLE_NAME] nvarchar(60),
	[METADATA_CODE] nvarchar(20),
	[DATA_SOURCE_REFID] varchar(32),
	[R_DATA_SOURCE_TYPE] nvarchar(10),
	[DATA_SOURCE_PATH] nvarchar(60),
	[INPUT_TYPE] nvarchar(10),
	[DATA_TYPE] nvarchar(10),
	[SCOPE_TYPE] nvarchar(10),
	[EVALUATE_TYPE] nvarchar(10),
	[EVALUATE_CODE] nvarchar(30),
	[EVALUATE_SCORE] int,
	[UNIT] nvarchar(30),
	[PRECISIONS] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [DOC_TABLE_ITEM_OPTION] (
	[REFID] varchar(32) NOT NULL,
	[DOC_TABLE_ITEM_REFID] varchar(32),
	[R_NURSE_DOC_REFID] varchar(32) NOT NULL,
	[METADATA_OPTION_REFID] varchar(32),
	[METADATA_REFID] varchar(32),
	[OPTION_NAME] nvarchar(60),
	[OPTION_CODE] nvarchar(20),
	[SCORE] int,
	[OPTION_TYPE] nvarchar(10),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [DOC_TABLE_PATTERN] (
	[REFID] varchar(32) NOT NULL,
	[NURSE_DOC_REFID] varchar(32) NOT NULL,
	[TABLE_PATTERN_REFID] varchar(32),
	[TABLE_REFID] varchar(32) NOT NULL,
	[PATTERN_NAME] nvarchar(60),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [DOC_TABLE_PATTERN_ITEM] (
	[REFID] varchar(32) NOT NULL,
	[DOC_TABLE_ITEM_REFID] varchar(32) NOT NULL,
	[R_NURSE_DOC_REFID] varchar(32) NOT NULL,
	[DOC_TABLE_PATTERN_REFID] varchar(32),
	[R_METADATA_NAME] nvarchar(60),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [FIELD_SWITCH] (
	[REFID] varchar(32) NOT NULL,
	[FROM_FIELD_CODE] nvarchar(20),
	[FROM_FIELD_NAME] nvarchar(60),
	[TO_TYPE] nvarchar(20),
	[TO_FIELD_CODE] nvarchar(20),
	[TO_FIELD_NAME] nvarchar(60),
	[SWITCH_PROGRAME_REFID] varchar(32),
	[REMARK] nvarchar(300),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [hosp_duty] (
	[id] int IDENTITY,
	[dept_id] int NOT NULL,
	[user_id] int,
	[user_name] varchar(20),
	[tel] varchar(30) NOT NULL,
	[type] varchar(10) NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [item_price] (
	[item_price_id] int IDENTITY,
	[item_type] varchar(1) NOT NULL CONSTRAINT [DF_item_price_item_type] DEFAULT ((1)),
	[item_code] varchar(12) NOT NULL,
	[item_price] decimal(12, 2) NOT NULL,
	[begin_date] datetime,
	[end_date] datetime
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [METADATA] (
	[REFID] varchar(32) NOT NULL,
	[METADATA_NAME] nvarchar(60),
	[METADATA_SIMPLE_NAME] nvarchar(60),
	[METADATA_NAME_PINYIN] nvarchar(100),
	[METADATA_CODE] nvarchar(20),
	[DATA_SOURCE_REFID] varchar(32),
	[R_DATA_SOURCE_TYPE] nvarchar(10),
	[DATA_SOURCE_PATH] nvarchar(60),
	[INPUT_TYPE] nvarchar(10),
	[DATA_TYPE] nvarchar(10),
	[SCOPE_TYPE] nvarchar(10),
	[READONLY_FLAG] char(1),
	[TIME_KEY_FLAG] char(1),
	[MIN_VALUE] numeric(10, 2),
	[MAX_VALUE] numeric(10, 2),
	[NORMAL_MIN_VALUE] numeric(10, 2),
	[NORMAL_MAX_VALUE] numeric(10, 2),
	[VERIFY_POLICY_CODE] nvarchar(100),
	[EVALUATE_TYPE] nvarchar(10),
	[EVALUATE_CODE] nvarchar(30),
	[EVALUATE_SCORE] int,
	[WIDTH] int,
	[HEIGHT] int,
	[UNIT] nvarchar(30),
	[PRECISIONS] int,
	[REMARK] nvarchar(300),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [METADATA_OPTION] (
	[REFID] varchar(32) NOT NULL,
	[METADATA_REFID] varchar(32),
	[OPTION_NAME] nvarchar(60),
	[OPTION_CODE] nvarchar(20),
	[SCORE] int,
	[OPTION_TYPE] nvarchar(10),
	[ORD] int,
	[REMARK] nvarchar(300),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [NURSE_DOC_DATA] (
	[REFID] varchar(32) NOT NULL,
	[NURSE_DOC_REFID] varchar(32),
	[FROM_DOC_DATA_REFID] varchar(32),
	[HOSPITAL_NO] nvarchar(10),
	[PATIENT_REFID] varchar(32),
	[ITEM_CODE] nvarchar(20),
	[ITEM_VALUE] nvarchar(100),
	[ITEM_INPUT_VALUE] nvarchar(100),
	[ITEM_VALUE_FULL] nvarchar(100),
	[ITEM_KEY_CODE] nvarchar(100),
	[ITEM_KEY_VALUE] nvarchar(300),
	[NORMAL_MIN_VALUE] numeric(10, 2),
	[NORMAL_MAX_VALUE] numeric(10, 2),
	[DOC_METADATA_REFID] varchar(32),
	[JSON_PATH] nvarchar(100),
	[DELETE_FLAG] char(1),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [NURSE_DOC_DATA_HIS] (
	[REFID] varchar(32) NOT NULL,
	[NURSE_DOC_DATA_REFID] varchar(32),
	[NURSE_DOC_REFID] varchar(32),
	[FROM_DOC_DATA_REFID] varchar(32),
	[HOSPITAL_NO] nvarchar(10),
	[PATIENT_REFID] varchar(32),
	[ITEM_CODE] nvarchar(20),
	[ITEM_VALUE] nvarchar(100),
	[ITEM_INPUT_VALUE] nvarchar(100),
	[NEW_ITEM_VALUE] nvarchar(100),
	[NEW_ITEM_INPUT_VALUE] nvarchar(100),
	[CHANGE_NURSER_REFID] varchar(32),
	[R_CHANGE_NURSER_NAME] nvarchar(60),
	[CHANGE_TIME] datetime,
	[ITEM_VALUE_FULL] nvarchar(100),
	[ITEM_KEY_CODE] nvarchar(100),
	[ITEM_KEY_VALUE] nvarchar(300),
	[NORMAL_MIN_VALUE] numeric(10, 2),
	[NORMAL_MAX_VALUE] numeric(10, 2),
	[DOC_METADATA_REFID] varchar(32),
	[JSON_PATH] nvarchar(100),
	[DELETE_FLAG] char(1),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [NURSE_DOC_ITEM] (
	[REFID] varchar(32) NOT NULL,
	[NURSE_DOC_REFID] varchar(32) NOT NULL,
	[TEMPLATE_ITEM_REFID] varchar(32) NOT NULL,
	[TEMPLATE_REFID] varchar(32),
	[ITEM_FROM] nvarchar(10),
	[METADATA_REFID] varchar(32),
	[TABLE_REFID] varchar(32),
	[WIDTH] int,
	[HEIGHT] int,
	[COPY_FLAG] char(1),
	[SEARCH_FLAG] char(1),
	[TEMPLATE_MENU_FLAG] char(1),
	[NDA_FIELD_TYPE] nvarchar(10),
	[SAVE_FLAG] char(1),
	[EDIT_FLAG] char(1),
	[MUST_INPUT_FLAG] char(1),
	[AREA_NAME] nvarchar(60),
	[ORD] int,
	[TABLE_NAME] nvarchar(60),
	[TABLE_CODE] nvarchar(30),
	[TABLE_TYPE] nvarchar(10),
	[HEAD_DIRECTION] nvarchar(10),
	[EDIT_LAYOUT_TYPE] nvarchar(10),
	[MODIFY_SUMMARY_PROGRAM] nvarchar(100),
	[EDIT_SAVE_PROGRAME] nvarchar(100),
	[EDIT_URL] nvarchar(100),
	[METADATA_NAME] nvarchar(60),
	[METADATA_SIMPLE_NAME] nvarchar(60),
	[METADATA_CODE] nvarchar(20),
	[DATA_SOURCE_REFID] varchar(32),
	[R_DATA_SOURCE_TYPE] nvarchar(10),
	[DATA_SOURCE_PATH] nvarchar(60),
	[INPUT_TYPE] nvarchar(10),
	[DATA_TYPE] nvarchar(10),
	[SCOPE_TYPE] nvarchar(10),
	[READONLY_FLAG] char(1),
	[TIME_KEY_FLAG] char(1),
	[MIN_VALUE] numeric(10, 2),
	[MAX_VALUE] numeric(10, 2),
	[NORMAL_MIN_VALUE] numeric(10, 2),
	[NORMAL_MAX_VALUE] numeric(10, 2),
	[VERIFY_POLICY_CODE] nvarchar(100),
	[EVALUATE_TYPE] nvarchar(10),
	[EVALUATE_CODE] nvarchar(30),
	[EVALUATE_SCORE] int,
	[UNIT] nvarchar(30),
	[PRECISIONS] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [NURSE_DOC_OPERATE] (
	[REFID] varchar(32) NOT NULL,
	[NURSE_DOC_REFID] varchar(32),
	[TEMPLATE_REFID] varchar(32),
	[OPERATE_REFID] varchar(32),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [NURSE_DOCUMENT] (
	[REFID] varchar(32) NOT NULL,
	[HOSPITAL_NO_TEMPLATE_REFID] varchar(32),
	[TEMPLATE_REFID] varchar(32),
	[R_TEMPLATE_NAME] nvarchar(60),
	[R_TEMPLATE_TYPE] nvarchar(10),
	[PATIENT_REFID] varchar(32),
	[R_PATIENT_NAME] nvarchar(60),
	[R_PATIENT_BARCODE] nvarchar(10),
	[HOSPITAL_NO] nvarchar(10),
	[DEPT_REFID] varchar(32),
	[BABY_REFID] varchar(32),
	[R_BABY_NAME] nvarchar(60),
	[DOC_NO] nvarchar(10),
	[DOC_TYPE] nvarchar(10),
	[NUMBER_TYPE] nvarchar(10),
	[PAGER_CODE] nvarchar(10),
	[VERTICAL_FLAG] char(1),
	[LOCAL_DATA_FLAG] char(1),
	[DATA_PRAGRAM] nvarchar(60),
	[BEGIN_TIME] datetime,
	[END_TIME] datetime,
	[END_FLAG] char(1),
	[STATUS] nvarchar(10),
	[WRITE_NURSER] varchar(32),
	[R_WRITE_NURSER_NAME] nvarchar(60),
	[WRITE_TIME] datetime,
	[APPROVE_NURSER] varchar(32),
	[R_APPROVE_NURSER_NAME] nvarchar(60),
	[APPROVE_TIME] datetime,
	[PRINT_COUNT] int,
	[PRINT_NURSER] varchar(32),
	[R_PRINT_NURSER_NAME] nvarchar(60),
	[PRINT_TIME] datetime,
	[TABLE_PATTERN] varchar(32),
	[TEMPLATE_HTML] text,
	[INSTANT_URL] nvarchar(100),
	[NDA_URL] nvarchar(100),
	[SEARCH_URL] nvarchar(100),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
 TEXTIMAGE_ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [OPERATE] (
	[REFID] varchar(32) NOT NULL,
	[OPERATE_TYPE] nvarchar(10),
	[OPERATE_NAME] nvarchar(60),
	[OPERATE_CODE] nvarchar(30),
	[PRAGRME] nvarchar(100),
	[REMARK] nvarchar(300),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [OPERATE_FIELD_SWITCH] (
	[REFID] varchar(32) NOT NULL,
	[OPERATE_REFID] varchar(32),
	[FIELD_SWITCH_REFID] varchar(32),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [order_exec] (
	[order_exec_id] int IDENTITY,
	[order_group_id] int NOT NULL,
	[plan_datetime] datetime NOT NULL,
	[exec_datetime] datetime,
	[exec_nurse_code] varchar(6),
	[exec_nurse_name] varchar(10),
	[finish_datetime] datetime,
	[finish_nurse_code] varchar(6),
	[finish_nurse_name] varchar(10),
	[order_exec_barcode] varchar(20),
	[deliver_speed] int,
	[pda_exec_datetime] datetime,
	[pda_exec_nurse_code] varchar(6),
	[pda_exec_nurse_name] varchar(10),
	[deliver_speed_unit] varchar(10),
	[approve_nurse_code] varchar(6),
	[approve_nurse_name] varchar(10)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [order_freq_plan_time] (
	[freq_code] varchar(10) NOT NULL,
	[dept_code] varchar(10) NOT NULL,
	[dept_name] varchar(16),
	[plan_time] varchar(80),
	[create_time] datetime
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [order_group] (
	[order_group_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[pat_name] varchar(10) NOT NULL,
	[pat_bed_code] varchar(10) NOT NULL,
	[freq_code] varchar(10),
	[freq_name] varchar(30),
	[order_type_code] varchar(10),
	[order_type_name] varchar(16),
	[order_exec_type_code] varchar(10),
	[order_exec_type_name] varchar(16),
	[create_datetime] datetime,
	[create_doc_id] varchar(10),
	[create_doc_name] varchar(10),
	[stop_datetime] datetime,
	[stop_doc_id] varchar(10),
	[stop_doc_name] varchar(10),
	[order_status_code] char(1) NOT NULL CONSTRAINT [DF_order_group_order_status_code] DEFAULT ((0)),
	[order_status_name] varchar(16),
	[usage_code] varchar(10),
	[usage_name] varchar(16),
	[emergent] tinyint NOT NULL CONSTRAINT [DF_order_group_emergent] DEFAULT ((0)),
	[his_comb_no] varchar(14),
	[confirm_datetime] datetime,
	[confirm_nurse_code] varchar(10),
	[confirm_nurse_name] varchar(10),
	[is_skin_test] tinyint,
	[plan_exec_time] varchar(80),
	[plan_first_exec_time] varchar(5)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [order_item] (
	[order_item_id] int IDENTITY,
	[order_group_id] int NOT NULL,
	[skt_order_group_id] int,
	[item_code] varchar(12) NOT NULL,
	[item_name] varchar(80) NOT NULL,
	[dosage] decimal(10, 2),
	[dosage_unit] varchar(16),
	[skin_test_required] tinyint NOT NULL CONSTRAINT [DF_order_item_skin_test_required] DEFAULT ((0)),
	[drug_specs] varchar(32),
	[specimen_code] varchar(20),
	[specimen_name] varchar(30),
	[exam_loc_code] varchar(20),
	[exam_loc_name] varchar(30),
	[remark] varchar(30)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [PAGER] (
	[REFID] varchar(32) NOT NULL,
	[PAGER_NAME] nvarchar(60),
	[PAGER_CODE] nvarchar(10),
	[WIDTH] int,
	[HEIGHT] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [pat_base] (
	[pat_id] int IDENTITY,
	[pat_name] varchar(20) NOT NULL,
	[pat_name_py] varchar(5),
	[gender] char(1) NOT NULL CONSTRAINT [DF_pat_base_gender] DEFAULT ('U'),
	[birthday] datetime,
	[is_married] tinyint NOT NULL CONSTRAINT [DF_pat_base_is_married] DEFAULT ((0)),
	[education_level] char(1) NOT NULL CONSTRAINT [DF_pat_base_education_level] DEFAULT ((0)),
	[financial_level] char(1) NOT NULL CONSTRAINT [DF_pat_base_financial_level] DEFAULT ((0)),
	[insurance_no] varchar(15),
	[medical_record_no] varchar(15),
	[height] int,
	[weight] int,
	[id_card_no] varchar(20),
	[career_code] varchar(4),
	[birth_place_code] varchar(2),
	[phone_no] varchar(16)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [pat_hosp] (
	[in_hosp_no] int IDENTITY,
	[pat_id] int NOT NULL,
	[bed_code] varchar(10),
	[in_state] char(1) NOT NULL CONSTRAINT [DF_pat_hosp_in_state] DEFAULT ('R'),
	[tend_level] char(1) NOT NULL CONSTRAINT [DF_pat_hosp_tend_level] DEFAULT ((3)),
	[illness_status] char(1) NOT NULL CONSTRAINT [DF_pat_hosp_illness_status] DEFAULT ((0)),
	[charge_type] char(1) NOT NULL CONSTRAINT [DF_pat_hosp_charge_type] DEFAULT ((0)),
	[admit_datetime] datetime,
	[admit_diagnosis] varchar(20),
	[surgery_datetime] datetime,
	[pat_barcode] varchar(20),
	[out_datetime] datetime,
	[diet_code] varchar(20),
	[duty_doc_code] varchar(10),
	[duty_doc_name] varchar(20),
	[duty_nurse_code] varchar(10),
	[duty_nurse_name] varchar(20),
	[fee_paid] decimal(10, 0),
	[fee_used] decimal(10, 0),
	[trans_in_datetime] datetime,
	[trans_out_datetime] datetime,
	[receive_datetime] datetime,
	[receive_nurse_code] varchar(10),
	[out_card_no] varchar(16),
	[his_pat_no] varchar(16),
	[ward_code] varchar(10),
	[ward_name] nvarchar(8),
	[dept_code] varchar(10),
	[dept_name] varchar(16),
	[diet_name] varchar(80)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [phone_bindings] (
	[binding_id] int IDENTITY,
	[aor] varchar(50),
	[contact] varchar(150),
	[callid] varchar(80),
	[cseq] int,
	[expiration_time] bigint
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [poc_bindings] (
	[bindind_id] int IDENTITY,
	[aor] varchar(50),
	[contact] varchar(150),
	[callid] varchar(80),
	[cseq] int,
	[expirationtime] bigint
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_allergy] (
	[rec_allergy_id] int IDENTITY,
	[allergen_code] varchar(12) NOT NULL,
	[pat_id] int NOT NULL,
	[allergy_degree] varchar(1) NOT NULL CONSTRAINT [DF_rec_allergy_allergy_degree] DEFAULT ((0)),
	[allergen_name] varchar(80),
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__rec_aller__is_va__72C60C4A] DEFAULT ((1)),
	[rec_date] datetime NOT NULL CONSTRAINT [DF_rec_allergy_rec_date] DEFAULT (getdate()),
	[rec_empl_code] varchar(6) NOT NULL,
	[priority] int,
	[modify_date] datetime,
	[modify_empl_code] varchar(6)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_body_sign_detail] (
	[body_sign_detail_id] int IDENTITY,
	[body_sign_mas_id] int NOT NULL,
	[item_code] varchar(20),
	[item_value] varchar(10),
	[measure_note_code] varchar(10),
	[measure_note_name] varchar(20),
	[abnormal_flag] tinyint NOT NULL CONSTRAINT [DF_rec_body_sign_detail_abnormal_flag] DEFAULT ((0))
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_body_sign_mas] (
	[body_sign_mas_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[cooled] tinyint NOT NULL CONSTRAINT [DF_rec_body_sign_mas_cooled] DEFAULT ((0)),
	[rec_datetime] datetime NOT NULL CONSTRAINT [DF_rec_body_sign_mas_rec_datetime] DEFAULT (getdate()),
	[rec_nurse_code] varchar(6) NOT NULL,
	[remark] varchar(50)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_diag] (
	[rec_diag_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[icd_code] varchar(50) NOT NULL,
	[diag_name] varchar(150) NOT NULL,
	[is_main] tinyint NOT NULL CONSTRAINT [DF_rec_diag_is_main] DEFAULT ((1)),
	[rec_date] datetime NOT NULL CONSTRAINT [DF_rec_diag_rec_date] DEFAULT (getdate()),
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__rec_diag__is_val__75A278F5] DEFAULT ((1)),
	[rec_empl_code] varchar(6) NOT NULL,
	[diag_kind] varchar(2),
	[priority] int,
	[modify_date] datetime,
	[modify_empl_code] varchar(6)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_infu_monitor] (
	[infu_monitor_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[rec_datetime] datetime NOT NULL,
	[rec_nurse_code] varchar(6) NOT NULL,
	[order_exec_id] int NOT NULL,
	[pat_name] varchar(10),
	[rec_nurse_name] varchar(10),
	[abnormal] tinyint NOT NULL CONSTRAINT [DF__rec_infu___abnor__3D5E1FD2] DEFAULT ((0)),
	[deliver_speed] int,
	[anomaly_msg] varchar(20),
	[anomaly_disposal] varchar(20),
	[deliver_speed_unit] varchar(10)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_inspection_detail] (
	[insp_detail_id] int IDENTITY,
	[insp_mas_id] int NOT NULL,
	[body_parts] varchar(20) NOT NULL,
	[insp_result] varchar(100) NOT NULL,
	[insp_suggestion] varchar(100)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_inspection_mas] (
	[insp_mas_id] int IDENTITY,
	[insp_subject] varchar(30),
	[pat_id] int NOT NULL,
	[applicant] varchar(10),
	[reporter] varchar(10),
	[auditor] varchar(10),
	[insp_datetime] datetime NOT NULL,
	[report_datetime] datetime NOT NULL,
	[status] varchar(10),
	[pri_flag] int
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_lab_test_detail] (
	[lab_test_detail_id] int IDENTITY,
	[lab_test_mas_id] int NOT NULL,
	[item_code] varchar(20),
	[item_name] varchar(40),
	[result_value] varchar(20),
	[result_unit] varchar(20),
	[ref_ranges] varchar(20),
	[normal_flag] varchar(1) NOT NULL CONSTRAINT [DF_rec_lab_test_detail_normal_flag] DEFAULT ('N'),
	[inst_code] varchar(16),
	[inst_name] varchar(16)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_lab_test_mas] (
	[lab_test_mas_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[pat_hosp_no] varchar(10) NOT NULL,
	[order_group_id] int NOT NULL,
	[order_exec_id] int,
	[lab_test_status] varchar(1) NOT NULL CONSTRAINT [DF_rec_lab_test_mas_lab_test_status] DEFAULT ('R'),
	[exec_datetime] datetime NOT NULL CONSTRAINT [DF_rec_lab_test_mas_exec_datetime] DEFAULT (getdate()),
	[exec_dept_code] varchar(4) NOT NULL,
	[test_subject] varchar(20),
	[test_specimen] varchar(20),
	[reporter_code] varchar(10),
	[confirmer_code] varchar(10),
	[record_flag] int,
	[pri_flag] int
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_nurse_item_detail] (
	[nurse_item_detail_id] int IDENTITY,
	[nurse_item_mas_id] int NOT NULL,
	[nurse_item_code] varchar(10)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_nurse_item_mas] (
	[nurse_item_mas_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[rec_datetime] datetime NOT NULL CONSTRAINT [DF_rec_nurse_item_mas_rec_datetime] DEFAULT (getdate()),
	[rec_nurse_code] varchar(6) NOT NULL,
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__rec_nurse__is_va__60A75C0F] DEFAULT ((1)),
	[remark] varchar(50) CONSTRAINT [DF__rec_nurse__remar__5FB337D6] DEFAULT (NULL)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_nurse_record_detail] (
	[nurse_record_detail_id] int IDENTITY,
	[nurse_record_mas_id] int NOT NULL,
	[item_code] varchar(20),
	[item_value_code] varchar(20),
	[item_value] varchar(20),
	[measure_note_code] varchar(10),
	[measure_note_name] varchar(400)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_nurse_record_mas] (
	[nurse_record_mas_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[rec_datetime] datetime NOT NULL CONSTRAINT [DF_rec_nurse_record_mas_rec_datetime] DEFAULT (getdate()),
	[rec_nurse_code] varchar(6) NOT NULL,
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__rec_nurse__is_va__6477ECF3] DEFAULT ((1)),
	[cooled] tinyint NOT NULL CONSTRAINT [DF__rec_nurse__coole__640DD89F] DEFAULT ((0)),
	[record_type_code] varchar(2),
	[remark] varchar(50) CONSTRAINT [DF__rec_nurse__remar__6383C8BA] DEFAULT (NULL)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_nurse_shift] (
	[rec_nurse_shift_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[rec_dept_code] varchar(4) NOT NULL,
	[rec_nurse_code] varchar(6) NOT NULL,
	[is_valid] tinyint NOT NULL CONSTRAINT [DF__rec_nurse__is_va__07020F21] DEFAULT ((1)),
	[rec_datetime] datetime NOT NULL CONSTRAINT [DF_rec_nurse_shift_rec_datetime] DEFAULT (getdate()),
	[event_info] varchar(500)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_pat_event] (
	[rec_pat_event_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[pat_name] varchar(10) NOT NULL,
	[body_sign_mas_id] int NOT NULL,
	[event_datetime] datetime NOT NULL CONSTRAINT [DF_rec_pat_event_event_datetime] DEFAULT (getdate()),
	[rec_nurse_code] varchar(6) NOT NULL,
	[rec_nurse_name] varchar(10),
	[confirm_nurse_code] varchar(6),
	[confirm_nurse_name] varchar(10),
	[problem] varchar(30),
	[interv] varchar(30),
	[outcome] varchar(30),
	[event_code] varchar(10),
	[event_name] varchar(20)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [rec_skin_test] (
	[rec_skin_test_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[pat_name] varchar(20) NOT NULL,
	[test_nurse_code] varchar(6) NOT NULL,
	[test_result] varchar(1) NOT NULL CONSTRAINT [DF_rec_skin_test_test_result] DEFAULT ('N'),
	[test_datetime] datetime NOT NULL CONSTRAINT [DF_rec_skin_test_test_datetime] DEFAULT (getdate()),
	[drug_code] varchar(12) NOT NULL,
	[drug_name] varchar(80) NOT NULL,
	[order_group_id] int,
	[order_exec_id] int,
	[body_sign_mas_id] int,
	[drug_batch_no] varchar(30),
	[test_nurse_name] varchar(10),
	[approve_nurse_code] varchar(6),
	[approve_nurse_name] varchar(10)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_dept] (
	[dept_code] varchar(4) NOT NULL,
	[dept_name] varchar(16) NOT NULL,
	[dept_name_py] varchar(8),
	[is_in_use] int,
	[create_date] datetime
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_employee] (
	[empl_code] varchar(6) NOT NULL,
	[empl_name] varchar(10) NOT NULL,
	[gender] varchar(1),
	[birthday] datetime,
	[post_code] varchar(2),
	[rank_code] varchar(2),
	[empl_type] varchar(2),
	[empl_name_py] varchar(8),
	[empl_name_wb] varchar(8)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_employee_serve] (
	[empl_code] varchar(6) NOT NULL,
	[dept_code] varchar(4) NOT NULL,
	[ward_code] varchar(4)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_group] (
	[group_id] int IDENTITY,
	[group_name] varchar(30) NOT NULL,
	[group_code] varchar(15),
	[group_type] int NOT NULL,
	[create_user_id] int NOT NULL,
	[create_datetime] datetime NOT NULL,
	[modify_user_id] int,
	[modify_datetime] datetime,
	[description] varchar(50)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_module] (
	[module_id] int IDENTITY,
	[module_code] varchar(20) NOT NULL,
	[module_name] varchar(30) NOT NULL,
	[module_parent_id] int NOT NULL,
	[create_user_id] int NOT NULL,
	[create_datetime] datetime NOT NULL,
	[modify_user_id] int,
	[modify_datetime] datetime,
	[description] varchar(100)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_operate] (
	[operate_code] varchar(15) NOT NULL,
	[operate_name] varchar(30),
	[description] varchar(50)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_permission] (
	[permission_id] int IDENTITY,
	[permission_name] varchar(30) NOT NULL,
	[module_id] int NOT NULL,
	[operate_code] varchar(15),
	[create_user_id] int NOT NULL,
	[create_datetime] datetime NOT NULL,
	[modify_user_id] int,
	[modify_datetime] datetime
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_role] (
	[role_id] int IDENTITY,
	[role_code] varchar(20) NOT NULL,
	[role_name] varchar(30) NOT NULL,
	[create_user_id] int NOT NULL,
	[create_datetime] datetime NOT NULL,
	[modify_user_id] int,
	[modify_datetime] datetime,
	[description] varchar(50)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_role_group] (
	[role_id] int NOT NULL,
	[group_id] int NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_role_permission] (
	[role_id] int NOT NULL,
	[module_id] int NOT NULL,
	[operate_code] varchar(15) NOT NULL,
	[valid_time] int NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_user] (
	[user_id] int IDENTITY,
	[empl_code] varchar(15) NOT NULL,
	[login_name] varchar(10) NOT NULL,
	[password] varchar(16) NOT NULL,
	[name] varchar(20) NOT NULL,
	[gender] varchar(1) NOT NULL,
	[birthday] datetime,
	[phone] varchar(20),
	[email] varchar(30),
	[create_user_id] int NOT NULL,
	[create_datetime] datetime NOT NULL,
	[modify_user_id] int,
	[modify_datetime] datetime,
	[remark] varchar(50),
	[valid] int NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_user_finger] (
	[fp_id] int IDENTITY,
	[user_id] int NOT NULL,
	[fp_feature] varchar(2048) NOT NULL,
	[sec_key] varchar(50) NOT NULL,
	[create_user_id] int NOT NULL,
	[create_datetime] datetime NOT NULL,
	[modify_user_id] int,
	[modify_datetime] datetime,
	[deleted] int NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_user_group] (
	[user_id] int NOT NULL,
	[group_id] int NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_user_role] (
	[role_id] int NOT NULL,
	[user_id] int NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [sys_user_session] (
	[session_id] int IDENTITY,
	[user_id] int NOT NULL,
	[token] varchar(64) NOT NULL,
	[logon_datetime] datetime NOT NULL,
	[last_op_datetime] datetime NOT NULL,
	[client_ip] varchar(15) NOT NULL,
	[client_os] varchar(30) NOT NULL,
	[alive] int NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [TABLE_TEMPLATE] (
	[REFID] varchar(32) NOT NULL,
	[TABLE_NAME] nvarchar(60),
	[TABLE_CODE] nvarchar(30),
	[TABLE_TYPE] nvarchar(10),
	[DATA_SOURCE_PATH] nvarchar(60),
	[HEAD_DIRECTION] nvarchar(10),
	[EDIT_LAYOUT_TYPE] nvarchar(10),
	[MODIFY_SUMMARY_PROGRAM] nvarchar(100),
	[EDIT_SAVE_PROGRAME] nvarchar(100),
	[EDIT_URL] nvarchar(100),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [TABLE_TEMPLATE_ITEM] (
	[REFID] varchar(32) NOT NULL,
	[TABLE_REFID] varchar(32),
	[METADATA_REFID] varchar(32),
	[R_METADATA_NAME] nvarchar(60),
	[TITLE] nvarchar(60),
	[KEY_FLAG] char(1),
	[SHOW_FLAG] char(1),
	[COPY_FLAG] char(1),
	[ORD] int,
	[ALTERNATE_FLAG] char(1),
	[EMPTY_FLAG] char(1),
	[WIDTH] int,
	[HEIGHT] int,
	[READONLY_FLAG] char(1),
	[TIME_KEY_FLAG] char(1),
	[MIN_VALUE] numeric(10, 3),
	[MAX_VALUE] numeric(10, 3),
	[VERIFY_POLICY_CODE] nvarchar(100),
	[SEARCH_FLAG] char(1),
	[TEMPLATE_MENU_FLAG] char(1),
	[SAVE_FLAG] char(1),
	[EDIT_FLAG] char(1),
	[MUST_INPUT_FLAG] char(1),
	[NDA_FLAG] char(1),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [TABLE_TEMPLATE_OPERATE] (
	[REFID] varchar(32) NOT NULL,
	[TABLE_REFID] varchar(32),
	[OPERATE_REFID] varchar(32),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [TABLE_TEMPLATE_PATTERN] (
	[REFID] varchar(32) NOT NULL,
	[TABLE_REFID] varchar(32) NOT NULL,
	[PATTERN_NAME] nvarchar(60),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [TABLE_TEMPLATE_PATTERN_ITEM] (
	[REFID] varchar(32) NOT NULL,
	[TABLE_ITEM_REFID] varchar(32),
	[TABLE_PATTERN_REFID] varchar(32),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [task_nurse_attention] (
	[id] bigint IDENTITY,
	[nurse_id] int NOT NULL,
	[patient_id] int NOT NULL
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [task_white_record] (
	[id] bigint IDENTITY,
	[nurse_id] int NOT NULL,
	[nurse_name] varchar(20) NOT NULL,
	[receive_area] varchar(20) NOT NULL,
	[receive_id] varchar(50),
	[create_time] datetime NOT NULL,
	[msg_text] varchar(1000)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [task_white_record_item] (
	[id] bigint IDENTITY,
	[record_id] bigint NOT NULL,
	[send_type] varchar(3) NOT NULL,
	[data] image NOT NULL
) ON [PRIMARY]
 TEXTIMAGE_ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [TEMPLATE] (
	[REFID] varchar(32) NOT NULL,
	[TEMPLATE_NAME] nvarchar(60),
	[TEMPLATE_CODE] nvarchar(30),
	[TEMPLATE_TYPE] nvarchar(10),
	[TEMPLATE_HTML] text,
	[PAGER_CODE] nvarchar(10),
	[PAGER_WIDTH] int,
	[PAGER_HEIGHT] int,
	[VERTICAL_FLAG] char(1),
	[LOCAL_DATA_FLAG] char(1),
	[DATA_PRAGRAM] nvarchar(60),
	[INSTANT_URL] nvarchar(100),
	[NDA_URL] nvarchar(100),
	[SEARCH_URL] nvarchar(100),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
 TEXTIMAGE_ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [TEMPLATE_ITEM] (
	[REFID] varchar(32) NOT NULL,
	[TEMPLATE_REFID] varchar(32),
	[ITEM_FROM] nvarchar(10),
	[METADATA_REFID] varchar(32),
	[R_METADATA_NAME] nvarchar(60),
	[TABLE_REFID] varchar(32),
	[WIDTH] int,
	[HEIGHT] int,
	[COPY_FLAG] char(1),
	[SEARCH_FLAG] char(1),
	[TEMPLATE_MENU_FLAG] char(1),
	[NDA_FIELD_TYPE] nvarchar(10),
	[SAVE_FLAG] char(1),
	[EDIT_FLAG] char(1),
	[MUST_INPUT_FLAG] char(1),
	[AREA_NAME] nvarchar(60),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [TEMPLATE_OPERATE] (
	[REFID] varchar(32) NOT NULL,
	[TEMPLATE_REFID] varchar(32),
	[OPERATE_REFID] varchar(32),
	[ORD] int,
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [VERIFY_POLICY] (
	[REFID] varchar(32) NOT NULL,
	[VERIFY_NAME] nvarchar(60),
	[VERIFY_CODE] nvarchar(30),
	[PRAGRME] nvarchar(100),
	[REMARK] nvarchar(300),
	[CREATE_USER_REFID] varchar(32),
	[CREATE_DATE_TIME] datetime,
	[MODIFY_USER_REFID] varchar(32),
	[MODIFY_DATE_TIME] datetime,
	[VERSION] int,
	[ACTIVE] bit
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [vital_sign_info] (
	[vital_sign_info_id] int IDENTITY,
	[exec_nurse] varchar(20) NOT NULL,
	[exec_time] datetime NOT NULL,
	[patient_id] varchar(15) NOT NULL,
	[create_time] datetime NOT NULL CONSTRAINT [DF_vital_sign_info_create_time] DEFAULT (getdate()),
	[temperature] varchar(20),
	[temperature_value] varchar(10),
	[temperature_flag] smallint,
	[temperature_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_temperature_unit] DEFAULT ('锟斤拷'),
	[pluse] varchar(20),
	[pluse_value] varchar(10),
	[pluse_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_pluse_unit] DEFAULT ('锟斤拷/锟斤拷'),
	[hr] varchar(20),
	[hr_value] varchar(10),
	[hr_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_hr_unit] DEFAULT ('锟斤拷/锟斤拷'),
	[breath] varchar(20),
	[breath_value] varchar(20),
	[breath_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_breath_unit] DEFAULT ('锟斤拷/锟斤拷'),
	[bp] varchar(20),
	[bp_value] varchar(20),
	[bp_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_bp_unit] DEFAULT ('mmHg'),
	[in_take] varchar(20),
	[in_take_value] varchar(20),
	[in_take_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_in_take_unit] DEFAULT ('ml'),
	[urine] varchar(20),
	[urine_value] varchar(20),
	[urine_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_urine_unit] DEFAULT ('ml'),
	[other_out_put] varchar(20),
	[other_out_put_value] varchar(20),
	[other_out_put_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_other_out_put_unit] DEFAULT ('ml'),
	[total_out_put] varchar(20),
	[total_out_put_value] varchar(20),
	[total_out_put_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_total_out_put_unit] DEFAULT ('ml'),
	[shit] varchar(20),
	[shit_value] varchar(20),
	[shit_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_shit_unit] DEFAULT ('锟斤拷'),
	[height] varchar(20),
	[height_value] varchar(20),
	[height_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_height_unit] DEFAULT ('cm'),
	[weight] varchar(20),
	[weight_value] varchar(20),
	[weight_unit] varchar(10) NOT NULL CONSTRAINT [DF_vital_sign_info_weight_unit] DEFAULT ('kg'),
	[skin_test] varchar(20),
	[skin_test_value] varchar(30),
	[other_value] varchar(50),
	[event] varchar(50),
	[event_value] varchar(50),
	[in_patient_num] varchar(20),
	[remark_value] varchar(100)
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [ward_patrol] (
	[ward_patrol_id] int IDENTITY,
	[pat_id] int NOT NULL,
	[nurse_code] varchar(6) NOT NULL,
	[dept_code] varchar(4) NOT NULL,
	[tend_level] char(1) NOT NULL CONSTRAINT [DF_ward_patrol_tend_level] DEFAULT ((3)),
	[patrol_date] datetime,
	[next_patrol_date] datetime
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:		wenhuan.cui
-- Create date: 2013-3-8 14:50
-- Description:	count unexecuted orders for a patient

-- test:
-- exec uih_sp_count_unexec_orders '122'
-- =============================================
CREATE FUNCTION [dbo].[count_unexec_orders]
(
	@pat_id int -- patient id
)
RETURNS int
AS
BEGIN
	-- Declare the return variable here
	DECLARE @unexec_count int

	-- Add the T-SQL statements to compute the return value here
	SELECT @unexec_count = (SELECT COUNT(e.order_exec_id) AS unexec_order_count
		FROM order_exec e
		LEFT JOIN order_group g
		ON g.order_group_id = e.order_group_id
		LEFT JOIN pat_base pb
		ON pb.pat_id = g.pat_id
		WHERE pb.pat_id = @pat_id
		AND	e.pda_exec_datetime is NULL								-- PDA execute datetime
		AND e.exec_nurse_code IS NOT NULL							-- HIS nurse code
 		AND (g.stop_datetime IS NULL OR getdate()<g.stop_datetime)	 
		 
			
			)

	RETURN @unexec_count

END
GO

SET ANSI_NULLS ON
SET QUOTED_IDENTIFIER ON
GO

CREATE   PROCEDURE  [dbo].[uih_sp_bodysign_speedy]
	@dept_code int -- dept id
AS

BEGIN
    select pb.pat_name,ph.bed_code,ph.in_hosp_no,ph.tend_level,ph.admit_datetime,
    CASE 
       
        WHEN datediff(day,ph.admit_datetime,getDate())<=3   
             THEN datediff(day,ph.admit_datetime,getDate())
        ELSE -1     
    END,
    'days_aftersurgery' =
    CASE     
        WHEN datediff(day,ph.surgery_datetime,getDate())<=3 
             THEN datediff(day,ph.surgery_datetime,getDate())
        ELSE -1
    END,
    'last_msmenttime' =
    CASE
        WHEN 
			(select top 1 bm.rec_datetime from rec_body_sign_mas bm
			left join rec_body_sign_detail bd on(bm.body_sign_mas_id=bd.body_sign_mas_id)
			where bm.pat_id=ph.pat_id
			and bd.item_code='temperature' 
			and item_value>=(select temp_rule_value from temp_rule where temp_rule_code='highTemp')
			order by bm.rec_datetime desc) is not null
            THEN (select top 1 bm.rec_datetime from rec_body_sign_mas bm
			left join rec_body_sign_detail bd on(bm.body_sign_mas_id=bd.body_sign_mas_id)
			where bm.pat_id=ph.pat_id
			and bd.item_code='temperature' 
			and item_value>=(select temp_rule_value from temp_rule where temp_rule_code='highTemp')
			order by bm.rec_datetime desc)
    END
    from pat_hosp ph
	left join pat_base pb on(ph.pat_id=pb.pat_id)
	where ph.dept_code='1005'
	order by ph.tend_level asc
	
END
GO

ALTER TABLE [criticalvalue_info]
 ADD CONSTRAINT [PK_criticalvalue_info] PRIMARY KEY ([criticalvalue_info_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [DATA_SOURCE]
 ADD CONSTRAINT [PK_DATA_SOURCE] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [DEPARTMENT_TEMPALTE]
 ADD CONSTRAINT [PK_DEPARTMENT_TEMPALTE] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [dict_diag]
 ADD CONSTRAINT [PK__dict_diag__6D0D32F4] PRIMARY KEY ([seq_no] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [dict_drug]
 ADD CONSTRAINT [PK__dict_drug__00200768] PRIMARY KEY ([drug_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [dict_measure_rule]
 ADD CONSTRAINT [PK_dict_measure_rule] PRIMARY KEY ([rule_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [dict_undrug]
 ADD CONSTRAINT [PK__dict_undrug__0C85DE4D] PRIMARY KEY ([undrug_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [DOC_ITEM_OPTION]
 ADD CONSTRAINT [PK_DOC_ITEM_OPTION] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [DOC_TABLE_ITEM]
 ADD CONSTRAINT [PK_DOC_TABLE_ITEM] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [DOC_TABLE_ITEM_OPTION]
 ADD CONSTRAINT [PK_DOC_TABLE_ITEM_OPTION] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [DOC_TABLE_PATTERN]
 ADD CONSTRAINT [PK_DOC_TABLE_PATTERN] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [DOC_TABLE_PATTERN_ITEM]
 ADD CONSTRAINT [PK_DOC_TABLE_PATTERN_ITEM] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [FIELD_SWITCH]
 ADD CONSTRAINT [PK_FIELD_SWITCH] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [hosp_duty]
 ADD PRIMARY KEY ([id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [item_price]
 ADD PRIMARY KEY ([item_price_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [METADATA]
 ADD CONSTRAINT [PK_METADATA] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [METADATA_OPTION]
 ADD CONSTRAINT [PK_METADATA_OPTION] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [NURSE_DOC_DATA]
 ADD CONSTRAINT [PK_NURSE_DOC_DATA] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [NURSE_DOC_DATA_HIS]
 ADD CONSTRAINT [PK_NURSE_DOC_DATA_HIS] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [NURSE_DOC_ITEM]
 ADD CONSTRAINT [PK_NURSE_DOC_ITEM] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [NURSE_DOC_OPERATE]
 ADD CONSTRAINT [PK_NURSE_DOC_OPERATE] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [NURSE_DOCUMENT]
 ADD CONSTRAINT [PK_NURSE_DOCUMENT] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [OPERATE]
 ADD CONSTRAINT [PK_OPERATE] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [OPERATE_FIELD_SWITCH]
 ADD CONSTRAINT [PK_OPERATE_FIELD_SWITCH] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [order_exec]
 ADD CONSTRAINT [PK__order_exec__66603565] PRIMARY KEY ([order_exec_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [order_freq_plan_time]
 ADD CONSTRAINT [PK__order_freq_plan_time] PRIMARY KEY ([freq_code] ASC, [dept_code] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [order_group]
 ADD CONSTRAINT [PK__order_group__5629CD9C] PRIMARY KEY ([order_group_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [order_item]
 ADD CONSTRAINT [PK__order_item__5CD6CB2B] PRIMARY KEY ([order_item_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [PAGER]
 ADD CONSTRAINT [PK_PAGER] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [pat_base]
 ADD CONSTRAINT [PK__pat_base__014935CB] PRIMARY KEY ([pat_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [pat_hosp]
 ADD CONSTRAINT [PK__pat_hosp__1273C1CD] PRIMARY KEY ([in_hosp_no] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [phone_bindings]
 ADD CONSTRAINT [PK__PHONE_BI__2F15A557160F4887] PRIMARY KEY ([binding_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [poc_bindings]
 ADD CONSTRAINT [PK__poc_bind__4C2351367DB89C09] PRIMARY KEY ([bindind_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_allergy]
 ADD CONSTRAINT [PK__rec_allergy__71D1E811] PRIMARY KEY ([rec_allergy_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_body_sign_detail]
 ADD CONSTRAINT [PK_rec_body_sign_detail] PRIMARY KEY ([body_sign_detail_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_body_sign_mas]
 ADD CONSTRAINT [PK__rec_body__AFE269F277FFC2B3] PRIMARY KEY ([body_sign_mas_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_diag]
 ADD CONSTRAINT [PK__rec_diag__74AE54BC] PRIMARY KEY ([rec_diag_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_infu_monitor]
 ADD CONSTRAINT [PK__rec_infu_monitor__3C69FB99] PRIMARY KEY ([infu_monitor_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_inspection_detail]
 ADD CONSTRAINT [PK_rec_inspection_detail] PRIMARY KEY ([insp_detail_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_inspection_mas]
 ADD CONSTRAINT [PK_rec_inspection_mas] PRIMARY KEY ([insp_mas_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_lab_test_detail]
 ADD CONSTRAINT [PK__rec_lab_test_det__5441852A] PRIMARY KEY ([lab_test_detail_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_lab_test_mas]
 ADD CONSTRAINT [PK__rec_lab_test_mas__7E6CC920] PRIMARY KEY ([lab_test_mas_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_nurse_item_detail]
 ADD PRIMARY KEY ([nurse_item_detail_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_nurse_item_mas]
 ADD CONSTRAINT [PK__rec_nurse_item_m__5EBF139D] PRIMARY KEY ([nurse_item_mas_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_nurse_record_detail]
 ADD CONSTRAINT [PK__rec_nurs__6C3B816773852659] PRIMARY KEY ([nurse_record_detail_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_nurse_record_mas]
 ADD CONSTRAINT [PK__rec_nurse_record__628FA481] PRIMARY KEY ([nurse_record_mas_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_nurse_shift]
 ADD CONSTRAINT [PK__rec_nurse_shift__060DEAE8] PRIMARY KEY ([rec_nurse_shift_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_pat_event]
 ADD CONSTRAINT [PK__rec_pat___2E9A67A8742F31CF] PRIMARY KEY ([rec_pat_event_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [rec_skin_test]
 ADD CONSTRAINT [PK__rec_skin__0F0746136E765879] PRIMARY KEY ([rec_skin_test_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_dept]
 ADD CONSTRAINT [PK_SYS_DEPT] PRIMARY KEY ([dept_code] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_employee]
 ADD CONSTRAINT [PK_SYS_EMPLOYEE] PRIMARY KEY ([empl_code] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_group]
 ADD CONSTRAINT [PK_SYS_GROUP] PRIMARY KEY ([group_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_module]
 ADD CONSTRAINT [PK_SYS_MODULE] PRIMARY KEY ([module_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_operate]
 ADD CONSTRAINT [PK_SYS_OPERATE] PRIMARY KEY ([operate_code] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_permission]
 ADD CONSTRAINT [PK_PERMISSION] PRIMARY KEY ([permission_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_role]
 ADD CONSTRAINT [PK_SYS_ROLE] PRIMARY KEY ([role_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_role_group]
 ADD CONSTRAINT [PK_SYS_ROLE_GROUP] PRIMARY KEY ([role_id] ASC, [group_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_role_permission]
 ADD CONSTRAINT [PK_SYS_ROLE_PERMISSION] PRIMARY KEY ([role_id] ASC, [module_id] ASC, [operate_code] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_user]
 ADD CONSTRAINT [PK_SYS_USER] PRIMARY KEY ([user_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_user]
 ADD CONSTRAINT [uniq_login_name] UNIQUE ([login_name] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_user_finger]
 ADD CONSTRAINT [PK_SYS_USER_FINGER] PRIMARY KEY ([fp_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_user_group]
 ADD CONSTRAINT [PK_SYS_USER_GROUP] PRIMARY KEY ([user_id] ASC, [group_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_user_role]
 ADD CONSTRAINT [PK_SYS_USER_ROLE] PRIMARY KEY ([role_id] ASC, [user_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [sys_user_session]
 ADD CONSTRAINT [PK_SYS_USER_SESSION] PRIMARY KEY ([session_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [TABLE_TEMPLATE]
 ADD CONSTRAINT [PK_TABLE_TEMPLATE] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [TABLE_TEMPLATE_ITEM]
 ADD CONSTRAINT [PK_TABLE_TEMPLATE_ITEM] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [TABLE_TEMPLATE_OPERATE]
 ADD CONSTRAINT [PK_TABLE_TEMPLATE_OPERATE] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [TABLE_TEMPLATE_PATTERN]
 ADD CONSTRAINT [PK_TABLE_TEMPLATE_PATTERN] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [TABLE_TEMPLATE_PATTERN_ITEM]
 ADD CONSTRAINT [PK_TABLE_TEMPLATE_PATTERN_ITEM] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [task_nurse_attention]
 ADD CONSTRAINT [PK__task_nur__id] PRIMARY KEY ([id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [task_white_record]
 ADD CONSTRAINT [PK__task_white__id] PRIMARY KEY ([id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [task_white_record_item]
 ADD CONSTRAINT [PK__task_white_id] PRIMARY KEY ([id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [TEMPLATE]
 ADD CONSTRAINT [PK_TEMPLATE] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [TEMPLATE_ITEM]
 ADD CONSTRAINT [PK_TEMPLATE_ITEM] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [TEMPLATE_OPERATE]
 ADD CONSTRAINT [PK_TEMPLATE_OPERATE] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [VERIFY_POLICY]
 ADD CONSTRAINT [PK_VERIFY_POLICY] PRIMARY KEY ([REFID] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [ward_patrol]
 ADD CONSTRAINT [PK_ward_patrol] PRIMARY KEY ([ward_patrol_id] ASC) WITH (FILLFACTOR=100) ON [PRIMARY]
GO

ALTER TABLE [DEPARTMENT_TEMPALTE]
 ADD CONSTRAINT [FK_DEPARTME_REFERENCE_TEMPLATE] FOREIGN KEY ([TEMPLATE_REFID]) 
		REFERENCES [dbo].[TEMPLATE] ([REFID]) 
GO

ALTER TABLE [DOC_ITEM_OPTION]
 ADD CONSTRAINT [FK_DOC_ITEM_REFERENCE_METADATA] FOREIGN KEY ([METADATA_OPTION_REFID]) 
		REFERENCES [dbo].[METADATA_OPTION] ([REFID]) 
GO

ALTER TABLE [DOC_ITEM_OPTION]
 ADD CONSTRAINT [FK_DOC_ITEM_REFERENCE_NURSE_DO] FOREIGN KEY ([DOC_ITEM_REFID]) 
		REFERENCES [dbo].[NURSE_DOC_ITEM] ([REFID]) 
GO

ALTER TABLE [DOC_TABLE_ITEM]
 ADD CONSTRAINT [FK_DOC_TBL_REF_NRSE_DO] FOREIGN KEY ([DOC_ITEM_REFID]) 
		REFERENCES [dbo].[NURSE_DOC_ITEM] ([REFID]) 
GO

ALTER TABLE [DOC_TABLE_ITEM]
 ADD CONSTRAINT [FK_DOC_TBL_REF_TBL_TE] FOREIGN KEY ([TABLE_ITEM_REFID]) 
		REFERENCES [dbo].[TABLE_TEMPLATE_ITEM] ([REFID]) 
GO

ALTER TABLE [DOC_TABLE_ITEM_OPTION]
 ADD CONSTRAINT [FK_DOC_TABL_REF_DOC_TBL] FOREIGN KEY ([DOC_TABLE_ITEM_REFID]) 
		REFERENCES [dbo].[DOC_TABLE_ITEM] ([REFID]) 
GO

ALTER TABLE [DOC_TABLE_PATTERN]
 ADD CONSTRAINT [FK_DOC_TBL_REF_NURS_DO] FOREIGN KEY ([NURSE_DOC_REFID]) 
		REFERENCES [dbo].[NURSE_DOCUMENT] ([REFID]) 
GO

ALTER TABLE [DOC_TABLE_PATTERN]
 ADD CONSTRAINT [FK_DOC_TBL_REF_TBLE_TE] FOREIGN KEY ([TABLE_PATTERN_REFID]) 
		REFERENCES [dbo].[TABLE_TEMPLATE_PATTERN] ([REFID]) 
GO

ALTER TABLE [DOC_TABLE_PATTERN_ITEM]
 ADD CONSTRAINT [FK_DOC_TABL_REE_DOC_TABL] FOREIGN KEY ([DOC_TABLE_PATTERN_REFID]) 
		REFERENCES [dbo].[DOC_TABLE_PATTERN] ([REFID]) 
GO

ALTER TABLE [DOC_TABLE_PATTERN_ITEM]
 ADD CONSTRAINT [FK_DOC_TBL_REF_DOC_TABL] FOREIGN KEY ([DOC_TABLE_ITEM_REFID]) 
		REFERENCES [dbo].[DOC_TABLE_ITEM] ([REFID]) 
GO

ALTER TABLE [FIELD_SWITCH]
 ADD CONSTRAINT [FK_FIELD_SW_REFERENCE_OPERATE] FOREIGN KEY ([SWITCH_PROGRAME_REFID]) 
		REFERENCES [dbo].[OPERATE] ([REFID]) 
GO

ALTER TABLE [METADATA]
 ADD CONSTRAINT [FK_METADATA_REFERENCE_DATA_SOU] FOREIGN KEY ([DATA_SOURCE_REFID]) 
		REFERENCES [dbo].[DATA_SOURCE] ([REFID]) 
GO

ALTER TABLE [METADATA_OPTION]
 ADD CONSTRAINT [FK_METADATA_REFERENCE_METADATA] FOREIGN KEY ([METADATA_REFID]) 
		REFERENCES [dbo].[METADATA] ([REFID]) 
GO

ALTER TABLE [NURSE_DOC_DATA]
 ADD CONSTRAINT [FK_NRSE_DO_REF_NRS_DO] FOREIGN KEY ([NURSE_DOC_REFID]) 
		REFERENCES [dbo].[NURSE_DOCUMENT] ([REFID]) 
GO

ALTER TABLE [NURSE_DOC_DATA_HIS]
 ADD CONSTRAINT [FK_NRSE_DO_REF_NS_DO] FOREIGN KEY ([NURSE_DOC_DATA_REFID]) 
		REFERENCES [dbo].[NURSE_DOC_DATA] ([REFID]) 
GO

ALTER TABLE [NURSE_DOC_ITEM]
 ADD CONSTRAINT [FK_NRS_DO_REF_NRS_DO] FOREIGN KEY ([NURSE_DOC_REFID]) 
		REFERENCES [dbo].[NURSE_DOCUMENT] ([REFID]) 
GO

ALTER TABLE [NURSE_DOC_ITEM]
 ADD CONSTRAINT [FK_NRS_DO_REF_TEMPLATE] FOREIGN KEY ([TEMPLATE_ITEM_REFID]) 
		REFERENCES [dbo].[TEMPLATE_ITEM] ([REFID]) 
GO

ALTER TABLE [NURSE_DOC_OPERATE]
 ADD CONSTRAINT [FK_NRS_DO_REF_NURSE_DO] FOREIGN KEY ([NURSE_DOC_REFID]) 
		REFERENCES [dbo].[NURSE_DOCUMENT] ([REFID]) 
GO

ALTER TABLE [NURSE_DOC_OPERATE]
 ADD CONSTRAINT [FK_NURSE_DO_REFERENCE_OPERATE] FOREIGN KEY ([OPERATE_REFID]) 
		REFERENCES [dbo].[OPERATE] ([REFID]) 
GO

ALTER TABLE [NURSE_DOCUMENT]
 ADD CONSTRAINT [FK_NURSE_DO_REF_TMPL] FOREIGN KEY ([TEMPLATE_REFID]) 
		REFERENCES [dbo].[TEMPLATE] ([REFID]) 
GO

ALTER TABLE [OPERATE_FIELD_SWITCH]
 ADD CONSTRAINT [FK_OPERATE__REFERENCE_FIELD_SW] FOREIGN KEY ([FIELD_SWITCH_REFID]) 
		REFERENCES [dbo].[FIELD_SWITCH] ([REFID]) 
GO

ALTER TABLE [OPERATE_FIELD_SWITCH]
 ADD CONSTRAINT [FK_OPERATE__REFERENCE_OPERATE] FOREIGN KEY ([OPERATE_REFID]) 
		REFERENCES [dbo].[OPERATE] ([REFID]) 
GO

ALTER TABLE [TABLE_TEMPLATE_ITEM]
 ADD CONSTRAINT [FK_TABLE_TE_REFERENCE_METADATA] FOREIGN KEY ([METADATA_REFID]) 
		REFERENCES [dbo].[METADATA] ([REFID]) 
GO

ALTER TABLE [TABLE_TEMPLATE_ITEM]
 ADD CONSTRAINT [FK_TBL_TE_REF_TBL_TE] FOREIGN KEY ([TABLE_REFID]) 
		REFERENCES [dbo].[TABLE_TEMPLATE] ([REFID]) 
GO

ALTER TABLE [TABLE_TEMPLATE_OPERATE]
 ADD CONSTRAINT [FK_TABLE_TE_REFERENCE_OPERATE] FOREIGN KEY ([OPERATE_REFID]) 
		REFERENCES [dbo].[OPERATE] ([REFID]) 
GO

ALTER TABLE [TABLE_TEMPLATE_OPERATE]
 ADD CONSTRAINT [FK_TABLE_TE_REFERENCE_TABLE_TE] FOREIGN KEY ([TABLE_REFID]) 
		REFERENCES [dbo].[TABLE_TEMPLATE] ([REFID]) 
GO

ALTER TABLE [TABLE_TEMPLATE_PATTERN]
 ADD CONSTRAINT [FK_TBL_TE_REF_TABLE_TE] FOREIGN KEY ([TABLE_REFID]) 
		REFERENCES [dbo].[TABLE_TEMPLATE] ([REFID]) 
GO

ALTER TABLE [TABLE_TEMPLATE_PATTERN_ITEM]
 ADD CONSTRAINT [FK_TABLE_TE_REF_TBL_TE] FOREIGN KEY ([TABLE_ITEM_REFID]) 
		REFERENCES [dbo].[TABLE_TEMPLATE_ITEM] ([REFID]) 
GO

ALTER TABLE [TABLE_TEMPLATE_PATTERN_ITEM]
 ADD CONSTRAINT [FK_TBLE_TE_REF_TBL_TE] FOREIGN KEY ([TABLE_PATTERN_REFID]) 
		REFERENCES [dbo].[TABLE_TEMPLATE_PATTERN] ([REFID]) 
GO

ALTER TABLE [task_white_record_item]
 ADD CONSTRAINT [FK_task_white_record_item_task_to_parent] FOREIGN KEY ([record_id]) 
		REFERENCES [dbo].[task_white_record] ([id]) 
GO

ALTER TABLE [TEMPLATE_ITEM]
 ADD CONSTRAINT [FK_TEMPLATE_REFERENCE_METADATA] FOREIGN KEY ([METADATA_REFID]) 
		REFERENCES [dbo].[METADATA] ([REFID]) 
GO

ALTER TABLE [TEMPLATE_ITEM]
 ADD CONSTRAINT [FK_TEMPLATE_REFERENCE_TABLE_TE] FOREIGN KEY ([TABLE_REFID]) 
		REFERENCES [dbo].[TABLE_TEMPLATE] ([REFID]) 
GO

ALTER TABLE [TEMPLATE_ITEM]
 ADD CONSTRAINT [FK_TMPT_REF_TEMPLATE] FOREIGN KEY ([TEMPLATE_REFID]) 
		REFERENCES [dbo].[TEMPLATE] ([REFID]) 
GO

ALTER TABLE [TEMPLATE_OPERATE]
 ADD CONSTRAINT [FK_TEMPLATE_REF_TMPL] FOREIGN KEY ([TEMPLATE_REFID]) 
		REFERENCES [dbo].[TEMPLATE] ([REFID]) 
GO

ALTER TABLE [TEMPLATE_OPERATE]
 ADD CONSTRAINT [FK_TEMPLATE_REFERENCE_OPERATE] FOREIGN KEY ([OPERATE_REFID]) 
		REFERENCES [dbo].[OPERATE] ([REFID]) 
GO