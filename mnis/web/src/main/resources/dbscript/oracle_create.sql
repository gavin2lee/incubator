CREATE TABLE ass_nurse_item_dept
(
   nurse_item_code   VARCHAR (20) NOT NULL,
   dept_code         VARCHAR (10) NOT NULL
);

CREATE TABLE ass_nurse_record_dept
(
   item_code   VARCHAR (20) NOT NULL,
   dept_code   VARCHAR (10) NOT NULL
);

CREATE TABLE bed
(
   bed_code          VARCHAR (12) NOT NULL,
   dept_code         VARCHAR (10),
   ward_code         VARCHAR (10),
   bed_type_code     CHAR (1),
   bed_type_name     VARCHAR (10),
   bed_price         DECIMAL (10, 0),
   use_datetime      DATE,
   room_code         VARCHAR (10),
   ward_name         VARCHAR (10),
   dept_name         VARCHAR (10),
   bed_status        CHAR (1) DEFAULT ('U'),
   duty_nurse_id     VARCHAR (10),
   duty_nurse_name   VARCHAR (10),
   nurse_cell_code   VARCHAR (4)
);

CREATE TABLE criticalvalue_info
(
   criticalvalue_info_id   INT NOT NULL PRIMARY KEY,
   pat_id                  INT NOT NULL,
   recive_people           INT NOT NULL,
   bed_code                VARCHAR (20) NOT NULL,
   in_hosp_no              INT NOT NULL,
   send_people             VARCHAR (20) NOT NULL,
   send_time               DATE NOT NULL,
   criticalvalue           VARCHAR (30),
   disponer                VARCHAR (10),
   dispone_datetime        DATE
);

CREATE TABLE DATA_SOURCE
(
   REFID               VARCHAR (32) NOT NULL,
   DATA_SOURCE_TYPE    VARCHAR (10),
   SOURCE_NAME         VARCHAR (60),
   JSON_PATH           VARCHAR (60),
   PRAGRME             VARCHAR (100),
   REMARK              VARCHAR (300),
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT,
   CONSTRAINT PK_DATA_SOURCE PRIMARY KEY (REFID)
);

CREATE TABLE DEPARTMENT_TEMPLATE
(
   REFID               VARCHAR (32) NOT NULL,
   DEPT_REFID          VARCHAR (32),
   NODE_TYPE           VARCHAR (10),
   NODE_NAME           VARCHAR (60),
   NODE_PARENT_REFID   VARCHAR (32),
   ORD                 INT,
   TEMPLATE_TYPE       VARCHAR (10),
   NUMBER_TYPE         VARCHAR (10),
   TEMPLATE_REFID      VARCHAR (32),
   URL                 VARCHAR (100),
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT
);

CREATE TABLE dict_body_sign
(
   item_code   VARCHAR (20) NOT NULL,
   item_name   VARCHAR (20) NOT NULL,
   item_unit   VARCHAR (10) NOT NULL
);

CREATE TABLE dict_diag
(
   seq_no           VARCHAR (10) NOT NULL,
   icd_code         VARCHAR (50),
   py_code          VARCHAR (8),
   wb_code          VARCHAR (8),
   icd_name         VARCHAR (150),
   infectious       INT DEFAULT (0),
   cancerous        INT DEFAULT (0),
   applied_gender   VARCHAR (1) DEFAULT ('A'),
   is_valid         INT DEFAULT (1)
);

CREATE TABLE dict_drug
(
   drug_id            INT NOT NULL PRIMARY KEY,
   drug_code          VARCHAR (12) NOT NULL,
   drug_name          VARCHAR (80) NOT NULL,
   py_code            VARCHAR (18),
   wb_code            VARCHAR (18),
   drug_type          CHAR (1) DEFAULT ('P'),
   dose_base          DECIMAL (12, 2) DEFAULT (NULL),
   dose_unit          VARCHAR (4) DEFAULT (NULL),
   drug_spec          VARCHAR (32) DEFAULT (NULL),
   pack_unit          VARCHAR (4) DEFAULT (NULL),
   pack_qty           INT DEFAULT (NULL),
   min_use_unit       VARCHAR (10) DEFAULT (NULL),
   min_bill_unit      VARCHAR (16) DEFAULT (NULL),
   order_class_code   VARCHAR (3) DEFAULT (NULL),
   is_valid           INT DEFAULT (1),
   create_date        DATE DEFAULT (NULL)
);

CREATE TABLE dict_freq
(
   freq_code     VARCHAR (10) NOT NULL,
   freq_name     VARCHAR (30) NOT NULL,
   is_valid      INT DEFAULT (1),
   create_date   DATE NOT NULL
);

CREATE TABLE dict_lab_sample
(
   lab_sample_code   VARCHAR (20) NOT NULL,
   lab_sample_name   VARCHAR (30) NOT NULL,
   priority          INT DEFAULT (1),
   is_valid          INT DEFAULT (1),
   create_date       DATE NOT NULL
);

CREATE TABLE dict_measure_note
(
   measure_note_code   VARCHAR (10) NOT NULL,
   measure_note_name   VARCHAR (20) NOT NULL,
   in_nurse_shift      INT DEFAULT (0)
);

CREATE TABLE dict_measure_rule
(
   rule_id      INT NOT NULL PRIMARY KEY,
   rule_code    VARCHAR (10) NOT NULL,
   rule_name    VARCHAR (20) NOT NULL,
   rule_type    VARCHAR (10) NOT NULL,
   rule_value   VARCHAR (10) NOT NULL,
   rule_time    VARCHAR (100) NOT NULL
);

CREATE TABLE dict_nurse_item
(
   nurse_item_code        VARCHAR (10) NOT NULL,
   nurse_item_name        VARCHAR (20) NOT NULL,
   nurse_item_type_code   VARCHAR (1) DEFAULT ('B'),
   nurse_item_type_name   VARCHAR (20),
   is_valid               INT DEFAULT (1)
);

CREATE TABLE dict_nurse_record
(
   item_code     VARCHAR (20) NOT NULL,
   item_name     VARCHAR (30) NOT NULL,
   item_unit     VARCHAR (10),
   parent_code   VARCHAR (20),
   is_valid      INT DEFAULT (1),
   priority      INT
);

CREATE TABLE dict_nurse_record_value
(
   value_code   VARCHAR (20) NOT NULL,
   value_name   VARCHAR (30) NOT NULL,
   item_code    VARCHAR (20) NOT NULL,
   is_valid     INT DEFAULT (1),
   seq_no       INT DEFAULT (1)
);

CREATE TABLE dict_order_class
(
   order_class_code   VARCHAR (3) NOT NULL,
   order_class_name   VARCHAR (10) NOT NULL,
   is_valid           INT DEFAULT (1),
   create_date        DATE NOT NULL
);

CREATE TABLE dict_undrug
(
   undrug_id          INT NOT NULL PRIMARY KEY,
   undrug_code        VARCHAR (12) NOT NULL,
   undrug_name        VARCHAR (80) NOT NULL,
   py_code            VARCHAR (18),
   wb_code            VARCHAR (18),
   order_class_code   VARCHAR (3) NOT NULL,
   min_bill_unit      VARCHAR (10),
   is_valid           INT DEFAULT (1),
   create_date        DATE NOT NULL
);

CREATE TABLE dict_usage
(
   usage_code    VARCHAR (10) NOT NULL,
   usage_name    VARCHAR (30) NOT NULL,
   is_valid      INT DEFAULT (1),
   create_date   DATE NOT NULL
);

CREATE TABLE dict_ward
(
   ward_code      VARCHAR (4) NOT NULL,
   ward_name      VARCHAR (16) NOT NULL,
   ward_name_py   VARCHAR (8),
   is_in_use      INT DEFAULT (1),
   create_date    DATE NOT NULL
);

CREATE TABLE DOC_ITEM_OPTION
(
   REFID                   VARCHAR (32) NOT NULL,
   DOC_ITEM_REFID          VARCHAR (32),
   R_NURSE_DOC_REFID       VARCHAR (32) NOT NULL,
   METADATA_OPTION_REFID   VARCHAR (32),
   METADATA_REFID          VARCHAR (32),
   OPTION_NAME             VARCHAR (60),
   OPTION_CODE             VARCHAR (20),
   SCORE                   INT,
   OPTION_TYPE             VARCHAR (10),
   ORD                     INT,
   CREATE_USER_REFID       VARCHAR (32),
   CREATE_DATE_TIME        DATE,
   MODIFY_USER_REFID       VARCHAR (32),
   MODIFY_DATE_TIME        DATE,
   VERSION                 INT,
   ACTIVE                  INT
);

CREATE TABLE DOC_TABLE_ITEM
(
   REFID                  VARCHAR (32) NOT NULL,
   DOC_ITEM_REFID         VARCHAR (32) NOT NULL,
   R_NURSE_DOC_REFID      VARCHAR (32) NOT NULL,
   TABLE_ITEM_REFID       VARCHAR (32),
   TABLE_REFID            VARCHAR (32),
   METADATA_REFID         VARCHAR (32),
   TITLE                  VARCHAR (60),
   KEY_FLAG               CHAR (1),
   SHOW_FLAG              CHAR (1),
   COPY_FLAG              CHAR (1),
   ORD                    INT,
   ALTERNATE_FLAG         CHAR (1),
   EMPTY_FLAG             CHAR (1),
   WIDTH                  INT,
   HEIGHT                 INT,
   READONLY_FLAG          CHAR (1),
   TIME_KEY_FLAG          CHAR (1),
   MIN_VALUE              NUMERIC (10, 3),
   MAX_VALUE              NUMERIC (10, 3),
   NORMAL_MIN_VALUE       NUMERIC (10, 2),
   NORMAL_MAX_VALUE       NUMERIC (10, 2),
   VERIFY_POLICY_CODE     VARCHAR (100),
   SEARCH_FLAG            CHAR (1),
   TEMPLATE_MENU_FLAG     CHAR (1),
   SAVE_FLAG              CHAR (1),
   EDIT_FLAG              CHAR (1),
   MUST_INPUT_FLAG        CHAR (1),
   NDA_FLAG               CHAR (1),
   METADATA_NAME          VARCHAR (60),
   METADATA_SIMPLE_NAME   VARCHAR (60),
   METADATA_CODE          VARCHAR (20),
   DATA_SOURCE_REFID      VARCHAR (32),
   R_DATA_SOURCE_TYPE     VARCHAR (10),
   DATA_SOURCE_PATH       VARCHAR (60),
   INPUT_TYPE             VARCHAR (10),
   DATA_TYPE              VARCHAR (10),
   SCOPE_TYPE             VARCHAR (10),
   EVALUATE_TYPE          VARCHAR (10),
   EVALUATE_CODE          VARCHAR (30),
   EVALUATE_SCORE         INT,
   UNIT                   VARCHAR (30),
   PRECISIONS             INT,
   CREATE_USER_REFID      VARCHAR (32),
   CREATE_DATE_TIME       DATE,
   MODIFY_USER_REFID      VARCHAR (32),
   MODIFY_DATE_TIME       DATE,
   VERSION                INT,
   ACTIVE                 INT
);

CREATE TABLE DOC_TABLE_ITEM_OPTION
(
   REFID                   VARCHAR (32) NOT NULL,
   DOC_TABLE_ITEM_REFID    VARCHAR (32),
   R_NURSE_DOC_REFID       VARCHAR (32) NOT NULL,
   METADATA_OPTION_REFID   VARCHAR (32),
   METADATA_REFID          VARCHAR (32),
   OPTION_NAME             VARCHAR (60),
   OPTION_CODE             VARCHAR (20),
   SCORE                   INT,
   OPTION_TYPE             VARCHAR (10),
   ORD                     INT,
   CREATE_USER_REFID       VARCHAR (32),
   CREATE_DATE_TIME        DATE,
   MODIFY_USER_REFID       VARCHAR (32),
   MODIFY_DATE_TIME        DATE,
   VERSION                 INT,
   ACTIVE                  INT
);

CREATE TABLE DOC_TABLE_PATTERN
(
   REFID                 VARCHAR (32) NOT NULL,
   NURSE_DOC_REFID       VARCHAR (32) NOT NULL,
   TABLE_PATTERN_REFID   VARCHAR (32),
   TABLE_REFID           VARCHAR (32) NOT NULL,
   PATTERN_NAME          VARCHAR (60),
   ORD                   INT,
   CREATE_USER_REFID     VARCHAR (32),
   CREATE_DATE_TIME      DATE,
   MODIFY_USER_REFID     VARCHAR (32),
   MODIFY_DATE_TIME      DATE,
   VERSION               INT,
   ACTIVE                INT
);

CREATE TABLE DOC_TABLE_PATTERN_ITEM
(
   REFID                     VARCHAR (32) NOT NULL,
   DOC_TABLE_ITEM_REFID      VARCHAR (32) NOT NULL,
   R_NURSE_DOC_REFID         VARCHAR (32) NOT NULL,
   DOC_TABLE_PATTERN_REFID   VARCHAR (32),
   R_METADATA_NAME           VARCHAR (60),
   ORD                       INT,
   CREATE_USER_REFID         VARCHAR (32),
   CREATE_DATE_TIME          DATE,
   MODIFY_USER_REFID         VARCHAR (32),
   MODIFY_DATE_TIME          DATE,
   VERSION                   INT,
   ACTIVE                    INT
);

CREATE TABLE FIELD_SWITCH
(
   REFID                   VARCHAR (32) NOT NULL,
   FROM_FIELD_CODE         VARCHAR (20),
   FROM_FIELD_NAME         VARCHAR (60),
   TO_TYPE                 VARCHAR (20),
   TO_FIELD_CODE           VARCHAR (20),
   TO_FIELD_NAME           VARCHAR (60),
   SWITCH_PROGRAME_REFID   VARCHAR (32),
   REMARK                  VARCHAR (300),
   CREATE_USER_REFID       VARCHAR (32),
   CREATE_DATE_TIME        DATE,
   MODIFY_USER_REFID       VARCHAR (32),
   MODIFY_DATE_TIME        DATE,
   VERSION                 INT,
   ACTIVE                  INT
);

CREATE TABLE hosp_duty
(
   id          INT NOT NULL PRIMARY KEY,
   dept_id     INT NOT NULL,
   user_id     INT,
   user_name   VARCHAR (20),
   tel         VARCHAR (30) NOT NULL,
   type        VARCHAR (10) NOT NULL
);

CREATE TABLE item_price
(
   item_price_id   INT NOT NULL PRIMARY KEY,
   item_type       VARCHAR (1) DEFAULT (1),
   item_code       VARCHAR (12) NOT NULL,
   item_price      DECIMAL (12, 2) NOT NULL,
   begin_date      DATE,
   end_date        DATE
);

CREATE TABLE METADATA
(
   REFID                  VARCHAR (32) NOT NULL,
   METADATA_NAME          VARCHAR (60),
   METADATA_SIMPLE_NAME   VARCHAR (60),
   METADATA_NAME_PINYIN   VARCHAR (100),
   METADATA_CODE          VARCHAR (20),
   DATA_SOURCE_REFID      VARCHAR (32),
   R_DATA_SOURCE_TYPE     VARCHAR (10),
   DATA_SOURCE_PATH       VARCHAR (60),
   INPUT_TYPE             VARCHAR (10),
   DATA_TYPE              VARCHAR (10),
   SCOPE_TYPE             VARCHAR (10),
   READONLY_FLAG          CHAR (1),
   TIME_KEY_FLAG          CHAR (1),
   MIN_VALUE              NUMERIC (10, 2),
   MAX_VALUE              NUMERIC (10, 2),
   NORMAL_MIN_VALUE       NUMERIC (10, 2),
   NORMAL_MAX_VALUE       NUMERIC (10, 2),
   VERIFY_POLICY_CODE     VARCHAR (100),
   EVALUATE_TYPE          VARCHAR (10),
   EVALUATE_CODE          VARCHAR (30),
   EVALUATE_SCORE         INT,
   WIDTH                  INT,
   HEIGHT                 INT,
   UNIT                   VARCHAR (30),
   PRECISIONS             INT,
   REMARK                 VARCHAR (300),
   CREATE_USER_REFID      VARCHAR (32),
   CREATE_DATE_TIME       DATE,
   MODIFY_USER_REFID      VARCHAR (32),
   MODIFY_DATE_TIME       DATE,
   VERSION                INT,
   ACTIVE                 INT
);

CREATE TABLE METADATA_OPTION
(
   REFID               VARCHAR (32) NOT NULL,
   METADATA_REFID      VARCHAR (32),
   OPTION_NAME         VARCHAR (60),
   OPTION_CODE         VARCHAR (20),
   SCORE               INT,
   OPTION_TYPE         VARCHAR (10),
   ORD                 INT,
   REMARK              VARCHAR (300),
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT
);

CREATE TABLE NURSE_DOC_DATA
(
   REFID                 VARCHAR (32) NOT NULL,
   NURSE_DOC_REFID       VARCHAR (32),
   FROM_DOC_DATA_REFID   VARCHAR (32),
   HOSPITAL_NO           VARCHAR (10),
   PATIENT_REFID         VARCHAR (32),
   ITEM_CODE             VARCHAR (20),
   ITEM_VALUE            VARCHAR (100),
   ITEM_INPUT_VALUE      VARCHAR (100),
   ITEM_VALUE_FULL       VARCHAR (100),
   ITEM_KEY_CODE         VARCHAR (100),
   ITEM_KEY_VALUE        VARCHAR (300),
   NORMAL_MIN_VALUE      NUMERIC (10, 2),
   NORMAL_MAX_VALUE      NUMERIC (10, 2),
   DOC_METADATA_REFID    VARCHAR (32),
   JSON_PATH             VARCHAR (100),
   DELETE_FLAG           CHAR (1),
   ORD                   INT,
   CREATE_USER_REFID     VARCHAR (32),
   CREATE_DATE_TIME      DATE,
   MODIFY_USER_REFID     VARCHAR (32),
   MODIFY_DATE_TIME      DATE,
   VERSION               INT,
   ACTIVE                INT
);

CREATE TABLE NURSE_DOC_DATA_HIS
(
   REFID                  VARCHAR (32) NOT NULL,
   NURSE_DOC_DATA_REFID   VARCHAR (32),
   NURSE_DOC_REFID        VARCHAR (32),
   FROM_DOC_DATA_REFID    VARCHAR (32),
   HOSPITAL_NO            VARCHAR (10),
   PATIENT_REFID          VARCHAR (32),
   ITEM_CODE              VARCHAR (20),
   ITEM_VALUE             VARCHAR (100),
   ITEM_INPUT_VALUE       VARCHAR (100),
   NEW_ITEM_VALUE         VARCHAR (100),
   NEW_ITEM_INPUT_VALUE   VARCHAR (100),
   CHANGE_NURSER_REFID    VARCHAR (32),
   R_CHANGE_NURSER_NAME   VARCHAR (60),
   CHANGE_TIME            DATE,
   ITEM_VALUE_FULL        VARCHAR (100),
   ITEM_KEY_CODE          VARCHAR (100),
   ITEM_KEY_VALUE         VARCHAR (300),
   NORMAL_MIN_VALUE       NUMERIC (10, 2),
   NORMAL_MAX_VALUE       NUMERIC (10, 2),
   DOC_METADATA_REFID     VARCHAR (32),
   JSON_PATH              VARCHAR (100),
   DELETE_FLAG            CHAR (1),
   ORD                    INT,
   CREATE_USER_REFID      VARCHAR (32),
   CREATE_DATE_TIME       DATE,
   MODIFY_USER_REFID      VARCHAR (32),
   MODIFY_DATE_TIME       DATE,
   VERSION                INT,
   ACTIVE                 INT
);

CREATE TABLE NURSE_DOC_ITEM
(
   REFID                    VARCHAR (32) NOT NULL,
   NURSE_DOC_REFID          VARCHAR (32) NOT NULL,
   TEMPLATE_ITEM_REFID      VARCHAR (32) NOT NULL,
   TEMPLATE_REFID           VARCHAR (32),
   ITEM_FROM                VARCHAR (10),
   METADATA_REFID           VARCHAR (32),
   TABLE_REFID              VARCHAR (32),
   WIDTH                    INT,
   HEIGHT                   INT,
   COPY_FLAG                CHAR (1),
   SEARCH_FLAG              CHAR (1),
   TEMPLATE_MENU_FLAG       CHAR (1),
   NDA_FIELD_TYPE           VARCHAR (10),
   SAVE_FLAG                CHAR (1),
   EDIT_FLAG                CHAR (1),
   MUST_INPUT_FLAG          CHAR (1),
   AREA_NAME                VARCHAR (60),
   ORD                      INT,
   TABLE_NAME               VARCHAR (60),
   TABLE_CODE               VARCHAR (30),
   TABLE_TYPE               VARCHAR (10),
   HEAD_DIRECTION           VARCHAR (10),
   EDIT_LAYOUT_TYPE         VARCHAR (10),
   MODIFY_SUMMARY_PROGRAM   VARCHAR (100),
   EDIT_SAVE_PROGRAME       VARCHAR (100),
   EDIT_URL                 VARCHAR (100),
   METADATA_NAME            VARCHAR (60),
   METADATA_SIMPLE_NAME     VARCHAR (60),
   METADATA_CODE            VARCHAR (20),
   DATA_SOURCE_REFID        VARCHAR (32),
   R_DATA_SOURCE_TYPE       VARCHAR (10),
   DATA_SOURCE_PATH         VARCHAR (60),
   INPUT_TYPE               VARCHAR (10),
   DATA_TYPE                VARCHAR (10),
   SCOPE_TYPE               VARCHAR (10),
   READONLY_FLAG            CHAR (1),
   TIME_KEY_FLAG            CHAR (1),
   MIN_VALUE                NUMERIC (10, 2),
   MAX_VALUE                NUMERIC (10, 2),
   NORMAL_MIN_VALUE         NUMERIC (10, 2),
   NORMAL_MAX_VALUE         NUMERIC (10, 2),
   VERIFY_POLICY_CODE       VARCHAR (100),
   EVALUATE_TYPE            VARCHAR (10),
   EVALUATE_CODE            VARCHAR (30),
   EVALUATE_SCORE           INT,
   UNIT                     VARCHAR (30),
   PRECISIONS               INT,
   CREATE_USER_REFID        VARCHAR (32),
   CREATE_DATE_TIME         DATE,
   MODIFY_USER_REFID        VARCHAR (32),
   MODIFY_DATE_TIME         DATE,
   VERSION                  INT,
   ACTIVE                   INT
);

CREATE TABLE NURSE_DOC_OPERATE
(
   REFID               VARCHAR (32) NOT NULL,
   NURSE_DOC_REFID     VARCHAR (32),
   TEMPLATE_REFID      VARCHAR (32),
   OPERATE_REFID       VARCHAR (32),
   ORD                 INT,
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT
);

CREATE TABLE NURSE_DOCUMENT
(
   REFID                        VARCHAR (32) NOT NULL,
   HOSPITAL_NO_TEMPLATE_REFID   VARCHAR (32),
   TEMPLATE_REFID               VARCHAR (32),
   R_TEMPLATE_NAME              VARCHAR (60),
   R_TEMPLATE_TYPE              VARCHAR (10),
   PATIENT_REFID                VARCHAR (32),
   R_PATIENT_NAME               VARCHAR (60),
   R_PATIENT_BARCODE            VARCHAR (10),
   HOSPITAL_NO                  VARCHAR (10),
   DEPT_REFID                   VARCHAR (32),
   BABY_REFID                   VARCHAR (32),
   R_BABY_NAME                  VARCHAR (60),
   DOC_NO                       VARCHAR (10),
   DOC_TYPE                     VARCHAR (10),
   NUMBER_TYPE                  VARCHAR (10),
   PAGER_CODE                   VARCHAR (10),
   VERTICAL_FLAG                CHAR (1),
   LOCAL_DATA_FLAG              CHAR (1),
   DATA_PRAGRAM                 VARCHAR (60),
   BEGIN_TIME                   DATE,
   END_TIME                     DATE,
   END_FLAG                     CHAR (1),
   STATUS                       VARCHAR (10),
   WRITE_NURSER                 VARCHAR (32),
   R_WRITE_NURSER_NAME          VARCHAR (60),
   WRITE_TIME                   DATE,
   APPROVE_NURSER               VARCHAR (32),
   R_APPROVE_NURSER_NAME        VARCHAR (60),
   APPROVE_TIME                 DATE,
   PRINT_COUNT                  INT,
   PRINT_NURSER                 VARCHAR (32),
   R_PRINT_NURSER_NAME          VARCHAR (60),
   PRINT_TIME                   DATE,
   TABLE_PATTERN                VARCHAR (32),
   TEMPLATE_HTML                CLOB,
   INSTANT_URL                  VARCHAR (100),
   NDA_URL                      VARCHAR (100),
   SEARCH_URL                   VARCHAR (100),
   ORD                          INT,
   CREATE_USER_REFID            VARCHAR (32),
   CREATE_DATE_TIME             DATE,
   MODIFY_USER_REFID            VARCHAR (32),
   MODIFY_DATE_TIME             DATE,
   VERSION                      INT,
   ACTIVE                       INT
);

CREATE TABLE OPERATE
(
   REFID               VARCHAR (32) NOT NULL,
   OPERATE_TYPE        VARCHAR (10),
   OPERATE_NAME        VARCHAR (60),
   OPERATE_CODE        VARCHAR (30),
   PRAGRME             VARCHAR (100),
   REMARK              VARCHAR (300),
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT
);

CREATE TABLE OPERATE_FIELD_SWITCH
(
   REFID                VARCHAR (32) NOT NULL,
   OPERATE_REFID        VARCHAR (32),
   FIELD_SWITCH_REFID   VARCHAR (32),
   CREATE_USER_REFID    VARCHAR (32),
   CREATE_DATE_TIME     DATE,
   MODIFY_USER_REFID    VARCHAR (32),
   MODIFY_DATE_TIME     DATE,
   VERSION              INT,
   ACTIVE               INT
);

CREATE TABLE order_exec
(
   order_exec_id         INT NOT NULL PRIMARY KEY,
   order_group_id        INT NOT NULL,
   plan_datetime         DATE NOT NULL,
   exec_datetime         DATE,
   exec_nurse_code       VARCHAR (6),
   exec_nurse_name       VARCHAR (10),
   finish_datetime       DATE,
   finish_nurse_code     VARCHAR (6),
   finish_nurse_name     VARCHAR (10),
   order_exec_barcode    VARCHAR (20),
   deliver_speed         INT,
   pda_exec_datetime     DATE,
   pda_exec_nurse_code   VARCHAR (6),
   pda_exec_nurse_name   VARCHAR (10),
   deliver_speed_unit    VARCHAR (10),
   approve_nurse_code    VARCHAR (6),
   approve_nurse_name    VARCHAR (10)
);

CREATE TABLE order_freq_plan_time
(
   freq_code     VARCHAR (10) NOT NULL,
   dept_code     VARCHAR (10) NOT NULL,
   dept_name     VARCHAR (16),
   plan_time     VARCHAR (80),
   create_time   DATE
);

CREATE TABLE order_group
(
   order_group_id         INT NOT NULL PRIMARY KEY,
   pat_id                 INT NOT NULL,
   pat_name               VARCHAR (10) NOT NULL,
   pat_bed_code           VARCHAR (10) NOT NULL,
   freq_code              VARCHAR (10),
   freq_name              VARCHAR (30),
   order_type_code        VARCHAR (10),
   order_type_name        VARCHAR (16),
   order_exec_type_code   VARCHAR (10),
   order_exec_type_name   VARCHAR (16),
   create_datetime        DATE,
   create_doc_id          VARCHAR (10),
   create_doc_name        VARCHAR (10),
   stop_datetime          DATE,
   stop_doc_id            VARCHAR (10),
   stop_doc_name          VARCHAR (10),
   order_status_code      CHAR (1) DEFAULT (0),
   order_status_name      VARCHAR (16),
   usage_code             VARCHAR (10),
   usage_name             VARCHAR (16),
   emergent               INT DEFAULT (0),
   his_comb_no            VARCHAR (14),
   confirm_datetime       DATE,
   confirm_nurse_code     VARCHAR (10),
   confirm_nurse_name     VARCHAR (10),
   is_skin_test           INT,
   plan_exec_time         VARCHAR (80),
   plan_first_exec_time   VARCHAR (5)
);

CREATE TABLE order_item
(
   order_item_id        INT NOT NULL PRIMARY KEY,
   order_group_id       INT NOT NULL,
   skt_order_group_id   INT,
   item_code            VARCHAR (12) NOT NULL,
   item_name            VARCHAR (80) NOT NULL,
   dosage               DECIMAL (10, 2),
   dosage_unit          VARCHAR (16),
   skin_test_required   INT DEFAULT (0),
   drug_specs           VARCHAR (32),
   specimen_code        VARCHAR (20),
   specimen_name        VARCHAR (30),
   exam_loc_code        VARCHAR (20),
   exam_loc_name        VARCHAR (30),
   remark               VARCHAR (30)
);

CREATE TABLE PAGER
(
   REFID               VARCHAR (32) NOT NULL,
   PAGER_NAME          VARCHAR (60),
   PAGER_CODE          VARCHAR (10),
   WIDTH               INT,
   HEIGHT              INT,
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT
);

CREATE TABLE pat_base
(
   pat_id              INT NOT NULL PRIMARY KEY,
   pat_name            VARCHAR (20) NOT NULL,
   pat_name_py         VARCHAR (5),
   gender              CHAR (1) DEFAULT ('U'),
   birthday            DATE,
   is_married          INT DEFAULT (0),
   education_level     CHAR (1) DEFAULT (0),
   financial_level     CHAR (1) DEFAULT (0),
   insurance_no        VARCHAR (15),
   medical_record_no   VARCHAR (15),
   height              INT,
   weight              INT,
   id_card_no          VARCHAR (20),
   career_code         VARCHAR (4),
   birth_place_code    VARCHAR (2),
   phone_no            VARCHAR (16)
);

CREATE TABLE pat_hosp
(
   in_hosp_no           INT NOT NULL PRIMARY KEY,
   pat_id               INT NOT NULL,
   bed_code             VARCHAR (10),
   in_state             CHAR (1) DEFAULT ('R'),
   tend_level           CHAR (1) DEFAULT (3),
   illness_status       CHAR (1) DEFAULT (0),
   charge_type          CHAR (1) DEFAULT (0),
   admit_datetime       DATE,
   admit_diagnosis      VARCHAR (20),
   surgery_datetime     DATE,
   pat_barcode          VARCHAR (20),
   out_datetime         DATE,
   diet_code            VARCHAR (20),
   duty_doc_code        VARCHAR (10),
   duty_doc_name        VARCHAR (20),
   duty_nurse_code      VARCHAR (10),
   duty_nurse_name      VARCHAR (20),
   fee_paid             DECIMAL (10, 0),
   fee_used             DECIMAL (10, 0),
   trans_in_datetime    DATE,
   trans_out_datetime   DATE,
   receive_datetime     DATE,
   receive_nurse_code   VARCHAR (10),
   out_card_no          VARCHAR (16),
   his_pat_no           VARCHAR (16),
   ward_code            VARCHAR (10),
   ward_name            VARCHAR (8),
   dept_code            VARCHAR (10),
   dept_name            VARCHAR (16),
   diet_name            VARCHAR (80)
);

CREATE TABLE phone_bindings
(
   binding_id        INT NOT NULL PRIMARY KEY,
   aor               VARCHAR (50),
   contact           VARCHAR (150),
   callid            VARCHAR (80),
   cseq              INT,
   expiration_time   INT
);

CREATE TABLE poc_bindings
(
   bindind_id       INT NOT NULL PRIMARY KEY,
   aor              VARCHAR (50),
   contact          VARCHAR (150),
   callid           VARCHAR (80),
   cseq             INT,
   expirationtime   INT
);

CREATE TABLE rec_allergy
(
   rec_allergy_id     INT NOT NULL PRIMARY KEY,
   allergen_code      VARCHAR (12) NOT NULL,
   pat_id             INT NOT NULL,
   allergy_degree     VARCHAR (1) DEFAULT (0),
   allergen_name      VARCHAR (80),
   is_valid           INT DEFAULT (1),
   rec_date           DATE NOT NULL,
   rec_empl_code      VARCHAR (6) NOT NULL,
   priority           INT,
   modify_date        DATE,
   modify_empl_code   VARCHAR (6)
);

CREATE TABLE rec_body_sign_detail
(
   body_sign_detail_id   INT NOT NULL PRIMARY KEY,
   body_sign_mas_id      INT NOT NULL,
   item_code             VARCHAR (20),
   item_value            VARCHAR (10),
   measure_note_code     VARCHAR (10),
   measure_note_name     VARCHAR (20),
   abnormal_flag         INT DEFAULT (0)
);

CREATE TABLE rec_body_sign_mas
(
   body_sign_mas_id   INT NOT NULL PRIMARY KEY,
   pat_id             INT NOT NULL,
   cooled             INT DEFAULT (0),
   rec_datetime       DATE NOT NULL,
   rec_nurse_code     VARCHAR (6) NOT NULL,
   remark             VARCHAR (50)
);

CREATE TABLE rec_diag
(
   rec_diag_id        INT NOT NULL PRIMARY KEY,
   pat_id             INT NOT NULL,
   icd_code           VARCHAR (50) NOT NULL,
   diag_name          VARCHAR (150) NOT NULL,
   is_main            INT DEFAULT (1),
   rec_date           DATE NOT NULL,
   is_valid           INT DEFAULT (1),
   rec_empl_code      VARCHAR (6) NOT NULL,
   diag_kind          VARCHAR (2),
   priority           INT,
   modify_date        DATE,
   modify_empl_code   VARCHAR (6)
);

CREATE TABLE rec_infu_monitor
(
   infu_monitor_id      INT NOT NULL PRIMARY KEY,
   pat_id               INT NOT NULL,
   rec_datetime         DATE NOT NULL,
   rec_nurse_code       VARCHAR (6) NOT NULL,
   order_exec_id        INT NOT NULL,
   pat_name             VARCHAR (10),
   rec_nurse_name       VARCHAR (10),
   abnormal             INT DEFAULT (0),
   deliver_speed        INT,
   anomaly_msg          VARCHAR (20),
   anomaly_disposal     VARCHAR (20),
   deliver_speed_unit   VARCHAR (10)
);

CREATE TABLE rec_inspection_detail
(
   insp_detail_id    INT NOT NULL PRIMARY KEY,
   insp_mas_id       INT NOT NULL,
   body_parts        VARCHAR (20) NOT NULL,
   insp_result       VARCHAR (100) NOT NULL,
   insp_suggestion   VARCHAR (100)
);

CREATE TABLE rec_inspection_mas
(
   insp_mas_id       INT NOT NULL PRIMARY KEY,
   insp_subject      VARCHAR (30),
   pat_id            INT NOT NULL,
   applicant         VARCHAR (10),
   reporter          VARCHAR (10),
   auditor           VARCHAR (10),
   insp_datetime     DATE NOT NULL,
   report_datetime   DATE NOT NULL,
   status            VARCHAR (10),
   pri_flag          INT
);

CREATE TABLE rec_lab_test_detail
(
   lab_test_detail_id   INT NOT NULL PRIMARY KEY,
   lab_test_mas_id      INT NOT NULL,
   item_code            VARCHAR (20),
   item_name            VARCHAR (40),
   result_value         VARCHAR (20),
   result_unit          VARCHAR (20),
   ref_ranges           VARCHAR (20),
   normal_flag          VARCHAR (1) DEFAULT ('N'),
   inst_code            VARCHAR (16),
   inst_name            VARCHAR (16)
);

CREATE TABLE rec_lab_test_mas
(
   lab_test_mas_id   INT NOT NULL PRIMARY KEY,
   pat_id            INT NOT NULL,
   pat_hosp_no       VARCHAR (10) NOT NULL,
   order_group_id    INT NOT NULL,
   order_exec_id     INT,
   lab_test_status   VARCHAR (1) DEFAULT ('R'),
   exec_datetime     DATE NOT NULL,
   exec_dept_code    VARCHAR (4) NOT NULL,
   test_subject      VARCHAR (20),
   test_specimen     VARCHAR (20),
   reporter_code     VARCHAR (10),
   confirmer_code    VARCHAR (10),
   record_flag       INT,
   pri_flag          INT
);

CREATE TABLE rec_nurse_item_detail
(
   nurse_item_detail_id   INT NOT NULL PRIMARY KEY,
   nurse_item_mas_id      INT NOT NULL,
   nurse_item_code        VARCHAR (10)
);

CREATE TABLE rec_nurse_item_mas
(
   nurse_item_mas_id   INT NOT NULL PRIMARY KEY,
   pat_id              INT NOT NULL,
   rec_datetime        DATE NOT NULL,
   rec_nurse_code      VARCHAR (6) NOT NULL,
   is_valid            INT DEFAULT (1),
   remark              VARCHAR (50) DEFAULT (NULL)
);

CREATE TABLE rec_nurse_record_detail
(
   nurse_record_detail_id   INT NOT NULL PRIMARY KEY,
   nurse_record_mas_id      INT NOT NULL,
   item_code                VARCHAR (20),
   item_value_code          VARCHAR (20),
   item_value               VARCHAR (20),
   measure_note_code        VARCHAR (10),
   measure_note_name        VARCHAR (400)
);

CREATE TABLE rec_nurse_record_mas
(
   nurse_record_mas_id   INT NOT NULL PRIMARY KEY,
   pat_id                INT NOT NULL,
   rec_datetime          DATE NOT NULL,
   rec_nurse_code        VARCHAR (6) NOT NULL,
   is_valid              INT DEFAULT (1),
   cooled                INT DEFAULT (0),
   record_type_code      VARCHAR (2),
   remark                VARCHAR (50) DEFAULT (NULL)
);

CREATE TABLE rec_nurse_shift
(
   rec_nurse_shift_id   INT NOT NULL PRIMARY KEY,
   pat_id               INT NOT NULL,
   rec_dept_code        VARCHAR (4) NOT NULL,
   rec_nurse_code       VARCHAR (6) NOT NULL,
   is_valid             INT DEFAULT (1),
   rec_datetime         DATE NOT NULL,
   event_info           VARCHAR (500)
);

CREATE TABLE rec_pat_event
(
   rec_pat_event_id     INT NOT NULL PRIMARY KEY,
   pat_id               INT NOT NULL,
   pat_name             VARCHAR (10) NOT NULL,
   body_sign_mas_id     INT NOT NULL,
   event_datetime       DATE NOT NULL,
   rec_nurse_code       VARCHAR (6) NOT NULL,
   rec_nurse_name       VARCHAR (10),
   confirm_nurse_code   VARCHAR (6),
   confirm_nurse_name   VARCHAR (10),
   problem              VARCHAR (30),
   interv               VARCHAR (30),
   outcome              VARCHAR (30),
   event_code           VARCHAR (10),
   event_name           VARCHAR (20)
);

CREATE TABLE rec_skin_test
(
   rec_skin_test_id     INT NOT NULL PRIMARY KEY,
   pat_id               INT NOT NULL,
   pat_name             VARCHAR (20) NOT NULL,
   test_nurse_code      VARCHAR (6) NOT NULL,
   test_result          VARCHAR (1) DEFAULT ('N'),
   test_datetime        DATE NOT NULL,
   drug_code            VARCHAR (12) NOT NULL,
   drug_name            VARCHAR (80) NOT NULL,
   order_group_id       INT,
   order_exec_id        INT,
   body_sign_mas_id     INT,
   drug_batch_no        VARCHAR (30),
   test_nurse_name      VARCHAR (10),
   approve_nurse_code   VARCHAR (6),
   approve_nurse_name   VARCHAR (10)
);

CREATE TABLE sys_dept
(
   dept_code      VARCHAR (4) NOT NULL,
   dept_name      VARCHAR (16) NOT NULL,
   dept_name_py   VARCHAR (8),
   is_in_use      INT,
   create_date    DATE
);

CREATE TABLE sys_employee
(
   empl_code      VARCHAR (6) NOT NULL,
   empl_name      VARCHAR (10) NOT NULL,
   gender         VARCHAR (1),
   birthday       DATE,
   post_code      VARCHAR (2),
   rank_code      VARCHAR (2),
   empl_type      VARCHAR (2),
   empl_name_py   VARCHAR (8),
   empl_name_wb   VARCHAR (8)
);

CREATE TABLE sys_employee_serve
(
   empl_code   VARCHAR (6) NOT NULL,
   dept_code   VARCHAR (4) NOT NULL,
   ward_code   VARCHAR (4)
);

CREATE TABLE sys_group
(
   group_id          INT NOT NULL PRIMARY KEY,
   group_name        VARCHAR (30) NOT NULL,
   group_code        VARCHAR (15),
   group_type        INT NOT NULL,
   create_user_id    INT NOT NULL,
   create_datetime   DATE NOT NULL,
   modify_user_id    INT,
   modify_datetime   DATE,
   description       VARCHAR (50)
);

CREATE TABLE sys_module
(
   module_id          INT NOT NULL PRIMARY KEY,
   module_code        VARCHAR (20) NOT NULL,
   module_name        VARCHAR (30) NOT NULL,
   module_parent_id   INT NOT NULL,
   create_user_id     INT NOT NULL,
   create_datetime    DATE NOT NULL,
   modify_user_id     INT,
   modify_datetime    DATE,
   description        VARCHAR (100)
);

CREATE TABLE sys_operate
(
   operate_code   VARCHAR (15) NOT NULL,
   operate_name   VARCHAR (30),
   description    VARCHAR (50)
);

CREATE TABLE sys_permission
(
   permission_id     INT NOT NULL PRIMARY KEY,
   permission_name   VARCHAR (30) NOT NULL,
   module_id         INT NOT NULL,
   operate_code      VARCHAR (15),
   create_user_id    INT NOT NULL,
   create_datetime   DATE NOT NULL,
   modify_user_id    INT,
   modify_datetime   DATE
);

CREATE TABLE sys_role
(
   role_id           INT NOT NULL PRIMARY KEY,
   role_code         VARCHAR (20) NOT NULL,
   role_name         VARCHAR (30) NOT NULL,
   create_user_id    INT NOT NULL,
   create_datetime   DATE NOT NULL,
   modify_user_id    INT,
   modify_datetime   DATE,
   description       VARCHAR (50)
);

CREATE TABLE sys_role_group
(
   role_id    INT NOT NULL,
   group_id   INT NOT NULL
);

CREATE TABLE sys_role_permission
(
   role_id        INT NOT NULL,
   module_id      INT NOT NULL,
   operate_code   VARCHAR (15) NOT NULL,
   valid_time     INT NOT NULL
);

CREATE TABLE sys_user
(
   user_id           INT NOT NULL PRIMARY KEY,
   empl_code         VARCHAR (15) NOT NULL,
   login_name        VARCHAR (10) NOT NULL,
   password          VARCHAR (16) NOT NULL,
   name              VARCHAR (20) NOT NULL,
   gender            VARCHAR (1) NOT NULL,
   birthday          DATE,
   phone             VARCHAR (20),
   email             VARCHAR (30),
   create_user_id    INT NOT NULL,
   create_datetime   DATE NOT NULL,
   modify_user_id    INT,
   modify_datetime   DATE,
   remark            VARCHAR (50),
   valid             INT NOT NULL
);

CREATE TABLE sys_user_finger
(
   fp_id             INT NOT NULL PRIMARY KEY,
   user_id           INT NOT NULL,
   fp_feature        VARCHAR (2048) NOT NULL,
   sec_key           VARCHAR (50) NOT NULL,
   create_user_id    INT NOT NULL,
   create_datetime   DATE NOT NULL,
   modify_user_id    INT,
   modify_datetime   DATE,
   deleted           INT NOT NULL
);

CREATE TABLE sys_user_group
(
   user_id    INT NOT NULL,
   group_id   INT NOT NULL
);

CREATE TABLE sys_user_role
(
   role_id   INT NOT NULL,
   user_id   INT NOT NULL
);

CREATE TABLE sys_user_session
(
   session_id         INT NOT NULL PRIMARY KEY,
   user_id            INT NOT NULL,
   token              VARCHAR (64) NOT NULL,
   logon_datetime     DATE NOT NULL,
   last_op_datetime   DATE NOT NULL,
   client_ip          VARCHAR (15) NOT NULL,
   client_os          VARCHAR (30) NOT NULL,
   alive              INT NOT NULL
);

CREATE TABLE TABLE_TEMPLATE
(
   REFID                    VARCHAR (32) NOT NULL,
   TABLE_NAME               VARCHAR (60),
   TABLE_CODE               VARCHAR (30),
   TABLE_TYPE               VARCHAR (10),
   DATA_SOURCE_PATH         VARCHAR (60),
   HEAD_DIRECTION           VARCHAR (10),
   EDIT_LAYOUT_TYPE         VARCHAR (10),
   MODIFY_SUMMARY_PROGRAM   VARCHAR (100),
   EDIT_SAVE_PROGRAME       VARCHAR (100),
   EDIT_URL                 VARCHAR (100),
   CREATE_USER_REFID        VARCHAR (32),
   CREATE_DATE_TIME         DATE,
   MODIFY_USER_REFID        VARCHAR (32),
   MODIFY_DATE_TIME         DATE,
   VERSION                  INT,
   ACTIVE                   INT
);

CREATE TABLE TABLE_TEMPLATE_ITEM
(
   REFID                VARCHAR (32) NOT NULL,
   TABLE_REFID          VARCHAR (32),
   METADATA_REFID       VARCHAR (32),
   R_METADATA_NAME      VARCHAR (60),
   TITLE                VARCHAR (60),
   KEY_FLAG             CHAR (1),
   SHOW_FLAG            CHAR (1),
   COPY_FLAG            CHAR (1),
   ORD                  INT,
   ALTERNATE_FLAG       CHAR (1),
   EMPTY_FLAG           CHAR (1),
   WIDTH                INT,
   HEIGHT               INT,
   READONLY_FLAG        CHAR (1),
   TIME_KEY_FLAG        CHAR (1),
   MIN_VALUE            NUMERIC (10, 3),
   MAX_VALUE            NUMERIC (10, 3),
   VERIFY_POLICY_CODE   VARCHAR (100),
   SEARCH_FLAG          CHAR (1),
   TEMPLATE_MENU_FLAG   CHAR (1),
   SAVE_FLAG            CHAR (1),
   EDIT_FLAG            CHAR (1),
   MUST_INPUT_FLAG      CHAR (1),
   NDA_FLAG             CHAR (1),
   CREATE_USER_REFID    VARCHAR (32),
   CREATE_DATE_TIME     DATE,
   MODIFY_USER_REFID    VARCHAR (32),
   MODIFY_DATE_TIME     DATE,
   VERSION              INT,
   ACTIVE               INT
);

CREATE TABLE TABLE_TEMPLATE_OPERATE
(
   REFID               VARCHAR (32) NOT NULL,
   TABLE_REFID         VARCHAR (32),
   OPERATE_REFID       VARCHAR (32),
   ORD                 INT,
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT
);

CREATE TABLE TABLE_TEMPLATE_PATTERN
(
   REFID               VARCHAR (32) NOT NULL,
   TABLE_REFID         VARCHAR (32) NOT NULL,
   PATTERN_NAME        VARCHAR (60),
   ORD                 INT,
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT
);

CREATE TABLE TABLE_TEMPLATE_PATTERN_ITEM
(
   REFID                 VARCHAR (32) NOT NULL,
   TABLE_ITEM_REFID      VARCHAR (32),
   TABLE_PATTERN_REFID   VARCHAR (32),
   ORD                   INT,
   CREATE_USER_REFID     VARCHAR (32),
   CREATE_DATE_TIME      DATE,
   MODIFY_USER_REFID     VARCHAR (32),
   MODIFY_DATE_TIME      DATE,
   VERSION               INT,
   ACTIVE                INT
);

CREATE TABLE task_nurse_attention
(
   id           INT NOT NULL PRIMARY KEY,
   nurse_id     INT NOT NULL,
   patient_id   INT NOT NULL
);

CREATE TABLE task_white_record
(
   id             INT NOT NULL PRIMARY KEY,
   nurse_id       INT NOT NULL,
   nurse_name     VARCHAR (20) NOT NULL,
   receive_area   VARCHAR (20) NOT NULL,
   receive_id     VARCHAR (50),
   create_time    DATE NOT NULL,
   msg_text       VARCHAR (1000)
);

CREATE TABLE task_white_record_item
(
   id          INT NOT NULL PRIMARY KEY,
   record_id   INT NOT NULL,
   send_type   VARCHAR (3) NOT NULL,
   data        BLOB NOT NULL
);

CREATE TABLE TEMPLATE
(
   REFID               VARCHAR (32) NOT NULL,
   TEMPLATE_NAME       VARCHAR (60),
   TEMPLATE_CODE       VARCHAR (30),
   TEMPLATE_TYPE       VARCHAR (10),
   TEMPLATE_HTML       CLOB,
   PAGER_CODE          VARCHAR (10),
   PAGER_WIDTH         INT,
   PAGER_HEIGHT        INT,
   VERTICAL_FLAG       CHAR (1),
   LOCAL_DATA_FLAG     CHAR (1),
   DATA_PRAGRAM        VARCHAR (60),
   INSTANT_URL         VARCHAR (100),
   NDA_URL             VARCHAR (100),
   SEARCH_URL          VARCHAR (100),
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT
);

CREATE TABLE TEMPLATE_ITEM
(
   REFID                VARCHAR (32) NOT NULL,
   TEMPLATE_REFID       VARCHAR (32),
   ITEM_FROM            VARCHAR (10),
   METADATA_REFID       VARCHAR (32),
   R_METADATA_NAME      VARCHAR (60),
   TABLE_REFID          VARCHAR (32),
   WIDTH                INT,
   HEIGHT               INT,
   COPY_FLAG            CHAR (1),
   SEARCH_FLAG          CHAR (1),
   TEMPLATE_MENU_FLAG   CHAR (1),
   NDA_FIELD_TYPE       VARCHAR (10),
   SAVE_FLAG            CHAR (1),
   EDIT_FLAG            CHAR (1),
   MUST_INPUT_FLAG      CHAR (1),
   AREA_NAME            VARCHAR (60),
   ORD                  INT,
   CREATE_USER_REFID    VARCHAR (32),
   CREATE_DATE_TIME     DATE,
   MODIFY_USER_REFID    VARCHAR (32),
   MODIFY_DATE_TIME     DATE,
   VERSION              INT,
   ACTIVE               INT
);

CREATE TABLE TEMPLATE_OPERATE
(
   REFID               VARCHAR (32) NOT NULL,
   TEMPLATE_REFID      VARCHAR (32),
   OPERATE_REFID       VARCHAR (32),
   ORD                 INT,
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT
);

CREATE TABLE VERIFY_POLICY
(
   REFID               VARCHAR (32) NOT NULL,
   VERIFY_NAME         VARCHAR (60),
   VERIFY_CODE         VARCHAR (30),
   PRAGRME             VARCHAR (100),
   REMARK              VARCHAR (300),
   CREATE_USER_REFID   VARCHAR (32),
   CREATE_DATE_TIME    DATE,
   MODIFY_USER_REFID   VARCHAR (32),
   MODIFY_DATE_TIME    DATE,
   VERSION             INT,
   ACTIVE              INT
);

CREATE TABLE vital_sign_info (
   vital_sign_info_id INT NOT NULL PRIMARY KEY,
   exec_nurse VARCHAR (20) NOT NULL,
   exec_time DATE NOT NULL,
   patient_id VARCHAR (15) NOT NULL,
   create_time DATE NOT NULL,
   temperature VARCHAR (20),
   temperature_value VARCHAR (10),
   temperature_flag SMALLINT,
   temperature_unit VARCHAR (10) DEFAULT ('℃'),
   pluse VARCHAR (20),
   pluse_value VARCHAR (10),
   pluse_unit VARCHAR (10) DEFAULT ('次/分'),
   hr VARCHAR (20),
   hr_value VARCHAR (10),
   hr_unit VARCHAR (10) DEFAULT ('次/分'),
   breath VARCHAR (20),
   breath_value VARCHAR (20),
   breath_unit VARCHAR (10) DEFAULT ('次/分'),
   bp VARCHAR (20),
   bp_value VARCHAR (20),
   bp_unit VARCHAR (10) DEFAULT ('mmHg'),
   in_take VARCHAR (20),
   in_take_value VARCHAR (20),
   in_take_unit VARCHAR (10) DEFAULT ('ml'),
   urine VARCHAR (20),
   urine_value VARCHAR (20),
   urine_unit VARCHAR (10) DEFAULT ('ml'),
   other_out_put VARCHAR (20),
   other_out_put_value VARCHAR (20),
   other_out_put_unit VARCHAR (10) DEFAULT ('ml'),
   total_out_put VARCHAR (20),
   total_out_put_value VARCHAR (20),
   total_out_put_unit VARCHAR (10) DEFAULT ('ml'),
   shit VARCHAR (20),
   shit_value VARCHAR (20),
   shit_unit VARCHAR (10) DEFAULT ('次'),
   height VARCHAR (20),
   height_value VARCHAR (20),
   height_unit VARCHAR (10) DEFAULT ('cm'),
   weight VARCHAR (20),
   weight_value VARCHAR (20),
   weight_unit VARCHAR (10) DEFAULT ('kg'),
   skin_test VARCHAR (20),
   skin_test_value VARCHAR (30),
   other_value VARCHAR (50),
   event VARCHAR (50),
   event_value VARCHAR (50),
   in_patient_num VARCHAR (20),
   remark_value VARCHAR (100)
);

CREATE TABLE lx_ward_patrol
(
   ward_patrol_id     INT NOT NULL PRIMARY KEY,
   pat_id             INT NOT NULL,
   nurse_code         VARCHAR (6) NOT NULL,
   dept_code          VARCHAR (4) NOT NULL,
   tend_level         INT DEFAULT 3,
   patrol_date        DATE,
   next_patrol_date   DATE
);