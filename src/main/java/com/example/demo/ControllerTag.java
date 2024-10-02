package com.example.demo;

import dao.TagDAO;
import dao.UserTagDAO;
import entity.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ControllerTag", urlPatterns = {"/ControllerTag"})
public class ControllerTag extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TagDAO tagDAO = new TagDAO();
        UserTagDAO userTagDAO = new UserTagDAO();
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("listTag")) {
                // Hiển thị danh sách tất cả các Tag
                List<Tag> tags = tagDAO.getAllTags();
                request.setAttribute("tags", tags);
                request.getRequestDispatcher("jsp/tag.jsp").forward(request, response);

            } else if (action.equals("insertTag")) {
                String submit = request.getParameter("submit");
                if (submit == null) {
                    // Hiển thị trang thêm Tag
                    request.getRequestDispatcher("jsp/insert-tag.jsp").forward(request, response);
                } else {
                    // Xử lý thêm Tag mới
                    String name = request.getParameter("name");
                    String description = request.getParameter("description");
                    if (name == null || name.trim().isEmpty()) {
                        request.setAttribute("error", "Tên tag là bắt buộc.");
                        request.getRequestDispatcher("jsp/insert-tag.jsp").forward(request, response);
                    } else {
                        Tag tag = new Tag(name, description);
                        boolean inserted = tagDAO.insertTag(tag);
                        if (inserted) {
                            response.sendRedirect("ControllerTag?action=listTag");
                        } else {
                            request.setAttribute("error", "Thêm tag thất bại.");
                            request.getRequestDispatcher("jsp/insert-tag.jsp").forward(request, response);
                        }
                    }
                }

            } else if (action.equals("updateTag")) {
                String submit = request.getParameter("submit");
                if (submit == null) {
                    // Hiển thị trang cập nhật tag
                    String idParam = request.getParameter("id");
                    if (idParam != null && !idParam.isEmpty()) {
                        int id = Integer.parseInt(idParam);
                        Tag tag = tagDAO.getTagById(id);
                        if (tag != null) {
                            request.setAttribute("tag", tag);
                            request.getRequestDispatcher("jsp/update-tag.jsp").forward(request, response);
                        } else {
                            request.setAttribute("error", "Không tìm thấy tag.");
                            request.getRequestDispatcher("ControllerTag?action=listTag").forward(request, response);
                        }
                    } else {
                        // Không có tham số id, chuyển hướng về danh sách tag
                        response.sendRedirect("ControllerTag?action=listTag");
                    }
                } else {
                    // Xử lý cập nhật tag
                    String idParam = request.getParameter("id");
                    if (idParam != null && !idParam.isEmpty()) {
                        int id = Integer.parseInt(idParam);
                        String name = request.getParameter("name");
                        String description = request.getParameter("description");
                        Tag tag = new Tag(id, name, description);
                        boolean updated = tagDAO.updateTag(tag);
                        if (updated) {
                            response.sendRedirect("ControllerTag?action=listTag");
                        } else {
                            request.setAttribute("error", "Cập nhật tag thất bại.");
                            request.setAttribute("tag", tag);
                            request.getRequestDispatcher("jsp/update-tag.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("error", "ID tag không hợp lệ.");
                        response.sendRedirect("ControllerTag?action=listTag");
                    }
                }
            } else if (action.equals("deleteTag")) {
                // Xử lý xóa tag
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    int id = Integer.parseInt(idParam);
                    boolean deleted = tagDAO.deleteTag(id);
                    if (deleted) {
                        response.sendRedirect("ControllerTag?action=listTag");
                    } else {
                        request.setAttribute("error", "Xóa tag thất bại.");
                        request.getRequestDispatcher("ControllerTag?action=listTag").forward(request, response);
                    }
                } else {
                    // Không có tham số id, chuyển hướng về danh sách tag
                    response.sendRedirect("ControllerTag?action=listTag");
                }
            } else {
                // Hành động không xác định
                response.sendRedirect("ControllerTag?action=listTag");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Tham số không hợp lệ.");
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
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
