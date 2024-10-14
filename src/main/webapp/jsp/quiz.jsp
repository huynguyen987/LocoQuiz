<!-- File: src/main/webapp/jsp/quiz.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String quizId = (String) request.getAttribute("quizId");
    if (quizId == null || quizId.isEmpty()) {
        quizId = "1"; // Giá trị mặc định nếu không có
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Practice - 30 Questions</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/quiz.css">
    <style>
        /* Thêm một số kiểu cơ bản cho modal */
        .modal {
            display: none; /* Ẩn mặc định */
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.5);
        }
        .modal-content {
            background-color: #fefefe;
            margin: 10% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 300px;
            text-align: center;
            border-radius: 5px;
        }
        .modal-content select, .modal-content button, .modal-content input {
            width: 80%;
            padding: 10px;
            margin: 10px 0;
            font-size: 16px;
        }
        .modal-content input {
            display: none; /* Ẩn input tùy chỉnh mặc định */
        }
    </style>
</head>
<body>
<div class="quiz-container">
    <!-- Quiz Header with Timer and Progress Bar -->
    <div class="quiz-header">
        <div class="countdown-timer" id="countdown-timer">Time left: 15:00</div>
        <div class="progress-container">
            <div class="progress-bar">
                <div class="progress" id="progress"></div>
            </div>
            <div class="progress-details">
                <span id="answered-count">0</span> / <span id="total-count">30</span> Answered
            </div>
        </div>
    </div>

    <!-- Quiz Content -->
    <div class="quiz-content">
        <div class="question-number" id="question-number">Question 1 of 30</div>
        <h2 id="question-text">Loading question...</h2>
        <form id="quiz-form">
            <div class="answer-options" id="answer-options">
                <!-- Answer options will be dynamically inserted here -->
            </div>
        </form>
    </div>

    <!-- Navigation Buttons -->
    <div class="navigation-buttons">
        <button id="prev-btn" onclick="quiz.prevQuestion()" disabled>Previous</button>
        <button id="next-btn" onclick="quiz.nextQuestion()">Next</button>
        <button id="submit-btn" onclick="quiz.submitQuiz()" style="display: none;">Submit</button>
    </div>

    <!-- Jump to Section -->
    <div class="jump-to-section">
        <label for="section-select">Jump to Section:</label>
        <select id="section-select" onchange="quiz.jumpToSection(this.value)">
            <option value="">Select Section</option>
            <option value="1">Section 1</option>
            <option value="2">Section 2</option>
            <option value="3">Section 3</option>
        </select>
    </div>

    <!-- Question Selector -->
    <div class="question-selector" id="question-selector">
        <!-- Dynamic question buttons will be inserted here -->
    </div>
</div>

<!-- Result Modal -->
<div id="result-modal" class="modal" style="display: none;">
    <div class="modal-content">
        <span class="close-button" onclick="quiz.closeModal()">&times;</span>
        <h2>Your Results</h2>
        <p id="result-text">You scored X out of Y.</p>
        <button onclick="quiz.restartQuiz()">Restart Quiz</button>
        <button onclick="quiz.exitQuiz()">Exit Quiz</button>
    </div>
</div>

<!-- Time Selection Modal -->
<div id="time-modal" class="modal">
    <div class="modal-content">
        <h2>Select Time for Quiz</h2>
        <select id="time-select" onchange="toggleCustomTimeInput(this.value)">
            <option value="900">15 Minutes</option>
            <option value="1200">20 Minutes</option>
            <option value="1500">25 Minutes</option>
            <option value="custom">Custom</option>
        </select>
        <br>
        <input type="number" id="custom-time" min="300" max="3600" step="60" placeholder="Enter time in seconds">
        <br>
        <button onclick="startQuiz()">Start Quiz</button>
    </div>
</div>

<!-- Thêm biến JavaScript để truyền quizId và contextPath -->
<script>
    // Truyền quizId và contextPath từ server sang client
    var quizId = '<%= quizId %>';
    var contextPath = '<%= request.getContextPath() %>';
</script>

<!-- External JavaScript file -->
<script src="${pageContext.request.contextPath}/js/quiz.js"></script>
</body>
</html>
