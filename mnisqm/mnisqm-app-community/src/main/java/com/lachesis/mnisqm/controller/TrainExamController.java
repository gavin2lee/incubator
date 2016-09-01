package com.lachesis.mnisqm.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnisqm.BaseDataVo;
import com.lachesis.mnisqm.common.WebContextUtils;
import com.lachesis.mnisqm.configuration.ExcelHeadInfo;
import com.lachesis.mnisqm.constants.Constants;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.ExcelUtil;
import com.lachesis.mnisqm.core.utils.StringUtils;
import com.lachesis.mnisqm.module.system.domain.SysUser;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemAttendanceManage;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemExamManage;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemPerformanceManage;
import com.lachesis.mnisqm.module.trainExamManage.domain.TemTrainManage;
import com.lachesis.mnisqm.module.trainExamManage.service.ITrainExamManageService;

@Controller
@RequestMapping("/trainExam")
public class TrainExamController {
	
	@Autowired
	ITrainExamManageService trainExamManageService;
	
	/**
	 * 保存培训管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/saveTemTrainManage")
	public @ResponseBody BaseDataVo saveTemTrainManage( @RequestBody TemTrainManage temTrainManage){
		BaseDataVo outVo = new BaseDataVo();
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		temTrainManage.setCreatePerson(user.getUserCode());//创建人
		temTrainManage.setCreateTime(new Date());//创建时间
		trainExamManageService.insertTemTrainManage(temTrainManage);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新培训管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/updateTemTrainManage")
	public @ResponseBody BaseDataVo updateTemTrainManage( @RequestBody TemTrainManage temTrainManage){
		BaseDataVo outVo = new BaseDataVo();
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		temTrainManage.setUpdatePerson((user.getUserCode()));//创建人
		temTrainManage.setUpdateTime(new Date());//创建时间
		trainExamManageService.updateTemTrainManage(temTrainManage);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除培训管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/deleteTemTrainManage")
	public @ResponseBody BaseDataVo deleteTemTrainManage(String seqId){
		BaseDataVo outVo = new BaseDataVo();
		trainExamManageService.deleteTemTrainManage(Long.parseLong(seqId));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存出勤管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/saveTemAttendanceManage")
	public @ResponseBody BaseDataVo saveTemAttendanceManage( @RequestBody TemAttendanceManage temAttendanceManage){
		BaseDataVo outVo = new BaseDataVo();
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		temAttendanceManage.setCreatePerson(user.getUserCode());//创建人
		temAttendanceManage.setCreateTime(new Date());//创建时间
		trainExamManageService.insertTemAttendanceManage(temAttendanceManage);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新出勤管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/updateTemAttendanceManage")
	public @ResponseBody BaseDataVo updateTemAttendanceManage( @RequestBody TemAttendanceManage temAttendanceManage){
		BaseDataVo outVo = new BaseDataVo();
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		temAttendanceManage.setUpdatePerson((user.getUserCode()));//创建人
		temAttendanceManage.setUpdateTime(new Date());//创建时间
		trainExamManageService.updateTemAttendanceManage(temAttendanceManage);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除出勤管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/deleteTemAttendanceManage")
	public @ResponseBody BaseDataVo deleteTemAttendanceManage(String seqId){
		BaseDataVo outVo = new BaseDataVo();
		trainExamManageService.deleteTemAttendanceManage(Long.parseLong(seqId));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存考试管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/saveTemExamManage")
	public @ResponseBody BaseDataVo saveTemExamManage( @RequestBody TemExamManage temExamManage){
		BaseDataVo outVo = new BaseDataVo();
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		temExamManage.setCreatePerson(user.getUserCode());//创建人
		temExamManage.setCreateTime(new Date());//创建时间
		trainExamManageService.insertTemExamManage(temExamManage);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新出勤管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/updateTemExamManage")
	public @ResponseBody BaseDataVo updateTemExamManage( @RequestBody TemExamManage temExamManage){
		BaseDataVo outVo = new BaseDataVo();
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		temExamManage.setUpdatePerson((user.getUserCode()));//创建人
		temExamManage.setUpdateTime(new Date());//创建时间
		trainExamManageService.updateTemExamManage(temExamManage);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除出勤管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/deleteTemExamManage")
	public @ResponseBody BaseDataVo deleteTemExamManage(String seqId){
		BaseDataVo outVo = new BaseDataVo();
		trainExamManageService.deleteTemExamManage(Long.parseLong(seqId));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 保存考试管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/saveTemPerformanceManage")
	public @ResponseBody BaseDataVo saveTemPerformanceManage( @RequestBody TemPerformanceManage temPerformanceManage){
		BaseDataVo outVo = new BaseDataVo();
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		temPerformanceManage.setCreatePerson(user.getUserCode());//创建人
		temPerformanceManage.setCreateTime(new Date());//创建时间
		trainExamManageService.insertTemPerformanceManage(temPerformanceManage);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 更新出勤管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/updateTemPerformanceManage")
	public @ResponseBody BaseDataVo updateTemPerformanceManage( @RequestBody TemPerformanceManage temPerformanceManage){
		BaseDataVo outVo = new BaseDataVo();
		//获取登录的用户信息
		SysUser user = WebContextUtils.getSessionUserInfo();
		temPerformanceManage.setUpdatePerson((user.getUserCode()));//创建人
		temPerformanceManage.setUpdateTime(new Date());//创建时间
		trainExamManageService.updateTemPerformanceManage(temPerformanceManage);
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 删除出勤管理信息
	 * @param temTrainManage
	 * @return
	 */
	@RequestMapping(value="/deleteTemPerformanceManage")
	public @ResponseBody BaseDataVo deleteTemPerformanceManage(String seqId){
		BaseDataVo outVo = new BaseDataVo();
		trainExamManageService.deleteTemPerformanceManage(Long.parseLong(seqId));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 根据条件查询培训信息
	 * @param beginTime 开始日期
	 * @param deptCode 科室编号
	 * @param courseName 培训标题
	 * @return
	 */
	@RequestMapping(value="/queryTemTrainManageByTDC")
	public @ResponseBody BaseDataVo queryTemTrainManageByTDC(String beginTime, String deptCode, String courseName){
		BaseDataVo outVo = new BaseDataVo();
		if(StringUtils.isEmpty(beginTime)){
			throw new CommRuntimeException("请填写日期!");
		}
		outVo.setData(trainExamManageService.queryTemTrainManageByTDC(beginTime, deptCode, courseName));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 根据条件查询考试信息
	 * @param examTime 考试日期
	 * @param deptCode 部门编号
	 * @param examName 考试名称
	 * @param userCode 参与考试人编号
	 * @param userName 参与考试人姓名
	 * @return
	 */
	@RequestMapping(value="/queryTemExamManageByTOrDCOrENOrUCOrUN")
	public @ResponseBody BaseDataVo queryTemExamManageByTOrDCOrENOrUCOrUN(String examTime, String deptCode, String examName, String userCode,String userName) {
		BaseDataVo outVo = new BaseDataVo();
		if(StringUtils.isEmpty(examTime)){
			throw new CommRuntimeException("请填写日期!");
		}
		outVo.setData(trainExamManageService.queryTemExamManageByTOrDCOrENOrUCOrUN(examTime, deptCode, examName, userCode, userName));
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	@RequestMapping(value="/exportExcelTest")
	public void exportExcelTest(String beginTime, String deptCode, String courseName, HttpServletResponse response){
		if(StringUtils.isEmpty(beginTime)){
			throw new CommRuntimeException("请填写日期!");
		}
		List<TemTrainManage> temTrainManageList = trainExamManageService.queryTemTrainManageByTDC(beginTime, deptCode, courseName);
		List<TemAttendanceManage> list = temTrainManageList.get(0).getTemAttendanceManageList();
		ExcelUtil<TemAttendanceManage> e = new ExcelUtil<TemAttendanceManage>();
		String[] headers = ExcelHeadInfo.temTrainManage;
		HSSFWorkbook wb = e.exportExcel("测试", headers, list, "yyyy-MM-dd");
		response.setContentType("application/vnd.ms-excel");    
        response.setHeader("Content-disposition", "attachment;filename=TemAttendanceManage.xls");    
        OutputStream ouputStream;
		try {
			ouputStream = response.getOutputStream();
			wb.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    
            
	}
	
	/*@RequestMapping(value="/importExcelTest")
	public void importExcelTest(){
		try {
            ExcelUtil excelReader = new ExcelUtil();
            // 对读取Excel表格内容测试
            InputStream is = new FileInputStream("E:\\a.xls");
            
            Student student = new Student();
            Map<String, Object> map  = excelReader.readExcelContent(is, student, "com.lachesis.mnisqm.module.trainExamManage.domain.Student");
            long beginTime = System.currentTimeMillis();
            for (int i = 1; i <= map.size(); i++) {
            	student = (Student) map.get(i+"");
            	System.out.println(student.toString());
            }
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-beginTime);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
            
	}*/
	
	/**
	 * 获取护士层级培训信息（北大）
	 * @return
	 */
	@RequestMapping(value="/queryNurseLevelTraining")
	public @ResponseBody BaseDataVo queryNurseLevelTraining() {
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(trainExamManageService.queryNurseLevelTraining());
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
	
	/**
	 * 获取继续教育信息（北大）
	 * @return
	 */
	@RequestMapping(value="/queryContinuingEducation")
	public @ResponseBody BaseDataVo queryContinuingEducation() {
		BaseDataVo outVo = new BaseDataVo();
		outVo.setData(trainExamManageService.queryContinuingEducation());
		//数据返回
		outVo.setCode(Constants.Success);
		return outVo;
	}
}
