package com.harmazing.openbridge.paas.util;

public class NoSupportEtcdException extends RuntimeException{
	
	 public NoSupportEtcdException(String message) {
	        super(message);
	    }

	  public NoSupportEtcdException(String message, Throwable cause) {
	        super(message, cause);
	    }
	
	

}
