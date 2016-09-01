package com.lachesis.mnis.core.old.doc;

public interface NurseDocCst {

	String YES_FLAG="Y";
	String NO_FLAG="N";
	
	String OBJECT="obj";
	String URL="url";
	String TIME="time";
	String NURSER = "nurser";
	String BLOOD_PRESSURE_SPLIT = "/";
	String NURSE_SHIFT_SPLIT = ",";
	String END_TIME_SUFFIX=" 23:59:59";

	Integer MAX_TABLE_ITEM_INDEX = 9999;
	
	String NURSE_DOC = "nursedoc";
	String TEMPLATE_TYPE = "templateType";
	String RESOUCE_SYSTEM_PRGRAME = "FromSystem";
	
	String NURSE_DOC_APPROVE_RESOURCE="NURSE_DOC_APPROVE";
	
	String METADATA_SHEET_NAME = "元数据";
	String METADATA_OPTION_SHEET_NAME = "元数据选项";
	String METADATA_CELL_DATASOURCE= "datasource";
	String METADATA_CELL_INPUTTYPE= "inputtype";
	String METADATA_CELL_DATATYPE= "datatype";
	String METADATA_APPROVE_ID = "APPROVE_USER";
	String METADATA_APPROVE_NAME = "APPROVE_USER_NAME";
	String METADATA_APPROVE_TIME = "APPROVE_TIME";
	String METADATA_WRITE_NAME = "DE02_01_039_00";
	String METADATA_RECODE_TIME = "DE09_00_053_00";
	String METADATA_EMPTY_CUSTOM= "EMPTY_CUSTOM";
	
	String NURSEDOC_APPROVE_OPERATE = "approve";
	String NURSEDOC_SAVE_OPERATE = "save";
	String NURSEDOC_DATE_SPLIT = " ~ ";
	String NURSEDOC_TEMPLATE_NODE_CLOSE = "closed";

	String OPERATE_REFERENCE_OPERATE_CODE = "REFERENCE";
	String OPERATE_SERVICE = "operateServiceImpl";
	String FIELD_SWITCH_SERVICE = "fieldSwitchServiceImpl";
	
	String NURSEDOC_LABEL_PROPERTIES = "config.properties.NurseDocLabel";
	String NURSEDOC_EXCEL_PROPERTIES = "config.properties.NurseDocExcel";
	String NURSEDOC_MESSAGE_PROPERTIES = "config.properties.NurseDocMessage";

}
