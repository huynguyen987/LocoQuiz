<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="initial-scale=1, width=device-width">

	<link rel="stylesheet" href="./dangki.css" />
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

	<div class="ng-k">

			<form action="<%= request.getContextPath() %>/register" method="post" class="form-log-in">
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
				<div class="input-field">
					<div class="label">Tài Khoản</div>
					<input class="input" type="text" name="username" placeholder="Xin mời nhập tài khoản" required>
				</div>
				<div class="input-field1">
					<div class="label">Mật khẩu</div>
					<input class="input" type="password" name="password" placeholder="Xin mời nhập mật khẩu" required>
				</div>
				<div class="input-field1">
					<div class="label">Nhập lại mật khẩu</div>
					<input class="input" type="password" name="confirm_password" placeholder="Xin mời nhập lại mật khẩu" required>
				</div>
				<script>
					function validateForm() {
						var password = document.forms["registerForm"]["password"].value;
						var confirmPassword = document.forms["registerForm"]["confirm_password"].value;
						if (password !== confirmPassword) {
							document.getElementById("passwordError").style.display = "block";
							return false;
						}
						return true;
					}
				</script>
				<div class="input-field1">
					<div class="label">Nhập email</div>
					<input class="input" type="text" name="email" placeholder="Xin mời nhập email" required>
				</div>
				<div class="input-field1">
					<div class="label">Role</div>
					<div class="input" style="border: none;">
						<select name="role" id="role" required>
							<option value="student">Sinh Viên</option>
							<option value="teacher">Giảng Viên</option>
						</select>
					</div>
				</div>
				<div class="button-group">
					<button type="submit" class="button" style="color: white; cursor: pointer; transition: transform 0.1s;">Đăng Kí</button>
				</div>
				<style>
					.button:active {
						transform: scale(0.95);
					}
				</style>
			</form>
		</div>

</body>

</html>
