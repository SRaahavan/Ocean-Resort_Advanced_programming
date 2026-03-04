package com.oceanview.servlet;

import com.oceanview.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class UpdateStatusServlet extends HttpServlet {

    private ReservationService service = new ReservationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String resNum = req.getParameter("resNum");
        String status = req.getParameter("status");
        String redirectTo = req.getParameter("redirectTo");

        try {
            service.updateStatus(resNum, status);
            req.getSession().setAttribute("successMsg",
                "Reservation <strong>" + resNum + "</strong> status updated to <strong>" + status + "</strong>.");
        } catch (Exception e) {
            req.getSession().setAttribute("errorMsg", "Failed to update status: " + e.getMessage());
        }

        if (redirectTo != null && redirectTo.equals("view")) {
            resp.sendRedirect(req.getContextPath() + "/view-reservation?resNum=" + resNum);
        } else {
            resp.sendRedirect(req.getContextPath() + "/reservations");
        }
    }
}
