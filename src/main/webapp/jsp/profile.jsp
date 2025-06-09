<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.sportbet.servlet.DatabaseUtil" %>
<%@ include file="header.jsp" %>

<%
if (session == null || session.getAttribute("username") == null) {
    response.sendRedirect("login.jsp");
    return;
}
String username = (String)session.getAttribute("username");
Connection con = DatabaseUtil.getConnection();

// Получаем инфу о пользователе
PreparedStatement psUser = con.prepareStatement("SELECT id, balance FROM users WHERE username=?");
psUser.setString(1, username);
ResultSet rsUser = psUser.executeQuery();
rsUser.next();
int userId = rsUser.getInt("id");
session.setAttribute("userId", userId);
double balance = rsUser.getDouble("balance");
%>

<h2>Добро пожаловать, <%= username %>!</h2>

<!-- Фото пользователя -->
<img src="../getPhoto?userId=<%= userId %>" alt="Аватар" width="150"><br>
<a href="uploadPhoto.jsp">Загрузить / сменить фото</a>

<p>Ваш баланс: <%= String.format("%.2f", balance) %> руб.</p>

<a href="games.jsp">Поставить ставку</a>
<br><br>

<h3>Пополнить баланс:</h3>
<form method="post" action="../topup">
    Сумма: <input type="number" step="0.01" name="amount" required>
    <input type="submit" value="Пополнить">
</form>

<h3>Ваши ставки:</h3>
<table border="1">
<tr>
    <th>Игра</th>
    <th>Ставка</th>
    <th>Выбор</th>
    <th>Коэффициент</th>
    <th>Результат</th>
    <th>Статус</th>
    <th>Выигрыш</th>
</tr>

<%
PreparedStatement psBets = con.prepareStatement(
    "SELECT g.game_name, b.bet_amount, b.bet_choice, b.coefficient, g.result, b.is_win, b.win_amount " +
    "FROM bets b JOIN games g ON b.game_id = g.id WHERE b.user_id=? ORDER BY g.game_date DESC");
psBets.setInt(1, userId);
ResultSet rsBets = psBets.executeQuery();
while(rsBets.next()){
%>
<tr>
    <td><%= rsBets.getString("game_name") %></td>
    <td><%= rsBets.getDouble("bet_amount") %> руб.</td>
    <td><%= rsBets.getString("bet_choice") %></td>
    <td><%= rsBets.getDouble("coefficient") %></td>
    <td><%= rsBets.getString("result") == null ? "Ожидается" : rsBets.getString("result") %></td>
    <td>
        <%
        Boolean win = rsBets.getObject("is_win") == null ? null : rsBets.getBoolean("is_win");
        if(win == null){
            out.print("Ожидается");
        } else if(win){
            out.print("Выигрыш");
        } else {
            out.print("Проигрыш");
        }
        %>
    </td>
    <td>
        <%= rsBets.getObject("win_amount") == null ? "-" : rsBets.getDouble("win_amount") + " руб." %>
    </td>
</tr>
<%
}
con.close();
%>
</table>

<a href="logout.jsp">Выйти</a>
