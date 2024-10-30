<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Không Có Quyền Truy Cập</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <style>
    .unauthorized-container {
      width: 90%;
      max-width: 600px;
      margin: 5em auto;
      background-color: #fff;
      padding: 2em;
      border-radius: 10px;
      text-align: center;
    }
    .unauthorized-container h2 {
      color: #e74c3c;
      margin-bottom: 1em;
    }
    .unauthorized-container a {
      display: inline-block;
      margin-top: 1.5em;
      padding: 0.8em 1.5em;
      background-color: #3498db;
      color: #fff;
      text-decoration: none;
      border-radius: 5px;
    }
    .unauthorized-container a:hover {
      background-color: #2980b9;
    }
  </style>
</head>
<body>
<div class="unauthorized-container">
  <h2>Không Có Quyền Truy Cập</h2>
  <p>Bạn không có quyền truy cập vào trang này.</p>
  <a href="${pageContext.request.contextPath}/jsp/login.jsp">Quay Lại Trang Đăng Nhập</a>
</div>
</body>
</html>
