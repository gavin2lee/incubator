CREATE TABLE DICTIONARY_
(
   ID_          VARCHAR (64),
   NAME_        VARCHAR (64),
   PARENT_      VARCHAR (64),
   VALUE_       VARCHAR (200),
   ORDER_       INT,
   REMARK_      VARCHAR (2000),
   VALID_       INT,
   DELETABLE_   INT,
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_MENU_
(
   ID_          VARCHAR (64),
   NAME_        VARCHAR (64),
   PARENT_      VARCHAR (64),
   URL_         VARCHAR (1000),
   ORDER_       INT,
   REMARK_      VARCHAR (2000),
   VALID_       INT,
   DELETABLE_   INT,
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_
(
   ID_            VARCHAR (64),
   CURE_NO_       VARCHAR (64),
   HOSPITAL_NO_   VARCHAR (64),
   DOC_TYPE_      VARCHAR (64),
   APPROVE_       INT,
   EDITABLE_      INT,
   DATE_          DATETIME,
   HOLD1_         VARCHAR (200),
   HOLD2_         VARCHAR (200),
   HOLD3_         VARCHAR (200),
   HOLD4_         VARCHAR (200),
   HOLD5_         VARCHAR (200),
   HOLD6_         VARCHAR (200),
   HOLD7_         VARCHAR (200),
   HOLD8_         VARCHAR (200),
   HOLD9_         VARCHAR (200),
   USER_CODE_     VARCHAR (64),
   DEPT_CODE_     VARCHAR (64),
   DEPT_NAME_     VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_BLOOD_SUGAR_
(
   ID_          VARCHAR (64),
   DOC_         VARCHAR (64),
   DATE_        VARCHAR (64),
   PERIOD_      VARCHAR (64),
   VALUE_       VARCHAR (64),
   SIGN_        VARCHAR (100),
   REMARK_      VARCHAR (2000),
   USER_CODE_   VARCHAR (64),
   DEPT_CODE_   VARCHAR (64),
   DEPT_NAME_   VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_BASE_NURSE_
(
   ID_             VARCHAR (64),
   DOC_            VARCHAR (64),
   DATE_           VARCHAR (64),
   MOUTH_          VARCHAR (10),
   PERINEUM_       VARCHAR (10),
   DRAINAGE_BAG_   VARCHAR (10),
   LUNG_PUNCH_     VARCHAR (10),
   PLACE_PIPE_     VARCHAR (10),
   APPLICATION_    VARCHAR (10),
   PIPE_WASH_      VARCHAR (10),
   TRACHEOTOMY_    VARCHAR (10),
   HEAD_WASH_      VARCHAR (10),
   BLADDER_WASH_   VARCHAR (10),
   HOLD1_          VARCHAR (10),
   HOLD2_          VARCHAR (10),
   SIGN_           VARCHAR (100),
   USER_CODE_      VARCHAR (64),
   DEPT_CODE_      VARCHAR (64),
   DEPT_NAME_      VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_NURSE_
(
   ID_                  VARCHAR (64),
   DOC_                 VARCHAR (64),
   DATE_                VARCHAR (64),
   TEMPERATURE_         VARCHAR (20),
   PULSE_               VARCHAR (20),
   BREATH_              VARCHAR (20),
   BLOOD_PRESSURE_      VARCHAR (20),
   BLOOD_OXYGEN_        VARCHAR (20),
   AWARENESS_           VARCHAR (10),
   PUPIL_SIZE_LEFT_     VARCHAR (10),
   PUPIL_LIGHT_LEFT_    VARCHAR (10),
   PUPIL_SIZE_RIGHT_    VARCHAR (10),
   PUPIL_LIGHT_RIGHT_   VARCHAR (10),
   OXYGEN_WAY_          VARCHAR (10),
   OXYGEN_FLOW_         VARCHAR (10),
   SPUTUM_              VARCHAR (10),
   QUALE_               VARCHAR (10),
   SKIN_                VARCHAR (10),
   PIPE1_               VARCHAR (10),
   PIPE2_               VARCHAR (10),
   PIPE3_               VARCHAR (10),
   PIPE4_               VARCHAR (10),
   PIPE5_               VARCHAR (10),
   URINE_               VARCHAR (10),
   RECORD_              VARCHAR (4000),
   SIGN_                VARCHAR (100),
   USER_CODE_           VARCHAR (64),
   DEPT_CODE_           VARCHAR (64),
   DEPT_NAME_           VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_DATA_
(
   ID_          VARCHAR (64),
   DOC_         VARCHAR (64),
   KEY_         VARCHAR (400),
   VALUE_       VARCHAR (4000),
   USER_CODE_   VARCHAR (64),
   DEPT_CODE_   VARCHAR (64),
   DEPT_NAME_   VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_PAIN_
(
   ID_                   VARCHAR (64),
   DOC_                  VARCHAR (64),
   DATE_                 VARCHAR (64),
   PART_                 VARCHAR (100),
   TYPE_                 VARCHAR (100),
   INTENSITY_            VARCHAR (100),
   QUALE_                VARCHAR (100),
   EFFECT_               VARCHAR (100),
   COMFORT_              VARCHAR (100),
   EXPLAIN_              VARCHAR (100),
   REST_                 VARCHAR (100),
   DISPERSE_ATTENTION_   VARCHAR (100),
   CURE_                 VARCHAR (100),
   HOLD1_                VARCHAR (100),
   NOTICE_               VARCHAR (100),
   TIME_                 VARCHAR (100),
   PILL_                 VARCHAR (100),
   DRUG_WAY_             VARCHAR (100),
   DRUG_RATE_            VARCHAR (100),
   DRUG_TIME_            VARCHAR (100),
   DRUG_INTENSITY_       VARCHAR (100),
   SIGN_                 VARCHAR (100),
   USER_CODE_            VARCHAR (64),
   DEPT_CODE_            VARCHAR (64),
   DEPT_NAME_            VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_RESTRAINT_
(
   ID_                VARCHAR (64),
   DOC_               VARCHAR (64),
   DATE_              VARCHAR (64),
   REASON_DRAW_       VARCHAR (10),
   REASON_FRET_       VARCHAR (10),
   REASON_FALL_       VARCHAR (10),
   PART_LEFT_HAND_    VARCHAR (10),
   PART_RIGHT_HAND_   VARCHAR (10),
   PART_LEFT_FOOT_    VARCHAR (10),
   PART_RIGHT_FOOT_   VARCHAR (10),
   PART_CHEST_        VARCHAR (10),
   POINT_GOOD_        VARCHAR (10),
   POINT_BAD_         VARCHAR (10),
   POINT_INTACT_      VARCHAR (10),
   POINT_BRUISE_      VARCHAR (10),
   POINT_NOT_SWELL_   VARCHAR (10),
   POINT_SWELL_       VARCHAR (10),
   OBSERVE_           VARCHAR (10),
   SIGN_              VARCHAR (100),
   USER_CODE_         VARCHAR (64),
   DEPT_CODE_         VARCHAR (64),
   DEPT_NAME_         VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_PRESS_
(
   ID_                          VARCHAR (64),
   DOC_                         VARCHAR (64),
   DATE_                        VARCHAR (64),
   AWARE_CLEAR_                 VARCHAR (10),
   AWARE_DIM_                   VARCHAR (10),
   AWARE_CONFUSE_               VARCHAR (10),
   AWARE_COMA_                  VARCHAR (10),
   MOVE_WELL_                   VARCHAR (10),
   MOVE_HELP_                   VARCHAR (10),
   MOVE_GET_UP_                 VARCHAR (10),
   MOVE_BED_                    VARCHAR (10),
   MEMBER_WELL_                 VARCHAR (10),
   MEMBER_BIT_LIMIT_            VARCHAR (10),
   MEMBER_LIMIT_                VARCHAR (10),
   MEMBER_CAN_NOT_              VARCHAR (10),
   EAT_ENOUGH_                  VARCHAR (10),
   EAT_NOT_ENOUGH_              VARCHAR (10),
   EAT_LITTLE_                  VARCHAR (10),
   EAT_CAN_NOT_                 VARCHAR (10),
   INCONTINENCE_DRY_            VARCHAR (10),
   INCONTINENCE_WET_SOMETIME_   VARCHAR (10),
   INCONTINENCE_WET_OFTEN_      VARCHAR (10),
   INCONTINENCE_WET_ALWAYS_     VARCHAR (10),
   SKIN_NORMAL_                 VARCHAR (10),
   SKIN_COLOR_                  VARCHAR (10),
   SKIN_TEMPERATURE_            VARCHAR (10),
   SKIN_SWELL_                  VARCHAR (10),
   SCORE_                       VARCHAR (10),
   LEVEL_                       VARCHAR (10),
   MEASURE_DRY_                 VARCHAR (10),
   MEASURE_OVER_                VARCHAR (10),
   MEASURE_BED_                 VARCHAR (10),
   MEASURE_PRESS_               VARCHAR (10),
   MEASURE_DRESSING_            VARCHAR (10),
   MEASURE_BILL_                VARCHAR (10),
   MEASURE_MARK_                VARCHAR (10),
   MEASURE_ANNOUNCE_            VARCHAR (10),
   SIGN_                        VARCHAR (100),
   USER_CODE_                   VARCHAR (64),
   DEPT_CODE_                   VARCHAR (64),
   DEPT_NAME_                   VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_ADULT_FALL_
(
   ID_          VARCHAR (64),
   DOC_         VARCHAR (64),
   DATE_        VARCHAR (64),
   HISTORY_     VARCHAR (10),
   AWARE_       VARCHAR (10),
   SIGHT_       VARCHAR (10),
   MOVE_        VARCHAR (10),
   AGE_         VARCHAR (10),
   WEAK_        VARCHAR (10),
   DIZZY_       VARCHAR (10),
   DRUG_        VARCHAR (100),
   COMPANY_     VARCHAR (10),
   SCORE_       VARCHAR (10),
   ANNOUCE_     VARCHAR (10),
   MARK_        VARCHAR (10),
   PATROL_      VARCHAR (10),
   FENCE_       VARCHAR (10),
   PEOPLE_      VARCHAR (10),
   SIGN_        VARCHAR (100),
   USER_CODE_   VARCHAR (64),
   DEPT_CODE_   VARCHAR (64),
   DEPT_NAME_   VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_CHILD_FALL_
(
   ID_          VARCHAR (64),
   DOC_         VARCHAR (64),
   DATE_        VARCHAR (64),
   AGE_LT1_     VARCHAR (10),
   AGE1TO4_     VARCHAR (10),
   AGE_GT4_     VARCHAR (10),
   MOVE_        VARCHAR (10),
   HISTORY_     VARCHAR (10),
   DRUG_        VARCHAR (10),
   AWARE_       VARCHAR (10),
   SIGHT_       VARCHAR (10),
   SCORE_       VARCHAR (10),
   ANNOUCE_     VARCHAR (10),
   MARK_        VARCHAR (10),
   PATROL_      VARCHAR (10),
   FENCE_       VARCHAR (10),
   PEOPLE_      VARCHAR (10),
   SIGN_        VARCHAR (100),
   USER_CODE_   VARCHAR (64),
   DEPT_CODE_   VARCHAR (64),
   DEPT_NAME_   VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_OVER_
(
   ID_          VARCHAR (64),
   DOC_         VARCHAR (64),
   DATE_        VARCHAR (64),
   LIE_         VARCHAR (100),
   SKIN_        VARCHAR (100),
   SIGN_        VARCHAR (100),
   USER_CODE_   VARCHAR (64),
   DEPT_CODE_   VARCHAR (64),
   DEPT_NAME_   VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_IN_OUT_
(
   ID_          VARCHAR (64),
   DOC_         VARCHAR (64),
   DATE_        VARCHAR (64),
   IN_ITEM_     VARCHAR (100),
   IN_VALUE_    VARCHAR (100),
   OUT_ITEM_    VARCHAR (100),
   OUT_VALUE_   VARCHAR (100),
   SIGN_        VARCHAR (100),
   USER_CODE_   VARCHAR (64),
   DEPT_CODE_   VARCHAR (64),
   DEPT_NAME_   VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_HEART_
(
   ID_               VARCHAR (64),
   DOC_              VARCHAR (64),
   DATE_             VARCHAR (64),
   PULSE_            VARCHAR (100),
   BREATH_           VARCHAR (100),
   BLOOD_PRESSURE_   VARCHAR (100),
   BLOOD_OXYGEN_     VARCHAR (100),
   SIGN_             VARCHAR (100),
   USER_CODE_        VARCHAR (64),
   DEPT_CODE_        VARCHAR (64),
   DEPT_NAME_        VARCHAR (200),
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_BARTHEL_
(
   ID_                        VARCHAR (64),
   DOC_                       VARCHAR (64),
   DATE_                      VARCHAR (64),
   EAT_INDEPENDENTLY_         VARCHAR (10),
   EAT_LITTLE_HELP_           VARCHAR (10),
   EAT_EXTREMELY_HELP_        VARCHAR (10),
   EAT_DEPENDENTLY_           VARCHAR (10),
   BATH_INDEPENDENTLY_        VARCHAR (10),
   BATH_LITTLE_HELP_          VARCHAR (10),
   BATH_EXTREMELY_HELP_       VARCHAR (10),
   BATH_DEPENDENTLY_          VARCHAR (10),
   ATTIRE_INDEPENDENTLY_      VARCHAR (10),
   ATTIRE_LITTLE_HELP_        VARCHAR (10),
   ATTIRE_EXTREMELY_HELP_     VARCHAR (10),
   ATTIRE_DEPENDENTLY_        VARCHAR (10),
   DRESS_INDEPENDENTLY_       VARCHAR (10),
   DRESS_LITTLE_HELP_         VARCHAR (10),
   DRESS_EXTREMELY_HELP_      VARCHAR (10),
   DRESS_DEPENDENTLY_         VARCHAR (10),
   DEFECATE_INDEPENDENTLY_    VARCHAR (10),
   DEFECATE_LITTLE_HELP_      VARCHAR (10),
   DEFECATE_EXTREMELY_HELP_   VARCHAR (10),
   DEFECATE_DEPENDENTLY_      VARCHAR (10),
   PEE_INDEPENDENTLY_         VARCHAR (10),
   PEE_LITTLE_HELP_           VARCHAR (10),
   PEE_EXTREMELY_HELP_        VARCHAR (10),
   PEE_DEPENDENTLY_           VARCHAR (10),
   WC_INDEPENDENTLY_          VARCHAR (10),
   WC_LITTLE_HELP_            VARCHAR (10),
   WC_EXTREMELY_HELP_         VARCHAR (10),
   WC_DEPENDENTLY_            VARCHAR (10),
   SHIFT_INDEPENDENTLY_       VARCHAR (10),
   SHIFT_LITTLE_HELP_         VARCHAR (10),
   SHIFT_EXTREMELY_HELP_      VARCHAR (10),
   SHIFT_DEPENDENTLY_         VARCHAR (10),
   WALK_INDEPENDENTLY_        VARCHAR (10),
   WALK_LITTLE_HELP_          VARCHAR (10),
   WALK_EXTREMELY_HELP_       VARCHAR (10),
   WALK_DEPENDENTLY_          VARCHAR (10),
   STAIR_INDEPENDENTLY_       VARCHAR (10),
   STAIR_LITTLE_HELP_         VARCHAR (10),
   STAIR_EXTREMELY_HELP_      VARCHAR (10),
   STAIR_DEPENDENTLY_         VARCHAR (10),
   SCORE_                     VARCHAR (10),
   LEVEL_                     VARCHAR (10),
   SIGN_                      VARCHAR (100),
   USER_CODE_                 VARCHAR (64),
   DEPT_CODE_                 VARCHAR (64),
   DEPT_NAME_                 VARCHAR (200),
   PRIMARY KEY (ID_)
);

-- 诊断修改记录表
CREATE TABLE DIAGNOSIS_
(
   ID_            VARCHAR (64),
   CURE_NO_       VARCHAR (64),
   MODIFY_DATE_   DATETIME,
   DIAGNOSIS_     VARCHAR (1000),
   USER_          VARCHAR (64),
   DATE_          DATETIME,
   PRIMARY KEY (ID_)
);

CREATE TABLE DOC_LIVER_NURSE_
(
   ID_                VARCHAR (64),
   DOC_               VARCHAR (64),
   DATE_              VARCHAR (64),
   TEMPERATURE_       VARCHAR (10),
   PULSE_             VARCHAR (10),
   BREATH_            VARCHAR (10),
   BLOOD_PRESSURE_    VARCHAR (10),
   BLOOD_OXYGEN_      VARCHAR (10),
   AWARENESS_         VARCHAR (10),
   IN_NAME_           VARCHAR (100),
   IN_AMOUNT_         VARCHAR (10),
   OUT_NAME_          VARCHAR (100),
   OUT_AMOUNT_        VARCHAR (10),
   OUT_QUALE_         VARCHAR (100),
   MENTAL_STATE_      VARCHAR (100),
   APPETITE_          VARCHAR (100),
   ENTERON_SYMPTOM_   VARCHAR (10),
   BLOOD_             VARCHAR (100),
   SKIN_              VARCHAR (10),
   PIPE_              VARCHAR (100),
   HOLD1_             VARCHAR (10),
   HOLD2_             VARCHAR (10),
   HOLD3_             VARCHAR (10),
   RECORD_            VARCHAR (4000),
   SIGN_              VARCHAR (100),
   USER_CODE_         VARCHAR (64),
   DEPT_CODE_         VARCHAR (64),
   DEPT_NAME_         VARCHAR (200),
   PRIMARY KEY (ID_)
);

