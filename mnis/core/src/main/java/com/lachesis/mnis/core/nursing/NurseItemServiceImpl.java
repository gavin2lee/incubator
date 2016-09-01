package com.lachesis.mnis.core.nursing;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.util.DateUtil;

@Service("nurseItemService")
public class NurseItemServiceImpl implements NurseItemService {
	@Autowired
	private NurseItemRepository nurseItemRepository;

	@Override
	public List<NurseItemCategory> getNurseItemConfig(String deptCode) {
		return nurseItemRepository.selectNurseItemCategory(deptCode);
	}

	@Override
	public int saveNurseItemRecord(NurseItemRecord nurseItemRecord) {
		if(nurseItemRecord != null ) {
			return nurseItemRepository.insertNurseItemRecord( nurseItemRecord );
		}
		return -1;
	}

	@Override
	public List<NurseItemRecord> getNurseItemRecords(String patientId,
			String day) throws ParseException {
		List<NurseItemRecord> nrItemList = null; 
		if(patientId != null) {
			String[] timeEndPoints = DateUtil.getTimeEndPoints(day);
			if(timeEndPoints!=null && timeEndPoints.length>1) {
				nrItemList = new ArrayList<NurseItemRecord>();
				List<NurseItemRecord> niEntityList = 
						nurseItemRepository.selectNurseItemRecords(patientId, timeEndPoints[0], timeEndPoints[1]);
				for(NurseItemRecord niEntity:niEntityList) {
					nrItemList.add( niEntity );
				}
			}		
		}
		return nrItemList;
	}

}
