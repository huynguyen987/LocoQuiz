<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Tag" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cập Nhật Tag</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/update-tag.css">
</head>
<body>
<%
    Tag tag = (Tag) request.getAttribute("tag");
%>
<h1>Cập Nhật Tag</h1>
<form action="ControllerTag?action=updateTag" method="post">
    <input type="hidden" name="id" value="<%= tag.getId() %>" />
    <input type="hidden" name="submit" value="true" />
    <label for="name">Tên Tag:</label><br/>
    <input type="text" id="name" name="name" value="<%= tag.getName() %>" required/><br/><br/>
    <label for="description">Mô Tả:</label><br/>
    <textarea id="description" name="description"><%= tag.getDescription() %></textarea><br/><br/>
    <input type="submit" value="Cập Nhật"/>
</form>
<br/>
<a href="ControllerTag?action=listTag">Quay lại danh sách Tag</a>
<form action="<%= request.getContextPath() %>/index.jsp" method="get">
    <button type="submit">Quay lại Trang Chủ</button>
</form>

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
