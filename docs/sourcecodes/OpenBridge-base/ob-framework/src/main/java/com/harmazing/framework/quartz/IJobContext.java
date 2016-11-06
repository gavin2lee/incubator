package com.harmazing.framework.quartz;

import org.apache.commons.logging.Log;

public interface IJobContext {
    public Log getLogger();

    public String getParameter();

    public void setLogger(Log logger);

    public void logMessage(String msg);

    public void logError(String errMsg);

    public void logError(Throwable e);

    public void logError(String errMsg, Throwable e);
}
