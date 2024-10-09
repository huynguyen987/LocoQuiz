<%@ page import="entity.Users" %>
<%
  session = request.getSession();
  Users currentUser = (Users) session.getAttribute("user");
  if (currentUser == null) {
    // User not logged in, redirect to login
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }

  String roleName = currentUser.getRoleName();

  if ("teacher".equals(roleName)) {
    // Show teacher-specific content
    request.getRequestDispatcher("/teacher.jsp").forward(request, response);
  } else if ("student".equals(roleName)) {
    // Show student-specific content
    request.getRequestDispatcher("/student.jsp").forward(request, response);
  } else if ("admin".equals(roleName)) {
    // Show admin-specific content
    request.getRequestDispatcher("/admin.jsp").forward(request, response);
  } else {
    // Unknown role, redirect to login
    response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
    return;
  }
%>
