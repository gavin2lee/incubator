/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web.vo;

import java.util.List;

import com.lachesis.mnis.core.order.entity.OrderFreqPlanTime;

/**
 * pc端保存开立医嘱执行频次默认时间bean
 * 
 *
 * @author yuliang.xu
 * @since: 2014-9-3
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
public class OrderFreqPlanTimeVo{

	private List<OrderFreqPlanTime> orderFreqList;
	public List<OrderFreqPlanTime> getOrderFreqList() {
		return orderFreqList;
	}
	public void setOrderFreqList(List<OrderFreqPlanTime> orderFreqList) {
		this.orderFreqList = orderFreqList;
	}

}
