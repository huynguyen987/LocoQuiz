<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Vector" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="initial-scale=1, width=device-width">
	
	<link rel="stylesheet"  href="../index.css" />
	<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap" />
	
</head>
<body>
	
	<div class="interface">
			<div class="hero-image">
				<div class="hero-image1">
						<div class="text-content-title">
							<b class="title">QuiLiz</b>
							<div class="subtitle">Lựa chọn tri thức</div>
						</div>
						<div class="button-group">
							<!-- tạo ô bấm để sendirect tên Đăng Nhâp -->
							<button onclick="location.href='dangnhap.jsp'" class="button" style="color: white;cursor: pointer; transition: transform 0.1s;">
								<div class="button1">Đăng Nhập</div>
							</button>
							<button onclick="location.href='dangki.jsp'" class="button" style="color: white;cursor: pointer; transition: transform 0.1s;">
								<div class="button1">Đăng Kí</div>
							</button>
						</div>
						<style>
							.button:active {
								transform: scale(0.95);
							}
						</style>
				</div>
			</div>
	</div>
	
</body>
</html>