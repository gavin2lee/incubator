package com.lachesis.mnisqm.constants;


public class MnisQmConstants {
	 public static final String STATUS_YX = "01";//有效
	 public static final String STATUS_XT = "08";//系统数据
	 public static final String STATUS_WX = "09";//无效
	 
	 public static final String APPRV_N = "01";//未审批
	 public static final String APPRV_A = "02";//已审批
	 
	 public static final String IS_USE = "1";//使用
	 public static final String NO_USE = "0";//未使用
	 
	 public class SysDicConstants{
		 public static final String papersType="papersType";//证件类型
		 public static final String practiceType="practiceType";//执业类型
		 public static final String politacalStatus = "politacalStatus";
		 public static final String nature = "nature";//员工性质
		 public static final String gender = "gender";//性别
		 public static final String whether = "whether";//是否
		 public static final String level = "level";//护士级别
		 public static final String dateType = "dateType";//日期类型
	 }
	 
	 public static final String defaultDept="0001";//新录入的人，默认科室
	 public static final String lvealDept ="0002";//已离职的科室

	 public static final String TeachLevel = "1";//带教类型
	 public static final String Clinical = "2";//临床层级
	 public static final String DeptChange = "3";//带教类型
	 
	 public static final String userType_04="04";//离职
	 
	 public static final String hisWebServiceUrl = "hisWebServiceUrl";//his的webService地址
	 public static final String bedWebServiceBody = "bedWebServiceBody";//床位信息请求body
	 public static final String inPatientInfoWebServiceBody = "inPatientInfoWebServiceBody";//在院患者信息请求body
	 public static final String outPatientInfoWebServiceBody = "outPatientInfoWebServiceBody";//出院患者信息请求body
	 public static final String lastInSynchronizationTime = "lastInSynchronizationTime";//入院最后同步时间
	 public static final String lastOutSynchronizationTime = "lastOutSynchronizationTime";//出院最后同步时间
	 public static final String deptInfoWebServiceBody = "deptInfoWebServiceBody";//科室信息请求body
	 public static final String operationInfoWebServiceBody = "operationInfoWebServiceBody";//手术患者人数请求body
	 
	 public static final String APPRV_01="01";//新建 
	 public static final String APPRV_02="02";//提交 
	 public static final String APPRV_03="03";//已审核
	 public static final String APPRV_04="04";//拒绝
	 
	 public static final String OP_TYPE_01="submitEventReportToHeadNurse"; //提交至护士长
	 public static final String OP_TYPE_02="submitEventReportToNurseDept"; //提交不良事件到护理部
	 public static final String OP_TYPE_03="trackEventReport"; //不良事件审核通过，开始追踪
	 public static final String OP_TYPE_04="returnEventReportToNurse"; //打回不良事件至护士，重新修改 (护士长的操作)
	 public static final String OP_TYPE_05="returnEventReportToHeadNurse"; //打回不良事件至护士长，重新修改 (护理部的操作)
	 public static final String OP_TYPE_06="terminateEventReport"; //终止不良事件
	 
	 
	 public static final String TASK_STATUS_01="01"; //提交至护士长
	 public static final String TASK_STATUS_02="02"; //提交不良事件到护理部
	 public static final String TASK_STATUS_03="03"; //审核通过，开始追踪
	 public static final String TASK_STATUS_04="04"; //打回不良事件至护士
	 public static final String TASK_STATUS_05="05"; //打回不良事件至护士长
	 public static final String TASK_STATUS_06="06"; //终止
	 
	 public static final String APPRV_STATUS_01="01"; //不良事件前端操作权限 01：护士可修改,上报 ,删除
	 public static final String APPRV_STATUS_02="02"; //02：护士长 可上报 打回 终止
	 public static final String APPRV_STATUS_03="03"; //03：护理部 可通过 打回 终止
	 public static final String APPRV_STATUS_04="04"; //04:没有权限 包含 已通过,终止
	 
	 public static final String SYS_DATA_1 = "1";//系统科室
	 public static final String SYS_DATA_0 = "0";//非系统科室
}