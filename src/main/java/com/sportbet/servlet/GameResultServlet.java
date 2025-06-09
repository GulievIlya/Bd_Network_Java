package com.sportbet.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Random;

public class GameResultServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int gameId = Integer.parseInt(request.getParameter("game_id"));
        String[] outcomes = {"TeamA", "TeamB"};
        String result = outcomes[new Random().nextInt(outcomes.length)];

        try (Connection con = DatabaseUtil.getConnection()) {
            PreparedStatement psResult = con.prepareStatement("UPDATE games SET result=? WHERE id=?");
            psResult.setString(1, result);
            psResult.setInt(2, gameId);
            psResult.executeUpdate();

            PreparedStatement psBets = con.prepareStatement("SELECT * FROM bets WHERE game_id=?");
            psBets.setInt(1, gameId);
            ResultSet rsBets = psBets.executeQuery();
            while (rsBets.next()) {
                boolean isWin = rsBets.getString("bet_choice").equals(result);
                double betAmount = rsBets.getDouble("bet_amount");
                int userId = rsBets.getInt("user_id");

                if (isWin) {
                    double winAmount = betAmount * 2;
                    PreparedStatement psUpdate = con.prepareStatement(
                        "UPDATE users SET balance = balance + ? WHERE id = ?");
                    psUpdate.setDouble(1, winAmount);
                    psUpdate.setInt(2, userId);
                    psUpdate.executeUpdate();
                }

                PreparedStatement psUpdateBet = con.prepareStatement(
                    "UPDATE bets SET is_win=? WHERE id=?");
                psUpdateBet.setBoolean(1, isWin);
                psUpdateBet.setInt(2, rsBets.getInt("id"));
                psUpdateBet.executeUpdate();
            }

            response.sendRedirect("jsp/admin.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
