package com.anyi.report;

import com.lachesis.mnis.core.bodysign.BodySignConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2016, Lachesis-mh.com
 * All rights reserved.
 * <p/>
 * Discription:文书模块使用的常量类
 * <p/>
 * Created by junming.ren
 * on 2016/5/26.
 */
public class DocReportConstants {
    public static final String DATE_LIST = "DATE_LIST";//记录日期
    public static final String TIME_LIST = "TIME_LIST";//记录时间
    public static final String CREATE_PERSON = "CREATE_PERSON";//记录创建人
    public static final String APPROVE_PERSON = "APPROVE_PERSON";//审核人
    public static final String PAT_NAME = "PTNT_NAM";//患者姓名
    public static final String DEPT_CODE = "DEPT_CODE";//科室编号
    public static final String DEPT_NAME = "DE08_10_054_00";//科室名称
    public static final String BED_NO = "PTNT_BED";//床号
    public static final String INPAT_ID = "PTNT_HOSPITAL_NO";//住院号
    public static final String PAT_GENDER = "PTNT_SEX";//性别
    public static final String PAT_AGE = "PTNT_AGE";//年龄
    public static final String ADMISSION_TIME = "BE_HOSPITALIZED_TIME";//入院时间
    public static final String PAT_DIAGNOSE = "PTNT_DIAGNOSE";//诊断
    public static final String PAT_COMPLAINT = "DE04_01_119_00";//主诉
    public static final String MARITAL_STATUS = "MARITAL_STATUS";//婚姻状况
    public static final String PAT_PROFESSION = "DE02_01_052_00";//职业
    public static final String EDUCATION_LEVEL = "LX_ZJYONGKANG_002";//文化程度
    public static final String PAT_BIRTHPLACE = "LX02_RYPG_040";//籍贯
    public static final String PAT_RELIGION = "LX_SZSANYUAN_486";//宗教
    public static final String PAYMENT_TYPE = "PAY_TYPE";//付费方式
    public static final String DATA_SOURCE = "LX_SZSANYUAN_307";//资料来源
    public static final String CARE_GIVER = "LX_SZSANYUAN_308";//日常照顾者
    public static final String NURSING_GRADE = "SZS_NURSING_GRADE";//护理等级
    public static final String NURSING_GRADE_SPECIAL = "SZS_NURSING_GRADE_1";//特级护理
    public static final String NURSING_GRADE_1 = "SZS_NURSING_GRADE_2";//一级护理
    public static final String NURSING_GRADE_2 = "SZS_NURSING_GRADE_3";//二级护理
    public static final String NURSING_GRADE_3 = "SZS_NURSING_GRADE_4";//三级护理
    public static final String ADMISSION_FORM = "LX_ZJYONGKANG_085";//入院方式
    public static final String ALLERGY_HISTORY = "DE02_10_022_00";//过敏史
    public static final String IN_TAKE = "inTake";//入量
    public static final String OUT_TAKE = "outTake";//出量
    public static final String OUT_NAME = "out_name";//出量名称
    public static final String REMARK = "remark1";//病情观察及护理措施

    public static final String DATA_TYPE_OPT = "OPT";//下拉菜单
    public static final String DATA_TYPE_DYNA_TITLE = "DYNA_TITLE";//动态表头
    public static final String DATA_TYPE_MSEL = "MSEL";//多选
    public static final String DATA_TYPE_SEL = "SEL";//单选
    public static final String DATA_TYPE_SWT = "SWT";//单选
    public static final String TEMPLATE_TYPE_FIXED = "fixed";//固定表格类型
    public static final String TEMPLATE_TYPE_LIST = "list";//列表类型

    public static final String REPORT_TYPE_HLJL = "1";//护理记录单
    public static final String REPORT_TYPE_DIEDAO = "2";//跌倒评估单
    public static final String REPORT_TYPE_NORTON = "3";//压疮norton评估单
    public static final String REPORT_TYPE_WATERLOW = "4";//压疮waterlow评估单

    public static final String RECORD_ACTION_CONFIG = "recordAction";//是否记录操作行为的配置

    public static final String TEMPLATE_BAND_NAME_DETAIL = "detail";//list类型文书的记录区域
    public static final String TEMPLATE_BAND_NAME_PAGEHEADER = "page-header";//list类型文书的记录区域

    public static final String TEMPLATE_ORIENTATION_LAND = "Landscape";//文书横向打印
    public static final String TEMPLATE_ORIENTATION_PORT = "Portrait";//文书纵向打印

    public static final int RECORD_OPEN_INTERVAL = 10;//文书记录允许修改的时间跨度，单位：天
    public static final int DIAGNOSE_NUM = 3;//诊断信息显示个数
    public static final String ZERO_HM_TIME = "00:00";

    public static final String RECORD_APPROVED_STATUS = "Y";//文书记录已审核状态

    //文书模板中，生命体征项目所使用的组件名称
    public static final List<String> BODYSIGN_NAME_LIST = new ArrayList<String>();
    static {
        BODYSIGN_NAME_LIST.add("temperature");
        BODYSIGN_NAME_LIST.add("pulse");
        BODYSIGN_NAME_LIST.add("heartRate");
        BODYSIGN_NAME_LIST.add("bloodPress");
        BODYSIGN_NAME_LIST.add("breath");
        BODYSIGN_NAME_LIST.add("oxygenSaturation");
    }

    public static final String INOUT_METADATA_TOTALINPUT = "inTake";//出入量元数据名称，总入量
    public static final String INOUT_METADATA_TOTALINPUT_CH = "总入量";//出入量元数据中文名称，总入量
    public static final String INOUT_METADATA_URINE = "out_name_01";//出入量元数据名称，尿
    //文书出入量项目与生命体征出入量项目名称的对应关系
    public static final Map<String, String> INOUT_ITEM_TO_BODYSIGN = new HashMap<>();
    static {
        INOUT_ITEM_TO_BODYSIGN.put(INOUT_METADATA_TOTALINPUT, BodySignConstants.TOTAL_INPUT);
        INOUT_ITEM_TO_BODYSIGN.put(INOUT_METADATA_TOTALINPUT_CH, BodySignConstants.TOTAL_INPUT);
        INOUT_ITEM_TO_BODYSIGN.put(INOUT_METADATA_URINE, BodySignConstants.URINE);
    }

    public enum ReportQueryType{
        TRANSFER("transfer", 1),
        HISTORY("history", 2);
        private String name;
        private int index;

        private ReportQueryType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }
    }

    public enum PatPrintType{
        REPORT("DOC", 0),
        TEMPRATURE("Temprature",1);
        private String name;
        private int index;

        private PatPrintType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }

        public static int getIndexByName(String name){
            for(PatPrintType type:PatPrintType.values()){
                if(name.equals(type.getName())){
                    return type.getIndex();
                }
            }
            return 0;
        }
    }

}
