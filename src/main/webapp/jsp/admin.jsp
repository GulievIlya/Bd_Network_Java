<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.sql.*, com.sportbet.servlet.DatabaseUtil" %>
<%@ include file="header.jsp" %>
<h2>Админ-панель</h2>
<a href="create_game.jsp">Создать игру</a>
<br><br>
<table border="1">
<tr><th>Игра</th><th>Дата</th><th>Результат</th><th>Действие</th></tr>
<%
    Connection con = DatabaseUtil.getConnection();
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM games ORDER BY game_date DESC");
    while(rs.next()){
%>
<tr>
    <td><%= rs.getString("game_name") %></td>
    <td><%= rs.getTimestamp("game_date") %></td>
    <td><%= rs.getString("result") %></td>
    <td>
        <% if(!rs.getBoolean("is_finished")){ %>
        <form method="post" action="../playGame">
            <input type="hidden" name="gameId" value="<%= rs.getInt("id") %>">
            <input type="submit" value="Провести игру">
        </form>
        <% } else { %>
        Игра завершена
        <% } %>
    </td>
</tr>
<% } con.close(); %>
</table>
