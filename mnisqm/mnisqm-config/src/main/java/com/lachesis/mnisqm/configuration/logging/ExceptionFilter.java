package com.lachesis.mnisqm.configuration.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @author Paul Xu
 * @since 1.0.0
 */
public class ExceptionFilter extends Filter<ILoggingEvent> {
    @SuppressWarnings("rawtypes")
	private static Class[] blacklistClss;

    static {
        try {
            blacklistClss = new Class[]{Class.forName("org.apache.jackrabbit.core.cluster.ClusterException")};
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("rawtypes")
	@Override
    public FilterReply decide(ILoggingEvent event) {
        final IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy == null) {
            return FilterReply.NEUTRAL;
        }

        if (!(throwableProxy instanceof ThrowableProxy)) {
            return FilterReply.NEUTRAL;
        }

        final ThrowableProxy throwableProxyImpl = (ThrowableProxy) throwableProxy;
        final Throwable throwable = throwableProxyImpl.getThrowable();
        for (Class exceptCls : blacklistClss) {
            if (exceptCls.isInstance(throwable)) {
                return FilterReply.DENY;
            }
        }


        return FilterReply.NEUTRAL;
    }
}
