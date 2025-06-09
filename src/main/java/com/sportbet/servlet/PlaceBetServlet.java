package com.sportbet.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class PlaceBetServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = (int) request.getSession().getAttribute("userId");
        int gameId = Integer.parseInt(request.getParameter("gameId"));
        double betAmount = Double.parseDouble(request.getParameter("betAmount"));
        String betChoice = request.getParameter("betChoice");

        double coefficient = 1.75;  // или можешь рандомно генерить

        try (Connection con = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO bets (user_id, game_id, bet_amount, bet_choice, coefficient) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, gameId);
            ps.setDouble(3, betAmount);
            ps.setString(4, betChoice);
            ps.setDouble(5, coefficient);
            ps.executeUpdate();

            // снимаем деньги со счета
            String updateBalanceSql = "UPDATE users SET balance = balance - ? WHERE id = ?";
            PreparedStatement psBalance = con.prepareStatement(updateBalanceSql);
            psBalance.setDouble(1, betAmount);
            psBalance.setInt(2, userId);
            psBalance.executeUpdate();

            response.sendRedirect("jsp/profile.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("jsp/error.jsp");
        }
    }
}
