<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.google.gson.Gson, com.google.gson.reflect.TypeToken" %>
<%@ page import="entity.quiz" %>
<%@ page import="entity.Tag" %>
<%@ page import="java.lang.reflect.Type" %>
<%@ page import="entity.type" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Edit Quiz - QuizLoco</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/quiz-creator.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="<%= request.getContextPath() %>/css/fonts.css" rel="stylesheet">
  <!-- Include Font Awesome for Icons -->
  <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</head>

<body>

<div class="container">
  <!-- Back to Home Button -->
  <a href="<%= request.getContextPath() %>/jsp/teacher.jsp" class="back-to-home-btn">Back to Home</a>

  <h1>Edit Quiz</h1>

  <!-- Display success or error messages -->
  <%
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");

    if (successMessage != null) {
  %>
  <div class="success-message"><%= successMessage %></div>
  <% } else if (errorMessage != null) { %>
  <div class="error-message"><%= errorMessage %></div>
  <% } %>


  <%
    // Retrieve quiz and tags from request attributes
    quiz quizObj = (quiz) request.getAttribute("quiz");
    List<Map<String, Object>> allTags = (List<Map<String, Object>>) request.getAttribute("allTags");
    List<Integer> quizTagIds = new ArrayList<>();
    if (quizObj != null) {
      // Giả sử bạn đã thêm phương thức getTag() trong lớp quiz để lấy danh sách tag
      List<Tag> quizTags = quizObj.getTag();
      if (quizTags != null) {
        for (Tag tag : quizTags) {
          quizTagIds.add(tag.getId());
        }
      }
    }

    if (quizObj == null) {
      out.println("<p>Quiz details not available.</p>");
      return;
    }

    // Retrieve type name
    String typeName = (String) request.getAttribute("typeName");
    if (typeName == null) {
      // If not passed, default to type_id
      int typeId = quizObj.getType_id();
      // Fetch type name based on typeId
      switch(typeId) {
        case 1:
          typeName = "Multiple Choice";
          break;
        case 2:
          typeName = "Fill in the Blank";
          break;
        case 3:
          typeName = "Matching";
          break;
        default:
          typeName = "Unknown";
      }
    }

    // Retrieve questions list
    List<Map<String, Object>> questions = (List<Map<String, Object>>) request.getAttribute("questions");
    if (questions == null) {
      // Parse the answer JSON
      String answerJson = quizObj.getAnswer();
      if (answerJson != null && !answerJson.trim().isEmpty()) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        questions = gson.fromJson(answerJson, listType);
      } else {
        questions = new ArrayList<>();
      }
    }
  %>

  <form action="<%= request.getContextPath() %>/EditQuizServlet" method="post" id="quizForm">
    <!-- Hidden Quiz ID -->
    <input type="hidden" name="quizId" value="<%= quizObj.getId() %>">

    <!-- Hidden User ID -->
    <input type="hidden" name="userId" value="<%= quizObj.getUser_id() %>">

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
        <% for (Map<String, Object> tag : allTags) { %>
        <label class="dropdown-item">
          <input type="checkbox" name="quizTags" value="<%= tag.get("id") %>" <%= quizTagIds.contains(tag.get("id")) ? "checked" : "" %> > <%= tag.get("name") %>
        </label>
        <% } %>
      </div>
    </div>
    <p class="tag-instruction">Select one or more tags for your quiz.</p>

    <!-- Quiz Type Selection -->
    <h2>Select Quiz Type</h2>
    <div class="quiz-type-selection" id="quizTypeSelection">
      <label>
        <input type="radio" name="quizTypeRadio" value="Multiple Choice" required <%= "Multiple Choice".equals(typeName) ? "checked" : "" %> > Multiple Choice
      </label>
      <label>
        <input type="radio" name="quizTypeRadio" value="Fill in the Blank" <%= "Fill in the Blank".equals(typeName) ? "checked" : "" %> > Fill in the Blank
      </label>
      <label>
        <input type="radio" name="quizTypeRadio" value="Matching" <%= "Matching".equals(typeName) ? "checked" : "" %> > Matching
      </label>
    </div>

    <!-- Hidden input to store selected quiz type -->
    <input type="hidden" id="quizType" name="quizType" value="<%= typeName %>">

    <!-- Start Quiz Editing Button -->
    <button type="button" id="startQuizBtn">Start Editing Quiz</button>

    <!-- Display Selected Quiz Type -->
    <div id="selectedQuizType" style="display: none;">
      <h2>Quiz Type: <span id="chosenQuizType"></span></h2>
    </div>

    <!-- Main Quiz Creator Container -->
    <div class="quiz-creator-container" id="quizCreatorContainer" style="display: none;">
      <!-- Question Grid Sidebar -->
      <div class="question-grid" id="questionGrid">
        <h2>Questions</h2>
        <div id="questionGridContainer">
          <!-- Question buttons sẽ được thêm động tại đây -->
          <%
            if (questions != null && !questions.isEmpty()) {
              int qNum = 1;
              for (Map<String, Object> question : questions) {
          %>
          <button type="button" class="question-btn <%= qNum == 1 ? "active" : "" %>" id="questionBtn<%= qNum %>" data-num="<%= qNum %>"><%= qNum %></button>
          <%
                qNum++;
              }
            }
          %>
        </div>
        <!-- Pagination Controls -->
        <div class="pagination-controls" id="paginationControls">
          <button type="button" id="prevPageBtn" <%= (questions == null || questions.size() <= 20) ? "disabled" : "" %> >&laquo; Prev</button>
          <span id="currentPage">1</span> / <span id="totalPages"><%= Math.ceil((questions != null ? questions.size() : 0)/20.0) %></span>
          <button type="button" id="nextPageBtn" <%= (questions == null || questions.size() <= 20) ? "disabled" : "" %> >Next &raquo;</button>
        </div>
        <button type="button" id="addQuestionBtn">Add Question</button>
      </div>

      <!-- Question Editor -->
      <div class="question-editor" id="questionEditor">
        <!-- Các phần câu hỏi sẽ được thêm động tại đây -->
        <%
          if (questions != null && !questions.isEmpty()) {
            int qNum = 1;
            for (Map<String, Object> question : questions) {
              String questionType = "multiple-choice"; // mặc định
              if (question.containsKey("options")) {
                questionType = "multiple-choice";
              } else if (question.containsKey("correctBool")) {
                questionType = "true-false";
              } else if (question.containsKey("correct")) {
                questionType = "short-answer";
              }
        %>
        <div class="question-section active" id="question<%= qNum %>" data-type="<%= questionType %>" data-answer-count="<%= ("multiple-choice".equals(questionType)) ? ((List<String>) question.get("options")).size() : 1 %>">
          <h3>Question <%= qNum %> (<%= formatQuizType(questionType) %>)</h3>
          <!-- Remove Question Button -->
          <button type="button" class="remove-btn" data-num="<%= qNum %>">Remove Question</button>
          <!-- Question Content -->
          <label for="questionContent<%= qNum %>">Question:</label>
          <textarea id="questionContent<%= qNum %>" name="questionContent<%= qNum %>" required aria-required="true"><%= question.get("question") %></textarea>

          <!-- Hidden field to store question type -->
          <input type="hidden" name="questionType<%= qNum %>" value="<%= questionType %>">

          <% if ("multiple-choice".equals(questionType)) { %>
          <!-- Answers -->
          <label>Answers:</label>
          <div id="answersContainer<%= qNum %>">
            <%
              List<String> options = (List<String>) question.get("options");
              String correct = (String) question.get("correct");
              int aNum = 1;
              for (String option : options) {
            %>
            <div class="answer-option">
              <input type="radio" id="correctAnswer<%= qNum %>_<%= aNum %>" name="correctAnswer<%= qNum %>" value="<%= aNum %>" <%= option.equals(correct) ? "checked" : "" %> required aria-required="true">
              <label for="answer<%= qNum %>_<%= aNum %>">Answer <%= aNum %>:</label>
              <input type="text" id="answer<%= qNum %>_<%= aNum %>" name="answer<%= qNum %>_<%= aNum %>" placeholder="Answer <%= aNum %>" required aria-required="true" value="<%= option %>">
            </div>
            <%
                aNum++;
              }
            %>
          </div>
          <button type="button" class="add-answer-btn" data-question="<%= qNum %>">Add Answer</button>
          <input type="hidden" id="answerCount<%= qNum %>" name="answerCount<%= qNum %>" value="<%= aNum - 1 %>">
          <% } else if ("true-false".equals(questionType)) { %>
          <!-- True/False Correct Answer -->
          <label for="correctAnswer<%= qNum %>">Correct Answer:</label>
          <select id="correctAnswer<%= qNum %>" name="correctAnswer<%= qNum %>" required aria-required="true">
            <option value="true" <%= Boolean.TRUE.equals(question.get("correctBool")) ? "selected" : "" %> >True</option>
            <option value="false" <%= Boolean.FALSE.equals(question.get("correctBool")) ? "selected" : "" %> >False</option>
          </select>
          <% } else if ("short-answer".equals(questionType)) { %>
          <!-- Short Answer Correct Answer -->
          <label for="correctAnswer<%= qNum %>">Correct Answer:</label>
          <input type="text" id="correctAnswer<%= qNum %>" name="correctAnswer<%= qNum %>" required aria-required="true" value="<%= question.get("correct") %>">
          <% } %>
        </div>
        <%
              qNum++;
            }
          }
        %>
      </div>
    </div>

    <!-- Hidden input to keep track of question count -->
    <input type="hidden" id="questionCount" name="questionCount" value="<%= (questions != null) ? questions.size() : 0 %>">

    <!-- Submit Button -->
    <input type="submit" value="Update Quiz">
  </form>

</div>

<!-- Include the external JavaScript file -->
<script src="<%= request.getContextPath() %>/js/quiz-creator.js"></script>

<script>
  // Initialize the selectedQuizType display
  document.addEventListener('DOMContentLoaded', function () {
    const quizType = '<%= typeName %>';
    const selectedQuizTypeDiv = document.getElementById('selectedQuizType');
    const chosenQuizTypeSpan = document.getElementById('chosenQuizType');
    if (quizType) {
      chosenQuizTypeSpan.textContent = (function(type) {
        switch(type) {
          case 'Multiple Choice': return 'Multiple Choice';
          case 'Fill in the Blank': return 'Fill in the Blank';
          case 'Matching': return 'Matching';
          default: return '';
        }
      })(quizType);
      selectedQuizTypeDiv.style.display = 'block';
      document.getElementById('quizCreatorContainer').style.display = 'flex';
    }

    // Nếu có file được tải lên, ẩn phần tạo quiz thủ công
    const quizFileInput = document.getElementById('quizFile');
    if (quizFileInput && quizFileInput.files.length > 0) {
      document.getElementById('quizCreatorContainer').style.display = 'none';
    }
  });
</script>

</body>
</html>

<%!
  // Helper method to format quiz type
  public String formatQuizType(String type) {
    switch (type) {
      case "multiple-choice":
      case "Multiple Choice":
        return "Multiple Choice";
      case "fill-in-the-blank":
      case "Fill in the Blank":
        return "Fill in the Blank";
      case "matching":
      case "Matching":
        return "Matching";
      default:
        return "";
    }
  }
%>

