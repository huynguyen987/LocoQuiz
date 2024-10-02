<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Practice - 30 Questions</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/quiz.css">
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
<div id="result-modal" class="modal">
    <div class="modal-content">
        <span class="close-button" onclick="quiz.closeModal()">&times;</span>
        <h2>Your Results</h2>
        <p id="result-text">You scored X out of Y.</p>
        <button onclick="quiz.restartQuiz()">Restart Quiz</button>
    </div>
</div>

<!-- External JavaScript file -->
<script src="${pageContext.request.contextPath}/js/quiz.js"></script>
</body>
</html>
