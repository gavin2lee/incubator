package com.gl.jcrawler;

import java.io.IOException;

import org.htmlparser.util.ParserException;

public class ImageCrawlerBootStrap {

	public static void main(String[] args) {
		String initUrl = "http://jshop.ofmall.org:81/jshop";
		Jcrawler j = new Jcrawler(initUrl);
		
		try {
			j.crawl();
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
