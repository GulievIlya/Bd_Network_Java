package com.sportbet.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class MakeBetServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = (int) request.getSession().getAttribute("userId");
        int gameId = Integer.parseInt(request.getParameter("gameId"));
        double betAmount = Double.parseDouble(request.getParameter("betAmount"));
        String betChoice = request.getParameter("betChoice");
        double coefficient = Double.parseDouble(request.getParameter("coefficient"));

        try (Connection con = DatabaseUtil.getConnection()) {
            // Проверка баланса пользователя
            PreparedStatement psBalance = con.prepareStatement("SELECT balance FROM users WHERE id = ?");
            psBalance.setInt(1, userId);
            ResultSet rsBalance = psBalance.executeQuery();
            if (rsBalance.next()) {
                double currentBalance = rsBalance.getDouble("balance");
                if (currentBalance < betAmount) {
                    response.sendRedirect("jsp/error.jsp");
                    return;
                }
            }

            // Сохраняем ставку
            String sql = "INSERT INTO bets (user_id, game_id, bet_amount, bet_choice, coefficient) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, gameId);
            ps.setDouble(3, betAmount);
            ps.setString(4, betChoice);
            ps.setDouble(5, coefficient);
            ps.executeUpdate();

            // Списываем деньги
            String updateBalanceSql = "UPDATE users SET balance = balance - ? WHERE id = ?";
            PreparedStatement psUpdateBalance = con.prepareStatement(updateBalanceSql);
            psUpdateBalance.setDouble(1, betAmount);
            psUpdateBalance.setInt(2, userId);
            psUpdateBalance.executeUpdate();

            response.sendRedirect("jsp/profile.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("jsp/error.jsp");
        }
    }
}
