package com.sportbet.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // если сессии нет — не создавать новую
        if (session != null) {
            session.invalidate(); // грохнуть сессию
        }
        response.sendRedirect("jsp/login.jsp"); // на логин страницу
    }
}
