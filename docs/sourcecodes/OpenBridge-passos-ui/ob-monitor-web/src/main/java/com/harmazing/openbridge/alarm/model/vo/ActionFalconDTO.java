package com.harmazing.openbridge.alarm.model.vo;

import com.harmazing.openbridge.alarm.model.Action;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/5 10:05.
 * 为open-falcon Alarm组件提供服务
 */
public class ActionFalconDTO extends ParentDTO {
    private long id;
    private String uic;
    private String url;
    private int callback;
    private int before_callback_sms;
    private int before_callback_mail;
    private int after_callback_sms;
    private int after_callback_mail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUic() {
        return uic;
    }

    public void setUic(String uic) {
        this.uic = uic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCallback() {
        return callback;
    }

    public void setCallback(int callback) {
        this.callback = callback;
    }

    public int getBefore_callback_sms() {
        return before_callback_sms;
    }

    public void setBefore_callback_sms(int before_callback_sms) {
        this.before_callback_sms = before_callback_sms;
    }

    public int getBefore_callback_mail() {
        return before_callback_mail;
    }

    public void setBefore_callback_mail(int before_callback_mail) {
        this.before_callback_mail = before_callback_mail;
    }

    public int getAfter_callback_sms() {
        return after_callback_sms;
    }

    public void setAfter_callback_sms(int after_callback_sms) {
        this.after_callback_sms = after_callback_sms;
    }

    public int getAfter_callback_mail() {
        return after_callback_mail;
    }

    public void setAfter_callback_mail(int after_callback_mail) {
        this.after_callback_mail = after_callback_mail;
    }
}
