package com.lachesis.mnisqm.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lachesis.mnisqm.constants.Constants;

public class BusinessException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty(Constants.ResultCode)
    protected String resultCode;
    @JsonProperty(Constants.ResultMsg)
    protected String resultMsg;

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

    protected BusinessException(String resultCode,String resultMsg){
        this.resultCode=resultCode;
        this.resultMsg=resultMsg;
    }
}
