package com.sportbet.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import com.sportbet.servlet.DatabaseUtil;
import jakarta.servlet.annotation.MultipartConfig;

@MultipartConfig
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        Part photoPart = req.getPart("photo");
        InputStream photoInputStream = photoPart.getInputStream();

        try (Connection con = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO users (username, password, role, photo) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); // можешь подключить хэширование
            ps.setString(3, role);
            ps.setBinaryStream(4, photoInputStream, (int) photoPart.getSize());
            ps.executeUpdate();
            res.sendRedirect("jsp/login.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("jsp/error.jsp");
        }
    }
}

