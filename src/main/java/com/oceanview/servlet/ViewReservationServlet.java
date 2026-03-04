package com.oceanview.servlet;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class ViewReservationServlet extends HttpServlet {

    private ReservationService service = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String resNum = req.getParameter("resNum");

        // If coming from search form via POST (redirect-after-search)
        if (resNum == null) resNum = req.getParameter("searchResNum");

        if (resNum == null || resNum.trim().isEmpty()) {
            req.getSession().setAttribute("errorMsg", "Please provide a reservation number.");
            resp.sendRedirect(req.getContextPath() + "/reservations");
            return;
        }

        try {
            Reservation r = service.getReservation(resNum.trim());
            if (r == null) {
                req.getSession().setAttribute("errorMsg", "Reservation <strong>" + resNum + "</strong> not found.");
                resp.sendRedirect(req.getContextPath() + "/reservations");
            } else {
                req.setAttribute("reservation", r);
                req.getRequestDispatcher("/WEB-INF/views/view-reservation.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.getSession().setAttribute("errorMsg", "Error: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/reservations");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Handle search from reservations page
        String resNum = req.getParameter("searchResNum");
        resp.sendRedirect(req.getContextPath() + "/view-reservation?resNum=" + resNum);
    }
}
