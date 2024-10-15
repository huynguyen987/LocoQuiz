<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Use the currentUser defined in header.jsp
  String role = currentUser != null ? currentUser.getRoleName() : "";
%>
<aside class="sidebar">
  <ul>
    <%
      if ("teacher".equals(role)) {
    %>
    <li><a href="<%= request.getContextPath() %>/jsp/teacher.jsp"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
    <li><a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=createClass"><i class="fas fa-plus"></i> Create Class</a></li>
    <li><a href="<%= request.getContextPath() %>/QuizController"><i class="fas fa-plus"></i> Create Quiz</a></li>
    <li><a href="<%= request.getContextPath() %>/AllQuizzesServlet"><i class="fas fa-eye"></i> View Quiz</a></li>
    <%
    } else if ("student".equals(role)) {
    %>
    <li><a href="<%= request.getContextPath() %>/jsp/student.jsp"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
    <li><a href="<%= request.getContextPath() %>/jsp/my-classes.jsp"><i class="fas fa-chalkboard"></i> My Classes</a></li>
    <li><a href="<%= request.getContextPath() %>/jsp/progress.jsp"><i class="fas fa-chart-line"></i> Progress</a></li>
    <li><a href="<%= request.getContextPath() %>/LogoutServlet"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
    <%
      }
    %>
  </ul>
</aside>