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
	<!-- Chỉ hiển thị khi có lỗi trong sessionScope -->
	<c:if test="${not empty sessionScope.error}">
		<div class="error-message" id="error-message">${sessionScope.error}</div>
		<script>
			// Tự động tắt sau 3 giây
			setTimeout(function() {
				document.getElementById('error-message').style.display = 'none';
			}, 3000); // 3000 milliseconds = 3 seconds

			// Tắt khi click vào màn hình
			document.addEventListener('click', function() {
				document.getElementById('error-message').style.display = 'none';
			});
		</script>
	</c:if>

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

<style>
	/* Thiết kế cho thông báo lỗi với gradient vàng */
	.error-message {
		position: fixed;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		padding: 20px;
		background: linear-gradient(45deg, #f5d547, #f5b947);
		color: black;
		font-size: 18px;
		border-radius: 10px;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
		text-align: center;
		z-index: 1000;
	}
</style>
</body>
</html>
