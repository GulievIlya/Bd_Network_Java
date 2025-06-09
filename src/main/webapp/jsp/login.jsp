<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Вход</title>
</head>
<body>
    <h2>Вход в систему</h2>
    <form method="post" action="../login">
        Логин: <input type="text" name="username" required><br><br>
        Пароль: <input type="password" name="password" required><br><br>
        <input type="submit" value="Войти">
    </form>
</body>
</html>
