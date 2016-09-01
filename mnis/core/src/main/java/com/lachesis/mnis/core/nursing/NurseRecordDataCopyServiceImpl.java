package com.lachesis.mnis.core.nursing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.bodysign.BodySignConstants;
import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.bodysign.repository.BodySignRepository;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.PatientSkinTest;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

/**
 * 不再使用体温单转抄到护理记录功能，改成在体温单里作转抄标记，护理模块查询转抄标记后生成文书
 * @author haobo.xu
 *
 */
@Deprecated
//@Service("nurseRecordDataCopyService")
public class NurseRecordDataCopyServiceImpl implements NurseRecordDataCopyService {
	private Logger LOGGER = LoggerFactory.getLogger(NurseRecordDataCopyServiceImpl.class);
	
	@Autowired
	private BodySignRepository bodySignRepository;
	@Autowired
	private NurseRecordRepository nurseRecordRepository;
	
	@Override
	public void transBodysignForNurseRecord(int masterId, String time,
			String emplCode, String loginName) {
		BodySignRecord bodySignRecord = bodySignRepository.getById(masterId);
		if (bodySignRecord == null) {
			return;
		}
		StringBuffer contentSb = new StringBuffer();
		processBodySignRecordForNurseRecord(bodySignRecord, contentSb);

		int downTempStatus = bodySignRecord.getCooled();
		List<NurseRecordSpecItem> specItems = new ArrayList<NurseRecordSpecItem>();
		NurseRecordSpecItem specItem = new NurseRecordSpecItem();
		specItem.setItemCode("bqgcjcs");
		specItem.setItemName("病情观察及措施");
		if (StringUtils.isNotEmpty(contentSb.toString())) {
			specItem.setItemValue(contentSb.toString());
			specItems.add(specItem);
		}
		StringBuffer timeSb = new StringBuffer();
		timeSb.append(DateUtil.format(DateFormat.YMD) + " ");
		timeSb.append(time);
		
		NurseRecord nurseRecord = new NurseRecord();
		nurseRecord.setBodySignItemList(bodySignRecord.getBodySignItemList());
		nurseRecord.setNurseRecordSpecItemList(specItems);
		nurseRecord.setRecordDate(DateUtil.parse(timeSb.toString(), DateFormat.FULL));
		nurseRecord.setRecordNurseCode(emplCode);
		nurseRecord.setRecordNurseName(loginName);
		nurseRecord.setPatientId(bodySignRecord.getPatientId());
		nurseRecord.setCooled(downTempStatus);
		if (nurseRecordRepository.save(nurseRecord) <= 0) {
			LOGGER.error("保存失败：传入的护理记录有误！");
			return;
		}
		
	}

	/**
	 * 为转抄护理记录构建体征明细
	 * 
	 * @param item
	 * @param contentStr
	 * @return
	 */
	private void processBodySignRecordForNurseRecord(BodySignRecord item, StringBuffer contentStr) {
		List<BodySignItem> items = item.getBodySignItemList();
		if (items == null || items.isEmpty()) {
			return;
		}

		for (BodySignItem bodySignItem : items) {
			String itemCode = bodySignItem.getItemCode();
			if (StringUtils.isNotEmpty(itemCode)
					&& !itemCode.equals(BodySignConstants.TEMPERATURE)
					&& !itemCode.equals(BodySignConstants.PULSE)
					&& !itemCode.equals(BodySignConstants.BREATH)
					&& !itemCode.equals(BodySignConstants.BLOODPRESS)) {
				contentStr.append(bodySignItem.getItemName() + " ");
				contentStr.append(BodySignConstants.MAP_BODYSIGN_ITEM
						.get(bodySignItem.getMeasureNoteCode()) + " ");
				contentStr.append(bodySignItem.getItemValue()
						+ bodySignItem.getItemUnit() + " ");
			}
		}
		
		PatientSkinTest skinInfo = item.getSkinTestInfo();
		if (skinInfo != null && StringUtils.isNotEmpty(skinInfo.getDrugName())) {
			contentStr.append(skinInfo.getDrugName() + " ");
			contentStr.append(BodySignConstants.MAP_BODYSIGN_ITEM.get(skinInfo.getTestResult()) + " ");
		}

		PatientEvent eventInfo = item.getEvent();
		if (eventInfo != null && eventInfo.isValid()) {
			contentStr.append(eventInfo.getEventName() + " ");
		//	contentStr.append(eventInfo.getRecordDate());
		}
	}
}
