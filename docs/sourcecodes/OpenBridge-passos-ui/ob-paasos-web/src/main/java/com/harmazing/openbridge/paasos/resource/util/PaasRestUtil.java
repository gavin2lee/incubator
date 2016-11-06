package com.harmazing.openbridge.paasos.resource.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class PaasRestUtil {
	
	public enum Method{
		Get, Post, Put, Delete;
	}

	public String sendHttpRequest(String url, Method method, Map<String,String>headers, Map<String,String> forms,
			Map<String,String> bodys, Map<String,String> cookies, String returyType) throws Exception{
		if(method==Method.Get){
			HttpGet get = new HttpGet(url);
			return  executeHttpRequest(get,headers,cookies);
		}else if (method == Method.Post){
			HttpPost post = new HttpPost(url);
			return postOrPut(post,headers,forms,bodys,cookies,returyType);
		}else if (method == Method.Put){
			HttpPut put = new HttpPut(url);
			return postOrPut(put,headers,forms,bodys,cookies,returyType);
		}else if (method == Method.Delete){
			HttpDelete delete = new HttpDelete(url);
			return executeHttpRequest(delete,headers,cookies);
		}else{
			throw new Exception("该请求方法暂不支持");
		}
	}
	
	private String postOrPut(HttpEntityEnclosingRequestBase request, Map<String,String> headers,Map<String,String> forms,
			Map<String,String> bodys,Map<String,String> cookies,String consume) throws Exception{
        
        if(forms!=null && forms.size()>0){
        	List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
        	for(Map.Entry<String,String> form : forms.entrySet()){
        		NameValuePair nameValuePair = new BasicNameValuePair(form.getKey(), String.valueOf(form.getValue()));
				valuePairs.add(nameValuePair);
        	}
 			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(valuePairs, "UTF-8");
 			request.setEntity(formEntity);
        }else{
        	if(bodys!=null && bodys.size()>0){
        		if("form".equals(consume)){
        			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
                	for(Map.Entry<String,String> body : bodys.entrySet()){
                		NameValuePair nameValuePair = new BasicNameValuePair(body.getKey(), String.valueOf(body.getValue()));
        				valuePairs.add(nameValuePair);
    	     			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(valuePairs, "UTF-8");
    	     			request.setEntity(formEntity);
        				break;
                	}
        		}else if ("json".equals(consume)){
        			StringEntity strEntity = null;
        			request.getParams().setParameter("Content-Type", "application/json");
                	for(Map.Entry<String,String> body : bodys.entrySet()){
                		ContentType type = ContentType.create("application/json", Charset.forName("UTF-8"));
    					strEntity = new StringEntity(body.getValue(),type);
    					request.setEntity(strEntity);
        				break;
                	}
        		}else if ("xml".equals(consume)){
        			StringEntity strEntity = null;
        			request.getParams().setParameter("Content-Type", "application/xml");
                	for(Map.Entry<String,String> body : bodys.entrySet()){
                		ContentType type = ContentType.create("application/xml", Charset.forName("UTF-8"));
    					strEntity = new StringEntity(body.getValue(),type);
    					request.setEntity(strEntity);
        				break;
                	}
        		}
            }
        }
        return executeHttpRequest(request, headers, cookies);
	}
	
	
	//发起http请求，解析响应内容
	private String executeHttpRequest(HttpRequestBase request,
			Map<String,String> headers, Map<String,String> cookies) throws Exception{
		CloseableHttpClient client = HttpClients.createDefault();
		//DefaultHttpClient client = new DefaultHttpClient();
		String response = null;
        try {
            if(headers!=null && headers.size()>0){
            	for(Map.Entry<String,String> head : headers.entrySet()){
            		request.addHeader(head.getKey(), head.getValue());
            	}
            }
            
            if(cookies!=null && cookies.size()>0){
            	StringBuilder cookieValuePair = new StringBuilder();
            	for(Map.Entry<String, String> cookie : cookies.entrySet()){
            		cookieValuePair.append(cookie.getKey()).append("=").append(cookie.getValue()).append(";");
            	}
            	request.addHeader("Cookie", cookieValuePair.toString());
            }
            HttpResponse httpResponse = client.execute(request);
            if(httpResponse==null){
            	throw new Exception("请求失败");
            }
            
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
            	HttpEntity entity = httpResponse.getEntity();
            	response = EntityUtils.toString(entity, "UTF-8");
            	return response;
            }else{
            	throw new Exception("请求失败,http状态码为"+statusCode);
            }
        } finally {
        	request.releaseConnection();
        }
	}
}
