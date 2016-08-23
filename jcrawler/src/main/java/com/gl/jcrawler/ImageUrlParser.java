package com.gl.jcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUrlParser {
	private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
	private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
	
	public List<String> getImageSrc(String html) {
		List<String> listImageUrl = getImageUrl(html);
        List<String> listImgSrc = new ArrayList<String>();  
        for (String image : listImageUrl) {  
            Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);  
            while (matcher.find()) {  
                listImgSrc.add(matcher.group().substring(0,  
                        matcher.group().length() - 1));  
            }  
        }  
        return listImgSrc;  
    }
	
	private List<String> getImageUrl(String html) {  
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html);  
        List<String> listImgUrl = new ArrayList<String>();  
        while (matcher.find()) {  
            listImgUrl.add(matcher.group());  
        }  
        return listImgUrl;  
    } 
}
