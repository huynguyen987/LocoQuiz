<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Check if user is student
  session = request.getSession();
  entity.Users currentUser = (entity.Users) session.getAttribute("user");
  if (currentUser == null || !"student".equals(currentUser.getRoleName())) {
    response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
    return;
  }
%>
<html>
<head>
  <title>Student Home</title>
</head>
<body>
<h1>Student Dashboard</h1>
<p>Welcome, <%= currentUser.getUsername() %>!</p>
<a href="<%= request.getContextPath() %>/logout">Logout</a>
</body>
</html>
