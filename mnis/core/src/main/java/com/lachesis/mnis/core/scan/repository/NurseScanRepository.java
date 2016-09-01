package com.lachesis.mnis.core.scan.repository;

import com.lachesis.mnis.core.scan.entity.NurseScanInfo;

public interface NurseScanRepository {
	int insertNurseScan(NurseScanInfo nurseScanInfo);
}
