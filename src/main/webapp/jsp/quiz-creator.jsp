<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, entity.Tag" %>
<%@ page import="dao.TagDAO" %>
<%
    Integer userId = (Integer) session.getAttribute("userId"); // Lấy userId từ session
    if (userId == null) {
        // Nếu userId không có trong session (người dùng chưa đăng nhập), chuyển hướng về trang đăng nhập
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create a New Quiz - QuizLoco</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quiz-creator.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/css/fonts.css" rel="stylesheet">
    <!-- Include Font Awesome for Icons -->
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</head>

<body>

<%
    // Retrieve the list of tags from the database
    List<Tag> tagList = new TagDAO().getAllTags();
%>

<div class="container">
    <!-- Back to Home Button -->
    <a href="${pageContext.request.contextPath}/jsp/teacher.jsp" class="back-to-home-btn">Back to Home</a>

    <h1>Create a New Quiz</h1>

    <!-- Display success or error messages -->
    <% String successMessage = (String) request.getAttribute("successMessage");
        String errorMessage = (String) request.getAttribute("errorMessage");

        if (successMessage != null) { %>
    <div class="success-message"><%= successMessage %></div>
    <% } else if (errorMessage != null) { %>
    <div class="error-message"><%= errorMessage %></div>
    <% } %>


    <form action="${pageContext.request.contextPath}/QuizController" method="post" id="quizForm">
        <!-- Quiz Name -->
        <label for="quizName">Quiz Name:</label>
        <input type="text" id="quizName" name="quizName" required aria-required="true">

        <!-- Description -->
        <label for="quizDescription">Description:</label>
        <textarea id="quizDescription" name="quizDescription" required aria-required="true"></textarea>

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
                    <input type="checkbox" name="quizTags" value="<%= tag.getId() %>"> <%= tag.getName() %>
                </label>
                <% } %>
            </div>
        </div>
        <p class="tag-instruction">Select one or more tags for your quiz.</p>

        <!-- Quiz Type Selection -->
        <h2>Select Quiz Type</h2>
        <div class="quiz-type-selection" id="quizTypeSelection">
            <label>
                <input type="radio" name="quizTypeRadio" value="multiple-choice" required> Multiple Choice
            </label>
            <label>
                <input type="radio" name="quizTypeRadio" value="fill-in-the-blank"> Fill in the Blank
            </label>
            <label>
                <input type="radio" name="quizTypeRadio" value="matching"> Matching
            </label>
        </div>

        <!-- Hidden input to store selected quiz type -->
        <input type="hidden" id="quizType" name="quizType" value="">

        <!-- Start Quiz Creation Button -->
        <button type="button" id="startQuizBtn">Start Creating Quiz</button>

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
                    <!-- Question buttons will be dynamically added here -->
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
            </div>
        </div>

        <!-- Hidden input to keep track of question count -->
        <input type="hidden" id="questionCount" name="questionCount" value="0">

        <input type="hidden" name="userId" value="<%= userId %>">

        <!-- Submit Button -->
        <input type="submit" value="Create Quiz">
    </form>



</div>

<!-- Include the external JavaScript file -->
<script src="${pageContext.request.contextPath}/js/quiz-creator.js"></script>

</body>
</html>