package com.sportbet.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection con = DatabaseUtil.getConnection()) {

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("role", rs.getString("role"));
                session.setAttribute("balance", rs.getBigDecimal("balance"));

                // Редирект в зависимости от роли
                if ("admin".equals(rs.getString("role"))) {
                    response.sendRedirect("jsp/admin.jsp");
                } else {
                    response.sendRedirect("jsp/profile.jsp");
                }

            } else {
                response.sendRedirect("jsp/login_error.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("jsp/login_error.jsp");
        }
    }
}
