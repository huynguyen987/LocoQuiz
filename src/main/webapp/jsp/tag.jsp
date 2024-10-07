<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="entity.Tag"%>
<!DOCTYPE html>
<html>
<head>
  <title>Danh Sách Tag Của Bạn</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/tag.css">
</head>
<body>
<h1>Danh Sách Tag Của Bạn</h1>

<!-- Form tìm kiếm -->
<form action="ControllerTag" method="get">
  <input type="hidden" name="action" value="searchTag" />
  <input type="text" name="searchName" value="<%= request.getAttribute("searchName") != null ? request.getAttribute("searchName") : "" %>" placeholder="Nhập tên tag cần tìm" />
  <input type="submit" value="Tìm kiếm" />
</form>

<!-- Liên kết thêm tag mới -->
<a href="ControllerTag?action=insertTag">Thêm Tag Mới</a>
<br/><br/>

<!-- Bảng hiển thị danh sách tag -->
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
      <a href="ControllerTag?action=deleteTag&tagId=<%= tag.getId() %>" onclick="return confirm('Bạn có chắc chắn muốn xóa tag này khỏi danh sách của bạn?');">Xóa</a>
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
<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>
