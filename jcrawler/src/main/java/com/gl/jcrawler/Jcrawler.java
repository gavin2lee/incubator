package com.gl.jcrawler;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.htmlparser.util.ParserException;

public class Jcrawler {
	private String initUrl;
	private LinkQueue linkQueue = new LinkQueue();

	public Jcrawler(String initUrl) {
		super();
		this.initUrl = initUrl;
	};

	public void crawl() throws IOException, ParserException {
		Filter filter = new Filter() {
			public boolean accept(String url) {
				if (url.indexOf("http://jshop.ofmall.org:81") != -1
						|| url.indexOf("http://jshop.ofmall.org:81/jshop") != -1) {
					return true;
				} else {
					return false;
				}
			}
		};
		
		linkQueue.addUnvisitedUrls(initUrl);
		
		while (!linkQueue.isUnvisitedUrlsEmpty()) {  
            // 队头URL出队列  
            String visitUrl = (String) linkQueue.popUnvisitedUrls();  
            if (visitUrl == null){
                continue;
            }
            
            String html = new HtmlContentParser(visitUrl).getHtml();
            List<String> imageUrls = new ImageUrlParser().getImageSrc(html);
            new ImageDownloader(imageUrls).download();
              
  
            Set<String> links = new HttpLinkParser(visitUrl, filter).parse().results();  
            for (String link : links) {  
                linkQueue.addUnvisitedUrls(link);  
            }  
        }  
	}

}
