<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<h2>Загрузить фото профиля</h2>

<form action="../uploadPhoto" method="post" enctype="multipart/form-data">
    Выберите фото: <input type="file" name="photo" accept="image/*" required><br><br>
    <input type="submit" value="Загрузить">
</form>
