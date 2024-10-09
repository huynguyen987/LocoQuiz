<%@ page import="entity.Users" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.ClassDAO" %>
<%@ page import="entity.classs" %>
<%@ page import="dao.UsersDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Kiểm tra quyền hạn và lấy thông tin
  session = request.getSession();
  Users currentUser = (Users) session.getAttribute("user");
  if (currentUser == null || (currentUser.getRole_id() != 2 && currentUser.getRole_id() != 3)) {
    response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
    return;
  }

  int classId = Integer.parseInt(request.getParameter("classId"));
  classs classEntity = null;
  List<Users> students = null;
  try {
    ClassDAO classDAO = new ClassDAO();
    classEntity = classDAO.getClassById(classId);
    if (classEntity == null || classEntity.getTeacher_id() != currentUser.getId()) {
      response.sendRedirect(request.getContextPath() + "/error.jsp");
      return;
    }

    // Lấy danh sách học sinh chưa được ghi danh
    UsersDAO usersDAO = new UsersDAO();
    students = usersDAO.getAllStudentsNotInClass(classId);
  } catch (Exception e) {
    e.printStackTrace();
    response.sendRedirect(request.getContextPath() + "/error.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Ghi Danh Học Sinh Vào Lớp</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
  <style>
    .container { width: 50%; margin: 50px auto; }
    h1 { text-align: center; }
    form label { display: block; margin-top: 15px; color: #555; }
    form select { width: 100%; padding: 8px; margin-top: 8px; border: 1px solid #ccc; border-radius: 4px; }
    form button { margin-top: 20px; padding: 10px 20px; background-color: #4285f4; color: #fff; border: none; border-radius: 4px; cursor: pointer; }
    form button:hover { background-color: #3367d6; }
  </style>
</head>
<body>
<div class="container">
  <h1>Ghi Danh Học Sinh Vào Lớp: <%= classEntity.getName() %></h1>
  <form action="<%=request.getContextPath()%>/EnrollStudentServlet" method="post">
    <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
    <label for="studentId">Chọn Học Sinh:</label>
    <select id="studentId" name="studentId">
      <% for (Users student : students) { %>
      <option value="<%= student.getId() %>"><%= student.getUsername() %> (<%= student.getEmail() %>)</option>
      <% } %>
    </select>

    <button type="submit">Ghi Danh</button>
  </form>
  <% if (request.getAttribute("errorMessage") != null) { %>
  <p class="error-message"><%= request.getAttribute("errorMessage") %></p>
  <% } %>
</div>
</body>
</html>
