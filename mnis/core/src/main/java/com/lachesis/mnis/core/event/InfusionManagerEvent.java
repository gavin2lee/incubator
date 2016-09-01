package com.lachesis.mnis.core.event;


import com.lachesis.mnis.core.event.entity.InfusionManagerEventEntity;
/**
 * 输液监视事件
 * @author ThinkPad
 *
 */
public class InfusionManagerEvent extends MnisEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//开始执行的输液和结束的输液
	private InfusionManagerEventEntity[] infusionManagerEventEntities;
	public InfusionManagerEvent(Object source) {
		super(source);
	}
	
	public InfusionManagerEvent(Object source,InfusionManagerEventEntity[] infusionManagerEventEntities){
		super(source);
		this.infusionManagerEventEntities = infusionManagerEventEntities;
	}

	public InfusionManagerEventEntity[] getInfusionManagerEventEntities() {
		return infusionManagerEventEntities;
	}

	public void setInfusionManagerEventEntities(
			InfusionManagerEventEntity[] infusionManagerEventEntities) {
		this.infusionManagerEventEntities = infusionManagerEventEntities;
	}

}
