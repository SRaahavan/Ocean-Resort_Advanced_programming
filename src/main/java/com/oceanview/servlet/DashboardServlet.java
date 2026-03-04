package com.oceanview.servlet;

import com.oceanview.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

public class DashboardServlet extends HttpServlet {

    private ReservationService service = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Map<String, Integer> stats = service.getDashboardStats();
            req.setAttribute("stats", stats);
            req.setAttribute("todayCheckIns", service.getTodayCheckIns());
            req.setAttribute("recentReservations", service.getAllReservations());
        } catch (Exception e) {
            req.setAttribute("dbError", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}
