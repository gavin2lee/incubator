package com.lachesis.mnisqm.common;

import java.util.List;

public class ExcelObject {

	private List<String> head; //表头
	
	private List<List<String>> body; //表体

	public List<String> getHead() {
		return head;
	}

	public void setHead(List<String> head) {
		this.head = head;
	}

	public List<List<String>> getBody() {
		return body;
	}

	public void setBody(List<List<String>> body) {
		this.body = body;
	}
	
	
}
