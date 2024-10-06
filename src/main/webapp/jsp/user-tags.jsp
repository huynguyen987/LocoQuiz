<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entity.Tag"%>
<%
  Tag tag = (Tag) request.getAttribute("tag");
  if (tag == null) {
%>
<p>Tag không tồn tại.</p>
<a href="ControllerTag?action=listTag">Quay lại danh sách Tag</a>
<%
} else {
%>
<!DOCTYPE html>
<html>
<head>
  <title>Cập Nhật Tag</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-tag.css">
</head>
<body>
<h1>Cập Nhật Tag</h1>
<form action="ControllerTag?action=updateTag" method="post">
  <input type="hidden" name="submit" value="true" />
  <input type="hidden" name="id" value="<%= tag.getId() %>" />
  <label for="name">Tên Tag:</label><br/>
  <input type="text" id="name" name="name" value="<%= tag.getName() %>" required/><br/>
  <label for="description">Mô Tả:</label><br/>
  <textarea id="description" name="description" required><%= tag.getDescription() %></textarea><br/><br/>
  <input type="submit" value="Cập Nhật Tag"/>
</form>
<br/>
<a href="ControllerTag?action=listTag">Quay lại danh sách Tag</a>

<!-- Hiển thị thông báo lỗi nếu có -->
<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>
<%
  }
%>
