<%@ page import="java.util.List" %>
<%@ page import="Module.AnswersReader" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
  // Retrieve quizId and maxQuestionCount from request attributes
  String quizIdParam = (String) request.getAttribute("quizId");
  if (quizIdParam == null || quizIdParam.isEmpty()) {
    quizIdParam = "1"; // Default value
  }

  int maxQuestionCount = (Integer) request.getAttribute("maxQuestionCount");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Match Quiz</title>
  <!-- Include CSS and JS files -->
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/match-quiz.css">
  <script src="https://cdn.jsdelivr.net/npm/interactjs/dist/interact.min.js"></script>
  <!-- Pass variables to JavaScript -->
  <script>
    var quizId = '<%= quizIdParam %>';
    var contextPath = '<%= request.getContextPath() %>';
    var maxQuestionCount = <%= maxQuestionCount %>;
  </script>
  <style>
    /* Include your custom styles here */
    /* Modal styles, progress bar, quiz content, etc. */
    /* ... */
    .modal {
      display: block; /* Show the modal by default */
      position: fixed;
      z-index: 1;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      overflow: auto;
      background-color: rgba(0,0,0,0.4);
    }
    /* Rest of the styles */
    /* ... */
  </style>
</head>
<body>
<!-- Quiz Settings Modal -->
<div id="settings-modal" class="modal">
  <div class="modal-content">
    <span class="close-button" onclick="closeModal('settings-modal')">&times;</span>
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
      <option value="5">5 Questions</option>
      <option value="10">10 Questions</option>
      <option value="15">15 Questions</option>
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

    <br><br>
    <button onclick="initializeQuiz()">Start Quiz</button>
  </div>
</div>

<!-- Quiz Container -->
<div class="quiz-container" style="display: none;">
  <h1 id="quiz-title">Match the Items</h1>
  <!-- Progress Bar -->
  <div class="progress-container">
    <div class="progress-bar" id="progress-bar"></div>
  </div>
  <!-- Timer -->
  <div id="countdown-timer">Time left: <span id="time-left">15:00</span></div>
  <!-- Quiz Content -->
  <div class="quiz-content" style="display: flex; justify-content: space-between; margin-top: 20px;">
    <!-- Questions Column -->
    <div class="questions-column" style="width: 45%;">
      <h2>Questions</h2>
      <div id="questions" class="items-list" style="min-height: 200px; border: 1px solid #ccc; padding: 10px; border-radius: 5px;">
        <!-- Questions will be dynamically inserted here -->
      </div>
    </div>
    <!-- Options Column -->
    <div class="options-column" style="width: 45%;">
      <h2>Options</h2>
      <div id="options" class="items-list" style="min-height: 200px; border: 1px solid #ccc; padding: 10px; border-radius: 5px;">
        <!-- Options will be dynamically inserted here -->
      </div>
    </div>
  </div>
  <!-- Submit Button -->
  <div style="text-align: center; margin-top: 20px;">
    <button type="button" onclick="submitQuiz()">Submit</button>
  </div>
</div>

<!-- Result Modal -->
<div id="result-modal" class="modal" style="display: none;">
  <div class="modal-content">
    <span class="close-button" onclick="closeModal('result-modal')">&times;</span>
    <h2>Your Results</h2>
    <p id="result-text">You scored X out of Y.</p>
    <button onclick="resetQuiz()">Retry Quiz</button>
    <button onclick="exitQuiz()">Exit</button>
  </div>
</div>

<!-- External JavaScript file -->
<script src="${pageContext.request.contextPath}/js/match-quiz.js"></script>
</body>
</html>
