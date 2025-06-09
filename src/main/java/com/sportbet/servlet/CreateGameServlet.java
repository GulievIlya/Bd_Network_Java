package com.sportbet.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;

public class CreateGameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String gameName = request.getParameter("gameName");

        try (Connection con = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO games (game_name, game_date, is_finished) VALUES (?, ?, false)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, gameName);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();

            response.sendRedirect("jsp/admin.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("jsp/error.jsp");
        }
    }
}
