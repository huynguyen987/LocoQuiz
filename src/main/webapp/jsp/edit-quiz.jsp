<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.google.gson.Gson, com.google.gson.reflect.TypeToken" %>
<%@ page import="entity.quiz" %>
<%@ page import="entity.Tag" %>
<%@ page import="java.lang.reflect.Type" %>
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
      // Lấy danh sách tag từ đối tượng quiz
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

    // Retrieve typeId
    int typeId = quizObj.getType_id();

    // Retrieve type name
    String typeName = formatQuizType(String.valueOf(typeId));

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
        <% if (allTags != null) { %>
        <% for (Map<String, Object> tag : allTags) { %>
        <label class="dropdown-item">
          <input type="checkbox" name="quizTags" value="<%= tag.get("id") %>" <%= quizTagIds.contains(tag.get("id")) ? "checked" : "" %> > <%= tag.get("name") %>
        </label>
        <% } %>
        <% } %>
      </div>
    </div>
    <p class="tag-instruction">Select one or more tags for your quiz.</p>

    <!-- Hidden input to store selected quiz type -->
    <input type="hidden" id="quizType" name="quizType" value="<%= typeId %>">

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
              String questionType = (String) question.get("type");
              if (questionType == null) {
                if (question.containsKey("options") && question.get("options") != null) {
                  questionType = "1"; // Multiple Choice
                } else if (question.containsKey("correct") && question.get("correct") != null) {
                  questionType = "2"; // Fill in the Blank
                } else if (question.containsKey("pairs") && question.get("pairs") != null) {
                  questionType = "3"; // Matching
                } else {
                  questionType = "0"; // Unknown
                }
              }
        %>
        <div class="question-section active" id="question<%= qNum %>" data-type="<%= questionType %>" data-answer-count="<%= ("1".equals(questionType) && question.get("options") != null) ? ((List<String>) question.get("options")).size() : 1 %>">
          <h3>Question <%= qNum %> (<%= formatQuizType(questionType) %>)</h3>
          <!-- Remove Question Button -->
            <button type="button" class="remove-question-btn" data-question="<%= qNum %>">Remove Question</button>
          <!-- Question Content -->
          <label for="questionContent<%= qNum %>">Question:</label>
          <textarea id="questionContent<%= qNum %>" name="questionContent<%= qNum %>" required aria-required="true"><%= question.get("question") %></textarea>

          <!-- Hidden field to store question type -->
          <input type="hidden" name="questionType<%= qNum %>" value="<%= questionType %>">

          <% if ("1".equals(questionType)) { %>
          <!-- Answers -->
          <label>Answers:</label>
          <div id="answersContainer<%= qNum %>">
            <%
              List<String> options = (List<String>) question.get("options");
              if (options != null) {
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
              }
            %>
          </div>
          <button type="button" class="add-answer-btn" data-question="<%= qNum %>">Add Answer</button>
          <input type="hidden" id="answerCount<%= qNum %>" name="answerCount<%= qNum %>" value="<%= options != null ? options.size() : 0 %>">
          <% } else if ("2".equals(questionType)) { %>
          <!-- Fill in the Blank Correct Answer -->
          <label for="correctAnswer<%= qNum %>">Correct Answer:</label>
          <input type="text" id="correctAnswer<%= qNum %>" name="correctAnswer<%= qNum %>" required aria-required="true" value="<%= question.get("correct") %>">
          <% } else if ("3".equals(questionType)) { %>
          <!-- Matching Question -->
          <!-- Bạn có thể thêm phần hiển thị câu hỏi matching ở đây -->
          <p>Matching question editing is not implemented yet.</p>
          <% } else { %>
          <!-- Unknown Question Type -->
          <p>Question type is unknown or unsupported.</p>
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
<script src="<%= request.getContextPath() %>/js/quiz-details.js"></script>

<script>
  // Initialize the selectedQuizType display
  document.addEventListener('DOMContentLoaded', function () {
    const quizType = '<%= typeId %>';
    const selectedQuizTypeDiv = document.getElementById('selectedQuizType');
    const chosenQuizTypeSpan = document.getElementById('chosenQuizType');
    if (quizType) {
      chosenQuizTypeSpan.textContent = (function(typeId) {
        switch(typeId) {
          case '1': return 'Multiple Choice';
          case '2': return 'Fill in the Blank';
          case '3': return 'Matching';
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
  // Helper method to format quiz type based on typeId
  public String formatQuizType(String typeIdStr) {
    if (typeIdStr == null) {
      return "Unknown";
    }
    int typeId;
    try {
      typeId = Integer.parseInt(typeIdStr);
    } catch (NumberFormatException e) {
      return "Unknown";
    }
    switch (typeId) {
      case 1:
        return "Multiple Choice";
      case 2:
        return "Fill in the Blank";
      case 3:
        return "Matching";
      default:
        return "Unknown";
    }
  }
%>
