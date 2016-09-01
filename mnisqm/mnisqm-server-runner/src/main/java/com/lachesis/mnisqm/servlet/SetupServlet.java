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

/**
 * @author Paul Xu.
 * @since 1.0.0
 */
public class SetupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        VelocityContext context = new VelocityContext();
        Reader reader = FileUtils.getReader("templates/page/SetupFresh.mt");

        String postUrl = "/install";
        context.put("redirectURL", postUrl);

        Map<String, String> defaultUrls = new HashMap<>();

        defaultUrls.put("cdn_url", "/assets/");
        defaultUrls.put("app_url", "/");

        defaultUrls.put("facebook_url", "https://www.facebook.com/mycollab2");
        defaultUrls.put("google_url", "https://plus.google.com/u/0/b/112053350736358775306/+mnisqm/about/p/pub");
        defaultUrls.put("twitter_url", "https://twitter.com/mycollabdotcom");

        context.put("defaultUrls", defaultUrls);

        StringWriter writer = new StringWriter();
        VelocityEngine voEngine = new VelocityEngine();

        voEngine.evaluate(context, writer, "log task", reader);

        PrintWriter out = response.getWriter();
        out.print(writer.toString());
    }
}
