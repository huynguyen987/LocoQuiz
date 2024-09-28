package com.example.demo;

import Module.ReturnMail;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


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
            ReturnMail mail = new ReturnMail();
            String email = request.getParameter("email");
            String capcha = mail.generateVerificationCode();
            mail.sendMail(email , capcha);
            session.setAttribute("email", email);
            session.setAttribute("capcha", capcha);
            response.sendRedirect(request.getContextPath() + "/jsp/verify.jsp");
            // send email and capcha to resetPassController.java via session.setAttribute to get email and capcha in verify.jsp
            // o day email va capcha qua session.setattribute de lay email va capcha o trang verify.jsp
            // neu chuyen sang trang verifyController thi can phai truyen email va capcha qua request.getparameter
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