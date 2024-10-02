package com.example.demo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.TagDAO;
import dao.UserTagDAO;
import entity.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "ControllerTag", urlPatterns = {"/ControllerTag"})
public class ControllerTag extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TagDAO tagDAO = new TagDAO();
        UserTagDAO userTagDAO = new UserTagDAO();
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("listTag")) {
                // Hiển thị danh sách các tag của người dùng
                Integer userIdObj = (Integer) request.getSession().getAttribute("userId");
                if (userIdObj == null) {
                    // User chưa đăng nhập, chuyển hướng đến trang đăng nhập
                    response.sendRedirect("jsp/login.jsp");
                    return;
                }
                int userId = userIdObj.intValue();

                List<Tag> tags = userTagDAO.getUserTagsByUserId(userId);
                request.setAttribute("tags", tags);
                request.getRequestDispatcher("jsp/tag.jsp").forward(request, response);
            } else if (action.equals("searchTag")) {
                // Xử lý tìm kiếm tag theo tên trong danh sách tag của người dùng
                String searchName = request.getParameter("searchName");
                Integer userIdObj = (Integer) request.getSession().getAttribute("userId");
                if (userIdObj == null) {
                    // User chưa đăng nhập, chuyển hướng đến trang đăng nhập
                    response.sendRedirect("jsp/login.jsp");
                    return;
                }
                int userId = userIdObj.intValue();

                List<Tag> tags = userTagDAO.searchUserTagsByName(userId, searchName);
                request.setAttribute("tags", tags);
                request.setAttribute("searchName", searchName);
                request.getRequestDispatcher("jsp/tag.jsp").forward(request, response);
            } else if (action.equals("insertTag")) {
                String submit = request.getParameter("submit");
                if (submit == null) {
                    // Hiển thị trang thêm tag với danh sách tag cố định
                    List<Tag> fixedTags = tagDAO.getFixedTags();
                    request.setAttribute("fixedTags", fixedTags);
                    request.getRequestDispatcher("/jsp/insert-tag.jsp").forward(request, response);
                } else {
                    // Xử lý thêm tag cho người dùng
                    String tagIdStr = request.getParameter("tagId");
                    if (tagIdStr == null || tagIdStr.isEmpty()) {
                        request.setAttribute("error", "Vui lòng chọn một tag.");
                        List<Tag> fixedTags = tagDAO.getFixedTags();
                        request.setAttribute("fixedTags", fixedTags);
                        request.getRequestDispatcher("jsp/insert-tag.jsp").forward(request, response);
                        return;
                    }
                    int tagId = Integer.parseInt(tagIdStr);

                    Integer userIdObj = (Integer) request.getSession().getAttribute("userId");
                    if (userIdObj == null) {
                        // User chưa đăng nhập, chuyển hướng đến trang đăng nhập
                        response.sendRedirect("jsp/login.jsp");
                        return;
                    }
                    int userId = userIdObj.intValue();

                    // Kiểm tra xem người dùng đã có tag này chưa
                    if (!userTagDAO.userHasTag(userId, tagId)) {
                        boolean added = userTagDAO.addUserTag(userId, tagId);
                        if (added) {
                            response.sendRedirect("ControllerTag?action=listTag");
                        } else {
                            request.setAttribute("error", "Thêm tag thất bại.");
                            List<Tag> fixedTags = tagDAO.getFixedTags();
                            request.setAttribute("fixedTags", fixedTags);
                            request.getRequestDispatcher("jsp/insert-tag.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("error", "Bạn đã có tag này rồi.");
                        List<Tag> fixedTags = tagDAO.getFixedTags();
                        request.setAttribute("fixedTags", fixedTags);
                        request.getRequestDispatcher("jsp/insert-tag.jsp").forward(request, response);
                    }
                }
            } else if (action.equals("deleteTag")) {
                // Xử lý xóa tag khỏi danh sách của người dùng
                String tagIdStr = request.getParameter("tagId");
                if (tagIdStr == null || tagIdStr.isEmpty()) {
                    request.setAttribute("error", "Không tìm thấy tag để xóa.");
                    request.getRequestDispatcher("ControllerTag?action=listTag").forward(request, response);
                    return;
                }
                int tagId = Integer.parseInt(tagIdStr);

                Integer userIdObj = (Integer) request.getSession().getAttribute("userId");
                if (userIdObj == null) {
                    // User chưa đăng nhập, chuyển hướng đến trang đăng nhập
                    response.sendRedirect("jsp/login.jsp");
                    return;
                }
                int userId = userIdObj.intValue();

                boolean deleted = userTagDAO.deleteUserTag(userId, tagId);
                if (deleted) {
                    response.sendRedirect("ControllerTag?action=listTag");
                } else {
                    request.setAttribute("error", "Xóa tag thất bại.");
                    request.getRequestDispatcher("ControllerTag?action=listTag").forward(request, response);
                }
            } else {
                // Hành động không xác định, chuyển hướng về danh sách tag
                response.sendRedirect("ControllerTag?action=listTag");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("jsp/error.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
