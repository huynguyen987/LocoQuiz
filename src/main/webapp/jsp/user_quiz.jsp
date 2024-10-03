<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.user_quiz" %>
<html>
<head>
  <title>Danh Sách User Quiz</title>
</head>
<body>
<h2>Danh Sách User Quiz</h2>
<a href="<%= request.getContextPath() %>/UserQuizServlet?action=insertUserQuiz">Thêm User Quiz</a><br><br>

<table border="1">
  <tr>
    <th>User ID</th>
    <th>Quiz ID</th>
    <th>Tag ID</th>
    <th>Hành Động</th>
  </tr>
  <%
    // Lấy danh sách user_quiz từ request
    List<user_quiz> userQuizzes = (List<user_quiz>) request.getAttribute("userQuizzes");
    if (userQuizzes != null && !userQuizzes.isEmpty()) {
      for (user_quiz uq : userQuizzes) {
  %>
  <tr>
    <td><%= uq.getUser_id() %></td>
    <td><%= uq.getQuiz_id() %></td>
    <td><%= uq.getTag_id() %></td>
    <td>
      <a href="<%= request.getContextPath() %>/UserQuizServlet?action=updateUserQuiz&old_user_id=<%= uq.getUser_id() %>&old_quiz_id=<%= uq.getQuiz_id() %>&old_tag_id=<%= uq.getTag_id() %>">Cập Nhật</a> |
      <a href="<%= request.getContextPath() %>/UserQuizServlet?action=deleteUserQuiz&user_id=<%= uq.getUser_id() %>&quiz_id=<%= uq.getQuiz_id() %>&tag_id=<%= uq.getTag_id() %>" onclick="return confirm('Bạn có chắc chắn muốn xóa?');">Xóa</a>
    </td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="4">Không có dữ liệu nào.</td>
  </tr>
  <%
    }
  %>
</table>

<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
<p style="color:red;"><%= error %></p>
<%
  }
%>
</body>
</html>
