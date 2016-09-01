package com.lachesis.mnis.core;

import com.lachesis.mnis.core.scan.entity.NurseScanInfo;

public interface NurseScanService {
	int saveNurseScan(NurseScanInfo nurseScanInfo) throws Exception;
	
	void handleNurseScan(final String barcode, final String patId,
			final String deptCode, final String error, final String nurseCode,
			final String nurseName, final String type, final int status);
}
