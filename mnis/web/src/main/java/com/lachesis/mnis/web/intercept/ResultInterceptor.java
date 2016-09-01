package com.lachesis.mnis.web.intercept;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lachesis.mnis.core.InspectionService;
import com.lachesis.mnis.core.LabTestService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;

/***
 * 
 * 医嘱接口拦截以格式化结果中时间格式
 *
 * @author yuliang.xu
 * @date 2015年6月2日 下午1:40:32 
 *
 */
@Aspect
@Component
public class ResultInterceptor {

	@Autowired
	private LabTestService labTestService;
	@Autowired
	private InspectionService inspectionSypacsService;


	@SuppressWarnings("unchecked")
	@Around("execution(com.lachesis.mnis.web.common.vo.BaseMapVo  com.lachesis.mnis.web.LabTestRecordAction.getLabTestRecordsForNda(java.lang.String,..))")
	public Object formatLabTestRecordTime(final ProceedingJoinPoint joinPoint)
			throws Throwable {
		BaseMapVo vo = (BaseMapVo) joinPoint.proceed();
		List<LabTestRecord> result = (List<LabTestRecord>) vo.getData().get(
				BaseVo.VO_KEY);
		if (vo.getRslt().equals(MnisConstants.ACKRESULT_SUCCESS)) {
			labTestService.formatLabTestRecordTime(result);
		}
		return vo;
	}

/*	@SuppressWarnings("unchecked")
	@Around("execution(com.lachesis.mnis.web.common.vo.BaseMapVo com.lachesis.mnis.web.InspectionReportAction.getInspectionRecordsForNda(java.lang.String,..))")
	public Object formatInspectRecordTime(final ProceedingJoinPoint joinPoint)
			throws Throwable {
		BaseMapVo vo = (BaseMapVo) joinPoint.proceed();
		List<InspectionRecord> result = (List<InspectionRecord>) vo.getData()
				.get(BaseVo.VO_KEY);
		return vo;
	}*/

}
