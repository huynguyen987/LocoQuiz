<!-- File: WebContent/jsp/edit-quiz.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, entity.quiz, entity.Tag, entity.Question" %>
<%
  Integer userId = (Integer) session.getAttribute("userId"); // Get userId from session
  if (userId == null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Edit Quiz - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quiz-creator.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="${pageContext.request.contextPath}/css/fonts.css" rel="stylesheet">
  <!-- Include Font Awesome for Icons -->
  <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</head>

<body>

<div class="container">
  <!-- Back to Home Button -->
  <a href="${pageContext.request.contextPath}/jsp/teacher.jsp" class="back-to-home-btn">Back to Home</a>

  <h1>Edit Quiz</h1>

  <!-- Display success or error messages -->
  <% String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");

    if (successMessage != null) { %>
  <div class="success-message"><%= successMessage %></div>
  <% } else if (errorMessage != null) { %>
  <div class="error-message"><%= errorMessage %></div>
  <% } %>

  <%
    quiz quizObj = (quiz) request.getAttribute("quiz");
    List<Tag> tagList = (List<Tag>) request.getAttribute("tagList");
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    String quizType = (String) request.getAttribute("quizType");

    if (quizObj == null) {
      out.println("<p>Quiz details not available.</p>");
      return;
    }

    // Get selected tag IDs
    List<Tag> quizTags = quizObj.getTag();
    List<Integer> quizTagIds = new ArrayList<>();
    if (quizTags != null) {
      for (Tag tag : quizTags) {
        quizTagIds.add(tag.getId());
      }
    }
  %>

  <form action="${pageContext.request.contextPath}/EditQuizServlet" method="post" id="quizForm" enctype="multipart/form-data">
    <!-- Hidden Quiz ID -->
    <input type="hidden" name="quizId" value="<%= quizObj.getId() %>">

    <!-- Quiz Name -->
    <label for="quizName">Quiz Name:</label>
    <input type="text" id="quizName" name="quizName" required aria-required="true" value="<%= quizObj.getName() %>">

    <!-- Description -->
    <label for="quizDescription">Description:</label>
    <textarea id="quizDescription" name="quizDescription" required aria-required="true"><%= quizObj.getDescription() %></textarea>

    <!-- Tags -->
    <label for="quizTag">Tags:</label>
    <div class="dropdown" id="quizTag">
      <button type="button" class="dropdown-toggle" id="dropdownMenuButton" aria-haspopup="true" aria-expanded="false">
        Select Tags
        <i class="fas fa-chevron-down"></i>
      </button>
      <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
        <% for (Tag tag : tagList) { %>
        <label class="dropdown-item">
          <input type="checkbox" name="quizTags" value="<%= tag.getId() %>" <%= quizTagIds.contains(tag.getId()) ? "checked" : "" %>> <%= tag.getName() %>
        </label>
        <% } %>
      </div>
    </div>
    <p class="tag-instruction">Select one or more tags for your quiz.</p>

    <!-- Quiz Type Selection -->
    <h2>Select Quiz Type</h2>
    <div class="quiz-type-selection" id="quizTypeSelection" style="display: none;">
      <label>
        <input type="radio" name="quizTypeRadio" value="multiple-choice" <%= "multiple-choice".equals(quizType) ? "checked" : "" %> required> Multiple Choice
      </label>
      <label>
        <input type="radio" name="quizTypeRadio" value="fill-in-the-blank" <%= "fill-in-the-blank".equals(quizType) ? "checked" : "" %>> Fill in the Blank
      </label>
      <label>
        <input type="radio" name="quizTypeRadio" value="matching" <%= "matching".equals(quizType) ? "checked" : "" %>> Matching
      </label>
    </div>

    <!-- File Upload -->
    <label for="quizFile">Import Quiz from File:</label>
    <input type="file" id="quizFile" name="quizFile" accept=".docx,.xlsx">

    <!-- Hidden input to store selected quiz type -->
    <input type="hidden" id="quizType" name="quizType" value="<%= quizType %>">

    <!-- Display Selected Quiz Type -->
    <div id="selectedQuizType">
      <h2>Quiz Type: <span id="chosenQuizType"><%= formatQuizType(quizType) %></span></h2>
    </div>

    <!-- Main Quiz Creator Container -->
    <div class="quiz-creator-container" id="quizCreatorContainer">
      <!-- Question Grid Sidebar -->
      <div class="question-grid" id="questionGrid">
        <h2>Questions</h2>
        <div id="questionGridContainer">
          <!-- Question buttons will be dynamically added here -->
          <% if (questions != null && !questions.isEmpty()) {
            int qNum = 1;
            for (Question question : questions) { %>
          <button type="button" class="question-btn <%= qNum == 1 ? "active" : "" %>" id="questionBtn<%= qNum %>" data-num="<%= qNum %>"><%= qNum %></button>
          <% qNum++;
          }
          } %>
        </div>
        <!-- Pagination Controls -->
        <div class="pagination-controls" id="paginationControls">
          <button type="button" id="prevPageBtn" disabled>&laquo; Prev</button>
          <span id="currentPage">1</span> / <span id="totalPages">1</span>
          <button type="button" id="nextPageBtn" disabled>Next &raquo;</button>
        </div>
        <button type="button" id="addQuestionBtn">Add Question</button>
      </div>

      <!-- Question Editor -->
      <div class="question-editor" id="questionEditor">
        <!-- Dynamic question sections will be added here -->
        <% if (questions != null && !questions.isEmpty()) {
          int qNum = 1;
          for (Question question : questions) { %>
        <div class="question-section <%= qNum == 1 ? "active" : "" %>" id="question<%= qNum %>">
          <h3>Question <%= qNum %> (<%= formatQuizType(quizType) %>)</h3>
          <!-- Remove Question Button -->
          <button type="button" class="remove-btn" data-num="<%= qNum %>">Remove Question</button>
          <!-- Question Content -->
          <label for="questionContent<%= qNum %>">Question:</label>
          <textarea id="questionContent<%= qNum %>" name="questionContent<%= qNum %>" required aria-required="true"><%= question.getQuestionText() %></textarea>

          <% if ("multiple-choice".equals(quizType)) { %>
          <!-- Answers -->
          <label>Answers:</label>
          <div id="answersContainer<%= qNum %>">
            <%
              List<String> options = question.getOptions();
              if (options != null) {
                int aNum = 1;
                for (String option : options) { %>
            <div class="answer-option">
              <input type="radio" id="correctAnswer<%= qNum %>_<%= aNum %>" name="correctAnswer<%= qNum %>" value="<%= aNum %>" <%= option.equals(question.getCorrectAnswer()) ? "checked" : "" %> required aria-required="true">
              <label for="answer<%= qNum %>_<%= aNum %>">Answer <%= aNum %>:</label>
              <input type="text" id="answer<%= qNum %>_<%= aNum %>" name="answer<%= qNum %>_<%= aNum %>" placeholder="Answer <%= aNum %>" required aria-required="true" value="<%= option %>">
            </div>
            <% aNum++;
            }
            } %>
          </div>
          <button type="button" class="add-answer-btn" data-question="<%= qNum %>">Add Answer</button>
          <% } else if ("fill-in-the-blank".equals(quizType)) { %>
          <!-- Correct Answer -->
          <label for="correctAnswer<%= qNum %>">Correct Answer:</label>
          <input type="text" id="correctAnswer<%= qNum %>" name="correctAnswer<%= qNum %>" required aria-required="true" value="<%= question.getCorrectAnswer() %>">
          <% } else if ("matching".equals(quizType)) { %>
          <!-- Matching Pairs -->
          <label for="answer<%= qNum %>_1">Matching Pairs:</label>
          <div id="answersContainer<%= qNum %>">
            <div class="answer-option">
              <input type="radio" id="correctAnswer<%= qNum %>_1" name="correctAnswer<%= qNum %>" value="1" checked style="display:none;">
              <input type="text" id="answer<%= qNum %>_1" name="answer<%= qNum %>_1" placeholder="Enter matching pairs (e.g., A-B; C-D; E-F)" required aria-required="true" value="<%= question.getCorrectAnswer() %>">
            </div>
          </div>
          <% } %>
        </div>
        <% qNum++;
        }
        } %>
      </div>
    </div>

    <!-- Hidden input to keep track of question count -->
    <input type="hidden" id="questionCount" name="questionCount" value="<%= questions != null ? questions.size() : 0 %>">

    <!-- Submit Button -->
    <input type="submit" value="Update Quiz">
  </form>

</div>

<!-- Include the external JavaScript file -->
<script src="${pageContext.request.contextPath}/js/quiz-details.js"></script>

</body>
</html>

<%!
  // Helper method to format quiz type
  public String formatQuizType(String type) {
    switch (type) {
      case "multiple-choice":
        return "Multiple Choice";
      case "fill-in-the-blank":
        return "Fill in the Blank";
      case "matching":
        return "Matching";
      default:
        return "";
    }
  }
%>
