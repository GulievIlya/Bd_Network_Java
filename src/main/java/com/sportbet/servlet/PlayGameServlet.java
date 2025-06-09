package com.sportbet.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Random;

public class PlayGameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int gameId = Integer.parseInt(request.getParameter("gameId"));
        String[] results = {"Team A", "Team B", "Draw"};
        String result = results[new Random().nextInt(results.length)];

        try (Connection con = DatabaseUtil.getConnection()) {

            // Обновить игру
            String updateGameSql = "UPDATE games SET result=?, is_finished=true WHERE id=?";
            PreparedStatement psUpdate = con.prepareStatement(updateGameSql);
            psUpdate.setString(1, result);
            psUpdate.setInt(2, gameId);
            psUpdate.executeUpdate();

            // Выбрать ставки на эту игру
            String selectBetsSql = "SELECT b.id, b.bet_choice, b.bet_amount, u.id as user_id, u.balance FROM bets b JOIN users u ON b.user_id=u.id WHERE b.game_id=?";
            PreparedStatement psSelect = con.prepareStatement(selectBetsSql);
            psSelect.setInt(1, gameId);
            ResultSet rs = psSelect.executeQuery();

            while (rs.next()) {
                int betId = rs.getInt("id");
                String betChoice = rs.getString("bet_choice");
                double betAmount = rs.getDouble("bet_amount");
                int userId = rs.getInt("user_id");
                double balance = rs.getDouble("balance");

                boolean isWin = betChoice.equals(result);
                if (isWin) {
                    balance += betAmount * 2;
                }

                // Обновить ставку
                PreparedStatement psBetUpdate = con.prepareStatement("UPDATE bets SET is_win=? WHERE id=?");
                psBetUpdate.setBoolean(1, isWin);
                psBetUpdate.setInt(2, betId);
                psBetUpdate.executeUpdate();

                // Обновить баланс
                PreparedStatement psBalanceUpdate = con.prepareStatement("UPDATE users SET balance=? WHERE id=?");
                psBalanceUpdate.setDouble(1, balance);
                psBalanceUpdate.setInt(2, userId);
                psBalanceUpdate.executeUpdate();
            }

            response.sendRedirect("jsp/admin.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("jsp/error.jsp");
        }
    }
}
