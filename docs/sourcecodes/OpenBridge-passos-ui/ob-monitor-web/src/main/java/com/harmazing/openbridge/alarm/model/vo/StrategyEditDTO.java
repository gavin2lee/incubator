package com.harmazing.openbridge.alarm.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.harmazing.openbridge.alarm.model.Strategy;

/**
 * monitor的策略编辑界面
 * 
 * @author luoan
 *
 */
public class StrategyEditDTO extends ParentDTO {
	private long actionId;
	private long tplId;
	private String tplName;
	//uic是唯一的
	private String uic;
	private List<Strategy> strategys = new ArrayList<Strategy>();

	
	
	public long getActionId() {
		return actionId;
	}

	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	public long getTplId() {
		return tplId;
	}

	public void setTplId(long tplId) {
		this.tplId = tplId;
	}

	public String getTplName() {
		return tplName;
	}

	public void setTplName(String tplName) {
		this.tplName = tplName;
	}

	public String getUic() {
		return uic;
	}

	public void setUic(String uic) {
		this.uic = uic;
	}

	public List<Strategy> getStrategys() {
		return strategys;
	}

	public void setStrategys(List<Strategy> strategys) {
		this.strategys = strategys;
	}

	@Override
	public String toString() {
		return "StrategyEditDTO [actionId=" + actionId + ", tplId=" + tplId
				+ ", tplName=" + tplName + ", uic=" + uic + ", strategys="
				+ strategys + "]";
	}

}
