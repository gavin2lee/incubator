package com.lachesis.mnisqm.jetty;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;

import javax.naming.NamingException;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

import com.lachesis.mnisqm.configuration.ApplicationProperties;
import com.lachesis.mnisqm.configuration.logging.LogConfig;
import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.utils.FileUtils;
import com.lachesis.mnisqm.servlet.DatabaseValidate;
import com.lachesis.mnisqm.servlet.EmailValidationServlet;
import com.lachesis.mnisqm.servlet.InstallationServlet;
import com.lachesis.mnisqm.servlet.SetupServlet;
import com.lachesis.mnisqm.servlet.UpgradeServlet;
import com.lachesis.mnisqm.servlet.UpgradeStatusServlet;

/**
 * Generic mnisqm embedded server
 *
 * @author Paul Xu.
 * @since 1.0.0
 */
public abstract class GenericServerRunner {
    private static final Logger LOG = LoggerFactory.getLogger(GenericServerRunner.class);

    private Server server;
    private int port = 8081;

    private InstallationServlet installServlet;
    private ContextHandlerCollection contexts;
    private WebAppContext appContext;
    private ServletContextHandler installationContextHandler;

    public abstract WebAppContext buildContext(String baseDir);

    /**
     * Detect web app folder
     *
     * @return
     */
    private String detectWebApp() {
        System.out.println(System.getProperty("user.dir")+"============");
        File webappFolder = FileUtils.getDesireFile(System.getProperty("user.dir"), "webapp", "src/main/webapp");

        if (webappFolder == null) {
            throw new CommRuntimeException("Can not detect webapp base folder");
        } else {
            return webappFolder.getAbsolutePath();
        }
    }

    private ClientCommunitor clientCommunitor;

    /**
     * Run web server with arguments
     *
     * @param args
     * @throws Exception
     */
    public void run(String[] args) throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
        ServerInstance.getInstance().registerInstance(this);
        System.setProperty("org.eclipse.jetty.annotations.maxWait", "300");

        for (int i = 0; i < args.length; i++) {
            LOG.info("Argument: " + args[i]);
            if ("--port".equals(args[i])) {
                port = Integer.parseInt(args[++i]);
            } else if ("--cport".equals(args[i])) {
                final int listenPort = Integer.parseInt(args[++i]);
                LOG.info("Detect client port " + listenPort);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            clientCommunitor = new ClientCommunitor(listenPort);
                        } catch (Exception e) {
                            LOG.error("Can not establish the client socket to port " + listenPort);
                        }
                    }
                }).start();
            }
        }

        System.setProperty(ApplicationProperties.MYCOLLAB_PORT, port + "");
        execute();
    }

    private void execute() throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                LOG.error("There is uncatch exception", e);
            }
        });
        server = new Server(port);
        contexts = new ContextHandlerCollection();

        boolean alreadySetup = false;

        if (!checkConfigFileExist()) {
            System.err
                    .println("It seems this is the first time you run mnisqm. For complete installation, you must " +
                            "open the browser and type address http://<your server name>:"
                            + port
                            + " and complete the steps to install mnisqm.");
            installationContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            installationContextHandler.setContextPath("/");

            installServlet = new InstallationServlet();
            installationContextHandler.addServlet(new ServletHolder(installServlet), "/install");
            installationContextHandler.addServlet(new ServletHolder(
                    new DatabaseValidate()), "/validate");
            installationContextHandler.addServlet(new ServletHolder(
                    new EmailValidationServlet()), "/emailValidate");

            installationContextHandler.addServlet(new ServletHolder(new SetupServlet()), "/*");
            installationContextHandler.addLifeCycleListener(new ServerLifeCycleListener());

            server.setStopAtShutdown(true);
            contexts.setHandlers(new Handler[]{installationContextHandler});
        } else if(!checkConfigFileExist()){
            alreadySetup = true;

            WebAppContext appContext = initWebAppContext();
            appContext.setContextPath("/mnisqm");
            appContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
            appContext.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
            ServletContextHandler upgradeContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            upgradeContextHandler.setServer(server);
            upgradeContextHandler.setContextPath("/");
            upgradeContextHandler.addServlet(new ServletHolder(new UpgradeServlet()), "/upgrade");
            upgradeContextHandler.addServlet(new ServletHolder(new UpgradeStatusServlet()), "/upgrade_status");
            contexts.setHandlers(new Handler[]{upgradeContextHandler, appContext});
        }else{
        	alreadySetup = true;
        	WebAppContext context = new WebAppContext();
        	context.setContextPath("/mnisqm");
        	context.setDescriptor(System.getProperty("user.dir")+"/src/main/webapp/WEB-INF/web.xml");
        	context.setResourceBase(detectWebApp());
        	//context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
            //context.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        	context.setParentLoaderPriority(true);
        	context.setClassLoader(Thread.currentThread().getContextClassLoader());
        	
        	server.setHandler(context);
        }
       // server.setHandler(contexts);
        server.start();

        if (!alreadySetup) {
            openDefaultWebBrowserForInstallation();
        }

        server.join();
    }

    private void openDefaultWebBrowserForInstallation() {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI("http://localhost:" + port));
            } catch (Exception e) {
                //do nothing, while user can install mnisqm on the remote server
            }
        }
    }

    void upgrade(File upgradeFile) {
        if (clientCommunitor != null) {
            clientCommunitor.reloadRequest(upgradeFile);
        } else {
            throw new CommRuntimeException("Can not contact host process. Terminate upgrade, you should download mnisqm manually");
        }
    }

    private boolean checkConfigFileExist() {
    	//系统运行路径
        System.out.println(System.getProperty("user.dir"));
        File confFolder = FileUtils.getDesireFile(System.getProperty("user.dir"),
                "conf", "src/main/resource");
        return (confFolder == null) ? false : new File(confFolder, "mnisqm.properties").exists();
    }

    @SuppressWarnings("unused")
	private WebAppContext initWebAppContext() {
        LogConfig.initMyCollabLog();
        //获取webapp目录
        String webAppDirLocation = detectWebApp();
        LOG.debug("Detect web location: {}", webAppDirLocation);
        appContext = buildContext(webAppDirLocation);
        appContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        appContext.setServer(server);
        appContext.setConfigurations(new Configuration[]{
                new AnnotationConfiguration(), new WebXmlConfiguration(),
                new WebInfConfiguration(), new PlusConfiguration(),
                new MetaInfConfiguration(), new FragmentConfiguration(),
                new EnvConfiguration()});

        String[] classPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        String fileSeparator = System.getProperty("file.separator");
        String osExprClassFolder, osExprJarFile;
        if ("/".equals(fileSeparator)) {
            osExprClassFolder = ".*mnisqm-\\S+/target/classes$";
            osExprJarFile = ".*mnisqm-\\S+.jar$";
        } else {
            osExprClassFolder = ".+\\\\mnisqm-\\S+\\\\target\\\\classes$";
            osExprJarFile = ".+\\\\mnisqm-\\S+.jar$";
        }

        for (String classpath : classPaths) {
            if (classpath.matches(osExprClassFolder)) {
                LOG.info("Load folder to classpath " + classpath);
                appContext.getMetaData().addWebInfJar(new PathResource(new File(classpath)));
            } else if (classpath.matches(osExprJarFile)) {
                try {
                    LOG.info("Load jar file in path " + classpath);
                    appContext.getMetaData().addWebInfJar(new PathResource(new File(classpath).toURI().toURL()));
                    appContext.getMetaData().getWebInfClassesDirs().add(new PathResource(new File(classpath).toURI().toURL()));
                } catch (Exception e) {
                    LOG.error("Exception to resolve classpath: " + classpath, e);
                }
            }
        }

        File libFolder = new File(System.getProperty("user.dir"), "lib");
        if (libFolder.isDirectory()) {
            File[] files = libFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().matches("mnisqm-\\S+.jar$")) {
                        LOG.info("Load jar file to classpath " + file.getAbsolutePath());
                        appContext.getMetaData().getWebInfClassesDirs().add(new FileResource(file.toURI()));
                    }
                }
            }
        }

        File runnerJarFile = new File(System.getProperty("user.dir"), "runner.jar");
        if (runnerJarFile.exists()) {
            appContext.getMetaData().getWebInfClassesDirs().add(new FileResource(runnerJarFile.toURI()));
        }

        // Register a mock DataSource scoped to the webapp
        // This must be linked to the webapp via an entry in
        // web.xml:
        // <resource-ref>
        // <res-ref-name>jdbc/mydatasource</res-ref-name>
        // <res-type>javax.sql.DataSource</res-type>
        // <res-auth>Container</res-auth>
        // </resource-ref>
        // At runtime the webapp accesses this as
        // java:comp/env/jdbc/mydatasource
        try {
            LOG.info("Init the datasource");
            org.eclipse.jetty.plus.jndi.Resource mydatasource = new org.eclipse.jetty.plus.jndi.Resource(
                    appContext, "jdbc/mycollabdatasource",null);
        } catch (NamingException e) {
            throw new CommRuntimeException(e);
        }

        return appContext;
    }
    
    @SuppressWarnings("unused")
	private WebAppContext initWebAppContext1() {
        LogConfig.initMyCollabLog();
        //获取webapp目录
        String webAppDirLocation = detectWebApp();
        LOG.debug("Detect web location: {}", webAppDirLocation);
        appContext = buildContext(webAppDirLocation);
        appContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        appContext.setServer(server);
        appContext.setConfigurations(new Configuration[]{
                new AnnotationConfiguration(), new WebXmlConfiguration(),
                new WebInfConfiguration(), new PlusConfiguration(),
                new MetaInfConfiguration(), new FragmentConfiguration(),
                new EnvConfiguration()});

        String[] classPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        String fileSeparator = System.getProperty("file.separator");
        String osExprClassFolder, osExprJarFile;
        if ("/".equals(fileSeparator)) {
            osExprClassFolder = ".*mnisqm-\\S+/target/classes$";
            osExprJarFile = ".*mnisqm-\\S+.jar$";
        } else {
            osExprClassFolder = ".+\\\\mnisqm-\\S+\\\\target\\\\classes$";
            osExprJarFile = ".+\\\\mnisqm-\\S+.jar$";
        }

        for (String classpath : classPaths) {
            if (classpath.matches(osExprClassFolder)) {
                LOG.info("Load folder to classpath " + classpath);
                appContext.getMetaData().addWebInfJar(new PathResource(new File(classpath)));
            } else if (classpath.matches(osExprJarFile)) {
                try {
                    LOG.info("Load jar file in path " + classpath);
                    appContext.getMetaData().addWebInfJar(new PathResource(new File(classpath).toURI().toURL()));
                    appContext.getMetaData().getWebInfClassesDirs().add(new PathResource(new File(classpath).toURI().toURL()));
                } catch (Exception e) {
                    LOG.error("Exception to resolve classpath: " + classpath, e);
                }
            }
        }

        File libFolder = new File(System.getProperty("user.dir"), "lib");
        if (libFolder.isDirectory()) {
            File[] files = libFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().matches("mnisqm-\\S+.jar$")) {
                        LOG.info("Load jar file to classpath " + file.getAbsolutePath());
                        appContext.getMetaData().getWebInfClassesDirs().add(new FileResource(file.toURI()));
                    }
                }
            }
        }

        File runnerJarFile = new File(System.getProperty("user.dir"), "runner.jar");
        if (runnerJarFile.exists()) {
            appContext.getMetaData().getWebInfClassesDirs().add(new FileResource(runnerJarFile.toURI()));
        }

        // Register a mock DataSource scoped to the webapp
        // This must be linked to the webapp via an entry in
        // web.xml:
        // <resource-ref>
        // <res-ref-name>jdbc/mydatasource</res-ref-name>
        // <res-type>javax.sql.DataSource</res-type>
        // <res-auth>Container</res-auth>
        // </resource-ref>
        // At runtime the webapp accesses this as
        // java:comp/env/jdbc/mydatasource
        try {
            LOG.info("Init the datasource");
            org.eclipse.jetty.plus.jndi.Resource mydatasource = new org.eclipse.jetty.plus.jndi.Resource(
                    appContext, "jdbc/mycollabdatasource", null);
        } catch (NamingException e) {
            throw new CommRuntimeException(e);
        }

        return appContext;
    }

    private class ServerLifeCycleListener implements LifeCycle.Listener {

        @Override
        public void lifeCycleStarting(LifeCycle event) {

        }

        @Override
        public void lifeCycleStarted(LifeCycle event) {
            Runnable thread = new Runnable() {
                @Override
                public void run() {
                    LOG.debug("Detect root folder webapp");
                    File confFolder = FileUtils.getDesireFile(System.getProperty("user.dir"),
                            "conf", "src/main/conf");

                    if (confFolder == null) {
                        throw new CommRuntimeException("Can not detect webapp base folder");
                    } else {
                        File confFile = new File(confFolder, "mnisqm.properties");
                        while (!confFile.exists()) {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new CommRuntimeException(e);
                            }
                        }

                        appContext = initWebAppContext();
                        appContext.setClassLoader(GenericServerRunner.class.getClassLoader());

                        contexts.addHandler(appContext);
                        try {
                            appContext.start();
                        } catch (Exception e) {
                            LOG.error("Error while starting server", e);
                        }
                        installServlet.setWaitFlag(false);
                        contexts.removeHandler(installationContextHandler);
                    }
                }
            };

            new Thread(thread).start();
        }

        @Override
        public void lifeCycleFailure(LifeCycle event, Throwable cause) {

        }

        @Override
        public void lifeCycleStopping(LifeCycle event) {

        }

        @Override
        public void lifeCycleStopped(LifeCycle event) {

        }
    }
}
