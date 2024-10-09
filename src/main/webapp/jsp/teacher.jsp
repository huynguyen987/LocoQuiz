<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users,entity.classs,dao.ClassDAO" %>
<%@ page import="java.util.List" %>
<%
   session = request.getSession();
  Users currentUser = (Users) session.getAttribute("user");
  if (currentUser == null || !"teacher".equals(currentUser.getRoleName())) {
    response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Teacher Dashboard</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
  <style>
    .dashboard {
      width: 80%;
      margin: 50px auto;
    }
    h1 {
      text-align: center;
    }
    .actions {
      list-style-type: none;
      padding: 0;
    }
    .actions li {
      margin: 10px 0;
    }
    .actions a {
      text-decoration: none;
      color: #4285f4;
      font-size: 18px;
    }
    .class-list {
      margin-top: 30px;
    }
    .class-list h2 {
      margin-bottom: 15px;
    }
    .class-list ul {
      list-style-type: none;
      padding: 0;
    }
    .class-list li {
      margin: 5px 0;
    }
    .class-list a {
      text-decoration: none;
      color: #333;
    }
    .class-list a:hover {
      text-decoration: underline;
    }
    .class-actions {
      margin-left: 20px;
    }
    .class-actions li {
      margin: 5px 0;
    }
    .class-actions a {
      color: #007BFF;
    }
  </style>
</head>
<body>
<div class="dashboard">
  <h1>Welcome, <%= currentUser.getUsername() %>!</h1>
  <h2>Teacher Dashboard</h2>

  <!-- Action Links -->
  <ul class="actions">
    <li><a href="<%= request.getContextPath() %>/jsp/createClass.jsp">Create New Class</a></li>
  </ul>

  <!-- Display Teacher's Classes -->
  <div class="class-list">
    <h2>Your Classes</h2>
    <ul>
      <%
        // Fetch the list of classes taught by the current teacher
        ClassDAO classDAO = new ClassDAO();
        List<classs> classes = null;
        try {
          classes = classDAO.getClassByTeacherId(currentUser.getId());
        } catch (Exception e) {
          e.printStackTrace();
        }

        if (classes != null && !classes.isEmpty()) {
          for (classs classEntity : classes) {
      %>
      <li>
        <strong><a href="<%= request.getContextPath() %>/ClassDetailsServlet?classId=<%= classEntity.getId() %>">
          <%= classEntity.getName() %>
        </a></strong>
        <ul class="class-actions">
          <li><a href="<%= request.getContextPath() %>/EditClassServlet?classId=<%= classEntity.getId() %>">Edit Class</a></li>
          <li><a href="<%= request.getContextPath() %>/EnrollStudentServlet?classId=<%= classEntity.getId() %>">Enroll Students</a></li>
          <li><a href="<%= request.getContextPath() %>/AssignQuizServlet?classId=<%= classEntity.getId() %>">Assign Quiz</a></li>
        </ul>
      </li>
      <%
        }
      } else {
      %>
      <li>You have not created any classes yet.</li>
      <% } %>
    </ul>
  </div>

  <a href="<%= request.getContextPath() %>/index.jsp">Logout</a>
</div>
</body>
</html>
