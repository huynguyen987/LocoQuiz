<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="entity.Tag"%>
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
    <label for="tagId">Chọn Tag:</label><br/>
    <select id="tagId" name="tagId" required>
        <option value="">-- Chọn một tag --</option>
        <%
            List<Tag> fixedTags = (List<Tag>) request.getAttribute("fixedTags");
            if (fixedTags != null) {
                for (Tag tag : fixedTags) {
        %>
        <option value="<%= tag.getId() %>"><%= tag.getName() %></option>
        <%
                }
            }
        %>
    </select><br/><br/>
    <input type="submit" value="Thêm Tag"/>
</form>
<br/>
<a href="ControllerTag?action=listTag">Quay lại danh sách Tag</a>

<!-- Hiển thị thông báo lỗi nếu có -->
<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>
