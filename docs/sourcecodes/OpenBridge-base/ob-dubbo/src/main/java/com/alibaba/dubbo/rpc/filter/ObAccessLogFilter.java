/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.filter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * 完全抄袭AccessLogFilter代码结构，只是修改日志格式。
 * 针对并发量不大 不会存在什么问题
 * 存在两个问题
 * 1. 默认5秒打开一个FileWriter
 * 2. 5秒单个服务只能记录5000条日志
 * TODO 采用log4j日志那种形式
 * @author dengxiaoqian
 */
@Activate(group = Constants.PROVIDER, value = Constants.ACCESS_LOG_KEY)
public class ObAccessLogFilter implements Filter {
    
    private static final Logger logger            = LoggerFactory.getLogger(ObAccessLogFilter.class);

    private static final String  ACCESS_LOG_KEY   = "dubbo.accesslog";
    
    private static final String  FILE_DATE_FORMAT   = "yyyyMMdd";

    private static final String  MESSAGE_DATE_FORMAT   = "yyyy-MM-dd HH:mm:ss";
    
    private static final String MESSAGE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private static final int LOG_MAX_BUFFER = 50000;

    private static final long LOG_OUTPUT_INTERVAL = 5000;

    private final ConcurrentMap<String, Set<String>> logQueue = new ConcurrentHashMap<String, Set<String>>();

    private final ScheduledExecutorService logScheduled = Executors.newScheduledThreadPool(2, new NamedThreadFactory("Dubbo-Access-Log", true));

    private volatile ScheduledFuture<?> logFuture = null;

    private class LogTask implements Runnable {
        public void run() {
            try {
                if (logQueue != null && logQueue.size() > 0) {
                    for (Map.Entry<String, Set<String>> entry : logQueue.entrySet()) {
                        try {
                            String accesslog = entry.getKey();
                            Set<String> logSet = entry.getValue();
                            File file = new File(accesslog);
                            File dir = file.getParentFile();
                            if (null!=dir&&! dir.exists()) {
                                dir.mkdirs();
                            }
                            if (logger.isDebugEnabled()) {
                                logger.debug("Append log to " + accesslog);
                            }
                            if (file.exists()) {
                                String now = new SimpleDateFormat(FILE_DATE_FORMAT).format(new Date());
                                String last = new SimpleDateFormat(FILE_DATE_FORMAT).format(new Date(file.lastModified()));
                                if (! now.equals(last)) {
                                    File archive = new File(file.getAbsolutePath() + "." + last);
                                    file.renameTo(archive);
                                }
                            }
                            FileWriter writer = new FileWriter(file, true);
                            try {
                                for(Iterator<String> iterator = logSet.iterator();
                                    iterator.hasNext();
                                    iterator.remove()) {
                                    writer.write(iterator.next());
                                    writer.write("\r\n");
                                }
                                writer.flush();
                            } finally {
                                writer.close();
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    private void init() {
        if (logFuture == null) {
            synchronized (logScheduled) {
                if (logFuture == null) {
                    logFuture = logScheduled.scheduleWithFixedDelay(new LogTask(), LOG_OUTPUT_INTERVAL, LOG_OUTPUT_INTERVAL, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
    
    private void log(String accesslog, String logmessage) {
        init();
        Set<String> logSet = logQueue.get(accesslog);
        if (logSet == null) {
            logQueue.putIfAbsent(accesslog, new ConcurrentHashSet<String>());
            logSet = logQueue.get(accesslog);
        }
        if (logSet.size() < LOG_MAX_BUFFER) {
            logSet.add(logmessage);
        }
    }

    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
    	String accesslog = invoker.getUrl().getParameter(Constants.ACCESS_LOG_KEY);
    	if(ConfigUtils.isNotEmpty(accesslog)){
    		Map<String,Object> param = new HashMap<String,Object>();
    		RpcContext context = RpcContext.getContext();
//    		String serviceName = invoker.getInterface().getName();
//    		param.put("serviceName", serviceName);
//    		String version = invoker.getUrl().getParameter(Constants.VERSION_KEY);
//    		param.put("version", version);
//    		String group = invoker.getUrl().getParameter(Constants.GROUP_KEY);
//    		param.put("group", group);
    		String remoteHost = context.getRemoteHost();
    		param.put("remote_host", remoteHost);
    		String remotePort = context.getRemotePort()+"";
    		param.put("remote_port", remotePort);
    		String localHost = context.getLocalHost();
    		param.put("local_host", localHost);
    		String localPort = context.getLocalPort()+"";
    		param.put("local_port", localPort);
    		String methodName = inv.getMethodName();
    		param.put("method_name", methodName);
    		param.put("log_source", "dubbo");
    		Date beginDate = new Date();
    		long beginHanler = beginDate.getTime();
    		try{
    			param.put("status", 200);
    			return invoker.invoke(inv);
    		}
    		catch(Exception e){
    			param.put("status", 500);
    			throw e;
    		}
    		finally{
    			Date endDate = new Date();
    			param.put("timestamp", new SimpleDateFormat(MESSAGE_FORMAT).format(beginDate));
    			long endHandler = endDate.getTime();
    			param.put("request_time", (endHandler-beginHanler)/1000.00);
    			
    			Object env = System.getenv().get("ENV_TYPE");
    			if(env != null){
    				param.put("env_type", env);
    			}
    			
    			Object categoryId = System.getenv().get("CATEGORY_ID");
    			if(categoryId != null){
    				param.put("category_id", categoryId);
    			}
    			
    			Object serviceId = System.getenv().get("SERVICE_ID");
    			if(serviceId != null){
    				param.put("service_id", serviceId);
    			}
    			
    			Object versionId = System.getenv().get("VERSION_ID");
    			if(versionId != null){
    				param.put("version_id", versionId);
    			}
    			
    			Object interfaceName = context.getAttachments().get("interface");
    			if(interfaceName==null){
    				interfaceName = context.getUrl().getServiceName();
    				if(interfaceName==null){
    					interfaceName = context.getUrl().getServiceInterface();
    				}
    			}
    			if(interfaceName!=null){
    				String in = interfaceName.toString();
    				in = in.replaceAll("\\.", "_");
    				in = "PI_"+in.toUpperCase();
    				String iId = System.getenv().get(in);
    	//			System.out.println(in+"-----"+iId);
    				param.put("interface_id", iId);
    			}
    			param.put("protocol", context.getUrl().getProtocol().toLowerCase());
    			param.put("log_type", System.getenv().get("APPLICATIONTYPE"));
    			try{
    				log(accesslog, JSON.json(param));
    			}
    			catch(Exception e){
    				e.printStackTrace();
    			}
    		}
    	}
    	else{
    		 return invoker.invoke(inv);
    	}
    	
    }

}