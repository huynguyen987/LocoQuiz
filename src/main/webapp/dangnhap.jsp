<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="initial-scale=1, width=device-width">

	<link rel="stylesheet" href="<%= request.getContextPath() %>/dangnhap.css" />
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400&display=swap" />
	<style>
		.error-message {
			color: red;
			font-size: 14px;
			position: absolute;
			top: 10px;
			right: 10px;
			background-color: #f8d7da;
			border-color: #f5c6cb;
			padding: 10px;
			border-radius: 5px;
		}
	</style>
</head>
<body>
<div class="ng-nhp">
	<!-- Chỉ hiển thị khi có lỗi trong sessionScope -->
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

	<!-- Form đăng nhập -->
	<form action="<%= request.getContextPath() %>/login" method="post" class="form-log-in">
		<div class="input-field">
			<div class="label">Tài Khoản</div>
			<input class="input" type="text" name="username" placeholder="Xin mời nhập tài khoản" required>
		</div>
		<div class="input-field1">
			<div class="label">Mật khẩu</div>
			<input class="input" type="password" name="password" placeholder="Xin mời nhập mật khẩu" required>
		</div>
		<div class="checkbox-field">
			<div class="checkbox-and-label">
				<input class="checkbox" type="checkbox">
				<div class="description">Lưu mật khẩu</div>
			</div>
		</div>

		<div class="button-group">
			<button type="submit" class="button" style="color: white; cursor: pointer; transition: transform 0.1s;">
				Đăng nhập
			</button>
		</div>
		<style>
			.button:active {
				transform: scale(0.95);
			}
		</style>
		<a href="<%= request.getContextPath() %>/lostpass.jsp" class="forgot-password">Quên mật khẩu?</a>
	</form>
</div>
</body>
</html>
