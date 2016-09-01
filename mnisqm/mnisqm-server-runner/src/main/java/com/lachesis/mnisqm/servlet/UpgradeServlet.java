package com.lachesis.mnisqm.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.lachesis.mnisqm.core.utils.FileUtils;
import com.lachesis.mnisqm.jetty.ServerInstance;

/**
 * @author Paul Xu.
 * @since 1.0.0
 */
public class UpgradeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        Reader reader = ServerInstance.getInstance().isUpgrading() ? FileUtils.getReader("templates/page/WaitingUpgrade.mt")
                : FileUtils.getReader("templates/page/NoUpgrade.mt");

        VelocityContext context = new VelocityContext();
        Map<String, String> defaultUrls = new HashMap<>();

        context.put("defaultUrls", defaultUrls);

        StringWriter writer = new StringWriter();
        VelocityEngine voEngine = new VelocityEngine();
        voEngine.evaluate(context, writer, "log task", reader);
        PrintWriter out = response.getWriter();
        out.print(writer.toString());
    }
}
