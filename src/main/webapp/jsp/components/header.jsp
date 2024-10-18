<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users" %>
<%
  Users currentUser = (Users) session.getAttribute("user");
%>
<header>
  <div class="container">
    <a href="<%= request.getContextPath() %>/index.jsp" class="logo">QuizLoco</a>
    <div class="user-info">
      <span>Welcome, <%= currentUser.getUsername() %></span>
      <a href="<%= request.getContextPath() %>/LogoutServlet" class="logout-btn">Logout</a>
      <a href="<%= request.getContextPath() %>/jsp/user-profile.jsp" class="dashboard-link">Profile</a>
    </div>
  </div>
</header>