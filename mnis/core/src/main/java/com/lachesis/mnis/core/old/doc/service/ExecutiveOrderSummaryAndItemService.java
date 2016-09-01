package com.lachesis.mnis.core.old.doc.service;

import java.util.List;

import com.lachesis.mnis.core.old.doc.ExecutiveOrderSummaryAndItem;


public interface ExecutiveOrderSummaryAndItemService<T> {
	
	List<ExecutiveOrderSummaryAndItem> queryListByCondition(T t);
}
