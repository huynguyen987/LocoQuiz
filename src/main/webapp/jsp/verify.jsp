<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>Xác Nhận Mã - QuizLoco</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<!-- Liên kết CSS ngoại vi -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/verify.css" />

	<!-- Google Fonts -->
	<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap" rel="stylesheet">

	<!-- Kiểu dáng cho thông báo lỗi -->
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
<!-- Phần Header -->
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

<!-- Nội dung chính -->
<main>
	<div class="verify-code-container">
		<div class="form-wrapper">
			<h2>Xác Nhận Mã</h2>
			<p>Vui lòng nhập mã xác nhận đã được gửi đến email của bạn.</p>

			<%
				String error = (String) session.getAttribute("error");
				if (error != null) {
			%>
			<div class="error-message">
				<%= error %>
			</div>
			<%
					session.removeAttribute("error");  // Xóa thông báo lỗi sau khi hiển thị
				}
			%>

			<form action="${pageContext.request.contextPath}/verify" method="post" class="verify-form">
				<div class="input-group">
					<label for="capcha">Mã Xác Nhận</label>
					<input type="text" id="capcha" name="capcha" placeholder="Nhập mã xác nhận" required>
				</div>
				<button type="submit" class="btn-submit">Xác Nhận</button>
			</form>
		</div>
	</div>
</main>

<!-- Phần Footer -->
<footer>
	<div class="container">
		<p>&copy; 2024 QuizLoco. All rights reserved.</p>
	</div>
</footer>

<!-- JavaScript ngoại vi -->
<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
