package com.gl.jcrawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinkQueue {
	private Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<String>());
	private List<String> unvisitedUrls = Collections.synchronizedList(new ArrayList<String>());
	
	public String popUnvisitedUrls(){
		if(!unvisitedUrls.isEmpty()){
			String url = unvisitedUrls.remove(0);
			visitedUrls.add(url);
			
			return url;
		}
		
		return null;
	}
	
	public void addUnvisitedUrls(String url){
		if(url == null){
			return;
		}
		
		if(url.trim().length() < 1){
			return;
		}
		
		if(visitedUrls.contains(url)){
			return;
		}
		
		if(unvisitedUrls.contains(url)){
			return;
		}
		
		unvisitedUrls.add(url);
	}
	
	public boolean isUnvisitedUrlsEmpty(){
		return unvisitedUrls.isEmpty();
	}
}
