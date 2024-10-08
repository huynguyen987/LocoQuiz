<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, entity.Tag" %>
<%@ page import="dao.TagDAO" %>
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
    <h1>Create a New Quiz</h1>

    <!-- Display error message if any -->
    <% String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) { %>
    <div class="error-message"><%= errorMessage %></div>
    <% } %>

    <form action="QuizController" method="post" id="quizForm">
        <!-- Quiz Name -->
        <label for="quizName">Quiz Name:</label>
        <input type="text" id="quizName" name="quizName" required aria-required="true">

        <!-- Description -->
        <label for="quizDescription">Description:</label>
        <textarea id="quizDescription" name="quizDescription" required aria-required="true"></textarea>

        <!-- Tags -->
        <label for="quizTag">Tags:</label>
        <div class="dropdown" id="tagDropdown">
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
        <div class="quiz-type-menu" role="radiogroup" aria-label="Quiz Type">
            <button type="button" class="quiz-type-btn active" data-type="multiple-choice" aria-pressed="true">Multiple Choice</button>
            <button type="button" class="quiz-type-btn" data-type="fill-in-the-blank" aria-pressed="false">Fill in the Blank</button>
            <button type="button" class="quiz-type-btn" data-type="matching" aria-pressed="false">Matching</button>
        </div>

        <!-- Hidden input to store selected quiz type -->
        <input type="hidden" id="quizType" name="quizType" value="multiple-choice">

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
        <input type="hidden" id="questionCount" name="questionCount" value="0">

        <!-- Submit Button -->
        <input type="submit" value="Create Quiz">
    </form>
</div>

<!-- Include the external JavaScript file -->
<script src="${pageContext.request.contextPath}/js/quiz-creator.js"></script>

</body>
</html>
