<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users" %>
<%
  Users currentUser = (Users) session.getAttribute("user");
  String role = (String) session.getAttribute("role");
  String homePage = "index.jsp"; // Default home page

  if ("student".equals(role)) {
    homePage = "student.jsp";
  } else if ("teacher".equals(role)) {
    homePage = "teacher.jsp";
  } else if ("admin".equals(role)) {
    homePage = "admin.jsp";
  }
%>
<header>
  <div class="container">
    <a href="<%= request.getContextPath() %>/jsp/<%= homePage %>" class="logo">QuizLoco</a>
    <div class="user-info">
      <span>Welcome, <%= currentUser.getUsername() %></span>
      <a href="<%= request.getContextPath() %>/LogoutServlet" class="logout-btn">Logout</a>
      <a href="<%= request.getContextPath() %>/jsp/user-profile.jsp" class="dashboard-link">Profile</a>
    </div>
  </div>
</header>