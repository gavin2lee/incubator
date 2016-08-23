package com.gl.jcrawler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.FrameTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HttpLinkParser {
	private Set<String> links = new HashSet<String>();
	private String url;
	private Filter filter;

	public HttpLinkParser(String url, Filter filter) {
		super();
		this.url = url;
		this.filter = filter;
	}

	public HttpLinkParser parse(){
		NodeFilter frameFilter = new NodeFilter() {

			private static final long serialVersionUID = 1L;

			public boolean accept(Node node) {
				if (node.getText().startsWith("frame") || node.getText().startsWith("FRAME")) {
					return true;
				}
				return false;
			}

		};

		OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);

		Parser parser;
		try {
			parser = new Parser(url);
			NodeList nodeList = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < nodeList.size(); i++) {
				Node tag = nodeList.elementAt(i);
				
				if(tag instanceof LinkTag){
					LinkTag linkTag = (LinkTag)tag;
					String link = linkTag.getLink();
					if(filter.accept(link)){
						links.add(link);
					}
				}else if(tag instanceof FrameTag){
					FrameTag frameTag = (FrameTag)tag;
					String link = frameTag.getAttribute("src");
					if(filter.accept(link)){
						links.add(link);
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return this;
	}

	public Set<String> results() {
		return Collections.unmodifiableSet(links);
	}

}
