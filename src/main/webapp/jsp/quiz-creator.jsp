<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, entity.Tag" %>
<!DOCTYPE html>
<html>
<head>
  <title>Create a New Quiz - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quiz-creator.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="${pageContext.request.contextPath}/css/fonts.css" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<body>
<header>
  <div class="container">
    <h1><a href="${pageContext.request.contextPath}/index.jsp" class="logo">QuizLoco</a></h1>
    <nav>
      <ul>
        <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/index.jsp#about">About</a></li>
        <li><a href="${pageContext.request.contextPath}/index.jsp#services">Services</a></li>
        <li><a href="${pageContext.request.contextPath}/index.jsp#contact">Contact</a></li>
      </ul>
    </nav>
  </div>
</header>
<%
  // Retrieve the list of tags from the request attribute
  List<Tag> tagList = (List<Tag>) request.getAttribute("tagList");
  if (tagList == null) {
    tagList = new ArrayList<>();
  }
%>
<div class="container">
  <h1>Create a New Quiz</h1>
  <!-- Display error message if any -->
  <% String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) { %>
  <div class="error-message"><%= errorMessage %></div>
  <% } %>
  <form action="QuizController" method="post" id="quizForm">
    <!-- Quiz Name -->
    <label for="quizName">Quiz Name:</label>
    <input type="text" id="quizName" name="quizName" required>

    <!-- Description -->
    <label for="quizDescription">Description:</label>
    <textarea id="quizDescription" name="quizDescription" required></textarea>

    <!-- Tag -->
    <label for="quizTag">Tag:</label>
    <select id="quizTag" name="quizTag" required>
      <% for (Tag tag : tagList) { %>
      <option value="<%= tag.getId() %>"><%= tag.getName() %></option>
      <% } %>
    </select>

    <!-- Question List Table -->
    <h2>Questions</h2>
    <table id="questionTable">
      <thead>
      <tr>
        <th>Question Number</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody id="questionTableBody">
      <!-- Dynamic rows will be added here -->
      </tbody>
    </table>
    <button type="button" id="addQuestionBtn">Add Question</button>

    <!-- Dynamic Questions Container -->
    <div id="questionsContainer">
      <!-- Dynamic question sections will be added here -->
    </div>

    <!-- Hidden input to keep track of question count -->
    <input type="hidden" id="questionCount" name="questionCount" value="1">

    <!-- Submit Button -->
    <input type="submit" value="Create Quiz">
  </form>
</div>

<script>
  let questionCount = 0;

  function addQuestion() {
    questionCount++;
    $('#questionCount').val(questionCount);

    // Add row to the question table
    $('#questionTableBody').append(`
      <tr id="row${questionCount}">
        <td><a href="#question${questionCount}">Question ${questionCount}</a></td>
        <td><button type="button" onclick="removeQuestion(${questionCount})">Remove</button></td>
      </tr>
    `);

    // Add question section with numbered labels
    $('#questionsContainer').append(`
      <div class="question-section" id="question${questionCount}">
        <h3>Question ${questionCount}</h3>
        <!-- Question Content -->
        <label for="questionContent${questionCount}">Question ${questionCount}:</label>
        <textarea id="questionContent${questionCount}" name="questionContent${questionCount}" required></textarea>

        <!-- Answers -->
        <label>Answers:</label>
        <div class="answer-option">
          <input type="radio" name="correctAnswer${questionCount}" value="1" required>
          <label for="answer${questionCount}_1">Answer 1:</label>
          <input type="text" id="answer${questionCount}_1" name="answer${questionCount}_1" placeholder="Answer 1" required>
        </div>
        <div class="answer-option">
          <input type="radio" name="correctAnswer${questionCount}" value="2">
          <label for="answer${questionCount}_2">Answer 2:</label>
          <input type="text" id="answer${questionCount}_2" name="answer${questionCount}_2" placeholder="Answer 2" required>
        </div>
        <div class="answer-option">
          <input type="radio" name="correctAnswer${questionCount}" value="3">
          <label for="answer${questionCount}_3">Answer 3:</label>
          <input type="text" id="answer${questionCount}_3" name="answer${questionCount}_3" placeholder="Answer 3" required>
        </div>
        <div class="answer-option">
          <input type="radio" name="correctAnswer${questionCount}" value="4">
          <label for="answer${questionCount}_4">Answer 4:</label>
          <input type="text" id="answer${questionCount}_4" name="answer${questionCount}_4" placeholder="Answer 4" required>
        </div>
      </div>
    `);
  }

  function removeQuestion(num) {
    // Remove the table row and question section
    $(`#row${num}`).remove();
    $(`#question${num}`).remove();

    // Decrease questionCount and update the hidden field
    questionCount--;
    $('#questionCount').val(questionCount);
  }

  $(document).ready(function() {
    // Initialize with one question
    addQuestion();

    // Add question button click
    $('#addQuestionBtn').click(function() {
      addQuestion();
    });
  });
</script>
</body>
</html>
