<!-- File: /jsp/include/header.jsp -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang = "vi">
<head>
  <meta charset="UTF-8">
  <title><c:out value="${pageTitle}" default="Trang Web" /></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
  <!-- Bạn có thể thêm các liên kết CSS khác hoặc các thẻ meta ở đây -->
  <style>
    /* Ví dụ về CSS cho header */
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
    }
    .header {
      background-color: #4CAF50;
      color: white;
      padding: 15px 20px;
      text-align: center;
    }
    .nav {
      margin: 0;
      padding: 0;
      overflow: hidden;
      background-color: #333;
    }
    .nav a {
      float: left;
      display: block;
      color: #f2f2f2;
      text-align: center;
      padding: 14px 16px;
      text-decoration: none;
    }
    .nav a:hover {
      background-color: #ddd;
      color: black;
    }
  </style>
</head>
<body>
<div class="header">
  <h1>Hệ Thống Quản Lý Cuộc Thi</h1>
</div>
<div class="nav">
  <a href="${pageContext.request.contextPath}/CompetitionsServlet">Danh Sách Cuộc Thi</a>
  <c:if test="${not empty currentUser}">
    <a href="${pageContext.request.contextPath}/LogoutServlet">Đăng Xuất (${currentUser.username})</a>
  </c:if>
  <c:if test="${empty currentUser}">
    <a href="${pageContext.request.contextPath}/login.jsp">Đăng Nhập</a>
    <a href="${pageContext.request.contextPath}/register.jsp">Đăng Ký</a>
  </c:if>
</div>
