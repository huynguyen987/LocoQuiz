<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="initial-scale=1, width=device-width">
	
	<link rel="stylesheet" href="./lostpass.css" />
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
	
	<div class="qun-mt-khu">
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
			<form action="<%= request.getContextPath() %>/forgetPass" method="post" class="form-log-in">
				<div class="input-field">
					<div class="label">Xin mời nhập email đã đăng kí tài khoản, sau đó kiểm tra tin nhắn trong mail</div>
					<input class="input" type="email" name="email" placeholder="Xin mời nhập email" required>
				</div>
				<div class="button-group">
					<button type="submit" class="button" style="color: white; cursor: pointer; transition: transform 0.1s;">Gửi</button>
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
