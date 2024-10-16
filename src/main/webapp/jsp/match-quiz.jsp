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
  <!-- External CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/match-quiz.css">
  <!-- Include Interact.js for drag-and-drop functionality -->
  <script src="https://cdn.jsdelivr.net/npm/interactjs/dist/interact.min.js"></script>

  <!-- Pass variables to JavaScript -->
  <script>
    const quizId = '<%= quizIdParam %>';
    const contextPath = '<%= request.getContextPath() %>';
    const maxQuestionCount = <%= maxQuestionCount %>;
  </script>
</head>
<body>
<div class="container">
  <!-- Quiz Settings Modal -->
  <div id="settings-modal" class="modal" aria-hidden="false" role="dialog" aria-labelledby="settings-modal-title">
    <div class="modal-content">
      <span class="close-button" onclick="closeModal('settings-modal')" aria-label="Close Settings Modal">&times;</span>
      <h2 id="settings-modal-title">Quiz Settings</h2>

      <!-- Time Selection -->
      <label for="time-select">Select Time for Quiz:</label>
      <select id="time-select" onchange="toggleCustomTimeInput(this.value)">
        <option value="900">15 Minutes</option>
        <option value="1200">20 Minutes</option>
        <option value="1500">25 Minutes</option>
        <option value="custom">Custom</option>
      </select>
      <div class="custom-input" style="display: none;">
        <input type="number" id="custom-time" min="300" max="3600" step="60" placeholder="Enter time in seconds">
      </div>

      <!-- Number of Questions Selection -->
      <label for="question-count-select">Select Number of Questions:</label>
      <select id="question-count-select" onchange="toggleCustomQuestionCountInput(this.value)">
        <option value="5">5 Questions</option>
        <option value="10">10 Questions</option>
        <option value="15">15 Questions</option>
        <option value="custom">Custom</option>
      </select>
      <div class="number-input-container" style="display: none;">
        <button type="button" class="spinner-button" onclick="decrementQuestionCount()">&#8722;</button>
        <input type="number" id="custom-question-count" min="1" max="<%= maxQuestionCount %>" step="1" placeholder="Enter number of questions">
        <button type="button" class="spinner-button" onclick="incrementQuestionCount()">&#43;</button>
      </div>

      <!-- Shuffle Questions Option -->
      <label for="shuffle-select">Shuffle Questions:</label>
      <select id="shuffle-select">
        <option value="true">Yes</option>
        <option value="false">No</option>
      </select>

      <button class="start-btn" onclick="initializeQuiz()">Start Quiz</button>
    </div>
  </div>

  <!-- Quiz Container -->
  <div class="quiz-container" id="quiz-container" aria-hidden="true">
    <h1 id="quiz-title">Match the Items</h1>
    <!-- Progress and Timer -->
    <div class="progress-timer">
      <div class="progress-container" aria-label="Quiz Progress">
        <div class="progress-bar" id="progress-bar"></div>
      </div>
      <div id="countdown-timer" aria-live="polite">Time left: <span id="time-left">15:00</span></div>
    </div>
    <!-- Quiz Content -->
    <div class="quiz-content">
      <!-- Questions Column -->
      <div class="questions-column">
        <h2>Questions</h2>
        <div id="questions" class="items-list">
          <!-- Questions will be dynamically inserted here -->
        </div>
      </div>
      <!-- Options Column -->
      <div class="options-column">
        <h2>Options</h2>
        <div id="options" class="items-list">
          <!-- Options will be dynamically inserted here -->
        </div>
      </div>
    </div>
    <!-- Submit Button -->
    <button type="button" class="submit-btn" onclick="submitQuiz()">Submit</button>
  </div>

  <!-- Result Modal -->
  <div id="result-modal" class="modal" aria-hidden="true" role="dialog" aria-labelledby="result-modal-title">
    <div class="modal-content">
      <span class="close-button" onclick="closeModal('result-modal')" aria-label="Close Result Modal">&times;</span>
      <h2 id="result-modal-title">Your Results</h2>
      <p id="result-text">You scored X out of Y.</p>
      <p id="time-total">Time taken: 0 minutes 0 seconds.</p>
      <div id="view-answers">
        <!-- Detailed answers will be populated here -->
      </div>
      <div class="button-container">
        <button onclick="resetQuiz()">Retry Quiz</button>
        <button onclick="exitQuiz()">Exit</button>
      </div>
    </div>
  </div>
</div>

<!-- External JavaScript file -->
<script src="${pageContext.request.contextPath}/js/match-quiz.js"></script>
</body>
</html>
