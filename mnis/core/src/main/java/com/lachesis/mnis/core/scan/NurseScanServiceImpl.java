package com.lachesis.mnis.core.scan;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.NurseScanService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.event.MnisThreadPoolTaskExecutor;
import com.lachesis.mnis.core.scan.entity.NurseScanInfo;
import com.lachesis.mnis.core.scan.repository.NurseScanRepository;

/**
 * 处理异常扫描
 * @author ThinkPad
 *
 */
@Service
public class NurseScanServiceImpl implements NurseScanService {
	private static Logger LOOGER = LoggerFactory
			.getLogger(NurseScanServiceImpl.class);

	@Autowired
	private MnisThreadPoolTaskExecutor mnisThreadPoolTaskExecutor;

	@Autowired
	private NurseScanRepository nurseScanRepository;

	@Override
	public int saveNurseScan(NurseScanInfo nurseScanInfo) {
		return nurseScanRepository.insertNurseScan(nurseScanInfo);
	}

	/**
	 * 处理核对扫描记录
	 * 
	 * @param barcode
	 * @param patId
	 * @param deptCode
	 * @param error
	 * @param nurseCode
	 * @param nurseName
	 * @param type
	 * @param status
	 */
	@Override
	public void handleNurseScan(final String barcode, final String patId,
			final String deptCode, final String error, final String nurseCode,
			final String nurseName, final String type, final int status) {
		mnisThreadPoolTaskExecutor.execute(new Runnable() {

			@Override
			public void run() {
				NurseScanInfo nurseScanInfo = new NurseScanInfo();
				nurseScanInfo.setBarcode(barcode);
				nurseScanInfo.setPatId(patId);
				nurseScanInfo.setDeptCode(deptCode);
				nurseScanInfo.setNurseCode(nurseCode);
				nurseScanInfo.setNurseName(nurseName);
				nurseScanInfo.setStatus(status);
				nurseScanInfo.setScanDate(new Date());
				try {
					if (StringUtils.isNotBlank(error)) {
						String[] errors = error.split(MnisConstants.LINE);
						if (errors.length > 2) {
							nurseScanInfo.setError(errors[2]);
							nurseScanInfo.setType(errors[0]);
							nurseScanInfo.setErrorType(errors[1]);
						} else {
							nurseScanInfo.setError(error);
						}

					} else {
						nurseScanInfo.setType(type);
					}

					saveNurseScan(nurseScanInfo);
				} catch (Exception e) {
					LOOGER.error("NurseScanServiceImpl hanldNurseScan error:"
							+ e.toString(),e);
				}

			}
		});
	}
}
