<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.json.JSONArray, org.json.JSONObject" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Danh Sách Câu Hỏi</title>
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
      max-width: 1000px;
      margin: auto;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;
    }
    th, td {
      padding: 12px;
      border: 1px solid #ddd;
      text-align: left;
    }
    th {
      background-color: #f2f2f2;
    }
    .button-group {
      margin-top: 20px;
      display: flex;
      justify-content: space-between;
    }
    .button-group button,
    .button-group a {
      padding: 10px 15px;
      background-color: #008CBA;
      color: white;
      border: none;
      cursor: pointer;
      font-size: 16px;
      text-decoration: none;
      text-align: center;
    }
    .button-group button:hover,
    .button-group a:hover {
      background-color: #007B9E;
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
<%@ include file="/jsp/components/header.jsp" %>

<div class="container">
  <h2>Danh Sách Câu Hỏi</h2>

  <!-- Nút Quay Lại -->
  <div class="button-group">
    <a href="javascript:history.back()" class="back-button"><i class="fas fa-arrow-left"></i> Quay lại</a>
  </div>

  <table>
    <tr>
      <th>Câu Hỏi</th>
      <th>Lựa Chọn 1</th>
      <th>Lựa Chọn 2</th>
      <th>Lựa Chọn 3</th>
      <th>Lựa Chọn 4</th>
      <th>Đáp Án Đúng</th>
    </tr>
    <%
      JSONArray questions = (JSONArray) request.getAttribute("questions");
      for (int i = 0; i < questions.length(); i++) {
        JSONObject questionObj = questions.getJSONObject(i);
        String questionText = questionObj.getString("question");
        JSONArray options = questionObj.getJSONArray("options");
        String correctAnswer = questionObj.getString("correct");

        out.println("<tr>");
        out.println("<td>" + questionText + "</td>");
        // Hiển thị từng lựa chọn trong các cột riêng biệt
        for (int j = 0; j < 4; j++) {
          if (j < options.length()) {
            out.println("<td>" + options.getString(j) + "</td>");
          } else {
            out.println("<td></td>");
          }
        }
        out.println("<td>" + correctAnswer + "</td>");
        out.println("</tr>");
      }
    %>
  </table>

  <!-- Nút Tải Xuống và Quay Lại -->
  <div class="button-group">
    <form method="get" action="download-excel" style="margin: 0;">
      <button type="submit"><i class="fas fa-file-excel"></i> Tải xuống dưới dạng Excel</button>
    </form>
    <!-- Nút Quay Lại (nếu cần thêm ở đây) -->
    <!-- <a href="javascript:history.back()" class="back-button"><i class="fas fa-arrow-left"></i> Quay lại</a> -->
  </div>
</div>

<!-- Bao gồm footer -->
<%@ include file="/jsp/components/footer.jsp" %>
</body>
</html>
