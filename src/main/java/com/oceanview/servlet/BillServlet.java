package com.oceanview.servlet;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

public class BillServlet extends HttpServlet {

    private ReservationService service = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String resNum = req.getParameter("resNum");
        if (resNum == null || resNum.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/reservations");
            return;
        }
        try {
            Reservation r = service.getReservation(resNum.trim());
            if (r == null) {
                req.getSession().setAttribute("errorMsg", "Reservation not found: " + resNum);
                resp.sendRedirect(req.getContextPath() + "/reservations");
                return;
            }
            Map<String, Object> bill = service.calculateBill(r);
            req.setAttribute("bill", bill);
            req.getRequestDispatcher("/WEB-INF/views/bill.jsp").forward(req, resp);
        } catch (Exception e) {
            req.getSession().setAttribute("errorMsg", "Bill error: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/reservations");
        }
    }
}
