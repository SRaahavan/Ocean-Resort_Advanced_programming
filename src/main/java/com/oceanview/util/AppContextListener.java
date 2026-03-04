package com.oceanview.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * AppContextListener - Initializes the DBConnectionPool singleton at application startup.
 * Reads DB configuration from web.xml context parameters.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        String url      = ctx.getInitParameter("db.url");
        String username = ctx.getInitParameter("db.username");
        String password = ctx.getInitParameter("db.password");

        // Initialize Singleton DB pool
        DBConnectionPool.getInstance(url, username, password);
        System.out.println("[OceanView] Database connection pool initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[OceanView] Application shutting down.");
    }
}
