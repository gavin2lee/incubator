package com.lachesis.mnisqm.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lachesis.mnisqm.constants.Constants;

public class Result {
    @JsonProperty(Constants.ResultMsg)
    protected String resultMsg;
    @JsonProperty(Constants.ResultCode)
    protected  String resultCode;

    public Result(){
        this.resultCode = Constants.Success;
        this.resultMsg = Constants.SuccessMsg;
    }

    public Result(String resultCode, String resultMsg){
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {

        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
