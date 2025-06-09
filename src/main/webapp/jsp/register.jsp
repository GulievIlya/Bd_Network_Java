<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Регистрация</title>
</head>
<body>
    <h2>Регистрация пользователя</h2>
    <form method="post" action="/KP_Korolev/register" enctype="multipart/form-data">
    Логин: <input type="text" name="username" required><br>
    Пароль: <input type="password" name="password" required><br>
    Роль: 
    <select name="role">
        <option value="user">Обычный пользователь</option>
        <option value="admin">Администратор</option>
    </select><br>
    Фото: <input type="file" name="photo"><br>
    <input type="submit" value="Зарегистрироваться">
</form>
	
</body>
</html>
