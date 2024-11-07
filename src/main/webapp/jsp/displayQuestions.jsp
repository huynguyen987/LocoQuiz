<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.json.JSONArray, org.json.JSONObject" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Danh sách Câu Hỏi</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
  <style>
    /* Thêm CSS tùy chỉnh nếu cần */
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
    }
    table {
      width: 100%;
      border-collapse: collapse;
    }
    th, td {
      padding: 12px;
      border: 1px solid #ddd;
      text-align: left;
    }
    th {
      background-color: #f2f2f2;
    }
    form {
      margin-top: 20px;
    }
    button {
      padding: 10px 15px;
      background-color: #008CBA;
      color: white;
      border: none;
      cursor: pointer;
      font-size: 16px;
    }
    button:hover {
      background-color: #007B9E;
    }
  </style>
</head>
<body>
<h2>Danh sách Câu Hỏi</h2>
<table>
  <tr>
    <th>Question</th>
    <th>Option 1</th>
    <th>Option 2</th>
    <th>Option 3</th>
    <th>Option 4</th>
    <th>Correct Answer</th>
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
<br>
<form method="get" action="download-excel">
  <button type="submit"><i class="fas fa-file-excel"></i> Tải xuống dưới dạng Excel</button>
</form>
</body>
</html>
