<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.sportbet.servlet.DatabaseUtil" %>
<%@ include file="header.jsp" %>
<h2>Доступные игры</h2>
<table border="1">
<tr><th>Игра</th><th>Дата</th><th>Результат</th><th>Ставка</th></tr>
<%
Connection con = DatabaseUtil.getConnection();
Statement stmt = con.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM games ORDER BY game_date DESC");
while(rs.next()){
    int gameId = rs.getInt("id");
%>
<tr>
    <td><%= rs.getString("game_name") %></td>
    <td><%= rs.getTimestamp("game_date") %></td>
    <td><%= rs.getString("result") == null ? "Ожидается" : rs.getString("result") %></td>
    <td>
        <% if(!rs.getBoolean("is_finished")){ %>
        <form method="post" action="/KP_Korolev/makeBet">
            <input type="hidden" name="gameId" value="<%= gameId %>">
            Сумма ставки: <input type="number" name="betAmount" required><br>
            Ваш выбор: <input type="text" name="betChoice" required><br>
            Коэффициент: <input type="text" name="coefficient" value="1.75" readonly><br>
            <input type="submit" value="Поставить">
        </form>
        <% } else { %>
        Игра завершена
        <% } %>
    </td>
</tr>
<% } con.close(); %>
