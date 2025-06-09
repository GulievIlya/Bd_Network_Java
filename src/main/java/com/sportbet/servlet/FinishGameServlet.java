package com.sportbet.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Random;

public class FinishGameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int gameId = Integer.parseInt(request.getParameter("gameId"));
        String[] possibleResults = {"Победа", "Ничья", "Поражение"};
        String result = possibleResults[new Random().nextInt(possibleResults.length)];

        try (Connection con = DatabaseUtil.getConnection()) {
            // устанавливаем результат игры
            String updateGameSql = "UPDATE games SET result=?, is_finished=true WHERE id=?";
            PreparedStatement psUpdateGame = con.prepareStatement(updateGameSql);
            psUpdateGame.setString(1, result);
            psUpdateGame.setInt(2, gameId);
            psUpdateGame.executeUpdate();

            // обрабатываем все ставки на эту игру
            String selectBetsSql = "SELECT id, user_id, bet_amount, bet_choice, coefficient FROM bets WHERE game_id=?";
            PreparedStatement psSelectBets = con.prepareStatement(selectBetsSql);
            psSelectBets.setInt(1, gameId);
            ResultSet rs = psSelectBets.executeQuery();

            while (rs.next()) {
                int betId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                double betAmount = rs.getDouble("bet_amount");
                String betChoice = rs.getString("bet_choice");
                double coefficient = rs.getDouble("coefficient");

                if (betChoice.equals(result)) {
                    double winAmount = betAmount * coefficient;

                    // начисляем на баланс
                    String updateBalanceSql = "UPDATE users SET balance = balance + ? WHERE id = ?";
                    PreparedStatement psBalance = con.prepareStatement(updateBalanceSql);
                    psBalance.setDouble(1, winAmount);
                    psBalance.setInt(2, userId);
                    psBalance.executeUpdate();

                    // помечаем ставку как выигрыш
                    String updateBetSql = "UPDATE bets SET is_win=true, win_amount=? WHERE id=?";
                    PreparedStatement psWin = con.prepareStatement(updateBetSql);
                    psWin.setDouble(1, winAmount);
                    psWin.setInt(2, betId);
                    psWin.executeUpdate();

                } else {
                    // помечаем как проигрыш
                    String updateBetSql = "UPDATE bets SET is_win=false, win_amount=0 WHERE id=?";
                    PreparedStatement psLose = con.prepareStatement(updateBetSql);
                    psLose.setInt(1, betId);
                    psLose.executeUpdate();
                }
            }

            response.sendRedirect("jsp/admin.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("jsp/error.jsp");
        }
    }
}
