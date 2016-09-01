package com.esofthead.mycollab.jetty;

import org.eclipse.jetty.webapp.WebAppContext;

import com.lachesis.mnisqm.jetty.GenericServerRunner;

/**
 * 程序启动类
 * @author Paul Xu.
 * @since 1.0
 */
public class DefaultServerRunner extends GenericServerRunner {

    @Override
    public WebAppContext buildContext(String baseDir) {
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/mnisqm/");
        webAppContext.setWar(baseDir);
        webAppContext.setResourceBase(baseDir);
//        GzipHandler gzipHandler = new GzipHandler();
//        gzipHandler.addExcludedMimeTypes("text/html,text/plain,text/xml,application/xhtml+xml,text/css,application/javascript,image/svg+xml");
//        webAppContext.setGzipHandler(gzipHandler);
        return webAppContext;
    }

    public static void main(String[] args) throws Exception {
        new DefaultServerRunner().run(args);
    }
}
