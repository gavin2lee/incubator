package com.lachesis.mnisqm.core;

/**
 * mnisqm 运行时错误基类. 所在此项目的错误类都需要从此类派生
 *
 */
public class CommRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    //错误编号
    private String code;

    public CommRuntimeException(String message) {
        super(message);
    }
    
    public CommRuntimeException(String code,String message) {
        super(message);
    	this.code = code;
    }

    public CommRuntimeException(Throwable e) {
        super(e);
    }

    public CommRuntimeException(String message, Throwable e) {
        super(message, e);
    }
    //获取错误编号
    public String getCode(){
    	return code;
    }
}
