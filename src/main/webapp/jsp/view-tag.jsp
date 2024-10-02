<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Tag" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chi Tiết Tag</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%
    Tag tag = (Tag) request.getAttribute("tag");
%>
<h1>Chi Tiết Tag</h1>
<p><strong>ID:</strong> <%= tag.getId() %></p>
<p><strong>Tên:</strong> <%= tag.getName() %></p>
<p><strong>Mô Tả:</strong> <%= tag.getDescription() %></p>
<br/>
<a href="ControllerTag?action=updateTag&id=<%= tag.getId() %>">Sửa</a>
<a href="ControllerTag?action=deleteTag&id=<%= tag.getId() %>" onclick="return confirm('Bạn có chắc chắn muốn xóa Tag này?');">Xóa</a>
<br/><br/>
<a href="ControllerTag?action=listTag">Quay lại danh sách Tag</a>

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
