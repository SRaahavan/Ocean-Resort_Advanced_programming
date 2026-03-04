package com.oceanview.servlet;

import com.oceanview.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AllReservationsServlet extends HttpServlet {

    private ReservationService service = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String keyword = req.getParameter("search");
        try {
            if (keyword != null && !keyword.trim().isEmpty()) {
                req.setAttribute("reservations", service.searchReservations(keyword));
                req.setAttribute("searchKeyword", keyword);
            } else {
                req.setAttribute("reservations", service.getAllReservations());
            }
            req.setAttribute("stats", service.getDashboardStats());
        } catch (Exception e) {
            req.setAttribute("dbError", e.getMessage());
        }

        // Pass flash messages from session
        String successMsg = (String) req.getSession().getAttribute("successMsg");
        String errorMsg   = (String) req.getSession().getAttribute("errorMsg");
        if (successMsg != null) { req.setAttribute("successMsg", successMsg); req.getSession().removeAttribute("successMsg"); }
        if (errorMsg   != null) { req.setAttribute("errorMsg",   errorMsg);   req.getSession().removeAttribute("errorMsg"); }

        req.getRequestDispatcher("/WEB-INF/views/reservations.jsp").forward(req, resp);
    }
}
