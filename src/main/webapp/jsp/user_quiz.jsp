<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dao.userQuizDAO"%>
<%@page import="entity.user_quiz"%>
<%@page import="java.util.List"%>
<%
  userQuizDAO dao = new userQuizDAO();
  List<user_quiz> userQuizList = null;
  try {
    userQuizList = dao.getAllUserQuiz();
  } catch (Exception e) {
    e.printStackTrace();
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Danh Sách User Quiz</title>
</head>
<body>
<h1>Danh Sách User Quiz</h1>
<a href="UserQuizServlet?action=insertUserQuiz">Thêm User Quiz</a>
<table border="1">
  <tr>
    <th>User ID</th>
    <th>Quiz ID</th>
    <th>Tag ID</th>
    <th>Hành Động</th>
  </tr>
  <%
    if (userQuizList != null) {
      for (user_quiz userQuiz : userQuizList) {
  %>
  <tr>
    <td><%= userQuiz.getUser_id() %></td>
    <td><%= userQuiz.getQuiz_id() %></td>
    <td><%= userQuiz.getTag_id() %></td>
    <td>
      <a href="UserQuizServlet?action=updateUserQuiz&old_user_id=<%= userQuiz.getUser_id() %>&old_quiz_id=<%= userQuiz.getQuiz_id() %>&old_tag_id=<%= userQuiz.getTag_id() %>">Sửa</a> |
      <a href="UserQuizServlet?action=deleteUserQuiz&user_id=<%= userQuiz.getUser_id() %>&quiz_id=<%= userQuiz.getQuiz_id() %>&tag_id=<%= userQuiz.getTag_id() %>" onclick="return confirm('Bạn có chắc chắn muốn xóa?');">Xóa</a>
    </td>
  </tr>
  <%
      }
    }
  %>
</table>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>
