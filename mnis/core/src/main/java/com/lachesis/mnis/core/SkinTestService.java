package com.lachesis.mnis.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.skintest.entity.SkinTestInfoLx;
import com.lachesis.mnis.core.skintest.entity.SkinTestItem;


/**
 * 皮试相关接口
 * @author liangming.deng
 *
 */
public interface SkinTestService {
	/**
	 * 获取皮试信息(当前日期，护士Id，护士部门，病人Id)
	 * @param paramMap
	 * @return
	 */
	List<SkinTestInfoLx> getSkinTestInfos(String patId,String userId,String deptId,String selectDate);
	
	/**
	 * 执行皮试信息
	 * @param stId
	 * @param execNurseId
	 * @param execNurseName
	 * @param stBeforeImg
	 * @return
	 */
	int execSkinTestInfo(SkinTestItem skinTestItem,String execNurseId,String execNurseName,String stBeforeImg);
	
	/**
	 * 确认核对
	 * @param stId
	 * @param approveNuserId
	 * @param approveNurseName
	 * @param inputNurseId
	 * @param inputNurseName
	 * @param stDrugBatchNo
	 * @param stResult
	 * @param stAfterImg
	 * @return
	 */
	int approveSkinTestInfo(SkinTestItem skinTestItem,String stAfterImg,UserInformation sessionUserInfo,String approveUserLoginName,String approveNursePwd);
	/**
	 * 插入皮试相关信息
	 * @param skinTestItem
	 * @return
	 */
	int saveSkinTestItem(SkinTestItem skinTestItem);
	
	/**
	 * 修改皮试信息
	 * @param skinTestItem
	 * @return
	 */
	int updateSkinTestItem(SkinTestItem skinTestItem);
	
	/**
	 * 修改图片信息
	 * @param skinTestItem
	 * @return
	 */
	void updateSkinTestImg(String patId,String stItemId,String imageString,int imageType);
	
	/**
	 * 根据皮试Id获取皮试信息
	 * @param skItemId
	 * @return
	 */
	SkinTestItem getSkinTestItemByStItemId(String stItemId);
	
	/**
	 * 根据皮试Id,皮试图片标示，返回标示对应的图片
	 * @param stItemId
	 * @param stItemImgExistFlag
	 * @return
	 */
	byte[] getSkinTestItemImg(String stItemId,String stItemImgExistFlag);
	
	List<SkinTestInfoLx> getPublishSkinTests(String deptCode,Date startTime,Date endTime);
}
