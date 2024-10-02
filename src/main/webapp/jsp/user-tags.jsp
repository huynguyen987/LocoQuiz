<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.Tag" %>
<%@ page import="entity.Tag" %>
<!DOCTYPE html>
<html>
<head>
  <title>Danh Sách Tag Của Bạn</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<h1>Danh Sách Tag Của Bạn</h1>
<a href="ControllerTag?action=insertTag">Thêm Tag Mới</a>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Tên</th>
    <th>Mô Tả</th>
    <th>Hành Động</th>
  </tr>
  <%
    List<Tag> tags = (List<Tag>) request.getAttribute("tags");
    if (tags != null && !tags.isEmpty()) {
      for (Tag tag : tags) {
  %>
  <tr>
    <td><%= tag.getId() %></td>
    <td><%= tag.getName() %></td>
    <td><%= tag.getDescription() %></td>
    <td>
      <a href="ControllerTag?action=deleteUserTag&tagId=<%= tag.getId() %>" onclick="return confirm('Bạn có chắc chắn muốn xóa Tag này khỏi danh sách của bạn?');">Xóa</a>
    </td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="4">Bạn chưa có tag nào.</td>
  </tr>
  <%
    }
  %>
</table>

<!-- Hiển thị thông báo lỗi nếu có -->
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
