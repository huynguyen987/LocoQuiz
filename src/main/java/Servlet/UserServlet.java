package Servlet;

import dao.UsersDAO;
import entity.Users;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {

    private UsersDAO usersDAO;

    @Override
    public void init() {
        usersDAO = new UsersDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int userId = Integer.parseInt(request.getParameter("userId"));

        try {
            switch (action) {
                case "updateRole":
                    updateRole(userId);
                    break;
                case "toggleStatus":
                    toggleStatus(userId);
                    break;
                case "deleteUser":
                    deleteUser(userId);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirect back to admin page after action
        response.sendRedirect(request.getContextPath() + "/jsp/admin.jsp");

    }

    private void updateRole(int userId) throws Exception {
        Users user = usersDAO.getUserById(userId);
        int newRoleId = user.getRole_id() == 1 ? 2 : 1; // Toggle between Admin (1) and Teacher (2) for example
        user.setRole_id(newRoleId);
        usersDAO.updateUser(user);
    }

    private void toggleStatus(int userId) throws Exception {
        Users user = usersDAO.getUserById(userId);
        user.setRole_id(user.getRole_id() == 1 ? 3 : 1); // Example: Toggle between active (1) and inactive (3)
        usersDAO.updateUser(user);
    }

    private void deleteUser(int userId) throws Exception {
        usersDAO.deleteUser(userId);
    }
}
