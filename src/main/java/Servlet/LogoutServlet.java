package Servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L; // For serialization

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate the session to log out the user
        HttpSession session = request.getSession();
        session.invalidate();

        // Redirect to homepage using absolute path
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
