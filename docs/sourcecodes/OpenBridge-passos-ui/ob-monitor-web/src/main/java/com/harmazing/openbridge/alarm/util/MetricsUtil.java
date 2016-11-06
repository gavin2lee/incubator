package com.harmazing.openbridge.alarm.util;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/4 11:52.
 */
@Component
public class MetricsUtil {
    private Logger logger = Logger.getLogger(this.getClass());

    public boolean isExistMetrics(String query){
        //返回读取指定资源的输入流
        InputStream is=MetricsUtil.class.getResourceAsStream("/config/metrics.txt");
        //InputStream is=当前类.class.getResourceAsStream("XX.config");
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        boolean result = false;
        try {
            String temp= "";
            while((temp = br.readLine())!=null){
                if(temp.equals(query)){
                    result=true;
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("Read metrics.txt error!",e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("Close metrics.txt InputStream error!",e);
            }
        }
        return result;
    }

    public List<String> getMertics(String query){
        //返回读取指定资源的输入流
        InputStream is=MetricsUtil.class.getResourceAsStream("/config/metrics.txt");
        //InputStream is=当前类.class.getResourceAsStream("XX.config");
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        List<String> result = new ArrayList<>();
        try {
            String temp= "";
            while((temp = br.readLine())!=null){
                if(temp.contains(query)){
                    result.add(temp);
                }
            }
        } catch (IOException e) {
            logger.error("Read metrics.txt error!",e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                logger.error("Close metrics.txt InputStream error!",e);
            }
        }
        return result;
    }
}
