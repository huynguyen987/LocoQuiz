<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="initial-scale=1, width=device-width">
	
	<link rel="stylesheet" href="<%= request.getContextPath() %>/dangnhap.css" />
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400&display=swap" />
	
</head>
<body>
	
	<div class="ng-nhp">
		<div class="form-log-in">
			<form action="<%= request.getContextPath() %>/login" method="post">
				<div class="input-field">
					<div class="label">Tài Khoản</div>
					<input class="input" type="text" name="username" placeholder="Xin mời nhập tài khoản">
				</div>
				<div class="input-field1">
					<div class="label">Mật khẩu</div>
					<input class="input" type="password" name="password" placeholder="Xin mời nhập mật khẩu">
				</div>
				<div class="checkbox-field">
					<div class="checkbox-and-label">
						<input class="checkbox" type="checkbox">
						<div class="description">Lưu mật khẩu</div>
					</div>
					<div class="description-row">
						<div class="check-icon">
						</div>
						<div class="description">
						</div>
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
	</div>
	
</body>
</html>
