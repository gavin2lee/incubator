package com.harmazing.framework.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.xml.DomUtils;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class XmlUtil extends DomUtils {
	private static final Log logger = LogFactory.getLog(XmlUtil.class);

	public static Document buildDocument(String path)
			throws ParserConfigurationException, SAXException, IOException {
		return buildDocument(new FileInputStream(path));
	}

	public static Document buildDocument(InputStream stream)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder parser = dbf.newDocumentBuilder();
		ErrorHandler handler = new SimpleSaxErrorHandler(logger);
		parser.setErrorHandler(handler);
		return parser.parse(stream);
	}

	public static Document buildDocumentForString(String xmlString)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder parser = dbf.newDocumentBuilder();
		ErrorHandler handler = new SimpleSaxErrorHandler(logger);
		parser.setErrorHandler(handler);
		return parser.parse(new InputSource(new StringReader(xmlString)));
	}

}
