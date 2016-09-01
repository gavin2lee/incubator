/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.web.common.util.WebContextUtils;

/**
 * The class NursingMemoAction.
 * 
 * 护理小白板
 * 
 * @author: yanhui.wang
 * @since: 2014-6-18
 * @version: $Revision$ $Date$ $LastChangedBy$
 * 
 */
@Controller
@RequestMapping("/nur/nursingMemo")
public class NursingMemoAction {

	private final static Log LOG = LogFactory.getLog(NursingMemoAction.class);
	private static boolean flag;  //用于标识该用户是否具有修改治疗项目的权限
	private String nurseId;
	private String nurseName;
	private String depeId;



	/**
	 * 
	 * 进入液晶显示屏界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/nursingMemoMain")
	public String patientGlanceMain(HttpServletRequest request) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(" >>>>>>>>>>>>> redirect to nursing memo maintiance page.");
		}
		note(request,null);
		return "/nur/nursingMemoMain";
	}
	
	@RequestMapping(value = "/getCAresult")
	public @ResponseBody List<String> getCAresult() {
		List<String> list = new ArrayList<String>();
		if(flag){			
			list.add("1");
		}else{
			list.add("0");
		}
		list.add(nurseId);
		list.add(nurseName);
		list.add(depeId);
		return list;
	}
		
	/**
	 * 进入护理小白板主界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/noteMain")
	public String note(HttpServletRequest request,ModelMap modelMap) {
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		modelMap.put("deptCode", sessionUserInfo.getDeptList().get(0).getCode());
		modelMap.put("deptName", sessionUserInfo.getDeptList().get(0).getName());
		//TODO 删除CA权限，请从新添加
		return "/test/note";
	}
	
	@RequestMapping(value = "/oldNoteMain")
	public String oldNoteMain(HttpServletRequest request,ModelMap modelMap) {
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		modelMap.put("deptCode", sessionUserInfo.getDeptList().get(0).getCode());
		modelMap.put("deptName", sessionUserInfo.getDeptList().get(0).getName());
		//TODO 删除CA权限，请从新添加
		return "/test/note_old";
	}
	
	/**
	 * 手术信息
	 * @return
	 */
	@RequestMapping(value = "/getSurgeryMan")
	public @ResponseBody List<SergeryMan> getSurgeryMan() {
		Calendar date = Calendar.getInstance();
		int YY = date.get(Calendar.YEAR) ;
		int MM = date.get(Calendar.MONTH)+1;
		int DD = date.get(Calendar.DATE);
		String today = YY + "-" + MM + "-" + DD;
		String str3 = YY + "-" + MM + "-" + DD + " 08:10:00";
		String str = YY + "-" + MM + "-" + DD + " 19:50:00";
		String str1 = YY + "-" + MM + "-" + DD + " 06:50:00";
		String str2 = YY + "-" + MM + "-" + DD + " 07:50:00";
		List<SergeryMan> list = new ArrayList<SergeryMan>();
		SurgeryInfo surgeryInfo = new SurgeryInfo(str,"搭桥","心脏","吴玥","李欢","李欢","2014-08-12 10:10","李雪","术前禁食","","李欢","37°C","85次/分","20次/分","120/80mmHg","","","","");
		SergeryMan sergeryMan = new SergeryMan("4","张桂珍","手术中",str3,"搭桥","心脏","吴玥","","李欢",today+" 07:30:00","李欢","",surgeryInfo);
		list.add(sergeryMan);
		surgeryInfo = new SurgeryInfo(str,"冠状动脉造影术","心脏","吴玥","李欢","李欢","2014-08-12 12:10","李雪","术前禁食","","李欢","37°C","85次/分","19次/分","120/80mmHg","","","","");
		sergeryMan = new SergeryMan("1","金秀芝秀","待手术",str,"冠状动脉造影术","心脏","吴玥","","李欢","","李欢","",surgeryInfo);
		list.add(sergeryMan);
		surgeryInfo = new SurgeryInfo(str1,"肾脏穿刺活检术","肾脏","李桂荣","李欢","李欢","2014-08-12 14:20","李雪","术前禁食","","李欢","37.5°C","88次/分","20次/分","120/80mmHg","37°C","85次/分","18次/分","119/80mmHg");
		sergeryMan = new SergeryMan("2","周雪","已手术",str1,"肾脏穿刺活检术","肾脏","李桂荣","","李欢",today+" 06:20:00","李欢",today+" 09:00:00",surgeryInfo);
		list.add(sergeryMan);
		surgeryInfo = new SurgeryInfo(str2,"心包穿刺抽液术","心脏","吴玥","李欢","李欢","2014-08-13 09:20","李雪","术前禁食","","李欢","37°C","88次/分","20次/分","119/80mmHg","37°C","85次/分","20次/分","118/80mmHg");
		sergeryMan = new SergeryMan("3","孙娜娜","已手术",str2,"心包穿刺抽液术","心脏","姜士安","","王思奥",today+" 07:15:00","李欢",today+" 10:10:00",surgeryInfo);
		list.add(sergeryMan);
		return list;
	}
	
	class SergeryMan{
		private String bedId;
		private String patientName;
		private String status;
		private String surgeryTime;
		private String surgeryName;
		private String surgeryPart;
		private String firDocName;
		private String secDocName;
		private String sendNurName;
		private String sendTime;
		private String receNurName;
		private String receTime;
		private SurgeryInfo surgeryInfo;
		
		public SergeryMan(){}
		
		public SergeryMan(String bedId, String patientName, String status,
				String surgeryTime, String surgeryName, String surgeryPart,
				String firDocName, String secDocName, String sendNurName,
				String sendTime, String receNurName, String receTime, SurgeryInfo surgeryInfo) {
			this.bedId = bedId;
			this.patientName = patientName;
			this.status = status;
			this.surgeryTime = surgeryTime;
			this.surgeryName = surgeryName;
			this.surgeryPart = surgeryPart;
			this.firDocName = firDocName;
			this.secDocName = secDocName;
			this.sendNurName = sendNurName;
			this.sendTime = sendTime;
			this.receNurName = receNurName;
			this.receTime = receTime;
			this.surgeryInfo = surgeryInfo;
		}
		
		

		public SurgeryInfo getSurgeryInfo() {
			return surgeryInfo;
		}

		public void setSurgeryInfo(SurgeryInfo surgeryInfo) {
			this.surgeryInfo = surgeryInfo;
		}

		public String getBedId() {
			return bedId;
		}
		public void setBedId(String bedId) {
			this.bedId = bedId;
		}
		public String getPatientName() {
			return patientName;
		}
		public void setPatientName(String patientName) {
			this.patientName = patientName;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getSurgeryTime() {
			return surgeryTime;
		}
		public void setSurgeryTime(String surgeryTime) {
			this.surgeryTime = surgeryTime;
		}
		public String getSurgeryName() {
			return surgeryName;
		}
		public void setSurgeryName(String surgeryName) {
			this.surgeryName = surgeryName;
		}
		public String getSurgeryPart() {
			return surgeryPart;
		}
		public void setSurgeryPart(String surgeryPart) {
			this.surgeryPart = surgeryPart;
		}
		public String getFirDocName() {
			return firDocName;
		}
		public void setFirDocName(String firDocName) {
			this.firDocName = firDocName;
		}
		public String getSecDocName() {
			return secDocName;
		}
		public void setSecDocName(String secDocName) {
			this.secDocName = secDocName;
		}
		public String getSendNurName() {
			return sendNurName;
		}
		public void setSendNurName(String sendNurName) {
			this.sendNurName = sendNurName;
		}
		public String getSendTime() {
			return sendTime;
		}
		public void setSendTime(String sendTime) {
			this.sendTime = sendTime;
		}
		public String getReceNurName() {
			return receNurName;
		}
		public void setReceNurName(String receNurName) {
			this.receNurName = receNurName;
		}
		public String getReceTime() {
			return receTime;
		}
		public void setReceTime(String receTime) {
			this.receTime = receTime;
		}		
	}
	
	class SurgeryInfo{
		private String surgeryTime;
		private String surgeryName;
		private String surgeryPart;
		private String DocName;
		private String firNurName;
		private String secNurName;
		private String creatTime;
		private String adviceDoc;
		private String adviceContent;
		private String performTime;
		private String performNurName;
		private String temperatureBefor;
		private String pulseBefor;
		private String breathBefor;
		private String pressureBefor;
		private String temperatureAfter;
		private String pulseAfter;
		private String breathAfter;
		private String pressureAfter;
		
		public SurgeryInfo(){}		

		public SurgeryInfo(String surgeryTime, String surgeryName,
				String surgeryPart, String docName, String firNurName,
				String secNurName, String creatTime, String adviceDoc,
				String adviceContent, String performTime,
				String performNurName, String temperatureBefor,
				String pulseBefor, String breathBefor, String pressureBefor,
				String temperatureAfter, String pulseAfter, String breathAfter,
				String pressureAfter) {
			this.surgeryTime = surgeryTime;
			this.surgeryName = surgeryName;
			this.surgeryPart = surgeryPart;
			DocName = docName;
			this.firNurName = firNurName;
			this.secNurName = secNurName;
			this.creatTime = creatTime;
			this.adviceDoc = adviceDoc;
			this.adviceContent = adviceContent;
			this.performTime = performTime;
			this.performNurName = performNurName;
			this.temperatureBefor = temperatureBefor;
			this.pulseBefor = pulseBefor;
			this.breathBefor = breathBefor;
			this.pressureBefor = pressureBefor;
			this.temperatureAfter = temperatureAfter;
			this.pulseAfter = pulseAfter;
			this.breathAfter = breathAfter;
			this.pressureAfter = pressureAfter;
		}

		public String getSurgeryTime() {
			return surgeryTime;
		}

		public void setSurgeryTime(String surgeryTime) {
			this.surgeryTime = surgeryTime;
		}

		public String getSurgeryName() {
			return surgeryName;
		}

		public void setSurgeryName(String surgeryName) {
			this.surgeryName = surgeryName;
		}

		public String getSurgeryPart() {
			return surgeryPart;
		}

		public void setSurgeryPart(String surgeryPart) {
			this.surgeryPart = surgeryPart;
		}

		public String getDocName() {
			return DocName;
		}

		public void setDocName(String docName) {
			DocName = docName;
		}

		public String getFirNurName() {
			return firNurName;
		}

		public void setFirNurName(String firNurName) {
			this.firNurName = firNurName;
		}

		public String getSecNurName() {
			return secNurName;
		}

		public void setSecNurName(String secNurName) {
			this.secNurName = secNurName;
		}

		public String getCreatTime() {
			return creatTime;
		}

		public void setCreatTime(String creatTime) {
			this.creatTime = creatTime;
		}

		public String getAdviceDoc() {
			return adviceDoc;
		}

		public void setAdviceDoc(String adviceDoc) {
			this.adviceDoc = adviceDoc;
		}

		public String getAdviceContent() {
			return adviceContent;
		}

		public void setAdviceContent(String adviceContent) {
			this.adviceContent = adviceContent;
		}

		public String getPerformTime() {
			return performTime;
		}

		public void setPerformTime(String performTime) {
			this.performTime = performTime;
		}

		public String getPerformNurName() {
			return performNurName;
		}

		public void setPerformNurName(String performNurName) {
			this.performNurName = performNurName;
		}

		public String getTemperatureBefor() {
			return temperatureBefor;
		}

		public void setTemperatureBefor(String temperatureBefor) {
			this.temperatureBefor = temperatureBefor;
		}

		public String getPulseBefor() {
			return pulseBefor;
		}

		public void setPulseBefor(String pulseBefor) {
			this.pulseBefor = pulseBefor;
		}

		public String getBreathBefor() {
			return breathBefor;
		}

		public void setBreathBefor(String breathBefor) {
			this.breathBefor = breathBefor;
		}

		public String getPressureBefor() {
			return pressureBefor;
		}

		public void setPressureBefor(String pressureBefor) {
			this.pressureBefor = pressureBefor;
		}

		public String getTemperatureAfter() {
			return temperatureAfter;
		}

		public void setTemperatureAfter(String temperatureAfter) {
			this.temperatureAfter = temperatureAfter;
		}

		public String getPulseAfter() {
			return pulseAfter;
		}

		public void setPulseAfter(String pulseAfter) {
			this.pulseAfter = pulseAfter;
		}

		public String getBreathAfter() {
			return breathAfter;
		}

		public void setBreathAfter(String breathAfter) {
			this.breathAfter = breathAfter;
		}

		public String getPressureAfter() {
			return pressureAfter;
		}

		public void setPressureAfter(String pressureAfter) {
			this.pressureAfter = pressureAfter;
		}
		
		
	}

}