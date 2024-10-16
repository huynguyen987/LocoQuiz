package Servlet;

import Module.ReturnMail;
import dao.UsersDAO;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "forgetPass", value = "/forgetPass")
public class forgetPass extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");
        if (service == null){
            service = "forgetPass";
        }
        if (service.equals("forgetPass")) {
            String email = request.getParameter("email");

            // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu không
            UsersDAO usersDAO = new UsersDAO();
            Users user = null;
            try {
                user = usersDAO.getUserByEmail(email);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                // Xử lý ngoại lệ nếu cần thiết
            }

            if (user == null) {
                // Email không tồn tại
                session.setAttribute("error", "Email không tồn tại trong hệ thống.");
                response.sendRedirect(request.getContextPath() + "/jsp/lostpass.jsp");
            } else {
                // Email tồn tại, tiếp tục gửi mã xác thực
                ReturnMail mail = new ReturnMail();
                String capcha = mail.generateVerificationCode();
                mail.sendMail(email , capcha);
                session.setAttribute("email", email);
                session.setAttribute("capcha", capcha);
                response.sendRedirect(request.getContextPath() + "/jsp/verify.jsp");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost
            (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
