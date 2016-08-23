package com.gl.jcrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ImageDownloader {
	private List<String> listImgSrc;
	
	
	public ImageDownloader(List<String> listImgSrc) {
		super();
		this.listImgSrc = listImgSrc;
	}


	public void download(){
		for (String url : listImgSrc) {  
            try {  
                String imageName = url.substring(url.lastIndexOf("/") + 1,  
                        url.length());  
                  
                URL uri = new URL(url);  
                InputStream in = uri.openStream();  
                FileOutputStream fo = new FileOutputStream(new File(imageName));  
                byte[] buf = new byte[1024];  
                int length = 0;  
                while ((length = in.read(buf, 0, buf.length)) != -1) {  
                    fo.write(buf, 0, length);  
                }  
                in.close();  
                fo.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  

	}
}
