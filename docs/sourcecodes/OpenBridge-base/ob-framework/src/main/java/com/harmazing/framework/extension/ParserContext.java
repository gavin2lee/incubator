package com.harmazing.framework.extension;

import org.springframework.core.io.Resource;
import org.w3c.dom.Element;

public class ParserContext {
	private Resource resource;
	private Element element;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
