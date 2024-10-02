<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Drag and Drop Match Type Quiz</title>
  <!-- Link to the enhanced CSS file -->
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/match-quiz.css">
  <!-- Include Interact.js via CDN -->
  <script src="https://cdn.jsdelivr.net/npm/interactjs/dist/interact.min.js"></script>
</head>
<body>
<div class="quiz-container">
  <h1>Match the Countries with Their Capitals</h1>
  <div class="progress-container">
    <div class="progress-bar" id="progress-bar"></div>
  </div>
  <div class="quiz-content">
    <div class="draggable-items">
      <h2>Countries</h2>
      <div id="countries" class="items-list">
        <div class="draggable" data-id="1" draggable="true" aria-grabbed="false" role="option" tabindex="0">Canada</div>
        <div class="draggable" data-id="2" draggable="true" aria-grabbed="false" role="option" tabindex="0">Germany</div>
        <div class="draggable" data-id="3" draggable="true" aria-grabbed="false" role="option" tabindex="0">Japan</div>
        <div class="draggable" data-id="4" draggable="true" aria-grabbed="false" role="option" tabindex="0">Australia</div>
        <div class="draggable" data-id="5" draggable="true" aria-grabbed="false" role="option" tabindex="0">Brazil</div>
      </div>
    </div>
    <div class="dropzones">
      <h2>Capitals</h2>
      <div id="capitals" class="items-list">
        <div class="dropzone" data-id="1" aria-dropeffect="move" role="option" tabindex="0">Ottawa</div>
        <div class="dropzone" data-id="2" aria-dropeffect="move" role="option" tabindex="0">Berlin</div>
        <div class="dropzone" data-id="3" aria-dropeffect="move" role="option" tabindex="0">Tokyo</div>
        <div class="dropzone" data-id="4" aria-dropeffect="move" role="option" tabindex="0">Canberra</div>
        <div class="dropzone" data-id="5" aria-dropeffect="move" role="option" tabindex="0">Brasília</div>
      </div>
    </div>
  </div>
  <button type="button" onclick="checkAnswers()">Submit</button>
</div>

<!-- Result Modal -->
<div id="result-modal" class="modal">
  <div class="modal-content">
    <span class="close-button" onclick="closeModal()">&times;</span>
    <h2>Your Results</h2>
    <p id="result-text">You scored X out of Y.</p>
    <button onclick="resetQuiz()">Retry Quiz</button>
  </div>
</div>

<!-- Link to the enhanced JavaScript file -->
<script src="${pageContext.request.contextPath}/js/match-quiz.js"></script>
</body>
</html>
