<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tạo Câu Hỏi</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        /* Thêm CSS tùy chỉnh nếu cần */
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        form {
            max-width: 600px;
            margin: auto;
        }
        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }
        input[type="file"], input[type="number"] {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
        }
        button {
            margin-top: 20px;
            padding: 10px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<h2>Tạo Câu Hỏi Từ Tài Liệu</h2>
<form action="generate-questions" method="post" enctype="multipart/form-data">
    <label for="document">Chọn tài liệu (Word hoặc PDF):</label>
    <input type="file" name="document" id="document" accept=".pdf,.docx" required><br><br>

    <label for="numQuestions">Số lượng câu hỏi muốn tạo:</label>
    <input type="number" name="numQuestions" id="numQuestions" min="1" required><br><br>

    <button type="submit">Tạo Câu Hỏi</button>
</form>
</body>
</html>
