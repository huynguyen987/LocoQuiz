<!-- File: src/main/webapp/jsp/quiz.jsp -->
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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Practice</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/quiz.css">
    <style>
        /* Basic styles for modal */
        .modal {
            display: none; /* Hidden by default */
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
            margin: 5% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 350px;
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
            display: none; /* Hide custom inputs by default */
        }
        .active {
            background-color: #4CAF50;
            color: white;
        }
        .answered {
            background-color: #2196F3;
            color: white;
        }
        /* Styles for number input with spinner */
        input[type=number]::-webkit-inner-spin-button,
        input[type=number]::-webkit-outer-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }
        input[type=number] {
            -moz-appearance: textfield;
        }
        .number-input-container {
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .number-input-container input[type=number] {
            width: 60%;
            text-align: center;
        }
        .number-input-container .spinner-button {
            width: 20%;
            padding: 10px;
            background-color: #ddd;
            border: none;
            cursor: pointer;
            font-size: 18px;
        }
        .number-input-container .spinner-button:active {
            background-color: #ccc;
        }
    </style>
</head>
<body>
<div class="quiz-container">
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

<!-- Time and Quiz Settings Modal -->
<div id="time-modal" class="modal">
    <div class="modal-content">
        <h2>Quiz Settings</h2>

        <!-- Time Selection -->
        <label for="time-select">Select Time for Quiz:</label>
        <select id="time-select" onchange="toggleCustomTimeInput(this.value)">
            <option value="900">15 Minutes</option>
            <option value="1200">20 Minutes</option>
            <option value="1500">25 Minutes</option>
            <option value="custom">Custom</option>
        </select>
        <div style="text-align: center;">
            <input type="number" id="custom-time" min="300" max="3600" step="60" placeholder="Enter time in seconds" style="text-align: center; display: none;">
        </div>

        <!-- Number of Questions Selection -->
        <label for="question-count-select">Select Number of Questions:</label>
        <select id="question-count-select" onchange="toggleCustomQuestionCountInput(this.value)">
            <option value="10">10 Questions</option>
            <option value="20">20 Questions</option>
            <option value="30">30 Questions</option>
            <option value="custom">Custom</option>
        </select>
        <div class="number-input-container" style="text-align: center; margin-top: 10px;">
            <button type="button" class="spinner-button" onclick="decrementQuestionCount()">&#8722;</button>
            <input type="number" id="custom-question-count" min="1" max="<%= maxQuestionCount %>" step="1" placeholder="Enter number of questions" style="text-align: center; display: none;">
            <button type="button" class="spinner-button" onclick="incrementQuestionCount()">&#43;</button>
        </div>

        <!-- Shuffle Questions Option -->
        <label for="shuffle-select">Shuffle Questions:</label>
        <select id="shuffle-select">
            <option value="true">Yes</option>
            <option value="false">No</option>
        </select>

        <br>
        <button onclick="startQuiz()">Start Quiz</button>
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
<script>
    // Functions to increment and decrement question count
    function incrementQuestionCount() {
        const input = document.getElementById('custom-question-count');
        let currentValue = parseInt(input.value, 10) || 0;
        if (currentValue < maxQuestionCount) {
            input.value = currentValue + 1;
        }
    }

    function decrementQuestionCount() {
        const input = document.getElementById('custom-question-count');
        let currentValue = parseInt(input.value, 10) || 0;
        if (currentValue > 1) {
            input.value = currentValue - 1;
        }
    }
</script>
</body>
</html>
