<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Thay Đổi Mật Khẩu - QuizLoco</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- External CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lostpass.css" />

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap" rel="stylesheet">

    <!-- Font Awesome for Icons (Ensure you have this for the theme toggle) -->
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
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
            </ul>
        </nav>
        <!-- Theme Toggle (Optional) -->
        <div class="theme-toggle">
            <input type="checkbox" id="dark-mode-toggle" aria-label="Toggle dark mode">
            <label for="dark-mode-toggle">
                <i class="fas fa-moon"></i>
                <i class="fas fa-sun"></i>
            </label>
        </div>
    </div>
</header>

<!-- Main Content -->
<main>
    <div class="password-reset-container">
        <div class="form-wrapper">
            <h2>Change Password</h2>
            <p>Please enter your registered email of your account.</p>

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

            <form action="${pageContext.request.contextPath}/forgetPass" method="post" class="reset-form">
                <div class="input-group">
                    <label for="email">Email Đăng Ký</label>
                    <input type="email" id="email" name="email" placeholder="Nhập email của bạn" required>
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
<!-- Dark Mode Script (if implementing dark mode) -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const toggleSwitch = document.getElementById('dark-mode-toggle');
        const currentTheme = localStorage.getItem('theme');

        if (currentTheme === 'dark') {
            document.body.classList.add('dark-mode');
            toggleSwitch.checked = true;
        }

        toggleSwitch.addEventListener('change', () => {
            if (toggleSwitch.checked) {
                document.body.classList.add('dark-mode');
                localStorage.setItem('theme', 'dark');
            } else {
                document.body.classList.remove('dark-mode');
                localStorage.setItem('theme', 'light');
            }
        });
    });
</script>
</body>
</html>
