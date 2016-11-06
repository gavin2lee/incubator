package com.harmazing.openbridge.alarm.model.vo;
/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/7/29 9:18.
 */
public class ErrorResult extends ParentDTO {

    private String code;

    private String error;

    public ErrorResult(String error, String code) {
        this.code = code;
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public String getError() {
        return error;
    }


}