package Servlet;

// SearchServlet.java

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class SearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String query = request.getParameter("query");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<h1>Search Results for: " + query + "</h1>");

        // TODO: Implement actual search logic here
    }
}

