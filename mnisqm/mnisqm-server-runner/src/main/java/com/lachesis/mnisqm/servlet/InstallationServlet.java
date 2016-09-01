package com.lachesis.mnisqm.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lachesis.mnisqm.core.CommRuntimeException;
import com.lachesis.mnisqm.core.UserInvalidInputException;
import com.lachesis.mnisqm.core.utils.FileUtils;

/**
 * @author Paul Xu.
 * @since 1.0.0
 */
public class InstallationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(InstallationServlet.class);

    private boolean waitFlag = true;

    public void setWaitFlag(boolean flag) {
        this.waitFlag = flag;
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        LOG.info("Try to install mnisqm");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "-1");
        PrintWriter out = response.getWriter();
        String sitename = request.getParameter("sitename");
        String serverAddress = request.getParameter("serverAddress");
        String databaseName = request.getParameter("databaseName");
        String dbUserName = request.getParameter("dbUserName");
        String dbPassword = request.getParameter("dbPassword");
        String databaseServer = request.getParameter("databaseServer");
        String smtpUserName = request.getParameter("smtpUserName");
        String smtpPassword = request.getParameter("smtpPassword");
        String smtpHost = request.getParameter("smtpHost");
        String smtpPort = request.getParameter("smtpPort");
        String tls = request.getParameter("tls");
        String ssl = request.getParameter("ssl");

        String dbUrl = String.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&rewriteBatchedStatements=true&useCompression=true&useServerPrepStmts=false",
                databaseServer, databaseName);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.error("Can not load mysql driver", e);
            return;
        }

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
            LOG.debug("Check database config");
            connection.getMetaData();
        } catch (Exception e) {
            String rootCause = (e.getCause() == null) ? e.getMessage() : e.getCause().getMessage();
            out.write("Cannot establish connection to database. Make sure your inputs are correct. Root cause is " + rootCause);
            LOG.error("Can not connect database", e);
            return;
        }

        int mailServerPort;
        try {
            mailServerPort = Integer.parseInt(smtpPort);
        } catch (Exception e) {
            mailServerPort = 0;
        }

        boolean isStartTls = Boolean.parseBoolean(tls);
        boolean isSsl = Boolean.parseBoolean(ssl);
        try {
            InstallUtils.checkSMTPConfig(smtpHost, mailServerPort, smtpUserName, smtpPassword, true, isStartTls, isSsl);
        } catch (UserInvalidInputException e) {
            LOG.warn("Cannot authenticate mail server successfully. Make sure your inputs are correct.");
        }

        VelocityContext templateContext = new VelocityContext();
        templateContext.put("sitename", sitename);
        templateContext.put("serveraddress", serverAddress);
        templateContext.put("dbUrl", dbUrl);
        templateContext.put("dbUser", dbUserName);
        templateContext.put("dbPassword", dbPassword);
        templateContext.put("smtpAddress", smtpHost);
        templateContext.put("smtpPort", mailServerPort + "");
        templateContext.put("smtpUserName", smtpUserName);
        templateContext.put("smtpPassword", smtpPassword);
        templateContext.put("smtpTLSEnable", tls);
        templateContext.put("smtpSSLEnable", ssl);

        File confFolder = FileUtils.getDesireFile(System.getProperty("user.dir"), "conf", "src/main/conf");
        if (confFolder == null) {
            out.write("Can not write the settings to the file system. You should check our knowledge base article at " +
                    "http://support.mnisqm.com/topic/994098-/ to solve this issue.");
            return;
        }

        if (!Files.isWritable(FileSystems.getDefault().getPath(confFolder.getAbsolutePath()))) {
            out.write("The folder " + confFolder.getAbsolutePath() + " has no write permission with the current user." +
                    " You should set the write permission for mnisqm process for this folder.  You should check our knowledge base article at http://support.mnisqm.com/topic/994098-/ to solve this issue.");
            return;
        }

        try {
            File templateFile = new File(confFolder, "mnisqm.properties.template");
            FileReader templateReader = new FileReader(templateFile);

            StringWriter writer = new StringWriter();

            VelocityEngine engine = new VelocityEngine();
            engine.evaluate(templateContext, writer, "log task", templateReader);

            FileOutputStream outStream = new FileOutputStream(new File(confFolder, "mnisqm.properties"));
            outStream.write(writer.toString().getBytes());
            outStream.flush();
            outStream.close();

            while (waitFlag) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new CommRuntimeException(e);
                }
            }

        } catch (Exception e) {
            LOG.error("Error while set up mnisqm", e);
            out.write("Can not write the settings to the file system. You should check our knowledge base article at " +
                    "http://support.mnisqm.com/topic/994098-/ to solve this issue.");
            return;
        }
    }
}
