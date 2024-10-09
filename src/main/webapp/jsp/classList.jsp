<%@ page import="entity.Users" %>
<%@ page import="dao.ClassDAO" %>
<%@ page import="entity.classs" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Kiểm tra quyền truy cập
  session = request.getSession();
  Users currentUser = (Users) session.getAttribute("user");
  if (currentUser == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }

  List<classs> classes = null;
  String search = request.getParameter("search");
  try {
    ClassDAO classDAO = new ClassDAO();
    if (search != null && !search.trim().isEmpty()) {
      classes = classDAO.searchClasses(search);
    } else {
      classes = classDAO.getAllClass();
    }
  } catch (Exception e) {
    e.printStackTrace();
    response.sendRedirect(request.getContextPath() + "/error.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Danh Sách Lớp Học</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
  <style>
    .container { width: 80%; margin: 50px auto; }
    h1 { text-align: center; }
    .search-form { text-align: center; margin-bottom: 20px; }
    .class-list { list-style-type: none; padding: 0; }
    .class-list li { background-color: #f9f9f9; margin: 5px 0; padding: 10px; border-radius: 4px; }
    .class-list a { text-decoration: none; color: #333; }
    .class-list a:hover { text-decoration: underline; }
  </style>
</head>
<body>
<div class="container">
  <h1>Danh Sách Lớp Học</h1>
  <div class="search-form">
    <form action="<%=request.getContextPath()%>/jsp/classList.jsp" method="get">
      <input type="text" name="search" placeholder="Tìm kiếm lớp học" value="<%= (search != null) ? search : "" %>">
      <button type="submit">Tìm Kiếm</button>
    </form>
  </div>

  <ul class="class-list">
    <% for (classs classEntity : classes) { %>
    <li>
      <a href="<%=request.getContextPath()%>/ClassDetailsServlet?classId=<%= classEntity.getId() %>"><%= classEntity.getName() %></a>
    </li>
    <% } %>
  </ul>
</div>
</body>
</html>
