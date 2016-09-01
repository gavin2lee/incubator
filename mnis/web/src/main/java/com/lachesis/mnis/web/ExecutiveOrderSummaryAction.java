package com.lachesis.mnis.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.old.doc.ExecutiveOrderSummaryAndItem;
import com.lachesis.mnis.core.old.doc.ExecutiveOrderSummaryCategory;
import com.lachesis.mnis.core.old.doc.service.ExecutiveOrderSummaryAndItemService;
import com.lachesis.mnis.core.order.entity.OrderExecGroup;
import com.lachesis.mnis.core.order.entity.OrderItem;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseListVo;
import com.lachesis.mnis.web.common.vo.BaseVo;




@Controller
@RequestMapping("/exeOrderSummary")
public class ExecutiveOrderSummaryAction {
	@Autowired
	private ExecutiveOrderSummaryAndItemService<ExecutiveOrderSummaryAndItem> executiveOrderSummaryAndItemService;
	@Autowired
	OrderService  orderService;
	/**
	 * 获取登录人科室的医嘱统计。
	 * @return
	 */
	@RequestMapping(value="/querySummary")
	public @ResponseBody 
	BaseVo querySummary(){
		BaseListVo vo = new BaseListVo();
		// NOTE 由于跨旧MNIS与新MNIS两个工程，将本应在LogicService中的代码置于此 

		// 获取用户科室编码
		String deptCode = WebContextUtils.getLocalUserInfo().getDeptCode();
		
		// 获取统计条目，医嘱条目
		List<ExecutiveOrderSummaryAndItem> summaryList = executiveOrderSummaryAndItemService.queryListByCondition(null);
//		List<OrderExecGroup> orderItemList = orderService.getOrderGroupByDeptCode(deptCode);
		List<OrderExecGroup> orderItemList = new ArrayList<>();
				
		//
		// 构造Map. 按Id，ItemCode&FreqCode 分别构造统计条目Map
		//
		Map<String,ExecutiveOrderSummaryAndItem> summaryIdMap = new  HashMap<String,ExecutiveOrderSummaryAndItem>();
		Map<String,Object> summaryItemCodeMap = new  HashMap<String,Object>();
		for(ExecutiveOrderSummaryAndItem summaryItem:summaryList){
			String codeKey = summaryItem.getItemCode();
			if(StringUtils.isNotEmpty(summaryItem.getFreqCode())){
				codeKey += "__" + summaryItem.getFreqCode();
			}
			Object codeMapValue = summaryItemCodeMap.get(codeKey);
			if(codeMapValue==null){
				// 无映射，直接映射
				summaryItemCodeMap.put(codeKey, summaryItem);
			}
			else if(codeMapValue instanceof ExecutiveOrderSummaryAndItem){
				// 已映射一个，将原映射转移到列表。并将当前添加到列表
				List<ExecutiveOrderSummaryAndItem> list = new ArrayList<ExecutiveOrderSummaryAndItem>();
				list.add((ExecutiveOrderSummaryAndItem)codeMapValue);
				
				list.add(summaryItem);
				summaryItemCodeMap.put(codeKey, list);
			}
			else {
				// 已映射多个,添加到列表
				List<ExecutiveOrderSummaryAndItem> list = (List<ExecutiveOrderSummaryAndItem>)codeMapValue;
				list.add(summaryItem);
			}
			
			
			summaryIdMap.put(summaryItem.getSummaryId(), summaryItem);			
		}// end for		
		
		//
		// 将条目放置到对应统计条目
		//
		for(OrderExecGroup item:orderItemList){
			String freqCode = item.getOrderGroup().getDeliverFreq();
			String bedCode = item.getOrderGroup().getPatientBedCode();

			for(OrderItem baseInfo : item.getOrderGroup().getOrderItems()){
				baseInfo.getOrderItemCode();
				String itemCode = baseInfo.getOrderItemCode();
				String itemFreqKey = itemCode +"__"+freqCode;
				
				Object summaryObjectFreq = summaryItemCodeMap.get(itemFreqKey);
				Object summaryObject = summaryItemCodeMap.get(itemCode);
				
				if(summaryObjectFreq!=null){
					addBedCodeToSummaryItem(bedCode,summaryObjectFreq);
				}
				if(summaryObject!=null){
					addBedCodeToSummaryItem(bedCode,summaryObject);
				}
			}
		}// end for
		
		//
		// 组织成分类、条目的层级关系
		//
		List<ExecutiveOrderSummaryCategory> list = new ArrayList<ExecutiveOrderSummaryCategory>();
		Map<String,ExecutiveOrderSummaryCategory> categoryMap = new HashMap<String,ExecutiveOrderSummaryCategory>();
		for(ExecutiveOrderSummaryAndItem summaryItem:summaryList){
			// 判断是否有显示必要
			if(summaryItem.getBedCodeList().isEmpty()&& "N".equals(summaryItem.getEmptyShowFlag())){
				// 跳过无统计结果，并且可以不显示的
				continue;
			}
			
			// 加入到分类
			ExecutiveOrderSummaryCategory category = categoryMap.get(summaryItem.getCategoryName());
			if(category==null){
				list.add(new ExecutiveOrderSummaryCategory(summaryItem));
				categoryMap.put(category.getCategroyName(), category);
			}
			else{
				category.addSummaryItem(summaryItem);
			}
		}// end for
		
		
		vo.setList(list);
		vo.setRslt(ResultCst.SUCCESS);
		
		
		return vo;
	}
	
	private void addBedCodeToSummaryItem(String bedCode, Object summaryObject){
		String [] bedCodes = bedCode.split("_");
		String bed = bedCodes[bedCodes.length-1];
		if(summaryObject instanceof ExecutiveOrderSummaryAndItem){
			// 已映射一个，
			((ExecutiveOrderSummaryAndItem)summaryObject).addBedCode(bed);
		}
		else {
			// 已映射多个,添加到列表
			List<ExecutiveOrderSummaryAndItem> list = (List<ExecutiveOrderSummaryAndItem>)summaryObject;
			for(ExecutiveOrderSummaryAndItem summaryItem:list){
				summaryItem.addBedCode(bed);
			}
		}
	}
}
