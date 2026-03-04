package com.oceanview.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * AuthFilter - Intercepts all requests and verifies the user is logged in.
 * 
 * Design Pattern: Chain of Responsibility (Filter Chain)
 * Purpose: Security layer - redirects unauthenticated users to the login page.
 */
public class AuthFilter implements Filter {

    // These paths do NOT require authentication
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/login", "/css/", "/js/", "/images/"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();

        // Allow public paths through without authentication
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);

        if (isPublic) {
            chain.doFilter(req, res);
            return;
        }

        // Check session for logged-in user
        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("loggedIn") != null);

        if (loggedIn) {
            // Prevent browser caching of secure pages
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
