<%@ page import="java.util.List" %>
<%@ page import="Module.AnswersReader" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    String quizId = (String) request.getAttribute("quizId");
    Object maxQuestionCountObj = request.getAttribute("maxQuestionCount");
    int maxQuestionCount = 0;
    if (maxQuestionCountObj != null) {
        try {
            maxQuestionCount = Integer.parseInt(maxQuestionCountObj.toString());
        } catch (NumberFormatException e) {
            maxQuestionCount = 0; // Handle invalid number
        }
    }
    if (quizId == null || quizId.isEmpty()) {
        quizId = "1"; // Default value if not present
    }
    session.setAttribute("quizId", quizId); // Store quizId in session
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quiz Practice</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/quiz.css">
    <!-- Include Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <style>
        /* Add your custom styles here */
    </style>
</head>
<body>
<div class="quiz-container" style="display: none;">
    <!-- Quiz Header with Timer and Progress Bar -->
    <div class="quiz-header">
        <div class="countdown-timer" id="countdown-timer">Time left: <span id="time-left"></span></div>
        <div class="progress-container">
            <div class="progress-bar">
                <div class="progress" id="progress"></div>
            </div>
            <div class="progress-details">
                <span id="answered-count">0</span> / <span id="total-count"></span> Answered
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

<!-- Redesigned Quiz Settings Modal -->
<div id="quiz-settings-modal" class="modal">
    <div class="modal-content">
        <h2>Customize Your Quiz</h2>

        <!-- Time Selection -->
        <div class="config-section">
            <label for="time-select">
                <i class="fas fa-clock"></i> Time Limit:
            </label>
            <select id="time-select" onchange="toggleCustomTimeInput(this.value)">
                <option value="900">15 Minutes</option>
                <option value="1200">20 Minutes</option>
                <option value="1500">25 Minutes</option>
                <option value="custom">Custom</option>
            </select>
            <input type="number" id="custom-time" min="300" max="3600" step="60" placeholder="Seconds" style="display: none;">
        </div>

        <!-- Number of Questions Selection -->
        <div class="config-section">
            <label for="question-count-select">
                <i class="fas fa-list-ol"></i> Number of Questions:
            </label>
            <select id="question-count-select" onchange="toggleCustomQuestionCountInput(this.value)">
                <option value="10">10 Questions</option>
                <option value="20">20 Questions</option>
                <option value="30">30 Questions</option>
                <option value="custom">Custom</option>
            </select>
            <input type="number" id="custom-question-count" min="1" max="<%= maxQuestionCount %>" step="1" placeholder="Enter number" style="display: none;">
        </div>

        <!-- Shuffle Questions Option -->
        <div class="config-section">
            <label for="shuffle-select">
                <i class="fas fa-random"></i> Shuffle Questions:
            </label>
            <div class="toggle-switch">
                <input type="checkbox" id="shuffle-select" checked>
                <span class="slider"></span>
            </div>
        </div>

        <!-- Start Quiz Button -->
        <button class="start-quiz-btn" onclick="startQuiz()">
            <i class="fas fa-play"></i> Start Quiz
        </button>
    </div>
</div>

<!-- JavaScript variables -->
<script>
    // Pass quizId, contextPath, and maxQuestionCount to JavaScript
    var quizId = '<%= quizId %>';
    var contextPath = '<%= request.getContextPath() %>';
    var maxQuestionCount = <%= maxQuestionCount %>;
</script>

<!-- External JavaScript file -->
<script src="${pageContext.request.contextPath}/js/quiz.js"></script>
</body>
</html>
