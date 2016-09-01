package com.lachesis.mnisqm.module.schedule.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.utils.DateUtils;
import com.lachesis.mnisqm.core.utils.DateUtils.Menu;
import com.lachesis.mnisqm.module.schedule.constants.ScheduleConstants;
import com.lachesis.mnisqm.module.schedule.domain.Schedule;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleCount;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleDetail;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleLeave;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleRecord;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleRecordDetail;
import com.lachesis.mnisqm.module.schedule.domain.ScheduleRule;
import com.lachesis.mnisqm.module.system.domain.SysDate;
import com.lachesis.mnisqm.module.user.domain.ComDeptNurse;

public class ScheduleUtil {
	
	/**
	 * 获取当前日期所在周和指点前后week周的日期列表
	 * @param week
	 */
	public static List<SysDate> getWeekDateList(int week){
		Calendar calendar = Calendar.getInstance();
		int nowWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.add(Calendar.DATE, (week-nowWeek)*7);//日期推week周
		//获取当天是本周的第几天
		int day = calendar.get(Calendar.DAY_OF_WEEK)-2;
		day = day==-1?6:day;
		//获取本周第一天，以周一为第一天
		calendar.add(Calendar.DATE, -day);
		List<SysDate> dateList = new ArrayList<SysDate>();
		for(int i=1;i<=7;i++){
			SysDate date = new SysDate();
			/*String weekDate = DateUtils.format(calendar.getTime(), DateUtils.YMD);
			if(!StringUtils.isEmpty(weekDate)){
				date.setDate(weekDate.substring(4,5));
			}*/
			date.setDate(DateUtils.format(calendar.getTime(), DateUtils.YMD));
			date.setWeekDay(Menu.getName(i));
			dateList.add(date);
			//加一天
			calendar.add(Calendar.DATE, 1);
		}
		return dateList;
	}
	
	/**
	 * 初始化排班表头
	 */
	public static Schedule initSchedule(List<SysDate> dateList,int weeks ){
		Schedule schedule = new Schedule();
		schedule.setStartDate(dateList.get(0).getDate());
		schedule.setEndDate(dateList.get(6).getDate());
		schedule.setWeek1(dateList.get(0).getDate()+" "+ dateList.get(0).getWeekDay());
		schedule.setWeek2(dateList.get(1).getDate()+" "+ dateList.get(1).getWeekDay());
		schedule.setWeek3(dateList.get(2).getDate()+" "+ dateList.get(2).getWeekDay());
		schedule.setWeek4(dateList.get(3).getDate()+" "+ dateList.get(3).getWeekDay());
		schedule.setWeek5(dateList.get(4).getDate()+" "+ dateList.get(4).getWeekDay());
		schedule.setWeek6(dateList.get(5).getDate()+" "+ dateList.get(5).getWeekDay());
		schedule.setWeek7(dateList.get(6).getDate()+" "+ dateList.get(6).getWeekDay());
		schedule.setWeeks(weeks);//周数
		return schedule;
	}
	
	/**
	 * 获取每天的排班班次
	 * @param ruleMap:排班规则
	 * @param dateList：排班日期
	 * @return
	 */
	public static Map<String,List<ScheduleRecordDetail>> getScheduleRecordDetail(Map<String,List<ScheduleRule>> ruleMap,List<SysDate> dateList){
		//数据返回
		Map<String,List<ScheduleRecordDetail>> rsMap = new HashMap<String,List<ScheduleRecordDetail>>();
		for(int i=0;i<dateList.size();i++){
			List<ScheduleRecordDetail> dtl = new ArrayList<ScheduleRecordDetail>();
			SysDate date = dateList.get(i);
			if(i<5){
				//工作日排班
				List<ScheduleRule> ruleList = ruleMap.get("01");
				if(ruleList !=null){
					for(ScheduleRule rule:ruleList){
						int useCount = Integer.parseInt(rule.getUserCount());
						for(int j=0;j<useCount;j++){
							ScheduleRecordDetail detail = new ScheduleRecordDetail();
							detail.setRecordDate(date.getDate());
							detail.setClassCode(rule.getClassCode());
							detail.setStatus(MnisQmConstants.STATUS_YX);
							dtl.add(detail);
						}
					}
				}
			}else{
				//周末排班
				List<ScheduleRule> ruleList = ruleMap.get("02");
				if(ruleList !=null){
					for(ScheduleRule rule:ruleList){
						int useCount = Integer.parseInt(rule.getUserCount());
						for(int j=0;j<useCount;j++){
							ScheduleRecordDetail detail = new ScheduleRecordDetail();
							detail.setRecordDate(date.getDate());
							detail.setClassCode(rule.getClassCode());
							detail.setStatus(MnisQmConstants.STATUS_YX);
							dtl.add(detail);
						}
					}
				}
			}
			rsMap.put(date.getDate(), dtl);
		}
		return rsMap;
	}
	
	/**
	 * 获取排班明细
	 * @param details
	 * @param userList
	 * @return
	 */
	public static List<ScheduleDetail> getScheduleDetail(Map<String,List<ScheduleRecordDetail>> details,List<ComDeptNurse> userList){
		List<ScheduleRecordDetail> rsDetails = new ArrayList<ScheduleRecordDetail>();
		int userIndex = 0;//当前序号
		for(Iterator<String> iter = details.keySet().iterator();iter.hasNext();){
			String recordDate = iter.next();
			List<ScheduleRecordDetail> dtls = details.get(recordDate);
			for(ScheduleRecordDetail detail: dtls){
				ComDeptNurse user = userList.get(userIndex);
				detail.setUserCode(user.getUserCode());
				detail.setUserName(user.getUserName());
				detail.setColor(user.getColor());
				detail.setClinical(user.getClinical());
				detail.setGroupName(user.getGroupName());
				detail.setBeds(user.getBeds());
				if(user.getLeave()!=null){
					detail.setUserLevel(user.getLeave().toString());
				}
				userIndex ++;
				//如果所有人都已经排班过，那么从第一个开始继续排班
				if(userIndex==userList.size()){
					userIndex = 0;
				}
				rsDetails.add(detail);
			}
			//如果存在的有人当天没分配，那么给他分配一个空的班次
			if(dtls.size()<userList.size()){
				int free = userList.size() - dtls.size();
				for(int i= 0;i < free;i++){
					//获取当前已经排班到哪个人
					int freeIndex = userIndex + i;
					if(freeIndex >= userList.size()){
						freeIndex = freeIndex - userList.size();
					}

					ScheduleRecordDetail detail = new ScheduleRecordDetail();
					detail.setUserCode(userList.get(freeIndex).getUserCode());
					detail.setUserName(userList.get(freeIndex).getUserName());
					detail.setColor(userList.get(freeIndex).getColor());
					detail.setRecordDate(recordDate);
					detail.setClinical(userList.get(freeIndex).getClinical());
					detail.setGroupName(userList.get(freeIndex).getGroupName());
					if(userList.get(freeIndex).getLeave() !=null){
						//积假
						detail.setUserLevel(userList.get(freeIndex).getLeave().toString());
					}
					detail.setBeds(userList.get(freeIndex).getBeds());
					rsDetails.add(detail);
				}
			}
		}
		
		return parseScheduleDetail(rsDetails);
	}
	
	/**
	 * 转换为页面显示的数据
	 * @param recordDtls
	 * @return
	 */
	public static List<ScheduleDetail> parseScheduleDetail(List<ScheduleRecordDetail> recordDtls){
		//把数据根据人分类
		Map<String,ScheduleDetail> dtlMap = new HashMap<String,ScheduleDetail>();
		for(ScheduleRecordDetail recordDtl : recordDtls){
			String userCode = recordDtl.getUserCode();//员工编号
			String hisCode = recordDtl.getHisCode();//医院员工编号
			String recordDate = recordDtl.getRecordDate();//记录日期
			String color = recordDtl.getColor();//颜色
			ScheduleDetail schedule = dtlMap.get(userCode);
			if(schedule == null){
				schedule = new ScheduleDetail();
				schedule.setUserCode(userCode);
				schedule.setHisCode(hisCode);
				schedule.setColor(color);
				schedule.setGroupName(recordDtl.getGroupName());
				schedule.setClinical(recordDtl.getClinical());
				schedule.setUserName(recordDtl.getUserName());
				schedule.setLeave(recordDtl.getUserLevel());
				schedule.setBeds(recordDtl.getBeds());
			}
			//获取日期所在周的天数
			Calendar c = Calendar.getInstance();
			c.setTime(DateUtils.parseDate(recordDate,DateUtils.YMD));
			//日历中，星期天是第一天，所有稍作转换，1为星期天
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			switch (dayOfWeek) {
			case 1 :
				schedule.getWeek7().add(recordDtl);//星期天
				break;
			case 2:
				schedule.getWeek1().add(recordDtl);
				break;
			case 3:
				schedule.getWeek2().add(recordDtl);
				break;
			case 4:
				schedule.getWeek3().add(recordDtl);
				break;
			case 5:
				schedule.getWeek4().add(recordDtl);
				break;
			case 6:
				schedule.getWeek5().add(recordDtl);
				break;
			case 7:
				schedule.getWeek6().add(recordDtl);
				break;
			default:
				break;
			}
			dtlMap.put(userCode, schedule);
		}
		List<ScheduleDetail> dtls = new ArrayList<ScheduleDetail>();
		dtls.addAll(dtlMap.values());
		return dtls ;
	}
	

	/**
	 * 主记录数据转换，转换为页面显示的数据
	 * @param record
	 * @return
	 */
	public static Schedule parseSchedule(ScheduleRecord record){
		Schedule schedule = new Schedule();
		schedule.setDeptCode(record.getDeptCode());//科室编号
		schedule.setDeptName(record.getDeptName());//科室名称
		schedule.setRecordCode(record.getRecordCode());//记录编号
		schedule.setStartDate(record.getStartDate());//其实日期
		schedule.setEndDate(record.getEndDate());//结束日期
		schedule.setWeeks(record.getWeeks());//周数
		schedule.setSeqId(record.getSeqId());
		schedule.setWeek1(record.getWeek1());
		schedule.setWeek2(record.getWeek2());
		schedule.setWeek3(record.getWeek3());
		schedule.setWeek4(record.getWeek4());
		schedule.setWeek5(record.getWeek5());
		schedule.setWeek6(record.getWeek6());
		schedule.setWeek7(record.getWeek7());
		return schedule;
	}
	
	/**
	 * 把请假信息转换为前端使用
	 * @param count
	 * @param leave
	 */
	public static ScheduleCount copyToLeaveCount(ScheduleCount count,ScheduleLeave leave){
		String leaveType = leave.getLeaveType();//请假类型
		switch (leaveType) {
		case ScheduleConstants.txj:	//调休假
			count.setTxj(String.valueOf(leave.getDays()));//天数
			break;
		case ScheduleConstants.shj:	//事假
			count.setShj(String.valueOf(leave.getDays()));//天数
			break;
		case ScheduleConstants.nj:	//年假
			count.setNj(String.valueOf(leave.getDays()));//天数
			break;
		case ScheduleConstants.bj:	//病假
			count.setBj(String.valueOf(leave.getDays()));//天数
			break;
		case ScheduleConstants.cj:	//产假
			count.setCj(String.valueOf(leave.getDays()));//天数
			break;
		case ScheduleConstants.sj:	//丧假
			count.setSj(String.valueOf(leave.getDays()));//天数
			break;
		case ScheduleConstants.hj:	//婚假
			count.setHj(String.valueOf(leave.getDays()));//天数
			break;
		}
		return count;
	}
	
	/**
	 * 计算积假
	 * @return
	 */
	public static Map<String,ComDeptNurse> countLeave(List<ComDeptNurse> oldNurse,List<ComDeptNurse> newNurse){
		if((oldNurse == null || oldNurse.isEmpty())&&(newNurse == null || newNurse.isEmpty())){
			return null;
		}
		Map<String,ComDeptNurse> rsMap = new HashMap<String,ComDeptNurse>();
		//获取保存前的积假
		Map<String,ComDeptNurse> oldMap = new HashMap<String,ComDeptNurse>();
		if(oldNurse != null && !oldNurse.isEmpty()){
			for(ComDeptNurse nurse:oldNurse){
				oldMap.put(nurse.getUserCode(), nurse);
			}
		}
		//计算积假
		if(newNurse != null && !newNurse.isEmpty()){
			for(ComDeptNurse nurse:newNurse){
				String userCode = nurse.getUserCode();
				if(oldMap.containsKey(userCode)){
					//计算积假
					BigDecimal oleave = oldMap.get(userCode).getLeave();
					oleave = oleave==null?BigDecimal.ZERO:oleave;
					BigDecimal nleave = nurse.getLeave();
					nleave = nleave==null?BigDecimal.ZERO:nleave;
					BigDecimal leave = nleave.subtract(oleave);
					nurse.setLeave(leave);
					//删除已经计算过的
					oldMap.remove(userCode);
				}
				rsMap.put(nurse.getUserCode(), nurse);
			}
		}
		if(!oldMap.isEmpty()){
			for(Iterator<String> iter = oldMap.keySet().iterator();iter.hasNext();){
				String userCode = iter.next();
				//计算积假
				ComDeptNurse nurse = oldMap.get(userCode);
				BigDecimal oleave = nurse.getLeave();
				oleave = oleave==null?BigDecimal.ZERO:oleave;
				BigDecimal leave = BigDecimal.ZERO.subtract(oleave);
				nurse.setLeave(leave);
				rsMap.put(userCode, nurse);
			}
		}
		return rsMap;
	}
	
	public static void main(String[] args){
		//getWeekDateList(0);
		Calendar c = Calendar.getInstance();
		System.out.println(c.get(Calendar.WEEK_OF_YEAR));
		c.add(Calendar.DATE, 4);
		System.out.println(c.get(Calendar.DAY_OF_WEEK));
	}
}