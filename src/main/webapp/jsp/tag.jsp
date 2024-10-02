<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.Tag" %>
<%@ page import="entity.Tag" %>
<!DOCTYPE html>
<html>
<head>
  <title>Danh Sách Tag Chính</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
<h1>Danh Sách Tag Chính</h1>
<a href="ControllerTag?action=insertTag">Thêm Tag Mới</a>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Tên</th>
    <th>Mô Tả</th>
    <th>Delete</th>
    <th>Update</th>
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
      <a href="ControllerTag?action=viewTag&id=<%= tag.getId() %>">Xem</a>
      <a href="ControllerTag?action=insertTag">Thêm Tag Mới</a>
      <a href="ControllerTag?action=updateTag&id=<%= tag.getId() %>">Sửa</a>
      <a href="ControllerTag?action=deleteTag&id=<%= tag.getId() %>" onclick="return confirm('Bạn có chắc chắn muốn xóa Tag này?');">Xóa</a>
    </td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="4">Không có tag nào.</td>
  </tr>
  <%
    }
  %>
</table>

<!-- Nút quay lại trang chủ -->
<br/>
<a href="<%= request.getContextPath() %>/index.jsp">Quay lại Trang Chủ</a>

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
