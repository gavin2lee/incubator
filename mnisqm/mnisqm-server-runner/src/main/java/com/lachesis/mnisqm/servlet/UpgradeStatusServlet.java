package com.lachesis.mnisqm.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lachesis.mnisqm.jetty.ServerInstance;

/**
 * @author Paul Xu.
 * @since 1.0.0
 */
public class UpgradeStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        if (ServerInstance.getInstance().isUpgrading()) {
            out.write("Still upgrading");
        }
    }
}
