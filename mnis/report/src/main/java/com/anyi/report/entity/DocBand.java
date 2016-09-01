package com.anyi.report.entity;

import java.util.List;

public class DocBand {
	private String parentTemplateId;
	private String bandId;
	private String bandName;
	private int posY;
	private int height;
	private List<DocTemplateItem> items;

	public String getBandId() {
		return bandId;
	}

	public void setBandId(String bandId) {
		this.bandId = bandId;
	}

	public String getParentTemplateId() {
		return parentTemplateId;
	}

	public void setParentTemplateId(String parentTemplateId) {
		this.parentTemplateId = parentTemplateId;
	}

	public String getBandName() {
		return bandName;
	}

	public void setBandName(String bandName) {
		this.bandName = bandName;
	}

	public List<DocTemplateItem> getItems() {
		return items;
	}

	public void setItems(List<DocTemplateItem> items) {
		this.items = items;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
