package com.harmazing.openbridge.alarm.model.vo;

import com.harmazing.openbridge.alarm.model.Action;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/5 10:05.
 */
public class ActionDTO extends ParentDTO {
    private long id;
    private long tplId;
    private String uic;
    private String url;
    private boolean callback;
    private boolean beforeCallbackSms;
    private boolean beforeCallbackMail;
    private boolean afterCallbackSms;
    private boolean afterCallbackMail;

    public Action getAction(){
        return new Action(id, uic, url, callback, beforeCallbackSms, beforeCallbackMail, afterCallbackSms, afterCallbackMail);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTplId() {
        return tplId;
    }

    public void setTplId(long tplId) {
        this.tplId = tplId;
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
