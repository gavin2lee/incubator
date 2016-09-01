package com.lachesis.mnis.core.event;

import com.lachesis.mnis.core.event.entity.DocReportEventEntity;


/**
 * 文书记录转抄事件
 * @author ThinkPad
 *
 */
public class DocReportEvent extends MnisEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//开始执行的输液和结束的输液
	private DocReportEventEntity docReportEventEntity;
	public DocReportEvent(Object source) {
		super(source);
	}
	
	public DocReportEvent(Object source,DocReportEventEntity docReportEventEntity){
		super(source);
		this.docReportEventEntity = docReportEventEntity;
	}

	public DocReportEventEntity getDocReportEventEntity() {
		return docReportEventEntity;
	}

	public void setDocReportEventEntity(DocReportEventEntity docReportEventEntity) {
		this.docReportEventEntity = docReportEventEntity;
	}

}
