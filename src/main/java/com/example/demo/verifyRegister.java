package com.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


    @WebServlet(name = "verifyRegister", value = "/verifyRegister")
public class verifyRegister extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");
        if (service == null) {
            service = "verify";
        }
        if (service.equals("verify")) {
            String capcha = request.getParameter("capcha");
            String email = (String) session.getAttribute("email");
            String capchaSession = (String) session.getAttribute("capcha");
            if (capcha.equals(capchaSession)) {
                session.setAttribute("email", email);
                session.setAttribute("capcha", capcha);
                session.setAttribute("service1", "resetPass");
                response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            } else {
                session.setAttribute("error", "Mã xác nhận không đúng");
                response.sendRedirect(request.getContextPath() + "/jsp/verify-register.jsp");
            }
        }
    }

    protected void doGet
            (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost
            (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}