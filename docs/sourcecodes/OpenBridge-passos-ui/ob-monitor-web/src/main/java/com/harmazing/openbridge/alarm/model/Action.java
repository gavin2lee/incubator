package com.harmazing.openbridge.alarm.model;

import com.harmazing.framework.common.model.BaseModel;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/5 9:17.
 */
public class Action extends BaseModel {
    private long id;
    private String uic;
    private String url;
    private boolean callback;
    private boolean beforeCallbackSms;
    private boolean beforeCallbackMail;
    private boolean afterCallbackSms;
    private boolean afterCallbackMail;

    public Action() {
    }

    public Action(long id, String uic, String url, boolean callback, boolean beforeCallbackSms,
                  boolean beforeCallbackMail, boolean afterCallbackSms, boolean afterCallbackMail) {
        this.id = id;
        this.uic = uic;
        this.url = url;
        this.callback = callback;
        this.beforeCallbackSms = beforeCallbackSms;
        this.beforeCallbackMail = beforeCallbackMail;
        this.afterCallbackSms = afterCallbackSms;
        this.afterCallbackMail = afterCallbackMail;
    }

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

    public boolean isCallback() {
        return callback;
    }

    public void setCallback(boolean callback) {
        this.callback = callback;
    }

    public boolean isBeforeCallbackSms() {
        return beforeCallbackSms;
    }

    public void setBeforeCallbackSms(boolean beforeCallbackSms) {
        this.beforeCallbackSms = beforeCallbackSms;
    }

    public boolean isBeforeCallbackMail() {
        return beforeCallbackMail;
    }

    public void setBeforeCallbackMail(boolean beforeCallbackMail) {
        this.beforeCallbackMail = beforeCallbackMail;
    }

    public boolean isAfterCallbackSms() {
        return afterCallbackSms;
    }

    public void setAfterCallbackSms(boolean afterCallbackSms) {
        this.afterCallbackSms = afterCallbackSms;
    }

    public boolean isAfterCallbackMail() {
        return afterCallbackMail;
    }

    public void setAfterCallbackMail(boolean afterCallbackMail) {
        this.afterCallbackMail = afterCallbackMail;
    }
}
