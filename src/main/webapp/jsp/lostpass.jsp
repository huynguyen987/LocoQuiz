<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Quên Mật Khẩu - QuizLoco</title>
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
    <!-- (Giữ nguyên phần header của bạn) -->
</header>

<!-- Main Content -->
<main>
    <div class="password-reset-container">
        <div class="form-wrapper">
            <h2>Quên Mật Khẩu</h2>
            <p>Vui lòng nhập email đăng ký tài khoản của bạn. Chúng tôi sẽ gửi hướng dẫn đặt lại mật khẩu vào email của bạn.</p>

            <%
                String error = (String) session.getAttribute("error");
                if (error != null) {
            %>
            <script type="text/javascript">
                alert("<%= error %>");
            </script>
            <%
                    session.removeAttribute("error");  // Xóa thông báo lỗi sau khi hiển thị
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
    <!-- (Giữ nguyên phần footer của bạn) -->

</footer>

<!-- External JavaScript -->
<script src="${pageContext.request.contextPath}/js/script.js"></script>
<!-- Dark Mode Script (if implementing dark mode) -->
<script>
    // (Giữ nguyên phần script của bạn)
</script>
</body>
</html>
