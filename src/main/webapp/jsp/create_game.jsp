<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Создать игру</title>
</head>
<body>
    <h2>Создать новую игру</h2>
    <form method="post" action="../createGame">
        Название игры: <input type="text" name="gameName" required><br><br>
        <input type="submit" value="Создать">
    </form>
    <br>
    <a href="admin.jsp">Назад</a>
</body>
</html>
