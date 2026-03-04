package com.oceanview.servlet;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddReservationServlet extends HttpServlet {

    private ReservationService service = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("roomTypes", service.getRoomTypes());
        } catch (Exception e) {
            req.setAttribute("dbError", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/add-reservation.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = (String) req.getSession().getAttribute("username");

        Map<String, String> params = new HashMap<>();
        params.put("guestName",       req.getParameter("guestName"));
        params.put("address",         req.getParameter("address"));
        params.put("contactNumber",   req.getParameter("contactNumber"));
        params.put("email",           req.getParameter("email"));
        params.put("roomType",        req.getParameter("roomType"));
        params.put("checkInDate",     req.getParameter("checkInDate"));
        params.put("checkOutDate",    req.getParameter("checkOutDate"));
        params.put("numGuests",       req.getParameter("numGuests"));
        params.put("specialRequests", req.getParameter("specialRequests"));

        try {
            Reservation r = service.createReservation(params, username);
            req.getSession().setAttribute("successMsg",
                "Reservation <strong>" + r.getReservationNumber() + "</strong> created successfully for <strong>" + r.getGuestName() + "</strong>!");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (Exception e) {
            // Re-display form with error and sticky values
            req.setAttribute("error", e.getMessage());
            req.setAttribute("formData", params);
            try {
                req.setAttribute("roomTypes", service.getRoomTypes());
            } catch (Exception ex) {
                req.setAttribute("dbError", ex.getMessage());
            }
            req.getRequestDispatcher("/WEB-INF/views/add-reservation.jsp").forward(req, resp);
        }
    }
}
