package com.sportbet.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;


@MultipartConfig(maxFileSize = 1024 * 1024 * 5)  // 5 МБ лимит
public class UploadPhotoServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = (int) request.getSession().getAttribute("userId");
        Part filePart = request.getPart("photo");

        try (Connection con = DatabaseUtil.getConnection()) {
            String sql = "UPDATE users SET photo=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBinaryStream(1, filePart.getInputStream());
            ps.setInt(2, userId);
            ps.executeUpdate();

            response.sendRedirect("jsp/profile.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Ошибка загрузки фото");
        }
    }
}
