<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<head>
    <meta charset="UTF-8">
    <title>Tạo Câu Hỏi</title>
    <!-- Bao gồm Font Awesome cho biểu tượng -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        /* CSS tùy chỉnh */
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h2 {
            text-align: center;
        }
        .container {
            max-width: 600px;
            margin: auto;
        }
        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }
        input[type="file"],
        input[type="number"] {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
        }
        .button-group {
            margin-top: 20px;
            display: flex;
            justify-content: space-between;
        }
        .button-group button,
        .button-group a {
            padding: 10px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
            text-align: center;
        }
        .button-group button:hover,
        .button-group a:hover {
            background-color: #45a049;
        }
        .back-button {
            background-color: #f44336;
        }
        .back-button:hover {
            background-color: #d32f2f;
        }
    </style>
</head>
<body>
<!-- Bao gồm header -->
<%@ include file="components/header.jsp" %>

<div class="container">
    <h2>Tạo Câu Hỏi Từ Tài Liệu</h2>

    <!-- Nút Quay Lại -->
    <div class="button-group">
        <a href="javascript:history.back()" class="back-button"><i class="fas fa-arrow-left"></i> Quay lại</a>
    </div>

    <form action="generate-questions" method="post" enctype="multipart/form-data">
        <label for="document">Chọn tài liệu (Word hoặc PDF):</label>
        <input type="file" name="document" id="document" accept=".pdf,.docx" required>

        <label for="numQuestions">Số lượng câu hỏi muốn tạo:</label>
        <input type="number" name="numQuestions" id="numQuestions" min="1" required>

        <!-- Nút Tạo Câu Hỏi -->
        <div class="button-group">
            <button type="submit"><i class="fas fa-plus"></i> Tạo Câu Hỏi</button>
        </div>
    </form>
</div>

<!-- Bao gồm footer -->
<%@ include file="components/footer.jsp" %>
</body>
</html>
