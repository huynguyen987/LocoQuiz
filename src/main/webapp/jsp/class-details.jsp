<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Class Details - QuizLoco</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
  <script src="<%= request.getContextPath() %>/js/quiz-details.js"></script>
  <style>
    /* CSS cho modal */
    .modal {
      display: none;
      position: fixed;
      z-index: 1;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.4);
      padding-top: 60px;
    }
    .modal-content {
      background-color: #fefefe;
      margin: 5% auto;
      padding: 20px;
      border: 1px solid #888;
      width: 50%;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
      animation: slide-down 0.4s ease-out;
    }
    .close {
      color: #aaa;
      float: right;
      font-size: 28px;
      font-weight: bold;
    }
    .close:hover, .close:focus {
      color: black;
      cursor: pointer;
    }
    /* Style cho nút hành động */
    .button {
      background-color: #4CAF50;
      color: white;
      padding: 10px 20px;
      text-align: center;
      display: inline-block;
      font-size: 16px;
      margin: 4px 2px;
      cursor: pointer;
      border: none;
      border-radius: 5px;
    }
    .action-btn {
      display: inline-block;
      width: 100px;
      height: 100px;
      background-color: #007bff;
      color: white;
      border-radius: 8px;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 14px;
      text-align: center;
      margin: 10px;
    }
    .action-btn i {
      font-size: 24px;
      margin-bottom: 5px;
    }
    .create-quiz-btn {
      background-color: #28a745;
    }
  </style>
  <script>
    // Mở và đóng modal
    function openModal() {
      document.getElementById("quizModal").style.display = "block";
    }
    function closeModal() {
      document.getElementById("quizModal").style.display = "none";
    }
  </script>
</head>
<body>

<!-- Các nút hành động của giáo viên -->
<div class="action-buttons">
  <div class="action-btn">
    <i class="fas fa-edit"></i>
    Edit Class
  </div>
  <div class="action-btn">
    <i class="fas fa-tasks"></i>
    Assign Quiz
  </div>
  <div class="action-btn">
    <i class="fas fa-user-plus"></i>
    Enroll Students
  </div>
  <div class="action-btn" style="background-color: #dc3545;">
    <i class="fas fa-trash"></i>
    Delete Class
  </div>
  <!-- Nút Tạo Quiz -->
  <div class="action-btn create-quiz-btn" onclick="openModal()">
    <i class="fas fa-plus"></i>
    Tạo Quiz
  </div>
</div>

<!-- Modal tạo Quiz -->
<div id="quizModal" class="modal">
  <div class="modal-content">
    <span class="close" onclick="closeModal()">&times;</span>
    <h3>Tạo Quiz Mới</h3>
    <form action="<%= request.getContextPath() %>/CompetitionController" method="post">
      <input type="hidden" name="action" value="createCompetition">
      <label>Thời gian làm bài (phút):</label>
      <input type="number" name="duration" required>

      <label>Số câu hỏi:</label>
      <input type="number" name="questionCount" required>

      <label>Xáo trộn câu hỏi:</label>
      <input type="checkbox" name="shuffle">

      <button type="submit" class="button">Tạo Cuộc Thi</button>
    </form>
  </div>
</div>

<script src="<%= request.getContextPath() %>/js/quiz-details.js"></script>
</body>
</html>
