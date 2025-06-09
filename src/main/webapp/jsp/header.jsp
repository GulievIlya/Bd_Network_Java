<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div style="background-color:#f1f1f1; padding:10px;">
    <a href="profile.jsp">Профиль</a> | 
    <a href="games.jsp">Игры</a> | 
    <a href="register.jsp">Регистрация</a> | 
    <a href="login.jsp">Вход</a> |
    <% if("admin".equals(session.getAttribute("role"))){ %>
        <a href="admin.jsp">Панель Админа</a> | 
    <% } %>
    <a href="logout">Выйти</a>
</div>
<hr>
