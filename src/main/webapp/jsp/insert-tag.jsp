<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Tag Mới</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
<h1>Thêm Tag Mới</h1>
<form action="ControllerTag?action=insertTag" method="post">
    <input type="hidden" name="submit" value="true" />
    <label for="name">Tên Tag:</label><br/>
    <input type="text" id="name" name="name" required/><br/><br/>
    <label for="description">Mô Tả:</label><br/>
    <textarea id="description" name="description"></textarea><br/><br/>
    <input type="submit" value="Thêm Tag"/>
</form>
<br/>
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
