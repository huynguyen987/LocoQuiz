<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Xác nhận người dùng - QuizLoco</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- External CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lostpass.css" />

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap" rel="stylesheet">

    <!-- Inline Styles for Error Message -->
    <style>
        .error-message {
            color: #721c24;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            padding: 10px 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-size: 14px;
        }
    </style>
</head>
<body>
<!-- Header Section -->
<header>
    <div class="container">
        <h1><a href="${pageContext.request.contextPath}/index.jsp" class="logo">QuizLoco</a></h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/index.jsp">Trang Chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/login.jsp">Đăng Nhập</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/register.jsp">Đăng Ký</a></li>
                <li><a href="${pageContext.request.contextPath}/index.jsp#contact">Liên Hệ</a></li>
            </ul>
        </nav>
    </div>
</header>

<!-- Main Content -->
<main>
    <div class="password-reset-container">
        <div class="form-wrapper">
            <h2>Xác nhận email</h2>
            <p>Vui lòng nhập captcha từ mail cá nhân</p>

            <%
                String error = (String) session.getAttribute("error");
                if (error != null) {
            %>
            <div class="error-message">
                <%= error %>
            </div>
            <%
                    session.removeAttribute("error");  // Clear error after displaying it
                }
            %>

            <form action="${pageContext.request.contextPath}/verify-register" method="post" class="reset-form">
                <div class="input-group">
                    <label for="email">Captcha</label>
                    <input type="text" id="email" name="capcha" placeholder="Nhập captcha của bạn" required>
                </div>
                <button type="submit" class="btn-submit">Gửi</button>
            </form>
        </div>
    </div>
</main>

<!-- Footer Section -->
<footer>
    <div class="container">
        <p>&copy; 2024 QuizLoco. All rights reserved.</p>
    </div>
</footer>

<!-- External JavaScript -->
<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
