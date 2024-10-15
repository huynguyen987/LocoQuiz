<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Users currentUser = (Users) session.getAttribute("user");
%>
<header>
  <div class="container">
    <a href="<%= request.getContextPath() %>/jsp/home.jsp" class="logo">QuizLoco</a>
    <div class="user-info">
      <span>Welcome, <%= currentUser.getUsername() %></span>
      <a href="<%= request.getContextPath() %>/LogoutServlet" class="logout-btn">Logout</a>
      <a href="<%= request.getContextPath() %>/jsp/edit-profile.jsp" class="dashboard-link">Profile</a>
    </div>
  </div>
</header>