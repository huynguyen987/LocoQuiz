<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.classs" %>
<%
    classs classEntity = (classs) request.getAttribute("classEntity");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Class</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
<div class="container">
    <h1>Edit Class: <%= classEntity.getName() %></h1>
    <form action="<%= request.getContextPath() %>/EditClassServlet" method="post">
        <input type="hidden" name="classId" value="<%= classEntity.getId() %>">

        <label for="name">Class Name:</label>
        <input type="text" id="name" name="name" value="<%= classEntity.getName() %>" required>

        <label for="description">Description:</label>
        <textarea id="description" name="description"><%= classEntity.getDescription() %></textarea>

        <button type="submit">Save Changes</button>
    </form>
    <a href="<%= request.getContextPath() %>/ClassDetailsServlet?classId=<%= classEntity.getId() %>">Back to Class Details</a>
</div>
</body>
</html>
